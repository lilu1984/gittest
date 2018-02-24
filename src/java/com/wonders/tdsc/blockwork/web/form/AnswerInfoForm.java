package com.wonders.tdsc.blockwork.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class AnswerInfoForm extends ActionForm{
    
    //������Ϣ¼��(������Ϣ��TDSC_FAQ_INFO)
    //������
    private BigDecimal[] questionSer;
    
    //��������
    private String[] content;
    
    //��ϵ��
    private String[] askPerson;
    
    //��ϵ�绰
    private String[] askTel;
    
    //�ؼ���
    private String[] keyWord;
    
    //Ӧѯ��λ
    private String[] answerUnit;
    
    //���ɻ���Ϣ¼��(���õؿ����ִ�б�,���ɻ���Ϣ��)
    //���ɻ�ʱ��
    private Timestamp answerDate;
    
    //���ɻ�ص�
    private String answerLoc;
    
    //��ϯ���
    private String confStat;
    
    //��Ҫ
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
