package com.wonders.android.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.android.service.AppService;
import com.wonders.db.page.PageUtil;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.util.PropertiesUtil;
public class NoticeAction extends DispatchAction{
	private AppService appService;

	public void setAppService(AppService appService) {
		this.appService = appService;
	}
	/**
	 * ��ȡ�ѳ��ù����б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ActionForward  getAssignNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//��ȡ���ù���url
		String assignNoticeUrl= "http://" + PropertiesUtil.getInstance().getProperty("app_ip") + "/android/notice.do?method=getAssignNoticeList";
		//��õ�ǰҳ��
		int currentPage=1;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//����ÿҳ��ʾ������
		//int pageSize=15;
		//�������ļ���ȡÿҳ��ʾ������
		int pageSize=Integer.parseInt(PropertiesUtil.getInstance().getProperty("pageSize"));
		//���ò�ѯ����
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//��ѯ���ù����б�ifReleased='1' ;noticeDate����
		PageList tdscNoticeAppPageList=appService.getAssignNoticesPageList(condition);	
		//��ȡ��ҳ��
		int totalPage=tdscNoticeAppPageList.getPager().getTotalPages();
		//��ȡ������
		int totalRows=tdscNoticeAppPageList.getPager().getTotalRows();
		//��ȡ���һҳ������
		int syRows=totalRows-(totalPage-1)*15;
		if(currentPage==1){
			//���ص�һҳ��15��������ָ��ҳ��
			request.setAttribute("tdscNoticeAppPageList", tdscNoticeAppPageList);
			request.setAttribute("assignNoticeUrl", assignNoticeUrl);
			response.setCharacterEncoding("UTF-8");
			return mapping.findForward("assignNotice");
		}else{
			//�ӵڶ�ҳ��ʼʶ��ˢ�¶��������������أ�ÿ�μ���һҳ
			Map<String,Object> map=new HashMap<String, Object>();
			if(tdscNoticeAppPageList!=null&&tdscNoticeAppPageList.getList().size()>0){
				map.put("textlist", tdscNoticeAppPageList);
				map.put("title", "�����õ�ʹ��Ȩ���ù���");
				map.put("totalPage", totalPage);
				map.put("syRows", syRows);
				map.put("currentPage", currentPage);
				map.put("pageSize",pageSize);
			}
			//JSONObject jsonObject = JSONObject.fromObject(tdscTradeNoticeAppPageList);
			//��map����תΪjson�ַ���
			JSONObject jsonObject = JSONObject.fromObject(map);
			//System.out.println(jsonObject.toString());
			//ajax�ص������ݣ���json���ݷ���
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");		
			PrintWriter pw=response.getWriter();
			pw.write(jsonObject.toString());
			pw.close();
			return null;
		}
		
		
	}
	public ActionForward getTradeNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//��ȡ���ù���url
		String tradeNoticeUrl= "http://" + PropertiesUtil.getInstance().getProperty("app_ip") + "/android/notice.do?method=getTradeNoticeList";
		//��õ�ǰҳ
		int currentPage=1;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//����ҳ������
		//int pageSize=15;
		//�������ļ���ȡÿҳ��ʾ������
		int pageSize=Integer.parseInt(PropertiesUtil.getInstance().getProperty("pageSize"));
		//������������
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//��ȡ�ɽ������б�ifResultPublish='1' resultPublishDate����
		PageList tdscTradeNoticeAppPageList=appService.getTradeNoticePageList(condition);
		System.out.println("-----------------------------1------------------"+currentPage);
		//��ȡ��ҳ��
		int totalPage=tdscTradeNoticeAppPageList.getPager().getTotalPages();
		System.out.println("-----------------------------2------------------"+currentPage);
		//��ȡ������
		int totalRows=tdscTradeNoticeAppPageList.getPager().getTotalRows();
		System.out.println("-----------------------------3------------------"+currentPage);
		//��ȡ���һҳ������
		int syRows=totalRows-(totalPage-1)*15;
		System.out.println("-----------------------------cuurrent2------------------"+currentPage);
		if(currentPage==1){
			System.out.println("-----------------------------�������------------------");
			//��ȡ��һҳ�ɽ����棬�����ݴ���ҳ��
			request.setAttribute("tdscTradeNoticeAppPageList", tdscTradeNoticeAppPageList);
			request.setAttribute("tradeNoticeUrl", tradeNoticeUrl);
			response.setCharacterEncoding("utf-8");
			return mapping.findForward("tranceNotice");
		}else{
			//�ӵڶ�ҳ��ʼʶ����ض�����ÿ�μ���һҳ����
			Map<String,Object> map=new HashMap<String, Object>();
			if(tdscTradeNoticeAppPageList!=null&&tdscTradeNoticeAppPageList.getList().size()>0){
				map.put("textlist", tdscTradeNoticeAppPageList);
				map.put("title", "�����õ�ʹ��Ȩ�ɽ�����");
				map.put("totalPage", totalPage);
				map.put("syRows", syRows);
				map.put("currentPage", currentPage);
				map.put("pageSize",pageSize);
			}
			//JSONObject jsonObject = JSONObject.fromObject(tdscTradeNoticeAppPageList);
			//��mapתΪjson�ַ���
			JSONObject jsonObject = JSONObject.fromObject(map);
			//System.out.println(jsonObject.toString());
			//��json�ַ�������
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");		
			PrintWriter pw=response.getWriter();
			pw.write(jsonObject.toString());
			pw.close();
			return null;
		}
		
		
	}
	/*public ActionForward getMoreTradeNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//��õ�ǰҳ
		int currentPage=0;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//����ҳ������
		int pageSize=15;		
		//������������
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//��ȡ�ɽ������б�
		PageList tdscTradeNoticeAppPageList=appService.getTradeNoticePageList(condition);	
		Map<String,Object> map=new HashMap<String, Object>();
		if(tdscTradeNoticeAppPageList!=null&&tdscTradeNoticeAppPageList.getList().size()>0){
			map.put("textlist", tdscTradeNoticeAppPageList);
			map.put("title", "�����õ�ʹ��Ȩ�ɽ�����");
		}
		//JSONObject jsonObject = JSONObject.fromObject(tdscTradeNoticeAppPageList);
		JSONObject jsonObject = JSONObject.fromObject(map);
		System.out.println(jsonObject.toString());
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");		
		PrintWriter pw=response.getWriter();
		pw.write(jsonObject.toString());
		pw.close();
		return null;
	}*/
}
