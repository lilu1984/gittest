package com.wonders.tdsc.blockwork.web.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class TdscNoticeForm extends ActionForm{
    
    /** ���ù���� (�û��޸ĵ�)*/
    private String noticeNo;

    /** ���ù������� */
    private String noticeName;
    
    //�����ļ����еؿ�����
    private String uniteBlockName;
    
    //���Ĺұ��
    private String tradeNum;
    
    /** ҵ��ID */
    private String appIds[];
    
    /** �ϴ��ļ��� */
    //private FormFile fileName;
    
    /** ����ID */
    private String noticeId;
    
    private Date issueStartDate;
    
    private String transferMode;
    
    private String blockType;
    
    /** ���ù���� ԭ����*/
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
