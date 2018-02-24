package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscAppNodeStat implements Serializable {

    /** identifier field */
    private String id;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String flowId;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String nodeName;

    /** nullable persistent field */
    private String nodeStat;

    /** nullable persistent field */
    private Date startDate;

    /** nullable persistent field */
    private Date endDate;

    /** nullable persistent field */
    private String hasSubFlow;

    /** nullable persistent field */
    private String subFlowId;

    /** full constructor */
    public TdscAppNodeStat(String id, String appId, String flowId, String nodeId, String nodeName, String nodeStat, Date startDate, Date endDate, String hasSubFlow, String subFlowId) {
        this.id = id;
        this.appId = appId;
        this.flowId = flowId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeStat = nodeStat;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasSubFlow = hasSubFlow;
        this.subFlowId = subFlowId;
    }

    /** default constructor */
    public TdscAppNodeStat() {
    }

    /** minimal constructor */
    public TdscAppNodeStat(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFlowId() {
        return this.flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeStat() {
        return this.nodeStat;
    }

    public void setNodeStat(String nodeStat) {
        this.nodeStat = nodeStat;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getHasSubFlow() {
        return this.hasSubFlow;
    }

    public void setHasSubFlow(String hasSubFlow) {
        this.hasSubFlow = hasSubFlow;
    }

    public String getSubFlowId() {
        return this.subFlowId;
    }

    public void setSubFlowId(String subFlowId) {
        this.subFlowId = subFlowId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
