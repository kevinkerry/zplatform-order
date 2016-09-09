package com.zlebank.zplatform.order.common.dao.pojo;

import java.util.Date;

/**
 * TProdCase entity. @author MyEclipse Persistence Tools
 */

public class PojoProdCase implements java.io.Serializable {

	// Fields

	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3692222731872281016L;
    private Long caseid;
	private String prdtver;
	private String busicode;
	private String businame;
	private String status;
	private Long inuser;
	private Date intime;
	private Long upuser;
	private Date uptime;
	private String notes;
	private String remarks;

	// Constructors

	/** default constructor */
	public PojoProdCase() {
	}

	/**
	 * @return the caseid
	 */
	public Long getCaseid() {
		return caseid;
	}

	/**
	 * @param caseid the caseid to set
	 */
	public void setCaseid(Long caseid) {
		this.caseid = caseid;
	}

	/**
	 * @return the prdtver
	 */
	public String getPrdtver() {
		return prdtver;
	}

	/**
	 * @param prdtver the prdtver to set
	 */
	public void setPrdtver(String prdtver) {
		this.prdtver = prdtver;
	}

	/**
	 * @return the busicode
	 */
	public String getBusicode() {
		return busicode;
	}

	/**
	 * @param busicode the busicode to set
	 */
	public void setBusicode(String busicode) {
		this.busicode = busicode;
	}

	/**
	 * @return the businame
	 */
	public String getBusiname() {
		return businame;
	}

	/**
	 * @param businame the businame to set
	 */
	public void setBusiname(String businame) {
		this.businame = businame;
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
	 * @return the inuser
	 */
	public Long getInuser() {
		return inuser;
	}

	/**
	 * @param inuser the inuser to set
	 */
	public void setInuser(Long inuser) {
		this.inuser = inuser;
	}

	/**
	 * @return the intime
	 */
	public Date getIntime() {
		return intime;
	}

	/**
	 * @param intime the intime to set
	 */
	public void setIntime(Date intime) {
		this.intime = intime;
	}

	/**
	 * @return the upuser
	 */
	public Long getUpuser() {
		return upuser;
	}

	/**
	 * @param upuser the upuser to set
	 */
	public void setUpuser(Long upuser) {
		this.upuser = upuser;
	}

	/**
	 * @return the uptime
	 */
	public Date getUptime() {
		return uptime;
	}

	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(Date uptime) {
		this.uptime = uptime;
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

	

	

}