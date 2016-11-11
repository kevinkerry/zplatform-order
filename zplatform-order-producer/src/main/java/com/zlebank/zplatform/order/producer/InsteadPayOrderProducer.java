/* 
 * InsteadPayOrderProducer.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.google.common.base.Charsets;
import com.zlebank.zplatform.order.producer.bean.ResultBean;
import com.zlebank.zplatform.order.producer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.producer.interfaces.Producer;
import com.zlebank.zplatform.order.producer.redis.RedisFactory;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 下午3:18:41
 * @since 
 */
public class InsteadPayOrderProducer implements Producer {
	private final static Logger logger = LoggerFactory.getLogger(SimpleOrderProducer.class);
	private static final String KEY = "INSTEADPAYORDER:";
	private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("producer_order");
	//RocketMQ消费者客户端
	private DefaultMQProducer producer;
	//主题
	private String topic;
	
	public InsteadPayOrderProducer(String namesrvAddr) throws MQClientException{
		logger.info("【初始化InsteadPayOrderProducer】");
		logger.info("【namesrvAddr】"+namesrvAddr);          
		producer = new DefaultMQProducer(RESOURCE.getString("insteadpay.order.producer.group"));
		producer.setNamesrvAddr(namesrvAddr);
		Random random = new Random();
        producer.setInstanceName(RESOURCE.getString("insteadpay.order.instancename")+random.nextInt(9999));
        topic = RESOURCE.getString("insteadpay.order.subscribe");
        logger.info("【初始化InsteadPayOrderProducer结束】");
	}
	/**
	 *
	 * @param message
	 * @param tags
	 * @param sendCallback
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	@Override
	public void sendMessage(Object message, OrderTagsEnum tags,
			SendCallback sendCallback) throws MQClientException,
			RemotingException, InterruptedException {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param message
	 * @param tags
	 * @param sendCallback
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	@Override
	public void sendJsonMessage(String message, OrderTagsEnum tags,
			SendCallback sendCallback) throws MQClientException,
			RemotingException, InterruptedException {
		// TODO Auto-generated method stub

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
			throws MQClientException, RemotingException, InterruptedException,
			MQBrokerException {
		if(producer==null){
			throw new MQClientException(-1,"InsteadPayOrderProducer为空");
		}
		producer.start();
		Message msg = new Message(topic, tags.getCode(), message.getBytes(Charsets.UTF_8));
		SendResult sendResult = producer.send(msg);
		return sendResult;
	}

	/**
	 *
	 */
	@Override
	public void closeProducer() {
		// TODO Auto-generated method stub
		producer.shutdown();
		producer = null;
	}

	/**
	 *
	 * @param sendResult
	 * @return
	 */
	@Override
	public ResultBean queryReturnResult(SendResult sendResult) {
		logger.info("【InsteadPayOrderProducer receive Result message】{}",JSON.toJSONString(sendResult));
		logger.info("msgID:{}",sendResult.getMsgId());
		
		for (int i = 0;i<1;i++) {
			String tn = getTnByCycle(sendResult.getMsgId());
			logger.info("从redis中取得key【{}】值为{}",KEY+sendResult.getMsgId(),tn);
			if(StringUtils.isNotEmpty(tn)){
				ResultBean resultBean = JSON.parseObject(tn, ResultBean.class);
				
				logger.info("msgID:{},结果数据:{}",sendResult.getMsgId(),JSON.toJSONString(resultBean));
				return resultBean;
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		logger.info("end time {}",System.currentTimeMillis());
		return null;
	}
	private String getTnByCycle(String msgId){
		Jedis jedis = RedisFactory.getInstance().getRedis();
		//String tn = jedis.get(KEY+msgId);
		List<String> brpop = jedis.brpop(40000, KEY+msgId);
		String tn = brpop.get(1);
		jedis.close();
		if(StringUtils.isNotEmpty(tn)){
			return tn;
		}
		return null;
	}
}
