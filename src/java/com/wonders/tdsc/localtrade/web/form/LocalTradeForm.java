package com.wonders.tdsc.localtrade.web.form;

import org.apache.struts.action.ActionForm;

public class LocalTradeForm extends ActionForm{
		
	//公告号
	private String noticeNo;
	
	//招拍挂编号
    private String tradeNum;
    
    //出让文件所有地块名称
    private String uniteBlockName;
    
    //出让文件所有地块位置集合
    private String landLocation;
    
    //出让方式
    private String transferMode;
    
    /** 号牌号 */
    private String cardNo;
    
    /** 号牌号 */
    private String[] cardNos;
    
    /** 交易卡卡号数组 */
    private String[] certNos;
    
    /** 业务ID*/
    private String appId;

    /**  报价顺序*/
    private String[] numbers;
    
    /**  所报单价*/
    private String[] addPrices;
    
    /**  总价*/
    private String[] totalPrices;
    
    /** 号牌号 */
    private String[] haoPais;

	public String[] getHaoPais() {
		return haoPais;
	}

	public void setHaoPais(String[] haoPais) {
		this.haoPais = haoPais;
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

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String[] getCardNos() {
		return cardNos;
	}

	public void setCardNos(String[] cardNos) {
		this.cardNos = cardNos;
	}

	public String[] getCertNos() {
		return certNos;
	}

	public void setCertNos(String[] certNos) {
		this.certNos = certNos;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String[] getNumbers() {
		return numbers;
	}

	public void setNumbers(String[] numbers) {
		this.numbers = numbers;
	}

	public String[] getAddPrices() {
		return addPrices;
	}

	public void setAddPrices(String[] addPrices) {
		this.addPrices = addPrices;
	}

	public String[] getTotalPrices() {
		return totalPrices;
	}

	public void setTotalPrices(String[] totalPrices) {
		this.totalPrices = totalPrices;
	}
	
}
