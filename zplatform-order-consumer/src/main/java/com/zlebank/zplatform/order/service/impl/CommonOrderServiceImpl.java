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

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.enums.AcctStatusType;
import com.zlebank.zplatform.acc.bean.enums.BusiType;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
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
import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.bean.OrderInfoBean;
import com.zlebank.zplatform.order.common.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.common.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.common.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.common.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.common.enums.ExceptionTypeEnum;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.rmi.acc.IFinanceProductService;
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
			throw new CommonException(ExceptionTypeEnum.SECOND_PAY.getCode(), "二次支付订单交易错误");
		}
		
		if(orderinfo.getOrdercommitime().equals(orderBean.getTxnTime())){
			throw new CommonException(ExceptionTypeEnum.SECOND_PAY.getCode(), "二次支付订单提交时间错误");
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
				throw new CommonException("T004","");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new CommonException("T009","");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new CommonException("T012","");
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
        	if(StringUtil.isNotEmpty(orderBean.getMerId())){
        		 throw new CommonException("GW26", "商户号为空");
        	}
        	PojoMerchDeta member = merchService.getMerchBymemberId(orderBean.getMerId());//memberService.getMemberByMemberId(order.getMerId());.java
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new CommonException("GW26", "商户未开通此业务");
            }
            if(BusinessEnum.CONSUMEQUICK_PRODUCT==businessEnum){//产品消费业务
            	FinanceProductQueryBean financeProductQueryBean = new FinanceProductQueryBean();
            	financeProductQueryBean.setProductCode(orderBean.getProductcode());
            	try {
					FinanceProductAccountBean productAccountBean = financeProductAccountService.queryBalance(financeProductQueryBean, Usage.BASICPAY);
					if(AcctStatusType.NORMAL!=AcctStatusType.fromValue(productAccountBean.getStatus())){
						throw new CommonException("", "产品异常，请联系客服");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new CommonException("", "产品不存在");
				}
            }
        }else if(busiTypeEnum==BusiTypeEnum.charge){//充值
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("GW19", "会员不存在无法进行充值");
			}
        }else if(busiTypeEnum==BusiTypeEnum.withdrawal){//提现
        	if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("GW19", "会员不存在无法进行充值");
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
            	throw new CommonException("GW05", "");
            }
            PojoMember pojoMember = memberService.getMbmberByMemberId(merchant, null);
            //校验商户会员信息 1-普通会员 2-商户会员 3-合作机构
            if (pojoMember.getMemberType()==MemberType.ENTERPRISE) {// 对于企业会员需要进行检查
            	CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiID(pojoMember.getInstiId());
                if (!coopInsti.equals(pojoCoopInsti.getInstiCode())) {
                	throw new CommonException("GW07", "");
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
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(
				orderBean.getTxnType(), orderBean.getTxnSubType(),
				orderBean.getBizType());
		
		
		if ("2000".equals(busiModel.getBusitype())) {
			if (StringUtil.isEmpty(orderBean.getMemberId()) || "999999999999999".equals(orderBean.getMemberId())) {
				throw new CommonException("GW19", "会员不存在无法进行充值");
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
				throw new CommonException("GW19", e.getMessage());
			}
			if (AcctStatusType.fromValue(memberAccountBean.getStatus()) != AcctStatusType.NORMAL) {
				//throw new TradeException("GW19");
				throw new CommonException("GW19", "");
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
			throw new CommonException("GW05", e.getMessage());
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_IN) {
			//throw new TradeException("GW05");
			throw new CommonException("GW05", "");
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

}
