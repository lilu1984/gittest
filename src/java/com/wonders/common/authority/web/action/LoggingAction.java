package com.wonders.common.authority.web.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.service.AuthorityManager;
import com.wonders.common.authority.service.UserManager;
import com.wonders.esframework.common.exception.BusinessException;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;

/**
 * 用户登录和注销的Action。
 */

public class LoggingAction extends DispatchAction {

	/** 日志 */
	private static Logger		logger	= Logger.getLogger(LoggingAction.class);

	private UserManager			userManager;

	private AuthorityManager	authorityManager;

	/**
	 * 用户登录。
	 */
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
			
			
			String logonName = request.getParameter("logonName");
			String password = request.getParameter("password");

			// 清除所有session信息
			this.removeAllAttributes(request.getSession());

			// 用户验证
			Object obj = userManager.logon(logonName, password);

			UserInfo userInfo = null;
			if (obj instanceof UserInfo) {
				// 用户验证通过
				userInfo = (UserInfo) obj;

				// 将用户信息放入session中
				request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

				// 取得用户的权限列表(包括：自己私有的、所属组的)
				List list = userManager.getAllAuthorityListOfUser(userInfo.getUserId());

				if (list == null || list.size() < 1) {
					if (userInfo.getUserId().equals(AuthorityConstants.SUPER_USER_ID) == false) {
						throw new BusinessException("您没有使用本系统的权限！");
					}
				}

				// 将用户权限放入session中
				setUserAuthorityIntoSession(request.getSession(), list);

				// 跳转到主页面
				return mapping.findForward("success");
			} else {
				// 用户验证不通过
				String errMsg = (String) obj;
				logger.info(errMsg + "登录名：" + logonName);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
				return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
			}
		} catch (BusinessException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(ex.getMessage(), ex);
			}
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, BaseAction.formatBusinessExceptionMsg(ex.getMessage()));
			return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, BaseAction.getExceptionMsg());
			return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
		}
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
	 * 到登录页面。
	 */
	public ActionForward toLogon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 清除所有session信息
		this.removeAllAttributes(request.getSession());

		// 转到登录页面
		return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
	}

	/**
	 * 到banner页面。
	 */
	public ActionForward toBanner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 转到登录页面
		return mapping.findForward("toBanner");
	}

	/**
	 * 到foot页面。
	 */
	public ActionForward toFoot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 转到登录页面
		return mapping.findForward("toFoot");
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

	/**
	 * 将用户的权限信息放到session中
	 * 
	 * @param session
	 *            HttpSession
	 * @param authorityList
	 *            用户的所有权限列表
	 * @throws Exception
	 */
	private void setUserAuthorityIntoSession(HttpSession session, List authorityList) throws Exception {
		List menuList = new ArrayList();

		// 设置用户菜单权限到Session中
		session.removeAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU);
		session.setAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU, menuList);

		if (authorityList == null || authorityList.size() < 1) {
			return;
		}

		for (Iterator iter = authorityList.iterator(); iter.hasNext();) {
			AuthorityTree AuthorityTree = (AuthorityTree) iter.next();
			menuList.add(AuthorityTree.getNodeId());
		}
	}

	// private void insertLog(Integer logType) {
	// LogInfo logInfo = new LogInfo();
	// logInfo.set
	//
	// }

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setAuthorityManager(AuthorityManager authorityManager) {
		this.authorityManager = authorityManager;
	}
}
