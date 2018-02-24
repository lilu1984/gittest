package com.wonders.tdsc.blockwork.web.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class TdscNoticeForm extends ActionForm{
    
    /** 出让公告号 (用户修改的)*/
    private String noticeNo;

    /** 出让公告名称 */
    private String noticeName;
    
    //出让文件所有地块名称
    private String uniteBlockName;
    
    //招拍挂编号
    private String tradeNum;
    
    /** 业务ID */
    private String appIds[];
    
    /** 上传文件名 */
    //private FormFile fileName;
    
    /** 公告ID */
    private String noticeId;
    
    private Date issueStartDate;
    
    private String transferMode;
    
    private String blockType;
    
    /** 出让公告号 原来的*/
    private String noticeNoOld;

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public Date getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public String gettransferMode() {
        return transferMode;
    }

    public void settransferMode(String transferMode) {
        this.transferMode = transferMode;
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

//    public FormFile getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(FormFile fileName) {
//        this.fileName = fileName;
//    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

	public String getNoticeNoOld() {
		return noticeNoOld;
	}

	public void setNoticeNoOld(String noticeNoOld) {
		this.noticeNoOld = noticeNoOld;
	}

	public String getUniteBlockName() {
		return uniteBlockName;
	}

	public void setUniteBlockName(String uniteBlockName) {
		this.uniteBlockName = uniteBlockName;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}


}
