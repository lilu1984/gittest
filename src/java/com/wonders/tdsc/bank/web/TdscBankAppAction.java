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
	 * ������Ա��ѯ���б����еı�֤����Ϣ
	 */
	public ActionForward queryTdscBankAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBankAppCondition tdscBankAppCondition = new TdscBankAppCondition();
		String queryType = (String) request.getParameter("queryType");
		String bankId = (String) request.getSession().getAttribute("bankId");

		String regex="[a-zA-Z0-9_\u4E00-\u9FA5]*";   //ƥ�������ֺ�26��Ӣ����ĸ��ɵ��ַ���
		regex="^[A-Za-z0-9]+$";   //ƥ�������ֺ�26��Ӣ����ĸ��ɵ��ַ���

		boolean result2= Pattern.matches(regex, bankId);
		if(!result2){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("certNo�������Ϸ���value="+bankId);
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "��¼���ɹ�������ϵϵͳ����Ա��");
			return mapping.findForward("tdscCaLogin");
		}
		
		if (StringUtils.isNotBlank(queryType)) {
			boolean result1= Pattern.matches(regex, queryType);
			if(!result1){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
				System.out.println("certNo�������Ϸ���value="+queryType);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "��¼���ɹ�������ϵϵͳ����Ա��");
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
		String regex="^[A-Za-z0-9]+$";   //ƥ�������ֺ�26��Ӣ����ĸ��ɵ��ַ���
		
		TdscBankApp tdscBankApp = null;
		if (!StringUtils.isEmpty(tdscBankAppId)) {
			boolean result= Pattern.matches(regex, tdscBankAppId);
			if(!result){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
				System.out.println("certNo�������Ϸ���value="+tdscBankAppId);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "��¼���ɹ�������ϵϵͳ����Ա��");
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
				request.setAttribute("saveMessage", "�þ���֤���Ѿ����������޷��ύ��");
				request.setAttribute("tdscBankApp", tdscBankApp);
				return mapping.findForward("toTdscBankApp");
			}
		}else{
			tdscBankApp = new TdscBankApp();
		}
		bindObject(tdscBankApp, tdscBankAppForm);
		tdscBankApp.setAcceptBank(bankId);// ��������
		tdscBankApp.setAcceptBankName((String) request.getSession().getAttribute("bankName"));// ������������
		tdscBankApp.setOperatingTime(DateUtil.string2Timestamp(DateConvertor.getCurrentDateWithTimeZone(), "yyyyMMddHHmmss"));// ����ʱ��
		tdscBankApp.setOperator(operator);// ������

		tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);

		request.setAttribute("saveMessage", "����ɹ�");
		request.setAttribute("tdscBankApp", tdscBankApp);

		return mapping.findForward("toTdscBankApp");
	}


	public ActionForward findTdscBankAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBankAppCondition tdscBankAppCondition = new TdscBankAppCondition();

		// ����������
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
			tdscBankAppCondition.setStatus("0");// 0��δ����;1���ѹ���
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
