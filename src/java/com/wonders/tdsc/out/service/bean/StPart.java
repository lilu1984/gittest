package com.wonders.tdsc.out.service.bean;

public class StPart {
	private String action = "";
	private String businessType = "";
	private String businessWhere = "";
	private String status = "";
	private String date = "";
	private String totalRecords = "";
	private String pageSize = "";
	private String totalPages = "";
	private String currentPage = "";
	private String message = "";
	private StData stData;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessWhere() {
		return businessWhere;
	}
	public void setBusinessWhere(String businessWhere) {
		this.businessWhere = businessWhere;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public StData getStData() {
		return stData;
	}
	public void setStData(StData stData) {
		this.stData = stData;
	}
	
}
