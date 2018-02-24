package com.wonders.tdsc.solve.web.form;

import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class TdscBlockSolveForm extends ActionForm {

	private static final long	serialVersionUID	= 1L;

	private String				blockName;
	private String				blockAuditStatus;
	private String				solveId;
	private String				blockId;
	private String				planId;
	private Integer				seqNo;
	private String				question;
	private String				answerContext;
	private String				answerUserId;
	private Timestamp			answerDate;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getSolveId() {
		return solveId;
	}

	public void setSolveId(String solveId) {
		this.solveId = solveId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswerContext() {
		return answerContext;
	}

	public void setAnswerContext(String answerContext) {
		this.answerContext = answerContext;
	}

	public String getAnswerUserId() {
		return answerUserId;
	}

	public void setAnswerUserId(String answerUserId) {
		this.answerUserId = answerUserId;
	}

	public Timestamp getAnswerDate() {
		return answerDate;
	}

	public void setAnswerDate(Timestamp answerDate) {
		this.answerDate = answerDate;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBlockAuditStatus() {
		return blockAuditStatus;
	}

	public void setBlockAuditStatus(String blockAuditStatus) {
		this.blockAuditStatus = blockAuditStatus;
	}

}
