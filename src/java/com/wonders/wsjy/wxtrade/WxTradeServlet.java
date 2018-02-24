package com.wonders.wsjy.wxtrade;

import com.wonders.engine.TradeEngine;
import com.wonders.engine.exception.EngineException;
import com.wonders.wsjy.dijia.EngineProcessManager;


/**
 * 引擎启动的servlet.
 * @author sunxin
 *
 */
public class WxTradeServlet extends javax.servlet.http.HttpServlet {
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
    	try {
	    	//启动核心引擎
    		TradeEngine tradeEngine = new TradeEngine();
	    	//设置参数
    		tradeEngine.setHandlerClass("com.wonders.wsjy.wxtrade.WxHandler");
    		tradeEngine.setDbClass("com.wonders.wsjy.wxtrade.WxDbService");
    		//设置是否需要短消息服务，如果需要，请设置短消息实现类
    		tradeEngine.setSmsService(true);
    		tradeEngine.setSmsClass("com.wonders.wsjy.wxtrade.WxSmsService");
    		//设置公告挂牌触发器
    		tradeEngine.setListingClass("com.wonders.wsjy.wxtrade.WxListing");
	    	//启动服务
    		tradeEngine.startTradeEngine();
			printStartInfo();
		} catch (EngineException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 打印启动信息
     */
    private void printStartInfo(){
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("|  无锡观摩引擎 v 1.0       |");
		System.out.println("|       启动成功，欢迎使用       |");
		System.out.println("+++++++++++++++++++++++++++++++");
    	
    }
    /**
     * 从容器中销毁
     */
    public void destroy() {
    	EngineProcessManager.getInstance().destory();
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

