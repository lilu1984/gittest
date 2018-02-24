package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFlowPostStatus implements Serializable {

    /** identifier field */
    private String id;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String actionId;

    /** nullable persistent field */
    private String resultId;

    /** nullable persistent field */
    private String resultName;

    /** nullable persistent field */
    private String isDefaultResult;

    /** nullable persistent field */
    private String ifCloseNode;

    /** nullable persistent field */
    private String postActionId;

    /** nullable persistent field */
    private String postNodeId;

    /** nullable persistent field */
    private String postStatusId;

    /** nullable persistent field */
    private String syFlowAction;

    /** full constructor */
    public TdscFlowPostStatus(String id, String transferMode, String nodeId, String actionId, String resultId, String resultName, String isDefaultResult, String ifCloseNode, String postActionId, String postNodeId, String postStatusId, String syFlowAction) {
        this.id = id;
        this.transferMode = transferMode;
        this.nodeId = nodeId;
        this.actionId = actionId;
        this.resultId = resultId;
        this.resultName = resultName;
        this.isDefaultResult = isDefaultResult;
        this.ifCloseNode = ifCloseNode;
        this.postActionId = postActionId;
        this.postNodeId = postNodeId;
        this.postStatusId = postStatusId;
        this.syFlowAction = syFlowAction;
    }

    /** default constructor */
    public TdscFlowPostStatus() {
    }

    /** minimal constructor */
    public TdscFlowPostStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransferMode() {
        return transferMode;
    }
    
    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }
    
    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getActionId() {
        return this.actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getResultId() {
        return this.resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getIsDefaultResult() {
        return this.isDefaultResult;
    }

    public void setIsDefaultResult(String isDefaultResult) {
        this.isDefaultResult = isDefaultResult;
    }

    public String getIfCloseNode() {
        return this.ifCloseNode;
    }

    public void setIfCloseNode(String ifCloseNode) {
        this.ifCloseNode = ifCloseNode;
    }

    public String getPostActionId() {
        return this.postActionId;
    }

    public void setPostActionId(String postActionId) {
        this.postActionId = postActionId;
    }

    public String getPostNodeId() {
        return this.postNodeId;
    }

    public void setPostNodeId(String postNodeId) {
        this.postNodeId = postNodeId;
    }

    public String getPostStatusId() {
        return this.postStatusId;
    }

    public void setPostStatusId(String postStatusId) {
        this.postStatusId = postStatusId;
    }

    public String getSyFlowAction() {
        return syFlowAction;
    }
    
    public void setSyFlowAction(String syFlowAction) {
        this.syFlowAction = syFlowAction;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
