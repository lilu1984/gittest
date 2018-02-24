package com.wonders.tdsc.out.service.bean;

import java.sql.ResultSet;

public class TdzpTransactInfoBean {
	// �������Ĺҳɽ���Ϣ��TDZP_CJXXB��
	private String	id;		// Ψһ��ʶ
	private String	baseinfoid; // ������Ϣid
	private String	csbj;		// ��ʼ����
	private String	bjfd;		// ���۷���
	private String	bjcs;		// ���۴���
	private String	cjje;		// �ɽ��۸�
	private String	jdr;		// ������
	private String	cjfs;		// �ɽ���ʽ
	private String	cjsj;		// �ɽ�ʱ��
	private String	qrssj;		// ȷ���鷢��ʱ��
	private String	bzjsj;		// ��֤���˻�ʱ��

	public static TdzpTransactInfoBean rs2Bean(ResultSet rs) {
		TdzpTransactInfoBean bean = null;
		try {
			if (rs != null) {
				bean = new TdzpTransactInfoBean();

				bean.setId(rs.getString("id"));
				bean.setBaseinfoid(rs.getString("baseinfoid"));
				bean.setCsbj(rs.getString("csbj"));
				bean.setBjfd(rs.getString("bjfd"));
				bean.setBjcs(rs.getString("bjcs"));
				bean.setCjje(rs.getString("cjje"));
				bean.setJdr(rs.getString("jdr"));
				bean.setCjfs(rs.getString("cjfs"));
				bean.setCjsj(rs.getString("cjsj"));
				bean.setQrssj(rs.getString("qrssj"));
				bean.setBzjsj(rs.getString("bzjsj"));

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

	public String getCsbj() {
		return csbj;
	}

	public void setCsbj(String csbj) {
		this.csbj = csbj;
	}

	public String getBjfd() {
		return bjfd;
	}

	public void setBjfd(String bjfd) {
		this.bjfd = bjfd;
	}

	public String getBjcs() {
		return bjcs;
	}

	public void setBjcs(String bjcs) {
		this.bjcs = bjcs;
	}

	public String getCjje() {
		return cjje;
	}

	public void setCjje(String cjje) {
		this.cjje = cjje;
	}

	public String getJdr() {
		return jdr;
	}

	public void setJdr(String jdr) {
		this.jdr = jdr;
	}

	public String getCjfs() {
		return cjfs;
	}

	public void setCjfs(String cjfs) {
		this.cjfs = cjfs;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getQrssj() {
		return qrssj;
	}

	public void setQrssj(String qrssj) {
		this.qrssj = qrssj;
	}

	public String getBzjsj() {
		return bzjsj;
	}

	public void setBzjsj(String bzjsj) {
		this.bzjsj = bzjsj;
	}
}
