package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.sql.Timestamp;

public class WJBZ implements Serializable {

	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** ���ù����	NOITCE_NO */
    private String noitceNo;
    
    /** ���淢��ʱ��	ISSUE_START_DATE */
    private Timestamp issueStartDate;
    
    /** �����ļ����ۿ�ʼʱ��	GET_FILE_START_DATE */
    private Timestamp getFileStartDate;
    
    /** �����ļ����۽���ʱ��	GET_FILE_END_DATE */
    private Timestamp getFileEndDate;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getProjectGuid() {
		return projectGuid;
	}

	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getNoitceNo() {
		return noitceNo;
	}

	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}

	public Timestamp getIssueStartDate() {
		return issueStartDate;
	}

	public void setIssueStartDate(Timestamp issueStartDate) {
		this.issueStartDate = issueStartDate;
	}

	public Timestamp getGetFileStartDate() {
		return getFileStartDate;
	}

	public void setGetFileStartDate(Timestamp getFileStartDate) {
		this.getFileStartDate = getFileStartDate;
	}

	public Timestamp getGetFileEndDate() {
		return getFileEndDate;
	}

	public void setGetFileEndDate(Timestamp getFileEndDate) {
		this.getFileEndDate = getFileEndDate;
	}
}
