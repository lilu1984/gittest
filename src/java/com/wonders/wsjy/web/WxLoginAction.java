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
	 * 登录进入交易外网
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
		//监测观摩登录
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
				request.getSession().setAttribute("bidderName", "未注册用户");
			} else {
				request.getSession().setAttribute("bidderName", info.getBidderName());
			}
//		if (bv != null) {
			// 验证黑名单
//			TdscCreditService creditService = (TdscCreditService)AppContextUtil.getInstance().getAppContext().getBean("tdscCreditService");
//			TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
//			condition.setBidderName(bv.getBidderName());
//			List resultList = creditService.queryInBlockList(condition);
//			if (resultList != null && resultList.size() > 0) {
//				String errMsg = "您已经被列入黑名单，无法登录系统进行土地交易活动！";
//				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
//				request.getSession().removeAttribute("certNo");
//				return mapping.findForward("tdscCaLogin");
//			}
//			
//			request.getSession().setAttribute("bidderName", bv.getBidderName());
			
			
		} else {
			request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, "登录不成功，请联系系统管理员！");
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
	 * 登出系统 清除会话
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
	 * 阅读交易规则
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
		 * 取得竞买人的key的唯一标示，key的类型，竞买人名称，竞买人身份证明(自然人为身份证，企业为组织机构编码)
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
		 * 取得KEY信息后对比数据库若不存在数据则进行插入数据工作，若存在则不处理
		 */
		PersonInfo personInfo = this.subscribeService.getPersonInfo(certNo);
		if(personInfo==null){
			personInfo = new PersonInfo();
			personInfo.setBidderId(certNo);
			//判断keyType是否为法人若为法人则值为12,否则为...
			if(keyType.startsWith("ent")){
				personInfo.setBidderProperty("12");
			}
			
			if(keyType.startsWith("user")){
				personInfo.setBidderProperty("11");
			}
			//假如竞买人名称和竞买人身份认证唯一标识没有则将资格证书编号给他.
			//(由于正式使用后CA信息内肯定含有这些信息，所以此逻辑可在测试时候使用,单也不影响正式环境的运行。)
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
