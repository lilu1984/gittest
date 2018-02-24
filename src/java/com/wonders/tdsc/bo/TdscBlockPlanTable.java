package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockPlanTable implements Serializable {

    /** identifier field */
    private String planId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private Integer planNum;

    /** nullable persistent field */
    private String modifyUser;

    /** nullable persistent field */
    private String status;

    /** nullable persistent field */
    private String landId;

    /** nullable persistent field */
    private String blockName;

    /** nullable persistent field */
    private String preNoticeNo;

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
    
    /** nullable persistent field */
    private Integer tenderMeetingNo;
    
    /** nullable persistent field */
    private Integer openingMeetingNo;
    
    /** nullable persistent field */
    private Integer bidEvaMeetingNo;
    
    /** nullable persistent field */
    private Integer auctionMeetingNo;
    
    /** nullable persistent field */
    private Integer listMeetingNo;
    
    /** nullable persistent field */
    private Integer sceBidMeetingNo;

    private String listLoc;
    
    /** nullable persistent field */
    private String ifSendGuard; 
    
    /** nullable persistent field */
    private String uniteBlockCode; 

    /** nullable persistent field */
    private String ifPCommit; 
    
    /** nullable persistent field */
    private String statusFlow;

    /** nullable persistent field */
    private Timestamp lastActionDate;
    
    /**招拍挂编号 */
    private String tradeNum;
    
    /** 实施方案iweb文档编号 RECORD_ID*/
    private String recordId;
	
    //协办机构 BZ_XBJG
    private String bzXbjg;     
    
    //挂牌手续费 BZ_GPSXF
    private String bzGpsxf;     
    
    //拍卖佣金 BZ_PMYJ
    private String bzPmyj;      
    
    //备注 BZ_BEIZHU
    private String bzBeizhu; 

    /** 竞买资格确认截止时间 */
    private Timestamp jmzgqrEndDate;
    
    /** nullable persistent field */
    private String landLocation;
    
    private String orgAppId;
    
    private String userId;
    
    private String ifOnLine;
    
    private Timestamp onLineStatDate;
    
    private Timestamp onLineEndDate;
    
  //最高限价竞买人摇号时间
    private Timestamp bidderRollDate;
    
	public Timestamp getOnLineEndDate() {
		return onLineEndDate;
	}

	public void setOnLineEndDate(Timestamp onLineEndDate) {
		this.onLineEndDate = onLineEndDate;
	}

	public Timestamp getOnLineStatDate() {
		return onLineStatDate;
	}

	public void setOnLineStatDate(Timestamp onLineStatDate) {
		this.onLineStatDate = onLineStatDate;
	}

	public String getIfOnLine() {
		return ifOnLine;
	}

	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgAppId() {
		return orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public Timestamp getJmzgqrEndDate() {
		return jmzgqrEndDate;
	}

	public void setJmzgqrEndDate(Timestamp jmzgqrEndDate) {
		this.jmzgqrEndDate = jmzgqrEndDate;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getBzXbjg() {
		return bzXbjg;
	}

	public void setBzXbjg(String bzXbjg) {
		this.bzXbjg = bzXbjg;
	}

	public String getBzGpsxf() {
		return bzGpsxf;
	}

	public void setBzGpsxf(String bzGpsxf) {
		this.bzGpsxf = bzGpsxf;
	}

	public String getBzPmyj() {
		return bzPmyj;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public String getBzBeizhu() {
		return bzBeizhu;
	}

	public void setBzBeizhu(String bzBeizhu) {
		this.bzBeizhu = bzBeizhu;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getIfPCommit() {
		return ifPCommit;
	}

	public void setIfPCommit(String ifPCommit) {
		this.ifPCommit = ifPCommit;
	}

	public String getUniteBlockCode() {
		return uniteBlockCode;
	}

	public void setUniteBlockCode(String uniteBlockCode) {
		this.uniteBlockCode = uniteBlockCode;
	}

	public String getIfSendGuard() {
		return ifSendGuard;
	}

	public void setIfSendGuard(String ifSendGuard) {
		this.ifSendGuard = ifSendGuard;
	}
	
    public String getListLoc() {
        return listLoc;
    }

    public void setListLoc(String listLoc) {
        this.listLoc = listLoc;
    }

    /** default constructor */
    public TdscBlockPlanTable() {
    }

    /** minimal constructor */
    public TdscBlockPlanTable(String planId) {
        this.planId = planId;
    }

    public String getPlanId() {
        return this.planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getPlanNum() {
        return this.planNum;
    }

    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLandId() {
        return this.landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getPreNoticeNo() {
        return this.preNoticeNo;
    }

    public void setPreNoticeNo(String preNoticeNo) {
        this.preNoticeNo = preNoticeNo;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("planId", getPlanId())
            .toString();
    }
    
    public Timestamp getResultShowDate() {
        return resultShowDate;
    }

    public void setResultShowDate(Timestamp resultShowDate) {
        this.resultShowDate = resultShowDate;
    }

    public Integer getAuctionMeetingNo() {
        return auctionMeetingNo;
    }

    public void setAuctionMeetingNo(Integer auctionMeetingNo) {
        this.auctionMeetingNo = auctionMeetingNo;
    }

    public Integer getBidEvaMeetingNo() {
        return bidEvaMeetingNo;
    }

    public void setBidEvaMeetingNo(Integer bidEvaMeetingNo) {
        this.bidEvaMeetingNo = bidEvaMeetingNo;
    }

    public Integer getListMeetingNo() {
        return listMeetingNo;
    }

    public void setListMeetingNo(Integer listMeetingNo) {
        this.listMeetingNo = listMeetingNo;
    }

    public Integer getOpeningMeetingNo() {
        return openingMeetingNo;
    }

    public void setOpeningMeetingNo(Integer openingMeetingNo) {
        this.openingMeetingNo = openingMeetingNo;
    }

    public Integer getSceBidMeetingNo() {
        return sceBidMeetingNo;
    }

    public void setSceBidMeetingNo(Integer sceBidMeetingNo) {
        this.sceBidMeetingNo = sceBidMeetingNo;
    }

    public Integer getTenderMeetingNo() {
        return tenderMeetingNo;
    }

    public void setTenderMeetingNo(Integer tenderMeetingNo) {
        this.tenderMeetingNo = tenderMeetingNo;
    }

	public String getStatusFlow() {
		return statusFlow;
	}

	public void setStatusFlow(String statusFlow) {
		this.statusFlow = statusFlow;
	}

	public Timestamp getLastActionDate() {
		return lastActionDate;
	}

	public void setLastActionDate(Timestamp lastActionDate) {
		this.lastActionDate = lastActionDate;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setBidderRollDate(Timestamp bidderRollDate) {
		this.bidderRollDate = bidderRollDate;
	}

	public Timestamp getBidderRollDate() {
		return bidderRollDate;
	}

}
