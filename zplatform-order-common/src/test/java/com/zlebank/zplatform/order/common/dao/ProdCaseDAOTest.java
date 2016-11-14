/* 
 * ProdCaseDAOTest.java  
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
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月14日 下午2:00:06
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ContextTest.xml")
public class ProdCaseDAOTest {
	
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	
	@Test
	public void test_getMerchProd(){
		PojoProdCase merchProd = prodCaseDAO.getMerchProd("00000009", "10000001");
		System.out.println(JSON.toJSONString(merchProd));
	}
}
