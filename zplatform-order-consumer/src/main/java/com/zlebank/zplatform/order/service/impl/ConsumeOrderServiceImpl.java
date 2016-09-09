/* 
 * ConsumeOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年9月8日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.service.CommonOrderService;
import com.zlebank.zplatform.order.service.ConsumeOrderService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月8日 下午3:35:11
 * @since 
 */
@Service("consumeOrderService")
public class ConsumeOrderServiceImpl implements ConsumeOrderService{

	
	@Autowired
	private CommonOrderService commonOrderService;
	
	
	public void checkOrderInfo(OrderBean orderBean) throws CommonException{
		/* 订单校验流程
		 * 0。验签因为API层已经做了，这里不再进行验签
		 * 1.二次订单支付，如果订单在有效期内返回TN不在有效期内返回异常信息
		 * 2.订单有效性校验，
		 * 3。外部交易类型校验，外部交易代码转换为内部业务代码，如果没有找到对应的业务代码返回异常信息
		 * 4.验证商户产品版本中是否有对应的业务
		 * 5.合作机构和商户有效性校验，如果有商户参与的话，没有则不校验（充值交易无商户参与）
		 * 6.业务校验:充值业务，会员号不得为空或者是999999999999999
		 * 7.校验个人会员，商户的资金账户的状态是否正常，如果不是返回异常信息
		 */
		
		
		commonOrderService.verifyRepeatOrder(orderBean.getOrderId(), orderBean.getTxnTime(), orderBean.getTxnAmt(), orderBean.getMerId(), orderBean.getMemberId());
		
		commonOrderService.verifyBusiness(orderBean);
		
		commonOrderService.verifyMerchantAndCoopInsti(orderBean.getMerId(),orderBean.getCoopInstiId());
		
		commonOrderService.validateBusiness(orderBean);
		
		commonOrderService.checkBusiAcct(orderBean.getMerId(), orderBean.getMemberId());
	}
	
	
	public String createConsumeOrder(OrderBean orderBean){
		String tn = null;
		try {
			//二次支付订单
			tn = commonOrderService.verifySecondPay(orderBean);
			if(StringUtils.isNotEmpty(tn)){
				return tn;
			}
		} catch (CommonException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//订单校验
			checkOrderInfo(orderBean);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//订单生成和交易流水保存
		
		return null;
	}
}
