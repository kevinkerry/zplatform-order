/* 
 * CommonOrderServiceTest.java  
 * 
 * version TODO
 *
 * 2016年9月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.consumer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.service.CommonOrderService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月14日 下午2:40:54
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ContextTest.xml")
public class CommonOrderServiceTest {

	@Autowired
	private CommonOrderService commonOrderService;
	
	private OrderBean orderBean;
	
	@Before
	public void test_generateOrderBean(){
		orderBean = new OrderBean();
	}
	
	@Test
	public void test_verifySecondPay() throws CommonException{
		commonOrderService.verifySecondPay(orderBean);
	}
}
