/* 
 * OrderInfoBean.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.bean;

import java.io.Serializable;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 上午11:12:22
 * @since 
 */
public class OrderInfoBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1621040582610343987L;
	private Long id;
    private String institution;
    private String orderno;
    private Long orderamt;
    private Long ordercomm;
    private Long orderfee;
    private String ordercommitime;
    private String firmemberno;
    private String firmembername;
    private String firmembershortname;
    private String secmemberno;
    private String secmembername;
    private String secmembershortname;
    private String goodsname;
    private Long goodsnum;
    private String goodscode;
    private String goodsdescr;
    private String goodstype;
    private Long goodsprice;
    private String fronturl;
    private String backurl;
    private String relatetradetxn;
    private String orderfinshtime;
    private String status;
    private String notes;
    private String remarks;
    private String txntype;
    private String txnsubtype;
    private String biztype;
    //private String merchorderno;
    private String certid;
    private String reqreserved;
    private String reserved;
    private String customerInfo;
    private String tn;
    private String orderdesc;
    private String paytimeout;
    private String payerip;
    private String syncnotify="01";
    private String accesstype;
    private String currencycode="156";
    private String memberid;
    private String productcode;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}
	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	/**
	 * @return the orderno
	 */
	public String getOrderno() {
		return orderno;
	}
	/**
	 * @param orderno the orderno to set
	 */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	/**
	 * @return the orderamt
	 */
	public Long getOrderamt() {
		return orderamt;
	}
	/**
	 * @param orderamt the orderamt to set
	 */
	public void setOrderamt(Long orderamt) {
		this.orderamt = orderamt;
	}
	/**
	 * @return the ordercomm
	 */
	public Long getOrdercomm() {
		return ordercomm;
	}
	/**
	 * @param ordercomm the ordercomm to set
	 */
	public void setOrdercomm(Long ordercomm) {
		this.ordercomm = ordercomm;
	}
	/**
	 * @return the orderfee
	 */
	public Long getOrderfee() {
		return orderfee;
	}
	/**
	 * @param orderfee the orderfee to set
	 */
	public void setOrderfee(Long orderfee) {
		this.orderfee = orderfee;
	}
	/**
	 * @return the ordercommitime
	 */
	public String getOrdercommitime() {
		return ordercommitime;
	}
	/**
	 * @param ordercommitime the ordercommitime to set
	 */
	public void setOrdercommitime(String ordercommitime) {
		this.ordercommitime = ordercommitime;
	}
	/**
	 * @return the firmemberno
	 */
	public String getFirmemberno() {
		return firmemberno;
	}
	/**
	 * @param firmemberno the firmemberno to set
	 */
	public void setFirmemberno(String firmemberno) {
		this.firmemberno = firmemberno;
	}
	/**
	 * @return the firmembername
	 */
	public String getFirmembername() {
		return firmembername;
	}
	/**
	 * @param firmembername the firmembername to set
	 */
	public void setFirmembername(String firmembername) {
		this.firmembername = firmembername;
	}
	/**
	 * @return the firmembershortname
	 */
	public String getFirmembershortname() {
		return firmembershortname;
	}
	/**
	 * @param firmembershortname the firmembershortname to set
	 */
	public void setFirmembershortname(String firmembershortname) {
		this.firmembershortname = firmembershortname;
	}
	/**
	 * @return the secmemberno
	 */
	public String getSecmemberno() {
		return secmemberno;
	}
	/**
	 * @param secmemberno the secmemberno to set
	 */
	public void setSecmemberno(String secmemberno) {
		this.secmemberno = secmemberno;
	}
	/**
	 * @return the secmembername
	 */
	public String getSecmembername() {
		return secmembername;
	}
	/**
	 * @param secmembername the secmembername to set
	 */
	public void setSecmembername(String secmembername) {
		this.secmembername = secmembername;
	}
	/**
	 * @return the secmembershortname
	 */
	public String getSecmembershortname() {
		return secmembershortname;
	}
	/**
	 * @param secmembershortname the secmembershortname to set
	 */
	public void setSecmembershortname(String secmembershortname) {
		this.secmembershortname = secmembershortname;
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
	/**
	 * @return the fronturl
	 */
	public String getFronturl() {
		return fronturl;
	}
	/**
	 * @param fronturl the fronturl to set
	 */
	public void setFronturl(String fronturl) {
		this.fronturl = fronturl;
	}
	/**
	 * @return the backurl
	 */
	public String getBackurl() {
		return backurl;
	}
	/**
	 * @param backurl the backurl to set
	 */
	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}
	/**
	 * @return the relatetradetxn
	 */
	public String getRelatetradetxn() {
		return relatetradetxn;
	}
	/**
	 * @param relatetradetxn the relatetradetxn to set
	 */
	public void setRelatetradetxn(String relatetradetxn) {
		this.relatetradetxn = relatetradetxn;
	}
	/**
	 * @return the orderfinshtime
	 */
	public String getOrderfinshtime() {
		return orderfinshtime;
	}
	/**
	 * @param orderfinshtime the orderfinshtime to set
	 */
	public void setOrderfinshtime(String orderfinshtime) {
		this.orderfinshtime = orderfinshtime;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the txntype
	 */
	public String getTxntype() {
		return txntype;
	}
	/**
	 * @param txntype the txntype to set
	 */
	public void setTxntype(String txntype) {
		this.txntype = txntype;
	}
	/**
	 * @return the txnsubtype
	 */
	public String getTxnsubtype() {
		return txnsubtype;
	}
	/**
	 * @param txnsubtype the txnsubtype to set
	 */
	public void setTxnsubtype(String txnsubtype) {
		this.txnsubtype = txnsubtype;
	}
	/**
	 * @return the biztype
	 */
	public String getBiztype() {
		return biztype;
	}
	/**
	 * @param biztype the biztype to set
	 */
	public void setBiztype(String biztype) {
		this.biztype = biztype;
	}
	/**
	 * @return the certid
	 */
	public String getCertid() {
		return certid;
	}
	/**
	 * @param certid the certid to set
	 */
	public void setCertid(String certid) {
		this.certid = certid;
	}
	/**
	 * @return the reqreserved
	 */
	public String getReqreserved() {
		return reqreserved;
	}
	/**
	 * @param reqreserved the reqreserved to set
	 */
	public void setReqreserved(String reqreserved) {
		this.reqreserved = reqreserved;
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
	 * @return the tn
	 */
	public String getTn() {
		return tn;
	}
	/**
	 * @param tn the tn to set
	 */
	public void setTn(String tn) {
		this.tn = tn;
	}
	/**
	 * @return the orderdesc
	 */
	public String getOrderdesc() {
		return orderdesc;
	}
	/**
	 * @param orderdesc the orderdesc to set
	 */
	public void setOrderdesc(String orderdesc) {
		this.orderdesc = orderdesc;
	}
	/**
	 * @return the paytimeout
	 */
	public String getPaytimeout() {
		return paytimeout;
	}
	/**
	 * @param paytimeout the paytimeout to set
	 */
	public void setPaytimeout(String paytimeout) {
		this.paytimeout = paytimeout;
	}
	/**
	 * @return the payerip
	 */
	public String getPayerip() {
		return payerip;
	}
	/**
	 * @param payerip the payerip to set
	 */
	public void setPayerip(String payerip) {
		this.payerip = payerip;
	}
	/**
	 * @return the syncnotify
	 */
	public String getSyncnotify() {
		return syncnotify;
	}
	/**
	 * @param syncnotify the syncnotify to set
	 */
	public void setSyncnotify(String syncnotify) {
		this.syncnotify = syncnotify;
	}
	/**
	 * @return the accesstype
	 */
	public String getAccesstype() {
		return accesstype;
	}
	/**
	 * @param accesstype the accesstype to set
	 */
	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}
	/**
	 * @return the currencycode
	 */
	public String getCurrencycode() {
		return currencycode;
	}
	/**
	 * @param currencycode the currencycode to set
	 */
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	/**
	 * @return the memberid
	 */
	public String getMemberid() {
		return memberid;
	}
	/**
	 * @param memberid the memberid to set
	 */
	public void setMemberid(String memberid) {
		this.memberid = memberid;
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
    
    
}
