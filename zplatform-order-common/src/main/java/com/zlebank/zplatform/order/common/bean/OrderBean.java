/* 
 * OrderBean.java  
 * 
 * version TODO
 *
 * 2015年8月27日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.bean;

import java.io.Serializable;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月27日 上午10:41:06
 * @since 
 */
public class OrderBean implements Serializable{
    private static final long serialVersionUID = -7317360443683683832L;
    private String version="";
    private String encoding="";
    private String certId="";
    private String frontUrl="";
    private String backUrl="";
    private String signature="";
    private String signMethod="";
    private String coopInstiId="";
    private String merId="";
    private String merName="";
    private String merAbbr="";
    private String orderId="";
    private String txnType="";
    private String txnSubType="";
    private String bizType="";
    private String channelType="";
    private String accessType="";
    private String txnTime="";
    private String accType="";
    private String accNo="";
    private String txnAmt="";
    private String currencyCode="";
    private String customerInfo="";
    private String orderTimeout="";
    private String payTimeout="";
    private String reqReserved="";
    private String reserved="";
    private String riskRateInfo="";
    private String encryptCertId="";
    private String frontFailUrl="";
    private String instalTransInfo="";
    private String defaultPayType="";
    private String issInsCode="";
    private String supPayType="";
    private String userMac="";
    private String customerIp="";
    private String cardTransData="";
    private String orderDesc="";
    private String memberId="";
    private String productcode;
    
    //商品信息
    private String goodsname;//商品名称
    private Long goodsnum;//商品数量
    private String goodscode;//商品代码
    private String goodsdescr;//商品描述
    private String goodstype;//商品类型
    private Long goodsprice;//商户价格
    
    
    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }
    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     * @return the certId
     */
    public String getCertId() {
        return certId;
    }
    /**
     * @param certId the certId to set
     */
    public void setCertId(String certId) {
        this.certId = certId;
    }
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
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }
    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
    /**
     * @return the signMethod
     */
    public String getSignMethod() {
        return signMethod;
    }
    /**
     * @param signMethod the signMethod to set
     */
    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
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
     * @return the customerInfo
     */
    public String getCustomerInfo() {
        return customerInfo;
    }
    /**
     * @param customerInfo the customerInfo to set
     */
    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }
    /**
     * @return the orderTimeout
     */
    public String getOrderTimeout() {
        return orderTimeout;
    }
    /**
     * @param orderTimeout the orderTimeout to set
     */
    public void setOrderTimeout(String orderTimeout) {
        this.orderTimeout = orderTimeout;
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
     * @return the riskRateInfo
     */
    public String getRiskRateInfo() {
        return riskRateInfo;
    }
    /**
     * @param riskRateInfo the riskRateInfo to set
     */
    public void setRiskRateInfo(String riskRateInfo) {
        this.riskRateInfo = riskRateInfo;
    }
    /**
     * @return the encryptCertId
     */
    public String getEncryptCertId() {
        return encryptCertId;
    }
    /**
     * @param encryptCertId the encryptCertId to set
     */
    public void setEncryptCertId(String encryptCertId) {
        this.encryptCertId = encryptCertId;
    }
    /**
     * @return the frontFailUrl
     */
    public String getFrontFailUrl() {
        return frontFailUrl;
    }
    /**
     * @param frontFailUrl the frontFailUrl to set
     */
    public void setFrontFailUrl(String frontFailUrl) {
        this.frontFailUrl = frontFailUrl;
    }
    /**
     * @return the instalTransInfo
     */
    public String getInstalTransInfo() {
        return instalTransInfo;
    }
    /**
     * @param instalTransInfo the instalTransInfo to set
     */
    public void setInstalTransInfo(String instalTransInfo) {
        this.instalTransInfo = instalTransInfo;
    }
    /**
     * @return the defaultPayType
     */
    public String getDefaultPayType() {
        return defaultPayType;
    }
    /**
     * @param defaultPayType the defaultPayType to set
     */
    public void setDefaultPayType(String defaultPayType) {
        this.defaultPayType = defaultPayType;
    }
    /**
     * @return the issInsCode
     */
    public String getIssInsCode() {
        return issInsCode;
    }
    /**
     * @param issInsCode the issInsCode to set
     */
    public void setIssInsCode(String issInsCode) {
        this.issInsCode = issInsCode;
    }
    /**
     * @return the supPayType
     */
    public String getSupPayType() {
        return supPayType;
    }
    /**
     * @param supPayType the supPayType to set
     */
    public void setSupPayType(String supPayType) {
        this.supPayType = supPayType;
    }
    /**
     * @return the userMac
     */
    public String getUserMac() {
        return userMac;
    }
    /**
     * @param userMac the userMac to set
     */
    public void setUserMac(String userMac) {
        this.userMac = userMac;
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
     * @return the cardTransData
     */
    public String getCardTransData() {
        return cardTransData;
    }
    /**
     * @param cardTransData the cardTransData to set
     */
    public void setCardTransData(String cardTransData) {
        this.cardTransData = cardTransData;
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
	 * @return the productcode
	 */
	public String getProductcode() {
		return productcode;
	}
	/**
	 * @param productcode the productcode to set
	 */
	public void setProductcode(String productcode) {
		this.productcode = productcode;
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
	 * @return the goodsname
	 */
	public String getGoodsname() {
		return goodsname;
	}
	/**
	 * @param goodsname the goodsname to set
	 */
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	/**
	 * @return the goodsnum
	 */
	public Long getGoodsnum() {
		return goodsnum;
	}
	/**
	 * @param goodsnum the goodsnum to set
	 */
	public void setGoodsnum(Long goodsnum) {
		this.goodsnum = goodsnum;
	}
	/**
	 * @return the goodscode
	 */
	public String getGoodscode() {
		return goodscode;
	}
	/**
	 * @param goodscode the goodscode to set
	 */
	public void setGoodscode(String goodscode) {
		this.goodscode = goodscode;
	}
	/**
	 * @return the goodsdescr
	 */
	public String getGoodsdescr() {
		return goodsdescr;
	}
	/**
	 * @param goodsdescr the goodsdescr to set
	 */
	public void setGoodsdescr(String goodsdescr) {
		this.goodsdescr = goodsdescr;
	}
	/**
	 * @return the goodstype
	 */
	public String getGoodstype() {
		return goodstype;
	}
	/**
	 * @param goodstype the goodstype to set
	 */
	public void setGoodstype(String goodstype) {
		this.goodstype = goodstype;
	}
	/**
	 * @return the goodsprice
	 */
	public Long getGoodsprice() {
		return goodsprice;
	}
	/**
	 * @param goodsprice the goodsprice to set
	 */
	public void setGoodsprice(Long goodsprice) {
		this.goodsprice = goodsprice;
	}
	
    
}
