package com.wonders.tdsc.bidder.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderFundService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBidderFundAction extends BaseAction {

    private TdscBidderFundService tdscBidderFundService;

    private CommonQueryService commonQueryService;

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setTdscBidderFundService(TdscBidderFundService tdscBidderFundService) {
        this.tdscBidderFundService = tdscBidderFundService;
    }

    /**
     * 根据条件获得竞买土地列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        bindObject(condition, form);
        // 如果是流程中的节点需要设置节点编号 FLOW_NODE_FILE_RELEASE

        List nodeList = new ArrayList();
        nodeList.add(FlowConstants.FLOW_NODE_BIDDER_FUND_AUCTION);
        nodeList.add(FlowConstants.FLOW_NODE_BIDDER_FUND_LISTING);
        nodeList.add(FlowConstants.FLOW_NODE_BIDDER_FUND_TENDER);
        condition.setNodeList(nodeList);
        condition.setOrderKey("blockNoticeNo");
        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
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
        // 查询列表
        PageList pageList = (PageList) commonQueryService.queryTdscBlockAppViewPageList(condition);
        
        request.setAttribute("pageList", pageList);
        request.setAttribute("condition", condition);

        return mapping.findForward("fundBlockList");
    }

    /**
     * 根据appId获得竞买人列表 (list.jsp 中 点击“记录”，进入list_dzzjgl.jsp)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    public ActionForward queryBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscBidderCondition condition = new TdscBidderCondition();
        // tdscBidderForm.setAppId(appId);
        bindObject(condition, form);
        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        request.setAttribute("pageList", tdscBidderFundService.findPageFundList(condition));
        request.setAttribute("condition", condition);
        request.setAttribute("tdscBlockAppView", queryOneBlockTran(condition.getAppId()));
        return mapping.findForward("bidderPersonAppList");
    }

    /**
     * 根据appId获得竞买人列表 ( list.jsp 中 点击“修改” ,进入list_bidchange.jsp)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryBidderDzqk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscBidderCondition condition = new TdscBidderCondition();
        bindObject(condition, form);

        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        request.setAttribute("pageList", tdscBidderFundService.findChangeFundList(condition));
        request.setAttribute("condition", condition);
        request.setAttribute("tdscBlockAppView", queryOneBlockTran(condition.getAppId()));
        return mapping.findForward("bidderChange");
    }
    /**查询机审结果
     * ( list.jsp 中 点击“机审结果” ,进入list_jsjg.jsp)
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryJsjg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        TdscBidderCondition condition = new TdscBidderCondition();
        bindObject(condition, form);

        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        
        request.setAttribute("pageList", tdscBidderFundService.queryJsjg(condition));
        request.setAttribute("condition", condition);
        request.setAttribute("tdscBlockAppView", queryOneBlockTran(condition.getAppId()));
        return mapping.findForward("queryJsjg");
    }
    
    public ActionForward queryCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        TdscBidderCondition condition = new TdscBidderCondition();
        bindObject(condition, form);

        request.setAttribute("pageList", tdscBidderFundService.findReturnSetByCondition(condition));
        request.setAttribute("condition", condition);
        return mapping.findForward("bidderPersonAppList");
    }
    /**
     * 根据条件查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryBidderPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscBidderCondition condition = new TdscBidderCondition();
        bindObject(condition, form);

        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        request.setAttribute("pageList", tdscBidderFundService.findFundQueryList(condition));
        request.setAttribute("condition", condition);
        request.setAttribute("tdscBlockTranApp", tdscBidderFundService.queryOneBlockTran(condition.getAppId()));
        return mapping.findForward("bidderDzqkAppList");
    }

    /**
     * 修改到账时间和数额，到账情况
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward recordPersonDzSeSj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String  ifPrint=(String)request.getParameter("ifPrint");
        TdscBidderForm personForm = (TdscBidderForm) form;
        //String appId = personForm.getAppId();
        // 获取页面参数 bidderPersonIds bzjDzses bzjDzsjs；
        String bidderPersonIds[] = personForm.getBidderPersonIds();
        String bzjDzses[] = personForm.getBzjDzses();
        Date bzjDzsjs[] = personForm.getBzjDzsjs();

        List personAppList = new ArrayList();
        //List bidPerIdList = new ArrayList();
        if (bzjDzses != null && bzjDzses.length > 0 && bzjDzsjs != null) {
            for (int i = 0; i < bzjDzses.length; i++) {
                // 构造临时的 TdscBidderPersonApp实体
                if (bzjDzses[i] != null && bzjDzsjs[i] != null && !"".equals(bzjDzses[i]) && !"".equals(bzjDzsjs[i])) {
                    TdscBidderPersonApp personApp = new TdscBidderPersonApp();
                    
                    //String bidPerId = bidderPersonIds[i];
                    //bidPerIdList.add(bidPerId);
                   // 把实体添加到 personAppList中
                    personApp.setBidderPersonId(bidderPersonIds[i]);
                    personApp.setBzjDzse(new BigDecimal(bzjDzses[i]));
                    personApp.setBzjDzsj(bzjDzsjs[i]);
                    personAppList.add(personApp);
                }
            }
        }
        // 修改竞买人的到账时间和数额，到账情况并且修改 申请竞买的到账情况
        tdscBidderFundService.modifyPersonDzqk(personAppList);
        if("11".equals(ifPrint)){
        	TdscBidderForm newqueryForm = new TdscBidderForm();
        	newqueryForm.setAppId(personForm.getAppId());
        	request.setAttribute("ifPrint", ifPrint);
        	return queryBidderDzqk(mapping, form, request, response);
        }
        return query(mapping, form, request, response);
    }

    /**
     * 根据appId 查询 竞买人列表 并打印到账情况列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bidderPersonAppDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String appId = (String) request.getParameter("appId");
        // 根据appId 查询该地块的 noticeId,blockName
        TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) queryOneBlockTran(appId);
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)tdscBidderFundService.queryOneBlockTran(appId);
        String noticeNo = (String) tdscBlockAppView.getBlockNoticeNo();
        String blockName = (String) tdscBlockAppView.getBlockName();
        // 根据appId 查询 竞买人列表
        List bidderPresonAppList = (List) tdscBidderFundService.queryBidPreByAppId(appId);

        if (bidderPresonAppList != null && bidderPresonAppList.size() > 0) {
            // 打印列表中显示 到账截至时间
            Date marginEndDate = (Date) tdscBlockAppView.getMarginEndDate();
            // 注意 日期类型大小写 yyyy-MM-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (marginEndDate != null) {
                String strNowDate = sdf.format(marginEndDate);

                String endYear = strNowDate.substring(0, 4);
                String endMonth = strNowDate.substring(5, 7);
                if ("0".equals(endMonth.substring(0, 1))) {
                    endMonth = strNowDate.substring(6, 7);
                }
                String endDay = strNowDate.substring(8, 10);
                if ("0".equals(endDay.substring(0, 1))) {
                    endDay = strNowDate.substring(9, 10);
                }
                request.setAttribute("endYear", endYear);
                request.setAttribute("endMonth", endMonth);
                request.setAttribute("endDay", endDay);
            }

            // 需要返回页面的值
            TdscBidderCondition condition = new TdscBidderCondition();
            bindObject(condition, form);

            // 设置页面行数
            condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
            Date printTime  = new Date(System.currentTimeMillis());
            tdscBlockAppView.setTempDate(printTime);
            String retPrintHour =  printTime.toString().substring(11, 13);
            String retPrintMini =  printTime.toString().substring(14, 16);
            request.setAttribute("retPrintHour", retPrintHour);
            request.setAttribute("retPrintMini", retPrintMini);
            request.setAttribute("printTime", printTime.toString());
            request.setAttribute("pageList", tdscBidderFundService.findChangeFundList(condition));
            request.setAttribute("tdscBlockAppView", tdscBlockAppView);
            request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
            request.setAttribute("blockName", blockName);
            request.setAttribute("noticeNo", noticeNo);
            return mapping.findForward("tdscbidderDzqkList");
        }
        return mapping.findForward("nullList");
    }

    /**
     * 根据appId 查询 地块的名称等信息
     * 
     * @param appId
     * @return
     */
    public TdscBlockAppView queryOneBlockTran(String appId) {
        // TdscBlockTranApp tdscBlockTranApp =(TdscBlockTranApp)tdscBidderFundService.queryOneBlockTran(appId);
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
        return tdscBlockAppView;
    }
    
    /**
     * 人工改变竞买人入围
     */
    public ActionForward modifyRwqkByPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
    	String appId = (String) request.getParameter("appId");
    	TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
    	TdscBidderCondition conditionback = new TdscBidderCondition();
    	 
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
        conditionback.setAppId(appId);
        conditionback.setAcceptNo("");
        
        conditionback.setAppId(appId);
        
        String bidId = (String) request.getParameter("bidId");
    	String subtype = (String) request.getParameter("subtype");
        
    	//判断状态
    	tdscBidderFundService.modifyRwqkByPerson(appId,bidId,subtype);
    	return mapping.findForward("modifyRwqkByPerson");
    }

}
