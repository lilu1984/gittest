package com.wonders.tdsc.localtrade.web.form;

import org.apache.struts.action.ActionForm;

public class LocalTradeForm extends ActionForm{
		
	//�����
	private String noticeNo;
	
	//���Ĺұ��
    private String tradeNum;
    
    //�����ļ����еؿ�����
    private String uniteBlockName;
    
    //�����ļ����еؿ�λ�ü���
    private String landLocation;
    
    //���÷�ʽ
    private String transferMode;
    
    /** ���ƺ� */
    private String cardNo;
    
    /** ���ƺ� */
    private String[] cardNos;
    
    /** ���׿��������� */
    private String[] certNos;
    
    /** ҵ��ID*/
    private String appId;

    /**  ����˳��*/
    private String[] numbers;
    
    /**  ��������*/
    private String[] addPrices;
    
    /**  �ܼ�*/
    private String[] totalPrices;
    
    /** ���ƺ� */
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
