package com.wonders.tdsc.blockwork.web;

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
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.service.UserManager;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscFileSaleInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.web.form.TdscBlockForm;
import com.wonders.tdsc.blockwork.web.form.TdscFileSaleInfoForm;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscFileSaleInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscFileSaleCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.publishinfo.service.TdscNoticePublishService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscFileSaleInfoAction extends BaseAction {

	// add by xys start
	// 地块领取资料统计表
	private static final String			SEQ_NO			= "SEQ_NO";

	private static final String			COMPANY_NAME	= "COMPANY_NAME";

	private static final String			BLOCK_NOTICE_NO	= "BLOCK_NOTICE_NO";

	private static final String			LINK_MAN		= "LINK_MAN";

	private static final String			LINK_TEL		= "LINK_TEL";

	private static final String			BLOCK_SUM		= "BLOCK_SUM";

	private static final String			SALE_QUXIAN		= "SALE_QUXIAN";

	// add by xys end

	private TdscNoticePublishService	tdscNoticePublishService;

	private TdscFileService				tdscFileService;

	private TdscNoticeService			tdscNoticeService;

	private TdscFileSaleInfoService		tdscFileSaleInfoService;

	private CommonQueryService			commonQueryService;

	private UserManager					userManager;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setTdscNoticePublishService(TdscNoticePublishService tdscNoticePublishService) {
		this.tdscNoticePublishService = tdscNoticePublishService;
	}

	public void setTdscFileSaleInfoService(TdscFileSaleInfoService tdscFileSaleInfoService) {
		this.tdscFileSaleInfoService = tdscFileSaleInfoService;
	}

	/**
	 * 查询出此人的所有已经买文件的地块和其基本信息 转到页面中
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toModifyOneBuyFilePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		String personName = request.getParameter("personName");

		if (validateChinese(personName))
			personName = StringUtil.GBKtoISO88591(personName);

		TdscBaseQueryCondition querycondition = new TdscBaseQueryCondition();
		querycondition.setNoticeId(noticeId);
		querycondition.setOrderKey("blockNoticeNo");
		List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(querycondition);
		request.setAttribute("blockList", blockList);

		// 该人的基本信息
		TdscFileSaleInfo info = (TdscFileSaleInfo) tdscFileSaleInfoService.getFileSaleInfo(noticeId, personName);
		request.setAttribute("retInfo", info);

		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("toModifyOneBuyFilePerson");

	}

	/**
	 * 删除一个买文件的单位
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteOneBuyFilePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		String personName = request.getParameter("personName");

		if (validateChinese(personName))
			personName = StringUtil.GBKtoISO88591(personName);

		tdscFileSaleInfoService.deleteFileSaleByNoticeAndUnitName(noticeId, personName);

		StringBuffer sb = new StringBuffer("sale.do?method=queryBuyFilePersonList");
		sb.append("&noticeId=").append(noticeId);
		ActionForward f = new ActionForward(sb.toString(), true);
		return f;

	}

	// 查找已发布的公告列表
	public ActionForward queryNoticeList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fromMenu = request.getParameter("fromMenu");

		// 获取页面参数
		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		//condition.setTradeNum(StringUtil.GBKtoISO88591(condition.getTradeNum()));
		bindObject(condition, form);

		//2011/05/09 加入可查询历史数据的条件
		if (!"1".equals(condition.getIfResultPublish())) {
			condition.setNoticeStatus("02");
			condition.setIfReleased("1");
			condition.setIfResultPublish("0");// 交易结果未公示的记录，未成交的数据
		}
		
		if (!StringUtils.isEmpty(request.getParameter("currentPage")))
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// 设置页面行数
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// 获取用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		// if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
		// condition.setUserId(user.getUserId());
		// }
		// 查询列表
		PageList pageList = tdscNoticePublishService.findPageList(condition);

		if (pageList != null) {
			// 增加页面需要的字段 from tdscBlockAppView----strat
			List list = pageList.getList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					TdscNoticeApp app = (TdscNoticeApp) list.get(i);

					TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
					conditionBlock.setNoticeId(app.getNoticeId());

					List retList = tdscFileSaleInfoService.queryAllCompanyList(app.getNoticeId());
					if (retList != null && retList.size() > 0) {
						app.setIfSendBlockWondertek("canModify");// 是否有“详细”的按钮
					}

					// 所有地块信息表
//					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
//					String blockName = "";
//					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
//						for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
//
//							TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
//
//							blockName += blockapp.getBlockName() + ",";
//							app.setTranManTel(blockapp.getTradeNum());// 将"招拍挂编号"存在此字段中
//							app.setTranManAddr(blockapp.getTransferMode());// 将"出让方式"存在此字段中
//						}
//						blockName = blockName.substring(0, blockName.length() - 1);
//						app.setTranManName(blockName);// 将地块名称存在此字段中
//					}
					
					app.setTranManTel(app.getTradeNum());// 将"招拍挂编号"存在此字段中
					app.setTranManAddr(app.getTransferMode());// 将"出让方式"存在此字段中

				}
			}
			// -----end
		}

		//condition.setTradeNum(StringUtil.ISO88591toGBK(condition.getTradeNum()));
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);

		request.setAttribute("fromMenu", fromMenu);

		return mapping.findForward("noticeList");
	}

	/**
	 * 查询所有买文件人的列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBuyFilePersonList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		List retList = tdscFileSaleInfoService.queryAllCompanyList(noticeId);
		request.setAttribute("returnList", filterCompany(retList));
		return mapping.findForward("allBuyFilePersons");
	}

	// 查找已发布的公告的所有登记竞买人列表
	public ActionForward queryFilePeasonList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		String noticeId = request.getParameter("noticeId");

		String type = "";
		if (request.getParameter("type") != null && !"".equals(request.getParameter("type"))) {
			type = request.getParameter("type");
		}
		TdscBaseQueryCondition querycondition = new TdscBaseQueryCondition();
		querycondition.setNoticeId(noticeId);
		querycondition.setStatus("01");//仅查询交易中的地块
		querycondition.setOrderKey("blockNoticeNo");
		List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(querycondition);
		request.setAttribute("blockList", blockList);

		bindObject(condition, form);
		condition.setIfValidity("1");
		if (noticeId != null && !"".equals(noticeId)) {
			condition.setNoticeId(noticeId);
			request.setAttribute("noticeId", noticeId);
		}

		// if (!StringUtils.isEmpty(request.getParameter("currentPage")))
		// condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// 设置页面行数
		// condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 查询竞买人列表
		// List pageList = tdscFileSaleInfoService.findPageList(condition);

		request.setAttribute("type", type);
		// request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);
		if ("dengji".equals(type)) {
			return mapping.findForward("toNewPeasonList");
		} else {
			return mapping.findForward("toPeasonList");
		}
	}

	public ActionForward addNewPeason(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		String type = request.getParameter("type");
		String noticeId = request.getParameter("noticeId");

		bindObject(condition, form);
		condition.setIfValidity("1");
		if (noticeId != null && !"".equals(noticeId)) {
			condition.setNoticeId(noticeId);
			request.setAttribute("noticeId", noticeId);
		}

		// 查询竞买人列表
		List pageList = tdscFileSaleInfoService.findPageList(condition);

		request.setAttribute("type", type);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("toNewPeasonList");
	}

	// iweb打印竞买人列表
	public ActionForward iwebPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		String noticeId = request.getParameter("noticeId");

		bindObject(condition, form);
		condition.setIfValidity("1");
		if (noticeId != null && !"".equals(noticeId)) {
			condition.setNoticeId(noticeId);
			request.setAttribute("noticeId", noticeId);
		}

		// 查询竞买人列表
		List list = tdscFileSaleInfoService.findPageList(condition);

		// 招拍挂编号 start
		String tradeNum = "";// "招拍挂编号"
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				TdscFileSaleInfo app = (TdscFileSaleInfo) list.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());

				// 所有地块信息表
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);

				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
						TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
						if (blockapp.getTradeNum() != null && !"".equals(blockapp.getTradeNum()))
							tradeNum = blockapp.getTradeNum();
					}
				}
			}
		}
		// -----end

		request.setAttribute("modeNameEn", "crwj_gmdjb.doc");
		request.setAttribute("tradeNum", tradeNum);
		request.setAttribute("list", list);
		request.setAttribute("condition", condition);

		return mapping.findForward("toIwebPrint");
	}

	public ActionForward saveFileSaleList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscFileSaleInfoForm tdscFileSaleInfoForm = (TdscFileSaleInfoForm) form;
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String flag = request.getParameter("fromPage") + "";

		// bidderLxdz 存的是所有 sale ID

		tdscFileSaleInfoService.saveFileSaleList(tdscFileSaleInfoForm, user, flag);
		request.setAttribute("saveMessage", "保存成功");
		return mapping.findForward("toNewPeasonList");
	}

	// 打印 出让地块领取资料统计表
	public ActionForward normalPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数 402887552cee4346012cee46c133001e
		String noticeId = request.getParameter("noticeId");
		List resultList = new ArrayList();
		String rptTitle = "";
		String transferMode = "";

		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setNoticeId(noticeId);
		//cond.setStatus("01");//仅查询正在交易的地块，过滤终止的地块
		cond.setOrderKey("blockNoticeNo");
		// 1. 查询该文件所有的地块
		List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
		if (blockList != null && blockList.size() > 0) {
			// 2. 得到每个地块购买人数
			for (int i = 0; i < blockList.size(); i++) {
				TdscBlockAppView results = (TdscBlockAppView) blockList.get(i);
				if ("3107".equals(results.getTransferMode())) {
					transferMode = "招标";
				} else if ("3103".equals(results.getTransferMode())) {
					transferMode = "拍卖";
				} else if ("3104".equals(results.getTransferMode())) {
					transferMode = "挂牌";
				}
				List personList = tdscFileSaleInfoService.countSalePerson(noticeId, results.getAppId());

				int iCountPerson = 0;
				if (personList != null && personList.size() > 0)
					iCountPerson = personList.size();

				TdscBlockForm formBean = new TdscBlockForm();

				formBean.setBlockNoticeNo(results.getBlockNoticeNo());// 地块公告号
				formBean.setBlockName(results.getBlockName());// 地块名称
				formBean.setCountSalePerson(iCountPerson + "");// 购买人数

				rptTitle = results.getNoitceNo().substring(0, 10)+ Integer.parseInt(results.getNoitceNo().substring(10, 12)) + "号" + transferMode + "出让地块购买人数统计表";

				resultList.add(formBean);
			}
		}
		request.setAttribute("resultList", resultList);
		request.setAttribute("rptTitle", rptTitle);

		return mapping.findForward("normalPrint");
	}

	public ActionForward superPrint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数 402887552cee4346012cee46c133001e
		String noticeId = request.getParameter("noticeId");

		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setNoticeId(noticeId);
		//cond.setStatus("01");//仅查询正在交易的地块，过滤终止的地块
		cond.setOrderKey("blockNoticeNo");
		// 1. 查询该文件所有的地块
		List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
		// 2. 查询所有竞买公司, 有重复,需过滤;
		List companyList = filterCompany(tdscFileSaleInfoService.queryAllCompanyList(noticeId));

		// 3. 整理数据
		String rptTitle = "";
		String transferMode = "";
		List result = new ArrayList();
		// 表头
		Map headMap = new HashMap();
		headMap.put(SEQ_NO, "序号");
		headMap.put(COMPANY_NAME, "单　　位");
		for (int j = 0; j < blockList.size(); j++) {
			TdscBlockAppView results = (TdscBlockAppView) blockList.get(j);
			if ("3107".equals(results.getTransferMode())) {
				transferMode = "招标";
			} else if ("3103".equals(results.getTransferMode())) {
				transferMode = "拍卖";
			} else if ("3104".equals(results.getTransferMode())) {
				transferMode = "挂牌";
			}
			headMap.put(BLOCK_NOTICE_NO + j, results.getBlockNoticeNo());
			
			rptTitle = results.getNoitceNo().substring(0, 10)+ Integer.parseInt(results.getNoitceNo().substring(10, 12)) + "号" + transferMode + "出让地块领取资料统计表";
		}
		headMap.put(LINK_MAN, "联系人");
		headMap.put(LINK_TEL, "联系电话");
		headMap.put(BLOCK_SUM, "合计");
		headMap.put(SALE_QUXIAN, "登记区县");// 卖文件的区县
		result.add(headMap);

		if ((companyList != null && companyList.size() > 0) && (blockList != null && blockList.size() > 0)) {
			for (int i = 0; i < companyList.size(); i++) {
				int iSum = 0;
				TdscFileSaleInfo companys = (TdscFileSaleInfo) companyList.get(i);

				Map blockMap = new HashMap();
				blockMap.put(SEQ_NO, (i + 1) + "");
				blockMap.put(COMPANY_NAME, companys.getBidderName());// 公司名称

				// 根据买文件人拿出 appId
				String strTmp = companys.getBidderName();
				if (validateChinese(companys.getBidderName()))
					strTmp = StringUtil.GBKtoISO88591(companys.getBidderName());

				String appIdList = tdscFileSaleInfoService.queryAppIdsByBidderName(companys.getNoticeId(), strTmp);

				// 遍历所有地块，如果买文件则 map 中放入 *，否则 map 中放入 空格
				for (int j = 0; j < blockList.size(); j++) {
					TdscBlockAppView results = (TdscBlockAppView) blockList.get(j);

					if (appIdList.indexOf(results.getAppId()) > -1) {
						blockMap.put(BLOCK_NOTICE_NO + j, "*");
						iSum++;
					} else {
						blockMap.put(BLOCK_NOTICE_NO + j, " ");
					}

				}
				blockMap.put(LINK_MAN, companys.getResultName());// 联系人
				blockMap.put(LINK_TEL, companys.getBidderLxdh());// 联系电话
				blockMap.put(BLOCK_SUM, iSum + "");// 合计

				UserInfo userInfo = userManager.getUser(companys.getActionUser());
				Map orgMap = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_DISTRICT_BELONG);

				blockMap.put(SALE_QUXIAN, orgMap.get(userInfo.getRegionId()) + "");// 卖文件的区县
				result.add(blockMap);
			}

			// 报表底部 合计
			Map footMap = new HashMap();
			footMap.put(SEQ_NO, "");
			footMap.put(COMPANY_NAME, "合计");
			for (int j = 0; j < blockList.size(); j++) {
				TdscBlockAppView results = (TdscBlockAppView) blockList.get(j);

				List personList = tdscFileSaleInfoService.countSalePerson(noticeId, results.getAppId());
				int iCountPerson = 0;
				if (personList != null && personList.size() > 0)
					iCountPerson = personList.size();

				footMap.put(BLOCK_NOTICE_NO + j, iCountPerson + "");
			}
			footMap.put(LINK_MAN, "");
			footMap.put(LINK_TEL, "");
			footMap.put(BLOCK_SUM, "");
			footMap.put(SALE_QUXIAN, "");
			result.add(footMap);
		}

		request.setAttribute("resultList", result);
		request.setAttribute("blockList", blockList);
		request.setAttribute("rptTitle", rptTitle);

		return mapping.findForward("superPrint");
	}

	/**
	 * 过滤重复的公司
	 * 
	 * @param companys
	 * @return
	 */
	private List filterCompany(List companys) {
		if (companys != null && companys.size() > 0) {
			for (int i = 0; i < companys.size() - 1; i++) {
				for (int j = companys.size() - 1; j > i; j--) {
					String jBidderName = ((TdscFileSaleInfo) companys.get(j)).getBidderName();
					String iBidderName = ((TdscFileSaleInfo) companys.get(i)).getBidderName();
					String jLxdh = ((TdscFileSaleInfo) companys.get(j)).getBidderLxdh();
					String iLxdh = ((TdscFileSaleInfo) companys.get(i)).getBidderLxdh();
					if (jBidderName.equals(iBidderName) && jLxdh.equals(iLxdh)) {
						companys.remove(j);
					}
				}
			}
			return companys;
		}

		return null;
	}

	private boolean validateChinese(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}
}
