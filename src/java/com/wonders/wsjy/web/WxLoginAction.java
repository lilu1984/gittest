package com.wonders.wsjy.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;

public class WxLoginAction extends DispatchAction {

	private TradeServer tradeServer;
	
	private SubscribeService subscribeService;
	
	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	/**
	 * ��¼���뽻������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
		
		String certNo = (String) request.getSession().getAttribute("certNo");
		
		if(StringUtils.isBlank(certNo)){
			certNo = request.getParameter("certNo");
		}
		
		
		//request.getSession().setAttribute("certNo", certNo);
		//����Ħ��¼
		//if ("GM001".equals(certNo)){
			//return new ActionForward("monitor.do?method=redirectMain", true);
		//}

		//TdscBidderView bv = tradeServer.getBidderViewByCertNo(certNo);
		
		//String certNo = (String) request.getSession().getAttribute("certNo");
		if (StringUtils.isNotEmpty(certNo)) {
			request.getSession().setAttribute("certNo", certNo);
			if (certNo.length()>5&&"GM001".equals(certNo.substring(0,5))){
				return new ActionForward("monitor.do?method=redirectMain", true);
			}
			PersonInfo info = subscribeService.getPersonInfo(certNo);
			if (info == null) {
				request.getSession().setAttribute("bidderName", "δע���û�");
			} else {
				request.getSession().setAttribute("bidderName", info.getBidderName());
			}
//		if (bv != null) {
			// ��֤������
//			TdscCreditService creditService = (TdscCreditService)AppContextUtil.getInstance().getAppContext().getBean("tdscCreditService");
//			TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
//			condition.setBidderName(bv.getBidderName());
//			List resultList = creditService.queryInBlockList(condition);
//			if (resultList != null && resultList.size() > 0) {
//				String errMsg = "���Ѿ���������������޷���¼ϵͳ�������ؽ��׻��";
//				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
//				request.getSession().removeAttribute("certNo");
//				return mapping.findForward("tdscCaLogin");
//			}
//			
//			request.getSession().setAttribute("bidderName", bv.getBidderName());
			
			
		} else {
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "��¼���ɹ�������ϵϵͳ����Ա��");
			request.getSession().removeAttribute("certNo");
			return mapping.findForward("tdscCaLogin");
		}
		}catch (Exception ex) {
			request.getSession().removeAttribute("certNo");
			return mapping.findForward("tdscCaLogin");
		}
		
		return new ActionForward("trade.do?method=redirectIndex", true);
	}

	/**
	 * �ǳ�ϵͳ ����Ự
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("certNo");
		return mapping.findForward("tdscCaLogin");
	}
	
	/**
	 * �Ķ����׹���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward readerRule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String certNo = (String)request.getParameter("certNo");
		if(StringUtils.isNotBlank(certNo)){
			request.getSession().setAttribute("certNo", certNo);
			
		}
		String toType = request.getParameter("toType");
		/**
		 * 
		 * ȡ�þ����˵�key��Ψһ��ʾ��key�����ͣ����������ƣ����������֤��(��Ȼ��Ϊ���֤����ҵΪ��֯��������)
		 */
		String keyType = "";
		String jmrName = "";
		String jmrCode = "";
		if(request.getSession().getAttribute("certNo")!=null){
			certNo = (String)request.getSession().getAttribute("certNo");
		}
		if(request.getSession().getAttribute("keyType")!=null){
			keyType = (String)request.getSession().getAttribute("keyType");
		}
		if(request.getSession().getAttribute("jmrName")!=null){
			jmrName = (String)request.getSession().getAttribute("jmrName");
		}
		if(request.getSession().getAttribute("jmrCode")!=null){
			jmrCode = (String)request.getSession().getAttribute("jmrCode");
		}
		
		/**
		 * ȡ��KEY��Ϣ��Ա����ݿ�����������������в������ݹ������������򲻴���
		 */
		PersonInfo personInfo = this.subscribeService.getPersonInfo(certNo);
		if(personInfo==null){
			personInfo = new PersonInfo();
			personInfo.setBidderId(certNo);
			//�ж�keyType�Ƿ�Ϊ������Ϊ������ֵΪ12,����Ϊ...
			if(keyType.startsWith("ent")){
				personInfo.setBidderProperty("12");
			}
			
			if(keyType.startsWith("user")){
				personInfo.setBidderProperty("11");
			}
			//���羺�������ƺ;����������֤Ψһ��ʶû�����ʸ�֤���Ÿ���.
			//(������ʽʹ�ú�CA��Ϣ�ڿ϶�������Щ��Ϣ�����Դ��߼����ڲ���ʱ��ʹ��,��Ҳ��Ӱ����ʽ���������С�)
			if(StringUtils.isEmpty(jmrName)){
				jmrName = certNo;
			}
			if(StringUtils.isEmpty(jmrCode)){
				jmrCode = certNo;
			}
			personInfo.setBidderName(jmrName);
			personInfo.setOrgNo(jmrCode);
			personInfo.setCreateDate(new Date());
			personInfo.setLastUpdateDate(new Date());
			this.subscribeService.getPersonInfoDAO().save(personInfo);
		}
		
		
		if (StringUtils.isNotEmpty(toType)) {
			return new ActionForward("file.do?method=list", true);
		}
		return mapping.findForward("readerRule");
	}
}
