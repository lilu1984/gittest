package com.wonders.tdsc.out.service.bean;

import java.sql.ResultSet;

public class TdzpExceptionInfoBean {
	// 项目异常信息（TDZP _XMYCXX）

	private String	id;		// 唯一标识
	private String	baseinfoid; // 基本信息标识
	private String	zdbh;		// 宗地编号
	private String	yclx;		// 异常类型
	private String	yccs_time;	// 异常产生时间
	private String	yc_info;	// 异常信息描述
	private String	ycclxx;	// 异常处理信息
	private String	yccl_time;	// 异常处理时间

	public static TdzpExceptionInfoBean rs2Bean(ResultSet rs) {
		TdzpExceptionInfoBean bean = null;
		try {
			if (rs != null) {
				bean = new TdzpExceptionInfoBean();

				bean.setId(rs.getString("id"));
				bean.setBaseinfoid(rs.getString("baseinfoid"));
				bean.setZdbh(rs.getString("zdbh"));
				bean.setYclx(rs.getString("yclx"));
				bean.setYccs_time(rs.getString("yccs_time"));
				bean.setYc_info(rs.getString("yc_info"));
				bean.setYcclxx(rs.getString("ycclxx"));
				bean.setYccl_time(rs.getString("yccl_time"));
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

	public String getZdbh() {
		return zdbh;
	}

	public void setZdbh(String zdbh) {
		this.zdbh = zdbh;
	}

	public String getYclx() {
		return yclx;
	}

	public void setYclx(String yclx) {
		this.yclx = yclx;
	}

	public String getYccs_time() {
		return yccs_time;
	}

	public void setYccs_time(String yccs_time) {
		this.yccs_time = yccs_time;
	}

	public String getYc_info() {
		return yc_info;
	}

	public void setYc_info(String yc_info) {
		this.yc_info = yc_info;
	}

	public String getYcclxx() {
		return ycclxx;
	}

	public void setYcclxx(String ycclxx) {
		this.ycclxx = ycclxx;
	}

	public String getYccl_time() {
		return yccl_time;
	}

	public void setYccl_time(String yccl_time) {
		this.yccl_time = yccl_time;
	}

}
