package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscSpecialistInfo implements Serializable {

    /** identifier field */
    private String specialistId;

    /** nullable persistent field */
    private String specialistName;

    /** nullable persistent field */
    private Blob photo;

    /** nullable persistent field */
    private String sex;

    /** nullable persistent field */
    private Date birthday;

    /** nullable persistent field */
    private Date regTime;

    /** nullable persistent field */
    private String corpName;

    /** nullable persistent field */
    private String corpAddress;

    /** nullable persistent field */
    private String corpCharacter;

    /** nullable persistent field */
    private String corpPost;

    /** nullable persistent field */
    private String corpLeader;

    /** nullable persistent field */
    private String trade;

    /** nullable persistent field */
    private String technicalPost;

    /** nullable persistent field */
    private String homeAddress;

    /** nullable persistent field */
    private String homePost;

    /** nullable persistent field */
    private String phone;

    /** nullable persistent field */
    private String mobilphone;

    /** nullable persistent field */
    private String email;

    /** nullable persistent field */
    private String certificateType;

    /** nullable persistent field */
    private String certificateNumber;

    /** nullable persistent field */
    private String auditingRecord;

    /** nullable persistent field */
    private String archivesId;

    /** nullable persistent field */
    private String memo;

    /** nullable persistent field */
    private String meeting;

    /** nullable persistent field */
    private String gradeResult;

    /** nullable persistent field */
    private String revisitedState;

    /** nullable persistent field */
    private String specialistType;

    /** full constructor */
    public TdscSpecialistInfo(String specialistId, String specialistName, Blob photo, String sex, Date birthday, Date regTime, String corpName, String corpAddress, String corpCharacter, String corpPost, String corpLeader, String trade, String technicalPost, String homeAddress, String homePost, String phone, String mobilphone, String email, String certificateType, String certificateNumber, String auditingRecord, String archivesId, String memo, String meeting, String gradeResult, String revisitedState, String specialistType) {
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.photo = photo;
        this.sex = sex;
        this.birthday = birthday;
        this.regTime = regTime;
        this.corpName = corpName;
        this.corpAddress = corpAddress;
        this.corpCharacter = corpCharacter;
        this.corpPost = corpPost;
        this.corpLeader = corpLeader;
        this.trade = trade;
        this.technicalPost = technicalPost;
        this.homeAddress = homeAddress;
        this.homePost = homePost;
        this.phone = phone;
        this.mobilphone = mobilphone;
        this.email = email;
        this.certificateType = certificateType;
        this.certificateNumber = certificateNumber;
        this.auditingRecord = auditingRecord;
        this.archivesId = archivesId;
        this.memo = memo;
        this.meeting = meeting;
        this.gradeResult = gradeResult;
        this.revisitedState = revisitedState;
        this.specialistType = specialistType;
    }

    /** default constructor */
    public TdscSpecialistInfo() {
    }

    /** minimal constructor */
    public TdscSpecialistInfo(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getSpecialistId() {
        return this.specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getSpecialistName() {
        return this.specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public Blob getPhoto() {
        return this.photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegTime() {
        return this.regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpAddress() {
        return this.corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public String getCorpCharacter() {
        return this.corpCharacter;
    }

    public void setCorpCharacter(String corpCharacter) {
        this.corpCharacter = corpCharacter;
    }

    public String getCorpPost() {
        return this.corpPost;
    }

    public void setCorpPost(String corpPost) {
        this.corpPost = corpPost;
    }

    public String getCorpLeader() {
        return this.corpLeader;
    }

    public void setCorpLeader(String corpLeader) {
        this.corpLeader = corpLeader;
    }

    public String getTrade() {
        return this.trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getTechnicalPost() {
        return this.technicalPost;
    }

    public void setTechnicalPost(String technicalPost) {
        this.technicalPost = technicalPost;
    }

    public String getHomeAddress() {
        return this.homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHomePost() {
        return this.homePost;
    }

    public void setHomePost(String homePost) {
        this.homePost = homePost;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilphone() {
        return this.mobilphone;
    }

    public void setMobilphone(String mobilphone) {
        this.mobilphone = mobilphone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return this.certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getAuditingRecord() {
        return this.auditingRecord;
    }

    public void setAuditingRecord(String auditingRecord) {
        this.auditingRecord = auditingRecord;
    }

    public String getArchivesId() {
        return this.archivesId;
    }

    public void setArchivesId(String archivesId) {
        this.archivesId = archivesId;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMeeting() {
        return this.meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getGradeResult() {
        return this.gradeResult;
    }

    public void setGradeResult(String gradeResult) {
        this.gradeResult = gradeResult;
    }

    public String getRevisitedState() {
        return this.revisitedState;
    }

    public void setRevisitedState(String revisitedState) {
        this.revisitedState = revisitedState;
    }

    public String getSpecialistType() {
        return specialistType;
    }
    
    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("specialistId", getSpecialistId())
            .toString();
    }

}
