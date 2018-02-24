package com.wonders.tdsc.blockwork.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class ScheduletableInfoForm extends ActionForm{
    
    //出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
    //接收审核要素材料起始时间
    //private Timestamp recMatStartDate;
    
    //接收审核要素材料截止时间
    //private Timestamp recMatEndDate;
    
    //制作出让公告和文件起始时间
    //private Timestamp noticeStartDate;
    
    //制作出让公告和文件截止时间
    //private Timestamp noticeEndDate;
	private String orgAppId;
	
	private String orgInfoId;
    
    //发布公告起始时间
    private Timestamp issueStartDate;
    
    //发布公告截止时间
    private Timestamp issueEndDate;
    
    //发售文件起始时间
    private Timestamp getFileStartDate;
    
    //发售文件截止时间
    private Timestamp getFileEndDate;
    
    //现场勘察会时间
    private Timestamp inspDate;
    
    //现场勘察会地点
    private String inspLoc;
    
    //收集提问时间
    private Timestamp gatherDate;
    
    //答疑会时间
    private Timestamp answerDate;
    
    //答疑会地点
    private String answerLoc;
    
    //发布答疑纪要时间
    private Timestamp relFaqDate;
    
    //受理竞买申请起始时间
    private Timestamp accAppStatDate;
    
    //受理竞买申请截止时间
    private Timestamp accAppEndDate;
    
    //出让结果公示时间
    private Timestamp resultShowDate;
        
    //地块名称
    private String blockName;
    
    //挂牌地点
    private String listLoc;
    
    //地块公告号
	private String blockNoticeNo;
	
	//审批区县
	private Long districtId;
	
	//所属区县
	private String endorseDistrict;
	
	//土地类型
	private String blockType;
	
	//出让方式
	private String transferMode;
	
	//当前节点
	private String nodeId;
	
	//当前状态
	private String blockAuditStatus;

    //工作日
	private String businessStr;
	
	//假期
	private String holidayStr;
	
	//查询条件：年
	private String conditionYear;

	//查询条件：月
    private String conditionMonth;
    
    /** 业务ID */
    private String appIds[];
    
    //出让结果公示时间
    private Timestamp marginEndDate;

    /** nullable persistent field */
    private Timestamp recMatStartDate;

    /** nullable persistent field */
    private Timestamp recMatEndDate;

    /** nullable persistent field */
    private Timestamp noticeStartDate;

    /** nullable persistent field */
    private Timestamp noticeEndDate;

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
    private String planId;
    
	//招拍挂编号第一部分
	private String tradeNum_1;
	//招拍挂编号第二部分
	private String tradeNum_2;
	//招拍挂编号第三部分
	private String tradeNum_3;
	
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
    
  //PARTAKE_BIDDER_CON_NUM参与的竞买人号牌
	private String partakeBidderConNum;
	
	//MAX_PRICE最高限价
	private BigDecimal maxPrice;
	
	  //最高限价竞买人摇号时间
    private Timestamp bidderRollDate;
    
    public Timestamp getJmzgqrEndDate() {
		return jmzgqrEndDate;
	}

	public void setJmzgqrEndDate(Timestamp jmzgqrEndDate) {
		this.jmzgqrEndDate = jmzgqrEndDate;
	}

	public String getTradeNum_1() {
		return tradeNum_1;
	}

	public void setTradeNum_1(String tradeNum_1) {
		this.tradeNum_1 = tradeNum_1;
	}

	public String getTradeNum_2() {
		return tradeNum_2;
	}

	public void setTradeNum_2(String tradeNum_2) {
		this.tradeNum_2 = tradeNum_2;
	}

	public String getTradeNum_3() {
		return tradeNum_3;
	}

	public void setTradeNum_3(String tradeNum_3) {
		this.tradeNum_3 = tradeNum_3;
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

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public String getListLoc() {
        return listLoc;
    }

    public String getBusinessStr() {
		return businessStr;
	}

	public void setBusinessStr(String businessStr) {
		this.businessStr = businessStr;
	}

	public String getConditionMonth() {
		return conditionMonth;
	}

	public void setConditionMonth(String conditionMonth) {
		this.conditionMonth = conditionMonth;
	}

	public String getConditionYear() {
		return conditionYear;
	}

	public void setConditionYear(String conditionYear) {
		this.conditionYear = conditionYear;
	}

	public String getHolidayStr() {
		return holidayStr;
	}

	public void setHolidayStr(String holidayStr) {
		this.holidayStr = holidayStr;
	}

	public void setListLoc(String listLoc) {
        this.listLoc = listLoc;
    }

    public Timestamp getAccAppEndDate() {
        return accAppEndDate;
    }

    public void setAccAppEndDate(Timestamp accAppEndDate) {
        this.accAppEndDate = accAppEndDate;
    }

    public Timestamp getAccAppStatDate() {
        return accAppStatDate;
    }

    public void setAccAppStatDate(Timestamp accAppStatDate) {
        this.accAppStatDate = accAppStatDate;
    }

    public Timestamp getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Timestamp answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswerLoc() {
        return answerLoc;
    }

    public void setAnswerLoc(String answerLoc) {
        this.answerLoc = answerLoc;
    }

    public Timestamp getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(Timestamp gatherDate) {
        this.gatherDate = gatherDate;
    }

    public Timestamp getGetFileEndDate() {
        return getFileEndDate;
    }

    public void setGetFileEndDate(Timestamp getFileEndDate) {
        this.getFileEndDate = getFileEndDate;
    }

    public Timestamp getGetFileStartDate() {
        return getFileStartDate;
    }

    public void setGetFileStartDate(Timestamp getFileStartDate) {
        this.getFileStartDate = getFileStartDate;
    }

    public Timestamp getInspDate() {
        return inspDate;
    }

    public void setInspDate(Timestamp inspDate) {
        this.inspDate = inspDate;
    }

    public String getInspLoc() {
        return inspLoc;
    }

    public void setInspLoc(String inspLoc) {
        this.inspLoc = inspLoc;
    }

    public Timestamp getIssueEndDate() {
        return issueEndDate;
    }

    public void setIssueEndDate(Timestamp issueEndDate) {
        this.issueEndDate = issueEndDate;
    }

    public Timestamp getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(Timestamp issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public Timestamp getRelFaqDate() {
        return relFaqDate;
    }

    public void setRelFaqDate(Timestamp relFaqDate) {
        this.relFaqDate = relFaqDate;
    }

    public Timestamp getResultShowDate() {
        return resultShowDate;
    }

    public void setResultShowDate(Timestamp resultShowDate) {
        this.resultShowDate = resultShowDate;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

	public String getBlockAuditStatus() {
		return blockAuditStatus;
	}

	public void setBlockAuditStatus(String blockAuditStatus) {
		this.blockAuditStatus = blockAuditStatus;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getEndorseDistrict() {
		return endorseDistrict;
	}

	public void setEndorseDistrict(String endorseDistrict) {
		this.endorseDistrict = endorseDistrict;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String[] getAppIds() {
		return appIds;
	}

	public void setAppIds(String[] appIds) {
		this.appIds = appIds;
	}

	public Timestamp getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Timestamp auctionDate) {
		this.auctionDate = auctionDate;
	}

	public String getAuctionLoc() {
		return auctionLoc;
	}

	public void setAuctionLoc(String auctionLoc) {
		this.auctionLoc = auctionLoc;
	}

	public Timestamp getBidEvaDate() {
		return bidEvaDate;
	}

	public void setBidEvaDate(Timestamp bidEvaDate) {
		this.bidEvaDate = bidEvaDate;
	}

	public String getBidEvaLoc() {
		return bidEvaLoc;
	}

	public void setBidEvaLoc(String bidEvaLoc) {
		this.bidEvaLoc = bidEvaLoc;
	}

	public Timestamp getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}

	public Timestamp getListStartDate() {
		return listStartDate;
	}

	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}

	public Timestamp getNoticeEndDate() {
		return noticeEndDate;
	}

	public void setNoticeEndDate(Timestamp noticeEndDate) {
		this.noticeEndDate = noticeEndDate;
	}

	public Timestamp getNoticeStartDate() {
		return noticeStartDate;
	}

	public void setNoticeStartDate(Timestamp noticeStartDate) {
		this.noticeStartDate = noticeStartDate;
	}

	public Timestamp getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Timestamp openingDate) {
		this.openingDate = openingDate;
	}

	public String getOpeningLoc() {
		return openingLoc;
	}

	public void setOpeningLoc(String openingLoc) {
		this.openingLoc = openingLoc;
	}

	public Timestamp getRecMatEndDate() {
		return recMatEndDate;
	}

	public void setRecMatEndDate(Timestamp recMatEndDate) {
		this.recMatEndDate = recMatEndDate;
	}

	public Timestamp getRecMatStartDate() {
		return recMatStartDate;
	}

	public void setRecMatStartDate(Timestamp recMatStartDate) {
		this.recMatStartDate = recMatStartDate;
	}

	public Timestamp getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getReviewLoc() {
		return reviewLoc;
	}

	public void setReviewLoc(String reviewLoc) {
		this.reviewLoc = reviewLoc;
	}

	public Timestamp getSceBidDate() {
		return sceBidDate;
	}

	public void setSceBidDate(Timestamp sceBidDate) {
		this.sceBidDate = sceBidDate;
	}

	public String getSceBidLoc() {
		return sceBidLoc;
	}

	public void setSceBidLoc(String sceBidLoc) {
		this.sceBidLoc = sceBidLoc;
	}

	public Timestamp getTenderEndDate() {
		return tenderEndDate;
	}

	public void setTenderEndDate(Timestamp tenderEndDate) {
		this.tenderEndDate = tenderEndDate;
	}

	public Timestamp getTenderStartDate() {
		return tenderStartDate;
	}

	public void setTenderStartDate(Timestamp tenderStartDate) {
		this.tenderStartDate = tenderStartDate;
	}

	public String getOrgAppId() {
		return orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public String getOrgInfoId() {
		return orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public void setPartakeBidderConNum(String partakeBidderConNum) {
		this.partakeBidderConNum = partakeBidderConNum;
	}

	public String getPartakeBidderConNum() {
		return partakeBidderConNum;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setBidderRollDate(Timestamp bidderRollDate) {
		this.bidderRollDate = bidderRollDate;
	}

	public Timestamp getBidderRollDate() {
		return bidderRollDate;
	}

}
