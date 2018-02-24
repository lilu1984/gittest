package com.wonders.tdsc.bidder.web;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderMaterialService;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBusinessRecordCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;

public class TdscBidderMaterialAction extends BaseAction {

    private TdscBidderMaterialService tdscBidderMaterialService;

    public void setTdscBidderMaterialService(TdscBidderMaterialService tdscBidderMaterialService) {
        this.tdscBidderMaterialService = tdscBidderMaterialService;
    }

    /**
     * 根据条件获得竞买土地列表
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        bindObject(condition, form);

        /** 如果是流程中的节点需要设置节点编号 */
        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setOrderKey("blockNoticeNo");
        // 获得用户信息
        UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // 查询列表
        request.setAttribute("pageList", tdscBidderMaterialService.findPageList(condition, user));
        request.setAttribute("condition", condition);

        return mapping.findForward("materialBlockList");
    }

    /**
     * 用iweboffice显示打印页面的信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bidderMaterialDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(request.getParameter("appId"));
        String printType = request.getParameter("printType");
        TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.detailTdscTranAppById(condition);
        //打印申请材料生成的Document文件夹路径
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        //判断该文件夹是否存在，不存在则新建；
        File dirname = new File(mFilePath);
        if(!dirname.exists()){
        	dirname.mkdir();
        }
        long start = System.currentTimeMillis();
        String tempStr = tdscBlockAppView.getTempStr();
        //生成条形码
        //tdscBidderMaterialService.creatBarImage(tempStr, mFilePath+"provide_"+start+".jpg");
        request.setAttribute("barName", "provide_"+start+".jpg");
        if ("1".equals(printType)){
            request.setAttribute("modeName", "");
            request.setAttribute("bidderType", "1");
            request.setAttribute("modeNameEn", "dandu.doc");
        }else{
            request.setAttribute("modeName", "");
            request.setAttribute("bidderType", "2");
            request.setAttribute("modeNameEn", "lianhe.doc");        	
        }
            
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        return mapping.findForward("print_iweboffice");      
    }
    /**
     * 用web显示打印页面
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bidderMaterialDetailForWeb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(request.getParameter("appId"));
		String printType = request.getParameter("printType");
		TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.detailTdscTranAppById(condition);
		
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		if ("1".equals(printType)){
			request.setAttribute("bidderType", "1");
			return mapping.findForward("print_ddjm");
		}else{
			request.setAttribute("bidderType", "2");
			return mapping.findForward("print_lhjm");
		}   
	}  
    

    /**
     * 打印确认
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    /*
     * public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception { //
     * 获取页面参数 TdscBidderCondition condition = new TdscBidderCondition(); bindObject(condition, form); // 设置页面行数 condition.setPageSize(((Integer)
     * DicPropertyUtil.getInstance().getPropertyValue( GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue()); // 查询列表
     * request.setAttribute("pageList", tdscBidderMaterialService.findPageList(condition)); request.setAttribute("condition", condition);
     * 
     * //取出登录用户ＩＤ SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO); String
     * uname=String.valueOf(user.getUserID());
     * 
     * tdscBidderMaterialService.updateTdscBusinessRecordById(request.getParameter("appId"),uname);
     * 
     * return mapping.findForward("materialBlockList"); }
     */
    /**
     * 更新窗口业务记录表，地块交易信息表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward updateMaterialCount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        String appId=(String)request.getParameter("appId");
        String mFileName=(String) request.getParameter("mFileName");
        condition.setAppId(appId);
        //查询TdscBlockAppView
        TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.getTdscTranAppById(condition);
        // 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        //判断该用户是否为“受理部总受理”
        List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
        Date nowDate = new Date();

        TdscBidderProvide tdscBidderProvide = new TdscBidderProvide();
        tdscBidderProvide.setAppId(tdscBlockAppView.getAppId());
        tdscBidderProvide.setUserId(user.getUserId());
        //设置 区县ID
        String districtId = user.getRegionId();
        tdscBidderProvide.setRegionId(new Integer(districtId));
        tdscBidderProvide.setBidType(request.getParameter("bidderType"));
        tdscBidderProvide.setProvideBm(request.getParameter("provideBm"));
        tdscBidderProvide.setProvideDate(nowDate);
        String userIdNum="0";
        String countNum="0";
        if(buttonList!=null){
        	tdscBidderMaterialService.updateFfcl(tdscBidderProvide);
        	//根据appId和userId查询该窗口受理的份数-----构造查询Condition
        	TdscBusinessRecordCondition busiRecCondition =new TdscBusinessRecordCondition ();
        	busiRecCondition.setAppId(appId);
        	busiRecCondition.setUserId(String.valueOf(user.getUserId()));
        	//返回查询结果
        	userIdNum=tdscBidderMaterialService.queryNumByUserId(busiRecCondition);
        	countNum=tdscBidderMaterialService.queryNumByAppIdId(busiRecCondition);
        }


        String resultText=tdscBlockAppView.getAppId()+","+userIdNum+","+countNum;
        
        // 将内容设置到输出
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        // 返回给回调函数的参数
        pw.write(resultText);
        pw.close();
        //删除本次打印所生成的条形码
        barImageDel(mFileName,request);
        return null;

    }
   
    /**
     * 删除本次打印所生成的条形码(jsp页面调用的方法)
     * @param request
     * @param response
     * @throws Exception
     */
    public void barImageDel(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        //打印申请材料生成的Document文件夹路径
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        String mFileName=(String) request.getParameter("mFileName");
        //构造File
        File file = new File(mFilePath+mFileName);
        //删除
        if (file.isFile()) {
        	try{
        		file.delete();
        	}catch(Exception e){
        		e.getMessage();
        	}
		}
    }
    /**
     * 删除本次打印所生成的条形码（Action中调用的方法）
     * @param request
     * @param response
     * @throws Exception
     */
    public void barImageDel(String mFileName,HttpServletRequest request) throws Exception {
        //打印申请材料生成的Document文件夹路径
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        //构造File
        File file = new File(mFilePath+mFileName);
        //删除
        if (file.isFile()) {
        	try{
        		file.delete();
        	}catch(Exception e){
        		e.getMessage();
        	}
		}
    }
}
