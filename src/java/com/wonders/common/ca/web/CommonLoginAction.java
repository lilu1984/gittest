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
	//是否为测试环境，若不为1则为测试环境
	private String isTest = PropertiesUtil.getInstance().getProperty("is_test");
	
	public ActionForward logon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardString = "";
		try {
			String toType = request.getParameter("toType");
			/**
			 * 由于此action是新老CA共同使用，所以建立了一个newCa标示用以区分调用此方法的是否为新的CA用户，新的CA用户暂时只开放竞买人CA功能
			 * 新CA登录页面需要传入两个值1、newCa=1   2、caId=唯一标识
			 */
			String newCa = request.getParameter("newCa");

			//判断新CA开始
			if("1".equals(newCa)){
				
				System.out.print("开始/r/n");
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
			// 登录类型 1. 银行 / 2. 竞买人 / 3. 业务人员
			String caType = caStrings[1].substring(0, 4);

			String caName = caStrings[0];

			// CA 证书序列号
			String caNum = caStrings[1].substring(4);
			// 银行名称、竞买单位名称 或者 业务人员姓名


			if ("QTYH".equals(caType)) {
				int caNumLength = caNum.length();
				if(caNumLength == 11){
					String str = caNum.substring(0, 10);
					//前10位为0000000000或者1000000000，同时第11位为1到9的数字
					if(("0000000000".equals(str)||"1000000000".equals(str)) && ("123456789".indexOf(caNum.substring(10))>=0)){
						request.getSession().setAttribute("caNum", caNum.substring(10));
						forwardString = "bank/bankLogin.do?method=bankLogon";
						ActionForward af = new ActionForward(forwardString, true);
						return af;
					}					
				}else if(caNumLength == 12){
					String str = caNum.substring(0, 10);
					//前10位为0000000000或者1000000000，同时第11到12位为10到99的数字
					if("0000000000".equals(str)||"1000000000".equals(str)){
						if(("123456789".indexOf(caNum.substring(10,11))>=0) && ("0123456789".indexOf(caNum.substring(11))>=0)){
							request.getSession().setAttribute("caNum", caNum.substring(10));
							forwardString = "bank/bankLogin.do?method=bankLogon";
							ActionForward af = new ActionForward(forwardString, true);
							return af;
						}
					}
				}
				String errMsg = "编号为" + caStrings[1] + "，无法通过系统验证！";
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
			// 获得登陆用户cert
			certPub = sed.getCertInfo(clientCert, 8);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String ranStr = (String) request.getSession().getAttribute("Random");

		// 验证客户端证书
		try {

			int retValue = sed.validateCert(clientCert);

			if (retValue == 1) {

				request.getSession().setAttribute("ContainerName", ContainerName);

				String uniqueIdStr = "";
				try {
					uniqueIdStr = sed.getCertInfo(clientCert, 17);
				} catch (Exception e) {
					System.out.println("<p><h3>客户端证书验证失败:" + e.getMessage() + "</h3><p>");
				}
				request.getSession().setAttribute("UniqueID", uniqueIdStr);

				String uniqueId = "";
				try {
					// 获得登陆用户ID
					uniqueId = sed.getCertInfoByOid(clientCert, "2.16.840.1.113732.2");
				} catch (Exception e) {
					System.out.println("<p><h3>客户端证书验证失败:" + e.getMessage() + "</h3><p>");
				}

				System.out.println("<h3>欢迎您使用本系统!</h3>");
				System.out.println("<h3>主题通用名：");
				System.out.println(uniqueIdStr);
				System.out.println("<br/>证书颁发者(颁发者通用名): ");
				System.out.println(certPub);
				System.out.println("<br/>证书唯一标识(备用主题通用名)：");
				System.out.println(uniqueId);
				System.out.println("<font color='red'>(实际集成时,会将唯一标识与数据库比对,判断是否为合法用户)</font>");
				System.out.println("</h3><br/>");
				return new String[] { uniqueIdStr, uniqueId };
			} else {
				System.out.println("<h3>客户端证书验证失败！</h3><br/>");
				System.out.println("<h3><font color='red'>");

				if (retValue == -1) {
					System.out.println("登录证书的根不被信任");
				} else if (retValue == -2) {
					System.out.println("登录证书超过有效期");
				} else if (retValue == -3) {
					System.out.println("登录证书为作废证书");
				} else if (retValue == -4) {
					System.out.println("登录证书被临时冻结");
				}
				System.out.println("</font></h3>");
			}
		} catch (Exception ex) {
			System.out.println("<p><h3>客户端证书验证失败:" + ex.getMessage() + "</h3><p>");
		}
		// 验证客户端签名
		try {
			if (sed.verifySignedData(clientCert, ranStr, UserSignedData)) {

			} else {
				System.out.println("<h3>验证客户端签名错误！</h3>");
			}
		} catch (Exception e) {
			System.out.println("<p><h3>验证客户端签名错误:" + e.getMessage() + "</h3><p>");
		}

		return null;
	}
	
	
	private boolean checkCa(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("验证签名开始");
		//获取签名信息
		String signTure=request.getParameter("signTure");
		//获取随机数
		HttpSession session = request.getSession();
		String ca_random=(String) session.getAttribute("ca_random");
		//SVSConnection svs=new SVSConnection(svsIp,Integer.valueOf(svsPort));
		//String rou=svs.verifyPKCS7(signTure);
		SVS svs= null;
		if("2".equals(isTest)){
			//本地
		    svs=new SVS("58.215.18.135",9188);
		}else{
			//真实
			svs=new SVS("32.63.252.148",9188);
		}
		String rou=svs.getVerifiPkcs7(signTure);
		logger.debug("通过服务器验证");
		if(rou.endsWith("200"))
		{
			//获取签名中随机数
			Pkcs7Coder p7=new Pkcs7Coder(signTure);
			String random=p7.getData();
			//与session中比对
			if(ca_random.endsWith(random))
			{
				//到这里CA验证结束
				X509Certificate x509=p7.getCert();
				//获取证书中的信息用于权限判断
				JCertInfo ji=new JCertInfo(x509);
				//获取证书主题
				String user=ji.getSubject();
				//获取序列号
				String serial=ji.getSerial();
				/**
				 * 
				 * 解析CA的key的字符串,并将CA信息写入session
				 */
				//ca类型 企业还是个人
				String keyType = "";
				//竞买人名称
				String jmrName = "";
				//个人为身份证，企业为组织机构编码
				String jmrCode = "";
				//KEY 的编码表示是否是工作人员key
				String keyCode = "";
				//工作人员登陆名称
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
								if("O".equals(key)){//O表示企业的
									jmrName = value;
								}
								if("GIVENNAME".equals(key)){//GIVENNAME表示个人的
									jmrName = value;
								}
								//备用字段
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
			//给予用户错误的提示页面
			//request.setAttribute("rou", rou);
			
		}
		
		return false;
	}
}
