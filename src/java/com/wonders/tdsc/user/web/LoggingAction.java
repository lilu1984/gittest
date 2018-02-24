package com.wonders.tdsc.user.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.service.UserManager;
import com.wonders.esframework.common.exception.BusinessException;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.user.model.AuthorityResource;
import com.wonders.tdsc.user.service.AuthorityResourceManager;

/**
 * �û���¼��ע����Action��
 */
public class LoggingAction extends DispatchAction {

	/** ��־ */
	private static Logger				logger	= Logger.getLogger(LoggingAction.class);

	private AuthorityResourceManager	resourceManager;

	private UserManager					userManager;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setResourceManager(AuthorityResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * CA Login
	 * 
	 * @param tokenId
	 */
	public String callURL(String tokenId) {
		String strUrl = "http://192.168.10.6/Sys/Flex/XmlService.ashx?SessionID=" + tokenId + "&events=GetOperatorInfo";
		String result = "";
		try {
			URL U = new URL(strUrl);
			URLConnection connection = U.openConnection();
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("get context is : " + result);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * �û���¼��
	 * 
	 * �����session,��ֱ�ӷ�����ҳ��
	 */
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			String strTokenId = request.getParameter("Token");

			String personId = "";

			if (!StringUtil.isEmpty(strTokenId)) {
				String context = callURL(strTokenId);
				// ���� �жϵ�¼�Ƿ�ɹ�
				if (StringUtil.isEmpty(context)) {
					String errMsg = "���¼��ʹ�ñ�ϵͳ��";
					request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
					return mapping.findForward("caNotLogin");
				}
				String errorCode = context.substring(context.indexOf("<ErrorCode>") + "<ErrorCode>".length(), context.indexOf("</ErrorCode>"));
				if ("1".equals(errorCode)) {
					String errMsg = "���¼��ʹ�ñ�ϵͳ��";
					request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
					return mapping.findForward("caNotLogin");
				}
				
				// ErrorCode is 0 , login success.
				if ("0".equals(errorCode)) {
					// �õ�personId����¼�ɹ���
					// ������û���ϵͳ�л�δ��������ʾ��ʾ��Ϣ������ϵϵͳ����Ա����
					personId = context.substring(context.indexOf("<PersonID>") + "<PersonID>".length(), context.indexOf("</PersonID>"));

					// �������session��Ϣ
					this.removeAllAttributes(request.getSession());

					// �û���֤
					Object obj = userManager.logonByPersonId(personId);

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
								//throw new BusinessException("��û��ʹ�ñ�ϵͳ��Ȩ�ޣ�");
								String errMsg = "��û��ʹ�ñ�ϵͳ��Ȩ��";
								request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
								return mapping.findForward("caNotLogin");
							}
						}

						// �û���Ϣ
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

						// �˵�Ȩ��
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_MENU));

						// ��ťȨ��
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_BUTTON));

						// ����Ȩ��
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_QX, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_QX));
						// ������Ȩ��
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_WORK_FLOW, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_WF));

						// ��ת����ҳ��
						return mapping.findForward("success");
					} else {
						// �û���֤��ͨ��
						String errMsg = "�û�δͬ������ϵͳ����һ�������պ����µ�¼��";
						logger.info(errMsg);
						request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);

						return mapping.findForward("caNotLogin");
					}
				}

			} else {
				// Ԥ����¼�ӿڣ��û�ϵͳ���ԣ����ԭ��ϵͳ���룻
				
				String logonName = (String) request.getSession().getAttribute("logonName");
				if(StringUtils.isBlank(logonName)){
					logonName = request.getParameter("logonName");
				}
				//String password = "123456";
				String password = (String) request.getSession().getAttribute("password");
				if(StringUtils.isBlank(password)){
					password = request.getParameter("password");
				}
				String newCa = "0";
				if(request.getSession().getAttribute("newCa")!=null){
					newCa = (String)request.getSession().getAttribute("newCa");
				}
				// �������session��Ϣ
				this.removeAllAttributes(request.getSession());

				// �û���֤
				Object obj = null;
				if("1".equals(newCa)){
					obj = userManager.logon(logonName);
				}else{
					obj = userManager.logon(logonName, password);
				}
				
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

					// �û���Ϣ
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

					// �˵�Ȩ��
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_MENU));

					// ��ťȨ��
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_BUTTON));

					// ����Ȩ��
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_QX, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_QX));
					// ������Ȩ��
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_WORK_FLOW, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_WF));

					// ��ת����ҳ��
					return mapping.findForward("success");
				} else {
					// �û���֤��ͨ��
					String errMsg = (String) obj;
					logger.info(errMsg + "��¼����" + logonName);
					request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
					return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
				}

			}
			return mapping.findForward("caNotLogin");

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
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward logoff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �������session��Ϣ
		this.removeAllAttributes(request.getSession());

		// ת����¼ҳ��
		return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);
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
	 * �������͹���Ȩ��
	 * 
	 * @param authorityList
	 * @param resourceType
	 * @return
	 */
	private List filterrAuthorityByType(List authorityList, String resourceType) {
		List rtnList = new ArrayList();

		if (authorityList == null || authorityList.size() < 1)
			return rtnList;

		List resourceList = resourceManager.queryResourceByType(resourceType);

		if (null == resourceList || resourceList.size() < 1)
			return rtnList;

		for (int i = 0; i < resourceList.size(); i++) {
			AuthorityResource resource = (AuthorityResource) resourceList.get(i);
			for (Iterator iter = authorityList.iterator(); iter.hasNext();) {
				AuthorityTree authoriry = (AuthorityTree) iter.next();
				if (resource.getResourceId().equals(authoriry.getNodeId())) {
					// ��������ػ��߹�����Ȩ�ޣ�ȡ��ֵ
					if (AuthorityConstants.CATEGORY_ID_QX.equals(resourceType) || AuthorityConstants.CATEGORY_ID_WF.equals(resourceType)) {
						if (resource.getValue() != null)
							rtnList.add(resource.getValue());
					} else {
						rtnList.add(resource.getResourceId());
					}
				}
			}
		}

		return rtnList;
	}
}
