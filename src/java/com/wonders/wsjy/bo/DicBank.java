package com.wonders.wsjy.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class DicBank implements Serializable{
	private String dicCode;
	private String dicValue;
	private BigDecimal dicNum;
	private String dicDescribe;
	private String isValidity;
	private String bankIp;
	private String bankPort;
	private String bankIcon;
	private BigDecimal bankNum;
	public String getBankIcon() {
		return bankIcon;
	}
	public void setBankIcon(String bankIcon) {
		this.bankIcon = bankIcon;
	}
	public String getBankIp() {
		return bankIp;
	}
	public void setBankIp(String bankIp) {
		this.bankIp = bankIp;
	}
	public BigDecimal getBankNum() {
		return bankNum;
	}
	public void setBankNum(BigDecimal bankNum) {
		this.bankNum = bankNum;
	}
	public String getBankPort() {
		return bankPort;
	}
	public void setBankPort(String bankPort) {
		this.bankPort = bankPort;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	public String getDicDescribe() {
		return dicDescribe;
	}
	public void setDicDescribe(String dicDescribe) {
		this.dicDescribe = dicDescribe;
	}
	public BigDecimal getDicNum() {
		return dicNum;
	}
	public void setDicNum(BigDecimal dicNum) {
		this.dicNum = dicNum;
	}
	public String getDicValue() {
		return dicValue;
	}
	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}
	public String getIsValidity() {
		return isValidity;
	}
	public void setIsValidity(String isValidity) {
		this.isValidity = isValidity;
	}
	
	
}
