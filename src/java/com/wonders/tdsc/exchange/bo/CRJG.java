package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CRJG implements Serializable {

	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** ���ù����	NOITCE_NO */
    private String noitceNo;
    
    /** �ɽ�ʱ��	RESULT_DATE */
    private Timestamp resultDate;
    
    /** ������	RESULT_NAME */
    private String resultName;
    
    /** �ɽ���	RESULT_PRICE */
    private BigDecimal resultPrice;
    
    /** ���÷�ʽ	TRANSFER_MODE */
    private String transferMode;

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

	public Timestamp getResultDate() {
		return resultDate;
	}

	public void setResultDate(Timestamp resultDate) {
		this.resultDate = resultDate;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public BigDecimal getResultPrice() {
		return resultPrice;
	}

	public void setResultPrice(BigDecimal resultPrice) {
		this.resultPrice = resultPrice;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

}
