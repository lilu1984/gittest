package com.wonders.tdsc.bo;

import java.util.Date;


/**
 * TdscPlanView entity. @author MyEclipse Persistence Tools
 */

public class TdscPlanView  implements java.io.Serializable {


    // Fields    

     private String blockNoticeNo;
     private String blockName;
     private String transferMode;
     private String recMatStartDate;
     private String recMatEndDate;
     private String noticeStartDate;
     private String noticeEndDate;
     private String issueStartDate;
     private String issueEndDate;
     private String getFileStartDate;
     private String getFileEndDate;
     private String inspDate;
     private String inspLoc;
     private String gatherDate;
     private String answerDate;
     private String answerLoc;
     private String relFaqDate;
     private String accAppStatDate;
     private String accAppEndDate;
     private String reviewDate;
     private String reviewLoc;
     private String tenderStartDate;
     private String tenderEndDate;
     private String openingDate;
     private String openingLoc;
     private String bidEvaDate;
     private String bidEvaLoc;
     private String auctionDate;
     private String auctionLoc;
     private String listStartDate;
     private String listEndDate;
     private String sceBidDate;
     private String sceBidLoc;
     private String marginEndDate;


    // Constructors

    /** default constructor */
    public TdscPlanView() {
    }

    
    /** full constructor */
    public TdscPlanView(String blockName, String transferMode, String recMatStartDate, String recMatEndDate, String noticeStartDate, String noticeEndDate, String issueStartDate, String issueEndDate, String getFileStartDate, String getFileEndDate, String inspDate, String inspLoc, String gatherDate, String answerDate, String answerLoc, String relFaqDate, String accAppStatDate, String accAppEndDate, String reviewDate, String reviewLoc, String tenderStartDate, String tenderEndDate, String openingDate, String openingLoc, String bidEvaDate, String bidEvaLoc, String auctionDate, String auctionLoc, String listStartDate, String listEndDate, String sceBidDate, String sceBidLoc, String marginEndDate) {
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
        this.marginEndDate = marginEndDate;
    }

   
    // Property accessors

    public String getBlockNoticeNo() {
        return this.blockNoticeNo;
    }
    
    public void setBlockNoticeNo(String blockNoticeNo) {
        this.blockNoticeNo = blockNoticeNo;
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

    public String getRecMatStartDate() {
        return this.recMatStartDate;
    }
    
    public void setRecMatStartDate(String recMatStartDate) {
        this.recMatStartDate = recMatStartDate;
    }

    public String getRecMatEndDate() {
        return this.recMatEndDate;
    }
    
    public void setRecMatEndDate(String recMatEndDate) {
        this.recMatEndDate = recMatEndDate;
    }

    public String getNoticeStartDate() {
        return this.noticeStartDate;
    }
    
    public void setNoticeStartDate(String noticeStartDate) {
        this.noticeStartDate = noticeStartDate;
    }

    public String getNoticeEndDate() {
        return this.noticeEndDate;
    }
    
    public void setNoticeEndDate(String noticeEndDate) {
        this.noticeEndDate = noticeEndDate;
    }

    public String getIssueStartDate() {
        return this.issueStartDate;
    }
    
    public void setIssueStartDate(String issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public String getIssueEndDate() {
        return this.issueEndDate;
    }
    
    public void setIssueEndDate(String issueEndDate) {
        this.issueEndDate = issueEndDate;
    }

    public String getGetFileStartDate() {
        return this.getFileStartDate;
    }
    
    public void setGetFileStartDate(String getFileStartDate) {
        this.getFileStartDate = getFileStartDate;
    }

    public String getGetFileEndDate() {
        return this.getFileEndDate;
    }
    
    public void setGetFileEndDate(String getFileEndDate) {
        this.getFileEndDate = getFileEndDate;
    }

    public String getInspDate() {
        return this.inspDate;
    }
    
    public void setInspDate(String inspDate) {
        this.inspDate = inspDate;
    }

    public String getInspLoc() {
        return this.inspLoc;
    }
    
    public void setInspLoc(String inspLoc) {
        this.inspLoc = inspLoc;
    }

    public String getGatherDate() {
        return this.gatherDate;
    }
    
    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }

    public String getAnswerDate() {
        return this.answerDate;
    }
    
    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswerLoc() {
        return this.answerLoc;
    }
    
    public void setAnswerLoc(String answerLoc) {
        this.answerLoc = answerLoc;
    }

    public String getRelFaqDate() {
        return this.relFaqDate;
    }
    
    public void setRelFaqDate(String relFaqDate) {
        this.relFaqDate = relFaqDate;
    }

    public String getAccAppStatDate() {
        return this.accAppStatDate;
    }
    
    public void setAccAppStatDate(String accAppStatDate) {
        this.accAppStatDate = accAppStatDate;
    }

    public String getAccAppEndDate() {
        return this.accAppEndDate;
    }
    
    public void setAccAppEndDate(String accAppEndDate) {
        this.accAppEndDate = accAppEndDate;
    }

    public String getReviewDate() {
        return this.reviewDate;
    }
    
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewLoc() {
        return this.reviewLoc;
    }
    
    public void setReviewLoc(String reviewLoc) {
        this.reviewLoc = reviewLoc;
    }

    public String getTenderStartDate() {
        return this.tenderStartDate;
    }
    
    public void setTenderStartDate(String tenderStartDate) {
        this.tenderStartDate = tenderStartDate;
    }

    public String getTenderEndDate() {
        return this.tenderEndDate;
    }
    
    public void setTenderEndDate(String tenderEndDate) {
        this.tenderEndDate = tenderEndDate;
    }

    public String getOpeningDate() {
        return this.openingDate;
    }
    
    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getOpeningLoc() {
        return this.openingLoc;
    }
    
    public void setOpeningLoc(String openingLoc) {
        this.openingLoc = openingLoc;
    }

    public String getBidEvaDate() {
        return this.bidEvaDate;
    }
    
    public void setBidEvaDate(String bidEvaDate) {
        this.bidEvaDate = bidEvaDate;
    }

    public String getBidEvaLoc() {
        return this.bidEvaLoc;
    }
    
    public void setBidEvaLoc(String bidEvaLoc) {
        this.bidEvaLoc = bidEvaLoc;
    }

    public String getAuctionDate() {
        return this.auctionDate;
    }
    
    public void setAuctionDate(String auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getAuctionLoc() {
        return this.auctionLoc;
    }
    
    public void setAuctionLoc(String auctionLoc) {
        this.auctionLoc = auctionLoc;
    }

    public String getListStartDate() {
        return this.listStartDate;
    }
    
    public void setListStartDate(String listStartDate) {
        this.listStartDate = listStartDate;
    }

    public String getListEndDate() {
        return this.listEndDate;
    }
    
    public void setListEndDate(String listEndDate) {
        this.listEndDate = listEndDate;
    }

    public String getSceBidDate() {
        return this.sceBidDate;
    }
    
    public void setSceBidDate(String sceBidDate) {
        this.sceBidDate = sceBidDate;
    }

    public String getSceBidLoc() {
        return this.sceBidLoc;
    }
    
    public void setSceBidLoc(String sceBidLoc) {
        this.sceBidLoc = sceBidLoc;
    }

    public String getMarginEndDate() {
        return this.marginEndDate;
    }
    
    public void setMarginEndDate(String marginEndDate) {
        this.marginEndDate = marginEndDate;
    }
   








}