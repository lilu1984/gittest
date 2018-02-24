package com.wonders.tdsc.jzdj.web;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.jzdj.service.JzdjService;

public class JzdjAction  extends DispatchAction{
	private JzdjService jzdjService;
	public JzdjService getJzdjService() {
		return jzdjService;
	}

	public void setJzdjService(JzdjService jzdjService) {
		this.jzdjService = jzdjService;
	}
	/**
	 * ��׼�ؼ�ͼƬ�б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = this.jzdjService.findJzdjFile();
		request.setAttribute("jzdjList", list);
		return mapping.findForward("jzdj-list");
	}
	/**
	 * ɾ����׼�ؼ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		this.jzdjService.delFileRefById(fileId);
		return new ActionForward("jzdj.do?method=list", true);
	}
	/**
	 * �ϴ���׼�ؼ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return mapping.findForward("jzdj-info");
	}
	/**
	 * �����׼�ؼ��ļ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//�ϴ��ļ�
		String fileName = request.getParameter("fileName");
		MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler(); 
		Hashtable elements = multipartRequestHandler.getFileElements(); 
		FormFile file = null;
		// ѭ������ÿһ���ļ� 
		Collection values = elements.values(); 
		for (java.util.Iterator i = values.iterator(); i.hasNext();) { 
			file = (org.apache.struts.upload.FormFile) i.next();
		}
		if(file!=null&&StringUtils.isNotBlank(fileName)){
			byte[] size = file.getFileData();
			String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
			this.jzdjService.uploadFile(size, fileName, fileType, GlobalConstants.JZDJ_FILE);
		}
		return new ActionForward("jzdj.do?method=list", true);
	}
}
