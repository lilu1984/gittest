package com.wonders.tdsc.bo;

import java.io.Serializable;

public class TdscPublishInterface implements Serializable {

	private String publishId;
	
	/** nullable persistent field */
	private String publishType;
	
	private String interfaceSelect;
	
	
	/** nullable persistent field */
	private String interfaceTables;
	
	private String publishReturnObject;
	
	private String interfaceLimited;
	
	private String interfaceOrder;
	
	private String publishFtpWay;
	
	private String publishObjectPath;

	public String getPublishObjectPath() {
		return publishObjectPath;
	}

	public void setPublishObjectPath(String publishObjectPath) {
		this.publishObjectPath = publishObjectPath;
	}

	public String getInterfaceLimited() {
		return interfaceLimited;
	}

	public void setInterfaceLimited(String interfaceLimited) {
		this.interfaceLimited = interfaceLimited;
	}

	public String getInterfaceOrder() {
		return interfaceOrder;
	}

	public void setInterfaceOrder(String interfaceOrder) {
		this.interfaceOrder = interfaceOrder;
	}

	public String getPublishFtpWay() {
		return publishFtpWay;
	}

	public void setPublishFtpWay(String publishFtpWay) {
		this.publishFtpWay = publishFtpWay;
	}

	public String getInterfaceTables() {
		return interfaceTables;
	}

	public void setInterfaceTables(String interfaceTables) {
		this.interfaceTables = interfaceTables;
	}

	public String getPublishId() {
		return publishId;
	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}

	public String getPublishType() {
		return publishType;
	}

	public void setPublishType(String publishType) {
		this.publishType = publishType;
	}

	public String getPublishReturnObject() {
		return publishReturnObject;
	}

	public void setPublishReturnObject(String publishReturnObject) {
		this.publishReturnObject = publishReturnObject;
	}

	public String getInterfaceSelect() {
		return interfaceSelect;
	}

	public void setInterfaceSelect(String interfaceSelect) {
		this.interfaceSelect = interfaceSelect;
	}
}
