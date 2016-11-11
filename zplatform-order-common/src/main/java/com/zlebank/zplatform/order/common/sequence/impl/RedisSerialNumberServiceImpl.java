/* 
 * RedisSerialNumberServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.sequence.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.order.common.sequence.SerialNumberService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午3:50:14
 * @since
 */
@Service("redisSerialNumberService")
public class RedisSerialNumberServiceImpl implements SerialNumberService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private static final String TXNSEQNO_KEY="SEQUENCE:TXNSEQNO";
	private static final String TN_KEY="SEQUENCE:TN";
	private static final String REFUND_KEY="SEQUENCE:REFUND";

	public String generateTxnseqno() {
		String seqNo = formateSequence(TXNSEQNO_KEY);
		return seqNo.substring(0, 6) + "99" + seqNo.substring(6);
	}

	public String generateTN(String memberId) {
		String seqNo = formateSequence(TN_KEY);
		return seqNo.substring(0, 6) + memberId.substring(11) + seqNo.substring(6);
	}
	
	public String formateSequence(String key){
		/*BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps(key);
		Long increment = boundValueOps.increment(1);
		if(increment>=99999999){
			boundValueOps.set("0");
		}
		DecimalFormat df = new DecimalFormat("00000000");
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String seqNo = sdf.format(new Date()) + df.format(increment);
		return seqNo;*/
		
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		Long increment = opsForValue.increment(key, 1);
		if(increment>=99999999){
			opsForValue.set(key, "0");
		}
		DecimalFormat df = new DecimalFormat("00000000");
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String seqNo = sdf.format(new Date()) + df.format(increment);
		return seqNo;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String generateRefundNo() {
		String seqNo = formateSequence(REFUND_KEY);
		return seqNo.substring(0, 6) + "40" + seqNo.substring(6);
	}
}
