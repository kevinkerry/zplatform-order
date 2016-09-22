/* 
 * ProducerThreadTest.java  
 * 
 * version TODO
 *
 * 2016年9月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer;

import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.zlebank.zplatform.order.producer.bean.OrderBean;
import com.zlebank.zplatform.order.producer.callback.SimpleOrderCallback;
import com.zlebank.zplatform.order.producer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.producer.interfaces.Producer;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月21日 下午5:11:08
 * @since 
 */
public class ProducerThreadTest implements Runnable{

	/**
	 *
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Producer producer = new SimpleOrderProducer("192.168.101.104:9876");
			
			for(int i=0;i<1;i++){
				String json = "{\"accNo\":\"\",\"accType\":\"\",\"accessType\":\"0\",\"backUrl\":\"http://192.168.101.209:8080/demo/ReciveNotifyServlet\",\"bizType\":\"000201\",\"cardTransData\":\"\",\"certId\":\"123\",\"channelType\":\"07\",\"coopInstiId\":\"300000000000014\",\"currencyCode\":\"156\",\"customerInfo\":\"\",\"customerIp\":\"\",\"defaultPayType\":\"\",\"encoding\":\"1\",\"encryptCertId\":\"1234\",\"frontFailUrl\":\"\",\"frontUrl\":\"http://192.168.101.209:8081/demo/ReciveNotifyServlet\",\"instalTransInfo\":\"\",\"issInsCode\":\"\",\"merAbbr\":\"\",\"merId\":\"200000000000597\",\"merName\":\"\",\"orderDesc\":\"\",\"orderId\":\"2016091911374464\",\"orderTimeout\":\"10000\",\"payTimeout\":\"20160920112215\",\"reqReserved\":\"00\",\"reserved\":\"\",\"riskRateInfo\":\"shippingFlag=000&shippingCountryCode=0&shippingProvinceCode=0&shippingCityCode=0&shippingDistrictCode=0&shippingStreet=0&commodityCategory=0&commodityName=iphone&commodityUrl=0&commodityUnitPrice=0&commodityQty=0&shippingMobile=0&addressModifyTim=0&userRegisterTime=0&orderNameModifyTime=0&userId=0&orderName=0&userFlag=0&mobileModifyTime=0&riskLevel=0&merUserId=100000000000576&merUserRegDt=0&merUserEmail=0\",\"signMethod\":\"01\",\"signature\":\"CHjzqn7n7X3ugpXQHX7kfpK3gifmW1aQ5DUWAsO+ibY1C5p0VaIXhgzAtkUkXRNbw+msMCdmPJtZEMoTHirgxYLAkf0FRNDLL0PQ0sOPDDyt51D3axR8Z4crHKP+/FDntLznYINOh4zuvwBCat32JhsviwhYEtwWEfn5eNlVGQk=\",\"supPayType\":\"\",\"txnAmt\":\"13\",\"txnSubType\":\"00\",\"txnTime\":\"20160919112215\",\"txnType\":\"01\",\"userMac\":\"\",\"version\":\"v1.0\"}";
				OrderBean orderBean = JSON.parseObject(json, OrderBean.class);
				Random random = new Random();
				orderBean.setOrderId("G"+System.currentTimeMillis()+random.nextInt(9999));
				orderBean.setMemberId("100000000000576");
				producer.sendJsonMessage(JSON.toJSONString(orderBean), OrderTagsEnum.COMMONCONSUME_SIMPLIFIED, new SimpleOrderCallback());
			}
			producer.closeProducer();
		} catch (MQClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemotingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
