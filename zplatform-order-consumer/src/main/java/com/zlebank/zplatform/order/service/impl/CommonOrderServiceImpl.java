/* 
 * CommonOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年9月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.enums.AcctStatusType;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.utils.BeanCopyUtil;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.FinanceProductAccountBean;
import com.zlebank.zplatform.member.bean.FinanceProductQueryBean;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.pojo.PojoMerchDeta;
import com.zlebank.zplatform.order.common.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.bean.OrderInfoBean;
import com.zlebank.zplatform.order.common.bean.ResultBean;
import com.zlebank.zplatform.order.common.dao.InsteadPayRealtimeDAO;
import com.zlebank.zplatform.order.common.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.common.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.common.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.common.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoInsteadPayRealtime;
import com.zlebank.zplatform.order.common.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.common.enums.AccountTypeEnum;
import com.zlebank.zplatform.order.common.enums.ExceptionTypeEnum;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.common.exception.InsteadPayOrderException;
import com.zlebank.zplatform.order.common.utils.Constant;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IFinanceProductAccountService;
import com.zlebank.zplatform.rmi.member.IMemberAccountService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.member.IMerchService;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月9日 下午4:13:25
 * @since 
 */
@Service("commonOrderService")
public class CommonOrderServiceImpl implements CommonOrderService{

	private static final Logger logger = LoggerFactory.getLogger(CommonOrderServiceImpl.class);
	
	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO; 
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	@Autowired
	private IMerchService merchService;
	@Autowired
	private IMemberService memberService;
	@Autowired
	private ICoopInstiService coopInstiService;
	@Autowired
	private IMemberAccountService memberAccountService;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private IFinanceProductAccountService financeProductAccountService;
	@Autowired
	private InsteadPayRealtimeDAO insteadPayRealtimeDAO;
	
	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws CommonException
	 */
	@Override
	public String verifySecondPay(OrderBean orderBean) throws CommonException {
		PojoTxnsOrderinfo orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if(orderinfo==null){
			return null;
		}
		if(orderinfo.getOrderamt().longValue()!=Long.valueOf(orderBean.getTxnAmt()).longValue()){
			logger.info("订单金额:{};数据库订单金额:{}", orderBean.getTxnAmt(),orderinfo.getOrderamt());
			throw new CommonException("OD015", "二次支付订单交易金额错误");
		}
		
		if(!orderinfo.getOrdercommitime().equals(orderBean.getTxnTime())){
			logger.info("订单时间:{};数据库订单时间:{}", orderBean.getTxnTime(),orderinfo.getOrdercommitime());
			throw new CommonException("OD016", "二次支付订单提交时间错误");
		}
		return orderinfo.getTn();
	}

	/**
	 *
	 * @param orderNo
	 * @param txntime
	 * @param amount
	 * @param merchId
	 * @param memberId
	 * @throws CommonException
	 */
	@Override
	public void verifyRepeatOrder(OrderBean orderBean)throws CommonException {
		OrderInfoBean orderInfo = getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new CommonException("OD001","订单交易成功，请不要重复支付");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new CommonException("OD002","订单正在支付中，请不要重复支付");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new CommonException("OD003","订单失效");
			}
		}
		
	}

	/**
	 *
	 * @param orderBean
	 * @throws CommonException
	 */
	@Override
	public void verifyBusiness(OrderBean orderBean) throws CommonException {
		// TODO Auto-generated method stub
        PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        
        if(busiTypeEnum==BusiTypeEnum.consumption){//消费
        	BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
        	if(StringUtil.isEmpty(orderBean.getMerId())){
        		 throw new CommonException("OD004", "商户号为空");
        	}
        	PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());//memberService.getMemberByMemberId(order.getMerId());.java
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new CommonException("OD005", "商户未开通此业务");
            }
            if(BusinessEnum.CONSUMEQUICK_PRODUCT==businessEnum){//产品消费业务
            	FinanceProductQueryBean financeProductQueryBean = new FinanceProductQueryBean();
            	financeProductQueryBean.setProductCode(orderBean.getProductcode());
            	try {
					FinanceProductAccountBean productAccountBean = financeProductAccountService.queryBalance(financeProductQueryBean, Usage.BASICPAY);
					if(AcctStatusType.NORMAL!=AcctStatusType.fromValue(productAccountBean.getStatus())){
						throw new CommonException("OD006", "产品异常，请联系客服");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new CommonException("OD007", "产品不存在");
				}
            }
        }else if(busiTypeEnum==BusiTypeEnum.charge){//充值
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("OD008", "会员不存在无法进行充值");
			}
        }else if(busiTypeEnum==BusiTypeEnum.withdrawal){//提现
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("OD008", "会员不存在无法进行充值");
			}
        }else if(busiTypeEnum==BusiTypeEnum.insteadPay){
        	BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
        	if(StringUtil.isEmpty(orderBean.getMerId())){
        		 throw new CommonException("OD004", "商户号为空");
        	}
        	PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new CommonException("OD005", "商户未开通此业务");
            }
            
        }
        
        
	}

	/**
	 *
	 * @param merchant
	 * @param coopInsti
	 * @throws CommonException
	 */
	@Override
	public void verifyMerchantAndCoopInsti(String merchant, String coopInsti)
			throws CommonException {
		// TODO Auto-generated method stub
		// 检验一级商户和二级商户有效性
        if (StringUtil.isNotEmpty(merchant)) {
            PojoMerchDeta subMember = merchService.getMerchBymemberId(merchant);
            if (subMember == null) {
            	throw new CommonException("OD009", "商户不存在");
            }
            PojoMember pojoMember = memberService.getMbmberByMemberId(merchant, null);
            //校验商户会员信息 
            if (pojoMember.getMemberType()==MemberType.ENTERPRISE) {// 对于企业会员需要进行检查
            	CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiID(pojoMember.getInstiId());
                if (!coopInsti.equals(pojoCoopInsti.getInstiCode())) {
                	throw new CommonException("OD010", "商户所属合作机构错误");
                }
            }

        }
	}

	/**
	 *
	 * @param orderBean
	 * @throws CommonException
	 */
	@Override
	public void validateBusiness(OrderBean orderBean) throws CommonException {
		// TODO Auto-generated method stub
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(),orderBean.getBizType());
		
		
		if ("2000".equals(busiModel.getBusitype())) {
			if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("OD011", "会员不存在无法进行充值");
			}
		}
	}

	/**
	 *
	 * @param merchant
	 * @param memberId
	 * @throws CommonException
	 */
	@Override
	public void checkBusiAcct(String merchant, String memberId)
			throws CommonException {
		if (!"999999999999999".equals(memberId)&&StringUtil.isNotEmpty(memberId)) {
			MemberBean member = new MemberBean();
			member.setMemberId(memberId);
			MemberAccountBean memberAccountBean = null;
			
				try {
					memberAccountBean = memberAccountService.queryBalance(MemberType.INDIVIDUAL, member, Usage.BASICPAY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage());
					throw new CommonException("GW19", "账户查询异常:"+e.getMessage());
				}
			
			if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus())== AcctStatusType.STOP_OUT) {
				//throw new TradeException("GW19");
				throw new CommonException("GW19", "会员账户状态异常");
			}
		}
		if(StringUtil.isEmpty(merchant)){
			return ;
		}
		MemberBean member = new MemberBean();
		member.setMemberId(memberId);
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.ENTERPRISE, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException("GW19", "账户查询异常:"+e.getMessage());
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_IN) {
			//throw new TradeException("GW05");
			throw new CommonException("GW05", "商户账户状态异常");
		}
		
	}

	/**
	 *
	 * @param orderNo
	 * @param merchNo
	 * @return
	 */
	@Override
	public OrderInfoBean getOrderinfoByOrderNoAndMerchNo(String orderNo,String merchNo) {
		// TODO Auto-generated method stub
		PojoTxnsOrderinfo orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderNo, merchNo);
		if(orderinfo==null){
			return null;
		}
		OrderInfoBean orderInfoBean = BeanCopyUtil.copyBean(OrderInfoBean.class, orderinfo);
		return orderInfoBean;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveOrderInfo(OrderInfoBean orderInfoBean){
		PojoTxnsOrderinfo pojoTxnsOrderinfo = BeanCopyUtil.copyBean(PojoTxnsOrderinfo.class, orderInfoBean);
		txnsOrderinfoDAO.saveOrderInfo(pojoTxnsOrderinfo);
	}

	/**
	 *
	 * @param txnsLog
	 */
	@Override
	public void saveTxnsLog(PojoTxnsLog txnsLog) {
		// TODO Auto-generated method stub
		txnsLogDAO.saveTxnsLog(txnsLog);
	}

	/**
	 *
	 * @param insteadPayOrderBean
	 * @return
	 * @throws InsteadPayOrderException
	 */
	@Override
	public String verifySecondInsteadPay(InsteadPayOrderBean insteadPayOrderBean)
			throws InsteadPayOrderException {
		PojoInsteadPayRealtime queryInsteadPayOrder = insteadPayRealtimeDAO.queryInsteadPayOrder(insteadPayOrderBean.getOrderId(), insteadPayOrderBean.getMerId());
		if(queryInsteadPayOrder!=null){
			if(!queryInsteadPayOrder.getOrderCommiTime().equals(insteadPayOrderBean.getTxnTime())){
				throw new InsteadPayOrderException("OD017");//二次代付订单提交时间不一致
			}
			if(!queryInsteadPayOrder.getAccName().equals(insteadPayOrderBean.getAccName())){
				throw new InsteadPayOrderException("OD018");//二次代付收款账户名称不一致
			}
			if(!queryInsteadPayOrder.getAccNo().equals(insteadPayOrderBean.getAccNo())){
				throw new InsteadPayOrderException("OD019");//二次代付收款账号不一致
			}
			if(!queryInsteadPayOrder.getTransAmt().toString().equals(insteadPayOrderBean.getTxnAmt())){
				throw new InsteadPayOrderException("OD020");//二次代付代付金额不一致
			}
			return queryInsteadPayOrder.getTn();
		}
		return null;
	}

	/**
	 *
	 * @param insteadPayOrderBean
	 * @throws InsteadPayOrderException 
	 */
	@Override
	public void verifyRepeatInsteadPayOrder(
			InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException {
		// TODO Auto-generated method stub
		PojoInsteadPayRealtime queryInsteadPayOrder = insteadPayRealtimeDAO.queryInsteadPayOrder(insteadPayOrderBean.getOrderId(), insteadPayOrderBean.getMerId());
		if (queryInsteadPayOrder != null) {
			if ("00".equals(queryInsteadPayOrder.getStatus())) {// 交易成功订单不可二次支付
				throw new InsteadPayOrderException("T004","订单交易成功，请不要重复支付");
			}
			if ("02".equals(queryInsteadPayOrder.getStatus())) {
				throw new InsteadPayOrderException("T009","订单正在支付中，请不要重复支付");
			}
			if ("04".equals(queryInsteadPayOrder.getStatus())) {
				throw new InsteadPayOrderException("T012","订单失效");
			}
		}
	}

	/**
	 *
	 * @param merchant
	 * @throws CommonException
	 * @throws InsteadPayOrderException 
	 */
	@Override
	public void checkBusiAcctOfInsteadPay(String merchant,String txnAmt)
			throws CommonException, InsteadPayOrderException {
		MemberBean member = new MemberBean();
		member.setMemberId(merchant);
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.ENTERPRISE, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new InsteadPayOrderException("OD012");
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_OUT) {
			//throw new TradeException("GW05");
			throw new InsteadPayOrderException("OD014");
		}
		
		// 商户余额是否足够
        BigDecimal payBalance = new BigDecimal(txnAmt);
       
        BigDecimal merBalance = memberAccountBean != null ? memberAccountBean.getBalance() : BigDecimal.ZERO;
        if (merBalance.compareTo(payBalance) < 0) {
        	throw new InsteadPayOrderException("OD023");
        }
		
	}

	/**
	 *
	 * @throws InsteadPayOrderException
	 */
	@Override
	public void checkInsteadPayTime() throws InsteadPayOrderException {
		// TODO Auto-generated method stub
		//交易时间是否在规定时间内
		Date currentTime;
        try {
            String startTime = Constant.getInstance().getInstead_pay_realtime_start_time();
                   
            String endTime = Constant.getInstance().getInstead_pay_realtime_end_time();
            currentTime = DateUtil.convertToDate(DateUtil.getCurrentTime(),"HHmmss");
            Date insteadStartTime = DateUtil.convertToDate(DateUtil.getCurrentDate()+startTime,"HHmmss");
            Date insteadEndTime = DateUtil.convertToDate(DateUtil.getCurrentDate()+endTime,"HHmmss");
            if (currentTime.before(insteadEndTime)
                   && currentTime.after(insteadStartTime)) {
            	throw new InsteadPayOrderException("OD021");//非交易时间
            } 
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage(), e);
            throw new InsteadPayOrderException("OD022");
        }
	}

	/**
	 *
	 * @param cardNo
	 * @param cardType
	 * @throws InsteadPayOrderException
	 */
	@Override
	public CardBin checkInsteadPayCard(String cardNo)throws InsteadPayOrderException {
		 CardBin cardMap = txnsLogDAO.getCard(cardNo);
		 if(cardMap==null||cardMap.getType()==null){
			 throw new InsteadPayOrderException("OD024");
		 }
		 return cardMap;
	}

}
