/* 
 * SimpleOrderProducer.java  
 * 
 * version TODO
 *
 * 2016年9月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.google.common.base.Charsets;
import com.zlebank.zplatform.order.producer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.producer.interfaces.Producer;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月7日 下午12:46:36
 * @since 
 */
public class SimpleOrderProducer implements Producer{
	private final static Logger logger = LoggerFactory.getLogger(SimpleOrderProducer.class);
	 private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("producer");
	//RocketMQ消费者客户端
	private DefaultMQProducer producer;
	//主题
	private String topic;
		
	public SimpleOrderProducer(String namesrvAddr) throws MQClientException{
		logger.info("【初始化SimpleOrderProducer】");
		logger.info("【namesrvAddr】"+namesrvAddr);
		producer = new DefaultMQProducer(RESOURCE.getString("simple.order.producer.group"));
		producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName(RESOURCE.getString("OrderProducer"));
        topic = RESOURCE.getString("simple.order.subscribe");
        logger.info("【初始化SimpleOrderProducer结束】");
	}
	
	
	 
	/**
	 *
	 * @param message
	 * @throws InterruptedException 
	 * @throws RemotingException 
	 * @throws MQClientException 
	 */
	@Override
	public void sendMessage(Object message,OrderTagsEnum tags,SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
		if(producer==null){
			throw new MQClientException(-1,"未创建SimpleOrderProducer");
		}
		producer.start();
		Message msg = new Message(topic, tags.getCode(), JSON.toJSONString(message).getBytes(Charsets.UTF_8));
		producer.send(msg,sendCallback);
		producer.shutdown();
		producer = null;
	}



	/**
	 *
	 * @param message
	 * @param tags
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	@Override
	public void sendJsonMessage(String message, OrderTagsEnum tags,SendCallback sendCallback)
			throws MQClientException, RemotingException, InterruptedException {
		// TODO Auto-generated method stub
		if(producer==null){
			throw new MQClientException(-1,"SimpleOrderProducer为空");
		}
		Message msg = new Message(topic, tags.getCode(), message.getBytes(Charsets.UTF_8));
		producer.send(msg,sendCallback);
		producer.shutdown();
		producer = null;
	}



	/**
	 *
	 * @param message
	 * @param tags
	 * @return
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 * @throws MQBrokerException 
	 */
	@Override
	public SendResult sendJsonMessage(String message, OrderTagsEnum tags)
			throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
		if(producer==null){
			throw new MQClientException(-1,"SimpleOrderProducer为空");
		}
		Message msg = new Message(topic, tags.getCode(), message.getBytes(Charsets.UTF_8));
		SendResult sendResult = producer.send(msg);
		producer.shutdown();
		producer = null;
		return sendResult;
	}
}
