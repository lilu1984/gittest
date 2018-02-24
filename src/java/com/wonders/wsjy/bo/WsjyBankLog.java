package com.wonders.wsjy.bo;

import java.io.Serializable;
import java.util.Date;

public class WsjyBankLog implements Serializable{
	
	private String logId;
	private String logBankIp;
	private String logBankXml;
	private Date logDate;
	private String logType;
	private String logUserId;
	private String logRecallXmll;
	public String getLogRecallXmll() {
		return logRecallXmll;
	}
	public void setLogRecallXmll(String logRecallXmll) {
		this.logRecallXmll = logRecallXmll;
	}
	public String getLogResult() {
		return logResult;
	}
	public void setLogResult(String logResult) {
		this.logResult = logResult;
	}
	private String logResult;
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getLogBankIp() {
		return logBankIp;
	}
	public void setLogBankIp(String logBankIp) {
		this.logBankIp = logBankIp;
	}
	public String getLogBankXml() {
		return logBankXml;
	}
	public void setLogBankXml(String logBankXml) {
		this.logBankXml = logBankXml;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogUserId() {
		return logUserId;
	}
	public void setLogUserId(String logUserId) {
		this.logUserId = logUserId;
	}
}
