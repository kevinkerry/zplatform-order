/* 
 * RechargeOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.recharge.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.member.coopinsti.service.CoopInstiProductService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.bean.MemberBean;
import com.zlebank.zplatform.member.individual.bean.enums.MemberType;
import com.zlebank.zplatform.member.individual.service.MemberInfoService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.bean.OrderInfoBean;
import com.zlebank.zplatform.order.consumer.enums.TradeStatFlagEnum;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.recharge.bean.RechargeOrderBean;
import com.zlebank.zplatform.order.sequence.SerialNumberService;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.service.recharge.AbstractRechargeOrderService;
import com.zlebank.zplatform.order.utils.DateUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午3:16:14
 * @since 
 */
@Service("rechargeOrderService")
public class RechargeOrderServiceImpl extends AbstractRechargeOrderService implements OrderService{
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private CommonOrderService commonOrderService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MerchService merchService;
	@Autowired
	private MemberInfoService memberInfoService;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private CoopInstiProductService coopInstiProductService;
	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws OrderException
	 */
	@Override
	public String create(BaseOrderBean baseOrderBean) throws OrderException {
		RechargeOrderBean orderBean = null;
		if(baseOrderBean instanceof RechargeOrderBean){
			orderBean = (RechargeOrderBean)baseOrderBean;
		}else{
			throw new OrderException("OD049","无效订单");
		}
		String tn = checkOfSecondPay(orderBean);
		if(StringUtils.isNotEmpty(tn)){
			return tn;
		}
		checkOfAll(orderBean);
		return saveRechargeOrder(orderBean);
	}

	/**
	 *
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfAll(BaseOrderBean baseOrderBean) throws OrderException {
		// TODO Auto-generated method stub
		RechargeOrderBean orderBean = null;
		if(baseOrderBean instanceof RechargeOrderBean){
			orderBean = (RechargeOrderBean)baseOrderBean;
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
	 */
	@Override
	public String saveRechargeOrder(BaseOrderBean baseOrderBean)
			throws OrderException {
		RechargeOrderBean orderBean = null;
		if(baseOrderBean instanceof RechargeOrderBean){
			orderBean = (RechargeOrderBean)baseOrderBean;
		}else{
			throw new OrderException("OD049","无效订单");
		}
		
		String txnseqno = serialNumberService.generateTxnseqno();
		String TN = serialNumberService.generateTN(orderBean.getMerId());
		OrderInfoBean orderInfoBean = generateOrderInfoBean(orderBean);
		orderInfoBean.setTn(TN);
		orderInfoBean.setRelatetradetxn(txnseqno);
		commonOrderService.saveOrderInfo(orderInfoBean);
		// 保存交易流水
		PojoTxnsLog txnsLog = generateTxnsLog(orderBean);
		txnsLog.setTxnseqno(txnseqno);
		commonOrderService.saveTxnsLog(txnsLog);
		return orderInfoBean.getTn();
	}
	private OrderInfoBean generateOrderInfoBean(RechargeOrderBean orderBean) {
		OrderInfoBean orderinfo = new OrderInfoBean();
		orderinfo.setId(-1L);
		orderinfo.setOrderno(orderBean.getOrderId());// 商户提交的订单号
		orderinfo.setOrderamt(Long.valueOf(orderBean.getTxnAmt()));
		orderinfo.setOrderfee(0L);
		orderinfo.setOrdercommitime(orderBean.getTxnTime());
		orderinfo.setFirmemberno(orderBean.getCoopInstiId());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(
				orderBean.getCoopInstiId()).getInstiName());
		MerchantBean merchant = merchService.getParentMerch(orderBean
				.getMerId());
		orderinfo.setSecmemberno(orderBean.getMerId());
		orderinfo
				.setSecmembername(StringUtils.isNotEmpty(orderBean.getMerName()) ? orderBean
						.getMerName() : merchant.getAccName());
		orderinfo.setSecmembershortname(orderBean.getMerAbbr());
		orderinfo.setPayerip(orderBean.getCustomerIp());
		orderinfo.setAccesstype(orderBean.getAccessType());
		// 商品信息
		/*orderinfo.setGoodsname(orderBean.getGoodsname());
		orderinfo.setGoodstype(orderBean.getGoodstype());
		orderinfo.setGoodsnum(orderBean.getGoodsnum());
		orderinfo.setGoodsprice(orderBean.getGoodsprice());*/
		orderinfo.setFronturl(orderBean.getFrontUrl());
		orderinfo.setBackurl(orderBean.getBackUrl());
		orderinfo.setTxntype(orderBean.getTxnType());
		orderinfo.setTxnsubtype(orderBean.getTxnSubType());
		orderinfo.setBiztype(orderBean.getBizType());
		orderinfo.setOrderdesc(orderBean.getOrderDesc());
		orderinfo.setReqreserved(orderBean.getReqReserved());
		orderinfo.setReserved(orderBean.getReserved());
		orderinfo.setPaytimeout(orderBean.getPayTimeout());
		orderinfo.setMemberid(orderBean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setStatus("01");
		return orderinfo;
	}

	private PojoTxnsLog generateTxnsLog(RechargeOrderBean orderBean) {
		PojoTxnsLog txnsLog = new PojoTxnsLog();
		MerchantBean member = null;
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(
				orderBean.getTxnType(), orderBean.getTxnSubType(),
				orderBean.getBizType());
		if (StringUtils.isNotEmpty(orderBean.getMerId())) {// 商户为空时，取商户的各个版本信息
			member = merchService.getMerchBymemberId(orderBean.getMerId());

			txnsLog.setRiskver(member.getRiskVer());
			txnsLog.setSplitver(member.getSpiltVer());
			txnsLog.setFeever(member.getFeeVer());
			txnsLog.setPrdtver(member.getPrdtVer());
			txnsLog.setRoutver(member.getRoutVer());
			txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer
					.valueOf(member.getSetlCycle().toString())));
		} else {
			// 10-产品版本,11-扣率版本,12-分润版本,13-风控版本,20-路由版本
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
		txnsLog.setTradcomm(0L);
		txnsLog.setAmount(Long.valueOf(orderBean.getTxnAmt()));
		txnsLog.setAccordno(orderBean.getOrderId());
		txnsLog.setAccfirmerno(orderBean.getCoopInstiId());
		txnsLog.setAcccoopinstino(orderBean.getCoopInstiId());
		// 个人充值和提现不记录商户号，保留在订单表中
		if ("2000".equals(busiModel.getBusitype())
				|| "3000".equals(busiModel.getBusitype())) {
			txnsLog.setAccsecmerno("");
		} else {
			txnsLog.setAccsecmerno(orderBean.getMerId());
		}

		txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
		txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
		// txnsLog.setTradcomm(GateWayTradeAnalyzer.generateCommAmt(order.getReserved()));
		if (StringUtils.isNotEmpty(orderBean.getMemberId())) {
			if ("999999999999999".equals(orderBean.getMemberId())) {
				txnsLog.setAccmemberid("999999999999999");// 匿名会员号
			} else {
				MemberBean memberOfPerson = memberInfoService.getMemberByMemberId(
						orderBean.getMemberId(), MemberType.INDIVIDUAL);
				if (memberOfPerson != null) {
					txnsLog.setAccmemberid(orderBean.getMemberId());
				} else {
					txnsLog.setAccmemberid("999999999999999");// 匿名会员号
				}
			}
		}
		txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());
		return txnsLog;
	}
}
