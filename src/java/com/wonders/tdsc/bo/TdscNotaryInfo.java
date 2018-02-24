package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscNotaryInfo implements Serializable {

    /** identifier field */
    private String notaryId;

    /** nullable persistent field */
    private String notaryName;

    /** nullable persistent field */
    private String notaryLinkman;

    /** nullable persistent field */
    private String notaryContactPlace;

    /** nullable persistent field */
    private String notaryContactWay;

    /** nullable persistent field */
    private String notaryContactMobile;

    /** nullable persistent field */
    private String notaryValid;

    /** nullable persistent field */
    private String notaryUserId;

    /** full constructor */
    public TdscNotaryInfo(String notaryId, String notaryName, String notaryLinkman, String notaryContactPlace, String notaryContactWay, String notaryContactMobile, String notaryValid, String notaryUserId) {
        this.notaryId = notaryId;
        this.notaryName = notaryName;
        this.notaryLinkman = notaryLinkman;
        this.notaryContactPlace = notaryContactPlace;
        this.notaryContactWay = notaryContactWay;
        this.notaryContactMobile = notaryContactMobile;
        this.notaryValid = notaryValid;
        this.notaryUserId = notaryUserId;
    }

    /** default constructor */
    public TdscNotaryInfo() {
    }

    /** minimal constructor */
    public TdscNotaryInfo(String notaryId) {
        this.notaryId = notaryId;
    }

    public String getNotaryId() {
        return this.notaryId;
    }

    public void setNotaryId(String notaryId) {
        this.notaryId = notaryId;
    }

    public String getNotaryName() {
        return this.notaryName;
    }

    public void setNotaryName(String notaryName) {
        this.notaryName = notaryName;
    }

    public String getNotaryLinkman() {
        return this.notaryLinkman;
    }

    public void setNotaryLinkman(String notaryLinkman) {
        this.notaryLinkman = notaryLinkman;
    }

    public String getNotaryContactPlace() {
        return this.notaryContactPlace;
    }

    public void setNotaryContactPlace(String notaryContactPlace) {
        this.notaryContactPlace = notaryContactPlace;
    }

    public String getNotaryContactWay() {
        return this.notaryContactWay;
    }

    public void setNotaryContactWay(String notaryContactWay) {
        this.notaryContactWay = notaryContactWay;
    }

    public String getNotaryContactMobile() {
        return notaryContactMobile;
    }
    
    public void setNotaryContactMobile(String notaryContactMobile) {
        this.notaryContactMobile = notaryContactMobile;
    }
    
    public String getNotaryValid() {
        return notaryValid;
    }
    
    public void setNotaryValid(String notaryValid) {
        this.notaryValid = notaryValid;
    }
    
    public String getNotaryUserId() {
        return notaryUserId;
    }
    
    public void setNotaryUserId(String notaryUserId) {
        this.notaryUserId = notaryUserId;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("notaryId", getNotaryId())
            .toString();
    }

}
