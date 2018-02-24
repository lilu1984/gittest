package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockConInfo implements Serializable {

    /** identifier field */
    private String blockConId;

    /** nullable persistent field */
    private String blockId;

    /** nullable persistent field */
    private String contractNum;

    /** nullable persistent field */
    private Date contractSignedDate;

    /** nullable persistent field */
    private BigDecimal copyNumber;

    /** nullable persistent field */
    private String landUseExpirationDate;

    /** nullable persistent field */
    private BigDecimal remiseAccountReceivable;

    /** nullable persistent field */
    private BigDecimal depositSum;

    /** nullable persistent field */
    private BigDecimal depositeBalance;

    /** nullable persistent field */
    private Date depositeDate;

    /** nullable persistent field */
    private BigDecimal remiseBalance;

    /** nullable persistent field */
    private Date remiseExpirationDate;

    /** nullable persistent field */
    private BigDecimal allocationCompensation;

    /** nullable persistent field */
    private String demolishCompesationMethod;
    
    private String auditedNum;

    /** full constructor */
    public TdscBlockConInfo(String blockConId, String blockId, String contractNum, Date contractSignedDate, BigDecimal copyNumber, String landUseExpirationDate, BigDecimal remiseAccountReceivable, BigDecimal depositSum, BigDecimal depositeBalance, Date depositeDate, BigDecimal remiseBalance, Date remiseExpirationDate, BigDecimal allocationCompensation, String demolishCompesationMethod, String auditedNum) {
        this.blockConId = blockConId;
        this.blockId = blockId;
        this.contractNum = contractNum;
        this.contractSignedDate = contractSignedDate;
        this.copyNumber = copyNumber;
        this.landUseExpirationDate = landUseExpirationDate;
        this.remiseAccountReceivable = remiseAccountReceivable;
        this.depositSum = depositSum;
        this.depositeBalance = depositeBalance;
        this.depositeDate = depositeDate;
        this.remiseBalance = remiseBalance;
        this.remiseExpirationDate = remiseExpirationDate;
        this.allocationCompensation = allocationCompensation;
        this.demolishCompesationMethod = demolishCompesationMethod;
        this.auditedNum = auditedNum;
    }

    /** default constructor */
    public TdscBlockConInfo() {
    }

    /** minimal constructor */
    public TdscBlockConInfo(String blockConId) {
        this.blockConId = blockConId;
    }

    public String getBlockConId() {
        return this.blockConId;
    }

    public void setBlockConId(String blockConId) {
        this.blockConId = blockConId;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getContractNum() {
        return this.contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Date getContractSignedDate() {
        return this.contractSignedDate;
    }

    public void setContractSignedDate(Date contractSignedDate) {
        this.contractSignedDate = contractSignedDate;
    }

    public BigDecimal getCopyNumber() {
        return this.copyNumber;
    }

    public void setCopyNumber(BigDecimal copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getLandUseExpirationDate() {
        return this.landUseExpirationDate;
    }

    public void setLandUseExpirationDate(String landUseExpirationDate) {
        this.landUseExpirationDate = landUseExpirationDate;
    }

    public BigDecimal getRemiseAccountReceivable() {
        return this.remiseAccountReceivable;
    }

    public void setRemiseAccountReceivable(BigDecimal remiseAccountReceivable) {
        this.remiseAccountReceivable = remiseAccountReceivable;
    }

    public BigDecimal getDepositSum() {
        return this.depositSum;
    }

    public void setDepositSum(BigDecimal depositSum) {
        this.depositSum = depositSum;
    }

    public BigDecimal getDepositeBalance() {
        return this.depositeBalance;
    }

    public void setDepositeBalance(BigDecimal depositeBalance) {
        this.depositeBalance = depositeBalance;
    }

    public Date getDepositeDate() {
        return this.depositeDate;
    }

    public void setDepositeDate(Date depositeDate) {
        this.depositeDate = depositeDate;
    }

    public BigDecimal getRemiseBalance() {
        return this.remiseBalance;
    }

    public void setRemiseBalance(BigDecimal remiseBalance) {
        this.remiseBalance = remiseBalance;
    }

    public Date getRemiseExpirationDate() {
        return this.remiseExpirationDate;
    }

    public void setRemiseExpirationDate(Date remiseExpirationDate) {
        this.remiseExpirationDate = remiseExpirationDate;
    }

    public BigDecimal getAllocationCompensation() {
        return this.allocationCompensation;
    }

    public void setAllocationCompensation(BigDecimal allocationCompensation) {
        this.allocationCompensation = allocationCompensation;
    }

    public String getDemolishCompesationMethod() {
        return this.demolishCompesationMethod;
    }

    public void setDemolishCompesationMethod(String demolishCompesationMethod) {
        this.demolishCompesationMethod = demolishCompesationMethod;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("blockConId", getBlockConId())
            .toString();
    }

	public String getAuditedNum() {
		return auditedNum;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

}
