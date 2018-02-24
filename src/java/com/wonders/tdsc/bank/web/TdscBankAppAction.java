package com.wonders.tdsc.bank.web;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.common.exception.BusinessException;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bank.service.TdscBankAppService;
import com.wonders.tdsc.bank.web.form.TdscBankAppForm;
import com.wonders.tdsc.bo.TdscBankApp;
import com.wonders.tdsc.bo.condition.TdscBankAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;

public class TdscBankAppAction extends DispatchAction {
	private TdscBankAppService tdscBankAppService;

	public void setTdscBankAppService(TdscBankAppService tdscBankAppService) {
		this.tdscBankAppService = tdscBankAppService;
	}

	/**
	 * 银行人员查询所有本银行的保证金信息
	 */
	public ActionForward queryTdscBankAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBankAppCondition tdscBankAppCondition = new TdscBankAppCondition();
		String queryType = (String) request.getParameter("queryType");
		String bankId = (String) request.getSession().getAttribute("bankId");

		String regex="[a-zA-Z0-9_\u4E00-\u9FA5]*";   //匹配由数字和26个英文字母组成的字符串
		regex="^[A-Za-z0-9]+$";   //匹配由数字和26个英文字母组成的字符串

		boolean result2= Pattern.matches(regex, bankId);
		if(!result2){//判断参数是否合法,不合法转到登陆页面
			System.out.println("certNo参数不合法：value="+bankId);
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "登录不成功，请联系系统管理员！");
			return mapping.findForward("tdscCaLogin");
		}
		
		if (StringUtils.isNotBlank(queryType)) {
			boolean result1= Pattern.matches(regex, queryType);
			if(!result1){//判断参数是否合法,不合法转到登陆页面
				System.out.println("certNo参数不合法：value="+queryType);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "登录不成功，请联系系统管理员！");
				return mapping.findForward("tdscCaLogin");
			}
			
			TdscBankAppForm tdscBankAppForm = (TdscBankAppForm) form;
			bindObject(tdscBankAppCondition, tdscBankAppForm);
		}

		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		tdscBankAppCondition.setOperator(bankId);
		tdscBankAppCondition.setCurrentPage(currentPage);
		tdscBankAppCondition.setOrderKey("payDate");
		tdscBankAppCondition.setOrderType("desc");
		PageList tdscBankAppPageList = tdscBankAppService.queryTdscBankAppList(tdscBankAppCondition);

		request.setAttribute("tdscBankAppPageList", tdscBankAppPageList);
		request.setAttribute("tdscBankAppCondition", tdscBankAppCondition);

		return mapping.findForward("tdscBankAppList");
	}

	public ActionForward toTdscBankApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tdscBankAppId = (String) request.getParameter("id");
		String regex="^[A-Za-z0-9]+$";   //匹配由数字和26个英文字母组成的字符串
		
		TdscBankApp tdscBankApp = null;
		if (!StringUtils.isEmpty(tdscBankAppId)) {
			boolean result= Pattern.matches(regex, tdscBankAppId);
			if(!result){//判断参数是否合法,不合法转到登陆页面
				System.out.println("certNo参数不合法：value="+tdscBankAppId);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "登录不成功，请联系系统管理员！");
				return mapping.findForward("tdscCaLogin");
			}
			
			tdscBankApp = tdscBankAppService.getTdscBankAppById(tdscBankAppId);
		}
		request.setAttribute("tdscBankApp", tdscBankApp);

		return mapping.findForward("toTdscBankApp");
	}

	public ActionForward saveTdscBankApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBankAppForm tdscBankAppForm = (TdscBankAppForm) form;
		String bankId = (String) request.getSession().getAttribute("bankId");
		String operator = bankId;
		String id = request.getParameter("id");
		TdscBankApp tdscBankApp = null;
		if(StringUtils.isNotBlank(id)){
			tdscBankApp = tdscBankAppService.getTdscBankAppById(id);
			if("1".equals(tdscBankApp.getStatus())){
				request.setAttribute("saveMessage", "该竞买保证金已经被关联，无法提交！");
				request.setAttribute("tdscBankApp", tdscBankApp);
				return mapping.findForward("toTdscBankApp");
			}
		}else{
			tdscBankApp = new TdscBankApp();
		}
		bindObject(tdscBankApp, tdscBankAppForm);
		tdscBankApp.setAcceptBank(bankId);// 接收银行
		tdscBankApp.setAcceptBankName((String) request.getSession().getAttribute("bankName"));// 接收银行名称
		tdscBankApp.setOperatingTime(DateUtil.string2Timestamp(DateConvertor.getCurrentDateWithTimeZone(), "yyyyMMddHHmmss"));// 操作时间
		tdscBankApp.setOperator(operator);// 操作人

		tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);

		request.setAttribute("saveMessage", "保存成功");
		request.setAttribute("tdscBankApp", tdscBankApp);

		return mapping.findForward("toTdscBankApp");
	}


	public ActionForward findTdscBankAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBankAppCondition tdscBankAppCondition = new TdscBankAppCondition();

		// 竞买人名称
		//String bidderName = new String((request.getParameter("bidderName") + "").getBytes("ISO-8859-1"), "GBK");
		String bidderName = (String)request.getParameter("bidderName");
		String bidderId = (String)request.getParameter("bidderId");
		String noticeId = (String)request.getParameter("noticeId");

		TdscBankAppForm tdscBankAppForm = (TdscBankAppForm) form;
		bindObject(tdscBankAppCondition, tdscBankAppForm);

		if (bidderName != null && !"null".equals(bidderName) && !"".equals(bidderName)) {
			tdscBankAppCondition.setPayName(bidderName);
		}
		if(StringUtils.isBlank(bidderId)){
			tdscBankAppCondition.setStatus("0");// 0是未关联;1是已关联
		}

		tdscBankAppCondition.setBidderId(bidderId);
		tdscBankAppCondition.setNoticeId(noticeId); 
		tdscBankAppCondition.setOrderKey("payDate");
		tdscBankAppCondition.setOrderType("desc");
		List tdscBankAppList = tdscBankAppService.findTdscBankAppList(tdscBankAppCondition);

		request.setAttribute("tdscBankAppList", tdscBankAppList);
		request.setAttribute("bidderId", bidderId);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("tdscBankAppCondition", tdscBankAppCondition);

		return mapping.findForward("selectTdscBankAppList");
	}

	protected void bindObject(Object object, ActionForm form) {
		if (form != null) {
			try {
				BeanUtils.copyProperties(object, form);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
	}

}
