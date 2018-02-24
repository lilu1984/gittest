package com.wonders.android.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.sql.Timestamp;

import com.wonders.android.service.AppService;
import com.wonders.engine.CoreService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bank.BankService;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.service.SubscribeService;

public class MyTradeAction extends BaseAction{
	private String isTest = PropertiesUtil.getInstance().getProperty("is_test");
	private AppService appService;
	
	private SubscribeService subscribeService;
	
	private CommonQueryService commonQueryService;

	public void setAppService(AppService appService) {
		this.appService = appService;
	}
	
	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		request.getSession().removeAttribute("certNo");
		return mapping.findForward("androidCaLogin");
	}
	
	/**
	 * 跳转选择号牌页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSelectCon(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		String noticeId = request.getParameter("noticeId");
		String userId = (String) request.getSession().getAttribute("certNo");
		
		List myBidderList = this.appService.queryBidderAppListForme(userId, noticeId);
		if (myBidderList != null && myBidderList.size() > 0 ) {
			TdscBidderApp bidderApp = (TdscBidderApp)myBidderList.get(0);
			String haopai = bidderApp.getConNum();
			String certNo = bidderApp.getCertNo();
			appService.saveSelectedCon(bidderId, haopai,certNo);

			return new ActionForward("myTrade.do?method=toMyOrderInfo&appId="+appId+"&bidderId="+bidderId, true);
		}
		List list = this.appService.findTdscBidderAppListByNoticeId(noticeId);
		Set set = new HashSet();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			set.add(app.getConNum());
		}
		request.setAttribute("containsNum", set);
		request.setAttribute("bidderId", bidderId);
		request.setAttribute("appId", appId);
		request.setAttribute("noticeId", noticeId);
		return mapping.findForward("bidder-select-con");
	}

	/**
	 * 保存号牌
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveCon(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		String noticeId = request.getParameter("noticeId");
		String conNum = request.getParameter("conNum");
		TdscBidderView bidderView = appService.getMyTrade(appId, bidderId);
		//检测同一块地是否有相同号牌已被保存,若再数据库已存在相同号牌则在页面做出提示
		if(bidderView!=null&&appService.checkSameHaoPaiByAppId(appId, conNum)){
			return new ActionForward("myTrade.do?method=toSelectCon&msg=conflict&noticeId="+noticeId+"&bidderId="+bidderId +"&appId="+appId, true);
		}
		appService.saveSelectedCon(bidderId, conNum,null);
		CoreService.reloadClientPipe(bidderView.getUserId());
		return new ActionForward("myTrade.do?method=toMyOrderInfo&appId="+appId+"&bidderId="+bidderId, true);
	}
	
	/**
	 * 跳到竞买申请书页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toJmsqs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		TdscTradeView view = appService.getTdscTradeView(appId);
		request.setAttribute("view", view);
		return mapping.findForward("bidder-jmsqs");
	}
	
	/**
	 * 银行列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toBankList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		TdscTradeView view = appService.getTdscTradeView(appId);
		//申请书页面参数传递
		String uploadFileId = request.getParameter("uploadFileId");
		String bidderType = request.getParameter("bidderType");
		String isCreateComp = request.getParameter("isCreateComp");
		request.setAttribute("uploadFileId", uploadFileId);
		request.setAttribute("bidderType", bidderType);
		request.setAttribute("isCreateComp", isCreateComp);		
		request.setAttribute("view", view);
		return mapping.findForward("bidder-bank-list");
	}
	
	/**
	 * 申购
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public ActionForward toBuy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		String certNo = (String) request.getSession().getAttribute("certNo");
		String bankId = request.getParameter("bankId");
		String appId = request.getParameter("appId");
		
		PersonInfo personInfo = subscribeService.getPersonInfo(certNo);
		if(personInfo != null){
			TdscTradeView tdscTradeView = appService.getTdscTradeView(appId);
			TdscBidderApp bidderApp = null;
			TdscBidderPersonApp tdscBidderPersonApp = null;
			if ("1".equals(tdscTradeView.getIsPurposeBlock())) {
				// 如果为意向地块
				bidderApp = appService.getPurposeBidderAppByAppId(appId);
				if (bidderApp != null) {
					tdscBidderPersonApp = appService.getBidderPersonAppByBidderId(bidderApp.getBidderId());
					if (tdscBidderPersonApp != null) {
						if (!StringUtils.trimToEmpty(personInfo.getOrgNo()).equals(tdscBidderPersonApp.getOrgNo())) {
							bidderApp = null;
							tdscBidderPersonApp = null;
						} else {
							bidderApp.setIsPurposePerson("1");
						}
					}
				}
			} 
			if (bidderApp == null){
				bidderApp = new TdscBidderApp();
				bidderApp.setIsPurposePerson("0");
			}
			if (tdscBidderPersonApp == null) {
				tdscBidderPersonApp = new TdscBidderPersonApp();
			}
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			initBidderApp(bidderApp, personInfo);
			initBidderPersonApp(tdscBidderPersonApp, personInfo);
			bidderApp.setUserId(certNo);
			bidderApp.setAppId(tdscTradeView.getAppId());
			bidderApp.setNoticeId(tdscTradeView.getNoticeId());
			bidderApp.setAcceptDate(nowTime);
			bidderApp.setBankId(bankId);
			//竞买方式
			bidderApp.setUploadFileId((String)request.getParameter("uploadFileId"));
			bidderApp.setBidderType((String)request.getParameter("bidderType"));
			bidderApp.setIsCreateComp((String)request.getParameter("isCreateComp"));
			if("1".equals(isTest)||"2".equals(isTest)||"99".equals(bankId)){
				//本地使用
				String number = getBankNumber();
				bidderApp.setBankNumber(number);
			}
			bidderApp.setSgDate(nowTime);
			String sqNumber = subscribeService.createSqNumber();
			bidderApp.setSqNumber(sqNumber);
//			bidderApp = this.takeConNum(bidderApp);
			if(org.apache.struts.util.TokenProcessor.getInstance().isTokenValid(request,true)){
				appService.persistenBidder(bidderApp, tdscBidderPersonApp);
			}else{
				//重复提交让界面提示消息并返回
				request.setAttribute("refresh", "true");
			}
			
			if("0".equals(isTest)&&!"99".equals(bankId)){
				//正式环境使用,保存银行返回的子账号。
				BankService service = new BankService();
				service.sendClientG00003(bankId, bidderApp.getBidderId());	
			}
			TdscBidderApp newBidder = null;
			if(StringUtils.isNotBlank(bidderApp.getBidderId())){
				newBidder = (TdscBidderApp)subscribeService.getTdscBidderAppDao().get(bidderApp.getBidderId());
			}else{
				newBidder = bidderApp;
			}
			request.setAttribute("bidderApp", newBidder);
			request.setAttribute("bankId", bankId);
			request.setAttribute("bidderId", bidderApp.getBidderId());
			request.setAttribute("tdscTradeView", tdscTradeView);
		}
		return mapping.findForward("bidder-receipt");
	}
	
	/**
	 * 手机端要自动生成号牌
	 * @param bidderApp
	 * @return
	 */
	private TdscBidderApp takeConNum(TdscBidderApp bidderApp) {
		List bidderList = appService.queryBiddersWithConNum(bidderApp.getAppId());
		List conList = new ArrayList();
		for(int i = 1; i <= 99; i++){
			conList.add(i);
		}
		if(bidderList != null && bidderList.size() > 0){
			for(int j = 0; j < bidderList.size(); j++){
				TdscBidderApp b = (TdscBidderApp) bidderList.get(j);
				if(StringUtils.isNotBlank(b.getConNum()))
					conList.remove(Integer.parseInt(b.getConNum()));
			}
		}
		bidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
		Random r = new Random();
		bidderApp.setConNum(r.nextInt(conList.size()) + "");
		return bidderApp;
	}

	private void initBidderApp(TdscBidderApp bidderApp, PersonInfo personInfo) {
		bidderApp.setIsAccptSms(personInfo.getIsAccptSms());
		bidderApp.setPushSmsPhone(personInfo.getPushSmsPhone());
//		bidderApp.setBidderType("1"); //竞买类型(1:单独竞买 2:联合竞买)
		bidderApp.setIfDownloadZgzs("1");
		bidderApp.setIfzwt("0");
	}
	
	private void initBidderPersonApp(TdscBidderPersonApp bidderPersonApp, PersonInfo personInfo) {
		bidderPersonApp.setBidderLxdh(personInfo.getBidderLxdh());
		bidderPersonApp.setBidderLxdz(personInfo.getBidderLxdz());
		
		bidderPersonApp.setBidderName(personInfo.getBidderName());
		bidderPersonApp.setBidderProperty(personInfo.getBidderProperty());
		bidderPersonApp.setBidderYzbm(personInfo.getBidderYzbm());
		bidderPersonApp.setBidderZjhm(personInfo.getBidderZjhm());
		
		bidderPersonApp.setBidderZjlx(personInfo.getBidderZjlx());
		bidderPersonApp.setCorpFddbrZjhm(personInfo.getCorpFddbrZjhm());
		bidderPersonApp.setCorpFddbrZjlx(personInfo.getCorpFddbrZjlx());
		bidderPersonApp.setCorpFr(personInfo.getCorpFr());
		bidderPersonApp.setCorpFrZjhm(personInfo.getCorpFrZjhm());
		bidderPersonApp.setCorpFrZjlx(personInfo.getCorpFrZjlx());
		bidderPersonApp.setLinkManName(personInfo.getLinkManName());
		bidderPersonApp.setOrgNo(personInfo.getOrgNo());
		
		bidderPersonApp.setBidderProperty(personInfo.getBidderProperty());
	}
	
	private String getBankNumber() {
		long time = System.currentTimeMillis();
		long random = (long)(Math.random() * 1000000000);
		String number = time + "" + random;
		number = number.substring(0, 16);
		return number;
	}
	
	/**
	 * 我
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String certNo = (String) request.getSession().getAttribute("certNo");
		PersonInfo person = subscribeService.getPersonInfo(certNo);
		request.setAttribute("person", person);
		return mapping.findForward("bidder-info");
	}
	
	/**
	 * 我的申购：通过useId取得tdsc_bidder_view列表，按申购时间倒叙展示
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMyOrders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		//获得userId
		String userId=(String) request.getSession().getAttribute("certNo");
		//根据userId取得申购列表
		List bidderOrdersList=appService.queryBidderOrdersList(userId);
		request.setAttribute("bidderOrdersList", bidderOrdersList);
		response.setContentType("utf-8");
		return mapping.findForward("bidder-orders");
	}
	
	/**
	 * 我的交易
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMyTrades(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String userId = (String) request.getSession().getAttribute("certNo");
		List myTradeList = appService.queryMyTradeList(userId);
		request.setAttribute("list", myTradeList);
		return mapping.findForward("bidder-trades");
	}
	
	/**
	 * 我的订单详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMyOrderInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		TdscTradeView tV = appService.getTdscTradeView(appId);
		TdscBidderView bV = appService.getMyTrade(appId, bidderId);
		request.setAttribute("tradeView", tV);
		request.setAttribute("bidderView", bV);
		return mapping.findForward("bidder-order-info");
	}
	/**
	 * 我的交易详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMyTradeInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		TdscTradeView tV = appService.getTdscTradeView(appId);
		TdscBidderView bV = appService.getMyTrade(appId, bidderId);
		request.setAttribute("tradeView", tV);
		request.setAttribute("bidderView", bV);
		return mapping.findForward("bidder-trade-info");
	}
	/**
	 * 跳转到竞买方式选择页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward chooseBidderType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		return mapping.findForward("bidder-type-info");
		
		
	}
}
