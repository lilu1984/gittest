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
 * 用户登录和注销的Action。
 */
public class LoggingAction extends DispatchAction {

	/** 日志 */
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
	 * 用户登录。
	 * 
	 * 如果有session,则直接返回首页面
	 */
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			String strTokenId = request.getParameter("Token");

			String personId = "";

			if (!StringUtil.isEmpty(strTokenId)) {
				String context = callURL(strTokenId);
				// 解析 判断登录是否成功
				if (StringUtil.isEmpty(context)) {
					String errMsg = "请登录后使用本系统。";
					request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
					return mapping.findForward("caNotLogin");
				}
				String errorCode = context.substring(context.indexOf("<ErrorCode>") + "<ErrorCode>".length(), context.indexOf("</ErrorCode>"));
				if ("1".equals(errorCode)) {
					String errMsg = "请登录后使用本系统。";
					request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
					return mapping.findForward("caNotLogin");
				}
				
				// ErrorCode is 0 , login success.
				if ("0".equals(errorCode)) {
					// 得到personId，登录成功。
					// 如果该用户在系统中还未创建，显示提示信息“请联系系统管理员”。
					personId = context.substring(context.indexOf("<PersonID>") + "<PersonID>".length(), context.indexOf("</PersonID>"));

					// 清除所有session信息
					this.removeAllAttributes(request.getSession());

					// 用户验证
					Object obj = userManager.logonByPersonId(personId);

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
								//throw new BusinessException("您没有使用本系统的权限！");
								String errMsg = "您没有使用本系统的权限";
								request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
								return mapping.findForward("caNotLogin");
							}
						}

						// 用户信息
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

						// 菜单权限
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_MENU));

						// 按钮权限
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_BUTTON));

						// 区县权限
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_QX, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_QX));
						// 工作流权限
						request.getSession().setAttribute(GlobalConstants.SESSION_USER_WORK_FLOW, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_WF));

						// 跳转到主页面
						return mapping.findForward("success");
					} else {
						// 用户验证不通过
						String errMsg = "用户未同步到本系统，请一个工作日后重新登录。";
						logger.info(errMsg);
						request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);

						return mapping.findForward("caNotLogin");
					}
				}

			} else {
				// 预留登录接口，用户系统调试，万达原有系统代码；
				
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
				// 清除所有session信息
				this.removeAllAttributes(request.getSession());

				// 用户验证
				Object obj = null;
				if("1".equals(newCa)){
					obj = userManager.logon(logonName);
				}else{
					obj = userManager.logon(logonName, password);
				}
				
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

					// 用户信息
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

					// 菜单权限
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_MENU));

					// 按钮权限
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_BUTTON));

					// 区县权限
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_QX, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_QX));
					// 工作流权限
					request.getSession().setAttribute(GlobalConstants.SESSION_USER_WORK_FLOW, filterrAuthorityByType(list, AuthorityConstants.CATEGORY_ID_WF));

					// 跳转到主页面
					return mapping.findForward("success");
				} else {
					// 用户验证不通过
					String errMsg = (String) obj;
					logger.info(errMsg + "登录名：" + logonName);
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
	 * 用户登出。
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

	/**
	 * 根据类型过滤权限
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
					// 如果是区县或者工作流权限，取其值
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
