package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CRJG implements Serializable {

	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 出让公告号	NOITCE_NO */
    private String noitceNo;
    
    /** 成交时间	RESULT_DATE */
    private Timestamp resultDate;
    
    /** 竞得人	RESULT_NAME */
    private String resultName;
    
    /** 成交价	RESULT_PRICE */
    private BigDecimal resultPrice;
    
    /** 出让方式	TRANSFER_MODE */
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
