package com.wonders.tdsc.publishinfo.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockContractInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.publishinfo.service.TdscBlockContractInfoService;
import com.wonders.tdsc.publishinfo.web.form.TdscBlockContractInfoForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBlockContractInfoAction extends BaseAction{

    private CommonQueryService commonQueryService;
    
    private TdscBlockInfoService tdscBlockInfoService;
	
    private TdscBlockContractInfoService tdscBlockContractInfoService;
    
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}
	
	public void setTdscBlockContractInfoService(
			TdscBlockContractInfoService tdscBlockContractInfoService) {
		this.tdscBlockContractInfoService = tdscBlockContractInfoService;
	}
	/**
	 * 查询出让合同列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryContractInfoList_old(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		this.bindObject(condition, tdscBlockContractInfoForm);
		
		//if(tdscBlockForm!=null){
		if(tdscBlockContractInfoForm!=null){
		condition.setContractNum(tdscBlockContractInfoForm.getContractNum());
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
		condition.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
		condition.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
		}
		
        TdscBaseQueryCondition condition2 = new TdscBaseQueryCondition();
        this.bindObject(condition2, tdscBlockContractInfoForm);
        if(tdscBlockContractInfoForm!=null){
        condition2.setContractNum(tdscBlockContractInfoForm.getContractNum());
    	condition2.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
    	condition2.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
    	condition2.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
    	}
        
        //查询交易结束地块的条件
        condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);
        List statusIdList = new ArrayList();
        statusIdList.add("9001");
        statusIdList.add("9002");
        condition.setStatusIDList(statusIdList);
        
        //查询交易成功，交易失败，交易中止的地块的条件
        List statusList = new ArrayList();
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADESUCCESS);
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADEFAILURE);
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADEEND);
        condition2.setStatusList(statusList);
        
        // 获得用户登陆的区县信息
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
            condition2.setDistrictId(String.valueOf(quList.get(0)));
        }else if(quList.size()>1){
            condition.setDistrictIdList(quList);
            condition2.setDistrictIdList(quList);
        }
        
        List viewList1 = tdscBlockContractInfoService.queryTdscBlockAppViewListWithoutNode(condition);
        List viewList2 = tdscBlockContractInfoService.queryTdscBlockAppViewListWithoutNode(condition2);        
        for(int i=0;i<viewList2.size();i++){
            viewList1.add(viewList2.get(i));
        }
        
        //lz+  土地总面积(㎡)存入totalLandArea   规划建筑总面积(㎡) totalbuildingarea
        TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
        TdscBlockPart tdscBlockPart = new TdscBlockPart();
        List resultList = new ArrayList();
        
        BigDecimal totalArea = new BigDecimal(0.00);//土地面积
        BigDecimal buildingArea = new BigDecimal(0.00);//规划建筑面积
        String contrackNum = "";//合同编号
        String acceptPeason = "";//受让人
        
        if(viewList1!=null&&viewList1.size()>0){
        	for(int i=0;i<viewList1.size();i++){
        		tdscBlockAppView = (TdscBlockAppView)viewList1.get(i);
        		if(tdscBlockAppView.getBlockId()!=null&!"".equals(tdscBlockAppView.getBlockId())){
        			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
        			for(int j=0;j<tdscBlockPartList.size();j++){
        				tdscBlockPart = (TdscBlockPart)tdscBlockPartList.get(j);
        				if(tdscBlockPart.getBlockArea()!=null)
        				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
        				if(tdscBlockPart.getPlanBuildingArea()!=null)
        				buildingArea = buildingArea.add(tdscBlockPart.getPlanBuildingArea());
        			}
        			tdscBlockAppView.setTotalLandArea(totalArea);
	        		tdscBlockAppView.setTotalBuildingArea(buildingArea);
        		
        			//增加出让合同信息
        			TdscBlockContractInfo TdscBlockContractInfo = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(tdscBlockAppView.getBlockId());
        			if(TdscBlockContractInfo!=null){
	        			contrackNum = TdscBlockContractInfo.getContractNum();
	        			acceptPeason = TdscBlockContractInfo.getAcceptPerson();
	        			//合同编号 存在rangeEast中，受让人  存在 RangeNorth
		        		tdscBlockAppView.setRangeEast(contrackNum);
		        		tdscBlockAppView.setRangeNorth(acceptPeason);
		        		
		        		resultList.add(tdscBlockAppView);
        			}
        		}
            }
        }
        
        //lz end
        
//      拼装分页
        PageList tdscBlockAppPageList = new PageList();
        //tdscBlockAppPageList.setList(viewList1);
        tdscBlockAppPageList.setList(viewList1);

        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        tdscBlockAppPageList = PageUtil.getPageList(viewList1, pageSize,currentPage);
		request.setAttribute("PageList", tdscBlockAppPageList);// 按条件查询列表
        
		condition2.setBlockName(StringUtil.ISO88591toGBK(condition2.getBlockName()));
		request.setAttribute("queryAppCondition", condition2);
		return mapping.findForward("queryList");
	}
	
	/**
	 * 查询出让合同列表from视图TDSC_CONTRACK_INFO_VIEW    lz+   20090713
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryContractInfoList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		this.bindObject(condition, tdscBlockContractInfoForm);
		
		if(tdscBlockContractInfoForm!=null){
		condition.setContractNum(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getContractNum()));
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
		condition.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
		condition.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
		}
        
        //查询交易成功地块的条件
        condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADESUCCESS);
        condition.setTranResult("01");
        
        List statusIdList = new ArrayList();
        statusIdList.add("9001");
        statusIdList.add("9002");
        condition.setStatusIDList(statusIdList);
        
        // 获得用户登陆的区县信息
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
        }else if(quList.size()>1){
            condition.setDistrictIdList(quList);
        }
        condition.setOrderKey("contractSignDate");
        condition.setOrderType("desc");
    	//获取用户信息
    	SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
    	//获得按钮权限列表
    	List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
    	//转化为buttonMap
    	Map buttonMap = new HashMap();
    	for ( int j = 0; j < buttonList.size() ; j++){
    		String id = (String)buttonList.get(j);
    		buttonMap.put(id, buttonList.get(j));
    	}
    	//判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
    	if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null){
    		condition.setUserId(user.getUserId());
    	}
        
        List viewList1 = tdscBlockContractInfoService.queryContractInfoListFromView(condition);
        //lz end
        
        // 拼装分页
        PageList tdscBlockAppPageList = new PageList();
        tdscBlockAppPageList.setList(viewList1);

        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        tdscBlockAppPageList = PageUtil.getPageList(viewList1, pageSize,currentPage);
		request.setAttribute("PageList", tdscBlockAppPageList);//按条件查询列表
        
		condition.setContractNum(StringUtil.ISO88591toGBK(condition.getContractNum()));
		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		condition.setLandLocation(StringUtil.ISO88591toGBK(condition.getLandLocation()));
		condition.setAcceptPerson(StringUtil.ISO88591toGBK(condition.getAcceptPerson()));
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("queryList");
	}

	
	//合同详细信息
	public ActionForward toViewContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		String blockId = request.getParameter("blockId");
		String type = request.getParameter("type");
		
		TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		if(blockId!=null&&!"".equals(blockId))
		   tdscBaseQueryCondition.setBlockId(blockId);
		   tdscBlockContractInfo = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
		   tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppViewByBlockId(blockId);
		
		   request.setAttribute("tdscBlockContractInfo", tdscBlockContractInfo);
		   request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		   request.setAttribute("type", type);
		
		   if("xiugai".equals(type))
		       return mapping.findForward("toEditContractInfo");
		   else
			   return mapping.findForward("toViewContractInfo");
		
     }
	
	
	//合同详细信息保存
	public ActionForward toSaveContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		// 获取页面参数
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		
		String blockId = request.getParameter("blockId");
		String contrackInfoId = request.getParameter("contrackInfoId");
		if(blockId!=null&&!"".equals(blockId)){
			TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
			
			//文件上传start
	        String appRealPath = request.getSession().getServletContext().getRealPath("/");
	        String fileName = null;
	        String savePath = null;//保存文件的文件夹路径
			String filePath = null;//保存的文件路径
			String endStyle = null;
			String contractNum = tdscBlockContractInfoForm.getContractNum();//作为文件名称保存
			
			FormFile file = null;
			if(tdscBlockContractInfoForm.getFile().getFileName()!=null&&!"".equals(tdscBlockContractInfoForm.getFile().getFileName())&&!"null".equals(tdscBlockContractInfoForm.getFile().getFileName())){
				try {
					file = tdscBlockContractInfoForm.getFile();//取得上传的文件
					InputStream stream = file.getInputStream();//把文件读入
					String uplodFilePathName = file.getFileName();
		            fileName = uplodFilePathName.substring(uplodFilePathName.lastIndexOf(File.separator) + 1);
					endStyle = fileName.substring(uplodFilePathName.lastIndexOf("."));
		            //filePath = appRealPath+"dk"+File.separator+"upload"+File.separator+"tempFile"+File.separator+System.currentTimeMillis()+endStyle;
					
					//文件夹路径
					savePath = appRealPath+"tdsc"+File.separator+"uploadfile"+File.separator+"contrackfile";
					filePath = appRealPath+"tdsc"+File.separator+"uploadfile"+File.separator+"contrackfile"+File.separator+contractNum+endStyle;
					//为了保证目录存在,如果没有则新建该目录
					File saveDir  = new File(savePath);
		            if (!saveDir .exists()) {
		            	saveDir .mkdirs();
					}
		            //保存文件
		            File fileOnServer = new File(filePath);
					OutputStream bos = new FileOutputStream(fileOnServer);
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
						bos.write(buffer, 0, bytesRead);
					}
					
				} catch (Exception e) {
		            e.printStackTrace();
		        }
		    } else {
		        System.out.println("the enctype must be multipart/form-data");
		    }
			//文件上传end
			
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		
		tdscBlockContractInfo.setActionDate(new Timestamp(System.currentTimeMillis()));
		tdscBlockContractInfo.setActionUser(StringUtils.trimToEmpty(user.getDisplayName()));
		tdscBlockContractInfo.setIfValidity("1");
		tdscBlockContractInfo.setBlockId(blockId);
		tdscBlockContractInfo.setContractInfoId(contrackInfoId);
		
		//保存保存的文件名称和路径
		if(tdscBlockContractInfoForm.getFile().getFileName()!=null&&!"".equals(tdscBlockContractInfoForm.getFile().getFileName())&&!"null".equals(tdscBlockContractInfoForm.getFile().getFileName())){
			tdscBlockContractInfo.setContractFileName(contractNum + StringUtils.trimToEmpty(endStyle));
			tdscBlockContractInfo.setContractFileUrl(StringUtils.trimToEmpty(filePath));
		}else{
			TdscBlockContractInfo tdscBlockContractInfoOld = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
			if(tdscBlockContractInfoOld!=null){
				tdscBlockContractInfo.setContractFileName(StringUtils.trimToEmpty(tdscBlockContractInfoOld.getContractFileName()));
				tdscBlockContractInfo.setContractFileUrl(StringUtils.trimToEmpty(tdscBlockContractInfoOld.getContractFileUrl()));
			}
		}
		
		bindObject(tdscBlockContractInfo, tdscBlockContractInfoForm);
		
		tdscBlockContractInfoService.save(tdscBlockContractInfo);
		}
		
		return queryContractInfoList(mapping,null,request,response);
		
     }
	
	/**
	 * 合同档案下载  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void fileDownLand(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		// 获取页面参数
		String blockId = request.getParameter("blockId");
		TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
		
		if(blockId!=null&&!"".equals(blockId)){
			tdscBlockContractInfo = tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
			String filePath = tdscBlockContractInfo.getContractFileUrl();
			
			
			File f = new File(filePath);
			if(!f.exists()){
			response.sendError(404,"File not found!");
			return;
			}
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset(); //非常重要
			
			//纯下载方式
			response.setContentType("application/x-msdownload"); 
			response.setHeader("Content-Disposition", "attachment; filename=" + f.getName()); 
			
			OutputStream out = response.getOutputStream();
			while((len = br.read(buf)) >0)
			out.write(buf,0,len);
			br.close();
			out.close();
		}
		
     }
		
		/**
		* 删除出让文件
		* 
		*/
		public ActionForward toDeleteContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			String blockId = request.getParameter("blockId");
		
			try{
			    tdscBlockContractInfoService.deleteContractInfo(blockId);
			}catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("saveMessage", "删除失败");
            }
		
			return queryContractInfoList(mapping,null,request,response);
		}
	
		/**
		* 返回出让合同列表without条件
		* 
		*/
		public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			return queryContractInfoList(mapping,null,request,response);
		}
	
	
}
