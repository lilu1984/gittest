package com.wonders.tdsc.tdscbase.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.tdscbase.dao.CommonQueryDao;

public class CommonQueryService extends BaseSpringManagerImpl {

	private CommonQueryDao commonQueryDao;


	private TdscBlockPlanTableDao tdscBlockPlanTableDao;
	private TdscBlockPartDao tdscBlockPartDao;



	private CommonFlowService commonFlowService;

	private AppFlowService appFlowService;

	private TdscBidderAppService tdscBidderAppService;

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setCommonQueryDao(CommonQueryDao commonQueryDao) {
		this.commonQueryDao = commonQueryDao;
	}
	public CommonQueryDao getCommonQueryDao() {
		return commonQueryDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}
	public TdscBlockPartDao getTdscBlockPartDao() {
		return tdscBlockPartDao;
	}

	public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}

	public TdscBlockPlanTableDao getTdscBlockPlanTableDao() {
		return tdscBlockPlanTableDao;
	}

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}
	/**
	 * 根据查询条件返回列表 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTdscBlockAppViewList(TdscBaseQueryCondition condition) {
		condition = queryAppListByNodeId(condition);
		List tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewList(condition);
		return tdscBlockAppViewList;
	}

	/**
	 * 根据查询条件返回列表(without节点) 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTdscBlockAppViewListWithoutNode(TdscBaseQueryCondition condition) {
		List tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewListWithoutNodeId(condition);
		List newList = new ArrayList();
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				TdscBlockAppView newTdscBlockAppView = new TdscBlockAppView();
				BeanUtils.copyProperties(newTdscBlockAppView, tdscBlockAppView);
				newList.add(newTdscBlockAppView);
				if (newTdscBlockAppView != null) {
					String unitebBlockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(newTdscBlockAppView.getBlockId());
					if (unitebBlockCode != null && !"".equals(unitebBlockCode)) {
						newTdscBlockAppView.setUnitebBlockCode(unitebBlockCode);
					}
				}
			}
		}
		return newList;
	}

	/**
	 * 根据查询条件返回列表(without节点) 地块交易信息,地块基本信息,进度安排表,地块用途信息列表,列表中的记录按照区县和地块名称排序，其中区县排序先后次序固定为锡山、惠山、滨湖、新区、崇安、南长、北塘
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTdscBlockAppViewListWithoutNodeOrderByDistrictAndName(TdscBaseQueryCondition condition) {
		List tdscBlockAppViewList = this.commonQueryDao.queryTdscBlockAppViewListWithoutNodeOrderByDistrictAndName(condition);
		List newList = new ArrayList();
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				TdscBlockAppView newTdscBlockAppView = new TdscBlockAppView();
				BeanUtils.copyProperties(newTdscBlockAppView, tdscBlockAppView);
				newList.add(newTdscBlockAppView);
				if (newTdscBlockAppView != null) {
					String unitebBlockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(newTdscBlockAppView.getBlockId());
					if (unitebBlockCode != null && !"".equals(unitebBlockCode)) {
						newTdscBlockAppView.setUnitebBlockCode(unitebBlockCode);
					}
				}
			}
		}
		return newList;
	}

	/**
	 * 根据查询条件(planId)返回列表 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTdscBlockAppViewListByPlanId(TdscBaseQueryCondition condition) {
		List tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewListByPlanId(condition);
		return tdscBlockAppViewList;
	}

	/**
	 * 根据查询条件返回双杨流程待办列表 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTdscBlockAppViewFlowList(TdscBaseQueryCondition condition) {
		condition = queryAppFlowListByNodeId(condition);
		condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);
		List tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewList(condition);
		return tdscBlockAppViewList;
	}

	/**
	 * 根据查询条件返回TdscBlockAppView对象 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBlockAppView getTdscBlockAppView(TdscBaseQueryCondition condition) {
		TdscBlockAppView tdscBlockAppView = this.commonQueryDao.getTdscBlockAppView(condition);
		return tdscBlockAppView;
	}

	/**
	 * 根据查询条件返回TdscBlockAppView对象 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBlockAppView getTdscBlockAppViewByBlockId(String blockId) {
		TdscBlockAppView tdscBlockAppView = this.commonQueryDao.getTdscBlockAppViewByBlockId(blockId);
		return tdscBlockAppView;
	}

	/**
	 * 通过节点ID,返回双杨流程中的APPIDLIST
	 * 
	 * @param condition
	 * @return
	 */
	private TdscBaseQueryCondition queryAppFlowListByNodeId(TdscBaseQueryCondition condition) {
		if (condition == null || condition.getNodeId() == null) {
			return null;
		} else {
			List appIdList = new ArrayList();
			try {
				appIdList = this.appFlowService.queryFlowList(condition.getNodeId(), condition.getUser(), condition.getNodeList());
				condition.setNodeList(null);
			} catch (Exception ex) {
				logger.debug("error");
			}
			condition.setAppIdList(appIdList);
			return condition;
		}
	}

	/**
	 * 通过节点ID,返回APPIDLIST
	 * 
	 * @param condition
	 * @return
	 */
	private TdscBaseQueryCondition queryAppListByNodeId(TdscBaseQueryCondition condition) {
		if (condition != null && condition.getNodeId() != null) {
			List appIdList = new ArrayList();
			try {
				if (condition.getStatusId() != null) {
					if (condition.getEndlessTag() != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(condition.getEndlessTag())) {
						appIdList = this.commonFlowService.queryAppList(condition.getNodeId(), condition.getStatusId(), GlobalConstants.QUERY_ENDLESS_TAG);
					} else {
						appIdList = this.commonFlowService.queryAppList(condition.getNodeId(), condition.getStatusId());
					}
				} else {
					if (condition.getEndlessTag() != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(condition.getEndlessTag())) {
						appIdList = this.commonFlowService.queryAppList(condition.getNodeId(), null, GlobalConstants.QUERY_ENDLESS_TAG);
					} else {
						appIdList = this.commonFlowService.queryAppList(condition.getNodeId());
					}
				}
			} catch (Exception ex) {
				logger.debug("error");
			}
			condition.setAppIdList(appIdList);
			return condition;
		} else if (condition != null && condition.getNodeList() != null && condition.getNodeList().size() > 0) {
			List appIdList = new ArrayList();
			try {
				for (int i = 0; i < condition.getNodeList().size(); i++) {
					List tempAppIdList = new ArrayList();
					if (condition.getPlusConditionTag() != null) {
						tempAppIdList = this.commonFlowService.queryAppList((String) condition.getNodeList().get(i), null, null, condition.getPlusConditionTag());
					} else {
						tempAppIdList = this.commonFlowService.queryAppList((String) condition.getNodeList().get(i));
					}
					if (tempAppIdList != null && tempAppIdList.size() > 0) {
						for (int j = 0; j < tempAppIdList.size(); j++) {
							appIdList.add(tempAppIdList.get(j));
						}
					}
				}
			} catch (Exception ex) {
				logger.debug("error");
			}
			condition.setAppIdList(appIdList);
			return condition;
		} else if (condition != null && condition.getFlowConditonList() != null && condition.getFlowConditonList().size() > 0) {
			List appIdList = new ArrayList();
			try {
				for (int i = 0; i < condition.getFlowConditonList().size(); i++) {
					TdscFlowCondition tempCondition = (TdscFlowCondition) condition.getFlowConditonList().get(i);
					String nodeId = tempCondition.getNodeId();
					String statusId = tempCondition.getStatusId();
					List tempAppIdList = null;
					if (statusId != null) {
						if (condition.getEndlessTag() != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(condition.getEndlessTag())) {
							tempAppIdList = this.commonFlowService.queryAppList(nodeId, statusId, GlobalConstants.QUERY_ENDLESS_TAG);
						} else {
							tempAppIdList = this.commonFlowService.queryAppList(nodeId, statusId);
						}
					} else {
						if (condition.getEndlessTag() != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(condition.getEndlessTag())) {
							tempAppIdList = this.commonFlowService.queryAppList(nodeId, null, GlobalConstants.QUERY_ENDLESS_TAG);
						} else {
							tempAppIdList = this.commonFlowService.queryAppList(nodeId);
						}
					}
					if (tempAppIdList != null && tempAppIdList.size() > 0) {
						for (int j = 0; j < tempAppIdList.size(); j++) {
							appIdList.add(tempAppIdList.get(j));
						}
					}
				}
			} catch (Exception ex) {
				logger.debug("error");
			}
			condition.setAppIdList(appIdList);
			return condition;
		} else {
			return null;
		}
	}

	/**
	 * without节点ID,返回APPIDLIST
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBaseQueryCondition queryAppListWithoutNodeId(TdscBaseQueryCondition condition) {
		if (condition == null) {
			return null;
		} else {
			List appIdList = new ArrayList();
			try {
				appIdList = this.commonFlowService.queryAppList(null);
			} catch (Exception ex) {
				logger.debug("error");
			}
			condition.setAppIdList(appIdList);
			return condition;
		}
	}

	/**
	 * 把queryTdscBlockAppViewList封装成pageList，返回TdscBlockAppView的pageList
	 * 
	 * @param condition
	 * @return
	 */
	public PageList queryTdscBlockAppViewPageList(TdscBaseQueryCondition condition) {
		condition = queryAppListByNodeId(condition);
		PageList tdscBlockAppPageList = this.commonQueryDao.findTdscBlockAppViewPageList(condition);
		return tdscBlockAppPageList;
	}

	/**
	 * 把queryTdscBlockAppViewList封装成pageList，返回TdscBlockAppView的pageList
	 * 
	 * @param condition
	 * @return
	 */
	public PageList queryTdscBlockAppViewPageListWithoutNode(TdscBaseQueryCondition condition) {
		PageList tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewPageListWithoutNodeId(condition);
		return tdscBlockAppViewList;
	}

	/**
	 * 把queryTdscBlockAppViewFlowList封装成pageList，返回TdscBlockAppView的pageList
	 * 
	 * @param condition
	 * @return
	 */
	public PageList queryTdscBlockAppViewFlowPageList(TdscBaseQueryCondition condition) {
		condition = queryAppFlowListByNodeId(condition);
		condition.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_INIT);
		PageList tdscBlockAppViewList = this.commonQueryDao.findTdscBlockAppViewPageList(condition);
		return tdscBlockAppViewList;
	}

	/**
	 * 根据 appid 查询所有的竞买人
	 * 
	 * @param appId
	 * @return List<TdscBidderForm>
	 */
	public List getAllBiddersByAppId(String appId) {

		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setAppId(appId);
		cond.setOrderKey("blockNoticeNo");
		TdscBlockAppView appView = commonQueryDao.getTdscBlockAppView(cond);

		List retList = new ArrayList();
		Map map = new HashMap();

		List bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appId);

		if (bidderAppList != null && bidderAppList.size() > 0) {
			for (int m = 0; m < bidderAppList.size(); m++) {
				TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);

				TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

				TdscBidderForm bean = new TdscBidderForm();

				//System.out.println(m+" bidderId is : "+bidderPersonApp.getBidderId());
				//System.out.println(m+" bidderName is : "+bidderPersonApp.getBidderName());
				bean.setAppId(bidderApp.getAppId());
				bean.setBidderId(bidderPersonApp.getBidderId());
				bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
				bean.setBidderName(bidderPersonApp.getBidderName());
				bean.setAcceptNo(bidderApp.getAcceptNo());

				bean.setIsPurposePerson(bidderApp.getIsPurposePerson());

				bean.setBlockName(appView.getBlockName());
				if (!StringUtil.isEmpty(bidderPersonApp.getBidderLxdh()))
					bean.setBidderLxdh(bidderPersonApp.getBidderLxdh());
				else
					bean.setBidderLxdh("&nbsp;");

				if (!StringUtil.isEmpty(bidderPersonApp.getLinkManName()))
					bean.setLinkManName(bidderPersonApp.getLinkManName());
				else
					bean.setLinkManName("&nbsp;");

				map.put(bidderPersonApp.getBidderName(), bean);

			}
			if (map.size() != 0) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					// Object key = entry.getKey();
					Object value = entry.getValue();
					retList.add((TdscBidderForm) value);
				}

			}
		} else {
			TdscBidderForm bean = new TdscBidderForm();
			bean.setBlockName(appView.getBlockName());
			retList.add(bean);
		}

		return retList;

	}
	
	
	/**
	 * 根据 appid 查询该地块的意向人
	 * 
	 * @param appId
	 * @return List<TdscBidderForm>
	 */
	public TdscBidderPersonApp getPurposePersonByAppId(String purposeAppId) {
		
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		TdscBidderCondition condition = new TdscBidderCondition();
		condition.setPurposeAppId(purposeAppId);
		
		List tdscBidderPersonAppList = tdscBidderAppService.findTdscBidderPersonAppListByCondition(condition);
		
		if(tdscBidderPersonAppList!=null && tdscBidderPersonAppList.size()>0){
			tdscBidderPersonApp = (TdscBidderPersonApp)tdscBidderPersonAppList.get(0);
		}
		
		return tdscBidderPersonApp;

	}
	
	
	public PageList queryTdscBlockAppInNoticeId(List noticeId, int currPage, String noticeNo, String blockNoticeNo, String blockName) {
		return commonQueryDao.queryTdscBlockAppInNoticeId(noticeId,currPage, noticeNo, blockNoticeNo, blockName);
	}
	
}
