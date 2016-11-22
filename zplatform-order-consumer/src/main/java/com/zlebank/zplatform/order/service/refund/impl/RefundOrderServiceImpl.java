/* 
 * RefundOrderService.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.refund.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.fee.bean.FeeBean;
import com.zlebank.zplatform.fee.exception.TradeFeeException;
import com.zlebank.zplatform.fee.service.TradeFeeService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiProductService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.TxnsRefundDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsRefund;
import com.zlebank.zplatform.order.enums.BusinessEnum;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.refund.bean.RefundOrderBean;
import com.zlebank.zplatform.order.sequence.SerialNumberService;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.service.refund.AbstractRefundOrderService;
import com.zlebank.zplatform.order.utils.Constant;
import com.zlebank.zplatform.order.utils.DateUtil;
import com.zlebank.zplatform.risk.bean.RiskBean;
import com.zlebank.zplatform.risk.exception.TradeRiskException;
import com.zlebank.zplatform.risk.service.TradeRiskControlService;
import com.zlebank.zplatform.trade.acc.service.RefundAccountingService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午4:06:18
 * @since 
 */
@Service("refundOrderService")
public class RefundOrderServiceImpl extends AbstractRefundOrderService implements OrderService{
	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private MerchService merchService;
	@Autowired
	private CoopInstiProductService coopInstiProductService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private TxnsRefundDAO txnsRefundDAO;
	@Autowired
	private RefundAccountingService refundAccountingService;
	@Autowired
	private TradeRiskControlService tradeRiskControlService;
	@Autowired
	private TradeFeeService tradeFeeService;
	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws OrderException
	 */
	@Override
	public String create(BaseOrderBean baseOrderBean) throws OrderException {
		RefundOrderBean orderBean = null;
		if(baseOrderBean instanceof RefundOrderBean){
			orderBean = (RefundOrderBean)baseOrderBean;
		}else{
			throw new OrderException("OD049","无效订单");
		}
		if (StringUtils.isEmpty(orderBean.getOrigOrderId())&& StringUtils.isEmpty(orderBean.getOrigTN())) {
			throw new OrderException("OD028");
		}
		String tn = checkOfSecondPay(orderBean);
		if(StringUtils.isNotEmpty(tn)){
			return tn;
		}
		checkOfAll(orderBean);
		try {
			return saveRefundOrder(orderBean);
		} catch (TradeRiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OrderException("OD049","被风控系统拒绝");
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
		RefundOrderBean orderBean = null;
		if(baseOrderBean instanceof RefundOrderBean){
			orderBean = (RefundOrderBean)baseOrderBean;
		}else{
			throw new OrderException("OD049","无效订单");
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
	public String saveRefundOrder(BaseOrderBean baseOrderBean)
			throws OrderException, TradeRiskException {
		RefundOrderBean orderBean = null;
		if(baseOrderBean instanceof RefundOrderBean){
			orderBean = (RefundOrderBean)baseOrderBean;
		}else{
			throw new OrderException("OD049","无效订单");
		}
		MerchantBean member = null;
		PojoTxnsLog txnsLog = null;
		PojoTxnsOrderinfo old_orderInfo = null;
		PojoTxnsLog old_txnsLog = null;
		old_orderInfo = txnsOrderinfoDAO.getOrderinfoByTN(orderBean.getOrigTN());
		old_txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(old_orderInfo.getRelatetradetxn());
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(),orderBean.getBizType());
		txnsLog = new PojoTxnsLog();
		if (StringUtil.isNotEmpty(orderBean.getMerId())) {// 商户为空时，取商户的各个版本信息
			member = merchService.getMerchBymemberId(orderBean.getMerId());
			txnsLog.setRiskver(member.getRiskVer());
			txnsLog.setSplitver(member.getSpiltVer());
			txnsLog.setFeever(member.getFeeVer());
			txnsLog.setPrdtver(member.getPrdtVer());
			txnsLog.setRoutver(member.getRoutVer());
			txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer.valueOf(member.getSetlCycle().toString())));
		} else {
			txnsLog.setRiskver(coopInstiProductService.getDefaultVerInfo(orderBean.getCoopInstiId(), busiModel.getBusicode(), 13));
			txnsLog.setSplitver(coopInstiProductService.getDefaultVerInfo(orderBean.getCoopInstiId(), busiModel.getBusicode(), 12));
			txnsLog.setFeever(coopInstiProductService.getDefaultVerInfo(orderBean.getCoopInstiId(), busiModel.getBusicode(), 11));
			txnsLog.setPrdtver(coopInstiProductService.getDefaultVerInfo(orderBean.getCoopInstiId(), busiModel.getBusicode(), 10));
			txnsLog.setRoutver(coopInstiProductService.getDefaultVerInfo(orderBean.getCoopInstiId(), busiModel.getBusicode(), 20));
			txnsLog.setAccsettledate(DateUtil.getSettleDate(1));
		}

		txnsLog.setTxndate(DateUtil.getCurrentDate());
		txnsLog.setTxntime(DateUtil.getCurrentTime());
		txnsLog.setBusicode(busiModel.getBusicode());
		txnsLog.setBusitype(busiModel.getBusitype());
		// 核心交易流水号，交易时间（yymmdd）+业务代码+6位流水号（每日从0开始）
		txnsLog.setTxnseqno(serialNumberService.generateTxnseqno());
		txnsLog.setAmount(Long.valueOf(orderBean.getTxnAmt()));
		txnsLog.setAccordno(orderBean.getOrderId());
		txnsLog.setAccfirmerno(orderBean.getCoopInstiId());
		txnsLog.setAccsecmerno(orderBean.getMerId());
		txnsLog.setAcccoopinstino(Constant.getInstance().getZlebank_coopinsti_code());
		txnsLog.setTxnseqnoOg(old_txnsLog.getTxnseqno());
		txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
		txnsLog.setTradestatflag("00000000");// 交易初始状态
		txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer.valueOf(member.getSetlCycle().toString())));
		txnsLog.setAccmemberid(orderBean.getMemberId());

		// 匿名判断
		if (old_txnsLog.getPayinst().equals("99999999")) {
			txnsLog.setBusicode(BusinessEnum.REFUND_ACCOUNT.getBusiCode());
		} else {
			txnsLog.setBusicode(BusinessEnum.REFUND_BANK.getBusiCode());
		}
		txnsLog.setTradcomm(0L);
		
		
		long fee = 0;
		try {
			FeeBean feeBean = new FeeBean();
			feeBean.setBusiCode(BusinessEnum.REFUND_BANK.getBusiCode());
			feeBean.setFeeVer(txnsLog.getFeever());
			feeBean.setTxnAmt(txnsLog.getAmount()+"");
			feeBean.setMerchNo(txnsLog.getAccsecmerno());
			feeBean.setCardType("");
			feeBean.setTxnseqnoOg(txnsLog.getTxnseqnoOg());
			feeBean.setTxnseqno(txnsLog.getTxnseqno());
			fee = tradeFeeService.getRefundFee(feeBean);
		} catch (TradeFeeException e) {
			e.printStackTrace();
			throw new OrderException("OD042");
		}
		txnsLog.setTxnfee(fee);
		txnsLogDAO.saveTxnsLog(txnsLog);
		com.zlebank.zplatform.trade.acc.bean.ResultBean resultBean = refundAccountingService.refundApply(txnsLog.getTxnseqno());
		if (!resultBean.isResultBool()) {
			throw new OrderException("OD041");
		}

		String tn = "";
		PojoTxnsOrderinfo orderinfo = null;

		// 保存订单信息
		orderinfo = new PojoTxnsOrderinfo();
		orderinfo.setId(Long.valueOf(RandomUtils.nextInt()));
		// orderinfo.setInstitution(member.getMerchinsti());
		orderinfo.setOrderno(orderBean.getOrderId());// 商户提交的订单号
		orderinfo.setOrderamt(Long.valueOf(orderBean.getTxnAmt()));
		orderinfo.setOrdercommitime(orderBean.getTxnTime());
		orderinfo.setRelatetradetxn(txnsLog.getTxnseqno());// 关联的交易流水表中的交易序列号
		orderinfo.setFirmemberno(orderBean.getCoopInstiId());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(
				orderBean.getCoopInstiId()).getInstiName());
		orderinfo.setSecmemberno(orderBean.getMerId());
		orderinfo.setSecmembername(member == null ? "" : member.getAccName());
		orderinfo.setBackurl("");
		orderinfo.setTxntype(orderBean.getTxnType());
		orderinfo.setTxnsubtype(orderBean.getTxnSubType());
		orderinfo.setBiztype(orderBean.getBizType());
		orderinfo.setOrderdesc(orderBean.getOrderDesc());
		orderinfo.setTn(serialNumberService.generateTN(orderBean.getMerId()));
		orderinfo.setStatus("02");
		orderinfo.setMemberid(orderBean.getMemberId());
		orderinfo.setCurrencycode("156");
		RiskBean riskBean = new RiskBean();
		riskBean.setBusiCode(txnsLog.getBusicode());
		riskBean.setCardNo("");
		riskBean.setCardType("1");
		riskBean.setCoopInstId(txnsLog.getAccfirmerno());
		riskBean.setMemberId(txnsLog.getAccmemberid());
		riskBean.setMerchId(txnsLog.getAccsecmerno());
		riskBean.setTxnAmt(txnsLog.getAmount()+"");
		riskBean.setTxnseqno(txnsLog.getTxnseqno());
		tradeRiskControlService.realTimeTradeRiskControl(riskBean);
		txnsOrderinfoDAO.saveOrderInfo(orderinfo);
		tn = orderinfo.getTn();

		// 无异常时保存退款交易流水表，以便于以后退款审核操作
		PojoTxnsRefund refundOrder = new PojoTxnsRefund();
		refundOrder.setRefundorderno(serialNumberService.generateRefundNo());
		refundOrder.setOldorderno(orderBean.getOrigOrderId());
		refundOrder.setOldtxnseqno(old_txnsLog.getTxnseqno());
		refundOrder.setMerchno(orderBean.getCoopInstiId());
		refundOrder.setSubmerchno(orderBean.getMerId());
		refundOrder.setMemberid(orderBean.getMemberId());
		refundOrder.setAmount(Long.valueOf(orderBean.getTxnAmt()));
		refundOrder.setOldamount(old_orderInfo.getOrderamt());
		refundOrder.setRefundtype(orderBean.getRefundType());
		refundOrder.setRefunddesc(orderBean.getOrderDesc());
		refundOrder.setReltxnseqno(txnsLog.getTxnseqno());
		refundOrder.setTxntime(DateUtil.getCurrentDateTime());
		refundOrder.setStatus("01");
		refundOrder.setRelorderno(orderBean.getOrderId());
		txnsRefundDAO.saveRefundOrder(refundOrder);
		return tn;
	}

}
