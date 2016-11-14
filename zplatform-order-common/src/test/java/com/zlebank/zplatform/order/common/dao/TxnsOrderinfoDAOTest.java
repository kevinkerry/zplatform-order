/* 
 * TxnsOrderinfoDAOTest.java  
 * 
 * version TODO
 *
 * 2016年9月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月14日 下午2:22:51
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ContextTest.xml")
public class TxnsOrderinfoDAOTest {

	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO;
	
	@Test
	@Ignore
	public void test_getOrderinfoByTxnseqno(){
		PojoTxnsOrderinfo orderinfoByTxnseqno = txnsOrderinfoDAO.getOrderinfoByTxnseqno("1510219900000043");
		Assert.assertNotNull(orderinfoByTxnseqno);
		System.out.println(JSON.toJSONString(orderinfoByTxnseqno));
	}
	
	@Test
	@Ignore
	public void test_getOrderinfoByOrderNoAndMerchNo(){
		PojoTxnsOrderinfo orderinfoByOrderNoAndMerchNo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo("2015102115657578", "200000000000002");
		Assert.assertNotNull(orderinfoByOrderNoAndMerchNo);
		System.out.println(JSON.toJSONString(orderinfoByOrderNoAndMerchNo));
	}
	
	@Test
	public void test_getOrderinfoByTN(){
		PojoTxnsOrderinfo orderinfoByTN = txnsOrderinfoDAO.getOrderinfoByTN("151114044300000503");
		Assert.assertNotNull(orderinfoByTN);
		System.out.println(JSON.toJSONString(orderinfoByTN));
	}
}
