/* 
 * InsteadPayOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.utils.BeanCopyUtil;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiProductService;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.service.MemberAccountService;
import com.zlebank.zplatform.member.individual.service.MemberService;
import com.zlebank.zplatform.member.merchant.bean.EnterpriseBean;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.EnterpriseService;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.bean.OrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.consumer.enums.TradeStatFlagEnum;
import com.zlebank.zplatform.order.dao.InsteadPayRealtimeDAO;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoInsteadPayRealtime;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.enums.AccountTypeEnum;
import com.zlebank.zplatform.order.enums.CertifTypeEnmu;
import com.zlebank.zplatform.order.enums.CurrencyEnum;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.exception.InsteadPayOrderException;
import com.zlebank.zplatform.order.sequence.SerialNumberService;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.order.service.InsteadPayOrderService;
import com.zlebank.zplatform.order.utils.Constant;
import com.zlebank.zplatform.order.utils.ValidateLocator;
import com.zlebank.zplatform.trade.acc.service.InsteadPayAccountingService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 上午9:25:11
 * @since 
 */
@Service("insteadPayOrderService")
public class InsteadPayOrderServiceImpl implements InsteadPayOrderService {

	private static final Logger log = LoggerFactory.getLogger(InsteadPayOrderServiceImpl.class);
	@Autowired
	private CommonOrderService commonOrderService;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private CoopInstiService coopInstiService ;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberAccountService memberAccountService;
	
	@Autowired
	private InsteadPayRealtimeDAO insteadPayRealtimeDAO;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private MerchService merchService;
	@Autowired
	private CoopInstiProductService coopInstiProductService;
	@Autowired
	private EnterpriseService enterpriseService;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private InsteadPayAccountingService insteadPayAccountingService;
	
	/**
	 *
	 * @param insteadPayOrderBean
	 * @return
	 * @throws InsteadPayOrderException 
	 * @throws CommonException 
	 */
	@Override
	public ResultBean createRealTimeOrder(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException, CommonException {
		
		/**
		 * 实时代付订单校验流程
		 * 1.代付订单非空和长短校验
		 * 2.校验订单是否为二次代付，校验准则：商户号，订单号，收款账户，收款账号，订单时间,金额必须完全一致
		 * 3.
		 * 4.
		 */
		ResultBean resultBean = null;
		String tn = commonOrderService.verifySecondInsteadPay(insteadPayOrderBean);
		if(StringUtils.isNotEmpty(tn)){
			resultBean = new ResultBean(tn);
			return resultBean;
		}
		checkOrderInfo(insteadPayOrderBean);
		
		
		String txnseqno = serialNumberService.generateTxnseqno();
		String TN = serialNumberService.generateTN(insteadPayOrderBean.getMerId());
		PojoInsteadPayRealtime insteadPayRealtime = generateInsteadPayOrder(insteadPayOrderBean);
		insteadPayRealtime.setTxnseqno(txnseqno);
		insteadPayRealtime.setTn(TN);
		PojoTxnsLog txnsLog = generateTxnsLog(insteadPayOrderBean);
		txnsLog.setTxnseqno(txnseqno);
		insteadPayRealtimeDAO.saveInsteadTrade(insteadPayRealtime);
		txnsLogDAO.saveTxnsLog(txnsLog);
		com.zlebank.zplatform.trade.acc.bean.ResultBean paymentAccountingResult = insteadPayAccountingService.advancePaymentAccounting(txnseqno);
		if(!paymentAccountingResult.isResultBool()){
			throw new InsteadPayOrderException();
		}
		return new ResultBean(TN);
	}
	
	private PojoInsteadPayRealtime generateInsteadPayOrder(InsteadPayOrderBean insteadPayOrderBean){
		PojoInsteadPayRealtime insteadBean = new PojoInsteadPayRealtime();
		 //授理订单
		
		 insteadBean.setOrderno(insteadPayOrderBean.getOrderId());
		 insteadBean.setOrderCommiTime(insteadPayOrderBean.getTxnTime());
		 insteadBean.setOrderDesc("");
		 //付款人信息
		 insteadBean.setPayAccNo(insteadPayOrderBean.getAccNo());
		 insteadBean.setPayAccName(insteadPayOrderBean.getAccName());
		 insteadBean.setPayBankType(insteadPayOrderBean.getBankCode());
		 insteadBean.setPayBankName("");
		 //收款人信息
		 insteadBean.setAccType(insteadPayOrderBean.getAccType());
		 insteadBean.setAccName(insteadPayOrderBean.getAccName());
		 insteadBean.setAccNo(insteadPayOrderBean.getAccNo());
		 insteadBean.setCertifyType(insteadPayOrderBean.getCertifTp());
		 insteadBean.setCertifyNo(insteadPayOrderBean.getCertifId());
		 insteadBean.setMobile(insteadPayOrderBean.getPhoneNo());
		 insteadBean.setBankType(insteadPayOrderBean.getBankCode());
		 insteadBean.setBankName(insteadPayOrderBean.getIssInsName());
		 insteadBean.setProvince(insteadPayOrderBean.getIssInsProvince());
		 insteadBean.setCity(insteadPayOrderBean.getIssInsCity());
		 insteadBean.setTransAmt(Long.valueOf(insteadPayOrderBean.getTxnAmt()));
		 insteadBean.setCurrencyCode(insteadPayOrderBean.getCurrencyCode());
		 insteadBean.setRemark(insteadPayOrderBean.getNotes());
		 //其它
		 insteadBean.setTxnType(insteadPayOrderBean.getTxnType());
		 insteadBean.setTxnSubType(insteadPayOrderBean.getTxnSubType());
		 insteadBean.setBizType(insteadPayOrderBean.getBizType());
		 insteadBean.setBackUrl(insteadPayOrderBean.getBackUrl());
		 insteadBean.setFrontUrl("");
		 //insteadBean.setTxnFee(txnsLog.getTxnfee());
		 //insteadBean.setTxnseqno(txnsLog.getTxnseqno());
		 //商户
		 insteadBean.setMerId(insteadPayOrderBean.getMerId());
		 insteadBean.setCoopInstCode(insteadPayOrderBean.getCoopInstiId());
		 insteadBean.setCreateTime(new Date());
		 String enterpriseName="";
		if(StringUtil.isNotEmpty(insteadPayOrderBean.getMerId())){
			 EnterpriseBean enter= enterpriseService.getEnterpriseByMemberId(insteadPayOrderBean.getMerId());
			 enterpriseName=enter.getEnterpriseName();
		}
		 insteadBean.setMerName(enterpriseName);
		 insteadBean.setMerNameAbbr(null);
		 //合作机构
		 insteadBean.setCoopInstCode(insteadPayOrderBean.getCoopInstiId());
		 insteadBean.setStatus("01");
		 //状态
		 return insteadBean;
	}
	
	private PojoTxnsLog generateTxnsLog(InsteadPayOrderBean insteadPayOrderBean){
		MerchantBean member = merchService.getMerchBymemberId(insteadPayOrderBean.getMerId());
		PojoTxnsLog txnsLog = new PojoTxnsLog();
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(
				insteadPayOrderBean.getTxnType(), insteadPayOrderBean.getTxnSubType(),
				insteadPayOrderBean.getBizType());
		if(member!=null){
			txnsLog.setRiskver(member.getRiskVer());
			txnsLog.setSplitver(member.getSpiltVer());
			txnsLog.setFeever(member.getFeeVer());
			txnsLog.setPrdtver(member.getPrdtVer());
			txnsLog.setRoutver(member.getRoutVer());
			txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer
					.valueOf(member.getSetlCycle().toString())));
		}else{
			// 10-产品版本,11-扣率版本,12-分润版本,13-风控版本,20-路由版本
			txnsLog.setRiskver(coopInstiProductService.getDefaultVerInfo(
					insteadPayOrderBean.getCoopInstiId(), busiModel.getBusicode(), 13));
			txnsLog.setSplitver(coopInstiProductService.getDefaultVerInfo(
					insteadPayOrderBean.getCoopInstiId(), busiModel.getBusicode(), 12));
			txnsLog.setFeever(coopInstiProductService.getDefaultVerInfo(
					insteadPayOrderBean.getCoopInstiId(), busiModel.getBusicode(), 11));
			txnsLog.setPrdtver(coopInstiProductService.getDefaultVerInfo(
					insteadPayOrderBean.getCoopInstiId(), busiModel.getBusicode(), 10));
			txnsLog.setRoutver(coopInstiProductService.getDefaultVerInfo(
					insteadPayOrderBean.getCoopInstiId(), busiModel.getBusicode(), 20));
			txnsLog.setAccsettledate(DateUtil.getSettleDate(1));
		}
		
		txnsLog.setAccsettledate(DateUtil.getSettleDate(1));
		txnsLog.setTxndate(DateUtil.getCurrentDate());
		txnsLog.setTxntime(DateUtil.getCurrentTime());
		txnsLog.setBusicode(busiModel.getBusicode());
		//代付
		txnsLog.setBusitype(busiModel.getBusitype());
		// 核心交易流水号，交易时间（yymmdd）+业务代码+6位流水号（每日从0开始）
		
		txnsLog.setAmount(Long.valueOf(insteadPayOrderBean.getTxnAmt()));
		txnsLog.setAccordno(insteadPayOrderBean.getOrderId());
		txnsLog.setAccfirmerno(insteadPayOrderBean.getCoopInstiId());
		txnsLog.setAcccoopinstino(Constant.getInstance().getZlebank_coopinsti_code());
		txnsLog.setAccsecmerno(insteadPayOrderBean.getMerId());
		txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
		txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
		txnsLog.setAccmemberid("999999999999999");
		//收款人户名
		txnsLog.setPanName(insteadPayOrderBean.getAccName());
		//收款人账号
		txnsLog.setPan(insteadPayOrderBean.getAccNo());
		//收款人账户联行号
		txnsLog.setCardinstino(insteadPayOrderBean.getBankCode_DB());
		//卡类型
		txnsLog.setCardtype(insteadPayOrderBean.getCardType());
		txnsLog.setTradcomm(0L);
		return txnsLog;
		
		
	}
	
	
	
	private ResultBean checkOrderInfo(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException, CommonException{
		ResultBean resultBean = validateInsteadPayOrder(insteadPayOrderBean);
		if(!resultBean.isResultBool()){
			return resultBean;
		}
		commonOrderService.checkInsteadPayTime();
		commonOrderService.validateBusiness(BeanCopyUtil.copyBean(OrderBean.class, insteadPayOrderBean));
		commonOrderService.verifyMerchantAndCoopInsti(insteadPayOrderBean.getMerId(), insteadPayOrderBean.getCoopInstiId());
		commonOrderService.checkBusiAcctOfInsteadPay(insteadPayOrderBean.getMerId(),insteadPayOrderBean.getTxnAmt());
		//校验账号
        if(insteadPayOrderBean.getAccType().equals(AccountTypeEnum.PRIVATE.getCode())){
        	CardBin cardBin = commonOrderService.checkInsteadPayCard(insteadPayOrderBean.getAccNo());
        	insteadPayOrderBean.setCardType(cardBin.getType());
        	insteadPayOrderBean.setBankCode_DB(cardBin.getBankCode());
        }
		return new ResultBean("success");
	}
	
	
	private ResultBean validateInsteadPayOrder(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException{
		ResultBean resultBean = ValidateLocator.validateBeans(insteadPayOrderBean);
		CurrencyEnum  rmb = CurrencyEnum.fromValue(insteadPayOrderBean.getCurrencyCode());
        if(rmb==null || rmb.equals(CurrencyEnum.UNKNOW)){
       	 	throw  new InsteadPayOrderException("OD025");//代付货币类型错误
        }
        AccountTypeEnum  accType = AccountTypeEnum.fromValue(insteadPayOrderBean.getAccType());
        if(accType==null || accType.equals(AccountTypeEnum.UNKNOW)){
        	throw  new InsteadPayOrderException("SE02");//代付账户类型错误
        }
        CertifTypeEnmu certype = CertifTypeEnmu.fromValue(insteadPayOrderBean.getCertifTp());
        if(certype==null || certype.equals(AccountTypeEnum.UNKNOW)){
        	throw  new InsteadPayOrderException("OD027");//代付证件类型错误
        }
		return resultBean;
	}

}
