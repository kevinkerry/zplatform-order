/* 
 * RedisSequenceTest.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.sequence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.order.sequence.SerialNumberService;



/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午5:14:47
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ContextTest.xml")
public class RedisSerialNumberServiceTest {

	@Autowired
	private SerialNumberService serialNumberService;
	
	@Test
	public void test_get_txnseqno(){
		String txnseqno = serialNumberService.generateTxnseqno();
		System.out.println(txnseqno);
	}
}
