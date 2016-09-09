/* 
 * CommonOrderServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年9月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.impl;

import org.springframework.stereotype.Service;

import com.zlebank.zplatform.order.common.bean.OrderBean;
import com.zlebank.zplatform.order.common.exception.CommonException;
import com.zlebank.zplatform.order.service.CommonOrderService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月9日 下午4:13:25
 * @since 
 */
@Service("commonOrderService")
public class CommonOrderServiceImpl implements CommonOrderService{

	/**
	 *
	 * @param orderBean
	 * @return
	 * @throws CommonException
	 */
	@Override
	public String verifySecondPay(OrderBean orderBean) throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param orderNo
	 * @param txntime
	 * @param amount
	 * @param merchId
	 * @param memberId
	 * @throws CommonException
	 */
	@Override
	public void verifyRepeatOrder(String orderNo, String txntime,
			String amount, String merchId, String memberId)
			throws CommonException {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param orderBean
	 * @throws CommonException
	 */
	@Override
	public void verifyBusiness(OrderBean orderBean) throws CommonException {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param merchant
	 * @param coopInsti
	 * @throws CommonException
	 */
	@Override
	public void verifyMerchantAndCoopInsti(String merchant, String coopInsti)
			throws CommonException {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param orderBean
	 * @throws CommonException
	 */
	@Override
	public void validateBusiness(OrderBean orderBean) throws CommonException {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param merchant
	 * @param memberId
	 * @throws CommonException
	 */
	@Override
	public void checkBusiAcct(String merchant, String memberId)
			throws CommonException {
		// TODO Auto-generated method stub
		
	}

}
