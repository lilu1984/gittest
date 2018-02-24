package com.wonders.android.httpservice.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wonders.android.httpservice.CmdFactory;


public class AndroidServlet extends HttpServlet{
	
	public AndroidServlet(){
		super();
	}
	public void destroy() {
		super.destroy();
	}
	public void init() throws ServletException {
		super.init();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	IOException {
		response.setHeader("Cache-Control","no-cache");    
		response.setHeader("Cache-Control","no-store");    
		response.setHeader("Pragma","no-cache");    
		response.setDateHeader("Expires", 0);   
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String result = "";
		result = CmdFactory.executeMethod(request);
		response.getWriter().println(result);
	}
	
}