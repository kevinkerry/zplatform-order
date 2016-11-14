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
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
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
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.FinanceProductAccountBean;
import com.zlebank.zplatform.member.bean.FinanceProductQueryBean;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.pojo.PojoMerchDeta;
import com.zlebank.zplatform.order.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.bean.OrderBean;
import com.zlebank.zplatform.order.bean.OrderInfoBean;
import com.zlebank.zplatform.order.bean.RefundOrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.bean.WithdrawBean;
import com.zlebank.zplatform.order.dao.InsteadPayRealtimeDAO;
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.TxnsRefundDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoInsteadPayRealtime;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.enums.AccountTypeEnum;
import com.zlebank.zplatform.order.enums.ExceptionTypeEnum;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.exception.InsteadPayOrderException;
import com.zlebank.zplatform.order.exception.RefundOrderException;
import com.zlebank.zplatform.order.exception.WithdrawOrderException;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.order.utils.Constant;
import com.zlebank.zplatform.order.utils.DateUtil;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IFinanceProductAccountService;
import com.zlebank.zplatform.rmi.member.IMemberAccountService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.member.IMerchService;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;

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
	@Autowired
	private TxnsRefundDAO txnsRefundDAO;
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
        	//个人充值
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("OD008", "会员不存在无法进行充值");
			}
        	//商户充值
        	if (StringUtil.isNotEmpty(orderBean.getMerId())){
        		PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());
            	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            	 if(prodCase==null){
                     throw new CommonException("OD005", "商户未开通此业务");
                 }
        	}
        	
        }else if(busiTypeEnum==BusiTypeEnum.withdrawal){//提现
        	//个人提现
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("OD008", "会员不存在无法进行充值");
			}
        	//商户提现
        	if (StringUtil.isNotEmpty(orderBean.getMerId())){
        		PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());
            	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            	 if(prodCase==null){
                     throw new CommonException("OD005", "商户未开通此业务");
                 }
        	}
        }else if(busiTypeEnum==BusiTypeEnum.insteadPay){
        	if(StringUtil.isEmpty(orderBean.getMerId())){
        		 throw new CommonException("OD004", "商户号为空");
        	}
        	PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new CommonException("OD005", "商户未开通此业务");
            }
            
        }else if(busiTypeEnum==BusiTypeEnum.refund){
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
            currentTime = com.zlebank.zplatform.commons.utils.DateUtil.convertToDate(DateUtil.getCurrentTime(),"HHmmss");
            Date insteadStartTime = com.zlebank.zplatform.commons.utils.DateUtil.convertToDate(DateUtil.getCurrentDate()+startTime,"HHmmss");
            Date insteadEndTime = com.zlebank.zplatform.commons.utils.DateUtil.convertToDate(DateUtil.getCurrentDate()+endTime,"HHmmss");
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

	/**
	 *
	 * @param refundOrderBean
	 * @throws RefundOrderException 
	 */
	@Override
	public void verifyRepeatRefundOrder(RefundOrderBean refundOrderBean) throws RefundOrderException {
		// TODO Auto-generated method stub
		OrderInfoBean orderInfo = getOrderinfoByOrderNoAndMerchNo(refundOrderBean.getOrderId(), refundOrderBean.getMerId());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new RefundOrderException("OD029");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new RefundOrderException("OD030");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new RefundOrderException("OD003");
			}
		}
	}

	/**
	 *
	 * @param merchant
	 * @param txnAmt
	 * @throws CommonException
	 * @throws RefundOrderException
	 */
	@Override
	public void checkBusiAcctOfRefund(String merchant, String txnAmt)
			throws CommonException, RefundOrderException {
		// TODO Auto-generated method stub
		MemberBean member = new MemberBean();
		member.setMemberId(merchant);
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.ENTERPRISE, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RefundOrderException("OD012");
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_OUT) {
			//throw new TradeException("GW05");
			throw new RefundOrderException("OD014");
		}
		
		// 商户余额是否足够
        BigDecimal payBalance = new BigDecimal(txnAmt);
       
        BigDecimal merBalance = memberAccountBean != null ? memberAccountBean.getBalance() : BigDecimal.ZERO;
        if (merBalance.compareTo(payBalance) < 0) {
        	throw new RefundOrderException("OD023");
        }
	}

	/**
	 *
	 * @param refundOrderBean
	 */
	@Override
	public void checkOldOrder(RefundOrderBean refundOrderBean) throws RefundOrderException{
		// TODO Auto-generated method stub
		PojoTxnsOrderinfo orderinfo_old = null;
		if(StringUtils.isNotEmpty(refundOrderBean.getOrigTN())){
			orderinfo_old = txnsOrderinfoDAO.getOrderinfoByTN(refundOrderBean.getOrigTN());
		}else{
			orderinfo_old = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(refundOrderBean.getOrigOrderId(), refundOrderBean.getMerId());
		}
		if (orderinfo_old == null) {
			throw new RefundOrderException("OD031");
		}

		PojoTxnsLog old_txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(orderinfo_old.getRelatetradetxn());
		if (old_txnsLog == null) {
			throw new RefundOrderException("OD032");
		}

		// /判断交易时间是否超过期限
		String txnDateTime = old_txnsLog.getAccordfintime();// 交易完成时间作为判断依据
		Date txnDate = DateUtil.parse(DateUtil.DEFAULT_DATE_FROMAT, txnDateTime);
		Date failureDateTime = DateUtil.skipDateTime(txnDate,
				Integer.valueOf(Constant.getInstance().getRefund_day()));// 失效的日期
		Calendar first_date = Calendar.getInstance();
		first_date.setTime(new Date());
		Calendar d_end = Calendar.getInstance();
		d_end.setTime(failureDateTime);
		logger.info("trade date:"
				+ DateUtil.formatDateTime(DateUtil.SIMPLE_DATE_FROMAT, txnDate));
		logger.info("first_date date:"
				+ DateUtil.formatDateTime(DateUtil.SIMPLE_DATE_FROMAT,
						first_date.getTime()));
		logger.info("d_end(trade failure) date:"
				+ DateUtil.formatDateTime(DateUtil.SIMPLE_DATE_FROMAT,
						failureDateTime));

		if (!DateUtil.calendarCompare(first_date, d_end)) {
			throw new RefundOrderException("OD033");
		}

		try {
			Long old_amount = orderinfo_old.getOrderamt();
			Long refund_amount = Long.valueOf(refundOrderBean.getTxnAmt());
			if (refund_amount > old_amount) {
				throw new RefundOrderException("T021");
			} else if (refund_amount == old_amount) {// 原始订单退款(全额退款)
				// 具体的处理方法暂时不明
			} else if (refund_amount < old_amount) {// 部分退款 支持

			}
			// 部分退款时校验t_txns_refund表中的正在审核或者已经退款的交易的金额之和
			Long sumAmt = txnsRefundDAO.getSumAmtByOldTxnseqno(old_txnsLog
					.getTxnseqno());
			if ((sumAmt + refund_amount) > old_amount) {
				logger.info("商户：" + refundOrderBean.getMerId() + "退款订单("
						+ refundOrderBean.getOrderId() + ")退款金额之和大于原始订单金额");
				throw new RefundOrderException("OD034");
			}

		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new RefundOrderException("OD035");
		}
	}
	@Override
	public void verifyRepeatWithdrawOrder(WithdrawBean withdrawBean) throws WithdrawOrderException {
		// TODO Auto-generated method stub
		OrderInfoBean orderInfo = getOrderinfoByOrderNoAndMerchNo(withdrawBean.getOrderId(), withdrawBean.getMerId());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new WithdrawOrderException("OD029");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new WithdrawOrderException("OD030");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new WithdrawOrderException("OD003");
			}
		}
	}
	
	@Override
	public void checkBusiAcctOfWithdraw(String memberId,String txnAmt) throws WithdrawOrderException{
		MemberBean member = new MemberBean();
		member.setMemberId(memberId);
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.INDIVIDUAL, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new WithdrawOrderException("OD012");
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_OUT) {
			//throw new TradeException("GW05");
			throw new WithdrawOrderException("OD014");
		}
		
		// 商户余额是否足够
        BigDecimal payBalance = new BigDecimal(txnAmt);
       
        BigDecimal merBalance = memberAccountBean != null ? memberAccountBean.getBalance() : BigDecimal.ZERO;
        if (merBalance.compareTo(payBalance) < 0) {
        	throw new WithdrawOrderException("OD023");
        }
	}
}
