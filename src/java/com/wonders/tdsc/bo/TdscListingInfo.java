package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscListingInfo extends BaseBO {

    /** identifier field */
    private String listingId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private BigDecimal currPrice;

    /** nullable persistent field */
    private BigDecimal currRound;

    /** nullable persistent field */
    private Timestamp listDate;

    /** nullable persistent field */
    private String listPlace;
    
    
    private String listCert;

    /** nullable persistent field */
    private String listModerator;

    /** nullable persistent field */
    private String listResult;

    /** nullable persistent field */
    private Timestamp listResultDate;

    /** nullable persistent field */
    private Timestamp sceneDate;

    /** nullable persistent field */
    private String sceneResult;

    /** nullable persistent field */
    private Timestamp sceneResultDate;

    /** nullable persistent field */
    private BigDecimal joinerNum;

    /** nullable persistent field */
    private String sceneModerator;

    /** nullable persistent field */
    private String sceneNorar;

    /** nullable persistent field */
    private String resultNo;

    /** nullable persistent field */
    private BigDecimal resultPrice;

    /** nullable persistent field */
    private String resultCert;

    /** nullable persistent field */
    private String resultName;

    /** nullable persistent field */
    private String yktXh;

    /** nullable persistent field */
    private String listFailReason;

    /** nullable persistent field */
    private String listFailMeno;

    /** nullable persistent field */
    private String listNo;

    public String getListNo() {
		return listNo;
	}

	public void setListNo(String listNo) {
		this.listNo = listNo;
	}

	/** default constructor */
    public TdscListingInfo() {
    }

    /** minimal constructor */
    public TdscListingInfo(String listingId) {
        this.listingId = listingId;
    }

    public String getListingId() {
        return this.listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getCurrPrice() {
        return this.currPrice;
    }

    public void setCurrPrice(BigDecimal currPrice) {
        this.currPrice = currPrice;
    }

    public BigDecimal getCurrRound() {
        return this.currRound;
    }

    public void setCurrRound(BigDecimal currRound) {
        this.currRound = currRound;
    }

    public Timestamp getListDate() {
        return this.listDate;
    }

    public void setListDate(Timestamp listDate) {
        this.listDate = listDate;
    }

    public String getListPlace() {
        return this.listPlace;
    }

    public void setListPlace(String listPlace) {
        this.listPlace = listPlace;
    }

    public String getListModerator() {
        return this.listModerator;
    }

    public void setListModerator(String listModerator) {
        this.listModerator = listModerator;
    }

    public String getListResult() {
        return this.listResult;
    }

    public void setListResult(String listResult) {
        this.listResult = listResult;
    }

    public Timestamp getListResultDate() {
        return this.listResultDate;
    }

    public void setListResultDate(Timestamp listResultDate) {
        this.listResultDate = listResultDate;
    }

    public Timestamp getSceneDate() {
        return this.sceneDate;
    }

    public void setSceneDate(Timestamp sceneDate) {
        this.sceneDate = sceneDate;
    }

    public String getSceneResult() {
        return this.sceneResult;
    }

    public void setSceneResult(String sceneResult) {
        this.sceneResult = sceneResult;
    }

    public Timestamp getSceneResultDate() {
        return this.sceneResultDate;
    }

    public void setSceneResultDate(Timestamp sceneResultDate) {
        this.sceneResultDate = sceneResultDate;
    }

    public BigDecimal getJoinerNum() {
        return this.joinerNum;
    }

    public void setJoinerNum(BigDecimal joinerNum) {
        this.joinerNum = joinerNum;
    }

    public String getSceneModerator() {
        return this.sceneModerator;
    }

    public void setSceneModerator(String sceneModerator) {
        this.sceneModerator = sceneModerator;
    }

    public String getSceneNorar() {
        return this.sceneNorar;
    }

    public void setSceneNorar(String sceneNorar) {
        this.sceneNorar = sceneNorar;
    }

    public String getResultNo() {
        return this.resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
    }

    public BigDecimal getResultPrice() {
        return this.resultPrice;
    }

    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }

    public String getResultCert() {
        return this.resultCert;
    }

    public void setResultCert(String resultCert) {
        this.resultCert = resultCert;
    }

    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getYktXh() {
        return yktXh;
    }

    public void setYktXh(String yktXh) {
        this.yktXh = yktXh;
    }
    public String getListFailMeno() {
        return listFailMeno;
    }
    
    public void setListFailMeno(String listFailMeno) {
        this.listFailMeno = listFailMeno;
    }
    
    public String getListFailReason() {
        return listFailReason;
    }
    
    public void setListFailReason(String listFailReason) {
        this.listFailReason = listFailReason;
    }

    public String getListCert() {
        return listCert;
    }

    public void setListCert(String listCert) {
        this.listCert = listCert;
    }
}
