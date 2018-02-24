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
 * 引擎启动的servlet.
 * @author sunxin
 *
 */
public class UploadifyServlet extends javax.servlet.http.HttpServlet {
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
    	
    }
    
    /**
     * 打印启动信息
     */
    private void printStartInfo(){
    	
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
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    	String uploadFileId = request.getParameter("uploadFileId");
    	String catalog = request.getParameter("catalog");
        //设置文件缓冲区大小
        factory.setSizeThreshold(5* 1024 * 1024);
        //超过缓冲区大小，临时文件放在什么地方
        String temppath = PropertiesUtil.getInstance().getProperty("file.save.path")+"\\temp";
        factory.setRepository(new File(temppath));
        
        //2、使用文件上传工厂，创建一个文件上传的servlet对象；解析表单，保存到list里面
        ServletFileUpload upload = new ServletFileUpload(factory);
       //设置编码格式
       upload.setHeaderEncoding("UTF-8");
       // 得到所有的表单域，它们目前都被当作FileItem

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

