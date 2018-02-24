package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CJQRS implements Serializable {

	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 成交时间	RESULT_Timestamp */
    private Timestamp resultDate;
    
    /** 竞得人	RESULT_NAME */
    private String resultName;
    
    /** 竞得人资格证书编号	RESULT_CERT */
    private String resultCert;
    
    /** 地块编号	BLOCK_CODE */
    private String blockCode;
    
    /** 出让人	REMISE_PERSON */
    private String remisePerson;
    
    /** 起始价	INIT_PRICE */
    private BigDecimal initPrice;
    
    /** 成交价	RESULT_PRICE */
    private BigDecimal resultPrice;

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

	public String getResultCert() {
		return resultCert;
	}

	public void setResultCert(String resultCert) {
		this.resultCert = resultCert;
	}

	public String getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public String getRemisePerson() {
		return remisePerson;
	}

	public void setRemisePerson(String remisePerson) {
		this.remisePerson = remisePerson;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public BigDecimal getResultPrice() {
		return resultPrice;
	}

	public void setResultPrice(BigDecimal resultPrice) {
		this.resultPrice = resultPrice;
	}
}
