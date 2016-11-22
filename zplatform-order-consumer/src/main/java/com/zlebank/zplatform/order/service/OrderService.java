/* 
 * OrderService.java  
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
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 上午10:28:29
 * @since 
 */
public interface OrderService {

	/**
	 * 订单生成
	 * @param orderBean
	 * @return
	 * @throws OrderException 
	 */
	public String create(BaseOrderBean orderBean) throws OrderException;
}
