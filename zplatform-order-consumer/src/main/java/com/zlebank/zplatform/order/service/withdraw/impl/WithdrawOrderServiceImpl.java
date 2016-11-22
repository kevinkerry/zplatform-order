/* 
 * WithdrawOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.withdraw.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.fee.bean.FeeBean;
import com.zlebank.zplatform.fee.exception.TradeFeeException;
import com.zlebank.zplatform.fee.service.TradeFeeService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiProductService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.individual.service.MemberBankCardService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.TxnsWithdrawDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsWithdraw;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.exception.WithdrawOrderException;
import com.zlebank.zplatform.order.sequence.SerialNumberService;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.service.withdraw.AbstractWithdrawOrderService;
import com.zlebank.zplatform.order.utils.Constant;
import com.zlebank.zplatform.order.withdraw.bean.WithdrawAccBean;
import com.zlebank.zplatform.order.withdraw.bean.WithdrawOrderBean;
import com.zlebank.zplatform.risk.bean.RiskBean;
import com.zlebank.zplatform.risk.exception.TradeRiskException;
import com.zlebank.zplatform.risk.service.TradeRiskControlService;
import com.zlebank.zplatform.trade.acc.bean.ResultBean;
import com.zlebank.zplatform.trade.acc.service.WithdrawAccountingService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午4:45:00
 * @since
 */
@Service("withdrawOrderService")
public class WithdrawOrderServiceImpl extends AbstractWithdrawOrderService
		implements OrderService {

	@Autowired
	private MemberBankCardService memberBankCardService;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private MerchService merchService;
	@Autowired
	private CoopInstiProductService coopInstiProductService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private TxnsWithdrawDAO txnsWithdrawDAO;
	@Autowired
	private WithdrawAccountingService withdrawAccountingService;
	@Autowired
	private TradeRiskControlService tradeRiskControlService;
	@Autowired
	private TradeFeeService tradeFeeService;
	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO;

	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws OrderException
	 */
	@Override
	public String create(BaseOrderBean baseOrderBean) throws OrderException {
		WithdrawOrderBean orderBean = null;
		if (baseOrderBean instanceof WithdrawOrderBean) {
			orderBean = (WithdrawOrderBean) baseOrderBean;
		} else {
			throw new OrderException("OD049", "无效订单");
		}
		WithdrawAccBean accBean = null;
		if (StringUtil.isNotEmpty(orderBean.getBindId())) {// 使用已绑定的卡进行提现
			QuickpayCustBean custCard = memberBankCardService
					.getMemberBankCardById(Long.valueOf(orderBean.getBindId()));
			if (custCard == null) {
				throw new OrderException("OD039");
			}
			accBean = new WithdrawAccBean(custCard);
		} else {
			accBean = JSON.parseObject(orderBean.getCardData(),
					WithdrawAccBean.class);
		}
		if (accBean == null) {
			throw new OrderException("OD040");
		}
		orderBean.setAccBean(accBean);
		String tn = checkOfSecondPay(orderBean);
		if (StringUtils.isNotEmpty(tn)) {
			return tn;
		}
		checkOfAll(orderBean);
		try {
			return saveWithdrawOrder(orderBean);
		} catch (TradeRiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OrderException("OD049", "被风控系统拒绝");
		}
	}

	/**
	 *
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfAll(BaseOrderBean baseOrderBean) throws OrderException {
		// TODO Auto-generated method stub
		WithdrawOrderBean orderBean = null;
		if (baseOrderBean instanceof WithdrawOrderBean) {
			orderBean = (WithdrawOrderBean) baseOrderBean;
		} else {
			throw new OrderException("OD049", "无效订单");
		}
		checkOfRepeatSubmit(orderBean);
		checkOfBusiness(orderBean);
		checkOfMerchantAndCoopInsti(orderBean);
		checkOfSpecialBusiness(orderBean);
		checkOfBusiAcct(orderBean);
		checkOfRepeatSubmit(orderBean);
	}

	/**
	 *
	 * @param baseOrderBean
	 * @return
	 * @throws OrderException
	 * @throws TradeRiskException 
	 */
	@Override
	public String saveWithdrawOrder(BaseOrderBean baseOrderBean)
			throws OrderException, TradeRiskException {
		// TODO Auto-generated method stub
		// 记录订单信息
		WithdrawOrderBean orderBean = null;
		if (baseOrderBean instanceof WithdrawOrderBean) {
			orderBean = (WithdrawOrderBean) baseOrderBean;
		} else {
			throw new OrderException("OD049", "无效订单");
		}
		PojoTxnsOrderinfo orderinfo = null;
		PojoTxnsLog txnsLog = null;

		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(
				orderBean.getTxnType(), orderBean.getTxnSubType(),
				orderBean.getBizType());
		// member = memberService.get(orderBean.getCoopInstiId());
		txnsLog = new PojoTxnsLog();
		if (StringUtil.isNotEmpty(orderBean.getMerId())) {// 商户为空时，取商户的各个版本信息
			MerchantBean member = merchService.getMerchBymemberId(orderBean
					.getMerId());
			txnsLog.setRiskver(member.getRiskVer());
			txnsLog.setSplitver(member.getSpiltVer());
			txnsLog.setFeever(member.getFeeVer());
			txnsLog.setPrdtver(member.getPrdtVer());
			txnsLog.setRoutver(member.getRoutVer());
			txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer
					.valueOf(member.getSetlCycle().toString())));
		} else {
			txnsLog.setRiskver(coopInstiProductService.getDefaultVerInfo(
					orderBean.getCoopInstiId(), busiModel.getBusicode(), 13));
			txnsLog.setSplitver(coopInstiProductService.getDefaultVerInfo(
					orderBean.getCoopInstiId(), busiModel.getBusicode(), 12));
			txnsLog.setFeever(coopInstiProductService.getDefaultVerInfo(
					orderBean.getCoopInstiId(), busiModel.getBusicode(), 11));
			txnsLog.setPrdtver(coopInstiProductService.getDefaultVerInfo(
					orderBean.getCoopInstiId(), busiModel.getBusicode(), 10));
			txnsLog.setRoutver(coopInstiProductService.getDefaultVerInfo(
					orderBean.getCoopInstiId(), busiModel.getBusicode(), 20));
			txnsLog.setAccsettledate(DateUtil.getSettleDate(1));
		}

		txnsLog.setTxndate(DateUtil.getCurrentDate());
		txnsLog.setTxntime(DateUtil.getCurrentTime());
		txnsLog.setBusicode(busiModel.getBusicode());
		txnsLog.setBusitype(busiModel.getBusitype());
		txnsLog.setTxnseqno(serialNumberService.generateTxnseqno());
		txnsLog.setAmount(Long.valueOf(orderBean.getAmount()));
		txnsLog.setAccordno(orderBean.getOrderId());
		txnsLog.setAccfirmerno(orderBean.getCoopInstiId());
		// 提现订单不记录商户号，记录在订单表中
		if ("3000".equals(txnsLog.getBusitype())) {
			txnsLog.setAccsecmerno("");
		} else {
			txnsLog.setAccsecmerno(orderBean.getMerId());
		}
		txnsLog.setAcccoopinstino(Constant.getInstance()
				.getZlebank_coopinsti_code());
		txnsLog.setAccordcommitime(orderBean.getTxnTime());
		txnsLog.setTradestatflag("00000000");// 交易初始状态
		txnsLog.setAccmemberid(orderBean.getMemberId());
		txnsLog.setPan(orderBean.getAccBean().getAccNo());
		txnsLog.setPanName(orderBean.getAccBean().getAccName());
		txnsLog.setCardtype("1");
		txnsLog.setCardinstino(orderBean.getAccBean().getBankCode());
		// txnsLog.setTxnfee(txnsLogService.getTxnFee(txnsLog));
		long fee = 0;
		try {
			FeeBean feeBean = new FeeBean();
			feeBean.setBusiCode(txnsLog.getBusicode());
			feeBean.setFeeVer(txnsLog.getFeever());
			feeBean.setTxnAmt(txnsLog.getAmount() + "");
			feeBean.setMerchNo(txnsLog.getAccsecmerno());
			feeBean.setCardType("1");
			feeBean.setTxnseqnoOg("");
			feeBean.setTxnseqno(txnsLog.getTxnseqno());
			fee = tradeFeeService.getCommonFee(feeBean);
		} catch (TradeFeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OrderException("OD043");
		}
		txnsLog.setTxnfee(fee);
		txnsLog.setTradcomm(0L);
		txnsLogDAO.saveTxnsLog(txnsLog);

		orderinfo = new PojoTxnsOrderinfo();
		orderinfo.setId(Long.valueOf(RandomUtils.nextInt()));
		orderinfo.setOrderno(orderBean.getOrderId());// 商户提交的订单号
		orderinfo.setOrderamt(Long.valueOf(orderBean.getAmount()));
		orderinfo.setOrderfee(txnsLog.getTxnfee());
		orderinfo.setOrdercommitime(orderBean.getTxnTime());
		orderinfo.setRelatetradetxn(txnsLog.getTxnseqno());// 关联的交易流水表中的交易序列号
		orderinfo.setFirmemberno(orderBean.getCoopInstiId());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(
				orderBean.getCoopInstiId()).getInstiName());

		// orderinfo.setBackurl(orderBean.getBackUrl());
		orderinfo.setTxntype(orderBean.getTxnType());
		orderinfo.setTxnsubtype(orderBean.getTxnSubType());
		orderinfo.setBiztype(orderBean.getBizType());
		orderinfo.setAccesstype(orderBean.getAccessType());
		orderinfo.setTn(serialNumberService.generateTN(orderBean
				.getMemberId()));
		orderinfo.setMemberid(orderBean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setStatus("02");
		txnsOrderinfoDAO.saveOrderInfo(orderinfo);

		PojoTxnsWithdraw withdraw = new PojoTxnsWithdraw(orderBean, orderBean.getAccBean());
		withdraw.setWithdraworderno(serialNumberService.generateWithdrawNo());
		withdraw.setTexnseqno(txnsLog.getTxnseqno());
		withdraw.setFee(txnsLog.getTxnfee());
		txnsWithdrawDAO.saveTxnsWithdraw(withdraw);
		// 风控
		RiskBean riskBean = new RiskBean();
		riskBean.setBusiCode(txnsLog.getBusicode());
		riskBean.setCardNo( orderBean.getAccBean().getAccNo());
		riskBean.setCardType("1");
		riskBean.setCoopInstId(txnsLog.getAccfirmerno());
		riskBean.setMemberId(txnsLog.getAccmemberid());
		riskBean.setMerchId(txnsLog.getAccsecmerno());
		riskBean.setTxnAmt(txnsLog.getAmount() + "");
		riskBean.setTxnseqno(txnsLog.getTxnseqno());
		tradeRiskControlService.realTimeTradeRiskControl(riskBean);
		ResultBean resultBean = withdrawAccountingService.withdrawApply(txnsLog
				.getTxnseqno());

		if (resultBean.isResultBool()) {
			return orderinfo.getTn();
		} else {
			throw new OrderException("OD044");
		}

	}

}
