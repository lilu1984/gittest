package com.wonders.tdsc.publishinfo.web.form;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts.action.ActionForm;

public class RecordAndResultForm extends ActionForm{
    
    private String currentPage;
    
    /** ҵ��ID */
    private String appId;

    /** ����״̬ */
    private String ifPublish;
    
    /** �����ʾ�������淢��״̬ */
    private String ifResultPublish;  

    /** ����ʱ�� */
    private Date publishDate;
    
    /** �ؿ�����*/
    private String blockName;
    
    /** �����*/
    private String noticeNo;
    
    private String transferMode;
    
    //���Ĺұ��
    private String tradeNum;
    
    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIfPublish() {
        return ifPublish;
    }

    public void setIfPublish(String ifPublish) {
        this.ifPublish = ifPublish;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}


}
