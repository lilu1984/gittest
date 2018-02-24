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
 * 该类负责初始化各管理静态信息的类。
 * <p>
 * 
 * 该类在应用服务器启动时，自动初始化。
 * <p>
 * 
 * 初始化时，该类将将初始化以下类：
 * 
 * <pre>
 *     字典管理信息类：com.wonders.esframework.common.dic.util.DicDataUtil
 *     应用配置信息类：com.wonders.esframework.common.dic.util.DicPropertyUtil
 *                          
 *     @version 1.0
 *     @since 1.0
 * 
 */

public class ConfigServlet extends javax.servlet.http.HttpServlet {
    /**
     * 处理get请求
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
     * 处理post请求
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
     * 取得servlet信息
     * 
     * @return servlet信息
     */
    public String getServletInfo() {
        return super.getServletInfo();
    }

    /**
     * 初始化servlet
     */
    public void init() {
        // 获得servlet的上下文
        ServletContext servletContext = this.getServletContext();

        /**
         * 获得spring的应用上下文
         * 
         * 其中：ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX 为使用plugin模式加载spring的配置文件，最终WebApplicationContext对象的存贮环境变量
         * 
         * 注意：此方法只适用于plugin方式
         */
        WebApplicationContext wac = (WebApplicationContext) servletContext.getAttribute(ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX);

        // 将appContext保存在单态类中以便其他程序调用
        AppContextUtil.getInstance().setAppContext(wac);
        AppContextUtil.getInstance().setWebDeployedRealPath(servletContext.getRealPath("/"));
        System.out.println("WebDeployedRealPath:" + servletContext.getRealPath("/"));
        AppContextUtil.getInstance().setCastorMapLocation(servletContext.getRealPath(servletContext.getInitParameter("castorMapLocation")));
        System.out.println("CastorMapLocation:" + servletContext.getRealPath(servletContext.getInitParameter("castorMapLocation")));

        // 加载初始化字典数据
        DicConfigManager dicConfigManager = (DicConfigManager) wac.getBean("tdscDicConfigManager");
        // DicDataUtil.getInstance().initLoad(dicConfigManager);
        dicConfigManager.loadAllDic();

        // 加载应用配置信息
        DicPropertyManager dicPropertyManager = (DicPropertyManager) wac.getBean("tdscDicPropertyManager");
        // DicPropertyUtil.getInstance().initLoad(dicPropertyManager);
        dicPropertyManager.loadAllProperties();
        //启动银行服务
        try{
        	BankServerThread thread = new BankServerThread();
        	thread.start();
        }catch(Exception ex){
        	System.out.print("银行服务启动出错");
        }
    }

    /**
     * 从容器中销毁
     */
    public void destroy() {
    }

    /**
     * 执行任务
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
    }

}
