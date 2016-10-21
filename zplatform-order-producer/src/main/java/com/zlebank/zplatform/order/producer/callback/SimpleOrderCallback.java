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

import java.util.concurrent.atomic.AtomicInteger;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.zlebank.zplatform.order.producer.bean.ResultBean;
import com.zlebank.zplatform.order.producer.redis.RedisFactory;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月7日 下午12:47:01
 * @since 
 */
public class SimpleOrderCallback implements SendCallback{

	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	private final static Logger logger = LoggerFactory.getLogger(SimpleOrderCallback.class);
	private static final String KEY = "SIMPLEORDER:";
	/**
	 *
	 * @param sendResult
	 */
	@Override
	public void onSuccess(SendResult sendResult) {
		logger.info("【SimpleOrderCallback receive Result message】{}",JSON.toJSONString(sendResult));
		logger.info("msgID:{}",sendResult.getMsgId());
		
		for (int i = 0;i<100;i++) {
			String tn = getTnByCycle(sendResult.getMsgId());
			logger.info("从redis中取得key【{}】值为{}",KEY+sendResult.getMsgId(),tn);
			if(StringUtils.isNotEmpty(tn)){
				ResultBean resultBean = JSON.parseObject(tn, ResultBean.class);
				atomicInteger.incrementAndGet();
				logger.info("msgID:{},结果数据:{},总和:{}", new Object[]{sendResult.getMsgId(),JSON.toJSONString(resultBean),atomicInteger});
				break;
			}else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		logger.info("end time {}",System.currentTimeMillis());
		
	}
	
	private String getTnByCycle(String msgId){
		Jedis jedis = RedisFactory.getInstance().getRedis();
		String tn = jedis.get(KEY+msgId);
		jedis.close();
		if(StringUtils.isNotEmpty(tn)){
			return tn;
		}
		return null;
		
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
