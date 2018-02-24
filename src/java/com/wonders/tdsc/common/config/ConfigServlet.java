package com.wonders.tdsc.common.config;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ContextLoaderPlugIn;

import com.wonders.esframework.dic.service.DicConfigManager;
import com.wonders.esframework.dic.service.DicPropertyManager;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.wsjy.bank.BankServer;
import com.wonders.wsjy.bank.BankServerThread;

/**
 * ���ฺ���ʼ��������̬��Ϣ���ࡣ
 * <p>
 * 
 * ������Ӧ�÷���������ʱ���Զ���ʼ����
 * <p>
 * 
 * ��ʼ��ʱ�����ཫ����ʼ�������ࣺ
 * 
 * <pre>
 *     �ֵ������Ϣ�ࣺcom.wonders.esframework.common.dic.util.DicDataUtil
 *     Ӧ��������Ϣ�ࣺcom.wonders.esframework.common.dic.util.DicPropertyUtil
 *                          
 *     @version 1.0
 *     @since 1.0
 * 
 */

public class ConfigServlet extends javax.servlet.http.HttpServlet {
    /**
     * ����get����
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {
        performTask(request, response);
    }

    /**
     * ����post����
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {
        performTask(request, response);
    }

    /**
     * ȡ��servlet��Ϣ
     * 
     * @return servlet��Ϣ
     */
    public String getServletInfo() {
        return super.getServletInfo();
    }

    /**
     * ��ʼ��servlet
     */
    public void init() {
        // ���servlet��������
        ServletContext servletContext = this.getServletContext();

        /**
         * ���spring��Ӧ��������
         * 
         * ���У�ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX Ϊʹ��pluginģʽ����spring�������ļ�������WebApplicationContext����Ĵ�����������
         * 
         * ע�⣺�˷���ֻ������plugin��ʽ
         */
        WebApplicationContext wac = (WebApplicationContext) servletContext.getAttribute(ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX);

        // ��appContext�����ڵ�̬�����Ա������������
        AppContextUtil.getInstance().setAppContext(wac);
        AppContextUtil.getInstance().setWebDeployedRealPath(servletContext.getRealPath("/"));
        System.out.println("WebDeployedRealPath:" + servletContext.getRealPath("/"));
        AppContextUtil.getInstance().setCastorMapLocation(servletContext.getRealPath(servletContext.getInitParameter("castorMapLocation")));
        System.out.println("CastorMapLocation:" + servletContext.getRealPath(servletContext.getInitParameter("castorMapLocation")));

        // ���س�ʼ���ֵ�����
        DicConfigManager dicConfigManager = (DicConfigManager) wac.getBean("tdscDicConfigManager");
        // DicDataUtil.getInstance().initLoad(dicConfigManager);
        dicConfigManager.loadAllDic();

        // ����Ӧ��������Ϣ
        DicPropertyManager dicPropertyManager = (DicPropertyManager) wac.getBean("tdscDicPropertyManager");
        // DicPropertyUtil.getInstance().initLoad(dicPropertyManager);
        dicPropertyManager.loadAllProperties();
        //�������з���
        try{
        	BankServerThread thread = new BankServerThread();
        	thread.start();
        }catch(Exception ex){
        	System.out.print("���з�����������");
        }
    }

    /**
     * ������������
     */
    public void destroy() {
    }

    /**
     * ִ������
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
    }

}
