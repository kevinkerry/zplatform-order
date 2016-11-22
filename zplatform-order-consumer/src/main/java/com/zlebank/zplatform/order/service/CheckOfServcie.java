/* 
 * CheckOfServcie.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service;

import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.exception.OrderException;

/**
 * 订单检查接口
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 上午11:34:31
 * @since 
 */
public interface CheckOfServcie<T> {

	/**
	 * 订单非空有效性检查
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException;
	
	/**
	 * 检查订单二次支付
	 * @param baseOrderBean
	 * @return 受理订单号 tn
	 * @throws OrderException
	 */
	public String checkOfSecondPay(T orderBean) throws OrderException;
	
	/**
	 * 检查订单是否为二次提交
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfRepeatSubmit(T orderBean) throws OrderException;
	
	/**
	 * 检查订单业务有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBusiness(T orderBean) throws OrderException;
	
	/**
	 * 检查商户和合作机构有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfMerchantAndCoopInsti(T orderBean) throws OrderException;
	
	/**
	 * 检查商户和会员的账户状态
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBusiAcct(T orderBean) throws OrderException;
	/**
	 * 检查消费订单特殊性要求检查，如果没有可以为空
	 * @param orderBean
	 * @throws OrderException 
	 */
	public void checkOfSpecialBusiness(T orderBean) throws OrderException;
}
