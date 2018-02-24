package com.wonders.tdsc.smartcardlogin.web;

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
import com.wonders.esframework.common.model.Pager;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.smartcardlogin.service.SmartCardLoginService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class SmartCardLoginAction extends BaseAction{
	
	private SmartCardLoginService smartCardLoginService;
	
	private CommonQueryService commonQueryService;
	
    private TdscBidderAppService tdscBidderAppService;
    
	public void setSmartCardLoginService(SmartCardLoginService smartCardLoginService) {
		this.smartCardLoginService = smartCardLoginService;
	}
	
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}
	/**
	 * 卡号传递
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		request.setAttribute("cardNo", request.getParameter("cardNo"));
		return mapping.findForward("showLogin");
	}

	/**
	 * 挂牌登陆
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//检验用户卡号和密码
		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		String checkMsg = "false";
		if(tdscBidderApp!=null){
			checkMsg="true";
		}
		request.setAttribute("checkMsg", checkMsg);
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		return mapping.findForward("showLogin");
	}
	
	/**
	 * 挂牌条件检验
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkListingInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		request.setAttribute("cardNo", cardNo);
		String password = request.getParameter("password");
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		//查询地块的交易状态
		String appId = tdscBidderApp.getAppId();
		TdscBlockTranApp tdscBlockTranApp =  this.smartCardLoginService.queryBlockTranAppInfoByAppId(appId);
		String blockId = tdscBlockTranApp.getBlockId();
		TdscBlockInfo tdscBlockInfo = this.smartCardLoginService.queryBlockInfoByBlockId(blockId);
		String status = tdscBlockInfo.getStatus();
		String isTran = "true";
		if(status.equals("02")||status.equals("03")||status.equals("04")){
			isTran = "false";//交易已结束
		}
		request.setAttribute("isTran", isTran);
		//查看是否入围"00"还没有审查"01"通过"02"没通过
		String reviewResult = tdscBidderApp.getReviewResult();	
		request.setAttribute("reviewResult", reviewResult);
		//查看挂牌开始时间和结束时间
		String isStart = this.smartCardLoginService.checkStartDate();
		request.setAttribute("isStart", isStart);
		String isEnd = this.smartCardLoginService.checkEndDate();
		request.setAttribute("isEnd", isEnd);
		return mapping.findForward("checkListing");
	}
	
	/**
	 * 卡号传递
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLoginToOp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		request.setAttribute("cardNo", request.getParameter("cardNo"));
		request.setAttribute("yktXh", request.getParameter("yktXh"));
		return mapping.findForward("indexoverpass");
	}
	/**
	 * 校验页面传入的卡号是否存在
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassLoginCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		String cardNo= request.getParameter("cardNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString="";
		String yktXh="";
		String count="";
		List tempList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		if(tempList.size()==0){
			count="0";
		}
		if(tempList.size()==1){
			count="1";
			//获得交易卡的编号
			TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.getBidderAppByYktBh(cardNo);
			if(tdscBidderApp!=null){
				 yktXh=tdscBidderApp.getYktXh()+",";
			}
		}
		if(tempList.size()>1){
			count="2";
			//获得交易卡的编号
			List tdscBidderAppList = (List)tdscBidderAppService.getBidderAppListByYktBh(cardNo);
			if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
				TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
				yktXh=tdscBidderApp.getYktXh()+",";
			}
		}
		
		retString=count+","+cardNo+","+yktXh;
		
/*		request.setAttribute("count",count);
		request.setAttribute("cardNo", cardNo);
		return mapping.findForward("overpass");*/
		pw.write(retString);
		pw.close();
		return null;
	}
	
	
	/**
	 * 入围密码检验
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassPasswordCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//检验用户卡号和密码
		String cardNo = request.getParameter("cardNo");
		request.setAttribute("cardNo", cardNo);
		String password = request.getParameter("password");
		request.setAttribute("password", password);
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		String checkMsg = "false";
		if(tdscBidderApp!=null){
			checkMsg="true";
		}
		request.setAttribute("checkMsg", checkMsg);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		return mapping.findForward("overpass");
	}
	
	/**
	 * 入围检验
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = request.getParameter("bidderId");
		
		TdscBidderCondition condition = new TdscBidderCondition();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		
		if(bidderId!=null&&!"".equals(bidderId)){
			condition.setBidderId(bidderId);
			//TdscBidderApp tdscBidderApp = smartCardLoginService..checkByAcceptNo(password);
			List tdscBidderAppList = smartCardLoginService.getBidderAppListByCondition(condition);
			if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
				tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
				if(tdscBidderApp!=null)
					request.setAttribute("appId", tdscBidderApp.getAppId());
					request.setAttribute("acceptNo", tdscBidderApp.getAcceptNo());
			}
			request.setAttribute("bidderId", bidderId);
		}
		//查看是否入围
		if(tdscBidderApp.getReviewResult()!=null&&tdscBidderApp.getReviewResult().equals(GlobalConstants.DIC_ID_REVIEW_RESULT_YES)){
			return mapping.findForward("overpassSuccess");
		}else{
			return mapping.findForward("overpassFailed");
		}
	}
	
	/**根据cardNo判断自助挂牌页面输入的一卡通卡号：1、该地块出让方式是不是挂牌
	 *                                      2、该地块是不是正在受理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCardNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		String retResult="00";
		List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		//判断数据库有没有该卡号；
		//没有时：
		if(cardNoList==null||cardNoList.size()==0){
			retResult = "01";
		}
        if(cardNoList.size()==1){
			
			//获得appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(0);
			//判断是否进行了机审
			if(tdscBidderApp.getReviewResult()==null){
				
				retResult = "88";
			}else{
				if(password!=null&&tdscBidderApp.getYktMm()!=null){
				   if(password.equals(tdscBidderApp.getYktMm())){
						String appId=(String)tdscBidderApp.getAppId();
						//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
						TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
						tdscBaseQueryCondition.setAppId(appId);
						TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
						//出让方式为“挂牌”时
					    if(GlobalConstants.DIC_TRANSFER_LISTING==tdscBlockAppView.getTransferMode()){
					    	retResult = "99";
					    }else{
					    	retResult = "02";
					    }
					   
				   }else{
					        retResult = "77";
				   }
				}
			}
		}
		//有但不唯一时，查询出该卡对应的所有地块中正在受理的地块信息
		if(cardNoList.size()>1){
		   for(int i=0;i<cardNoList.size();i++){
			   TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(i); 
			   String appId=(String)tdscBidderApp.getAppId();
				//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				//TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//未完。。。
		   }
		   retResult="03";
		} 
        // 返回给回调函数的参数
        //String rowId = bidderPersonId;
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(password);
        pw.close();

        return null;
	}
	
	/**
	 * 根据cardNo判断密码修改页面输入的一卡通卡号是否存在   lz+  090724
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCardNoAndPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		String cardNo = request.getParameter("cardNo");//一卡通序号
		String password = request.getParameter("password");
		String retResult="00";
		String yktBh = "";
		if(bidderId!=null&&!"".equals(bidderId)){
		TdscBidderApp bidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
		if(bidderApp!=null&&cardNo.equals(bidderApp.getYktXh()))
			yktBh = bidderApp.getYktBh();
		
		List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(yktBh);
		//判断数据库有没有该卡号；
		//没有时：
		if(cardNoList==null||cardNoList.size()==0){
			retResult = "01";
		}
        if(cardNoList.size()==1){
			//获得appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(0);
			
				if(password!=null&&tdscBidderApp.getYktMm()!=null){
				   if(password.equals(tdscBidderApp.getYktMm())){
					   retResult = "02";//通过验证
				   }else{
					   retResult = "77";//密码错误
				   }
				}
			}
		
		//有但不唯一时，查询出该卡对应的所有地块中正在受理的地块信息
		if(cardNoList.size()>1){
		   retResult="03";
		}
		
		}
        // 返回给回调函数的参数
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(password);
        pw.close();

        return null;
	}
	
	/**
	 * 返回到初始页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toHuiTui(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
		return mapping.findForward("indexht");
	}
	/**
	 * 查询持卡人信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String bidderId =(String)request.getParameter("bidderId");
		
		//根据bidderId(交易卡卡号和密码)查询持卡人
		if(bidderId!=null&&!"".equals(bidderId)){
			TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			List bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
			TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
			//获得竞买人的List
			if(tdscBidderApp!=null){
			   String appId = (String)tdscBidderApp.getAppId();
			   //根据appId通过通用查询，查出该appId对应的TdscBlockAppView
			   TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			   tdscBaseQueryCondition.setAppId(appId);
			   tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
			}
			
			request.setAttribute("bidlist", bidlist);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			
			return mapping.findForward("infoindex");
		}
		
		return null;
		
	}
	
	/**
	 * 查询持卡人信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String cardNo =(String)request.getParameter("cardNo");
		String password =(String)request.getParameter("password");
		//根据 cardNo，password(交易卡卡号和密码)查询持卡人
		List tdscBidderAppList = (List)smartCardLoginService.getBidderAppListByCard(cardNo,password);
		List  bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView=new TdscBlockAppView();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		//获得竞买人的List
		if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
			tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
			String bidderId = (String)tdscBidderApp.getBidderId();
			bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String)tdscBidderApp.getAppId();
			//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		
		return mapping.findForward("infoindex");
	}
	
	/**
	 * 查询持卡人竞价地块信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String cardNo =(String)request.getParameter("cardNo");
		String password =(String)request.getParameter("password");
		//根据 cardNo，password(交易卡卡号和密码)查询持卡人列表
		//TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkSmartCard(cardNo,password);
		List tdscBidderAppList = smartCardLoginService.getBidderAppListByCard(cardNo, password);
		List returnList = new ArrayList();
		if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
			for(int i=0;i<tdscBidderAppList.size();i++){
				TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(i);
				if(tdscBidderApp!=null&&tdscBidderApp.getAppId()!=null){
					//根据appId通过通用查询，查出该appId对应的在交易中的TdscBlockAppView
					TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
					tdscBaseQueryCondition.setAppId(tdscBidderApp.getAppId());
					tdscBaseQueryCondition.setStatus("01");//交易中
					List tdscBlockAppViewList = (List)commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);
					if(tdscBlockAppViewList!=null&&tdscBlockAppViewList.size()==1){
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView)tdscBlockAppViewList.get(0);
						tdscBlockAppView.setBzBeizhu(tdscBidderApp.getBidderId());//将bidderId存入BzBeizhu字段中
						returnList.add(tdscBlockAppView);
					}
				}
			}
		}
		
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("password", password);
		request.setAttribute("tdscBlockAppViewList", returnList);
		
		return mapping.findForward("showBlockList");
	}
	
//*////////////////////////////////////////////////////*/	
	
	/**根据cardNo判断自助挂牌页面输入的一卡通卡号：1、该地块出让方式是不是挂牌
	 *                                      2、该地块是不是正在受理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkAcceptNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String acceptNo = request.getParameter("password");
		String retResult="00";
		List acceptNoList=(List)tdscBidderAppService.checkIfUsedAcceptNo(acceptNo);
		//判断数据库有没有该卡号；
		//没有时：
		if(acceptNoList==null||acceptNoList.size()==0){
			retResult = "01";
		}
        if(acceptNoList.size()==1){
			//获得appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)acceptNoList.get(0);
			//判断是否进行了机审
			if(tdscBidderApp.getReviewResult()==null){
				retResult = "88";
			}else{
				String appId=(String)tdscBidderApp.getAppId();
				//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//出让方式为“挂牌”时
			    if(GlobalConstants.DIC_TRANSFER_LISTING==tdscBlockAppView.getTransferMode()){
			    	retResult = "99";
			    }else{
			    	retResult = "02";
			    }
			}
		}
		//有但不唯一时，查询出该卡对应的所有地块中正在受理的地块信息
		if(acceptNoList.size()>1){
		   for(int i=0;i<acceptNoList.size();i++){
			   TdscBidderApp tdscBidderApp = (TdscBidderApp)acceptNoList.get(i); 
			   String appId=(String)tdscBidderApp.getAppId();
				//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				//TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//未完。。。
		   }
		   retResult="03";
		} 
        // 返回给回调函数的参数
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(acceptNo);
        pw.close();

        return null;
	}
	
	/**
	 * 查询持卡人信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidByAcceptNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String acceptNo =(String)request.getParameter("password");
		//根据受理编号acceptNo查询持卡人
		TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkByAcceptNo(acceptNo);
		List  bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView=new TdscBlockAppView();
		//获得竞买人的List
		if(tdscBidderApp!=null){
		   String bidderId = (String)tdscBidderApp.getBidderId();
		   bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
		   String appId = (String)tdscBidderApp.getAppId();
			//根据appId通过通用查询，查出该appId对应的TdscBlockAppView
		   TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		   tdscBaseQueryCondition.setAppId(appId);
		   tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		
		return mapping.findForward("infoindex");
	}
	
	
	/**
     * 获得所有一卡通列表
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryYkt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
    	TdscBidderCondition condition = new TdscBidderCondition();
    	TdscBidderForm tdscBidderForm = (TdscBidderForm)form;
        bindObject(condition, tdscBidderForm);
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
        if(StringUtils.isNotEmpty(condition.getBidderName()) ){
        	List bidderIdList = tdscBidderAppService.getBidderIdListByBidderName(condition.getBidderName());
        	if(bidderIdList != null && bidderIdList.size() > 0){
        		condition.setBidderIdList(bidderIdList);
        		//根据竞买人名称查询时，如果有LIST集合则查询，否则返回空的PageList
        	}else{
                PageList pageList = new PageList();
                request.setAttribute("pageList", pageList);
                request.setAttribute("condition", condition);
                
                return mapping.findForward("bidderYktList");
        	}
        }
        // 获得用户信息
		//SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        //String appUserId=String.valueOf(user.getUserId());
        
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = (String) request.getAttribute("currentPage");
        }

        int cPage = 0;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        
        /* 设置页面行数 */
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setCurrentPage(cPage);
        
        // 查询列表
//        PageList pageList = (PageList) tdscBidderAppService.findAllYktPageList(condition);
//        if(pageList != null){
//        	List bidderList = (List)pageList.getList();
//        	if(bidderList != null && bidderList.size() > 0){
//        		for(int i = 0; i< bidderList.size(); i++){
//        			TdscBidderApp tdscBidderApp = (TdscBidderApp)bidderList.get(i);
//        			TdscBidderPersonApp tempApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tdscBidderApp.getBidderId());
//        			if(StringUtils.isNotEmpty(tempApp.getMemo())){
//        				tdscBidderApp.setMemo(tempApp.getMemo());
//        			}
//        		}
//        	}
//        }
        List bidderIdList = tdscBidderAppService.tidyBidderAppList(condition);
        List currentAppList = new ArrayList();
        
        int listSize = 0;
        if(bidderIdList != null && bidderIdList.size() > 0){
        	listSize =  bidderIdList.size();
        }
        PageList pageList = new PageList();
        Pager _pager=new Pager(listSize, condition.getPageSize(), condition.getCurrentPage());
        pageList.setPager(_pager);
        
		for(int h = 0; h < _pager.getPageSize(); h++){
			if(bidderIdList != null && bidderIdList.size() > 0){
				if(_pager.getStartRow() + h >= bidderIdList.size())break;
				currentAppList.add(bidderIdList.get(_pager.getStartRow()+h));//当前页应该显示数据加到当前显示列表里，返回页面
			}
		}
        
        pageList.setList(currentAppList);

        
        request.setAttribute("pageList", pageList);
        request.setAttribute("condition", condition);
        return mapping.findForward("bidderYktList");
    }
	
    /**
     * 点击结果列表中的“土地交易卡卡号”，弹出新页面，显示持卡人信息
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toViewDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String bidderId = (String)request.getParameter("bidderId");
    	//竞买信息
        TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
        if(tdscBidderApp != null){
        	if(StringUtils.isNotEmpty(tdscBidderApp.getAppId())){
        		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        		condition.setAppId(tdscBidderApp.getAppId());
        		TdscBlockAppView appView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);
        		request.setAttribute("appView", appView);
        	}
        	
        }
        //竞买人列表
        List tdscBidderPersonAppList=tdscBidderAppService.queryBidderPersonList(bidderId);
        //设置返回值
        
        request.setAttribute("tdscBidderPersonAppList", tdscBidderPersonAppList);
        request.setAttribute("tdscBidderApp", tdscBidderApp);
        return mapping.findForward("bidderAppDetails");
    }
   
	/**
	 * 
	 */
    public ActionForward chooseType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
    	String bidderId =(String)request.getParameter("bidderId");
		String type =(String)request.getParameter("type");
		
		if(bidderId!=null){
			TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			if(tdscBidderApp!=null&&!"".equals(tdscBidderApp.getYktXh())){
				request.setAttribute("cardNo", tdscBidderApp.getYktXh());
				request.setAttribute("tdscBidderApp", tdscBidderApp);
			}
			request.setAttribute("bidderId", bidderId);
		}
		
		if(type!=null){
			if("xiugai".equals(type))
				return mapping.findForward("toOldPassword");
			else if("chongzhi".equals(type))
				return mapping.findForward("toNewPassword");
			else
				return mapping.findForward("toNewYkt");
		}
		
		return null;
	}
    
    /**
	 * 密码修改进入旧密码输入页面
	 */
	public ActionForward toOldPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String cardNo =(String)request.getParameter("cardNo");
		
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		
		return mapping.findForward("toOldPassword");
	}
	
	/**
	 * 密码修改进入新密码输入页面
	 */
	public ActionForward toNewPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String cardNo =(String)request.getParameter("cardNo");
		String bidderId =(String)request.getParameter("bidderId");
		
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		if(bidderId!=null&&!"".equals(bidderId))
			request.setAttribute("bidderId", bidderId);
		
		return mapping.findForward("toNewPassword");
	}
	
	
	/**
	 * 密码2次确认
	 */
	public ActionForward checkPasswordTwice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String newPassword =(String)request.getParameter("newPassword");
		String cardNo =(String)request.getParameter("cardNo");
		String bidderId =(String)request.getParameter("bidderId");
		
		//判断数据库有没有该卡号
		//List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		
		if(newPassword!=null&&!"".equals(newPassword))
			request.setAttribute("newPassword", newPassword);
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		if(bidderId!=null&&!"".equals(bidderId))
			request.setAttribute("bidderId", bidderId);
		
		return mapping.findForward("toNewPassword2");
	}
	
	
	
	/**
	 * 密码2次确认，进行密码修改
	 */
	public ActionForward changePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String newPassword2 = (String)request.getParameter("newPassword2");
		String bidderId =(String)request.getParameter("bidderId");
		
		TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			
		if(tdscBidderApp!=null&&tdscBidderApp.getYktBh()!=null&&tdscBidderApp.getYktMm()!=null){
			tdscBidderApp.setYktMm(newPassword2);
			tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
		}
		
		request.setAttribute("saveMessage", "保存成功!");
		return mapping.findForward("toNewPassword2");
	}
	
	
	
	/**
	 * 一卡通挂失，新卡录入
	 */
	public ActionForward changeYktAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //接受页面传入的值
		String bidderId =(String)request.getParameter("bidderId");
		String yktXh =(String)request.getParameter("yktXh");
		String yktBh =(String)request.getParameter("yktBh");
		String yktMm =(String)request.getParameter("yktMm");
		
		if(bidderId!=null&&!"".equals(bidderId)){
			TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
				
			if(tdscBidderApp!=null&&tdscBidderApp.getYktBh()!=null&&tdscBidderApp.getYktMm()!=null){
				tdscBidderApp.setYktBh(yktBh);
				tdscBidderApp.setYktXh(yktXh);
				tdscBidderApp.setYktMm(yktMm);
				tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			}
		}
		
		request.setAttribute("saveMessage", "保存成功!");
		return queryYkt(mapping, form, request, response);
	}
}