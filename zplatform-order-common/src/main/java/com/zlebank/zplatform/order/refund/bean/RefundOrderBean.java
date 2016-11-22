/* 
 * RefundOrderBean.java  
 * 
 * version TODO
 *
 * 2016年11月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.refund.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zlebank.zplatform.order.bean.BaseOrderBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月11日 下午1:46:43
 * @since
 */
public class RefundOrderBean extends BaseOrderBean {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2058796550085892852L;
	/**
	 * 合作机构
	 */
	@NotEmpty(message = "param.empty.coopInstiId")
	@Length(max = 15, message = "param.error.coopInstiId")
	private String coopInstiId;
	/**
	 * 二级商户
	 */
	@NotEmpty(message = "param.empty.merId")
	@Length(max = 15, message = "param.error.merId")
	private String merId;
	/**
	 * 会员ID
	 */
	@NotEmpty(message = "param.empty.memberId")
	@Length(max = 15, message = "param.error.memberId")
	private String memberId;
	/**
	 *  提交时间
	 */
	@NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
	private String txnTime;
	/**
	 *  商户订单号
	 */
	@NotEmpty(message = "param.empty.orderId")
	@Length(max = 32, message = "param.error.orderId")
	private String orderId;
	
	/**
	 *  接入类型
	 */
	private String accessType;
	/**
	 *  原始商户订单号
	 */
	@Length(max = 32, message = "param.error.origOrderId")
	private String origOrderId;
	/**
	 *  原始受理订单号
	 */
	@Length(max = 32, message = "param.error.origTN")
	private String origTN;
	/**
	 *  交易金额
	 */
	@NotEmpty(message = "param.empty.txnAmt")
	@Length(max = 32, message = "param.error.txnAmt")
	private String txnAmt;
	/**
	 *  退款描述
	 */
	private String orderDesc;
	/**
	 *  退款方式
	 */
	private String refundType;
	/**
	 *  请求方保留域
	 */
	private String reqReserved;
	/**
	 * @return the coopInstiId
	 */
	public String getCoopInstiId() {
		return coopInstiId;
	}
	/**
	 * @param coopInstiId the coopInstiId to set
	 */
	public void setCoopInstiId(String coopInstiId) {
		this.coopInstiId = coopInstiId;
	}
	/**
	 * @return the merId
	 */
	public String getMerId() {
		return merId;
	}
	/**
	 * @param merId the merId to set
	 */
	public void setMerId(String merId) {
		this.merId = merId;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the txnTime
	 */
	public String getTxnTime() {
		return txnTime;
	}
	/**
	 * @param txnTime the txnTime to set
	 */
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}
	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	/**
	 * @return the origOrderId
	 */
	public String getOrigOrderId() {
		return origOrderId;
	}
	/**
	 * @param origOrderId the origOrderId to set
	 */
	public void setOrigOrderId(String origOrderId) {
		this.origOrderId = origOrderId;
	}
	/**
	 * @return the origTN
	 */
	public String getOrigTN() {
		return origTN;
	}
	/**
	 * @param origTN the origTN to set
	 */
	public void setOrigTN(String origTN) {
		this.origTN = origTN;
	}
	/**
	 * @return the txnAmt
	 */
	public String getTxnAmt() {
		return txnAmt;
	}
	/**
	 * @param txnAmt the txnAmt to set
	 */
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	/**
	 * @return the orderDesc
	 */
	public String getOrderDesc() {
		return orderDesc;
	}
	/**
	 * @param orderDesc the orderDesc to set
	 */
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	/**
	 * @return the refundType
	 */
	public String getRefundType() {
		return refundType;
	}
	/**
	 * @param refundType the refundType to set
	 */
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	/**
	 * @return the reqReserved
	 */
	public String getReqReserved() {
		return reqReserved;
	}
	/**
	 * @param reqReserved the reqReserved to set
	 */
	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	
}
