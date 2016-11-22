/* 
 * RechargeOrderBean.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.recharge.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zlebank.zplatform.order.bean.BaseOrderBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 下午2:53:56
 * @since 
 */
public class RechargeOrderBean extends BaseOrderBean{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 316337553246615699L;
	
	@Length(max = 128, message = "param.error.frontUrl")
    private String frontUrl="";
	@Length(max = 128, message = "param.error.backUrl")
    private String backUrl="";
    @NotEmpty(message = "param.empty.coopInstiId")
	@Length(max = 15, message = "param.error.coopInstiId")
    private String coopInstiId="";
	@Length(max = 15, message = "param.error.merId")
    private String merId="";
    private String merName="";
    private String merAbbr="";
    @NotEmpty(message = "param.empty.orderId")
	@Length(max = 32, message = "param.error.orderId")
    private String orderId="";
    @NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
    private String txnTime="";
    @NotEmpty(message = "param.empty.txnAmt")
	@Length(max = 12, message = "param.error.txnAmt")
    private String txnAmt="";
    private String currencyCode="";
    @NotEmpty(message = "param.empty.payTimeout")
	@Length(max = 14, message = "param.error.payTimeout")
    private String payTimeout="";
    private String reqReserved="";
    private String reserved="";
    private String customerIp="";
    private String orderDesc="";
    @NotEmpty(message = "param.empty.memberId")
	@Length(max = 15, message = "param.error.memberId")
    private String memberId="";
	/**
	 * @return the frontUrl
	 */
	public String getFrontUrl() {
		return frontUrl;
	}
	/**
	 * @param frontUrl the frontUrl to set
	 */
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
	/**
	 * @return the backUrl
	 */
	public String getBackUrl() {
		return backUrl;
	}
	/**
	 * @param backUrl the backUrl to set
	 */
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
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
	 * @return the merName
	 */
	public String getMerName() {
		return merName;
	}
	/**
	 * @param merName the merName to set
	 */
	public void setMerName(String merName) {
		this.merName = merName;
	}
	/**
	 * @return the merAbbr
	 */
	public String getMerAbbr() {
		return merAbbr;
	}
	/**
	 * @param merAbbr the merAbbr to set
	 */
	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
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
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the payTimeout
	 */
	public String getPayTimeout() {
		return payTimeout;
	}
	/**
	 * @param payTimeout the payTimeout to set
	 */
	public void setPayTimeout(String payTimeout) {
		this.payTimeout = payTimeout;
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
	/**
	 * @return the reserved
	 */
	public String getReserved() {
		return reserved;
	}
	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	/**
	 * @return the customerIp
	 */
	public String getCustomerIp() {
		return customerIp;
	}
	/**
	 * @param customerIp the customerIp to set
	 */
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
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
    
    
}
