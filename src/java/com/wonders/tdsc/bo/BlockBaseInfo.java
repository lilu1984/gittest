package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class BlockBaseInfo implements Serializable {
	private String noticeNo;

	private List list;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

}
