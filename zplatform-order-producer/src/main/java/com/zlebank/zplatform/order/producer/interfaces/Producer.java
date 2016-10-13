/* 
 * Producer.java  
 * 
 * version TODO
 *
 * 2016年9月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer.interfaces;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.zlebank.zplatform.order.producer.bean.ResultBean;
import com.zlebank.zplatform.order.producer.enums.OrderTagsEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月7日 下午2:51:29
 * @since 
 */
public interface Producer {

	/**
	 * 生产者发送信息
	 * @param message
	 * @param tags
	 * @param sendCallback
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	public void sendMessage(Object message,OrderTagsEnum tags,SendCallback sendCallback)throws MQClientException, RemotingException, InterruptedException;
	
	/**
	 * 生产者发送JSON信息
	 * @param message
	 * @param tags
	 * @param sendCallback
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	public void sendJsonMessage(String message,OrderTagsEnum tags,SendCallback sendCallback)throws MQClientException, RemotingException, InterruptedException;
	
	/**
	 * 生产者发送JSON信息
	 * @param message
	 * @param tags
	 * @return
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	public SendResult sendJsonMessage(String message,OrderTagsEnum tags)throws MQClientException, RemotingException, InterruptedException,MQBrokerException;
	
	/**
	 * 关闭生产者
	 */
	public void closeProducer();
	
	/**
	 * 查询结果
	 * @param sendResult 
	 * @return 结果bean
	 */
	public ResultBean queryReturnResult(SendResult sendResult);
}
