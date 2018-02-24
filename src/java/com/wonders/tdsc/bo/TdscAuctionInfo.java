package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscAuctionInfo implements Serializable {

    /** identifier field */
    private String auctionId;

    /** nullable persistent field */
    private String appId;

    /** �ֳ�����ʱ�� */
    private Date auctionDate;

    /** �ֳ����۽�� */
    private String autcionResult;

    /** ���ȷ��ʱ�� */
    private Date autcionResultDate;

    /** ���뾺������ */
    private BigDecimal joinerNum;

    /** �ֳ����������� */
    private String auctionModerator;

    /** �����ṫ֤�� */
    private String auctionNorar;

    /** �ɽ��۸� */
    private BigDecimal resultPrice;

    /** ���ú��� */
    private String resultNo;

    /** �������ʸ�֤���� */
    private String resultCert;

    /** ���������� */
    private String resultName;

    /** ����ʧ��ԭ�� */
    private String auctionFailReason;

    /** ����ʧ�ܱ�ע */
    private String auctionFailMeno;

    /** full constructor */
    public TdscAuctionInfo(String auctionId, String appId, Date auctionDate, String autcionResult, Date autcionResultDate, BigDecimal joinerNum, String auctionModerator, String auctionNorar, BigDecimal resultPrice, String resultNo, String resultCert, String resultName, String auctionFailReason, String auctionFailMeno) {
        this.auctionId = auctionId;
        this.appId = appId;
        this.auctionDate = auctionDate;
        this.autcionResult = autcionResult;
        this.autcionResultDate = autcionResultDate;
        this.joinerNum = joinerNum;
        this.auctionModerator = auctionModerator;
        this.auctionNorar = auctionNorar;
        this.resultPrice = resultPrice;
        this.resultNo = resultNo;
        this.resultCert = resultCert;
        this.resultName = resultName;
        this.auctionFailReason = auctionFailReason;
        this.auctionFailMeno = auctionFailMeno;
    }

    /** default constructor */
    public TdscAuctionInfo() {
    }

    /** minimal constructor */
    public TdscAuctionInfo(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionId() {
        return this.auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getAuctionDate() {
        return this.auctionDate;
    }

    public void setAuctionDate(Date auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getAutcionResult() {
        return this.autcionResult;
    }

    public void setAutcionResult(String autcionResult) {
        this.autcionResult = autcionResult;
    }

    public Date getAutcionResultDate() {
        return this.autcionResultDate;
    }

    public void setAutcionResultDate(Date autcionResultDate) {
        this.autcionResultDate = autcionResultDate;
    }

    public BigDecimal getJoinerNum() {
        return this.joinerNum;
    }

    public void setJoinerNum(BigDecimal joinerNum) {
        this.joinerNum = joinerNum;
    }

    public String getAuctionModerator() {
        return this.auctionModerator;
    }

    public void setAuctionModerator(String auctionModerator) {
        this.auctionModerator = auctionModerator;
    }

    public String getAuctionNorar() {
        return this.auctionNorar;
    }

    public void setAuctionNorar(String auctionNorar) {
        this.auctionNorar = auctionNorar;
    }

    public BigDecimal getResultPrice() {
        return this.resultPrice;
    }

    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }

    public String getResultNo() {
        return this.resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
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

    public String getAuctionFailReason() {
        return this.auctionFailReason;
    }

    public void setAuctionFailReason(String auctionFailReason) {
        this.auctionFailReason = auctionFailReason;
    }

    public String getAuctionFailMeno() {
        return this.auctionFailMeno;
    }

    public void setAuctionFailMeno(String auctionFailMeno) {
        this.auctionFailMeno = auctionFailMeno;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("auctionId", getAuctionId())
            .toString();
    }

}
