package com.wonders.tdsc.exchange.bo;

import java.util.List;

public class Document {
	public Document() {
		super();
	}
	public String root;
	public List dataList;
	
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
}
