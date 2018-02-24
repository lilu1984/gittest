package com.wonders.tdsc.out.service.bean;

import java.sql.ResultSet;

public class TdzpBiddingInfoBean {
	// �������ĹҾ�����Ϣ��TDZP_JMXXB��

	private String	id;		// Ψһ��ʶ
	private String	baseinfoid; // ������Ϣid
	private String	sqdw;		// ����λ
	private String	sqsj;		// ��������ʱ��
	private String	fddbr;		// ����������
	private String	wtdbr;		// ί�д�����
	private String	jnje;		// ���ɽ��
	private String	jnsj;		// ����ʱ��
	private String	csbj;		// ��ʼ����
	private String	bjcs;		// ���۴���
	private String	bzjsj;		// ���ɽ��˻�ʱ��
	private String	jmsj;		// ����ʱ��

	public static TdzpBiddingInfoBean rs2Bean(ResultSet rs) {
		TdzpBiddingInfoBean bean = null;
		try {
			if (rs != null) {
				bean = new TdzpBiddingInfoBean();

				bean.setId(rs.getString("id"));
				bean.setBaseinfoid(rs.getString("baseinfoid"));
				bean.setSqdw(rs.getString("sqdw"));
				bean.setSqsj(rs.getString("sqsj"));
				bean.setFddbr(rs.getString("fddbr"));
				bean.setWtdbr(rs.getString("wtdbr"));
				bean.setJnje(rs.getString("jnje"));
				bean.setJnsj(rs.getString("jnsj"));
				bean.setCsbj(rs.getString("csbj"));
				bean.setBjcs(rs.getString("bjcs"));
				bean.setBzjsj(rs.getString("bzjsj"));
				bean.setJmsj(rs.getString("jmsj"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseinfoid() {
		return baseinfoid;
	}

	public void setBaseinfoid(String baseinfoid) {
		this.baseinfoid = baseinfoid;
	}

	public String getSqdw() {
		return sqdw;
	}

	public void setSqdw(String sqdw) {
		this.sqdw = sqdw;
	}

	public String getFddbr() {
		return fddbr;
	}

	public void setFddbr(String fddbr) {
		this.fddbr = fddbr;
	}

	public String getWtdbr() {
		return wtdbr;
	}

	public void setWtdbr(String wtdbr) {
		this.wtdbr = wtdbr;
	}

	public String getJnje() {
		return jnje;
	}

	public void setJnje(String jnje) {
		this.jnje = jnje;
	}

	public String getCsbj() {
		return csbj;
	}

	public void setCsbj(String csbj) {
		this.csbj = csbj;
	}

	public String getBjcs() {
		return bjcs;
	}

	public void setBjcs(String bjcs) {
		this.bjcs = bjcs;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getJnsj() {
		return jnsj;
	}

	public void setJnsj(String jnsj) {
		this.jnsj = jnsj;
	}

	public String getBzjsj() {
		return bzjsj;
	}

	public void setBzjsj(String bzjsj) {
		this.bzjsj = bzjsj;
	}

	public String getJmsj() {
		return jmsj;
	}

	public void setJmsj(String jmsj) {
		this.jmsj = jmsj;
	}

}
