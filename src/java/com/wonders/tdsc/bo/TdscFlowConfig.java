package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFlowConfig implements Serializable {

    /** identifier field */
    private String id;

    /** nullable persistent field */
    private String flowId;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private String nodeName;

    /** nullable persistent field */
    private String plusCondition;

    /** nullable persistent field */
    private String isStartNode;

    /** nullable persistent field */
    private String postNode;

    /** nullable persistent field */
    private String nodeInitType;

    /** nullable persistent field */
    private String closeType;

    /** nullable persistent field */
    private String closeCondition;

    /** nullable persistent field */
    private String hasSubFlow;

    /** nullable persistent field */
    private String subFlowStandId;

    /** nullable persistent field */
    private String startScheduleCol;

    /** nullable persistent field */
    private String startScheduleInfoType;

    /** nullable persistent field */
    private String endScheduleCol;

    /** nullable persistent field */
    private String endScheduleInfoType;

    /** full constructor */
    public TdscFlowConfig(String id, String flowId, String nodeId, String transferMode, String nodeName, String plusCondition, String isStartNode, String postNode, String nodeInitType, String closeType, String closeCondition, String hasSubFlow, String subFlowStandId, String startScheduleCol, String startScheduleInfoType, String endScheduleCol, String endScheduleInfoType) {
        this.id = id;
        this.flowId = flowId;
        this.nodeId = nodeId;
        this.transferMode = transferMode;
        this.nodeName = nodeName;
        this.plusCondition = plusCondition;
        this.isStartNode = isStartNode;
        this.postNode = postNode;
        this.nodeInitType = nodeInitType;
        this.closeType = closeType;
        this.closeCondition = closeCondition;
        this.hasSubFlow = hasSubFlow;
        this.subFlowStandId = subFlowStandId;
        this.startScheduleCol = startScheduleCol;
        this.startScheduleInfoType = startScheduleInfoType;
        this.endScheduleCol = endScheduleCol;
        this.endScheduleInfoType = endScheduleInfoType;
    }

    /** default constructor */
    public TdscFlowConfig() {
    }

    /** minimal constructor */
    public TdscFlowConfig(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPlusCondition() {
        return this.plusCondition;
    }

    public void setPlusCondition(String plusCondition) {
        this.plusCondition = plusCondition;
    }

    public String getIsStartNode() {
        return this.isStartNode;
    }

    public void setIsStartNode(String isStartNode) {
        this.isStartNode = isStartNode;
    }

    public String getPostNode() {
        return this.postNode;
    }

    public void setPostNode(String postNode) {
        this.postNode = postNode;
    }

    public String getNodeInitType() {
        return this.nodeInitType;
    }

    public void setNodeInitType(String nodeInitType) {
        this.nodeInitType = nodeInitType;
    }

    public String getCloseType() {
        return this.closeType;
    }

    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }

    public String getCloseCondition() {
        return this.closeCondition;
    }

    public void setCloseCondition(String closeCondition) {
        this.closeCondition = closeCondition;
    }

    public String getHasSubFlow() {
        return this.hasSubFlow;
    }

    public void setHasSubFlow(String hasSubFlow) {
        this.hasSubFlow = hasSubFlow;
    }

    public String getSubFlowStandId() {
        return this.subFlowStandId;
    }

    public void setSubFlowStandId(String subFlowStandId) {
        this.subFlowStandId = subFlowStandId;
    }
    
    public String getStartScheduleCol() {
        return startScheduleCol;
    }
    
    public void setStartScheduleCol(String startScheduleCol) {
        this.startScheduleCol = startScheduleCol;
    }
    
    public String getStartScheduleInfoType() {
        return startScheduleInfoType;
    }
    
    public void setStartScheduleInfoType(String startScheduleInfoType) {
        this.startScheduleInfoType = startScheduleInfoType;
    }

    public String getEndScheduleCol() {
        return endScheduleCol;
    }
    
    public void setEndScheduleCol(String endScheduleCol) {
        this.endScheduleCol = endScheduleCol;
    }
    
    public String getEndScheduleInfoType() {
        return endScheduleInfoType;
    }
    
    public void setEndScheduleInfoType(String endScheduleInfoType) {
        this.endScheduleInfoType = endScheduleInfoType;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
