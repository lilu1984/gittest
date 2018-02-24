package com.wonders.tdsc.publishinfo.web.form;

import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class TdscBlockContractInfoForm extends ActionForm{

	/** CONTRACK_INFO_ID */
    private String contractInfoId;

    /** CONTRACK_SIGN_DATE */
    private Timestamp contractSignDate;
    
    /** ELECTRIC_NUM */
    private String electricNum;
    
    /** CONTRACK_NUM */
    private String contractNum;
    
    /** ACCEPT_PERSON */
    private String acceptPerson;
    
    /** BLOCK_REVIEW_DATE */
    private Timestamp blockReviewDate;
    
    /** TRADE_PAYMENT_DEMAND */
    private String tradePaymentDemand;
    
    /** PROJECT_START_DATE */
    private Timestamp projectStartDate;    
    
    /** PROJECT_END_DATE */
    private Timestamp projectEndDate;   
    
    /** CONTRACK_FILE_URL */
    private String contractFileUrl;
    
    /** CONTRACK_FILE_NAME */
    private String contractFileName;
    
    /** TRADE_NUM */
    private String tradeNum;
    
    /** ZONGDI_NUM */
    private String zongDiNum;
    
    /** ACTION_DATE */
    private Timestamp actionDate;   
    
    /** ACTION_USER */
    private String actionUser;
    
    /** IF_VALIDITY */
    private String ifValidity;
    
    private String blockId;
    
    /** nullable persistent field */
	private String blockName;
	
	/** nullable persistent field */
	private String transferMode;
	
	/** 地块坐落 */
	private String landLocation;
	
	//上传文件
    private FormFile file;

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getContractInfoId() {
		return contractInfoId;
	}

	public void setContractInfoId(String contractInfoId) {
		this.contractInfoId = contractInfoId;
	}

	public Timestamp getContractSignDate() {
		return contractSignDate;
	}

	public void setContractSignDate(Timestamp contractSignDate) {
		this.contractSignDate = contractSignDate;
	}

	public String getElectricNum() {
		return electricNum;
	}

	public void setElectricNum(String electricNum) {
		this.electricNum = electricNum;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(String acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	public Timestamp getBlockReviewDate() {
		return blockReviewDate;
	}

	public void setBlockReviewDate(Timestamp blockReviewDate) {
		this.blockReviewDate = blockReviewDate;
	}

	public String getTradePaymentDemand() {
		return tradePaymentDemand;
	}

	public void setTradePaymentDemand(String tradePaymentDemand) {
		this.tradePaymentDemand = tradePaymentDemand;
	}

	public Timestamp getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(Timestamp projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public Timestamp getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(Timestamp projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public String getContractFileUrl() {
		return contractFileUrl;
	}

	public void setContractFileUrl(String contractFileUrl) {
		this.contractFileUrl = contractFileUrl;
	}

	public String getContractFileName() {
		return contractFileName;
	}

	public void setContractFileName(String contractFileName) {
		this.contractFileName = contractFileName;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getZongDiNum() {
		return zongDiNum;
	}

	public void setZongDiNum(String zongDiNum) {
		this.zongDiNum = zongDiNum;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getIfValidity() {
		return ifValidity;
	}

	public void setIfValidity(String ifValidity) {
		this.ifValidity = ifValidity;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}
}
