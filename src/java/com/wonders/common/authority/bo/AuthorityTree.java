package com.wonders.common.authority.bo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class AuthorityTree extends BaseBO {

    /** identifier field */
    private String nodeId;

    /** nullable persistent field */
    private String nodeDesc;

    /** nullable persistent field */
    private Integer nodeLevel;

    /** nullable persistent field */
    private String nodePid;

    /** nullable persistent field */
    private Integer nodeOrder;

    /**  «∑Ò—°÷– */
    private boolean checked;

    /** default constructor */
    public AuthorityTree() {
    }

    /** minimal constructor */
    public AuthorityTree(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeDesc() {
        return this.nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public Integer getNodeLevel() {
        return this.nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getNodePid() {
        return this.nodePid;
    }

    public void setNodePid(String nodePid) {
        this.nodePid = nodePid;
    }

    public Integer getNodeOrder() {
        return this.nodeOrder;
    }

    public void setNodeOrder(Integer nodeOrder) {
        this.nodeOrder = nodeOrder;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String toString() {
        return new ToStringBuilder(this).append("nodeId", getNodeId()).toString();
    }

}
