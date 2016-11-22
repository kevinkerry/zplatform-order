/* 
 * RefundOrderService.java  
 * 
 * version TODO
 *
 * 2016年11月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service;

import com.zlebank.zplatform.order.bean.RefundOrderBean;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.exception.RefundOrderException;

/**
 * 退款订单service
 *
 * @author guojia
 * @version
 * @date 2016年11月11日 下午1:55:40
 * @since 
 */
@Deprecated
public interface RefundOrderService {

	/**
	 * 创建退款订单
	 * @param refundOrderBean 退款订单bean
	 * @return 受理订单号tn
	 */
	public String createRefundOrder(RefundOrderBean refundOrderBean) throws RefundOrderException,CommonException;
}
