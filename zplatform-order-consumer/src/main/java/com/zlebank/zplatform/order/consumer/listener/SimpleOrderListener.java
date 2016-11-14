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

import org.apache.commons.lang.StringUtils;
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
import com.zlebank.zplatform.order.bean.OrderBean;
import com.zlebank.zplatform.order.bean.RefundOrderBean;
import com.zlebank.zplatform.order.bean.WithdrawBean;
import com.zlebank.zplatform.order.consumer.enums.OrderTagsEnum;
import com.zlebank.zplatform.order.exception.CommonException;
import com.zlebank.zplatform.order.service.ConsumeOrderService;
import com.zlebank.zplatform.order.service.OrderCacheResultService;
import com.zlebank.zplatform.order.service.RefundOrderService;
import com.zlebank.zplatform.order.service.WithdrawOrderService;
import com.zlebank.zplatform.trade.bean.ResultBean;

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
	private static final Logger log = LoggerFactory.getLogger(SimpleOrderListener.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("consumer_order");
	private static final String KEY = "SIMPLEORDER:";

	@Autowired
	private ConsumeOrderService consumeOrderService;
	@Autowired
	private OrderCacheResultService orderCacheResultService;
	@Autowired
	private RefundOrderService refundOrderService;
	@Autowired
	private WithdrawOrderService withdrawOrderService;
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
			if (msg.getTopic().equals(RESOURCE.getString("simple.order.subscribe"))) {
				OrderTagsEnum orderTagsEnum = OrderTagsEnum.fromValue(msg
						.getTags());
				if(orderTagsEnum==OrderTagsEnum.COMMONCONSUME_SIMPLIFIED){
					String json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:{}", json);
					log.info("接收到的MSGID:{}", msg.getMsgId());
					OrderBean orderBean = JSON.parseObject(json,
							OrderBean.class);
					if (orderBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
								msg.getMsgId(), json);
						break;
					}
					String tn = "";
					ResultBean resultBean = null;
					try {
						tn = consumeOrderService.createConsumeOrder(orderBean);
						if (StringUtils.isNotEmpty(tn)) {
							resultBean = new ResultBean(tn);
						}else{
							resultBean = new ResultBean("09","创建订单失败");
						}
					} catch (CommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						resultBean = new ResultBean(e.getCode(),e.getMessage());
					}catch (Throwable e) {
						e.printStackTrace();
						resultBean = new ResultBean("T000",e.getMessage());
					}
					orderCacheResultService.saveConsumeOrderOfTN(KEY
							+ msg.getMsgId(), JSON.toJSONString(resultBean));
				}else if(orderTagsEnum==OrderTagsEnum.REFUND_SIMPLIFIED){
					String json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:{}", json);
					log.info("接收到的MSGID:{}", msg.getMsgId());
					RefundOrderBean orderBean = JSON.parseObject(json,
							RefundOrderBean.class);
					if (orderBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
								msg.getMsgId(), json);
						break;
					}
					
					String tn = "";
					ResultBean resultBean = null;
					try {
						tn = refundOrderService.createRefundOrder(orderBean);
						if (StringUtils.isNotEmpty(tn)) {
							resultBean = new ResultBean(tn);
						}else{
							resultBean = new ResultBean("09","创建订单失败");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						resultBean = new ResultBean("",e.getMessage());
					}catch (Throwable e) {
						e.printStackTrace();
						resultBean = new ResultBean("T000",e.getMessage());
					}
					orderCacheResultService.saveConsumeOrderOfTN(KEY
							+ msg.getMsgId(), JSON.toJSONString(resultBean));
				}else if(orderTagsEnum==OrderTagsEnum.WITHDRAW_SIMPLIFIED) {
					String json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:{}", json);
					log.info("接收到的MSGID:{}", msg.getMsgId());
					WithdrawBean orderBean = JSON.parseObject(json,
							WithdrawBean.class);
					if (orderBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
								msg.getMsgId(), json);
						break;
					}
					String tn = "";
					ResultBean resultBean = null;
					try {
						tn = withdrawOrderService.createIndividualWithdrawOrder(orderBean);
						if (StringUtils.isNotEmpty(tn)) {
							resultBean = new ResultBean(tn);
						}else{
							resultBean = new ResultBean("09","创建订单失败");
						}
					} catch (CommonException e) {
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
