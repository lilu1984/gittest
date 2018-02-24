package com.wonders.tdsc.publishinfo.web.form;

import org.apache.struts.action.ActionForm;

public class NoticePublishForm extends ActionForm{
    
	private String noticeNo;
    
    private String ifReleased;
    
    //упед╧р╠Ю╨е
    private String tradeNum;
    
    private String transferMode;

    public String getIfReleased() {
        return ifReleased;
    }

    public void setIfReleased(String ifReleased) {
        this.ifReleased = ifReleased;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
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
