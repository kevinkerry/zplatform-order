/* 
 * InsteadPayOrderListener.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.consumer.listener;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Charsets;
import com.zlebank.zplatform.order.bean.InsteadPayOrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.consumer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.exception.InsteadPayOrderException;
import com.zlebank.zplatform.order.service.InsteadPayOrderService;
import com.zlebank.zplatform.order.service.OrderCacheResultService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 下午3:22:49
 * @since 
 */
@Service("insteadPayOrderListener")
public class InsteadPayOrderListener implements MessageListenerConcurrently {

	private static final Logger log = LoggerFactory.getLogger(InsteadPayOrderListener.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("consumer_order");
	private static final String KEY = "INSTEADPAYORDER:";
	
	@Autowired
	private OrderCacheResultService orderCacheResultService;
	@Autowired
	private InsteadPayOrderService insteadPayOrderService;
	
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
			if (msg.getTopic().equals(RESOURCE.getString("insteadpay.order.subscribe"))) {
				OrderTagsEnum orderTagsEnum = OrderTagsEnum.fromValue(msg.getTags());
				if(orderTagsEnum==OrderTagsEnum.INSTEADPAY_REALTIME) {
						String json = new String(msg.getBody(), Charsets.UTF_8);
						log.info("接收到的MSG:{}", json);
						log.info("接收到的MSGID:{}", msg.getMsgId());
						InsteadPayOrderBean orderBean = JSON.parseObject(json,
								InsteadPayOrderBean.class);
						if (orderBean == null) {
							log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
									msg.getMsgId(), json);
							break;
						}
						String tn = "";
						ResultBean resultBean = null;
						try {
							resultBean = insteadPayOrderService.createRealTimeOrder(orderBean);
							
						} catch (CommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							resultBean = new ResultBean(e.getCode(),e.getMessage());
						} catch (InsteadPayOrderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							resultBean = new ResultBean(e.getCode(),e.getMessage());
						}catch (Throwable e) {
							e.printStackTrace();
							resultBean = new ResultBean("T000",e.getMessage());
						}
						orderCacheResultService.saveConsumeOrderOfTN(KEY
								+ msg.getMsgId(), JSON.toJSONString(resultBean));
				}
			}
			log.info(Thread.currentThread().getName()
					+ " Receive New Messages: " + msgs);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
