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
	 * 获取已出让公告列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ActionForward  getAssignNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获取出让公告url
		String assignNoticeUrl= "http://" + PropertiesUtil.getInstance().getProperty("app_ip") + "/android/notice.do?method=getAssignNoticeList";
		//获得当前页码
		int currentPage=1;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//设置每页显示的条数
		//int pageSize=15;
		//从配置文件获取每页显示的条数
		int pageSize=Integer.parseInt(PropertiesUtil.getInstance().getProperty("pageSize"));
		//设置查询条件
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//查询出让公告列表：ifReleased='1' ;noticeDate倒叙
		PageList tdscNoticeAppPageList=appService.getAssignNoticesPageList(condition);	
		//获取总页数
		int totalPage=tdscNoticeAppPageList.getPager().getTotalPages();
		//获取总条数
		int totalRows=tdscNoticeAppPageList.getPager().getTotalRows();
		//获取最后一页的条数
		int syRows=totalRows-(totalPage-1)*15;
		if(currentPage==1){
			//加载第一页的15条，返回指定页面
			request.setAttribute("tdscNoticeAppPageList", tdscNoticeAppPageList);
			request.setAttribute("assignNoticeUrl", assignNoticeUrl);
			response.setCharacterEncoding("UTF-8");
			return mapping.findForward("assignNotice");
		}else{
			//从第二页开始识别刷新动作进行上拉加载，每次加载一页
			Map<String,Object> map=new HashMap<String, Object>();
			if(tdscNoticeAppPageList!=null&&tdscNoticeAppPageList.getList().size()>0){
				map.put("textlist", tdscNoticeAppPageList);
				map.put("title", "国有用地使用权出让公告");
				map.put("totalPage", totalPage);
				map.put("syRows", syRows);
				map.put("currentPage", currentPage);
				map.put("pageSize",pageSize);
			}
			//JSONObject jsonObject = JSONObject.fromObject(tdscTradeNoticeAppPageList);
			//将map数据转为json字符串
			JSONObject jsonObject = JSONObject.fromObject(map);
			//System.out.println(jsonObject.toString());
			//ajax回调的数据：将json数据返回
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");		
			PrintWriter pw=response.getWriter();
			pw.write(jsonObject.toString());
			pw.close();
			return null;
		}
		
		
	}
	public ActionForward getTradeNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获取出让公告url
		String tradeNoticeUrl= "http://" + PropertiesUtil.getInstance().getProperty("app_ip") + "/android/notice.do?method=getTradeNoticeList";
		//获得当前页
		int currentPage=1;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//设置页面条数
		//int pageSize=15;
		//从配置文件获取每页显示的条数
		int pageSize=Integer.parseInt(PropertiesUtil.getInstance().getProperty("pageSize"));
		//创建条件对象
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//获取成交公告列表：ifResultPublish='1' resultPublishDate倒叙
		PageList tdscTradeNoticeAppPageList=appService.getTradeNoticePageList(condition);
		System.out.println("-----------------------------1------------------"+currentPage);
		//获取总页数
		int totalPage=tdscTradeNoticeAppPageList.getPager().getTotalPages();
		System.out.println("-----------------------------2------------------"+currentPage);
		//获取总条数
		int totalRows=tdscTradeNoticeAppPageList.getPager().getTotalRows();
		System.out.println("-----------------------------3------------------"+currentPage);
		//获取最后一页的条数
		int syRows=totalRows-(totalPage-1)*15;
		System.out.println("-----------------------------cuurrent2------------------"+currentPage);
		if(currentPage==1){
			System.out.println("-----------------------------进入界面------------------");
			//获取第一页成交公告，将数据传入页面
			request.setAttribute("tdscTradeNoticeAppPageList", tdscTradeNoticeAppPageList);
			request.setAttribute("tradeNoticeUrl", tradeNoticeUrl);
			response.setCharacterEncoding("utf-8");
			return mapping.findForward("tranceNotice");
		}else{
			//从第二页开始识别加载动作，每次加载一页内容
			Map<String,Object> map=new HashMap<String, Object>();
			if(tdscTradeNoticeAppPageList!=null&&tdscTradeNoticeAppPageList.getList().size()>0){
				map.put("textlist", tdscTradeNoticeAppPageList);
				map.put("title", "国有用地使用权成交公告");
				map.put("totalPage", totalPage);
				map.put("syRows", syRows);
				map.put("currentPage", currentPage);
				map.put("pageSize",pageSize);
			}
			//JSONObject jsonObject = JSONObject.fromObject(tdscTradeNoticeAppPageList);
			//将map转为json字符串
			JSONObject jsonObject = JSONObject.fromObject(map);
			//System.out.println(jsonObject.toString());
			//将json字符串返回
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");		
			PrintWriter pw=response.getWriter();
			pw.write(jsonObject.toString());
			pw.close();
			return null;
		}
		
		
	}
	/*public ActionForward getMoreTradeNoticeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获得当前页
		int currentPage=0;
		if(StringUtils.isNotEmpty(request.getParameter("currentPage"))){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		//设置页面条数
		int pageSize=15;		
		//创建条件对象
		TdscNoticeAppCondition condition=new TdscNoticeAppCondition();
		condition.setCurrentPage(currentPage);
		condition.setPageSize(pageSize);
		//获取成交公告列表
		PageList tdscTradeNoticeAppPageList=appService.getTradeNoticePageList(condition);	
		Map<String,Object> map=new HashMap<String, Object>();
		if(tdscTradeNoticeAppPageList!=null&&tdscTradeNoticeAppPageList.getList().size()>0){
			map.put("textlist", tdscTradeNoticeAppPageList);
			map.put("title", "国有用地使用权成交公告");
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
