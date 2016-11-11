/* 
 * SerialNumberService.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.sequence;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午3:49:23
 * @since 
 */
public interface SerialNumberService {

	/**
	 * 生成交易序列号
	 * @return
	 */
	public String generateTxnseqno();
	
	/**
	 * 生成TN序列号
	 * @param memberId
	 * @return
	 */
	public String generateTN(String memberId);
	
	/**
	 * 生成退款订单号
	 * @return
	 */
	public String generateRefundNo();
}
