/* 
 * OrderCacheResultRedisService.java  
 * 
 * version TODO
 *
 * 2016年9月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.order.service.OrderCacheResultService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月20日 上午10:11:34
 * @since 
 */
@Service
public class OrderCacheResultRedisService implements OrderCacheResultService{
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 将消费订单的TN保存到redis中
	 * @param tn
	 */
	@Override
	public void saveConsumeOrderOfTN(String key,String tn) {
		// TODO Auto-generated method stub
		ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(key, tn, 10, TimeUnit.MINUTES);
	}

}
