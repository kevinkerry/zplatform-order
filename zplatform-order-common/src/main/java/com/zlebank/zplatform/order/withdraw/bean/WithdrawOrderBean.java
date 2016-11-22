/* 
 * WapWithdrawBean.java  
 * 
 * version TODO
 *
 * 2015年10月23日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.withdraw.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zlebank.zplatform.order.bean.BaseOrderBean;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月23日 上午10:30:28
 * @since
 */
public class WithdrawOrderBean extends BaseOrderBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5268876520689075572L;
	/**
	 *  合作机构
	 */
	@NotEmpty(message = "param.empty.coopInstiId")
	@Length(max = 15, message = "param.error.coopInstiId")
	private String coopInstiId;
	/**
	 *  商户
	 */
	@Length(max = 15, message = "param.error.merId")
	private String merId;
	/**
	 *  会员ID
	 */
	@NotEmpty(message = "param.empty.memberId")
	@Length(max = 15, message = "param.error.memberId")
	private String memberId;
	/**
	 *  订单号
	 */
	@NotEmpty(message = "param.empty.orderId")
	@Length(max = 32, message = "param.error.orderId")
	private String orderId;
	/**
	 *  提交时间
	 */
	@NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
	private String txnTime;
	
	/**
	 *  接入类型
	 */
	private String accessType;
	/**
	 *  提现金额
	 */
	@NotEmpty(message = "param.empty.txnAmt")
	@Length(max = 12, message = "param.error.txnAmt")
	private String amount;
	/**
	 *  绑定标识号
	 */
	private String bindId;
	/**
	 *  银行卡信息域
	 */
	private String cardData;
	
	private WithdrawAccBean accBean;
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
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return the bindId
	 */
	public String getBindId() {
		return bindId;
	}
	/**
	 * @param bindId the bindId to set
	 */
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	/**
	 * @return the cardData
	 */
	public String getCardData() {
		return cardData;
	}
	/**
	 * @param cardData the cardData to set
	 */
	public void setCardData(String cardData) {
		this.cardData = cardData;
	}
	/**
	 * @return the accBean
	 */
	public WithdrawAccBean getAccBean() {
		return accBean;
	}
	/**
	 * @param accBean the accBean to set
	 */
	public void setAccBean(WithdrawAccBean accBean) {
		this.accBean = accBean;
	}
	
}
