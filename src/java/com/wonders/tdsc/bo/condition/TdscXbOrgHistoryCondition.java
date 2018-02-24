package com.wonders.tdsc.bo.condition;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscXbOrgHistoryCondition extends BaseBO {

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
	
	private int currentPage;// Ò³Êý

	public Date getAcitonDate() {
		return acitonDate;
	}

	public void setAcitonDate(Date acitonDate) {
		this.acitonDate = acitonDate;
	}

	public String getBzPmyj() {
		return bzPmyj;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getGzphNote() {
		return gzphNote;
	}

	public void setGzphNote(String gzphNote) {
		this.gzphNote = gzphNote;
	}

	public Double getGzphScore() {
		return gzphScore;
	}

	public void setGzphScore(Double gzphScore) {
		this.gzphScore = gzphScore;
	}

	public String getHczcNote() {
		return hczcNote;
	}

	public void setHczcNote(String hczcNote) {
		this.hczcNote = hczcNote;
	}

	public Double getHczcScore() {
		return hczcScore;
	}

	public void setHczcScore(Double hczcScore) {
		this.hczcScore = hczcScore;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getOrgAppId() {
		return orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public String getOrgInfoId() {
		return orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgZhucr() {
		return orgZhucr;
	}

	public void setOrgZhucr(String orgZhucr) {
		this.orgZhucr = orgZhucr;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getQtfwNote() {
		return qtfwNote;
	}

	public void setQtfwNote(String qtfwNote) {
		this.qtfwNote = qtfwNote;
	}

	public Double getQtfwScore() {
		return qtfwScore;
	}

	public void setQtfwScore(Double qtfwScore) {
		this.qtfwScore = qtfwScore;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getXctkNote() {
		return xctkNote;
	}

	public void setXctkNote(String xctkNote) {
		this.xctkNote = xctkNote;
	}

	public Double getXctkScore() {
		return xctkScore;
	}

	public void setXctkScore(Double xctkScore) {
		this.xctkScore = xctkScore;
	}

	public String getZhNote() {
		return zhNote;
	}

	public void setZhNote(String zhNote) {
		this.zhNote = zhNote;
	}

	public Double getZhScore() {
		return zhScore;
	}

	public void setZhScore(Double zhScore) {
		this.zhScore = zhScore;
	}
	
	
}
