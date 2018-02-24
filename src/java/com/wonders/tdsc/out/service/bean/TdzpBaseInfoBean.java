package com.wonders.tdsc.out.service.bean;

import java.sql.ResultSet;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;

public class TdzpBaseInfoBean {

	private static String	GUPAI		= "3104";
	private static String	PAIMAI		= "3103";
	private static String	ZHAOBIAO	= "3107";

	// �������Ĺһ�����Ϣ��(TDZP_BASEINFO)

	private String			id;					// Ψһ��ʶ, appId
	private String			zdbh;					// �ڵر��
	private String			xmmc;					// ��Ŀ����
	private String			ydqy;					// �õ�����
	private String			ydlx;					// �õ����� 1:�ض��õ�2:��ҵ�õ�3:סլ�õ�4:��ҵ�õ�
	private String			ydxz;					// �õ����� 1:��ҵ�õ�2:�ۺ��õ�3:סլ�õ�4:��ҵ�õ�5:�����õ�
	private String			ydmj;					// �õ����
	private String			ydwz;					// �õ�λ��
	private String			jsmj;					// �������
	private String			dlydmj;				// ��·�õ����
	private String			ldydmj;				// �̵��õ����
	private String			tdpg;					// ��������

	private String			pgunit;
	private String			pgren;
	private String			pgdate;
	private String			pgmethod;
	private String			pgresult;

	private String			ghxk;					// �滮���
	private String			tdlx;					// �������� 1:��������Ȩ2:����ʹ��Ȩ3:��������Ȩ��
	private String			falx;					// �������� 1:�б�2:����3:����
	private String			jmsqsj;				// �������뿪ʼʱ��
	private String			jmjzsj;				// ���������ֹʱ��
	private String			jmkssj;				// ����ʼʱ��
	private String			jmjssj;				// �������ʱ��
	private String			ggfbsj;				// ���淢��ʱ��
	private String			bzj;					// ��֤��
	private String			state;					// ��Ŀ״̬ 0��������1���쳣���������ġ��ϱ�ȣ�

	private static String getDescYdlx(String str) {
		// 1:�ض��õ�2:��ҵ�õ�3:סլ�õ�4:��ҵ�õ�
		String ret = "";
		if ("101".equals(str))
			ret = "4";
		if ("102".equals(str))
			ret = "2";
		return StringUtil.GBKtoISO88591(ret);
	}

	private static String getJmfs(String tranMode) {
		//1:�б�2:����3:����
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

				String pgUnit = "������λ��" + getPgUnitStr(rs.getString("PGUNIT"));
				String pgRen = "�����ˣ�" + StringUtil.ISO88591toGBK(rs.getString("PGREN"));
				String pgMethod = "����������" + getPgMethodStr(rs.getString("PGMETHOD"));
				String gpDate = "�������ڣ�" + DateUtil.date2String(rs.getDate("PGDATE"), "yyyy/MM/dd");
				String pgResult = "���������" + rs.getString("PGRESULT") + "��Ԫ";
				bean.setTdpg(StringUtil.GBKtoISO88591(pgUnit + "  " + pgRen + "  " + gpDate + "  " + pgMethod + "  " + pgResult));

				//bean.setTdpg(StringUtil.ISO88591toGBK(rs.getString("TDPG")));
				
				bean.setGhxk(rs.getString("GHXK"));
				// bean.setTdlx(StringUtil.GBKtoISO88591("����ʹ��Ȩ"));
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
			retStr = "���ս����ﲻ����������ѯ";
		if("02".equals(str))
			retStr = "����ͬ�����ط��ز�������";
		if("03".equals(str))
			retStr = "�������ŷ��ز�������ѯ��";
		if("04".equals(str))
			retStr = "�����յ������ط��ز�����";
		return retStr;
	}

	private static String getOrganName(String str) {
		String retStr = "";
		if("1".equals(str))
			retStr = "��ɽ��";
		if("2".equals(str))
			retStr = "��ɽ��";
		if("3".equals(str))
			retStr = "������";
		if("4".equals(str))
			retStr = "��������";
		if("5".equals(str))
			retStr = "�簲��";
		if("6".equals(str))
			retStr = "�ϳ���";
		if("7".equals(str))
			retStr = "������";
		return retStr;
	}

	private static String getPgMethodStr(String vStr) {
		String str = "";
		String[] s = vStr.split(",");
		for (int i = 0; i < s.length; i++) {
			str += getPgMethodContext(s[i]) + "��";
		}
		return str.substring(0, str.length() - 1);
	}

	public static String getPgMethodContext(String sId) {
		String str = "";
		if ("01".equals(sId))
			str = "���迪����";
		if ("02".equals(sId))
			str = "���滹ԭ��";
		if ("03".equals(sId))
			str = "�г��ȽϷ�";
		if ("04".equals(sId))
			str = "�ɱ��ƽ���";
		if ("05".equals(sId))
			str = "��׼�ؼ�ϵ��������";
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
