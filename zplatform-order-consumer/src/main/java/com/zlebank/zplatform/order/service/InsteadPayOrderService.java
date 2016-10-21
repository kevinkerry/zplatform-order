/* 
 * InsteadPayOrderService.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service;

import com.zlebank.zplatform.order.common.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.common.bean.ResultBean;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.common.exception.InsteadPayOrderException;

/**
 * 代付订单接口
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 上午8:42:57
 * @since 
 */
public interface InsteadPayOrderService {

	/**
	 * 创建实时代付订单
	 * @param insteadPayOrderBean 实时代付订单bean
	 * @return 结果bean
	 */
	public ResultBean createRealTimeOrder(InsteadPayOrderBean insteadPayOrderBean) throws InsteadPayOrderException,CommonException;
	
}
