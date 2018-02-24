package com.wonders.tdsc.randomselect.web;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscCompereInfo;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscSpecialistInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.randomselect.service.TdscSelectService;
import com.wonders.tdsc.randomselect.web.form.SelectSpecialistForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.ShortMessageService;

public class SelectSpecailistAction extends BaseAction {

    private TdscSelectService tdscSelectService;

    private CommonQueryService commonQueryService;

    private TdscBidderAppService tdscBidderAppService;
    
    private ShortMessageService smsService;

    public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
        this.tdscBidderAppService = tdscBidderAppService;
    }

    public void setTdscSelectService(TdscSelectService tdscSelectService) {
        this.tdscSelectService = tdscSelectService;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setSmsService(ShortMessageService smsService) {
        this.smsService = smsService;
    }
    /**
     * 根据抽选类型抽选十位专家
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward toListSpecialist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //地块信息
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        String forwardType = "1";
        request.setAttribute("forwardType", forwardType);

        String selectType = request.getParameter("selectType");
        request.setAttribute("forwardType", forwardType);
        //查询专家
        //List sublist = new ArrayList();
        //		TdscBlockSelectApp tdscBlockSelectApp = new TdscBlockSelectApp();
        Date nowTime = new Date(System.currentTimeMillis());
        //		List specailistInfoList = this.tdscSelectService.querySpecialistInfoList(nowTime);

        //根据专家类别查出所需专家
        List sublist = this.tdscSelectService.specialistSelect(nowTime);

        request.setAttribute("sublist", sublist);

        return mapping.findForward("list");
    }

    /**
     * 保存专家信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward saveSpecialistInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        SelectSpecialistForm selectSpecialistForm = (SelectSpecialistForm) form;
        String specialistIds[] = selectSpecialistForm.getSpecialistIds();
        String specialistType[] = selectSpecialistForm.getSpecialistType();

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        request.setAttribute("condition", condition);
        String appId = request.getParameter("appId");
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        String selectType = request.getParameter("selectType");

        String blockNoticeNo = tdscBlockAppView.getBlockNoticeNo();

        TdscBlockPlanTable tdscBlockPlanTable = this.tdscSelectService.query(appId);
        //保存抽选结果
        List tdscBlockSelecctAppList = new ArrayList();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        for (int j = 0; j < specialistIds.length; j++) {
            String temp = "";
            if (String.valueOf(j + 1).length() == 1)
                temp = "0" + String.valueOf(j + 1);
            else
                temp = String.valueOf(j + 1);
            String sub = blockNoticeNo + selectType + temp;
            TdscSpecialistInfo tdscSpecialistInfo = this.tdscSelectService.getSpecialistInfoById(specialistIds[j]);
            TdscBlockSelectApp tempSelectApp = new TdscBlockSelectApp();
            tempSelectApp.setSelectedId(specialistIds[j]);
            tempSelectApp.setAppId(appId);
            tempSelectApp.setSelectUser(user.getDisplayName());
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            tempSelectApp.setSelectDate(nowTime);
            tempSelectApp.setSelectType(selectType);
            tempSelectApp.setSelectNum(sub);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            now.setHours(23);
            now.setMinutes(59);
            now.setSeconds(59);
            tempSelectApp.setReplyDeadline(now);
            tempSelectApp.setSpecialistType(specialistType[j]);
            tempSelectApp.setActivityDate(tdscBlockPlanTable.getOpeningDate());
            tempSelectApp.setActivityLoc(tdscBlockPlanTable.getOpeningLoc());
            tempSelectApp.setUnlockTime(tdscBlockPlanTable.getOpeningDate());
            tempSelectApp.setReplyStatus("00");
            tempSelectApp.setIsValid(GlobalConstants.SELECT_RESULT_VALID);
            this.tdscSelectService.saveBlockSelectApp(tempSelectApp);

            if (tdscSpecialistInfo.getMobilphone() != null) {
                String returnMsg = smsService.sendSpecialistMessage(tdscSpecialistInfo, tdscBlockPlanTable, tempSelectApp);
            }

            tdscBlockSelecctAppList.add(tempSelectApp);
        }
        tdscSelectService.updatestate(appId, "4");
        return mapping.findForward("success");
    }

    /**
     * 更改一条专家记录
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward changeSelectApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //TODO 1 通过APPID找出已经抽选出的7条记录
        //      2 将需要删除的记录从7条记录中删除
        //      3 将需要删除的记录更改状态
        //      4 再从剩下的专家中随机抽选出一条放如抽选信息表中
        //      5 将重新拼装好的7条记录返回页面

        String specialistId = request.getParameter("specialistId");//更改专家的Id
        String appId = request.getParameter("appId");
        String xuhao = request.getParameter("xuhao");
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        List sublist = tdscSelectService.queryBlockSelectAppListByAppId(appId);
        // 查询全部可用专家信息
        List tdscSpecialistInfoList = this.tdscSelectService.querySpecialistInfoList(nowTime);
        //删除已选过的七位专家
        if (tdscSpecialistInfoList != null && sublist != null) {
            for (int i = 0; i < tdscSpecialistInfoList.size(); i++) {
                TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) tdscSpecialistInfoList.get(i);
                for (int j = 0; j < sublist.size(); j++) {
                    TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) sublist.get(j);
                    String selectedId = tdscBlockSelectApp.getSelectedId();
                    if (tdscSpecialistInfo.getSpecialistId().equals(selectedId)) {
                        tdscSpecialistInfoList.remove(i);
                        i = 0;
                        break;
                    }
                }
            }
        }
        //随机抽选出一个专家
        Random r = new Random();
        int irdm = 0;
        irdm = r.nextInt(tdscSpecialistInfoList.size());
        TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) tdscSpecialistInfoList.get(irdm);

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        TdscBlockSelectApp tempSelectApp = new TdscBlockSelectApp();
        tempSelectApp.setAppId(appId);
        tempSelectApp.setSelectedId(tdscSpecialistInfo.getSpecialistId());
        tempSelectApp.setSelectDate(nowTime);
        tempSelectApp.setSelectType("1");
        tempSelectApp.setSelectUser(user.getDisplayName());
        this.tdscSelectService.saveBlockSelectApp(tempSelectApp);

        for (int i = 0; i < sublist.size(); i++) {
            TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) sublist.get(i);
            if (tdscBlockSelectApp.getSelectedId().equals(specialistId)) {
                //sublist.remove(i);
                this.tdscSelectService.removeChangeSpecialist(tdscBlockSelectApp);//删除数据库中更改的专家
                //sublist.add(i, tempSelectApp);
                break;
            }
        }

        String no = tdscSpecialistInfo.getSpecialistId() + ",";
        String name = tdscSpecialistInfo.getSpecialistName() + ",";
        String link = tdscSpecialistInfo.getPhone() + ",";
        String str = no + name + link + xuhao;
        // 将内容设置到输出
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();

        // 返回给回调函数的参数
        pw.write(str);
        pw.close();

        return null;
    }

    /**
     * 抽选地块列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward queryBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setBlockName(request.getParameter("blockName"));
        condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
        condition.setTransferMode(request.getParameter("transferMode"));
        //确定入口URL,评标专家抽选为"1"主持人抽选为"2"公证处抽选为"3"
        condition.setEnterWay(request.getParameter("enterWay"));
        //condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
        String enterWay = request.getParameter("enterWay");
        
        if (request.getParameter("currentPage") != null)
            condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        PageList tdscBlockAppViewPageList = new PageList();
        if ("1".equals(enterWay)) {

            condition.setNodeId(FlowConstants.FLOW_NODE_SELECT_SPECAILIST);

            List buttomList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
        	Map buttomMap = new HashMap();
        	for ( int j = 0; j < buttomList.size() ; j++){
        		String id = (String)buttomList.get(j);
        		buttomMap.put(id, buttomList.get(j));
        	}
            if (buttomMap.get(GlobalConstants.BUTTON_ID_MANAGE_SPECAILIST_REPLY) != null
                    && buttomMap.get(GlobalConstants.BUTTON_ID_SELECT_SPECAILIST) == null) {
                SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO);
                String userId = user.getUserId();
                //根据userId查询公证处ID
                TdscNotaryInfo notaryInfo = this.tdscSelectService.queryNotaryInfoByUserId(userId);
                if (notaryInfo != null && notaryInfo.getNotaryId() != null) {
                    condition.setSelectSpecialistNotaryID(notaryInfo.getNotaryId());
                }
                condition.setHasSelectSpecialist(GlobalConstants.SELECT_DONE);
            }
            
            tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
            if (tdscBlockAppViewPageList == null) {
                if ("3".equals(enterWay)) {
                    return mapping.findForward("queryBlockList");
                } else {
                    return mapping.findForward("queryList");

                }
            }
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            List list = tdscBlockAppViewPageList.getList();
            List selectList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                String appId = ((TdscBlockAppView) list.get(i)).getAppId();
                String select = this.tdscSelectService.ifSelectAgain(appId);
                selectList.add(select);
                List selectAppList = this.tdscSelectService.findSelectAppbyAppIdAndSelectType(appId, "05");
                if (selectAppList != null && selectAppList.size() > 0) {
                    for (int j = 0; j < selectAppList.size(); j++) {
                        TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) selectAppList.get(j);
                        if (tdscBlockSelectApp.getReplyDeadline() != null
                                && DateUtil.string2Timestamp(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATETIME), DateUtil.FORMAT_DATETIME)
                                        .after(tdscBlockSelectApp.getReplyDeadline())) {
                            String replyStatus = tdscBlockSelectApp.getReplyStatus();
                            if ("00".equals(replyStatus)) {
                                this.tdscSelectService.update(tdscBlockSelectApp.getSelectId(), replyStatus);
                            }
                        }
                    }
                }
            }
            request.setAttribute("selectList", selectList);

        } else if ("2".equals(enterWay)) {
            ArrayList nodeList = new ArrayList();
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_COMPERE_TENDER);
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_COMPERE_AUCTION);
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_COMPERE_LISTING);
            condition.setNodeList(nodeList);
            tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
        } else if ("3".equals(enterWay)) {
            ArrayList nodeList = new ArrayList();
            //		    nodeList.add(FlowConstants.FLOW_NODE_SELECT_B_NOTARY_TENDER);
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_B_NOTARY_AUCTION);
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_B_NOTARY_LISTING);
            nodeList.add(FlowConstants.FLOW_NODE_SELECT_C_NOTARY);
            condition.setNodeList(nodeList);
            tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
            if(null!=tdscBlockAppViewPageList){
	            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
	            List list = tdscBlockAppViewPageList.getList();
	            List selectList = new ArrayList();
	            if(null!=list && list.size()>0){
		            for (int i = 0; i < list.size(); i++) {
		            	TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
		                String appId = tdscBlockAppView.getAppId();
		                List selectAppList = this.tdscSelectService.findSelectAppbyAppIdAndSelectType(appId, "04");
		                if (selectAppList != null && selectAppList.size() > 0) {
		                	tdscBlockAppView.setTempStr(((TdscBlockSelectApp)selectAppList.get(0)).getSelectedId());
		                }
		            }
	            }
            }
        }

        request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
        request.setAttribute("condition", condition);
        if ("3".equals(enterWay)) {

            return mapping.findForward("queryBlockList");
        } else {
            return mapping.findForward("queryList");

        }

    }

    /**
     * 公证处抽选
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward selectNotary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String blockNoticeNo = request.getParameter("blockNoticeNo");
        //		if (request.getParameter("blockNoticeNo") != null) {
        //			blockNoticeNo = request.getParameter("blockNoticeNo");
        //        } else if (request.getAttribute("blockNoticeNo") != null) {
        //        	blockNoticeNo = (String) request.getAttribute("blockNoticeNo");
        //        }

        //logger.debug("blockNoticeNo============="+blockNoticeNo);
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setBlockNoticeNo(blockNoticeNo);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        //取得竞买人数
        List purchaserList = this.tdscBidderAppService.findPurchaserList();
        request.setAttribute("purchaserList", purchaserList);
        String selectType = request.getParameter("selectType");
        //String selectStage = "2";
        //String blockId ="3";
        String appId = tdscBlockAppView.getAppId();
        TdscBlockSelectApp tdscBlockSelectApp = new TdscBlockSelectApp();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得抽选的公证处
        List notatyList = this.tdscSelectService.acrossSelect(selectType, appId, purchaserList);
        request.setAttribute("notatyList", notatyList);
        //如果多次抽选,进行删除,只保存最后一次的抽选结果
        List spilthList = this.tdscSelectService.querySpilthList(nowTime, appId);
        if (spilthList != null) {
            for (int i = 0; i < spilthList.size(); i++) {
                tdscBlockSelectApp = (TdscBlockSelectApp) spilthList.get(i);
                this.tdscSelectService.deleteSpilthList(tdscBlockSelectApp);
            }
        }
        //保存抽选结果

        TdscNotaryInfo tdscNotaryInfo = new TdscNotaryInfo();

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        for (int i = 0; i < notatyList.size(); i++) {
            tdscNotaryInfo = (TdscNotaryInfo) notatyList.get(i);
            tdscBlockSelectApp.setSelectUser(user.getDisplayName());
            tdscBlockSelectApp.setSelectType(selectType);
            //tdscBlockSelectApp.setSelectStage(selectStage);
            tdscBlockSelectApp.setSelectDate(nowTime);
            tdscBlockSelectApp.setSelectedId(tdscNotaryInfo.getNotaryId());
            this.tdscSelectService.saveBlockSelectApp(tdscBlockSelectApp);
        }
        return mapping.findForward("notaryList");

    }

    /**
     * 公证处抽选地块信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward queryBlockInfoToNotary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String blockNoticeNo = request.getParameter("blockNoticeNo");

        String selectType = request.getParameter("selectType");
        request.setAttribute("selectType", selectType);
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setBlockNoticeNo(blockNoticeNo);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        List purchaserList = this.tdscBidderAppService.findPurchaserList();
        request.setAttribute("purchaserList", purchaserList);
        return mapping.findForward("notaryList");
    }

    /**
     * 专家/主持人抽选地块信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward queryBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String selectType = request.getParameter("selectType");
        request.setAttribute("selectType", selectType);
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        //List purchaserList = this.tdscBidderAppService.findPurchaserList();
        List purchaserList=tdscBidderAppService.findPurchaserListByAppId(appId);
        request.setAttribute("purchaserList", purchaserList);
        return mapping.findForward("selectNotary");
    }

    /**
     * 审查专家抽选地块信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward queryBlockInfoToSpecialist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        return mapping.findForward("list");
    }

    /**
     * 主持人抽选
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward queryBlockInfoToCompere(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forwardType = "1";
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        String selectType = request.getParameter("selectType");
        TdscBlockSelectApp tdscBlockSelectApp = new TdscBlockSelectApp();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());

        // 取得抽选的主持人
        List compereList = this.tdscSelectService.compereSelect(selectType, appId);
        request.setAttribute("compereList", compereList);
        request.setAttribute("forwardType", forwardType);

        return mapping.findForward("compereList");
    }

    /**
     * 保存主持人抽选信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward saveCompereInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        request.setAttribute("condition", condition);
        String appId = request.getParameter("appId");
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        String selectType = request.getParameter("selectType");
        String compereId = request.getParameter("compereId");
        request.setAttribute("compereId", compereId);
        String blockNoticeNo = tdscBlockAppView.getBlockNoticeNo();
        String sub = blockNoticeNo + selectType + compereId;
        String transferMode = tdscBlockAppView.getTransferMode();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());

        TdscBlockPlanTable tdscBlockPlanTable = this.tdscSelectService.query(appId);
        //保存抽选结果
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        TdscBlockSelectApp tempSelectApp = new TdscBlockSelectApp();
        tempSelectApp.setAppId(appId);
        tempSelectApp.setSelectUser(user.getDisplayName());
        tempSelectApp.setSelectDate(nowTime);
        tempSelectApp.setSelectType(selectType);
        tempSelectApp.setSelectedId(compereId);
        tempSelectApp.setSelectNum(sub);
        tempSelectApp.setIsValid(GlobalConstants.SELECT_RESULT_VALID);
        if ("3107".equals(transferMode)) {
            tempSelectApp.setActivityDate(tdscBlockPlanTable.getTenderStartDate());
            tempSelectApp.setActivityLoc(tdscBlockPlanTable.getOpeningLoc());
            tempSelectApp.setUnlockTime(tdscBlockPlanTable.getTenderStartDate());
        } else if ("3103".equals(transferMode)) {
            tempSelectApp.setActivityDate(tdscBlockPlanTable.getAuctionDate());
            tempSelectApp.setActivityLoc(tdscBlockPlanTable.getAuctionLoc());
            tempSelectApp.setUnlockTime(tdscBlockPlanTable.getAuctionDate());
        } else if ("3104".equals(transferMode)) {
            tempSelectApp.setActivityDate(tdscBlockPlanTable.getSceBidDate());
            tempSelectApp.setActivityLoc(tdscBlockPlanTable.getSceBidLoc());
            tempSelectApp.setUnlockTime(tdscBlockPlanTable.getSceBidDate());
        }

        this.tdscSelectService.saveBlockSelectApp(tempSelectApp);

        tdscSelectService.updatestate(appId, "1");
        return mapping.findForward("success");
    }

    /**
     * 进入主持人抽选页面
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */

    public ActionForward selectCompere(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String selectType = request.getParameter("selectType");
        request.setAttribute("selectType", selectType);
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        return mapping.findForward("selectCompere");
    }

    /**
     * 查看主持人信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward checkCompereInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        List compereIdList = tdscSelectService.queryselectedId(appId);
        tdscBlockAppView.setTempStr("compereId");
        String selectType = request.getParameter("selectType");

        TdscBlockPlanTable tdscBlockPlanTable = this.tdscSelectService.query(appId);
        request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
        List compereList = new ArrayList();
        if(compereIdList!=null){
            for(int i=0;i<compereIdList.size();i++){
                TdscCompereInfo tdscCompereInfo = tdscSelectService.queryCompereInfoByCompereId((String)compereIdList.get(i));
                if(tdscCompereInfo!=null)
                    compereList.add(tdscCompereInfo);
            }
        }
        request.setAttribute("compereList", compereList);

        return mapping.findForward("checkCompereInfo");
    }

    /**
     * 为专家和到帐审核抽选公证处
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward selectNotaryToSpecialist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forwardType = "1";
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        request.setAttribute("forwardType", forwardType);
        String selectType = request.getParameter("selectType");
        TdscBlockSelectApp tdscBlockSelectApp = new TdscBlockSelectApp();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得抽选的公证处
        List notatyList = this.tdscSelectService.acrossSelectToSpecialist(appId);
        request.setAttribute("notatyList", notatyList);
        List purchaserList = tdscBidderAppService.findPurchaserListByAppId(appId);
        request.setAttribute("purchaserList", purchaserList);

        return mapping.findForward("selectNotary");
    }

    /**
     * 保存B类/C类公证处信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward saveNotaryInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        request.setAttribute("condition", condition);
        String appId = request.getParameter("appId");
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        String notaryId = request.getParameter("notaryId");
        TdscNotaryInfo tdscNotaryInfo = this.tdscSelectService.queryNotaryInfoBynotaryId(notaryId);
        tdscBlockAppView.setTempStr("notaryId");
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        
        String transferMode = tdscBlockAppView.getTransferMode();
        String selectType = request.getParameter("selectType");
       
        
        String blockNoticeNo = tdscBlockAppView.getBlockNoticeNo();
        String sub = blockNoticeNo + selectType + notaryId;
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());

        TdscBlockPlanTable tdscBlockPlanTable = this.tdscSelectService.query(appId);
        if(null==tdscBlockPlanTable.getBlockName()){
        	tdscBlockPlanTable.setBlockName(tdscBlockAppView.getBlockName());
        }
        //保存抽选结果
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        TdscBlockSelectApp tempSelectApp = new TdscBlockSelectApp();
        tempSelectApp.setAppId(appId);
        tempSelectApp.setSelectUser(user.getDisplayName());
        tempSelectApp.setSelectDate(nowTime);
        tempSelectApp.setSelectType(selectType);
        tempSelectApp.setSelectedId(notaryId);
        tempSelectApp.setSelectNum(sub);
        tempSelectApp.setIsValid(GlobalConstants.SELECT_RESULT_VALID);
        tempSelectApp.setNodeStat("00");
        String replyTime = tdscSelectService.setReplyDeadLine();
        tempSelectApp.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
        if ("3103".equals(transferMode)) {
            Timestamp temp = tdscBlockPlanTable.getAuctionDate();
            int hours = temp.getHours();
            temp.setHours(hours - 2);
            tempSelectApp.setActivityDate(temp);
            tempSelectApp.setActivityLoc("南泉北路房地大厦");
            tempSelectApp.setUnlockTime(temp);
        } else if ("3104".equals(transferMode)) {
        	
        	   Timestamp temp = new Timestamp(System.currentTimeMillis());
	            if(null!=temp){
		            int date = temp.getDate()+2;
		            temp.setDate(date);
		            temp.setHours(9);
		            temp.setMinutes(00);
		            temp.setSeconds(00);
	        	}
	            tempSelectApp.setActivityDate(temp);
	            tempSelectApp.setActivityLoc("南泉北路房地大厦");
	            tempSelectApp.setUnlockTime(temp);
	            this.tdscSelectService.saveBlockSelectApp(tempSelectApp);
	            if (tdscNotaryInfo!= null) {
	                String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable, tempSelectApp);
                    request.setAttribute("msg", returnMsg);
	            }

        } else if ("3107".equals(transferMode)) {
            Timestamp temp = tdscBlockPlanTable.getTenderStartDate();
            int hours = temp.getHours();
            temp.setHours(hours - 1);
            tempSelectApp.setActivityDate(temp);
            tempSelectApp.setActivityLoc("南泉北路房地大厦");
            tempSelectApp.setUnlockTime(temp);
        }
        this.tdscSelectService.saveBlockSelectApp(tempSelectApp);
        
        
        if ("3107".equals(transferMode)) {
            tdscSelectService.updatestate(appId, "3");
        } else {
            tdscSelectService.updatestate(appId, "2");
        }
        request.setAttribute("notaryId", notaryId);
        return mapping.findForward("success");
    }

    /**
     * 查看公证处信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward checkNotaryInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String appId = request.getParameter("appId");
        String notaryId = request.getParameter("notaryId");
        request.setAttribute("notaryId", notaryId);
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        //List notaryIdList = tdscSelectService.queryselectedId(appId);
        tdscBlockAppView.setTempStr("notaryId");
        String selectType = request.getParameter("selectType");
        
        TdscBlockPlanTable tdscBlockPlanTable = this.tdscSelectService.query(appId);
        request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
        List notatyList = new ArrayList();
        //if(notaryIdList!=null){
           // for(int i=0;i<notaryIdList.size();i++){
        TdscNotaryInfo tdscNotaryInfo = tdscSelectService.queryNotaryInfoBynotaryId(notaryId);
        if(null!=tdscNotaryInfo ){
        	notatyList.add(tdscNotaryInfo);
        }
           // }
       // }
       

        List purchaserList=tdscBidderAppService.findPurchaserListByAppId(appId);
        request.setAttribute("purchaserList", purchaserList);
        request.setAttribute("notatyList", notatyList);
        return mapping.findForward("checkNotaryInfo");
    }

    public ActionForward replyManage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String appId = request.getParameter("appId");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        List specailistList = tdscSelectService.getSpecailistInfoList(appId);
        request.setAttribute("specailistList", specailistList);

        if ("1".equals(request.getParameter("forwardType")))
            return mapping.findForward("addSpecailist");
        return mapping.findForward("specialistManage");
    }

    public ActionForward SpecailistUpdateInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String selectId = request.getParameter("selectId");
        TdscBlockSelectApp tdscBlockSelectApp = tdscSelectService.getTdscBlockSelectAppBySelectId(selectId);
        request.setAttribute("tdscBlockSelectApp", tdscBlockSelectApp);
        return mapping.findForward("changeReplyStatus");
    }

    public void changeStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String selectId = request.getParameter("selectId");
        String replyStatus = request.getParameter("replyStatus");
        tdscSelectService.changeStatusBySelectId(selectId, replyStatus);
    }

    public ActionForward reSelectSpecailist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String appId = request.getParameter("appId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(appId);
        TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);

        //通过appId查出已抽选出来的专家列表
        List specailistList = tdscSelectService.getSpecailistInfoList(appId);
        
        List newSpecailist = tdscSelectService.reSelectSpecailist(appId, user.getDisplayName());
        request.setAttribute("newSpecailistList", newSpecailist);
        //修改累计抽选人数，将新抽出的人数加入到其中
        if(null!=newSpecailist&&newSpecailist.size()>0){
            List tempList=(List)newSpecailist.get(0);
            if(null!=tempList&&tempList.size()>0){
                int aa=Integer.parseInt((String)specailistList.get(4))+tempList.size();
                specailistList.set(4, String.valueOf(aa));
            }
            
        }
        request.setAttribute("specailistList", specailistList);

        return mapping.findForward("addSpecailistResult");
    }

}

