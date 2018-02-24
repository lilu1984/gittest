package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscInfoDiscApp implements Serializable {

    /** identifier field */
    private String infoId;

    /** nullable persistent field */
    private String infoType;

    /** nullable persistent field */
    private Date initDate;

    /** nullable persistent field */
    private String initUser;

    /** nullable persistent field */
    private String status;

    /** nullable persistent field */
    private Date statusDate;

    /** nullable persistent field */
    private String infoUrl;

    /** full constructor */
    public TdscInfoDiscApp(String infoId, String infoType, Date initDate, String initUser, String status, Date statusDate, String infoUrl) {
        this.infoId = infoId;
        this.infoType = infoType;
        this.initDate = initDate;
        this.initUser = initUser;
        this.status = status;
        this.statusDate = statusDate;
        this.infoUrl = infoUrl;
    }

    /** default constructor */
    public TdscInfoDiscApp() {
    }

    /** minimal constructor */
    public TdscInfoDiscApp(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoId() {
        return this.infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoType() {
        return this.infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public Date getInitDate() {
        return this.initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public String getInitUser() {
        return this.initUser;
    }

    public void setInitUser(String initUser) {
        this.initUser = initUser;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return this.statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getInfoUrl() {
        return this.infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("infoId", getInfoId())
            .toString();
    }

}
