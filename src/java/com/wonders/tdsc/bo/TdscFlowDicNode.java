package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscFlowDicNode implements Serializable {

    /** identifier field */
    private String nodeId;

    /** nullable persistent field */
    private String nodeName;

    /** full constructor */
    public TdscFlowDicNode(String nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    /** default constructor */
    public TdscFlowDicNode() {
    }

    /** minimal constructor */
    public TdscFlowDicNode(String nodeId) {
        this.nodeId = nodeId;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("nodeId", getNodeId())
            .toString();
    }

}
