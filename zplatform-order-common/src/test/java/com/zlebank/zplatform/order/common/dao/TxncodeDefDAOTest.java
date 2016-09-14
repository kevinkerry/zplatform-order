/* 
 * TxncodeDefDAOTest.java  
 * 
 * version TODO
 *
 * 2016年9月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxncodeDef;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月14日 下午2:18:43
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ContextTest.xml")
public class TxncodeDefDAOTest {

	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	
	@Test
	public void test(){
		PojoTxncodeDef busiCode = txncodeDefDAO.getBusiCode("17", "00", "000205");
		System.out.println(JSON.toJSONString(busiCode));
	}
}
