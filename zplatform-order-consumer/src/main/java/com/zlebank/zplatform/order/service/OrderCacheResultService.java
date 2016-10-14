/* 
 * OrderCacheResultService.java  
 * 
 * version TODO
 *
 * 2016年9月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月20日 上午10:08:49
 * @since 
 */
public interface OrderCacheResultService {

	/**
	 * 将订单的TN保存到缓存中
	 * @param key
	 * @param json
	 */
	public void saveConsumeOrderOfTN(String key,String json);
}
