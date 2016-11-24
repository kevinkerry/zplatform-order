/* 
 * AbstractRefundOrderService.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.refund;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zlebank.zplatform.acc.bean.enums.AcctStatusType;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.member.coopinsti.bean.CoopInsti;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.bean.MemberAccountBean;
import com.zlebank.zplatform.member.individual.bean.MemberBean;
import com.zlebank.zplatform.member.individual.bean.PoMemberBean;
import com.zlebank.zplatform.member.individual.bean.enums.MemberType;
import com.zlebank.zplatform.member.individual.service.MemberAccountService;
import com.zlebank.zplatform.member.individual.service.MemberService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.TxnsRefundDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.enums.BusiTypeEnum;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.refund.bean.RefundOrderBean;
import com.zlebank.zplatform.order.service.CheckOfServcie;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.utils.Constant;
import com.zlebank.zplatform.order.utils.DateUtil;
import com.zlebank.zplatform.order.utils.ValidateLocator;
import com.zlebank.zplatform.risk.exception.TradeRiskException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午3:47:24
 * @since 
 */
@Component
public abstract class AbstractRefundOrderService implements OrderService,CheckOfServcie<RefundOrderBean>{
	private static final Logger logger = LoggerFactory.getLogger(AbstractRefundOrderService.class);
	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private MerchService merchService;
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MemberAccountService memberAccountService;
	@Autowired
	private TxnsRefundDAO txnsRefundDAO;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	/**
	 *
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException {
		// TODO Auto-generated method stub
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans((RefundOrderBean)baseOrderBean);
		if(!resultBean.isResultBool()){
			throw new OrderException("OD049", resultBean.getErrMsg());
		}
	}

	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws OrderException
	 */
	@Override
	public String checkOfSecondPay(RefundOrderBean orderBean)
			throws OrderException {
		PojoTxnsOrderinfo orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if(orderinfo==null){
			return null;
		}
		if(orderinfo.getOrderamt().longValue()!=Long.valueOf(orderBean.getTxnAmt()).longValue()){
			logger.info("订单金额:{};数据库订单金额:{}", orderBean.getTxnAmt(),orderinfo.getOrderamt());
			throw new OrderException("OD015");
		}
		
		if(!orderinfo.getOrdercommitime().equals(orderBean.getTxnTime())){
			logger.info("订单时间:{};数据库订单时间:{}", orderBean.getTxnTime(),orderinfo.getOrdercommitime());
			throw new OrderException("OD016");
		}
		return orderinfo.getTn();
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfRepeatSubmit(RefundOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		PojoTxnsOrderinfo orderInfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new OrderException("OD001","订单交易成功，请不要重复支付");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new OrderException("OD002","订单正在支付中，请不要重复支付");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new OrderException("OD003","订单失效");
			}
			
		}
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiness(RefundOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.refund){
        	if(StringUtils.isEmpty(orderBean.getMerId())){
        		 throw new OrderException("OD004", "商户号为空");
        	}
        	MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerId());
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new OrderException("OD005", "商户未开通此业务");
            }
        }else{
            throw new OrderException("OD045");
        }
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfMerchantAndCoopInsti(RefundOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		MerchantBean subMember = merchService.getMerchBymemberId(orderBean.getMerId());
        if (subMember == null) {
        	throw new OrderException("OD009");
        }
        PoMemberBean pojoMember = memberService.getMbmberByMemberId(orderBean.getMerId(), null);
        //校验商户会员信息 
        if (pojoMember.getMemberType()==MemberType.ENTERPRISE) {// 对于企业会员需要进行检查
        	CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiID(pojoMember.getInstiId());
            if (!orderBean.getCoopInstiId().equals(pojoCoopInsti.getInstiCode())) {
            	throw new OrderException("OD010");
            }
        }
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiAcct(RefundOrderBean orderBean)
			throws OrderException {
		MemberBean member = new MemberBean();
		member.setMemberId(orderBean.getMerId());
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.ENTERPRISE, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new OrderException("OD012");
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_OUT) {
			//throw new TradeException("GW05");
			throw new OrderException("OD014");
		}
		
		// 商户余额是否足够
        BigDecimal payBalance = new BigDecimal(orderBean.getTxnAmt());
       
        BigDecimal merBalance = memberAccountBean != null ? memberAccountBean.getBalance() : BigDecimal.ZERO;
        if (merBalance.compareTo(payBalance) < 0) {
        	throw new OrderException("OD023");
        }
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfSpecialBusiness(RefundOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		PojoTxnsOrderinfo orderinfo_old = null;
		if(StringUtils.isNotEmpty(orderBean.getOrigTN())){
			orderinfo_old = txnsOrderinfoDAO.getOrderinfoByTN(orderBean.getOrigTN());
		}else{
			orderinfo_old = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrigOrderId(), orderBean.getMerId());
		}
		if (orderinfo_old == null) {
			throw new OrderException("OD031");
		}

		PojoTxnsLog old_txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(orderinfo_old.getRelatetradetxn());
		if (old_txnsLog == null) {
			throw new OrderException("OD032");
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
			throw new OrderException("OD033");
		}

		try {
			Long old_amount = orderinfo_old.getOrderamt();
			Long refund_amount = Long.valueOf(orderBean.getTxnAmt());
			if (refund_amount > old_amount) {
				throw new OrderException("T021");
			} else if (refund_amount == old_amount) {// 原始订单退款(全额退款)
				// 具体的处理方法暂时不明
			} else if (refund_amount < old_amount) {// 部分退款 支持

			}
			// 部分退款时校验t_txns_refund表中的正在审核或者已经退款的交易的金额之和
			Long sumAmt = txnsRefundDAO.getSumAmtByOldTxnseqno(old_txnsLog
					.getTxnseqno());
			if ((sumAmt + refund_amount) > old_amount) {
				logger.info("商户：" + orderBean.getMerId() + "退款订单("
						+ orderBean.getOrderId() + ")退款金额之和大于原始订单金额");
				throw new OrderException("OD034");
			}

		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new OrderException("OD035");
		}
	}

	/**
	 * 检查所有订单有效性检查项
	 * @param baseOrderBean
	 * @throws OrderException 
	 */
	public abstract void checkOfAll(BaseOrderBean baseOrderBean) throws OrderException;
	
	/**
	 * 保存订单信息
	 * @param orderBean
	 * @throws OrderException 
	 * @throws TradeRiskException 
	 */
	public abstract String saveRefundOrder(BaseOrderBean baseOrderBean) throws OrderException, TradeRiskException;
}
