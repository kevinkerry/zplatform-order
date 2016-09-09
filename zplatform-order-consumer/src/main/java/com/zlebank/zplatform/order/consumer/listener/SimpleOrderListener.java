/* 
 * SimpleOrderListener.java  
 * 
 * version TODO
 *
 * 2016年9月8日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.consumer.listener;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Charsets;
import com.zlebank.zplatform.order.consumer.enums.OrderTagsEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月8日 下午3:00:14
 * @since
 */
@Service("simpleOrderListener")
public class SimpleOrderListener implements MessageListenerConcurrently {
	private static final Logger log = LoggerFactory
			.getLogger(SimpleOrderListener.class);
	private static final ResourceBundle RESOURCE = ResourceBundle
			.getBundle("consumer");

	/**
	 *
	 * @param msgs
	 * @param context
	 * @return
	 */
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeConcurrentlyContext context) {
		for (MessageExt msg : msgs) {

			if (msg.getTopic().equals(
					RESOURCE.getString("simple.order.subscribe"))) {
				OrderTagsEnum orderTagsEnum = OrderTagsEnum.fromValue(msg
						.getTags());
				switch (orderTagsEnum) {
					case COMMONCONSUME_SIMPLIFIED:
						log.info(new String(msg.getBody(), Charsets.UTF_8));
						log.info(msg.getMsgId());
						break;
	
					case PRODUCTCONSUME_SIMPLIFIED:
						log.info(new String(msg.getBody(), Charsets.UTF_8));
						log.info(msg.getMsgId());
						break;
					default:
						break;
				}

			}
			log.info(Thread.currentThread().getName()
					+ " Receive New Messages: " + msgs);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
