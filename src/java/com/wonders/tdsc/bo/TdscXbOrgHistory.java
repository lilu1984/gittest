package com.wonders.tdsc.bo;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

/**
 * TdscXbOrgHistory entity.
 * 
 * @author wondersInfomation
 */

public class TdscXbOrgHistory extends BaseBO {

	// Fields

	private String historyId;

	private String tradeNum;

	private String planId;

	private String orgAppId;

	private String orgName;

	private String orgZhucr;

	private String xctkNote;

	private Double xctkScore;

	private String hczcNote;

	private Double hczcScore;

	private String gzphNote;

	private Double gzphScore;

	private String qtfwNote;

	private Double qtfwScore;

	private String bzPmyj;

	private String zhNote;

	private Double zhScore;

	private Date acitonDate;

	private String orgInfoId;
	
	private String wtHetongNo;
	
	private String ifGoon;
	// Constructors

	public String getWtHetongNo() {
		return wtHetongNo;
	}

	public void setWtHetongNo(String wtHetongNo) {
		this.wtHetongNo = wtHetongNo;
	}

	/** default constructor */
	public TdscXbOrgHistory() {
	}

	/** full constructor */
	public TdscXbOrgHistory(String tradeNum, String planId, String orgAppId,
			String orgName, String orgZhucr, String xctkNote, Double xctkScore,
			String hczcNote, Double hczcScore, String gzphNote,
			Double gzphScore, String qtfwNote, Double qtfwScore, String bzPmyj,
			String zhNote, Double zhScore, Date acitonDate, String orgInfoId) {
		this.tradeNum = tradeNum;
		this.planId = planId;
		this.orgAppId = orgAppId;
		this.orgName = orgName;
		this.orgZhucr = orgZhucr;
		this.xctkNote = xctkNote;
		this.xctkScore = xctkScore;
		this.hczcNote = hczcNote;
		this.hczcScore = hczcScore;
		this.gzphNote = gzphNote;
		this.gzphScore = gzphScore;
		this.qtfwNote = qtfwNote;
		this.qtfwScore = qtfwScore;
		this.bzPmyj = bzPmyj;
		this.zhNote = zhNote;
		this.zhScore = zhScore;
		this.acitonDate = acitonDate;
		this.orgInfoId = orgInfoId;
	}

	// Property accessors

	public String getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getTradeNum() {
		return this.tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getOrgAppId() {
		return this.orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgZhucr() {
		return this.orgZhucr;
	}

	public void setOrgZhucr(String orgZhucr) {
		this.orgZhucr = orgZhucr;
	}

	public String getXctkNote() {
		return this.xctkNote;
	}

	public void setXctkNote(String xctkNote) {
		this.xctkNote = xctkNote;
	}

	public Double getXctkScore() {
		return this.xctkScore;
	}

	public void setXctkScore(Double xctkScore) {
		this.xctkScore = xctkScore;
	}

	public String getHczcNote() {
		return this.hczcNote;
	}

	public void setHczcNote(String hczcNote) {
		this.hczcNote = hczcNote;
	}

	public Double getHczcScore() {
		return this.hczcScore;
	}

	public void setHczcScore(Double hczcScore) {
		this.hczcScore = hczcScore;
	}

	public String getGzphNote() {
		return this.gzphNote;
	}

	public void setGzphNote(String gzphNote) {
		this.gzphNote = gzphNote;
	}

	public Double getGzphScore() {
		return this.gzphScore;
	}

	public void setGzphScore(Double gzphScore) {
		this.gzphScore = gzphScore;
	}

	public String getQtfwNote() {
		return this.qtfwNote;
	}

	public void setQtfwNote(String qtfwNote) {
		this.qtfwNote = qtfwNote;
	}

	public Double getQtfwScore() {
		return this.qtfwScore;
	}

	public void setQtfwScore(Double qtfwScore) {
		this.qtfwScore = qtfwScore;
	}

	public String getBzPmyj() {
		return this.bzPmyj;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public String getZhNote() {
		return this.zhNote;
	}

	public void setZhNote(String zhNote) {
		this.zhNote = zhNote;
	}

	public Double getZhScore() {
		return this.zhScore;
	}

	public void setZhScore(Double zhScore) {
		this.zhScore = zhScore;
	}

	public Date getAcitonDate() {
		return this.acitonDate;
	}

	public void setAcitonDate(Date acitonDate) {
		this.acitonDate = acitonDate;
	}

	public String getOrgInfoId() {
		return this.orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public String getIfGoon() {
		return ifGoon;
	}

	public void setIfGoon(String ifGoon) {
		this.ifGoon = ifGoon;
	}

}