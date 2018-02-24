package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class BlockTransInfo implements Serializable {

	private String noticeNo;

	private String totalICountPerson;

	private String totalCountPerson;

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

	public String getTotalCountPerson() {
		return totalCountPerson;
	}

	public void setTotalCountPerson(String totalCountPerson) {
		this.totalCountPerson = totalCountPerson;
	}

	public String getTotalICountPerson() {
		return totalICountPerson;
	}

	public void setTotalICountPerson(String totalICountPerson) {
		this.totalICountPerson = totalICountPerson;
	}
	
	

}
