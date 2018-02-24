package com.wonders.tdsc.daping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.esframework.util.DateUtil;
import com.wonders.util.PropertiesUtil;

/**
 * Servlet implementation class for Servlet: DapingHttpRest
 * 
 */
public class DapingHttpRest extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static final String DAPING_FILE_PATH = PropertiesUtil.getInstance().getProperty("daping.path");

	public DapingHttpRest() {
		super();
	}

	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		loggerInfo(request, "doDelete");
		super.doDelete(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		loggerInfo(request, "doGet");
		String path = getRequestPath(request);
		String html = doFileListHtml(path);
		if (StringUtils.isNotEmpty(html)) {
			response.setHeader("Cache-Control","no-cache");    
			response.setHeader("Cache-Control","no-store");    
			response.setHeader("Pragma","no-cache");    
			response.setDateHeader("Expires", 0);   
			response.setContentType("text/html; charset=gbk");
			PrintWriter out = response.getWriter();
			out.println(html);
			out.flush();
			out.close();
		}
	}

	private String doFileListHtml(String path) throws IOException {
		String rootPath = DAPING_FILE_PATH + (DAPING_FILE_PATH.endsWith(File.separator)?"daping":File.separator + "daping");
		String dapingPath = rootPath + (StringUtils.isEmpty(path)?"":File.separator + path);
		File file = new File(dapingPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles(new FileFilter(){
				public boolean accept(File pathname) {
					return true;
				}
			});
			StringBuffer htmlBuffer = new StringBuffer();
			htmlBuffer.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\">");
			htmlBuffer.append("<tr>");
			htmlBuffer.append("<td align=\"left\">");
			htmlBuffer.append("<font size=\"+1\"><strong>文件名称</strong></font>");
			htmlBuffer.append("</td>");
			htmlBuffer.append("<td align=\"center\">");
			htmlBuffer.append("<font size=\"+1\"><strong>大小</strong></font>");
			htmlBuffer.append("</td>");
			htmlBuffer.append("<td align=\"right\">");
			htmlBuffer.append("<font size=\"+1\"><strong>最后修改日期</strong></font>");
			htmlBuffer.append("</td>");
			htmlBuffer.append("</tr>");
			for (int i = 0 ; files != null && i < files.length; i++) {
				StringBuffer libuffer = new StringBuffer();
				libuffer.append("<tr>");
				libuffer.append("<td align=\"left\">&nbsp;&nbsp;<a href=\"/daping/" + (StringUtils.isEmpty(path)?path:path + "/") +files[i].getName()+ "\"><tt>").append(files[i].getName() + "</tt></a>");
				libuffer.append("<td align=\"right\"><tt>" +files[i].length()+ " kb</tt></td>");
				libuffer.append("<td align=\"right\"><tt>" + DateUtil.date2String(new Date(files[i].lastModified()), DateUtil.FORMAT_DATETIME)+ "</tt></td>");
				libuffer.append("</tr>");
				htmlBuffer.append(libuffer);
			}
			htmlBuffer.append("</table>");
			return htmlBuffer.toString();
		} else if (file.isFile()){
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			StringBuffer content = new StringBuffer();
			String line = bufferedReader.readLine();
			while(line!= null) {
				content.append(line);
				line = bufferedReader.readLine();
			}
			String result = content.toString();
			return result;
		}
		return null;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		loggerInfo(request, "doPost");
		doGet(request, response);
	}

	private void loggerInfo(HttpServletRequest request, String string) {
		logger.info(string + "is running...");
	}

	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		loggerInfo(request, "doPut");
		super.doPut(request, response);
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String id = null;
		if (url.endsWith("/"))
			url = url.substring(0, url.length() - 1);
		int formIndex = url.indexOf("daping");
		int subBeginIndex = url.indexOf("/",formIndex);
		if (subBeginIndex < 0) {
			subBeginIndex = url.lastIndexOf('/');
		}
		id = url.substring(subBeginIndex + 1);
		return "daping".equals(id)?"":id;
	}
}