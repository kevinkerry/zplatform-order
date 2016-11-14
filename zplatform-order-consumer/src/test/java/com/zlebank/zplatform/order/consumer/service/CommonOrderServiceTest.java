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

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.order.bean.OrderBean;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.order.service.ConsumeOrderService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月14日 下午2:40:54
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class CommonOrderServiceTest {

	@Autowired
	private CommonOrderService commonOrderService;
	@Autowired
	private ConsumeOrderService consumeOrderService;
	private OrderBean orderBean;
	
	@Before
	public void test_generateOrderBean(){
		String json = "{\"accNo\":\"\",\"accType\":\"\",\"accessType\":\"0\",\"backUrl\":\"http://192.168.101.209:8080/demo/ReciveNotifyServlet\",\"bizType\":\"000201\",\"cardTransData\":\"\",\"certId\":\"123\",\"channelType\":\"07\",\"coopInstiId\":\"300000000000014\",\"currencyCode\":\"156\",\"customerInfo\":\"\",\"customerIp\":\"\",\"defaultPayType\":\"\",\"encoding\":\"1\",\"encryptCertId\":\"1234\",\"frontFailUrl\":\"\",\"frontUrl\":\"http://192.168.101.209:8081/demo/ReciveNotifyServlet\",\"instalTransInfo\":\"\",\"issInsCode\":\"\",\"merAbbr\":\"\",\"merId\":\"200000000000597\",\"merName\":\"\",\"orderDesc\":\"\",\"orderId\":\"2016091911374464\",\"orderTimeout\":\"10000\",\"payTimeout\":\"20160920112215\",\"reqReserved\":\"00\",\"reserved\":\"\",\"riskRateInfo\":\"shippingFlag=000&shippingCountryCode=0&shippingProvinceCode=0&shippingCityCode=0&shippingDistrictCode=0&shippingStreet=0&commodityCategory=0&commodityName=iphone&commodityUrl=0&commodityUnitPrice=0&commodityQty=0&shippingMobile=0&addressModifyTim=0&userRegisterTime=0&orderNameModifyTime=0&userId=0&orderName=0&userFlag=0&mobileModifyTime=0&riskLevel=0&merUserId=100000000000576&merUserRegDt=0&merUserEmail=0\",\"signMethod\":\"01\",\"signature\":\"CHjzqn7n7X3ugpXQHX7kfpK3gifmW1aQ5DUWAsO+ibY1C5p0VaIXhgzAtkUkXRNbw+msMCdmPJtZEMoTHirgxYLAkf0FRNDLL0PQ0sOPDDyt51D3axR8Z4crHKP+/FDntLznYINOh4zuvwBCat32JhsviwhYEtwWEfn5eNlVGQk=\",\"supPayType\":\"\",\"txnAmt\":\"13\",\"txnSubType\":\"00\",\"txnTime\":\"20160919112215\",\"txnType\":\"01\",\"userMac\":\"\",\"version\":\"v1.0\"}";
		orderBean = JSON.parseObject(json, OrderBean.class);
		orderBean.setOrderId("G"+DateUtil.getCurrentDateTime());
		//orderBean.setTxnAmt("1");
		orderBean.setMemberId("100000000000576");
	}
	
	@Test
	public void test_verifySecondPay() throws CommonException{
		//commonOrderService.verifySecondPay(orderBean);
		
		//consumeOrderService.checkOrderInfo(orderBean);
		consumeOrderService.createConsumeOrder(orderBean);
	}
	
	
}
