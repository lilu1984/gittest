package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;

public class TdscBlockSolve implements Serializable {

	private static final long	serialVersionUID	= 1476776480067268222L;

	private String				solveId;
	private String				blockId;
	private String				planId;
	private int					seqNo;
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

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
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

}
