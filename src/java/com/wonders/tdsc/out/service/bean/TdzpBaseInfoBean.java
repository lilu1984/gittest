package com.wonders.tdsc.out.service.bean;

import java.sql.ResultSet;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;

public class TdzpBaseInfoBean {

	private static String	GUPAI		= "3104";
	private static String	PAIMAI		= "3103";
	private static String	ZHAOBIAO	= "3107";

	// 土地招拍挂基本信息表(TDZP_BASEINFO)

	private String			id;					// 唯一标识, appId
	private String			zdbh;					// 宗地编号
	private String			xmmc;					// 项目名称
	private String			ydqy;					// 用地区域
	private String			ydlx;					// 用地类型 1:特定用地2:商业用地3:住宅用地4:工业用地
	private String			ydxz;					// 用地性质 1:商业用地2:综合用地3:住宅用地4:工业用地5:其他用地
	private String			ydmj;					// 用地面积
	private String			ydwz;					// 用地位置
	private String			jsmj;					// 建设面积
	private String			dlydmj;				// 道路用地面积
	private String			ldydmj;				// 绿地用地面积
	private String			tdpg;					// 土地评估

	private String			pgunit;
	private String			pgren;
	private String			pgdate;
	private String			pgmethod;
	private String			pgresult;

	private String			ghxk;					// 规划许可
	private String			tdlx;					// 土地类型 1:土地所用权2:土地使用权3:土地他项权利
	private String			falx;					// 方案类型 1:招标2:拍卖3:挂牌
	private String			jmsqsj;				// 竞买申请开始时间
	private String			jmjzsj;				// 竞买申请截止时间
	private String			jmkssj;				// 竞买开始时间
	private String			jmjssj;				// 竞买结束时间
	private String			ggfbsj;				// 公告发布时间
	private String			bzj;					// 保证金
	private String			state;					// 项目状态 0：正常；1：异常（包含流拍、废标等）

	private static String getDescYdlx(String str) {
		// 1:特定用地2:商业用地3:住宅用地4:工业用地
		String ret = "";
		if ("101".equals(str))
			ret = "4";
		if ("102".equals(str))
			ret = "2";
		return StringUtil.GBKtoISO88591(ret);
	}

	private static String getJmfs(String tranMode) {
		//1:招标2:拍卖3:挂牌
		String str = "";
		if (GUPAI.equals(tranMode))
			str = "3";
		if (PAIMAI.equals(tranMode))
			str = "2";
		if (ZHAOBIAO.equals(tranMode))
			str = "1";
		return str;
		//return StringUtil.GBKtoISO88591(str);
	}

	public static TdzpBaseInfoBean rs2Bean(ResultSet rs) {
		TdzpBaseInfoBean bean = null;
		try {
			if (rs != null) {
				bean = new TdzpBaseInfoBean();
				bean.setId(rs.getString("APPID"));
				bean.setZdbh(rs.getString("ZDBH"));
				bean.setXmmc(rs.getString("XMMC"));
				bean.setYdqy(StringUtil.GBKtoISO88591(getOrganName(rs.getString("YDQY"))));
				bean.setYdlx(StringUtil.GBKtoISO88591(getDescYdlx(rs.getString("YDLX"))));
				bean.setYdxz(StringUtil.GBKtoISO88591(getDescYdlx(rs.getString("YDXZ"))));
				bean.setYdmj(rs.getString("YDMJ"));
				bean.setYdwz(rs.getString("YDWZ"));
				bean.setJsmj(rs.getString("JSMJ"));
				bean.setDlydmj(rs.getString("DLYDMJ"));
				bean.setLdydmj(rs.getString("LDYDMJ"));

				String pgUnit = "评估单位：" + getPgUnitStr(rs.getString("PGUNIT"));
				String pgRen = "评估人：" + StringUtil.ISO88591toGBK(rs.getString("PGREN"));
				String pgMethod = "评估方法：" + getPgMethodStr(rs.getString("PGMETHOD"));
				String gpDate = "评估日期：" + DateUtil.date2String(rs.getDate("PGDATE"), "yyyy/MM/dd");
				String pgResult = "评估结果：" + rs.getString("PGRESULT") + "万元";
				bean.setTdpg(StringUtil.GBKtoISO88591(pgUnit + "  " + pgRen + "  " + gpDate + "  " + pgMethod + "  " + pgResult));

				//bean.setTdpg(StringUtil.ISO88591toGBK(rs.getString("TDPG")));
				
				bean.setGhxk(rs.getString("GHXK"));
				// bean.setTdlx(StringUtil.GBKtoISO88591("土地使用权"));
				bean.setTdlx("2");
				bean.setFalx(StringUtil.GBKtoISO88591(getJmfs(rs.getString("FALX"))));
				bean.setJmsqsj(rs.getString("Jmsqsj"));
				bean.setJmjzsj(rs.getString("Jmjzsj"));
				bean.setJmkssj(rs.getString("Jmkssj"));
				bean.setJmjssj(rs.getString("Jmjssj"));
				bean.setGgfbsj(rs.getString("Ggfbsj"));
				bean.setBzj(rs.getString("BZJ"));
				bean.setState(rs.getString("STATE"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	private static String getPgUnitStr(String str) {
		String retStr = "";
		if("01".equals(str))
			retStr = "江苏金宁达不动产评估咨询";
		if("02".equals(str))
			retStr = "江苏同方土地房地产评估有";
		if("03".equals(str))
			retStr = "江苏苏信房地产评估咨询有";
		if("04".equals(str))
			retStr = "江苏苏地行土地房地产评估";
		return retStr;
	}

	private static String getOrganName(String str) {
		String retStr = "";
		if("1".equals(str))
			retStr = "锡山区";
		if("2".equals(str))
			retStr = "惠山区";
		if("3".equals(str))
			retStr = "滨湖区";
		if("4".equals(str))
			retStr = "无锡新区";
		if("5".equals(str))
			retStr = "崇安区";
		if("6".equals(str))
			retStr = "南长区";
		if("7".equals(str))
			retStr = "北塘区";
		return retStr;
	}

	private static String getPgMethodStr(String vStr) {
		String str = "";
		String[] s = vStr.split(",");
		for (int i = 0; i < s.length; i++) {
			str += getPgMethodContext(s[i]) + "，";
		}
		return str.substring(0, str.length() - 1);
	}

	public static String getPgMethodContext(String sId) {
		String str = "";
		if ("01".equals(sId))
			str = "假设开发法";
		if ("02".equals(sId))
			str = "收益还原法";
		if ("03".equals(sId))
			str = "市场比较法";
		if ("04".equals(sId))
			str = "成本逼近法";
		if ("05".equals(sId))
			str = "基准地价系数修正法";
		return str;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZdbh() {
		return zdbh;
	}

	public void setZdbh(String zdbh) {
		this.zdbh = zdbh;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getYdqy() {
		return ydqy;
	}

	public void setYdqy(String ydqy) {
		this.ydqy = ydqy;
	}

	public String getYdlx() {
		return ydlx;
	}

	public void setYdlx(String ydlx) {
		this.ydlx = ydlx;
	}

	public String getYdxz() {
		return ydxz;
	}

	public void setYdxz(String ydxz) {
		this.ydxz = ydxz;
	}

	public String getYdmj() {
		return ydmj;
	}

	public void setYdmj(String ydmj) {
		this.ydmj = ydmj;
	}

	public String getYdwz() {
		return ydwz;
	}

	public void setYdwz(String ydwz) {
		this.ydwz = ydwz;
	}

	public String getJsmj() {
		return jsmj;
	}

	public void setJsmj(String jsmj) {
		this.jsmj = jsmj;
	}

	public String getDlydmj() {
		return dlydmj;
	}

	public void setDlydmj(String dlydmj) {
		this.dlydmj = dlydmj;
	}

	public String getLdydmj() {
		return ldydmj;
	}

	public void setLdydmj(String ldydmj) {
		this.ldydmj = ldydmj;
	}

	public String getTdpg() {
		return tdpg;
	}

	public void setTdpg(String tdpg) {
		this.tdpg = tdpg;
	}

	public String getGhxk() {
		return ghxk;
	}

	public void setGhxk(String ghxk) {
		this.ghxk = ghxk;
	}

	public String getTdlx() {
		return tdlx;
	}

	public void setTdlx(String tdlx) {
		this.tdlx = tdlx;
	}

	public String getFalx() {
		return falx;
	}

	public void setFalx(String falx) {
		this.falx = falx;
	}

	public String getJmsqsj() {
		return jmsqsj;
	}

	public void setJmsqsj(String jmsqsj) {
		this.jmsqsj = jmsqsj;
	}

	public String getJmjzsj() {
		return jmjzsj;
	}

	public void setJmjzsj(String jmjzsj) {
		this.jmjzsj = jmjzsj;
	}

	public String getJmkssj() {
		return jmkssj;
	}

	public void setJmkssj(String jmkssj) {
		this.jmkssj = jmkssj;
	}

	public String getJmjssj() {
		return jmjssj;
	}

	public void setJmjssj(String jmjssj) {
		this.jmjssj = jmjssj;
	}

	public String getGgfbsj() {
		return ggfbsj;
	}

	public void setGgfbsj(String ggfbsj) {
		this.ggfbsj = ggfbsj;
	}

	public String getBzj() {
		return bzj;
	}

	public void setBzj(String bzj) {
		this.bzj = bzj;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPgunit() {
		return pgunit;
	}

	public void setPgunit(String pgunit) {
		this.pgunit = pgunit;
	}

	public String getPgren() {
		return pgren;
	}

	public void setPgren(String pgren) {
		this.pgren = pgren;
	}

	public String getPgdate() {
		return pgdate;
	}

	public void setPgdate(String pgdate) {
		this.pgdate = pgdate;
	}

	public String getPgmethod() {
		return pgmethod;
	}

	public void setPgmethod(String pgmethod) {
		this.pgmethod = pgmethod;
	}

	public String getPgresult() {
		return pgresult;
	}

	public void setPgresult(String pgresult) {
		this.pgresult = pgresult;
	}

}
