package com.wonders.tdsc.xborg.web.form;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

public class TdscXbOrgForm extends ActionForm{

	// tdscXbOrgApp
	private String orgAppId;

	private String orgName;

	private String orgZhucr;

	private String orgLinkMan;

	private String orgLinkPhone;

	private String ifUsed;

	private String memo;

	private String validity;

	private String lotNo;

	// tdscXbOrgHistory

	private String historyId;

	private String tradeNum;

	private String planId;

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

	// TdscXbOrgInfo

	private String batchNo;

	private Date stratDate;

	private BigDecimal nextNo;

	private String[] currentOrgIds;
	
	private String status;
	
	private String wtHetongNo;
	
	private String ifGoon;
	
	private Date queryBeginDate;
	
	private Date queryEndDate;

	public String getIfGoon() {
		return ifGoon;
	}

	public void setIfGoon(String ifGoon) {
		this.ifGoon = ifGoon;
	}

	public String getWtHetongNo() {
		return wtHetongNo;
	}

	public void setWtHetongNo(String wtHetongNo) {
		this.wtHetongNo = wtHetongNo;
	}

	public Date getAcitonDate() {
		return acitonDate;
	}

	public void setAcitonDate(Date acitonDate) {
		this.acitonDate = acitonDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBzPmyj() {
		return bzPmyj;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public String[] getCurrentOrgIds() {
		return currentOrgIds;
	}

	public void setCurrentOrgIds(String[] currentOrgIds) {
		this.currentOrgIds = currentOrgIds;
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

	public String getIfUsed() {
		return ifUsed;
	}

	public void setIfUsed(String ifUsed) {
		this.ifUsed = ifUsed;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getNextNo() {
		return nextNo;
	}

	public void setNextNo(BigDecimal nextNo) {
		this.nextNo = nextNo;
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

	public String getOrgLinkMan() {
		return orgLinkMan;
	}

	public void setOrgLinkMan(String orgLinkMan) {
		this.orgLinkMan = orgLinkMan;
	}

	public String getOrgLinkPhone() {
		return orgLinkPhone;
	}

	public void setOrgLinkPhone(String orgLinkPhone) {
		this.orgLinkPhone = orgLinkPhone;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStratDate() {
		return stratDate;
	}

	public void setStratDate(Date stratDate) {
		this.stratDate = stratDate;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
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
	
	public String getCurrentOrgId() {
		return getArrayString(this.currentOrgIds);
	}
	
	/**
	 * Æ´½Ó×Ö·û´®
	 * @param list
	 * @return
	 */
	public static String getArrayString(String[] strs){
		if (strs == null) return null;
		StringBuffer str = new StringBuffer();
		for(int i = 0 ; i < strs.length; i++){
			if(StringUtils.isNotEmpty(strs[i])) {
				str.append(strs[i]);
				if (i+1 != strs.length )
					str.append(",");
			}
		}
		String stro = str.toString();
		if (stro.endsWith(",")) stro = stro.substring(0, stro.length()-1);
		return stro;
	}

	public Date getQueryBeginDate() {
		return queryBeginDate;
	}

	public void setQueryBeginDate(Date queryBeginDate) {
		this.queryBeginDate = queryBeginDate;
	}

	public Date getQueryEndDate() {
		return queryEndDate;
	}

	public void setQueryEndDate(Date queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

	
}
