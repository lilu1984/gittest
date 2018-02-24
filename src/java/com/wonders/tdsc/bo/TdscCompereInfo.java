package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscCompereInfo implements Serializable {

    /** identifier field */
    private String compereId;

    /** nullable persistent field */
    private String compereName;

    /** nullable persistent field */
    private String compereGender;

    /** nullable persistent field */
    private Integer compereAge;

    /** nullable persistent field */
    private String comperePhone;

    /** nullable persistent field */
    private String compereAddress;

    /** nullable persistent field */
    private String compereEmail;

    /** nullable persistent field */
    private String validity;

    /** nullable persistent field */
    private String memo;
    
    private String compereIdentityCard;
    
    private Integer certificateNumber;
    
    private Date certificateIssueDate;  

    /** full constructor */
    public TdscCompereInfo(String compereName, String compereGender, Integer compereAge, String comperePhone, String compereAddress, String compereEmail, String validity, String memo) {
        this.compereName = compereName;
        this.compereGender = compereGender;
        this.compereAge = compereAge;
        this.comperePhone = comperePhone;
        this.compereAddress = compereAddress;
        this.compereEmail = compereEmail;
        this.validity = validity;
        this.memo = memo;
    }

    /** default constructor */
    public TdscCompereInfo() {
    }

    public String getCompereId() {
        return this.compereId;
    }

    public void setCompereId(String compereId) {
        this.compereId = compereId;
    }

    public String getCompereName() {
        return this.compereName;
    }

    public void setCompereName(String compereName) {
        this.compereName = compereName;
    }

    public String getCompereGender() {
        return this.compereGender;
    }

    public void setCompereGender(String compereGender) {
        this.compereGender = compereGender;
    }

    public Integer getCompereAge() {
        return this.compereAge;
    }

    public void setCompereAge(Integer compereAge) {
        this.compereAge = compereAge;
    }

    public String getComperePhone() {
        return this.comperePhone;
    }

    public void setComperePhone(String comperePhone) {
        this.comperePhone = comperePhone;
    }

    public String getCompereAddress() {
        return this.compereAddress;
    }

    public void setCompereAddress(String compereAddress) {
        this.compereAddress = compereAddress;
    }

    public String getCompereEmail() {
        return this.compereEmail;
    }

    public void setCompereEmail(String compereEmail) {
        this.compereEmail = compereEmail;
    }

    public String getValidity() {
        return this.validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compereId", getCompereId())
            .toString();
    }

    public Date getCertificateIssueDate() {
        return certificateIssueDate;
    }

    public void setCertificateIssueDate(Date certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public String getCompereIdentityCard() {
        return compereIdentityCard;
    }

    public void setCompereIdentityCard(String compereIdentityCard) {
        this.compereIdentityCard = compereIdentityCard;
    }

    public Integer getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(Integer certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

}
