package com.wonders.tdsc.kyq.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.form.MiningTranAppForm;
import com.wonders.tdsc.kyq.service.KyqContent;
import com.wonders.tdsc.kyq.service.KyqNoticeService;
import com.wonders.tdsc.kyq.service.MiningTranAppService;

public class KyqNoticeAction extends BaseAction {

	private KyqNoticeService		kyqNoticeService;

	private MiningTranAppService	miningTranAppService;
	
	private TdscFileService         tdscFileService;

	public void setKyqNoticeService(KyqNoticeService kyqNoticeService) {
		this.kyqNoticeService = kyqNoticeService;
	}

	public void setMiningTranAppService(MiningTranAppService miningTranAppService) {
		this.miningTranAppService = miningTranAppService;
	}
	
	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	/**
	 * 点击新增出让公告，列出所有已经制定的实施方案
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toNewKyqNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List allPlanList = kyqNoticeService.queryKyqAllPlanList();

		HashMap map = new HashMap();
		if (allPlanList != null && allPlanList.size() > 0) {
			for (int i = 0; i < allPlanList.size(); i++) {
				KyqNotice notice = (KyqNotice) allPlanList.get(i);
				String miningNames = kyqNoticeService.appenMiningNames(notice.getNoticeId());
				map.put(notice.getNoticeId(), miningNames);
			}
		}		
		request.setAttribute("map", map);
		request.setAttribute("kyqAllPlanList", allPlanList);

		return mapping.findForward("kyqSelectNotice");
	}
	
	/**
	 * 查询已经制定好的,待发布的出让公告
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryMakedNoticeList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();

		MiningTranAppForm miningTranAppForm = (MiningTranAppForm) form;
		List tranStatusList = new ArrayList();
		tranStatusList.add(KyqContent.KYQ_STATUS_MAKED_NOTICE);
		tranStatusList.add(KyqContent.KYQ_STATUS_PUBLISHED_NOTICE);
		condition.setTranStatusList(tranStatusList);

		this.bindObject(condition, miningTranAppForm);

		if (StringUtils.isNotBlank(condition.getNoticeNumber())) {
			condition.setNoticeNumber(StringUtil.GBKtoISO88591(condition.getNoticeNumber()));
		}

		List noticeList = kyqNoticeService.queryMakedNoticeList(condition);

		HashMap map = new HashMap();
		if (noticeList != null && noticeList.size() > 0) {
			for (int i = 0; i < noticeList.size(); i++) {
				KyqNotice notice = (KyqNotice) noticeList.get(i);
				String miningNames = kyqNoticeService.appenMiningNames(notice.getNoticeId());
				map.put(notice.getNoticeId(), miningNames);
			}
		}

		request.setAttribute("condition", condition);
		request.setAttribute("map", map);
		request.setAttribute("noticeList", noticeList);

		return mapping.findForward("kyqMakeNoticeList");
	}

	public ActionForward queryNoticeList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();

		List noticeList = kyqNoticeService.queryNoticeList(condition);

		HashMap map = new HashMap();
		if (noticeList != null && noticeList.size() > 0) {
			for (int i = 0; i < noticeList.size(); i++) {
				KyqNotice notice = (KyqNotice) noticeList.get(i);
				String miningNames = kyqNoticeService.appenMiningNames(notice.getNoticeId());
				map.put(notice.getNoticeId(), miningNames);
			}
		}

		request.setAttribute("condition", condition);
		request.setAttribute("map", map);
		request.setAttribute("noticeList", noticeList);

		return mapping.findForward("noticeList");

	}

	public ActionForward queryPlanList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();
		
		condition.setTranStatus(request.getParameter("tranStatus"));
		condition.setOrderKey("miningName");

		List planList = miningTranAppService.queryMiningTranAppList(condition);

		request.setAttribute("condition", condition);

		request.setAttribute("planList", planList);

		return mapping.findForward("planList");

	}

	public ActionForward savaPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String status = request.getParameter("status");
		
		KyqNotice notice = new KyqNotice();		
		MiningTranAppForm tranAppForm = (MiningTranAppForm) form;
		this.bindObject(notice, tranAppForm);
		
		if(StringUtils.isNotBlank(notice.getNoticeId())){
			notice.setStatus(status);
		}

		String[] tranAppIds = request.getParameterValues("appIds");

		kyqNoticeService.saveNotice(notice, tranAppIds);

		return new ActionForward("kyqNotices.do?method=queryNoticeList", true);
	}

	public ActionForward toNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");

		String isEmpty = "false";

		KyqNotice notice = kyqNoticeService.getNoticeByNoticeId(noticeId);

		List miningList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);

		request.setAttribute("notice", notice);

		request.setAttribute("isEmpty", isEmpty);

		request.setAttribute("miningList", miningList);

		return mapping.findForward("tonotice");
	}

	public ActionForward toPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MiningTranAppForm tranform = (MiningTranAppForm) form;
		String[] appIds = tranform.getAppIds();

		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();		
		condition.setTranAppIds(Arrays.asList(appIds));
		
		List miningList = new ArrayList();
		miningList = miningTranAppService.queryMiningTranAppList(condition);

		request.setAttribute("miningList", miningList);

		return mapping.findForward("toplan");
	}

	public ActionForward toResetPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return mapping.findForward("toresetplan");
	}
		
	public ActionForward newKqFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String recordId="";
		String noticeId = request.getParameter("noticeId");
		String noticeNumber = request.getParameter("noticeNumber");
		String opType = request.getParameter("opType");
		if(StringUtils.isEmpty(opType)){
			opType = "";
		}
		KyqNotice kyqNotice = kyqNoticeService.getNoticeByNoticeId(noticeId);
		String miningNames = kyqNoticeService.appenMiningNames(kyqNotice.getNoticeId());
		
		if(StringUtils.isEmpty(kyqNotice.getRecordId())){
			recordId = new Date().getTime()+"";
		}else {
			recordId = kyqNotice.getRecordId();
		}
		
		//目前宜兴是一个公告一个矿山，是一对一的关系
		List miningList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);
		if(miningList!=null && miningList.size()>0){
			MiningTranApp miningTranApp = (MiningTranApp)miningList.get(0);		
			request.setAttribute("miningTranApp", miningTranApp);
		}
		request.setAttribute("opType", opType);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("recordId", recordId);
		request.setAttribute("noticeNumber", noticeNumber);
		request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("miningNames", miningNames);
		request.setAttribute("modeNameEn", "print_kq_word.doc");
		return mapping.findForward("printKqNotice");
	}
	
	public ActionForward saveOrPublishKqFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取当前用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		
		String noticeId = request.getParameter("noticeId");
		String noticeNumber = request.getParameter("noticeNumber");
		String modeNameEn = request.getParameter("modeNameEn");
		String recordId = request.getParameter("recordId");
		String submitType = request.getParameter("submitType");
		
		KyqNotice kyqNotice = kyqNoticeService.getNoticeByNoticeId(noticeId);		
		kyqNotice.setRecordId(recordId);		
		kyqNotice.setNoticeNumber(noticeNumber);
		if("save".equals(submitType)){
			kyqNotice.setStatus(KyqContent.KYQ_STATUS_MAKED_NOTICE);
		}else if("publish".equals(submitType)){
			kyqNotice.setStatus(KyqContent.KYQ_STATUS_PUBLISHED_NOTICE);
			kyqNotice.setIfReleased("1");//“1”为出让公告已经发布
		}
		kyqNoticeService.saveNoticeByObj(kyqNotice);
		
		String miningNames = kyqNoticeService.appenMiningNames(kyqNotice.getNoticeId());
		//目前宜兴是一个公告一个矿山，是一对一的关系
		List miningList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);
		if(miningList!=null && miningList.size()>0){
			MiningTranApp miningTranApp = (MiningTranApp)miningList.get(0);		
			request.setAttribute("miningTranApp", miningTranApp);
		}
		
		TdscBlockFileApp tdscBlockFileOld = null;
		if (kyqNotice != null) {
			tdscBlockFileOld = tdscFileService.getBlockFileAppByRecordId(kyqNotice.getRecordId());
			
			if (tdscBlockFileOld != null) {
				tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
				tdscFileService.update(tdscBlockFileOld);
			} else {
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				tdscBlockFileApp.setRecordId(recordId);
				tdscBlockFileApp.setFileId(noticeId);
				tdscBlockFileApp.setFilePerson(user.getUserId());
				tdscBlockFileApp.setFileUrl(modeNameEn);// 将文件类型 保存在附件地址中
				tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
				tdscFileService.save(tdscBlockFileApp);
			}
		}
		
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("recordId", recordId);
		request.setAttribute("noticeNumber", noticeNumber);
		request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("miningNames", miningNames);
		request.setAttribute("modeNameEn", "print_kq_word.doc");

		return mapping.findForward("printKqNotice");
	}
	
	public ActionForward modifyKqFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		KyqNotice kyqNotice = kyqNoticeService.getNoticeByNoticeId(noticeId);
        if(kyqNotice != null){
        	String recordId = kyqNotice.getRecordId();
        	if(StringUtils.isNotBlank(recordId)){
        		request.setAttribute("recordId", recordId);
        	}
        	request.setAttribute("noticeNumber", kyqNotice.getNoticeNumber());
        }
        
        String miningNames = kyqNoticeService.appenMiningNames(kyqNotice.getNoticeId());
		//目前宜兴是一个公告一个矿山，是一对一的关系
		List miningList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);
		if(miningList!=null && miningList.size()>0){
			MiningTranApp miningTranApp = (MiningTranApp)miningList.get(0);		
			request.setAttribute("miningTranApp", miningTranApp);
		}
		
        request.setAttribute("noticeId", noticeId);
        request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("miningNames", miningNames);
        
        return mapping.findForward("printKqNotice");
	}
	
	
	public ActionForward queryResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();
		List kyqNoticeList = kyqNoticeService.queryNoticeList(condition);
		
        HashMap map = new HashMap();
		if (kyqNoticeList != null && kyqNoticeList.size() > 0) {
			for (int i = 0; i < kyqNoticeList.size(); i++) {
				KyqNotice notice = (KyqNotice) kyqNoticeList.get(i);
				String miningNames = kyqNoticeService.appenMiningNames(notice.getNoticeId());
				map.put(notice.getNoticeId(), miningNames);
			}
		}
        
        request.setAttribute("kyqNoticeList", kyqNoticeList);
        request.setAttribute("map", map);
        
        return mapping.findForward("kqResultNoticeList");
	}
	
	public ActionForward publishResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String resultRecordId="";
		String noticeId = request.getParameter("noticeId");
		String opType = request.getParameter("opType");
		if(StringUtils.isEmpty(opType)){
			opType = "";
		}
		KyqNotice kyqNotice = kyqNoticeService.getNoticeByNoticeId(noticeId);
		
		if(kyqNotice != null){
			List miningTranAppList = kyqNoticeService.queryMiningTranAppListByNoticeId(kyqNotice.getNoticeId());
			if(miningTranAppList!=null && miningTranAppList.size()>0){
				MiningTranApp miningTranApp = (MiningTranApp)miningTranAppList.get(0);
				request.setAttribute("miningTranApp", miningTranApp);
			}
		}
				
		if(StringUtils.isEmpty(kyqNotice.getResultRecordId())){
			resultRecordId = new Date().getTime()+"";
		}else {
			resultRecordId = kyqNotice.getResultRecordId();
		}
		request.setAttribute("opType", opType);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("recordId", resultRecordId);
		request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("modeNameEn", "print_kq_result_word.doc");
		return mapping.findForward("printKqResultNotice");
	}
	
	public ActionForward saveOrPublishKqResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");
		String submitType = request.getParameter("submitType");
		
		KyqNotice kyqNotice = kyqNoticeService.getNoticeByNoticeId(noticeId);		
		kyqNotice.setResultRecordId(recordId);
		kyqNotice.setStatus(KyqContent.KYQ_STATUS_PUBLISHED_RESULT_NOTICE);
		if("save".equals(submitType)){
			kyqNotice.setIfResultPublish("0");
		}else if("publish".equals(submitType)){
			kyqNotice.setIfResultPublish("1");
		}
		kyqNoticeService.saveNoticeByObj(kyqNotice);
		
		if(StringUtils.isNotBlank(noticeId)){
			List miningTranAppList = kyqNoticeService.queryMiningTranAppListByNoticeId(noticeId);
			if(miningTranAppList!=null && miningTranAppList.size()>0){
				MiningTranApp miningTranApp = (MiningTranApp)miningTranAppList.get(0);
				request.setAttribute("miningTranApp", miningTranApp);
			}
		}
		
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("recordId", recordId);
		request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("modeNameEn", "print_kq_result_word.doc");

		return mapping.findForward("printKqResultNotice");
	}
	
	public ActionForward queryMiningFileList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();
		condition.setTranStatus(KyqContent.KYQ_STATUS_PUBLISHED_NOTICE);//发布出让公告
		List kyqNoticeList = kyqNoticeService.queryNoticeList(condition);		
		
		List miningTranAppList = new ArrayList();
		if(kyqNoticeList!=null && kyqNoticeList.size()>0){
			for(int i=0; i<kyqNoticeList.size(); i++){
				KyqNotice kyqNotice = (KyqNotice)kyqNoticeList.get(i);
				List tempMiningTranAppList = kyqNoticeService.queryMiningTranAppListByNoticeId(kyqNotice.getNoticeId());
				miningTranAppList.addAll(tempMiningTranAppList);
			}
		}
			
		request.setAttribute("miningTranAppList", miningTranAppList);

		return mapping.findForward("toKqFileList");
	}
	
	public ActionForward makeFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tranAppId = request.getParameter("tranAppId");
		
		MiningTranApp miningTranApp = miningTranAppService.getMiningTranAppByTranAppId(tranAppId);
		KyqNotice kyqNotice= kyqNoticeService.getNoticeByNoticeId(miningTranApp.getNoticeId());
		
		//如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		TdscBlockFileApp tdscBlockFileOld = null;
		tdscBlockFileOld = tdscFileService.getBlockFileAppById(tranAppId);
		if (tdscBlockFileOld != null) {
			request.setAttribute("recordId",tdscBlockFileOld.getRecordId());
		}else{
			request.setAttribute("recordId", new Date().getTime()+"");
		}
			
		request.setAttribute("miningTranApp", miningTranApp);
		request.setAttribute("kyqNotice", kyqNotice);
		request.setAttribute("tranAppId", tranAppId);
		request.setAttribute("modeNameEn", "print_kq_file_word.doc");

		return mapping.findForward("printKqFile");
	}
	
	public ActionForward saveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tranAppId = request.getParameter("tranAppId") + "";
		String noticeId = request.getParameter("noticeId") + "";
		String modeNameEn = request.getParameter("fileName");
		String recordId = request.getParameter("recordID");

		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		TdscBlockFileApp tdscBlockFileOld = null;
		tdscBlockFileOld = tdscFileService.getBlockFileAppById(tranAppId);

		if (tdscBlockFileOld != null) {
			tdscBlockFileOld.setFilePerson(user.getUserId());
			tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
			// 说明已经保存过，update下
			tdscFileService.update(tdscBlockFileOld);
		} else {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp.setRecordId(recordId);
			tdscBlockFileApp.setFileId(tranAppId);
			tdscBlockFileApp.setFileUrl(modeNameEn);// 将文件类型 保存在附件地址中
			tdscBlockFileApp.setFilePerson(user.getUserId());
			tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
			// 保存出让文件信息 TDSC_BLOCK_FILE_APP
			tdscFileService.save(tdscBlockFileApp);
		}
		request.setAttribute("recordId", recordId);
		request.setAttribute("tranAppId", tranAppId);
		request.setAttribute("modeNameEn", modeNameEn);
		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("printKqFile");
	}
	
}
