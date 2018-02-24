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
		// ֤����Ϣ
		String certInfo = request.getParameter("cert").replace(" ", "+");
		if("0".equals(isTest)){
		// ǩ����Ϣ
		String signTure = request.getParameter("signTure").replace(" ", "+");
		// ��ȡ�����
		String ca_random = (String) session.getAttribute("ca_random");
		// ��ԭ�Ľ���Base64����
		String encodeString = "";
		try {
			byte[] encodeBase64 = Base64.encodeBase64(ca_random
					.getBytes("UTF-8"));
			encodeString = new String(encodeBase64);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ����CA������ǩ
		SVS svs = null;
		svs = new SVS("58.213.51.60", 9188);
		String rou = svs.getVerifiPkcs1(certInfo, signTure, encodeString);
		// ��ǩ�ɹ�
		if (rou.endsWith("200")) {
			try {
				//��ǩ�ɹ�������֤����Ϣ
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
	 * ����ģ���Ħ�����б�ҳ
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
	 * ����ؿ���Ϣ����(����δ��½��δȡ�ñ��ؿ�ľ����ʸ�)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toBlockInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// �Ƿ���뽻�׽���
		boolean ifTrade = false;
		String appId = request.getParameter("appId");
		Date nowDate = new Date();
		// �ؿ���Ϣ
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		if (nowDate.after(tradeView.getListStartDate())
				&& "00".equals(tradeView.getTranResult())) {
			// ��ǰʱ���ڹ��ƿ�ʼʱ��֮���Ҵ��ڽ���δ����״̬
			// �ж��Ƿ�ȡ���˵�ǰ�ؿ�ľ����ʸ�
			TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(
					request, appId, true);
			if (bidderApp != null) {
				// ���ʸ��������Խ��뽻�׽���
				ifTrade = true;
			}
		}
		if (ifTrade) {
			// ���ѵ�½��ȡ�ô˵ؿ�ľ����ʸ�����ת�����۽���
			return new ActionForward("/android/trade.do?method=toTrade&appId="
					+ appId, true);
		} else {
			// ����δ��½��δȡ�ñ��ؿ�ľ����ʸ�������Ħ����
			return new ActionForward("monitor.do?method=toMonitor&appId="
					+ appId, true);
		}
	}

	/**
	 * �����Ħ����
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
		// �ؿ���Ϣ
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		// ������Ϣ
		List listingAppList = appService.findListingApp(appId);
		// ��֤��ǰ�û��Ƿ����깺��
		TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(
				request, appId, false);
		String canAccApp = "1";// ��ǰ�Ƿ��������1���ԡ�0������
		if (bidderApp != null || new Date().after(tradeView.getAccAppEndDate())) {
			canAccApp = "0";
		}
		// ��ǰ��������
		// ��ǰ��ע����
		int bidderNum = this.appService.getBlockBidderNum(appId);
		int lookingNum = CoreService.getGmClientPool().size()+1;
		// ��ȡ��ǰ�ĵؿ�����״̬
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
	 * ����ģ���Ħ�ؿ��б�ҳ
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
