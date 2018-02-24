package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFlowDicAction implements Serializable {

    /** identifier field */
    private String actionId;

    /** nullable persistent field */
    private String actionName;

    /** full constructor */
    public TdscFlowDicAction(String actionId, String actionName) {
        this.actionId = actionId;
        this.actionName = actionName;
    }

    /** default constructor */
    public TdscFlowDicAction() {
    }

    /** minimal constructor */
    public TdscFlowDicAction(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return this.actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("actionId", getActionId())
            .toString();
    }

}
