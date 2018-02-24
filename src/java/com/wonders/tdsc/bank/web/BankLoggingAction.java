package com.wonders.tdsc.bank.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.common.util.StringUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.common.GlobalConstants;

public class BankLoggingAction extends DispatchAction {

	/** 日志 */
	private static Logger logger = Logger.getLogger(BankLoggingAction.class);

	public ActionForward bankLogon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {

			//YH0000000000XX,XX为dic_bank中的code
			String bankId = (String) request.getSession().getAttribute("caNum");
			if(StringUtils.isBlank(bankId)){
				bankId = request.getParameter("caNum");
			}
			
			// 清除所有session信息
			this.removeAllAttributes(request.getSession());
			
			String bankName = getBankName(Integer.parseInt(bankId));
			
			if (StringUtils.isBlank(bankName)) {
				String errMsg = "登录不成功，请联系系统管理员！";
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
				return mapping.findForward("tdscCaLogin");
			}
			
			request.getSession().setAttribute("bankName", bankName);
			request.getSession().setAttribute("bankId", Integer.parseInt(bankId)+"");

			ActionForward af = new ActionForward("bankApp.do?method=queryTdscBankAppList", true);
			return af;

		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}

	/**
	 * 用户登出。
	 */
	public ActionForward logoff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 清除所有session信息
		this.removeAllAttributes(request.getSession());

		// 转到登录页面
		return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
	}

	/**
	 * 清除Session所有信息
	 * 
	 * @param session
	 *            HttpSession
	 */
	private void removeAllAttributes(HttpSession session) {
		if (session == null)
			return;

		java.util.Enumeration enu = session.getAttributeNames();
		while (enu.hasMoreElements()) {
			session.removeAttribute((String) enu.nextElement());
		}
	}

	private String getBankName(int bankId) {
		Connection conn = ConnectionManager.getInstance().getConnection();
		String sql = "select * from DIC_BANK where DIC_CODE='" + bankId + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				String tmp =  StringUtil.ISO88591toGBK(rs.getString("DIC_DESCRIBE"));
				return tmp;
			}

		} catch (Exception e) {
			// Nothing to do
		}
		return null;
	}
}
