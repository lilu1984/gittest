package com.wonders.tdsc.presell.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBlockPresell;
import com.wonders.tdsc.bo.condition.TdscBlockPresellCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.presell.service.TdscBlockPresellService;
import com.wonders.tdsc.presell.web.form.TdscBlockPresellForm;

public class TdscBlockPresellAction extends BaseAction {
	private TdscBlockPresellService tdscBlockPresellService;

	public void setTdscBlockPresellService(TdscBlockPresellService tdscBlockPresellService) {
		this.tdscBlockPresellService = tdscBlockPresellService;
	}

	public ActionForward toBlockPresell(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String presellId = request.getParameter("presellId");
		TdscBlockPresell tdscBlockPresell = new TdscBlockPresell();

		if (StringUtils.isNotBlank(presellId)) {
			tdscBlockPresell = tdscBlockPresellService.getBlockPresellById(presellId);
			if (tdscBlockPresell != null) {
				FileRef nominateImagefileRef = tdscBlockPresellService.getFileRefById(tdscBlockPresell.getNominateImageId());
				request.setAttribute("nominateImagefileRef", nominateImagefileRef);
			}
			List fileList = tdscBlockPresellService.findFileRefByBusId(tdscBlockPresell.getPresellId(), GlobalConstants.PRESELL_FILE);
			request.setAttribute("fileList", fileList);
		}

		request.setAttribute("tdscBlockPresell", tdscBlockPresell);
		request.setAttribute("saveMessage", "");

		return mapping.findForward("blockPresell");
	}

	public ActionForward saveBlockPresell(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBlockPresellForm tdscBlockPresellForm = (TdscBlockPresellForm) form;
		TdscBlockPresell tdscBlockPresell = new TdscBlockPresell();

		this.bindObject(tdscBlockPresell, tdscBlockPresellForm);
		ArrayList fileList = new ArrayList();
		String[] fileNameList = tdscBlockPresellForm.getFileName();
		FormFile nominateImageFile = tdscBlockPresellForm.getNominateImageFile();
		
		MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler(); 
		// 取得所有上传文件的对象集合 
		Hashtable elements = multipartRequestHandler.getFileElements(); 
		// 循环遍历每一个文件 
		Collection values = elements.values(); 
		for (java.util.Iterator i = values.iterator(); i.hasNext();) { 
			FormFile file = (org.apache.struts.upload.FormFile) i.next();
			if(!file.equals(nominateImageFile)){
				fileList.add(file);
			}
		}
		//对文件对象进行重新排序
		ArrayList fileListOrder = new ArrayList();
		for(int i=fileList.size()-1;i>=0;i--){
			fileListOrder.add(fileList.get(i));
		}
		tdscBlockPresellService.saveBlockPresell(tdscBlockPresell, fileListOrder, fileNameList, nominateImageFile);
		
		
		String presellId = tdscBlockPresell.getPresellId();
		if (StringUtils.isNotBlank(presellId)) {
			tdscBlockPresell = tdscBlockPresellService.getBlockPresellById(presellId);
			if (tdscBlockPresell != null) {
				FileRef nominateImagefileRef = tdscBlockPresellService.getFileRefById(tdscBlockPresell.getNominateImageId());
				request.setAttribute("nominateImagefileRef", nominateImagefileRef);
			}
			List fileRefList = tdscBlockPresellService.findFileRefByBusId(tdscBlockPresell.getPresellId(), GlobalConstants.PRESELL_FILE);
			request.setAttribute("fileList", fileRefList);
		}
		request.setAttribute("tdscBlockPresell", tdscBlockPresell);
		request.setAttribute("saveMessage", new String("保存成功！"));
		return new ActionForward("presell.do?method=toBlockPresell&presellId="+tdscBlockPresell.getPresellId(), true);
	}

	public ActionForward findBlockPresellList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取页面参数
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给入市申请页面 查询条件
		TdscBlockPresellCondition condition = new TdscBlockPresellCondition();
		
		String queryType = request.getParameter("queryType"); 
		if("queryBtn".equals(queryType)){
			TdscBlockPresellForm tdscBlockPresellForm = (TdscBlockPresellForm) form;
			this.bindObject(condition, tdscBlockPresellForm);
		}

		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		PageList blockPresellPageList = tdscBlockPresellService.findPageList(condition);

		request.setAttribute("blockPresellPageList", blockPresellPageList);// 查询土地信息
		request.setAttribute("condition", condition);// 返回查询条件

		return mapping.findForward("blockPresellList");
	}

	/**
	 * 发布公告信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBlockPresellForm presellForm = (TdscBlockPresellForm)form;
		String[] list = presellForm.getSelectedIds();
		List ids = Arrays.asList(list);
		tdscBlockPresellService.fabu(ids);
		return new ActionForward("presell.do?method=findBlockPresellList", true);
	}
	
	/**
	 * 删除预公告信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delPresell(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String presellId = request.getParameter("presellId");
		tdscBlockPresellService.delPresellById(presellId);
		return new ActionForward("presell.do?method=findBlockPresellList", true);
	}
	
	/**
	 * 删除预公告信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		String presellId = request.getParameter("presellId");
		tdscBlockPresellService.delFileRefById(fileId);
		return new ActionForward("presell.do?method=toBlockPresell&presellId=" + presellId, true);
	}
	
}
