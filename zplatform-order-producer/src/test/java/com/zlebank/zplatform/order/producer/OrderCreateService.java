/* 
 * OrderCreateService.java  
 * 
 * version TODO
 *
 * 2016年9月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer;

import com.zlebank.zplatform.order.producer.bean.OrderBean;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月21日 下午3:38:25
 * @since 
 */
public interface OrderCreateService {

	/**
	 * 创建一般消费订单
	 * @param orderBean
	 * @return
	 */
	public String createConsumeOrder(OrderBean orderBean);
	
	/**
	 * 创建产品消费订单
	 * @param orderBean
	 * @return
	 */
	public String createPorductOrder(OrderBean orderBean);
	
	/**
	 * 创建提现订单
	 * @param orderBean
	 * @return
	 */
	public String createWithdrawOrder(OrderBean orderBean);
	
	/**
	 * 创建退款订单
	 * @param orderBean
	 * @return
	 */
	public String createRefundOrder(OrderBean orderBean);
}
