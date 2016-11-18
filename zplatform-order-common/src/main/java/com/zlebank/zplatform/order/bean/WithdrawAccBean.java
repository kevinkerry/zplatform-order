/* 
 * WapWithdrawAccBean.java  
 * 
 * version TODO
 *
 * 2015年10月23日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.bean;

import java.io.Serializable;

import com.zlebank.zplatform.member.individual.bean.QuickpayCustBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月23日 上午10:33:04
 * @since
 */
public class WithdrawAccBean implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8162417102547533932L;
	/**
	 *  收款人账户号
	 */
	private String accNo;
	/**
	 *  收款人账户名
	 */
	private String accName;
	/**
	 *  联行号
	 */
	private String bankCode;
	/**
	 *  银行名称
	 */
	private String bankName;
	/**
	 *  证件类型
	 */
	private String certType;
	/**
	 *  证件号
	 */
	private String certNo;
	/**
	 *  手机号
	 */
	private String phoneNo;

	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return accNo;
	}

	/**
	 * @param accNo
	 *            the accNo to set
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
	 * @param accName
	 *            the accName to set
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
	 * @param bankCode
	 *            the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * @param certType
	 *            the certType to set
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}

	/**
	 * @return the certNo
	 */
	public String getCertNo() {
		return certNo;
	}

	/**
	 * @param certNo
	 *            the certNo to set
	 */
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo
	 *            the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @param accNo
	 * @param accName
	 * @param bankCode
	 * @param bankName
	 * @param certType
	 * @param certNo
	 * @param phoneNo
	 */
	public WithdrawAccBean(QuickpayCustBean quickpayCustBean) {
		super();
		this.accNo = quickpayCustBean.getCardno();
		this.accName = quickpayCustBean.getAccname();
		this.bankCode = quickpayCustBean.getBankcode();
		this.bankName = quickpayCustBean.getBankname();
		this.certType = quickpayCustBean.getIdtype();
		this.certNo = quickpayCustBean.getIdnum();
		this.phoneNo = quickpayCustBean.getPhone();
	}
	
}
