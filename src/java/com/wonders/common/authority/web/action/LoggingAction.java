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
 * �û���¼��ע����Action��
 */

public class LoggingAction extends DispatchAction {

	/** ��־ */
	private static Logger		logger	= Logger.getLogger(LoggingAction.class);

	private UserManager			userManager;

	private AuthorityManager	authorityManager;

	/**
	 * �û���¼��
	 */
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
			
			
			String logonName = request.getParameter("logonName");
			String password = request.getParameter("password");

			// �������session��Ϣ
			this.removeAllAttributes(request.getSession());

			// �û���֤
			Object obj = userManager.logon(logonName, password);

			UserInfo userInfo = null;
			if (obj instanceof UserInfo) {
				// �û���֤ͨ��
				userInfo = (UserInfo) obj;

				// ���û���Ϣ����session��
				request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

				// ȡ���û���Ȩ���б�(�������Լ�˽�еġ��������)
				List list = userManager.getAllAuthorityListOfUser(userInfo.getUserId());

				if (list == null || list.size() < 1) {
					if (userInfo.getUserId().equals(AuthorityConstants.SUPER_USER_ID) == false) {
						throw new BusinessException("��û��ʹ�ñ�ϵͳ��Ȩ�ޣ�");
					}
				}

				// ���û�Ȩ�޷���session��
				setUserAuthorityIntoSession(request.getSession(), list);

				// ��ת����ҳ��
				return mapping.findForward("success");
			} else {
				// �û���֤��ͨ��
				String errMsg = (String) obj;
				logger.info(errMsg + "��¼����" + logonName);
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
	 * �û��ǳ���
	 */
	public ActionForward logoff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �������session��Ϣ
		this.removeAllAttributes(request.getSession());

		// ת����¼ҳ��
		return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
	}

	/**
	 * ����¼ҳ�档
	 */
	public ActionForward toLogon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �������session��Ϣ
		this.removeAllAttributes(request.getSession());

		// ת����¼ҳ��
		return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
	}

	/**
	 * ��bannerҳ�档
	 */
	public ActionForward toBanner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ת����¼ҳ��
		return mapping.findForward("toBanner");
	}

	/**
	 * ��footҳ�档
	 */
	public ActionForward toFoot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ת����¼ҳ��
		return mapping.findForward("toFoot");
	}

	/**
	 * ���Session������Ϣ
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
	 * ���û���Ȩ����Ϣ�ŵ�session��
	 * 
	 * @param session
	 *            HttpSession
	 * @param authorityList
	 *            �û�������Ȩ���б�
	 * @throws Exception
	 */
	private void setUserAuthorityIntoSession(HttpSession session, List authorityList) throws Exception {
		List menuList = new ArrayList();

		// �����û��˵�Ȩ�޵�Session��
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
