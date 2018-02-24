package com.wonders.wsjy.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class WsjyBankApp implements Serializable{
	private String id;
	private String instcode;
	private java.util.Date indate;
	private BigDecimal inamout;
	private String inname;
	private String inacct;
	private String inmemo;
	private String inbankflcode;
	private String result;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInacct() {
		return inacct;
	}
	public void setInacct(String inacct) {
		this.inacct = inacct;
	}
	public BigDecimal getInamout() {
		return inamout;
	}
	public void setInamout(BigDecimal inamout) {
		this.inamout = inamout;
	}
	public String getInbankflcode() {
		return inbankflcode;
	}
	public void setInbankflcode(String inbankflcode) {
		this.inbankflcode = inbankflcode;
	}
	public java.util.Date getIndate() {
		return indate;
	}
	public void setIndate(java.util.Date indate) {
		this.indate = indate;
	}
	public String getInmemo() {
		return inmemo;
	}
	public void setInmemo(String inmemo) {
		this.inmemo = inmemo;
	}
	public String getInname() {
		return inname;
	}
	public void setInname(String inname) {
		this.inname = inname;
	}
	public String getInstcode() {
		return instcode;
	}
	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
