/* 
 * AbstractWithdrawOrderService.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.withdraw;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.bean.MemberAccountBean;
import com.zlebank.zplatform.member.individual.bean.MemberBean;
import com.zlebank.zplatform.member.individual.bean.enums.MemberType;
import com.zlebank.zplatform.member.individual.service.MemberAccountService;
import com.zlebank.zplatform.member.individual.service.MemberService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.enums.AcctStatusType;
import com.zlebank.zplatform.order.enums.BusiTypeEnum;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.service.CheckOfServcie;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.utils.ValidateLocator;
import com.zlebank.zplatform.order.withdraw.bean.WithdrawOrderBean;
import com.zlebank.zplatform.risk.exception.TradeRiskException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午4:23:38
 * @since 
 */
public abstract class AbstractWithdrawOrderService implements OrderService,CheckOfServcie<WithdrawOrderBean>{
	private static final Logger logger = LoggerFactory.getLogger(AbstractWithdrawOrderService.class);
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
	
	/**
	 *
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException {
		// TODO Auto-generated method stub
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans((WithdrawOrderBean)baseOrderBean);
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
	public String checkOfSecondPay(WithdrawOrderBean orderBean)
			throws OrderException {
		PojoTxnsOrderinfo orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if(orderinfo==null){
			return null;
		}
		if(orderinfo.getOrderamt().longValue()!=Long.valueOf(orderBean.getAmount()).longValue()){
			logger.info("订单金额:{};数据库订单金额:{}", orderBean.getAmount(),orderinfo.getOrderamt());
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
	public void checkOfRepeatSubmit(WithdrawOrderBean orderBean)
			throws OrderException {
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
	public void checkOfBusiness(WithdrawOrderBean orderBean)
			throws OrderException {
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.withdrawal){//提现
        	//个人提现
        	if (StringUtils.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new OrderException("OD008");
			}
        	//商户提现
        	if (StringUtils.isNotEmpty(orderBean.getMerId())){
        		MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerId());
        		if(member==null){
            		throw new OrderException("OD009");
            	}
            	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            	if(prodCase==null){
                    throw new OrderException("OD005");
                }
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
	public void checkOfMerchantAndCoopInsti(WithdrawOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiAcct(WithdrawOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		MemberBean member = new MemberBean();
		member.setMemberId(orderBean.getMemberId());
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.INDIVIDUAL, member, Usage.BASICPAY);
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
        BigDecimal payBalance = new BigDecimal(orderBean.getAmount());
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
	public void checkOfSpecialBusiness(WithdrawOrderBean orderBean)
			throws OrderException {
		// TODO Auto-generated method stub
		
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
	public abstract String saveWithdrawOrder(BaseOrderBean baseOrderBean) throws OrderException, TradeRiskException;
}
