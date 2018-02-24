package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockScheduleTable implements Serializable {

    /** identifier field */
    private String scheduleId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String blockName;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private Timestamp recMatStartDate;

    /** nullable persistent field */
    private Timestamp recMatEndDate;

    /** nullable persistent field */
    private Timestamp noticeStartDate;

    /** nullable persistent field */
    private Timestamp noticeEndDate;

    /** nullable persistent field */
    private Timestamp issueStartDate;

    /** nullable persistent field */
    private Timestamp issueEndDate;

    /** nullable persistent field */
    private Timestamp getFileStartDate;

    /** nullable persistent field */
    private Timestamp getFileEndDate;

    /** nullable persistent field */
    private Timestamp inspDate;

    /** nullable persistent field */
    private String inspLoc;

    /** nullable persistent field */
    private Timestamp gatherDate;

    /** nullable persistent field */
    private Timestamp answerDate;

    /** nullable persistent field */
    private String answerLoc;

    /** nullable persistent field */
    private Timestamp relFaqDate;

    /** nullable persistent field */
    private Timestamp accAppStatDate;

    /** nullable persistent field */
    private Timestamp accAppEndDate;

    /** nullable persistent field */
    private Timestamp reviewDate;

    /** nullable persistent field */
    private String reviewLoc;

    /** nullable persistent field */
    private Timestamp tenderStartDate;

    /** nullable persistent field */
    private Timestamp tenderEndDate;

    /** nullable persistent field */
    private Timestamp openingDate;

    /** nullable persistent field */
    private String openingLoc;

    /** nullable persistent field */
    private Timestamp bidEvaDate;

    /** nullable persistent field */
    private String bidEvaLoc;

    /** nullable persistent field */
    private Timestamp auctionDate;

    /** nullable persistent field */
    private String auctionLoc;

    /** nullable persistent field */
    private Timestamp listStartDate;

    /** nullable persistent field */
    private Timestamp listEndDate;

    /** nullable persistent field */
    private Timestamp sceBidDate;

    /** nullable persistent field */
    private String sceBidLoc;

    /** nullable persistent field */
    private Timestamp resultShowDate;
    
    private String listLoc;
    
    public String getListLoc() {
        return listLoc;
    }

    public void setListLoc(String listLoc) {
        this.listLoc = listLoc;
    }

    /** full constructor */
    public TdscBlockScheduleTable(String scheduleId, String appId, String blockName, String transferMode, Timestamp recMatStartDate, Timestamp recMatEndDate, Timestamp noticeStartDate, Timestamp noticeEndDate, Timestamp issueStartDate, Timestamp issueEndDate, Timestamp getFileStartDate, Timestamp getFileEndDate, Timestamp inspDate, String inspLoc, Timestamp gatherDate, Timestamp answerDate, String answerLoc, Timestamp relFaqDate, Timestamp accAppStatDate, Timestamp accAppEndDate, Timestamp reviewDate, String reviewLoc, Timestamp tenderStartDate, Timestamp tenderEndDate, Timestamp openingDate, String openingLoc, Timestamp bidEvaDate, String bidEvaLoc, Timestamp auctionDate, String auctionLoc, Timestamp listStartDate, Timestamp listEndDate, Timestamp sceBidDate, String sceBidLoc, Timestamp resultShowDate,String listLoc) {
        this.scheduleId = scheduleId;
        this.appId = appId;
        this.blockName = blockName;
        this.transferMode = transferMode;
        this.recMatStartDate = recMatStartDate;
        this.recMatEndDate = recMatEndDate;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.issueStartDate = issueStartDate;
        this.issueEndDate = issueEndDate;
        this.getFileStartDate = getFileStartDate;
        this.getFileEndDate = getFileEndDate;
        this.inspDate = inspDate;
        this.inspLoc = inspLoc;
        this.gatherDate = gatherDate;
        this.answerDate = answerDate;
        this.answerLoc = answerLoc;
        this.relFaqDate = relFaqDate;
        this.accAppStatDate = accAppStatDate;
        this.accAppEndDate = accAppEndDate;
        this.reviewDate = reviewDate;
        this.reviewLoc = reviewLoc;
        this.tenderStartDate = tenderStartDate;
        this.tenderEndDate = tenderEndDate;
        this.openingDate = openingDate;
        this.openingLoc = openingLoc;
        this.bidEvaDate = bidEvaDate;
        this.bidEvaLoc = bidEvaLoc;
        this.auctionDate = auctionDate;
        this.auctionLoc = auctionLoc;
        this.listStartDate = listStartDate;
        this.listEndDate = listEndDate;
        this.sceBidDate = sceBidDate;
        this.sceBidLoc = sceBidLoc;
        this.resultShowDate = resultShowDate;
        this.listLoc = listLoc;
    }

    /** default constructor */
    public TdscBlockScheduleTable() {
    }

    /** minimal constructor */
    public TdscBlockScheduleTable(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public Timestamp getRecMatStartDate() {
        return this.recMatStartDate;
    }

    public void setRecMatStartDate(Timestamp recMatStartDate) {
        this.recMatStartDate = recMatStartDate;
    }

    public Timestamp getRecMatEndDate() {
        return this.recMatEndDate;
    }

    public void setRecMatEndDate(Timestamp recMatEndDate) {
        this.recMatEndDate = recMatEndDate;
    }

    public Timestamp getNoticeStartDate() {
        return this.noticeStartDate;
    }

    public void setNoticeStartDate(Timestamp noticeStartDate) {
        this.noticeStartDate = noticeStartDate;
    }

    public Timestamp getNoticeEndDate() {
        return this.noticeEndDate;
    }

    public void setNoticeEndDate(Timestamp noticeEndDate) {
        this.noticeEndDate = noticeEndDate;
    }

    public Timestamp getIssueStartDate() {
        return this.issueStartDate;
    }

    public void setIssueStartDate(Timestamp issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public Timestamp getIssueEndDate() {
        return this.issueEndDate;
    }

    public void setIssueEndDate(Timestamp issueEndDate) {
        this.issueEndDate = issueEndDate;
    }

    public Timestamp getGetFileStartDate() {
        return this.getFileStartDate;
    }

    public void setGetFileStartDate(Timestamp getFileStartDate) {
        this.getFileStartDate = getFileStartDate;
    }

    public Timestamp getGetFileEndDate() {
        return this.getFileEndDate;
    }

    public void setGetFileEndDate(Timestamp getFileEndDate) {
        this.getFileEndDate = getFileEndDate;
    }

    public Timestamp getInspDate() {
        return this.inspDate;
    }

    public void setInspDate(Timestamp inspDate) {
        this.inspDate = inspDate;
    }

    public String getInspLoc() {
        return this.inspLoc;
    }

    public void setInspLoc(String inspLoc) {
        this.inspLoc = inspLoc;
    }

    public Timestamp getGatherDate() {
        return this.gatherDate;
    }

    public void setGatherDate(Timestamp gatherDate) {
        this.gatherDate = gatherDate;
    }

    public Timestamp getAnswerDate() {
        return this.answerDate;
    }

    public void setAnswerDate(Timestamp answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswerLoc() {
        return this.answerLoc;
    }

    public void setAnswerLoc(String answerLoc) {
        this.answerLoc = answerLoc;
    }

    public Timestamp getRelFaqDate() {
        return this.relFaqDate;
    }

    public void setRelFaqDate(Timestamp relFaqDate) {
        this.relFaqDate = relFaqDate;
    }

    public Timestamp getAccAppStatDate() {
        return this.accAppStatDate;
    }

    public void setAccAppStatDate(Timestamp accAppStatDate) {
        this.accAppStatDate = accAppStatDate;
    }

    public Timestamp getAccAppEndDate() {
        return this.accAppEndDate;
    }

    public void setAccAppEndDate(Timestamp accAppEndDate) {
        this.accAppEndDate = accAppEndDate;
    }

    public Timestamp getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewLoc() {
        return this.reviewLoc;
    }

    public void setReviewLoc(String reviewLoc) {
        this.reviewLoc = reviewLoc;
    }

    public Timestamp getTenderStartDate() {
        return this.tenderStartDate;
    }

    public void setTenderStartDate(Timestamp tenderStartDate) {
        this.tenderStartDate = tenderStartDate;
    }

    public Timestamp getTenderEndDate() {
        return this.tenderEndDate;
    }

    public void setTenderEndDate(Timestamp tenderEndDate) {
        this.tenderEndDate = tenderEndDate;
    }

    public Timestamp getOpeningDate() {
        return this.openingDate;
    }

    public void setOpeningDate(Timestamp openingDate) {
        this.openingDate = openingDate;
    }

    public String getOpeningLoc() {
        return this.openingLoc;
    }

    public void setOpeningLoc(String openingLoc) {
        this.openingLoc = openingLoc;
    }

    public Timestamp getBidEvaDate() {
        return this.bidEvaDate;
    }

    public void setBidEvaDate(Timestamp bidEvaDate) {
        this.bidEvaDate = bidEvaDate;
    }

    public String getBidEvaLoc() {
        return this.bidEvaLoc;
    }

    public void setBidEvaLoc(String bidEvaLoc) {
        this.bidEvaLoc = bidEvaLoc;
    }

    public Timestamp getAuctionDate() {
        return this.auctionDate;
    }

    public void setAuctionDate(Timestamp auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getAuctionLoc() {
        return this.auctionLoc;
    }

    public void setAuctionLoc(String auctionLoc) {
        this.auctionLoc = auctionLoc;
    }

    public Timestamp getListStartDate() {
        return this.listStartDate;
    }

    public void setListStartDate(Timestamp listStartDate) {
        this.listStartDate = listStartDate;
    }

    public Timestamp getListEndDate() {
        return this.listEndDate;
    }

    public void setListEndDate(Timestamp listEndDate) {
        this.listEndDate = listEndDate;
    }

    public Timestamp getSceBidDate() {
        return this.sceBidDate;
    }

    public void setSceBidDate(Timestamp sceBidDate) {
        this.sceBidDate = sceBidDate;
    }

    public String getSceBidLoc() {
        return this.sceBidLoc;
    }

    public void setSceBidLoc(String sceBidLoc) {
        this.sceBidLoc = sceBidLoc;
    }

    public Timestamp getResultShowDate() {
        return resultShowDate;
    }
    
    public void setResultShowDate(Timestamp resultShowDate) {
        this.resultShowDate = resultShowDate;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("scheduleId", getScheduleId())
            .toString();
    }

}
