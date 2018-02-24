package com.wonders.tdsc.blockwork.web.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class TdscFileForm extends ActionForm {
    //private FormFile fileName;

    private String modeName;
    
    private String modeNameEn;
    
    private String blockName;
    
    private String blockType;
    
    private String transferMode;
    
    private String districtId;
    
    /** 出让公告号 用户修改的*/
    private String noticeNo;
    
    /** 出让公告号 系统自身的原来的出让公告号*/
    private String noticeNoOld;

    /** 出让公告名称 */
    private String noticeName;
    
    /** 业务ID */
    private String appIds[];
    
    /** 公告ID */
    private String noticeId;
    
    //实施方案id
    private String planId;
    
    //出让文件所有地块名称
    private String uniteBlockName;
    
    //出让文件所有地块位置集合
    private String landLocation;
    
    private Date issueStartDate;
    
    //招拍挂编号
    private String tradeNum;
    
    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String gettransferMode() {
        return transferMode;
    }

    public void settransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeNameEn() {
        return modeNameEn;
    }

    public void setModeNameEn(String modeNameEn) {
        this.modeNameEn = modeNameEn;
    }
    
    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String[] getAppIds() {
        return appIds;
    }

    public void setAppIds(String[] appIds) {
        this.appIds = appIds;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
    
    public Date getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

	public String getNoticeNoOld() {
		return noticeNoOld;
	}

	public void setNoticeNoOld(String noticeNoOld) {
		this.noticeNoOld = noticeNoOld;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getUniteBlockName() {
		return uniteBlockName;
	}

	public void setUniteBlockName(String uniteBlockName) {
		this.uniteBlockName = uniteBlockName;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

//    public FormFile getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(FormFile fileName) {
//        this.fileName = fileName;
//    }

}
