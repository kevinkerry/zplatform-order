/* 
 * CommonOrderService.java  
 * 
 * version TODO
 *
 * 2016年9月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service;

import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.order.common.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.bean.OrderInfoBean;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsLog;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.common.exception.InsteadPayOrderException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月9日 下午2:50:59
 * @since 
 */
public interface CommonOrderService {

	
	/**
	 * 校验订单是否为二次支付订单
	 * @param orderBean
	 * @return
	 * @throws CommonException
	 */
	public String verifySecondPay(OrderBean orderBean) throws CommonException;
	
	/**
	 * 校验订单的唯一性
	 * @param orderinfo
	 * @throws CommonException
	 */
	public void verifyRepeatOrder(OrderBean orderBean) throws CommonException;
	
	
	/**
	 * 校验业务类型
	 * @param order
	 * @return
	 */
    public void verifyBusiness(OrderBean orderBean) throws CommonException;
    
    /**
     * 校验商户和合作机构
     * @param merchant
     * @param coopInsti
     */
    public void verifyMerchantAndCoopInsti(String merchant,String coopInsti) throws CommonException;
    
    /**
     * 业务校验
     * @param orderBean
     * @return
     * @throws CommonException
     */
    public void validateBusiness(OrderBean orderBean) throws CommonException;
    
    
    /**
     * 检查资金账户状态
     * @param merchant 商户号
     * @param memberId 会员号
     * @throws CommonException
     */
    public void checkBusiAcct(String merchant,String memberId) throws CommonException;
    
    /**
     * 通过订单号和商户号获取订单信息
     * @param orderNo
     * @param merchNo
     * @return
     */
    public OrderInfoBean getOrderinfoByOrderNoAndMerchNo(String orderNo,String merchNo);
    
    /**
     * 保存订单信息
     * @param orderInfoBean
     */
    public void saveOrderInfo(OrderInfoBean orderInfoBean);
    
    /**
     * 保存交易流水信息
     * @param txnsLog
     */
    public void saveTxnsLog(PojoTxnsLog txnsLog);
    
    /**
     * 校验订单是否为二次代付订单
     * @param insteadPayOrderBean 代付订单bean
     * @return tn 受理订单号
     * @throws InsteadPayOrderException 代付订单异常
     */
    public String verifySecondInsteadPay(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException;
    
    /**
     * 校验代付订单的唯一性
     * @param insteadPayOrderBean
     * @throws InsteadPayOrderException 代付订单异常
     */
    public void verifyRepeatInsteadPayOrder(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException ;
    
    /**
     * 检查代付业务资金账户状态
     * @param merchant 商户号
     * @param txnAmt 交易金额
     * @throws CommonException
     * @throws InsteadPayOrderException
     */
    public void checkBusiAcctOfInsteadPay(String merchant,String txnAmt) throws CommonException,InsteadPayOrderException;
    
    /**
     * 检查代付时间
     * @throws InsteadPayOrderException
     */
    public void checkInsteadPayTime() throws InsteadPayOrderException;
    
    /**
     * 检查代付的银行卡（只限对私）
     * @param cardNo 银行卡号
     * @throws InsteadPayOrderException
     */
    public CardBin checkInsteadPayCard(String cardNo) throws InsteadPayOrderException;
}
