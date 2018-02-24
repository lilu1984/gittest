package com.wonders.android.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


import sun.misc.BASE64Decoder;

import com.ibm.pkcs11.f;
import com.wonders.android.AppConsts;
import com.wonders.android.service.AppService;
import com.wonders.engine.CoreService;
import com.wonders.org.liuy.certdn.Pkcs7Coder;
import com.wonders.org.liuy.credlink.SVS;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;

public class MonitorAction extends DispatchAction {

	private String isTest = PropertiesUtil.getInstance().getProperty("is_test");
	
	private AppService appService;

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public ActionForward toLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		// 证书信息
		String certInfo = request.getParameter("cert").replace(" ", "+");
		if("0".equals(isTest)){
		// 签名信息
		String signTure = request.getParameter("signTure").replace(" ", "+");
		// 获取随机数
		String ca_random = (String) session.getAttribute("ca_random");
		// 对原文进行Base64编码
		String encodeString = "";
		try {
			byte[] encodeBase64 = Base64.encodeBase64(ca_random
					.getBytes("UTF-8"));
			encodeString = new String(encodeBase64);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 进行CA网关验签
		SVS svs = null;
		svs = new SVS("58.213.51.60", 9188);
		String rou = svs.getVerifiPkcs1(certInfo, signTure, encodeString);
		// 验签成功
		if (rou.endsWith("200")) {
			try {
				//验签成功，解析证书信息
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(certInfo);
				// System.out.println(new String(b).toString());
				InputStream inStream = new ByteArrayInputStream(b);
				CertificateFactory cf;
				cf = CertificateFactory.getInstance("X.509");
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(inStream);
				inStream.close();

				request.getSession().setAttribute("certNo",
						cert.getSerialNumber().toString());
				String subInfo = cert.getSubjectDN().getName();
				String[] subsInfo = subInfo.split(",");
				for (int i = 0; i < subsInfo.length; i++) {
					String[] eachInfo = subsInfo[i].split("=");
					if (eachInfo[0].equals("OU")) {
						request.getSession().setAttribute("jmrCode",
								eachInfo[1]);
					} else if (eachInfo[0].equals("CN")) {
						request.getSession().setAttribute("jmrName",
								eachInfo[1]);
					}
				}
				request.getSession().setAttribute("keyType", cert.getType());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				java.util.Date date = cert.getNotAfter();
				String str = sdf.format(date);
				request.getSession().setAttribute("date", str);
				appService.saveWxPersonInfo(request);
			} catch (CertificateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ActionForward("/android/myTrade.do?method=toMe", true);

		}
		}else{
			session.setAttribute("certNo", certInfo);
		}
		return new ActionForward("/android/myTrade.do?method=toMe", true);

	}

	/**
	 * 交易模块观摩公告列表页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toNoticeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List planList = appService.queryMonitorPlanList();
		// if(planList != null && planList.size() > 0){
		// for(int i = 0; i < planList.size(); i++){
		// TdscTradeView v = (TdscTradeView) planList.get(i);
		// v.setTradeStatus(appService.getCurrBlockTranStatus(v));
		// if(AppConsts.TRAN_STATUS_WAITING.equals(v.getTradeStatus()))
		// v.setTradeStatus(AppConsts.TRAN_STATUS_LISTING);
		// }
		// }
		request.setAttribute("planList", planList);
		return mapping.findForward("notice-list");
	}

	/**
	 * 进入地块信息界面(假如未登陆或未取得本地块的竞买资格)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toBlockInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 是否进入交易界面
		boolean ifTrade = false;
		String appId = request.getParameter("appId");
		Date nowDate = new Date();
		// 地块信息
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		if (nowDate.after(tradeView.getListStartDate())
				&& "00".equals(tradeView.getTranResult())) {
			// 当前时间在挂牌开始时间之后并且处于交易未结束状态
			// 判断是否取得了当前地块的竞买资格
			TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(
					request, appId, true);
			if (bidderApp != null) {
				// 有资格的情况可以进入交易界面
				ifTrade = true;
			}
		}
		if (ifTrade) {
			// 若已登陆并取得此地块的竞买资格则跳转到竞价界面
			return new ActionForward("/android/trade.do?method=toTrade&appId="
					+ appId, true);
		} else {
			// 假如未登陆或未取得本地块的竞买资格则进入观摩界面
			return new ActionForward("monitor.do?method=toMonitor&appId="
					+ appId, true);
		}
	}

	/**
	 * 进入观摩界面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMonitor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		// 地块信息
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		// 报价信息
		List listingAppList = appService.findListingApp(appId);
		// 验证当前用户是否已申购过
		TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(
				request, appId, false);
		String canAccApp = "1";// 当前是否可以受理1可以、0不可以
		if (bidderApp != null || new Date().after(tradeView.getAccAppEndDate())) {
			canAccApp = "0";
		}
		// 当前报名人数
		// 当前关注人数
		int bidderNum = this.appService.getBlockBidderNum(appId);
		int lookingNum = CoreService.getGmClientPool().size()+1;
		// 获取当前的地块所处状态
		String tranStatus = this.appService.getCurrBlockTranStatus(tradeView);
		request.setAttribute("tradeView", tradeView);
		request.setAttribute("listingAppList", listingAppList);
		request.setAttribute("canAccApp", canAccApp);
		request.setAttribute("bidderNum", bidderNum);
		request.setAttribute("lookingNum", lookingNum);
		request.setAttribute("tranStatus", tranStatus);
		return mapping.findForward("block-info");
	}

	/**
	 * 交易模块观摩地块列表页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toBlockList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String planId = request.getParameter("planId");
		List blockList = appService.queryMonitorBlockList(planId);
		if (blockList != null && blockList.size() > 0) {
			for (int i = 0; i < blockList.size(); i++) {
				TdscTradeView v = (TdscTradeView) blockList.get(i);
				v.setTradeStatus(appService.getCurrBlockTranStatus(v));
			}
		}
		request.setAttribute("blockList", blockList);
		return mapping.findForward("block-list");
	}
}
