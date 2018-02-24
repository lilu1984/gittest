package com.wonders.tdsc.out.service.bean;

import java.util.ArrayList;

public class StData {
	 private StNoticeTableData noticeData;
	 private ArrayList subDataList;
	public StNoticeTableData getNoticeData() {
		return noticeData;
	}
	public void setNoticeData(StNoticeTableData noticeData) {
		this.noticeData = noticeData;
	}
	public ArrayList getSubDataList() {
		return subDataList;
	}
	public void setSubDataList(ArrayList subDataList) {
		this.subDataList = subDataList;
	}
}