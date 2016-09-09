/* 
 * SimpleOrderCallback.java  
 * 
 * version TODO
 *
 * 2016年9月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月7日 下午12:47:01
 * @since 
 */
public class SimpleOrderCallback implements SendCallback{

	private final static Logger logger = LoggerFactory.getLogger(SimpleOrderCallback.class);
	
	/**
	 *
	 * @param sendResult
	 */
	@Override
	public void onSuccess(SendResult sendResult) {
		logger.info("【SimpleOrderCallback receive Result message】"+JSON.toJSONString(sendResult));
	}

	/**
	 *
	 * @param e
	 */
	@Override
	public void onException(Throwable e) {
		// TODO Auto-generated method stub
		logger.error(e.getMessage(), e);
	}

	
}
