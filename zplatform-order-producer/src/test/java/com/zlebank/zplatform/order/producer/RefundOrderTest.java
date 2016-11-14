/* 
 * RefundOrderTest.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer;

import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.zlebank.zplatform.order.producer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.producer.interfaces.Producer;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午1:18:31
 * @since 
 */
public class RefundOrderTest {

	@Test
	public void test_refund() throws MQClientException, RemotingException, InterruptedException, MQBrokerException, IOException{
		String json = "{\"accNo\":\"\",\"accType\":\"\",\"accessType\":\"\",\"backUrl\":\"wallet message has no this field\",\"bizType\":\"000205\",\"cardTransData\":\"\",\"certId\":\"-1\",\"channelType\":\"00\",\"coopInstiId\":\"300000000000014\",\"currencyCode\":\"156\",\"customerInfo\":\"\",\"customerIp\":\"\",\"defaultPayType\":\"\",\"encoding\":\"1\",\"encryptCertId\":\"\",\"frontFailUrl\":\"\",\"frontUrl\":\"wallet message has no this field\",\"instalTransInfo\":\"\",\"issInsCode\":\"\",\"merAbbr\":\"\",\"merId\":\"\",\"merName\":\"\",\"orderDesc\":\"消费退款\",\"orderId\":\"20161021112021\",\"orderTimeout\":\"1800000\",\"origOrderId\":\"161020059700058608\",\"payTimeout\":\"20161022112021\",\"productcode\":\"\",\"reqReserved\":\"\",\"reserved\":\"\",\"riskRateInfo\":\"merUserId=null&commodityQty=0&commodityUnitPrice=0&\",\"signMethod\":\"\",\"signature\":\"\",\"supPayType\":\"\",\"tn\":\"20161021112021\",\"txnAmt\":\"1\",\"txnSubType\":\"00\",\"txnTime\":\"20161021112021\",\"txnType\":\"14\",\"userMac\":\"\",\"version\":\"1.0\"}";
		RefundOrderTestBean orderTestBean = JSON.parseObject(json, RefundOrderTestBean.class);
		orderTestBean.setOrderId(System.currentTimeMillis()+"");
		orderTestBean.setOrigTN("161114059700000546");
		orderTestBean.setOrigOrderId("1479095843250");
		orderTestBean.setMerId("200000000000597");
		orderTestBean.setMemberId("100000000000576");
		orderTestBean.setTxnAmt("1");
		Producer producer = new SimpleOrderProducer("192.168.101.104:9876");
		producer.sendJsonMessage(JSON.toJSONString(orderTestBean), OrderTagsEnum.REFUND_SIMPLIFIED);
		System.in.read();
	}
}
