package com.wonders.common.ca.web;

import java.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.wonders.esframework.common.util.ext.BeanUtils;
import com.wonders.org.liuy.certdn.JCertInfo;
import com.wonders.org.liuy.certdn.Pkcs7Coder;
import com.wonders.org.liuy.credlink.SVS;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.util.PropertiesUtil;

import cn.org.bjca.security.SecurityEngineDeal;

public class CommonLoginAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(CommonLoginAction.class);
	//�Ƿ�Ϊ���Ի���������Ϊ1��Ϊ���Ի���
	private String isTest = PropertiesUtil.getInstance().getProperty("is_test");
	
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardString = "";
		try {
			String toType = request.getParameter("toType");
			/**
			 * ���ڴ�action������CA��ͬʹ�ã����Խ�����һ��newCa��ʾ�������ֵ��ô˷������Ƿ�Ϊ�µ�CA�û����µ�CA�û���ʱֻ���ž�����CA����
			 * ��CA��¼ҳ����Ҫ��������ֵ1��newCa=1   2��caId=Ψһ��ʶ
			 */
			String newCa = request.getParameter("newCa");

			//�ж���CA��ʼ
			if("1".equals(newCa)){
				
				System.out.print("��ʼ/r/n");
				this.checkCa(request,response);

				//forwardString = "../wsjy/wxlogin.do?method=login";
				if (StringUtils.isNotEmpty(toType)) {
					forwardString = "../wsjy/file.do?method=list";
				}
				String keyCode = (String)request.getSession().getAttribute("keyCode");
				System.out.print(keyCode);
				if("564".equals(keyCode)){
					if(request.getSession().getAttribute("logonName")!=null){
						request.getSession().setAttribute("newCa", "1");
						forwardString = "logging.do?method=logon";
					}
					
				}else if("600".equals(keyCode)){
					String certNo = (String)request.getSession().getAttribute("certNo");
					request.getSession().setAttribute("certNo", "GM"+certNo);
					forwardString = "../wsjy/monitor.do?method=redirectMain";
				}else{
					forwardString = "../wsjy/wxlogin.do?method=readerRule";
				}
				return new ActionForward(forwardString, true);
		
			}
			
			String[] caStrings = caLogon(request);
			// ��¼���� 1. ���� / 2. ������ / 3. ҵ����Ա
			String caType = caStrings[1].substring(0, 4);

			String caName = caStrings[0];

			// CA ֤�����к�
			String caNum = caStrings[1].substring(4);
			// �������ơ�����λ���� ���� ҵ����Ա����


			if ("QTYH".equals(caType)) {
				int caNumLength = caNum.length();
				if(caNumLength == 11){
					String str = caNum.substring(0, 10);
					//ǰ10λΪ0000000000����1000000000��ͬʱ��11λΪ1��9������
					if(("0000000000".equals(str)||"1000000000".equals(str)) && ("123456789".indexOf(caNum.substring(10))>=0)){
						request.getSession().setAttribute("caNum", caNum.substring(10));
						forwardString = "bank/bankLogin.do?method=bankLogon";
						ActionForward af = new ActionForward(forwardString, true);
						return af;
					}					
				}else if(caNumLength == 12){
					String str = caNum.substring(0, 10);
					//ǰ10λΪ0000000000����1000000000��ͬʱ��11��12λΪ10��99������
					if("0000000000".equals(str)||"1000000000".equals(str)){
						if(("123456789".indexOf(caNum.substring(10,11))>=0) && ("0123456789".indexOf(caNum.substring(11))>=0)){
							request.getSession().setAttribute("caNum", caNum.substring(10));
							forwardString = "bank/bankLogin.do?method=bankLogon";
							ActionForward af = new ActionForward(forwardString, true);
							return af;
						}
					}
				}
				String errMsg = "���Ϊ" + caStrings[1] + "���޷�ͨ��ϵͳ��֤��";
				logger.info(errMsg);
				request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, errMsg);
				return mapping.findForward(GlobalConstants.FORWARD_PAGE_LOGIN);			
			}
			if ("QTJM".equals(caType)) {
				request.getSession().setAttribute("certNo", caNum);
				//forwardString = "../wsjy/wxlogin.do?method=login";
				if (StringUtils.isNotEmpty(toType)) {
					forwardString = "../wsjy/file.do?method=list";
				}
				forwardString = "../wsjy/wxlogin.do?method=readerRule";
			}
			if ("QTYW".equals(caType)) {
				request.getSession().setAttribute("logonName", caNum);
				request.getSession().setAttribute("password", "123456");
				forwardString = "logging.do?method=logon";
			}
			if ("QTGM".equals(caType)) {
				request.getSession().setAttribute("certNo", "GM"+caNum);
				forwardString = "../wsjy/monitor.do?method=redirectMain";
			}

		} catch (Exception e) {
			logger.error(e);
		}

		ActionForward af = new ActionForward(forwardString, true);
		return af;

	}

	private String[] caLogon(HttpServletRequest request) {

		SecurityEngineDeal sed = null;
		String clientCert = request.getParameter("UserCert");
		String UserSignedData = request.getParameter("UserSignedData");
		String ContainerName = request.getParameter("ContainerName");
		String certPub = null;
		try {
			sed = SecurityEngineDeal.getInstance("SecXV3Default");
			// ��õ�½�û�cert
			certPub = sed.getCertInfo(clientCert, 8);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String ranStr = (String) request.getSession().getAttribute("Random");

		// ��֤�ͻ���֤��
		try {

			int retValue = sed.validateCert(clientCert);

			if (retValue == 1) {

				request.getSession().setAttribute("ContainerName", ContainerName);

				String uniqueIdStr = "";
				try {
					uniqueIdStr = sed.getCertInfo(clientCert, 17);
				} catch (Exception e) {
					System.out.println("<p><h3>�ͻ���֤����֤ʧ��:" + e.getMessage() + "</h3><p>");
				}
				request.getSession().setAttribute("UniqueID", uniqueIdStr);

				String uniqueId = "";
				try {
					// ��õ�½�û�ID
					uniqueId = sed.getCertInfoByOid(clientCert, "2.16.840.1.113732.2");
				} catch (Exception e) {
					System.out.println("<p><h3>�ͻ���֤����֤ʧ��:" + e.getMessage() + "</h3><p>");
				}

				System.out.println("<h3>��ӭ��ʹ�ñ�ϵͳ!</h3>");
				System.out.println("<h3>����ͨ������");
				System.out.println(uniqueIdStr);
				System.out.println("<br/>֤��䷢��(�䷢��ͨ����): ");
				System.out.println(certPub);
				System.out.println("<br/>֤��Ψһ��ʶ(��������ͨ����)��");
				System.out.println(uniqueId);
				System.out.println("<font color='red'>(ʵ�ʼ���ʱ,�ὫΨһ��ʶ�����ݿ�ȶ�,�ж��Ƿ�Ϊ�Ϸ��û�)</font>");
				System.out.println("</h3><br/>");
				return new String[] { uniqueIdStr, uniqueId };
			} else {
				System.out.println("<h3>�ͻ���֤����֤ʧ�ܣ�</h3><br/>");
				System.out.println("<h3><font color='red'>");

				if (retValue == -1) {
					System.out.println("��¼֤��ĸ���������");
				} else if (retValue == -2) {
					System.out.println("��¼֤�鳬����Ч��");
				} else if (retValue == -3) {
					System.out.println("��¼֤��Ϊ����֤��");
				} else if (retValue == -4) {
					System.out.println("��¼֤�鱻��ʱ����");
				}
				System.out.println("</font></h3>");
			}
		} catch (Exception ex) {
			System.out.println("<p><h3>�ͻ���֤����֤ʧ��:" + ex.getMessage() + "</h3><p>");
		}
		// ��֤�ͻ���ǩ��
		try {
			if (sed.verifySignedData(clientCert, ranStr, UserSignedData)) {

			} else {
				System.out.println("<h3>��֤�ͻ���ǩ������</h3>");
			}
		} catch (Exception e) {
			System.out.println("<p><h3>��֤�ͻ���ǩ������:" + e.getMessage() + "</h3><p>");
		}

		return null;
	}
	
	
	private boolean checkCa(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("��֤ǩ����ʼ");
		//��ȡǩ����Ϣ
		String signTure=request.getParameter("signTure");
		//��ȡ�����
		HttpSession session = request.getSession();
		String ca_random=(String) session.getAttribute("ca_random");
		//SVSConnection svs=new SVSConnection(svsIp,Integer.valueOf(svsPort));
		//String rou=svs.verifyPKCS7(signTure);
		SVS svs= null;
		if("2".equals(isTest)){
			//����
		    svs=new SVS("58.215.18.135",9188);
		}else{
			//��ʵ
			svs=new SVS("32.63.252.148",9188);
		}
		String rou=svs.getVerifiPkcs7(signTure);
		logger.debug("ͨ����������֤");
		if(rou.endsWith("200"))
		{
			//��ȡǩ���������
			Pkcs7Coder p7=new Pkcs7Coder(signTure);
			String random=p7.getData();
			//��session�бȶ�
			if(ca_random.endsWith(random))
			{
				//������CA��֤����
				X509Certificate x509=p7.getCert();
				//��ȡ֤���е���Ϣ����Ȩ���ж�
				JCertInfo ji=new JCertInfo(x509);
				//��ȡ֤������
				String user=ji.getSubject();
				//��ȡ���к�
				String serial=ji.getSerial();
				/**
				 * 
				 * ����CA��key���ַ���,����CA��Ϣд��session
				 */
				//ca���� ��ҵ���Ǹ���
				String keyType = "";
				//����������
				String jmrName = "";
				//����Ϊ���֤����ҵΪ��֯��������
				String jmrCode = "";
				//KEY �ı����ʾ�Ƿ��ǹ�����Աkey
				String keyCode = "";
				//������Ա��½����
				String logonName = "";
				if(StringUtils.isNotBlank(user)){
					String[] caArr = user.split(",");
					for(int i=0;i<caArr.length;i++){
						String item = caArr[i];
						if(StringUtils.isNotBlank(item)){
							String[] itemArr = item.split("=");
							if(itemArr.length==2){
								String key = itemArr[0];
								key = key.trim();
								String value = itemArr[1];
								if("OID.2.5.4.45".equals(key)){
									keyType = value;
								}
								if("OU".equals(key)){
									keyCode = value;
								}
								if("OID.2.5.4.1".equals(key)){
									jmrCode = value;
								}
								if("O".equals(key)){//O��ʾ��ҵ��
									jmrName = value;
								}
								if("GIVENNAME".equals(key)){//GIVENNAME��ʾ���˵�
									jmrName = value;
								}
								//�����ֶ�
								if("OID.2.5.4.1111".equals(key)){
									logonName = value;
								}
							}
						}
					}
					request.getSession().setAttribute("certNo", serial);
					request.getSession().setAttribute("keyType", keyType);
					request.getSession().setAttribute("jmrName", jmrName);
					request.getSession().setAttribute("jmrCode", jmrCode);
					request.getSession().setAttribute("keyCode", keyCode);
					request.getSession().setAttribute("logonName", logonName);
					System.out.print(keyType);
					return true;
				}

			}
		}
		else
		{
			//�����û��������ʾҳ��
			//request.setAttribute("rou", rou);
			
		}
		
		return false;
	}
}
