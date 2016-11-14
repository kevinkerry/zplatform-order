/* 
 * InsteadPayOrderBean.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.bean;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 上午8:44:59
 * @since
 */
public class InsteadPayOrderBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8986669943007549989L;
	/**
	 *  交易类型
	 */
	@NotEmpty(message = "param.empty.txnType")
	@Length(max = 2, message = "param.error.txnType")
	private String txnType;
	/**
	 *  交易子类
	 */
	@NotEmpty(message = "param.empty.txnSubType")
	@Length(max = 2, message = "param.error.txnSubType")
	private String txnSubType;
	/**
	 *  产品类型
	 */
	@NotEmpty(message = "param.empty.bizType")
	@Length(max = 6, message = "param.error.bizType")
	private String bizType;
	/** 
	 * 渠道类型
	 */
	@Length(max = 2, message = "param.error.channelType")
	private String channelType;
	/** 
	 * 通知地址
	 */
	@Length(max = 256, message = "param.error.channelType")
	private String backUrl;
	/** 
	 * 订单发送时间
	 */
	@NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
	private String txnTime;
	/** 
	 * 商户代码
	 */
	@NotEmpty(message = "param.empty.merId")
	@Length(max = 15, message = "param.error.merId")
	private String merId;
	/** 
	 * 商户订单号
	 */
	@NotEmpty(message = "param.empty.orderId")
	@Length(max = 40, message = "param.error.orderId")
	private String orderId;
	/** 
	 * 交易币种
	 */
	@Length(max = 3, message = "param.error.currencyCode")
	private String currencyCode;
	/** 
	 * 金额
	 */
	@NotEmpty(message = "param.empty.txnAmt")
	@Length(max = 12, message = "param.error.txnAmt")
	private String txnAmt;
	/** 
	 * 账号类型
	 */
	@NotEmpty(message = "param.empty.accType")
	@Length(max = 2, message = "param.error.accType")
	private String accType;
	/** 
	 * 账号
	 */
	@NotEmpty(message = "param.empty.accNo")
	@Length(max = 40, message = "param.error.accNo")
	private String accNo;
	/** 
	 * 户名
	 */
	@NotEmpty(message = "param.empty.accName")
	@Length(max = 64, message = "param.error.accName")
	private String accName;
	/** 
	 * 开户行代码
	 */
	@Length(max = 11, message = "param.error.bankCode")
	private String bankCode;
	/** 
	 * 开户行省
	 */
	private String issInsProvince;
	/** 
	 * 开户行市
	 */
	private String issInsCity;
	/** 
	 * 开户行名称
	 */
	private String issInsName;
	/** 
	 * 证件类型
	 */
	@Length(max = 2, message = "param.error.certifTp")
	private String certifTp;
	/** 
	 * 证件号码
	 */
	@Length(max = 20, message = "param.error.certifId")
	private String certifId;
	/** 
	 * 手机号
	 */
	@Length(max = 11, message = "param.error.phoneNo")
	private String phoneNo;
	
	/**
	 * 合作机构代码
	 */
	@NotEmpty(message = "param.empty.coopInstiId")
	@Length(max = 15, message = "param.error.coopInstiId")
	private String coopInstiId;
	/**
	 * 附言
	 */
	@Length(max = 100, message = "param.error.notes")
	private String notes;
	
	private String cardType;
	
	private String bankCode_DB;
	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	/**
	 * @return the txnSubType
	 */
	public String getTxnSubType() {
		return txnSubType;
	}
	/**
	 * @param txnSubType the txnSubType to set
	 */
	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}
	/**
	 * @return the bizType
	 */
	public String getBizType() {
		return bizType;
	}
	/**
	 * @param bizType the bizType to set
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}
	/**
	 * @param channelType the channelType to set
	 */
	public void setChannelType(String channelType) {
		this.channelType = channelType;
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
	 * @return the accType
	 */
	public String getAccType() {
		return accType;
	}
	/**
	 * @param accType the accType to set
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return accNo;
	}
	/**
	 * @param accNo the accNo to set
	 */
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	/**
	 * @return the accName
	 */
	public String getAccName() {
		return accName;
	}
	/**
	 * @param accName the accName to set
	 */
	public void setAccName(String accName) {
		this.accName = accName;
	}
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the issInsProvince
	 */
	public String getIssInsProvince() {
		return issInsProvince;
	}
	/**
	 * @param issInsProvince the issInsProvince to set
	 */
	public void setIssInsProvince(String issInsProvince) {
		this.issInsProvince = issInsProvince;
	}
	/**
	 * @return the issInsCity
	 */
	public String getIssInsCity() {
		return issInsCity;
	}
	/**
	 * @param issInsCity the issInsCity to set
	 */
	public void setIssInsCity(String issInsCity) {
		this.issInsCity = issInsCity;
	}
	/**
	 * @return the issInsName
	 */
	public String getIssInsName() {
		return issInsName;
	}
	/**
	 * @param issInsName the issInsName to set
	 */
	public void setIssInsName(String issInsName) {
		this.issInsName = issInsName;
	}
	/**
	 * @return the certifTp
	 */
	public String getCertifTp() {
		return certifTp;
	}
	/**
	 * @param certifTp the certifTp to set
	 */
	public void setCertifTp(String certifTp) {
		this.certifTp = certifTp;
	}
	/**
	 * @return the certifId
	 */
	public String getCertifId() {
		return certifId;
	}
	/**
	 * @param certifId the certifId to set
	 */
	public void setCertifId(String certifId) {
		this.certifId = certifId;
	}
	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}
	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * @return the bankCode_DB
	 */
	public String getBankCode_DB() {
		return bankCode_DB;
	}
	/**
	 * @param bankCode_DB the bankCode_DB to set
	 */
	public void setBankCode_DB(String bankCode_DB) {
		this.bankCode_DB = bankCode_DB;
	}
	
	
}
