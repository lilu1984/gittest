package com.wonders.tdsc.tdscbase.web;

import java.io.PrintWriter;
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

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.web.form.TdscBlockForm;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPjxxInfo;
import com.wonders.tdsc.bo.TdscBlockQqjcInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockMaterialCondition;
import com.wonders.tdsc.bo.condition.TdscListingInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.lob.bo.TdscEsClob;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscQueryAction extends BaseAction {

	// 业务service注入到action
	private CommonQueryService commonQueryService;

	private TdscBlockInfoService tdscBlockInfoService;

	private TdscLocalTradeService tdscLocalTradeService;

	private AppFlowService appFlowService;
	
	private CommonFlowService commonFlowService;
	
	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBlockInfoService(
			TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscLocalTradeService(
			TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	/**
	 * 根据appId查询待办地块基本信息
	 */
	public ActionForward toViewTranAppInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 接收参数
		String appId = request.getParameter("appId");
		String actionType = request.getParameter("actionType");
		// 通用查询方法 查询出相应视图数据
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
		String blockId = commonInfo.getBlockId();
		String blockType = commonInfo.getBlockType();
		String statusId = commonInfo.getStatusId();
		//	查询土地基本信息
		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
		//	查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		//“规划设计要点”字段，clob
		TdscEsClob tdscEsClob = (TdscEsClob)tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		//	查询土地使用信息列表
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);	
		// 查询历史意见
		Map opnnMap;
		try {
			opnnMap = appFlowService.queryOpnnInfo(appId);
			request.setAttribute("opnninfo", opnnMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 签收材料list
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);
		//获得配建信息
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo)tdscBlockInfoService.getTdscBlockPjxxInfoByBlockId(blockId);
		
        Map retMap = new HashMap();
        retMap.put("tdscBlockInfo", tdscBlockInfo);
        retMap.put("tdscBlockTranApp", tdscBlockTranApp);
        retMap.put("tdscEsClob", tdscEsClob);
        retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);
        
        request.setAttribute("retMap", retMap);
        request.setAttribute("actionType", actionType);
		request.setAttribute("statusId", statusId);
		request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
		request.setAttribute("commonInfo", commonInfo);
		request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);

		// 判断土地类型，确定页面走向
		if (blockType != null
				&& (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer
						.parseInt(blockType)) {
			return mapping.findForward("commonQueryGy");
		}
        //查询子地块信息(zzf)
        if(null!=blockId&&!"".equals(blockId)){
            List blockPartList= this.tdscBlockInfoService.getTdscBlockPartList(blockId);
            request.setAttribute("blockPartList", blockPartList);
        }
        
        TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo)tdscBlockInfoService.getQqjcInfoByblockId(blockId);
        request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
        
		TdscBidderPersonApp tdscBidderPersonApp = commonQueryService.getPurposePersonByAppId(appId);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		
		List tdscBlockRemisemoneyDefrayList = (List)tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId);
		request.setAttribute("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);
		
		List bankDicList = tdscBlockInfoService.queryBankDicList();
		request.setAttribute("bankDicList", bankDicList);
		
		return mapping.findForward("commonQuerySy");
	}
	
	public ActionForward queryDqAppList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm)form;

		this.bindObject(condition, tdscBlockForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockForm.getBlockName()));
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setOrderType("asc");
		condition.setOrderKey("blockNoticeNo");
		
		// 获得用户登陆的区县信息
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
        }
//        //按当前状态查询
//        
//        //入室审核当前状态为待出让
        if("00".equals(tdscBlockForm.getBlockAuditStatus())){
            condition.setNodeId(null);
        }
        else{
            List statusList = new ArrayList();
            statusList.add("01");
            statusList.add("00");
            condition.setStatusList(statusList);
            List statusIdList = new ArrayList();
            statusIdList.add("9001");
            statusIdList.add("9002");
            condition.setStatusIdList2(statusIdList);
        }
//        //入室审核当前状态为已出让待受理
//        }else if("01".equals(tdscBlockForm.getBlockAuditStatus())){
//            condition.setStatusId("0102");
//        //入室审核当前状态为已受理
//        }else if("02".equals(tdscBlockForm.getBlockAuditStatus())){
//            condition.setNodeId("01");
//            condition.setStatusId("0102");
//            
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
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
		if(pageList!=null && pageList.getList()!=null && pageList.getList().size()>0){
           
		List tempList = pageList.getList();
		List blockList = new ArrayList();
		//tempList便利出多个交易地块
		if(tempList!=null && tempList.size()>0){
		    for(int i=0;i<tempList.size();i++){
	    		TdscBlockAppView commonInfo = (TdscBlockAppView)tempList.get(i);
	    		//一个地块得到结点列表blokcNodeList
                if(null!=commonInfo){
                        if(null!=commonInfo.getAppId()){
                        List blokcNodeList = commonFlowService.getAppDiagramActiveNodeList(commonInfo.getAppId());
                        //结点列表blokcNodeList整合成string字符串
                        String nodeName = "";
                        if (blokcNodeList!=null && blokcNodeList.size()>0){
                            for (int j=0; j<blokcNodeList.size();j++){
                                TdscAppNodeStat appStat = (TdscAppNodeStat)blokcNodeList.get(j);
                                nodeName += appStat.getNodeName() + "，";
                            }
                        }
                        if (!"".equals(nodeName) && nodeName.length()>0){
                            nodeName = nodeName.substring(0, nodeName.length()-1);
                        }
                        //string字符串塞到地块对象commonInfo的nodeId中
                        commonInfo.setNodeId(nodeName);
                        }
                    }
                    //将改造的地块对象塞入blockList中
                    blockList.add(commonInfo);   
                }	   		
		    }

		pageList.setList(blockList);
	}
        //入室审核当前状态为待出让
        if(StringUtils.isNotEmpty(condition.getBlockAuditStatus())){
            condition.setNodeId("01");                       
        }
        //查询条件中的当前节点
        String transferMode = condition.getTransferMode();
        if(StringUtils.isNotEmpty(transferMode)){
           List nodeIdList = new ArrayList();
           if(GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)){
        	   nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("01");
           }
           if(GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)){
        	   nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("02");
           }
           if(GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)){
        	   nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("03");
           }
           request.setAttribute("nodeIdList", nodeIdList);// 按条件查询列表     
        }
		request.setAttribute("queryAppList", pageList);// 按条件查询列表
		
		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("queryDqAppList");
	}

	public ActionForward queryZxAppList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm)form;

		this.bindObject(condition, tdscBlockForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockForm.getBlockName()));
		condition.setTransferMode("3104");
		condition.setStatus("01");
		condition.setNodeId("16");
		condition.setOrderType("desc");
		condition.setOrderKey("blockNoticeNo");
        // 获得用户登陆的区县信息
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
        }else if(quList.size()>1){
            condition.setDistrictIdList(quList);
        }
		// 根据通用无节点查询view列表,得到appIdlist
		List viewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		TdscBlockAppView commonInfo = null;
		List appIdList = new ArrayList();
		for (int i = 0; i < viewList.size(); i++) {
			commonInfo = (TdscBlockAppView) viewList.get(i);
			appIdList.add(commonInfo.getAppId());
		}
		
		// 通过appIdList查出挂牌列表
		TdscListingInfoCondition tdscListingInfoCondition = new TdscListingInfoCondition();
		tdscListingInfoCondition.setAppIdList(appIdList);
		List tdscListingInfoList = tdscBlockInfoService.findBlistingInfoList(tdscListingInfoCondition);
		
		// 把viewlist和挂牌列表tdscListingInfoList整合在PageList中
		PageList tdscBlockAppPageList = new PageList();
		List list1 = new ArrayList();
		if (viewList != null && viewList.size() > 0) {
			int count = 0;
			for (int i = 0; i < viewList.size(); i++) {
				Map map = new HashMap();
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) viewList.get(i);
				map.put("tdscBlockAppView", tdscBlockAppView);

				if (count < tdscListingInfoList.size() + 1) {
					for (int j = 0; j < tdscListingInfoList.size(); j++) {
						TdscListingInfo tdscListingInfo = (TdscListingInfo) tdscListingInfoList.get(j);

						if (appIdList.get(i).toString().equals(tdscListingInfo.getAppId())) {
							map.put("tdscListingInfo", tdscListingInfo);
							count++;
						}
					}
				} else {
					map.put("tdscListingInfo", new TdscListingInfo());
				}
				list1.add(map);
			}
		}
		tdscBlockAppPageList.setList(list1);

		// 拼装分页信息
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		tdscBlockAppPageList = PageUtil.getPageList(list1, pageSize,currentPage);
		request.setAttribute("queryAppList", tdscBlockAppPageList);// 按条件查询列表
		
		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("queryZxAppList");
	}

	public ActionForward queryLsAppList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();       
		TdscBlockForm tdscBlockForm = (TdscBlockForm)form;
		this.bindObject(condition, tdscBlockForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockForm.getBlockName()));
		condition.setOrderKey("blockNoticeNo");
		condition.setOrderType("desc");
		
        TdscBaseQueryCondition condition2 = new TdscBaseQueryCondition();
        this.bindObject(condition2, tdscBlockForm);
        condition2.setBlockName(StringUtil.GBKtoISO88591(tdscBlockForm.getBlockName()));
        condition2.setOrderKey("blockNoticeNo");
        condition2.setOrderType("desc");
        
        //查询交易结束地块的条件
        condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);
        List statusIdList = new ArrayList();
        statusIdList.add("9001");

        statusIdList.add("9002");
        condition.setStatusIDList(statusIdList);
		//condition.setCurrentPage(currentPage);
		
		//condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
 
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
        
        List viewList1 = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
        List viewList2 = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition2);        
        for(int i=0;i<viewList2.size();i++){
            viewList1.add(viewList2.get(i));
        }
        
        //拼装分页
        PageList tdscBlockAppPageList = new PageList();
        tdscBlockAppPageList.setList(viewList1);

        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        tdscBlockAppPageList = PageUtil.getPageList(viewList1, pageSize,currentPage);
		request.setAttribute("queryAppList", tdscBlockAppPageList);// 按条件查询列表
		
		condition2.setBlockName(StringUtil.ISO88591toGBK(condition2.getBlockName()));
		request.setAttribute("queryAppCondition", condition2);
		return mapping.findForward("queryLsAppList");
	}

	// 查询历史挂牌价格列表
	public ActionForward queryLsjgList(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		// 接收参数
		String appId = request.getParameter("appId");
		String blockId = request.getParameter("blockId");
		
		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		PageList pageList = new PageList();
		if(null!= appId)
		{
			//根据appId查listingId;
			String listingId = this.tdscLocalTradeService.queryListingId(appId);
			if(null!= listingId)
			{
				List retList = new ArrayList();
				//根据listingId查挂牌列表;
				retList = this.tdscLocalTradeService.queryTdscListingAppListByListingId(listingId);
				//根据blockId查blockName;
				String blockName = tdscBlockInfoService.findBlockInfo(blockId).getBlockName();
				request.setAttribute("blockName", blockName);
				if(null!=retList)
				{
					pageList.setList(retList);
				}

				int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

				pageList = PageUtil.getPageList(retList, pageSize, currentPage);
				request.setAttribute("retList", pageList);
			}
			
		}					
		return mapping.findForward("queryListingList");
	}
    
    public ActionForward queryNodeListAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        String transferMode = request.getParameter("transferMode");
        List nodeList = null;
        if(StringUtils.isNotEmpty(transferMode)){
            if(GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)){
                nodeList = this.appFlowService.queryNodeListWithBlockTransfer("01");
            }
            if(GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)){
                nodeList = this.appFlowService.queryNodeListWithBlockTransfer("02");
            }
            if(GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)){
                nodeList = this.appFlowService.queryNodeListWithBlockTransfer("03");
            }
            // 将内容设置到输出
            response.setContentType("text/xml; charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter pw = response.getWriter();
    //      结点列表blokcNodeList整合成string字符串
            String node ="";
            String nodeName = "";
            String nodeId = "";
            if (nodeList!=null && nodeList.size()>0){
                for (int j=0; j<nodeList.size();j++){
                    nodeName += DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_FLOW_NODE).get(nodeList.get(j)) + "，";
                    nodeId += nodeList.get(j)+"，";
                }
            }
            // 返回给回调函数的参数
            if (!"".equals(nodeName) && nodeName.length()>0){
                nodeName = nodeName.substring(0, nodeName.length()-1);
            }
            if (!"".equals(nodeId) && nodeId.length()>0){
                nodeId = nodeId.substring(0, nodeId.length()-1);
            }
            node = nodeName +"$"+ nodeId;
            pw.write(node);
            pw.close();
         }
        return null;
    }

}
