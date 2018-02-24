package com.wonders.wsjy.wxtrade;

import com.wonders.engine.TradeEngine;
import com.wonders.engine.exception.EngineException;
import com.wonders.wsjy.dijia.EngineProcessManager;


/**
 * ����������servlet.
 * @author sunxin
 *
 */
public class WxTradeServlet extends javax.servlet.http.HttpServlet {
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
    	try {
	    	//������������
    		TradeEngine tradeEngine = new TradeEngine();
	    	//���ò���
    		tradeEngine.setHandlerClass("com.wonders.wsjy.wxtrade.WxHandler");
    		tradeEngine.setDbClass("com.wonders.wsjy.wxtrade.WxDbService");
    		//�����Ƿ���Ҫ����Ϣ���������Ҫ�������ö���Ϣʵ����
    		tradeEngine.setSmsService(true);
    		tradeEngine.setSmsClass("com.wonders.wsjy.wxtrade.WxSmsService");
    		//���ù�����ƴ�����
    		tradeEngine.setListingClass("com.wonders.wsjy.wxtrade.WxListing");
	    	//��������
    		tradeEngine.startTradeEngine();
			printStartInfo();
		} catch (EngineException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * ��ӡ������Ϣ
     */
    private void printStartInfo(){
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("|  ������Ħ���� v 1.0       |");
		System.out.println("|       �����ɹ�����ӭʹ��       |");
		System.out.println("+++++++++++++++++++++++++++++++");
    	
    }
    /**
     * ������������
     */
    public void destroy() {
    	EngineProcessManager.getInstance().destory();
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

