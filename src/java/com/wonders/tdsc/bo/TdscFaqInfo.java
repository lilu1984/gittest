package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFaqInfo implements Serializable {

    /** identifier field */
    private String faqId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private BigDecimal questionSer;

    /** nullable persistent field */
    private String keyWord;

    /** nullable persistent field */
    private String answerUnit;

    /** nullable persistent field */
    private String content;

    /** nullable persistent field */
    private String askPerson;

    /** nullable persistent field */
    private Date askDate;

    /** nullable persistent field */
    private String askTel;

    /** nullable persistent field */
    private String soluType;

    /** nullable persistent field */
    private String answer;

    /** nullable persistent field */
    private String answerPerson;

    /** full constructor */
    public TdscFaqInfo(String faqId, String appId, BigDecimal questionSer, String keyWord, String answerUnit, String content, String askPerson, Date askDate, String askTel, String soluType, String answer, String answerPerson) {
        this.faqId = faqId;
        this.appId = appId;
        this.questionSer = questionSer;
        this.keyWord = keyWord;
        this.answerUnit = answerUnit;
        this.content = content;
        this.askPerson = askPerson;
        this.askDate = askDate;
        this.askTel = askTel;
        this.soluType = soluType;
        this.answer = answer;
        this.answerPerson = answerPerson;
    }

    /** default constructor */
    public TdscFaqInfo() {
    }

    /** minimal constructor */
    public TdscFaqInfo(String faqId) {
        this.faqId = faqId;
    }

    public String getFaqId() {
        return this.faqId;
    }

    public void setFaqId(String faqId) {
        this.faqId = faqId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getQuestionSer() {
        return this.questionSer;
    }

    public void setQuestionSer(BigDecimal questionSer) {
        this.questionSer = questionSer;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getAnswerUnit() {
        return answerUnit;
    }
    
    public void setAnswerUnit(String answerUnit) {
        this.answerUnit = answerUnit;
    }
    
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAskPerson() {
        return this.askPerson;
    }

    public void setAskPerson(String askPerson) {
        this.askPerson = askPerson;
    }

    public Date getAskDate() {
        return this.askDate;
    }

    public void setAskDate(Date askDate) {
        this.askDate = askDate;
    }

    public String getAskTel() {
        return this.askTel;
    }

    public void setAskTel(String askTel) {
        this.askTel = askTel;
    }

    public String getSoluType() {
        return this.soluType;
    }

    public void setSoluType(String soluType) {
        this.soluType = soluType;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerPerson() {
        return this.answerPerson;
    }

    public void setAnswerPerson(String answerPerson) {
        this.answerPerson = answerPerson;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("faqId", getFaqId())
            .toString();
    }

}
