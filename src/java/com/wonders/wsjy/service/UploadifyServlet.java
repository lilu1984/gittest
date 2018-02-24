package com.wonders.wsjy.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.dijia.EngineProcessManager;


/**
 * ����������servlet.
 * @author sunxin
 *
 */
public class UploadifyServlet extends javax.servlet.http.HttpServlet {
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
    	
    }
    
    /**
     * ��ӡ������Ϣ
     */
    private void printStartInfo(){
    	
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
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    	String uploadFileId = request.getParameter("uploadFileId");
    	String catalog = request.getParameter("catalog");
        //�����ļ���������С
        factory.setSizeThreshold(5* 1024 * 1024);
        //������������С����ʱ�ļ�����ʲô�ط�
        String temppath = PropertiesUtil.getInstance().getProperty("file.save.path")+"\\temp";
        factory.setRepository(new File(temppath));
        
        //2��ʹ���ļ��ϴ�����������һ���ļ��ϴ���servlet���󣻽����������浽list����
        ServletFileUpload upload = new ServletFileUpload(factory);
       //���ñ����ʽ
       upload.setHeaderEncoding("UTF-8");
       // �õ����еı�������Ŀǰ��������FileItem

       WebApplicationContext context = AppContextUtil.getInstance().getAppContext();
       SubscribeService subscribeService = (SubscribeService)context.getBean("subscribeService");
	   String retString = "1";
       try {
		List items = upload.parseRequest(request);
		if(items !=null){
			for(int i=0;i<items.size();i++){
				DiskFileItem diskFileItem = (DiskFileItem) items.get(i); 
				if(StringUtils.isNotBlank(diskFileItem.getName())){
					String fileType = diskFileItem.getName().substring(diskFileItem.getName().lastIndexOf(".")+1);
					FileRef fileRef = subscribeService.uploadFile(uploadFileId,diskFileItem.get(), diskFileItem.getName(), fileType,catalog);
					retString = fileRef.getFileId();
				}
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       response.setContentType("text/html; charset=GBK");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.write(retString);
		pw.close();
    }

}

