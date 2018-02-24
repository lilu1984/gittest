package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFlowDicStatus implements Serializable {

    /** identifier field */
    private String statusId;

    /** nullable persistent field */
    private String statusName;

    /** full constructor */
    public TdscFlowDicStatus(String statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    /** default constructor */
    public TdscFlowDicStatus() {
    }

    /** minimal constructor */
    public TdscFlowDicStatus(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("statusId", getStatusId())
            .toString();
    }

}
