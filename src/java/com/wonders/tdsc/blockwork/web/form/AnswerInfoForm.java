package com.wonders.tdsc.blockwork.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class AnswerInfoForm extends ActionForm{
    
    //问题信息录入(答疑信息表TDSC_FAQ_INFO)
    //问题编号
    private BigDecimal[] questionSer;
    
    //具体问题
    private String[] content;
    
    //联系人
    private String[] askPerson;
    
    //联系电话
    private String[] askTel;
    
    //关键字
    private String[] keyWord;
    
    //应询单位
    private String[] answerUnit;
    
    //答疑会信息录入(出让地块进度执行表,答疑会信息表)
    //答疑会时间
    private Timestamp answerDate;
    
    //答疑会地点
    private String answerLoc;
    
    //出席情况
    private String confStat;
    
    //纪要
//    private FormFile faqUrl;

    public Timestamp getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Timestamp answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswerLoc() {
        return answerLoc;
    }

    public void setAnswerLoc(String answerLoc) {
        this.answerLoc = answerLoc;
    }

    public String getConfStat() {
        return confStat;
    }

    public void setConfStat(String confStat) {
        this.confStat = confStat;
    }

//    public FormFile getFaqUrl() {
//        return faqUrl;
//    }
//
//    public void setFaqUrl(FormFile faqUrl) {
//        this.faqUrl = faqUrl;
//    }

    public String[] getAnswerUnit() {
        return answerUnit;
    }

    public void setAnswerUnit(String[] answerUnit) {
        this.answerUnit = answerUnit;
    }

    public String[] getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String[] keyWord) {
        this.keyWord = keyWord;
    }

    public String[] getAskPerson() {
        return askPerson;
    }

    public void setAskPerson(String[] askPerson) {
        this.askPerson = askPerson;
    }

    public String[] getAskTel() {
        return askTel;
    }

    public void setAskTel(String[] askTel) {
        this.askTel = askTel;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public BigDecimal[] getQuestionSer() {
        return questionSer;
    }

    public void setQuestionSer(BigDecimal[] questionSer) {
        this.questionSer = questionSer;
    }

 

}
