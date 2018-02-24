package com.wonders.tdsc.blockwork.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.json.JSONException;
import org.json.JSONObject;

import com.quarts.manager.xo.XmlEntry;
import com.quarts.manager.xo.XmlJobDataMap;
import com.wonders.common.authority.SysUser;
import com.wonders.common.protocol.impl.HttpClientRemoteFactory;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.id.bo.IncrementId;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.blockwork.dao.IncrementIdDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockConInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockMaterialDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPjxxInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockQqjcInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockRemisemoneyDefrayDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockUsedInfoDao;
import com.wonders.tdsc.blockwork.dao.WxPersonInfoDao;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockConInfo;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockMaterial;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPjxxInfo;
import com.wonders.tdsc.bo.TdscBlockQqjcInfo;
import com.wonders.tdsc.bo.TdscBlockRemisemoneyDefray;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockMaterialCondition;
import com.wonders.tdsc.bo.condition.TdscBlockUsedInfoCondition;
import com.wonders.tdsc.bo.condition.TdscListingInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.dao.TdscAppDao;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.lob.bo.TdscEsClob;
import com.wonders.tdsc.lob.dao.TdscEsClobDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;
import com.wonders.tdsc.presell.dao.FileRefDao;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bo.PersonInfo;

/**
 * @author administrator
 * 
 */
public class TdscBlockInfoService extends BaseSpringManagerImpl {

	private static Logger logger = Logger.getLogger(TdscBlockInfoService.class);

	private TdscBlockInfoDao tdscBlockInfoDao;

	private TdscBlockMaterialDao tdscBlockMaterialDao;

	private TdscBlockTranAppDao tdscBlockTranAppDao;

	private TdscBlockUsedInfoDao tdscBlockUsedInfoDao;

	private TdscListingInfoDao tdscListingInfoDao;

	private TdscBlockConInfoDao tdscBlockConInfoDao;

	private TdscEsClobDao tdscEsClobDao;

	private TdscBlockPartDao tdscBlockPartDao;

	private AppFlowService appFlowService;

	private TdscBlockQqjcInfoDao tdscBlockQqjcInfoDao;

	private TdscBlockPjxxInfoDao tdscBlockPjxxInfoDao;

	private TdscBidderPersonAppDao tdscBidderPersonAppDao;

	private TdscBidderAppDao tdscBidderAppDao;
	
	private TdscAppDao tdscAppDao;

	private TdscBlockRemisemoneyDefrayDao tdscBlockRemisemoneyDefrayDao;
	
	private FileRefDao fileRefDao;
	
	private WxPersonInfoDao wxPersonInfoDao;
	
//	private IdSpringManager idSpringManager;
	
	private IncrementIdDao incrementIdDao;

	private String PATH = PropertiesUtil.getInstance().getProperty("file.save.path");
	
	private String HTTPURL = PropertiesUtil.getInstance().getProperty("httpclient_fileuploadfile_url");
	
	public void setTdscAppDao(TdscAppDao tdscAppDao) {
		this.tdscAppDao = tdscAppDao;
	}

	public void setTdscBlockRemisemoneyDefrayDao(
			TdscBlockRemisemoneyDefrayDao tdscBlockRemisemoneyDefrayDao) {
		this.tdscBlockRemisemoneyDefrayDao = tdscBlockRemisemoneyDefrayDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}

	public void setTdscBlockConInfoDao(TdscBlockConInfoDao tdscBlockConInfoDao) {
		this.tdscBlockConInfoDao = tdscBlockConInfoDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	public void setTdscBlockMaterialDao(TdscBlockMaterialDao tdscBlockMaterialDao) {
		this.tdscBlockMaterialDao = tdscBlockMaterialDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscListingInfoDao(TdscListingInfoDao tdscListingInfoDao) {
		this.tdscListingInfoDao = tdscListingInfoDao;
	}

	public void setTdscBlockUsedInfoDao(TdscBlockUsedInfoDao tdscBlockUsedInfoDao) {
		this.tdscBlockUsedInfoDao = tdscBlockUsedInfoDao;
	}

	public void setTdscEsClobDao(TdscEsClobDao tdscEsClobDao) {
		this.tdscEsClobDao = tdscEsClobDao;
	}

	public void setTdscBlockQqjcInfoDao(TdscBlockQqjcInfoDao tdscBlockQqjcInfoDao) {
		this.tdscBlockQqjcInfoDao = tdscBlockQqjcInfoDao;
	}

	public void setTdscBlockPjxxInfoDao(TdscBlockPjxxInfoDao tdscBlockPjxxInfoDao) {
		this.tdscBlockPjxxInfoDao = tdscBlockPjxxInfoDao;
	}

//	public void setIdSpringManager(IdSpringManager idSpringManager) {
//		this.idSpringManager = idSpringManager;
//	}

	public void setWxPersonInfoDao(WxPersonInfoDao wxPersonInfoDao) {
		this.wxPersonInfoDao = wxPersonInfoDao;
	}

	public void setIncrementIdDao(IncrementIdDao incrementIdDao) {
		this.incrementIdDao = incrementIdDao;
	}

	/**
	 * 根据地块Id获得土地信息
	 * 
	 * @param blockId
	 *            土地信息
	 * @return TdscBlockInfo对象
	 */
	public TdscBlockInfo findInfoById(String blockId) {
		return (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
	}

	/**
	 * 查询土地列表
	 */
	public PageList findPageList(TdscBlockInfoCondition condition) {
		return tdscBlockInfoDao.findPageList(condition);
	}

	public List queryBlockInfoList(TdscBlockInfoCondition condition) {
		return tdscBlockInfoDao.queryBlockInfoList(condition);
	}

	public List queryAppViewList(TdscBlockInfoCondition condition) {
		return tdscBlockInfoDao.queryAppViewList(condition);
	}

	/**
	 * 查询指定clobId的文件
	 * 
	 * @param clobId
	 * @return
	 */
	public TdscEsClob findClobContent(String clobId) {
		return (TdscEsClob) tdscEsClobDao.get(clobId);
	}

	/**
	 * 查询指定blockId的土地基本信息
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockInfo findBlockInfo(String blockId) {
		if (blockId != null) {
			return tdscBlockInfoDao.findBlockInfo(blockId);
		} else {
			return null;
		}
	}

	/**
	 * 保存子地块信息
	 * 
	 * @param tdscBlockPart
	 */
	public TdscBlockPart saveTdscBlockPart(TdscBlockPart tdscBlockPart) {
		if (tdscBlockPart != null) {
			tdscBlockPart = (TdscBlockPart) tdscBlockPartDao.save(tdscBlockPart);
			return tdscBlockPart;
		}
		return null;
	}

	/**
	 * 通过供地批文号查询地块
	 * 
	 * @param auditeNum
	 *            供地批文号
	 * @return
	 */
	public TdscBlockInfo blockInfoByAuditeNum(String auditeNum) {
		return tdscBlockInfoDao.blockInfoByAuditeNum(auditeNum);
	}

	/**
	 * 根据子地块partId查询子地块信息
	 * 
	 * @param partId
	 * @return
	 */
	public TdscBlockPart getTdscblockPart(String partId) {
		if (null != partId && !"".equals(partId)) {
			return (TdscBlockPart) this.tdscBlockPartDao.get(partId);
		}
		return null;
	}

	/**
	 * 查询blockId的所有子地块信息
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		if (null != blockId && !"".equals(blockId)) {
			return tdscBlockPartDao.getTdscBlockPartList(blockId);
		}
		return null;
	}

	/**
	 * 根据BlockCode查询所有子地块信息
	 * 
	 * @param blockCode
	 * @return
	 */
	public List getblockPartListByBlockCode(String blockId, String blockCode) {
		if (null != blockCode && !"".equals(blockCode)) {
			return tdscBlockPartDao.getblockPartListByBlockCode(blockId, blockCode);
		}
		return null;
	}

	/**
	 * 根据blockId获得所有子地块的地块编号
	 * 
	 * @param blockId
	 * @return
	 */
	public String getBlockCodeByBlockId(String blockId) {
		// 查询blockId的所有子地块信息
		List blockPartList = (List) this.getTdscBlockPartList(blockId);
		// 整理地块编号
		if (blockPartList != null && blockPartList.size() > 0) {
			StringBuffer blockCodes = new StringBuffer();
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(i);
				if (tdscBlockPart != null && !"".equals(tdscBlockPart.getBlockCode())) {
					if (i < blockPartList.size() - 1) {
						blockCodes.append(tdscBlockPart.getBlockCode()).append("，");
					} else {
						blockCodes.append(tdscBlockPart.getBlockCode());
					}
				}
			}
			return blockCodes.toString();
		}
		return null;
	}

	/**
	 * 根据blockCode获得指定地块编号的所有子地块的土地面积之和
	 * 
	 * @param blockCode
	 * @return
	 */
	public BigDecimal getAllBlockAreaByBlockCode(String blockId, String blockCode) {

		BigDecimal allBlockArea = new BigDecimal(0);// 地块面积之和

		// 查询blockId的所有子地块信息
		List blockPartList = (List) this.getblockPartListByBlockCode(blockId, blockCode);
		// 整理地块编号
		if (blockPartList != null && blockPartList.size() > 0) {
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(i);
				if (tdscBlockPart != null && !"".equals(tdscBlockPart.getBlockArea())) {
					BigDecimal blockArea = tdscBlockPart.getBlockArea();
					allBlockArea = allBlockArea.add(blockArea);
				}
			}
			return allBlockArea;
		}
		return null;
	}

	/**
	 * 根据blockId获得所有子地块的规划建筑面积之和
	 * 
	 * @param blockId
	 * @return
	 */
	public BigDecimal getAllBlockBuildingAreaByBlockCode(String blockId, String blockCode) {

		BigDecimal allBlockBuildingArea = new BigDecimal(0);// 地块面积之和

		// 查询blockId的所有子地块信息
		List blockPartList = (List) this.getblockPartListByBlockCode(blockId, blockCode);
		// 整理地块编号
		if (blockPartList != null && blockPartList.size() > 0) {
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(i);
				if (tdscBlockPart != null && !"".equals(tdscBlockPart.getBlockArea())) {
					BigDecimal blockArea = tdscBlockPart.getPlanBuildingArea();
					allBlockBuildingArea = allBlockBuildingArea.add(blockArea);
				}
			}
			return allBlockBuildingArea;
		}
		return null;
	}

	/**
	 * 删除子地块信息
	 * 
	 * @param tdscBlockPart
	 */
	public void deleteTdscBlockPart(TdscBlockPart tdscBlockPart) {
		if (tdscBlockPart != null)
			this.tdscBlockPartDao.delete(tdscBlockPart);
	}

	/**
	 * 修改子地块信息
	 * 
	 * @param tdscBlockPart
	 */
	public TdscBlockPart updateTdscBlockPart(TdscBlockPart tdscBlockPart) {
		if (tdscBlockPart != null) {
			return (TdscBlockPart) this.tdscBlockPartDao.saveOrUpdate(tdscBlockPart);
		}
		return null;
	}

	// 根据appId查挂牌会信息
	public TdscListingInfo findBlistingInfo(String appId) {
		return tdscListingInfoDao.getTdscListingInfoByAppId(appId);
	}

	// 根据status查待出让地块
	public List findBlockIdList(String status) {
		return tdscBlockInfoDao.blockInfoList(status);
	}

	// 查挂牌会信息列表
	public List findBlistingInfoList(TdscListingInfoCondition condition) {
		return tdscListingInfoDao.findAppList(condition);
	}

	/*
	 * 取消申请
	 */
	public void cancelApp(String appId) {
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		if (tdscBlockTranApp != null) {
			String blockId = tdscBlockTranApp.getBlockId();
			TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
			if (tdscBlockInfo != null) {
				tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_TRADEEND);
				tdscBlockInfoDao.update(tdscBlockInfo);
			}
			tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_CANCEL);
			tdscBlockTranAppDao.update(tdscBlockTranApp);
		}
	}

	public void saveBlockInfoAndPart(TdscBlockInfo tdscBlockInfo, List blockPartList) {
		// 保存土地基本信息
		String blockId = tdscBlockInfo.getBlockId();
		if (StringUtils.isNotBlank(blockId)) {
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		} else {
			tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_NOTTRADE);
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
			blockId = tdscBlockInfo.getBlockId();
		}
		// 删除原有子地块信息
		delBlockPartByBlockId(blockId);
		// 新增子地块信息
		if (blockPartList != null && blockPartList.size() > 0) {

			for (int j = 0; j < blockPartList.size(); j++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);

				tdscBlockPart.setBlockId(blockId);
				tdscBlockPartDao.save(tdscBlockPart);
			}
		}
	}

	/**
	 * 保存工业商业出让信息
	 * 
	 * @param tdscBlockInfo
	 * @param tdscBlockTranApp
	 * @param list
	 */
	// public Map saveRemise(TdscBlockInfo tdscBlockInfo, TdscBlockTranApp tdscBlockTranApp, List list,String saveType,SysUser user,List blockPartList,TdscBlockQqjcInfo
	// tdscBlockQqjcInfo,TdscEsClob tdscEsClob ) {
	// // 保存返回值
	// Map retMap = new HashMap();
	// // 保存土地基本信息
	// String blockId = tdscBlockInfo.getBlockId();
	// if (StringUtils.isNotBlank(blockId)) {
	// tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
	// } else {
	// tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
	// blockId = tdscBlockInfo.getBlockId();
	// }
	// retMap.put("tdscBlockInfo", tdscBlockInfo);
	// //“规划设计要点”字段作为CLOB保存
	// String bzGhsjyd = (String)tdscBlockTranApp.getBzGhsjyd();
	// if(tdscBlockTranApp != null ){
	// //查询原数据，获得“规划设计要点”对应的，在“TDSC_ES_CLOB”表中的ID;
	// //TdscBlockTranApp oldTranApp = (TdscBlockTranApp)tdscBlockTranAppDao.get(tdscBlockTranApp.getAppId());
	// // if(StringUtils.isNotBlank(oldTranApp.getBzGhsjyd())){
	// // //查询时，按照原ID查询
	// // TdscEsClob clob = (TdscEsClob)tdscEsClobDao.get(oldTranApp.getBzGhsjyd());
	// // if(clob != null){
	// // //更新时，保存新的数据内容
	// // clob.setClobContent(tdscBlockTranApp.getBzGhsjyd());
	// // tdscEsClobDao.update(clob);
	// // }else{
	// // TdscEsClob clob_1 = new TdscEsClob();
	// // clob_1.setClobContent(tdscBlockTranApp.getBzGhsjyd());
	// // clob_1 = (TdscEsClob) tdscEsClobDao.save(clob_1);
	// // tdscBlockTranApp.setBzGhsjyd(clob_1.getClobId());
	// // }
	// // }
	//            
	// }
	// // 通过保存TdscBlockInfo返回的BO取出主键 然后向TdscBlockTranApp设置外来键
	// tdscBlockTranApp.setBlockId(blockId);
	// String appId = tdscBlockTranApp.getAppId();
	// if (StringUtils.isNotBlank(appId)) {
	// tdscBlockTranAppDao.update(tdscBlockTranApp);
	// } else {
	// tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
	// appId = tdscBlockTranApp.getAppId();
	// }
	// //规划设计要点：页面显示的时候，显示文本内容
	// tdscBlockTranApp.setBzGhsjyd(bzGhsjyd);
	// retMap.put("tdscBlockTranApp", tdscBlockTranApp);
	// // 通过保存TdscBlockTranApp返回的BO取出主键 然后向TdscBlockMaterial设置外来键
	// deleteTdscBlockMaterial(appId);
	// if(list!=null && list.size()>0){
	// List rList = new ArrayList();
	// for (int i = 0; i < list.size(); i++) {
	// TdscBlockMaterial tdscBlockMaterial = (TdscBlockMaterial) list.get(i);
	// tdscBlockMaterial.setAppId(appId);
	//
	// if (tdscBlockMaterial != null) {
	// tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialDao.save(tdscBlockMaterial);
	// rList.add(tdscBlockMaterial);
	// }
	// }
	// }
	// //删除原有子地块信息
	// delBlockPartByBlockId(blockId);
	// //新增子地块信息
	// if(blockPartList != null && blockPartList.size()>0){
	// for(int j=0;j<blockPartList.size();j++){
	// TdscBlockPart tdscBlockPart = (TdscBlockPart)blockPartList.get(j);
	// tdscBlockPart.setBlockId(blockId);
	// tdscBlockPartDao.save(tdscBlockPart);
	// }
	// }
	// retMap.put("blockPartList", blockPartList);
	// //设置新的实体tdscBlockQqjcInfo的blockId；
	// tdscBlockQqjcInfo.setBlockId(blockId);
	// //删除原有“前期监察信息”表信息，再新增
	// if(blockId!=null && !"".equals(blockId)){
	// TdscBlockQqjcInfo QqjcInfoOld = (TdscBlockQqjcInfo)tdscBlockQqjcInfoDao.getTdscBlockQqjcInfo(blockId);
	// if(QqjcInfoOld!=null){
	// tdscBlockQqjcInfoDao.delete(QqjcInfoOld);
	// }
	// }
	// tdscBlockQqjcInfoDao.save(tdscBlockQqjcInfo);
	//        
	// try {
	// if ("tempSave".equals(saveType)) {
	// this.appFlowService.tempSaveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
	// } else if ("submitSave".equals(saveType)) {
	// tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
	// tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
	// this.appFlowService.saveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//        
	// return retMap;
	// }
	/**
	 * 
	 */
	public Map saveRemise(Map parameterMap) {

		TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) parameterMap.get("tdscBlockInfo");
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) parameterMap.get("tdscBlockTranApp");
		List saveMateriallist = (List) parameterMap.get("saveMateriallist");
		String saveType = (String) parameterMap.get("saveType");
		List blockPartList = (List) parameterMap.get("blockPartList");
		List tdscBlockRemisemoneyDefrayList = (List) parameterMap.get("tdscBlockRemisemoneyDefrayList");
		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) parameterMap.get("tdscBlockQqjcInfo");
		TdscEsClob tdscEsClob = (TdscEsClob) parameterMap.get("tdscEsClob");
		SysUser user = (SysUser) parameterMap.get("user");

		// 获得配建信息
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) parameterMap.get("tdscBlockPjxxInfo");
		// 保存配建信息
		if (StringUtils.isNotEmpty(tdscBlockPjxxInfo.getPjxxInfoId())) {
			tdscBlockPjxxInfoDao.update(tdscBlockPjxxInfo);
		} else {
			tdscBlockPjxxInfo.setPjxxInfoId(null);
			tdscBlockPjxxInfoDao.save(tdscBlockPjxxInfo);
		}

		// 保存"规划设计要点"
		tdscEsClob = (TdscEsClob) tdscEsClobDao.saveOrUpdate(tdscEsClob);
		// tdscEsClob.setClobContent(Base64.encode(tdscEsClob.getClobContent()));
		// 构造保存返回值的MAP
		Map retMap = new HashMap();
		// 保存土地基本信息
		String blockId = tdscBlockInfo.getBlockId();
		if (StringUtils.isNotBlank(blockId)) {
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		} else {
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
			blockId = tdscBlockInfo.getBlockId();
		}
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		// tdscEsClob.setClobContent(StringUtil.ISO88591toGBK(tdscEsClob.getClobContent()));
		retMap.put("tdscEsClob", tdscEsClob);
		// 规划设计要点：页面显示的时候，显示文本内容
		tdscBlockTranApp.setBzGhsjyd(tdscEsClob.getClobId());
		// 通过保存TdscBlockInfo返回的BO取出主键 然后向TdscBlockTranApp设置外来键
		tdscBlockTranApp.setBlockId(blockId);
		String appId = tdscBlockTranApp.getAppId();
		if (StringUtils.isNotBlank(appId)) {
			tdscBlockTranAppDao.update(tdscBlockTranApp);
		} else {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
			appId = tdscBlockTranApp.getAppId();
		}

		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		// 配建信息
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);
		// 通过保存TdscBlockTranApp返回的BO取出主键 然后向TdscBlockMaterial设置外来键
		deleteTdscBlockMaterial(appId);
		if (saveMateriallist != null && saveMateriallist.size() > 0) {
			List rList = new ArrayList();
			for (int i = 0; i < saveMateriallist.size(); i++) {
				TdscBlockMaterial tdscBlockMaterial = (TdscBlockMaterial) saveMateriallist.get(i);
				tdscBlockMaterial.setAppId(appId);

				if (tdscBlockMaterial != null) {
					tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialDao.save(tdscBlockMaterial);
					rList.add(tdscBlockMaterial);
				}
			}
		}
		// 删除原有子地块信息
		delBlockPartByBlockId(blockId);
		// 新增子地块信息
		if (blockPartList != null && blockPartList.size() > 0) {
			for (int j = 0; j < blockPartList.size(); j++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);
				tdscBlockPart.setBlockId(blockId);
				tdscBlockPartDao.save(tdscBlockPart);
			}
		}
		retMap.put("blockPartList", blockPartList);

		// 删除原有的地块出让金支付信息
		delTdscBlockRemisemoneyDefrayByBlockId(blockId);
		// 新增地块出让金支付信息
		if (tdscBlockRemisemoneyDefrayList != null && tdscBlockRemisemoneyDefrayList.size() > 0) {
			for (int j = 0; j < tdscBlockRemisemoneyDefrayList.size(); j++) {
				TdscBlockRemisemoneyDefray tdscBlockRemisemoneyDefray = (TdscBlockRemisemoneyDefray) tdscBlockRemisemoneyDefrayList.get(j);
				tdscBlockRemisemoneyDefray.setBlockId(blockId);
				tdscBlockRemisemoneyDefrayDao.save(tdscBlockRemisemoneyDefray);
			}
		}
		retMap.put("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

		// 设置新的实体tdscBlockQqjcInfo的blockId；
		tdscBlockQqjcInfo.setBlockId(blockId);
		// 删除原有“前期监察信息”表信息，再新增
		if (blockId != null && !"".equals(blockId)) {
			TdscBlockQqjcInfo QqjcInfoOld = (TdscBlockQqjcInfo) tdscBlockQqjcInfoDao.getTdscBlockQqjcInfo(blockId);
			if (QqjcInfoOld != null) {
				tdscBlockQqjcInfoDao.delete(QqjcInfoOld);
			}
		}
		tdscBlockQqjcInfoDao.save(tdscBlockQqjcInfo);

		// 保存 意向人信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) parameterMap.get("tdscBidderApp");
		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) parameterMap.get("tdscBidderPersonApp");
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockTranApp.getTransferMode())
				&& GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(tdscBlockTranApp.getIsPurposeBlock())) {
			// 如果为有意向受让人
			if (StringUtils.isNotEmpty(tdscBidderPersonApp.getBidderPersonId())) {
				// 只需要更新bidderperson表
				TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) this.tdscBidderPersonAppDao.get(tdscBidderPersonApp.getBidderPersonId());
				bidderPersonApp.setBidderName(tdscBidderPersonApp.getBidderName());
				bidderPersonApp.setOrgNo(tdscBidderPersonApp.getOrgNo());
				bidderPersonApp = (TdscBidderPersonApp) this.tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);
			} else {
				// 新增bidderapp 新增 bidderpersonapp
				tdscBidderApp.setAppId(appId);
				tdscBidderApp.setBidderId(null);
				tdscBidderApp = (TdscBidderApp) this.tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
				tdscBidderPersonApp.setBidderPersonId(null);
				tdscBidderPersonApp.setBidderId(tdscBidderApp.getBidderId());
				// 如果是已经存在的意向人，取出原有意向人所选择的 appid +“,” ， 再 set 到 purposeAppId 中
				tdscBidderPersonApp.setPurposeAppId(appId);
				tdscBidderPersonApp = (TdscBidderPersonApp) this.tdscBidderPersonAppDao.saveOrUpdate(tdscBidderPersonApp);
			}
		} else {
			TdscBidderApp getTdscBidderApp = this.getTdscBidderAppByAppId(appId);
			if (getTdscBidderApp != null) {
				TdscBidderPersonApp getbidderPersonApp = this.getTdscBidderPersonAppByBidderId(getTdscBidderApp.getBidderId());
				if (getbidderPersonApp != null) {
					this.tdscBidderPersonAppDao.deleteById(getbidderPersonApp.getBidderPersonId());
				}
				this.tdscBidderAppDao.deleteById(getTdscBidderApp.getBidderId());
			}
		}
		try {
			if ("tempSave".equals(saveType)) {
				this.appFlowService.tempSaveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			} else if ("submitSave".equals(saveType)) {
				tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
				tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
				this.appFlowService.saveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retMap;
	}

	public void saveBlockInfo(TdscBlockInfo tdscBlockInfo) {
		this.tdscBlockInfoDao.update(tdscBlockInfo);
	}

	/**
	 * 保存受理
	 * 
	 * @param tdscBlockInfo
	 * @param tdscBlockTranApp
	 * @param tdscBlockConInfo
	 * @param tdscBlockUsedInfo
	 */
	public void saveAccept(TdscBlockInfo tdscBlockInfo, TdscBlockTranApp tdscBlockTranApp, TdscBlockUsedInfo tdscBlockUsedInfo, String saveType, SysUser user) {

		String blockId = tdscBlockInfo.getBlockId();
		if (blockId != null) {
			tdscBlockInfoDao.update(tdscBlockInfo);
		} else {
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
			blockId = tdscBlockInfo.getBlockId();
		}

		tdscBlockUsedInfo.setBlockId(blockId);
		String blockUsedId = tdscBlockUsedInfo.getBlockUsedId();
		if (blockUsedId != null) {
			tdscBlockUsedInfoDao.update(tdscBlockUsedInfo);
		} else {
			tdscBlockUsedInfoDao.save(tdscBlockUsedInfo);
		}

		tdscBlockTranApp.setBlockId(blockId);
		String appId = tdscBlockTranApp.getAppId();
		if (appId != null) {
			tdscBlockTranAppDao.update(tdscBlockTranApp);
		} else {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
		}

		try {
			if ("tempSave".equals(saveType)) {
				this.appFlowService.tempSaveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			} else if ("submitSave".equals(saveType)) {
				this.appFlowService.saveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存审核
	 * 
	 * @param tdscBlockInfo
	 * @param tdscBlockTranApp
	 * @return
	 */
	public Map saveCheck(TdscBlockInfo tdscBlockInfo, TdscBlockTranApp tdscBlockTranApp) {
		Map retMap = new HashMap();
		tdscBlockInfo = (TdscBlockInfo) this.tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		tdscBlockTranApp = (TdscBlockTranApp) this.tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		return retMap;
	}

	/**
	 * 查询土地使用信息列表
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryTdscBlockUsedInfoListByBlockId(String blockId) {
		return tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
	}

	/**
	 * 删除appId指定的TdscBlockMaterial的信息
	 * 
	 * @param appId
	 */
	public void deleteTdscBlockMaterial(String appId) {
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List list = tdscBlockMaterialDao.findMaterialList(tdscBlockMaterialCondition);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				tdscBlockMaterialDao.delete(list.get(i));
			}
		}
	}

	/**
	 * 获得TdscBlockMaterial的列表
	 * 
	 * @param condition
	 * @return
	 */
	public List getTdscBlockMaterialList(TdscBlockMaterialCondition condition) {
		return tdscBlockMaterialDao.findMaterialList(condition);
	}

	/**
	 * 根据appId查询材料列表
	 * 
	 * @param appId
	 * @return
	 */
	public List findMaterialListByAppId(String appId) {
		return tdscBlockMaterialDao.findMaterialListByAppId(appId);
	}

	/**
	 * 获得TdscBlockMaterial的列表
	 * 
	 * @param condition
	 * @return
	 */
	public List getAppList(String blockId) {
		return tdscBlockTranAppDao.findAppListByBlockId(blockId);
	}

	public List queryAppList(TdscBaseQueryCondition condition) {
		try {
			return tdscBlockInfoDao.queryAppList(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得土地基本信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getTdscBlockUsedInfoList(TdscBlockUsedInfoCondition condition) {
		return tdscBlockUsedInfoDao.findPageList(condition);
	}

	public TdscBlockUsedInfo findTdscBlockUsedInfo(String blockUsedId) {
		// 土地用途信息查询
		return tdscBlockUsedInfoDao.findTdscBlockUsedInfo(blockUsedId);
	}

	// 写入挂牌成交信息
	public void modifyStatus(String appId, BigDecimal topPrice) {
		// 交易信息表
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscBlockTranAppDao tdscBlockTranAppDao = new TdscBlockTranAppDao();
		tdscBlockTranApp.setAppId(appId);
		// 查出对应地块Id
		tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(tdscBlockTranApp);
		String blockId = tdscBlockTranApp.getBlockId();
		// 交易信息表中存入价格
		tdscBlockTranApp.setResultPrice(topPrice);
		tdscBlockTranAppDao.save(tdscBlockTranApp);
		// 地块信息表
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		tdscBlockInfo.setBlockId(blockId);
		tdscBlockInfo.setResultPrice(topPrice);
		tdscBlockInfo.setStatus("02");
		TdscBlockConInfoDao tdscBlockConInfoDao = new TdscBlockConInfoDao();
		tdscBlockConInfoDao.save(tdscBlockInfo);
	}

	/**
	 * 查询指定appId的土地交易信息
	 * 
	 * @param appId
	 * @return
	 */
	public TdscBlockTranApp findTdscBlockTranApp(String appId) {
		return tdscBlockTranAppDao.findTdscBlockTranApp(appId);
	}

	/**
	 * 查询对应blockId的土地基本信息
	 */
	public TdscBlockInfo findTdscBlockInfo(String blockId) {
		return tdscBlockInfoDao.findBlockInfo(blockId);
	}

	/**
	 * 上传附件
	 * 
	 * @param upLoadFile
	 * @return
	 */
	public String upLoadFile(FormFile upLoadFile) {
		return tdscBlockInfoDao.upLoadFile(upLoadFile);
	}

	/** *********************add by huangjiawei************************** */
	/**
	 * 通过APPID返回TdscBlockTranApp
	 */
	public TdscBlockTranApp getTdscBlockTranAppByAppId(String appId) {
		return (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
	}

	/**
	 * 通过BLOCKID返回TdscBlockInfo
	 */
	public TdscBlockInfo getTdscBlockInfoByBlockId(String blockId) {
		return (TdscBlockInfo) this.tdscBlockInfoDao.get(blockId);
	}

	/**
	 * 修改交易结果后存如Tdsc_Block_Tran_App表
	 */
	public void updateTdscBlockTranApp(TdscBlockTranApp tdscBlockTranApp) {
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);
	}

	/**
	 * 修改交易结果后存如Tdsc_Block_Info表
	 */
	public void updateTdscBlockInfo(TdscBlockInfo tdscBlockInfo) {
		this.tdscBlockInfoDao.update(tdscBlockInfo);
	}
	public void updateTdscBidderApp(TdscBidderApp tdscBidderApp) {
		this.tdscBidderAppDao.update(tdscBidderApp);
	}
	public void updateTdscBidderPersonApp(TdscBidderPersonApp tdscBidderPersonApp) {
		this.tdscBidderPersonAppDao.update(tdscBidderPersonApp);
	}
	public void updateTdscListingInfo(TdscListingInfo tdscListingInfo) {
		this.tdscListingInfoDao.update(tdscListingInfo);
	}

	/**
	 * 保存挂牌交易结果
	 * 
	 * @param tdscBlockTranApp
	 * @param tdscBlockInfo
	 */
	public void updateListingTranResult(TdscBlockTranApp tdscBlockTranApp, TdscBlockInfo tdscBlockInfo, TdscListingInfo tdscListingInfo) {
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);
		this.tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		if (null != tdscListingInfo) {
			this.tdscListingInfoDao.saveOrUpdate(tdscListingInfo);
		}
	}

	/** *********************add by huangjiawei************************** */

	/**
	 * 将土地审批系统传过来的土地信息保存的数据库 TdscBlockInfo.java TdscBlockUsedInfo.java
	 * 
	 * author:weedlu 20071222
	 * 
	 * @param tdscBlockInfo
	 */
	public void addAuditedBlockInfo(TdscBlockInfo tdscBlockInfo) {
		// 1、设置土地状态
		tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_NOTTRADE);

		// 2、将地块基本信息中的文件信息保存到文件夹，将文件路径保存数据库
		// String path = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.DIRECTORY_RECEIPT_AUDITED);
		// 用地批文
		if (tdscBlockInfo.getLandAuditedDoc() != null) {
			// String landAuditedFileName = GlobalConstants.LAND_AUDITED_PREFIX + "_" + System.currentTimeMillis() + ".doc";
			// FileUtils.saveStringToFile(tdscBlockInfo.getLandAuditedDoc(), path, landAuditedFileName);
			//
			// tdscBlockInfo.setLandAuditedDoc(landAuditedFileName);

			TdscEsClob clob = new TdscEsClob();
			clob.setClobContent(tdscBlockInfo.getLandAuditedDoc());
			clob = (TdscEsClob) tdscEsClobDao.save(clob);
			tdscBlockInfo.setLandAuditedDoc(clob.getClobId());
		}
		// 供地方案
		if (tdscBlockInfo.getSupplyCaseDoc() != null) {
			// String supplyCaseFileName = GlobalConstants.SUPPLY_CASE_PREFIX + "_" + System.currentTimeMillis() + ".doc";
			// FileUtils.saveStringToFile(tdscBlockInfo.getSupplyCaseDoc(), path, supplyCaseFileName);
			//
			// tdscBlockInfo.setSupplyCaseDoc(supplyCaseFileName);

			TdscEsClob clob = new TdscEsClob();
			clob.setClobContent(tdscBlockInfo.getSupplyCaseDoc());
			clob = (TdscEsClob) tdscEsClobDao.save(clob);
			tdscBlockInfo.setSupplyCaseDoc(clob.getClobId());
		}

		// 3、计算建筑总面积、整理土地类型
		List usedInfoList = tdscBlockInfo.getUsedInfoList();
		if (usedInfoList != null && usedInfoList.size() > 0) {
			double totalBuildingArea = 0.0;
			List blockTypeList = new ArrayList();
			for (int i = 0; i < usedInfoList.size(); i++) {
				TdscBlockUsedInfo usedInfo = (TdscBlockUsedInfo) usedInfoList.get(i);

				// 计算建筑总面积
				double buildingArea = usedInfo.getBuildingArea() == null ? 0.0 : usedInfo.getBuildingArea().doubleValue();
				totalBuildingArea += buildingArea;

				// 整理土地类型
				if (usedInfo.getLandUseType() != null) {
					Map landUseMap = DicDataUtil.getInstance().getComplexDicItemMap(GlobalConstants.DIC_ID_PLAN_COMPLEX_USE, usedInfo.getLandUseType());
					String blockType = null;
					if (landUseMap != null)
						blockType = (String) landUseMap.get("DIC_DESCRIBE");

					if (blockType != null && !blockTypeList.contains(blockType))
						blockTypeList.add(blockType);
				}

			}
			// 计算建筑总面积
			tdscBlockInfo.setTotalBuildingArea(new BigDecimal(totalBuildingArea));

			// 整理土地类型
			if (blockTypeList.size() == 1) {
				tdscBlockInfo.setBlockType((String) blockTypeList.get(0));
			} else {
				logger.warn("======土地审批系统的土地用途转换为土地类型有误======");
				logger.warn(blockTypeList);
			}
		}

		// 4、地块基本信息表
		TdscBlockInfo savedBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);

		// 5、地块用途信息表
		List usedInfoList2 = savedBlockInfo.getUsedInfoList();
		if (usedInfoList2 != null && usedInfoList2.size() > 0) {
			for (int i = 0; i < usedInfoList2.size(); i++) {
				TdscBlockUsedInfo usedInfo = (TdscBlockUsedInfo) usedInfoList2.get(i);
				usedInfo.setBlockId(savedBlockInfo.getBlockId());

				tdscBlockUsedInfoDao.save(usedInfo);
			}
		}

	}

	public TdscBlockInfo getTdscBlockInfoByAuditedNum(String auditedNum) {
		return tdscBlockInfoDao.getTdscBlockInfoByAuditedNum(auditedNum);
	}

	public List getTdscBlockInfoListByAuditedNum(String[] auditedNum) {
		return tdscBlockInfoDao.getTdscBlockInfoListByAuditedNum(auditedNum);
	}

	public List getTdscBlockInfoListByAuditedNum(String auditedNum) {
		return tdscBlockInfoDao.getTdscBlockInfoListByAuditedNum(auditedNum);
	}

	/**
	 * 保存双杨提供的土地合同信息
	 * 
	 * @param tdscBlockConInfo
	 */
	public void addBlockConInfo(TdscBlockConInfo tdscBlockConInfo) {
		this.tdscBlockConInfoDao.save(tdscBlockConInfo);
	}

	/**
	 * 修改实体的 发布状态并记录发布时间
	 * 
	 * @param appId
	 */
	public void modIfPublish(String appId, String transferMode, SysUser user) {
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		// 设置发布状态
		tdscBlockTranApp.setIfPublish("1");
		// 记录发布时间
		Date publishDate = new Date(System.currentTimeMillis());
		tdscBlockTranApp.setPublishDate(publishDate);
		tdscBlockTranAppDao.update(tdscBlockTranApp);
		// 推动流程
		try {
			appFlowService.saveOpnn(appId, transferMode, user);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 根据noticeId，noitceNo查询TdscBlockTranApp列表
	 * 
	 * @param noticeId
	 * @param noitceNo
	 * @return
	 */
	public List findTdscBlockTranAppByNoticeNo(String noticeId, String noitceNo) {
		return tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, noitceNo);
	}

	/**
	 * 根据PlanId查询TdscBlockTranApp列表
	 * 
	 * @param planId
	 * @return
	 */
	public List findTdscBlockTranAppByPlanId(String planId) {
		if (StringUtils.isEmpty(planId))
			return null;
		return tdscBlockTranAppDao.findTranAppList(planId);
	}

	/**
	 * 整理规划用途 包括子地块信息
	 * 
	 * @param tdscBlockAppView
	 * @return
	 */

	public String getLandUseTypeByBlockId(TdscBlockAppView tdscBlockAppView) {

		List tempList = (List) tdscBlockPartDao.getTdscBlockPartList(tdscBlockAppView.getBlockId());
		StringBuffer tmpUseType = new StringBuffer("");
		if (tempList != null && tempList.size() > 0) {

			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			TdscBlockPart tempTdscBlockPart = new TdscBlockPart();
			for (int i = 0; i < tempList.size(); i++) {
				tdscBlockPart = (TdscBlockPart) tempList.get(i);
				// 规划用途 子地块用途
				List tempTdscBlockPartlist = new ArrayList();
				for (int j = 0; j < tempList.size(); j++) {
					tempTdscBlockPart = (TdscBlockPart) tempList.get(j);
					if (tempTdscBlockPart.getLandUseType().equals(tdscBlockPart.getLandUseType())) {
						tempTdscBlockPartlist.add(tempTdscBlockPart);
						tempList.remove(tempTdscBlockPart);
						j--;
					}
				}
				i--;
				if (tempTdscBlockPartlist != null && tempTdscBlockPartlist.size() > 0) {
					tmpUseType.append("；");
					if (StringUtils.isNotEmpty(tdscBlockPart.getLandUseType())) {
						tmpUseType.append(DicDataUtil.getInstance().getDic(GlobalConstants.DIC_LAND_CODETREE_T).get(tdscBlockPart.getLandUseType()));
					}
				}
			}
		}

		return tmpUseType.toString();
	}

	/**
	 * 整理规划用途 包括子地块信息
	 * 
	 * @param tdscBlockAppView
	 * @return
	 */

	public TdscBlockAppView tidyTdscBlockAppViewByBlockId(String appId) {
		if (appId == null)
			return null;

		TdscBlockAppView blockAppView = new TdscBlockAppView();
		// 地块交易信息
		TdscBlockTranApp blockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);

		// 地块基本信息
		TdscBlockInfo blockinfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockTranApp.getBlockId());

		// 子地块信息
		List blockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(blockinfo.getBlockId());

		// 双杨地块信息
		List blockUsedInfoList = (List) tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockinfo.getBlockId());

		// ===出让年限整理===
		if ("101".equals(blockinfo.getBlockType())) {
			blockinfo.setTransferLife("50年");
		}

		// ===0.以下为地块用途整理===
		StringBuffer tmpUsedAll = new StringBuffer("");
		if (blockUsedInfoList != null && blockUsedInfoList.size() > 0)
			for (int i = 0; i < blockUsedInfoList.size(); i++) {
				TdscBlockUsedInfo blockUsedInfo = (TdscBlockUsedInfo) blockUsedInfoList.get(i);
				tmpUsedAll.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_ID_PLAN_USE, blockUsedInfo.getLandUseType())).append("，");
			}

		// 去掉末尾的“，”号
		if (!"".equals(tmpUsedAll.toString()))
			blockAppView.setTempStr2(tmpUsedAll.substring(0, tmpUsedAll.length() - 1));

		// ===1.以下为子地块信息整理===
		// 临时变量
		StringBuffer tmpRangeSide = new StringBuffer("");
		StringBuffer tmpUseType = new StringBuffer("");
		StringBuffer tmpVolumeRate = new StringBuffer("");
		StringBuffer tmpTransferLife = new StringBuffer("");
		StringBuffer tmpGreenRate = new StringBuffer("");
		StringBuffer tmpArea = new StringBuffer("");

		if (blockPartList != null && blockPartList.size() > 0)
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
				// 子地块名称
				String blockName = "P" + i;
				if (StringUtils.isNotEmpty(blockPart.getBlockName())) {
					blockName = blockPart.getBlockName();
				}

				// 四至范围 总地块四至范围＋分地块四至范围
				tmpRangeSide.append(blockName).append("：");
				if (StringUtils.isNotEmpty(blockPart.getRangeEast())) {
					tmpRangeSide.append("东至").append(blockPart.getRangeEast()).append("，");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeSouth())) {
					tmpRangeSide.append("南至").append(blockPart.getRangeSouth()).append("，");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeWest())) {
					tmpRangeSide.append("西至").append(blockPart.getRangeWest()).append("，");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeNorth())) {
					tmpRangeSide.append("北至").append(blockPart.getRangeNorth()).append("，");
				}

				// 去掉“，”
				if ("，".equals(tmpRangeSide.substring(tmpRangeSide.length() - 1)))
					tmpRangeSide = new StringBuffer(tmpRangeSide.substring(0, tmpRangeSide.length() - 1));

				tmpRangeSide.append("；");

				// 去掉“：；”
				if (tmpRangeSide.indexOf("：；") > 0)
					tmpRangeSide = new StringBuffer("");

				// 规划用途 子地块用途
				tmpUseType.append(blockName).append("：");
				if (StringUtils.isNotEmpty(blockPart.getLandUseType())) {
					tmpUseType.append(blockPart.getLandUseType());
				}
				tmpUseType.append("；");

				// 去掉“：；”
				if (tmpUseType.indexOf("：；") > 0)
					tmpUseType = new StringBuffer("");

				// 容积率 子地块
				tmpVolumeRate.append(blockName).append("：");
				if (StringUtils.isNotEmpty(blockPart.getVolumeRate())) {
					tmpVolumeRate.append(blockPart.getVolumeRate());
				}
				tmpVolumeRate.append("；");

				// 去掉“：；”
				if (tmpVolumeRate.indexOf("：；") > 0)
					tmpVolumeRate = new StringBuffer("");

				// 绿化率
				tmpGreenRate.append(blockName).append(":");
				if (StringUtils.isNotEmpty(blockPart.getGreeningRate())) {
					tmpGreenRate.append(blockPart.getGreeningRate());
				}
				tmpGreenRate.append("；");

				// 去掉“：；”
				if (tmpGreenRate.indexOf("：；") > 0)
					tmpGreenRate = new StringBuffer("");

				// 出让面积 子地块
				tmpArea.append(blockName).append("：");
				if (blockPart.getTotalLandArea() != null) {
					tmpArea.append(blockPart.getTotalLandArea());
				}
				tmpArea.append("；");

				// 去掉“：；”
				if (tmpArea.indexOf("：；") > 0)
					tmpArea = new StringBuffer("");

				// 出让年限 子地块
				tmpTransferLife.append(blockName).append("：");
				if (StringUtils.isNotEmpty(blockPart.getTransferLife())) {
					tmpTransferLife.append(blockPart.getTransferLife());
				}
				tmpTransferLife.append("；");

				// 去掉“：；”
				if (tmpTransferLife.indexOf("：；") > 0)
					tmpTransferLife = new StringBuffer("");

			}

		// ===========四至范围 总地块四至范围＋分地块四至范围========
		StringBuffer sbTmp = new StringBuffer("总范围：");

		if (StringUtils.isNotEmpty(blockinfo.getRangeEast())) {
			sbTmp.append("东至").append(blockinfo.getRangeEast()).append("，");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeSouth())) {
			sbTmp.append("南至").append(blockinfo.getRangeSouth()).append("，");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeWest())) {
			sbTmp.append("西至").append(blockinfo.getRangeWest()).append("，");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeNorth())) {
			sbTmp.append("北至").append(blockinfo.getRangeNorth()).append("，");
		}

		// 去掉“，”
		if ("，".equals(sbTmp.substring(sbTmp.length() - 1)))
			sbTmp = new StringBuffer(sbTmp.substring(0, sbTmp.length() - 1));

		sbTmp.append("；");

		// 去掉“：；”
		if (sbTmp.indexOf("：；") > 0)
			sbTmp = new StringBuffer("");

		if (!"".equals(tmpRangeSide.toString()))
			sbTmp.append(tmpRangeSide);

		if (!"".equals(sbTmp.toString()))
			blockAppView.setRangeEast(sbTmp.toString());

		// ==========规划用途 子地块用途===========
		if (!"".equals(tmpUseType.toString()))
			blockAppView.setTempStr2(tmpUseType.toString());

		// ==========容积率 子地块============
		if (!"".equals(tmpVolumeRate.toString())) {
			blockAppView.setVolumeRate(tmpVolumeRate.toString());
		} else {
			blockAppView.setVolumeRate(blockinfo.getVolumeRate());
		}

		// =========出让年限 子地块=======
		if (!"".equals(tmpTransferLife.toString())) {
			blockAppView.setTransferLife(tmpTransferLife.toString());
		} else {
			blockAppView.setTransferLife(blockinfo.getTransferLife());
		}

		// =========绿化率 子地块=======
		if (!"".equals(tmpGreenRate.toString())) {
			blockAppView.setGreeningRate(tmpGreenRate.toString());
		} else {
			blockAppView.setGreeningRate(blockinfo.getGreeningRate());
		}

		if (!"".equals(tmpArea.toString())) {
			blockAppView.setTempStr(tmpArea.toString());
		} else {
			if (blockinfo.getTotalBuildingArea() != null)
				blockAppView.setTempStr("" + blockinfo.getTotalBuildingArea());
		}

		blockAppView.setBlockName(blockinfo.getBlockName());

		blockAppView.setFlatRate(blockinfo.getFlatRate());

		blockAppView.setBlockNoticeNo(blockTranApp.getBlockNoticeNo());
		blockAppView.setResultName(blockTranApp.getResultName());
		blockAppView.setResultDate(blockTranApp.getResultDate());
		if (blockTranApp.getResultPrice() != null)
			blockAppView.setResultPrice(blockTranApp.getResultPrice());
		blockAppView.setTransferMode(blockTranApp.getTransferMode());
		blockAppView.setAccessProductType(blockTranApp.getAccessProductType());

		return blockAppView;
	}

	/**
	 * 根据blockId删除子地块信息
	 * 
	 * @param blockId
	 */
	public void delBlockPartByBlockId(String blockId) {
		// 查询原有信息LIST
		List oldBlockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
		if (oldBlockPartList != null && oldBlockPartList.size() > 0) {
			for (int i = 0; i < oldBlockPartList.size(); i++) {
				tdscBlockPartDao.delete(oldBlockPartList.get(i));
			}
		}
	}

	/**
	 * 根据blockId删除该地块的出让金支付信息
	 * 
	 * @param blockId
	 */
	public void delTdscBlockRemisemoneyDefrayByBlockId(String blockId) {
		// 查询原有信息LIST
		List oldtdscBlockRemisemoneyDefrayList = (List) tdscBlockRemisemoneyDefrayDao.getTdscBlockRemisemoneyDefrayList(blockId);
		if (oldtdscBlockRemisemoneyDefrayList != null && oldtdscBlockRemisemoneyDefrayList.size() > 0) {
			for (int i = 0; i < oldtdscBlockRemisemoneyDefrayList.size(); i++) {
				tdscBlockRemisemoneyDefrayDao.delete(oldtdscBlockRemisemoneyDefrayList.get(i));
			}
		}
	}

	/**
	 * 根据blockId删除地块及子地块信息
	 * 
	 * @param blockId
	 */
	public void delBlockInfoByBlockId(String strAppId, String blockId) {
		tdscBlockInfoDao.deleteById(blockId);
		this.delBlockPartByBlockId(blockId);
		tdscBlockTranAppDao.deleteById(strAppId);
		tdscAppDao.deleteById(strAppId);
	}

	public List getTdscBlockQqjcInfoList(String blockId) {
		return (List) tdscBlockQqjcInfoDao.getTdscBlockQqjcInfoList(blockId);
	}

	public TdscBlockQqjcInfo getQqjcInfoByQqjcInfoId(String qqjcInfoId) {
		return (TdscBlockQqjcInfo) tdscBlockQqjcInfoDao.get(qqjcInfoId);
	}

	public TdscBlockQqjcInfo getQqjcInfoByblockId(String blockId) {
		return (TdscBlockQqjcInfo) tdscBlockQqjcInfoDao.getTdscBlockQqjcInfo(blockId);
	}

	/**
	 * 将json字符串返回MAP
	 * 
	 * @param jobDataMap
	 * @return
	 * @throws JSONException
	 */
	public XmlJobDataMap getXmlJobDataMap(String jobDataMap) throws JSONException {
		XmlJobDataMap dataMap = null;
		if (StringUtils.isNotEmpty(jobDataMap)) {
			JSONObject jsonObject = new JSONObject(jobDataMap);
			Iterator iter = jsonObject.keys();
			List entryList = new ArrayList();
			while (iter.hasNext()) {
				String key = (String) iter.next();

				XmlEntry entry = new XmlEntry();

				entry.setKey(key);
				// Note:converter object type in jobs;
				entry.setValue(jsonObject.getString(key));

				entryList.add(entry);
			}

			dataMap = new XmlJobDataMap();
			dataMap.setEntryList(entryList);
		}

		return dataMap;
	}

	public Map getDataMap(String jobDataMap) throws JSONException {
		Map dataMap = null;
		if (StringUtils.isNotEmpty(jobDataMap)) {
			JSONObject jsonObject = new JSONObject(jobDataMap);
			Iterator iter = jsonObject.keys();
			dataMap = new HashMap();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				dataMap.put(key, jsonObject.getString(key));
			}
		}
		return dataMap;
	}

	public Map tidyMap(String jobDataMap) throws JSONException {
		if (StringUtils.isNotEmpty(jobDataMap)) {
			String tempStr = jobDataMap.substring(1, jobDataMap.length() - 1);
			Map tidyMap = (Map) getDataMap(tempStr);
			return tidyMap;
		}
		return null;
	}

	/*
	 * 
	 * public JSONArray tidyMap(String jobDataMap) throws JSONException{ return new JSONArray(jobDataMap); } public Map tidyMap(String jobDataMap) throws JSONException{ if
	 * (StringUtils.isNotEmpty(jobDataMap)) { String tempStr = jobDataMap.substring(1, jobDataMap.length()-1); Map tidyMap =(Map) getDataMap(tempStr); return tidyMap; } return
	 * null; }
	 */

	/**
	 * 根据blockId整理地块编号，不重复
	 */
	public String tidyBlockCodeByBlockId(String blockId) {
		List tdscBlockPartList = (List) tdscBlockPartDao.getDistinctBlockPartList(blockId);
		String retString = "";
		if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
			StringBuffer retBuffer = new StringBuffer();
			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					// TdscBlockPart tdscBlockPart = (TdscBlockPart)tdscBlockPartList.get(i);
					// retBuffer.append(tdscBlockPart.getBlockCode()).append(",");
					retBuffer.append(tdscBlockPartList.get(i)).append("、");
				}
			}
			if (retBuffer != null) {
				retString = retBuffer.substring(0, retBuffer.length() - 1);
			}
		}
		return retString;
	}

	/**
	 * 根据blockId整理所有不重复土地用途
	 */
	public String tidyLandUseTypeByBlockId(String blockId) {
		List objList = (List) tdscBlockPartDao.getDistinctUseTypeAndTransferLifeList(blockId);
		StringBuffer retBuffer = new StringBuffer();
		String retString = "";
		if (objList != null && objList.size() > 0) {
			for (int i = 0; i < objList.size(); i++) {
				Object[] obj = (Object[]) objList.get(i);
				if (obj[0] != null && !"".equals(obj[0]))
					retBuffer.append(obj[0].toString()).append("、");
			}
		}

		if (retBuffer != null && !"".equals(retBuffer)) {
			retString = retBuffer.substring(0, retBuffer.length() - 1);
		}
		return retString;
	}

	/**
	 * 根据blockId整理所有不重复 土地用途+出让年限+“年”
	 */
	public String tidyUseTypeAndTransferLife(String blockId) {
		List objList = (List) tdscBlockPartDao.getDistinctUseTypeAndTransferLifeList(blockId);
		StringBuffer retBuffer = new StringBuffer();
		String retString = "";
		if (objList != null && objList.size() > 0) {
			for (int i = 0; i < objList.size(); i++) {
				Object[] obj = (Object[]) objList.get(i);
				if (obj[0] != null && !"".equals(obj[0]))
					retBuffer.append(obj[0].toString());
				if (obj[1] != null && !"".equals(obj[1])) {
					retBuffer.append(obj[1].toString()).append("年、");
				}
			}
		}

		if (retBuffer != null && !"".equals(retBuffer)) {
			retString = retBuffer.substring(0, retBuffer.length() - 1);
		}
		return retString;
	}

	public TdscBlockTranApp reTidyTranApp(TdscBlockTranApp tdscBlockTranApp) {
		if (tdscBlockTranApp != null) {
			TdscEsClob clob_1 = (TdscEsClob) tdscEsClobDao.get(tdscBlockTranApp.getBzGhsjyd());
			if (clob_1 != null) {
				tdscBlockTranApp.setBzGhsjyd(clob_1.getClobContent());
			}
		}
		return tdscBlockTranApp;
	}

	public TdscEsClob getTdscEsClob(String clobId) {
		TdscEsClob tdscEsClob = new TdscEsClob();
		if (!"".equals(StringUtils.trimToEmpty(clobId))) {
			tdscEsClob = (TdscEsClob) tdscEsClobDao.get(clobId);
			if (tdscEsClob == null) {
				tdscEsClob = new TdscEsClob();
			}
		}
		return tdscEsClob;
	}

	/**
	 * 为出让文件和公告最上面的信息表构造指定格式数据
	 * 
	 * @param blockidList
	 */
	public List makeDataForFile(List blockidList) {

		// 子地块信息查询列表
		List returnPartList = new ArrayList();

		if (blockidList != null && blockidList.size() > 0) {
			for (int n = 0; n < blockidList.size(); n++) {
				String blockId2 = (String) blockidList.get(n);
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) this.getTdscBlockInfoByBlockId(blockId2);
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getTdscBlockTranAppInfo(blockId2);

				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					String blockCodes = this.tidyBlockCodeByBlockId(blockId2);
					if (blockCodes != null && blockCodes.length() > 0) {
						String[] blockCodes2 = StringUtils.split(blockCodes, "、");
						for (int a = 0; a < blockCodes2.length; a++) {
							String blockCode = blockCodes2[a];
							List partList = this.getblockPartListByBlockCode(blockId2, blockCode);

							if (partList != null && partList.size() > 1) {

								BigDecimal allBlockArea = new BigDecimal(0);// 地块面积之和
								BigDecimal allBlockBuildingArea = new BigDecimal(0);// 规划建筑面积之和
								for (int k = 0; k < partList.size(); k++) {
									TdscBlockPart tdscBlockPart = (TdscBlockPart) partList.get(k);
									TdscBlockPart retBlockPart = new TdscBlockPart();

									// 根据word的格式构造指定机构的数据
									// 每条地块的第一个子地块不能有空数据，否则全值为“——”
									if (k == 0) {
										allBlockArea = this.getAllBlockAreaByBlockCode(blockId2, blockCode);
										allBlockBuildingArea = this.getAllBlockBuildingAreaByBlockCode(blockId2, blockCode);

										// 序号存在BlockName，地块公告号
										retBlockPart.setBlockName(tdscBlockTranApp.getXuHao());
										retBlockPart.setBlockCode(tdscBlockPart.getBlockCode());
										// 地块位置保存在RangeEast中,地块公告号保存在RangeNorth中
										retBlockPart.setRangeEast(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()), "——"));
										retBlockPart.setRangeNorth(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()), "——"));
										// 土地用途,子地块总土地面积，出让年限
										retBlockPart.setLandUseType(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()), "——"));
										retBlockPart.setBlockArea(allBlockArea);
										retBlockPart.setTransferLife(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()), "——"));
										// 容积率,建筑密度,绿地率,
										retBlockPart.setVolumeRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()), "——"));
										retBlockPart.setDensity(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getDensity()), "——"));
										retBlockPart.setGreeningRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()), "——"));
										// 规划建筑总面积，保证金setMemo，起拍价setBlockDetailedUsed
										retBlockPart.setPlanBuildingArea(allBlockBuildingArea);
										retBlockPart.setMemo(NumberUtil.numberDisplay(tdscBlockTranApp.getMarginAmount(), 2));
										retBlockPart.setBlockDetailedUsed(NumberUtil.numberDisplay(tdscBlockTranApp.getInitPrice(), 2));

									} else {

										// 地块编号，土地用途,出让年限
										retBlockPart.setBlockCode("");
										retBlockPart.setLandUseType(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()));
										retBlockPart.setTransferLife(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()));
										// 容积率,建筑密度,绿地率,
										retBlockPart.setVolumeRate(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()));
										retBlockPart.setDensity(StringUtils.trimToEmpty(tdscBlockPart.getDensity()));
										retBlockPart.setGreeningRate(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()));

										// 容积率,建筑密度,绿地率,要跟上个比较
										TdscBlockPart shangBlockPart = (TdscBlockPart) partList.get(k - 1);
										// （1）如果不为空，并且和上个子地块值相同，则允许合并
										if (StringUtils.isNotEmpty(shangBlockPart.getVolumeRate()) && StringUtils.isNotEmpty(tdscBlockPart.getVolumeRate())
												&& StringUtils.trimToEmpty(shangBlockPart.getVolumeRate()).equals(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()))) {
											retBlockPart.setVolumeRate("");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getDensity()) && StringUtils.isNotEmpty(tdscBlockPart.getDensity())
												&& StringUtils.trimToEmpty(shangBlockPart.getDensity()).equals(StringUtils.trimToEmpty(tdscBlockPart.getDensity()))) {
											retBlockPart.setDensity("");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getGreeningRate()) && StringUtils.isNotEmpty(tdscBlockPart.getGreeningRate())
												&& StringUtils.trimToEmpty(shangBlockPart.getGreeningRate()).equals(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()))) {
											retBlockPart.setGreeningRate("");
										}
										// （2）如果为空，并且和上个子地块的值不为空，则不允许合并
										if (StringUtils.isNotEmpty(shangBlockPart.getVolumeRate()) && StringUtils.trimToEmpty(shangBlockPart.getVolumeRate()) != "——"
												&& StringUtils.isEmpty(tdscBlockPart.getVolumeRate())) {
											retBlockPart.setVolumeRate("——");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getDensity()) && StringUtils.trimToEmpty(shangBlockPart.getDensity()) != "——"
												&& StringUtils.isEmpty(tdscBlockPart.getDensity())) {
											retBlockPart.setDensity("——");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getGreeningRate()) && StringUtils.trimToEmpty(shangBlockPart.getGreeningRate()) != "——"
												&& StringUtils.isEmpty(tdscBlockPart.getGreeningRate())) {
											retBlockPart.setGreeningRate("——");
										}
									}

									// 土地面积，建筑面积
									retBlockPart.setRangeSouth(NumberUtil.numberDisplay(tdscBlockPart.getBlockArea(), 2) + "");
									retBlockPart.setRangeWest(NumberUtil.numberDisplay(tdscBlockPart.getPlanBuildingArea(), 2) + "");
									returnPartList.add(retBlockPart);
								}

								// 如果只有1个子地块，则土地面积和规划面积2个表格增加一个标示来合并
							} else if (partList != null && partList.size() == 1) {

								TdscBlockPart tdscBlockPart = (TdscBlockPart) partList.get(0);
								TdscBlockPart retBlockPart = new TdscBlockPart();

								// 序号存在BlockName，地块公告号
								retBlockPart.setBlockName(tdscBlockTranApp.getXuHao());
								retBlockPart.setBlockCode(tdscBlockPart.getBlockCode());
								// 地块位置保存在RangeEast中,地块公告号保存在RangeNorth中
								retBlockPart.setRangeEast(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()), "——"));
								retBlockPart.setRangeNorth(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()), "——"));
								// 土地用途,土地面积，子地块总土地面积存在BlockArea中，出让年限
								retBlockPart.setLandUseType(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()), "——"));
								retBlockPart.setRangeSouth("__" + NumberUtil.numberDisplay(tdscBlockPart.getBlockArea(), 2));
								retBlockPart.setBlockArea(tdscBlockPart.getBlockArea());
								retBlockPart.setTransferLife(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()), "——"));
								// 容积率,建筑密度,绿地率,
								retBlockPart.setVolumeRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()), "——"));
								retBlockPart.setDensity(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getDensity()), "——"));
								retBlockPart.setGreeningRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()), "——"));
								// 规划建筑面积，规划建筑总面积，保证金setMemo，起拍价setBlockDetailedUsed
								retBlockPart.setRangeWest("__" + NumberUtil.numberDisplay(tdscBlockPart.getPlanBuildingArea(), 2));
								retBlockPart.setPlanBuildingArea(tdscBlockPart.getPlanBuildingArea());
								retBlockPart.setMemo(NumberUtil.numberDisplay(tdscBlockTranApp.getMarginAmount(), 2));
								retBlockPart.setBlockDetailedUsed(NumberUtil.numberDisplay(tdscBlockTranApp.getInitPrice(), 2));

								returnPartList.add(retBlockPart);

							}
						}
					}
				}
			}
		}
		return returnPartList;
	}

	public static void setLogger(Logger logger) {
		TdscBlockInfoService.logger = logger;
	}

	/**
	 * 根据配建信息ID获得配件信息
	 * 
	 * @param pjxxInfoId
	 * @return
	 */
	public TdscBlockPjxxInfo getTdscBlockPjxxInfo(String pjxxInfoId) {
		return (TdscBlockPjxxInfo) tdscBlockPjxxInfoDao.get(pjxxInfoId);
	}

	/**
	 * 根据blockId获得配件信息
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockPjxxInfo getTdscBlockPjxxInfoByBlockId(String blockId) {
		return (TdscBlockPjxxInfo) tdscBlockPjxxInfoDao.getTdscBlockPjxxInfoByBlockId(blockId);
	}

	/**
	 * 根据appID 查找对应的bidderapp
	 * 
	 * @param appId
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppId(String appId) {
		List list = this.tdscBidderAppDao.findBidderAppListByAppId(appId);
		TdscBidderApp tdscBidderApp = null;
		if (list != null && list.size() > 0) {
			tdscBidderApp = (TdscBidderApp) list.get(0);
		}
		return tdscBidderApp;
	}

	/**
	 * 根据bidderId查找对应的personapp
	 * 
	 * @param bidderId
	 * @return
	 */
	public TdscBidderPersonApp getTdscBidderPersonAppByBidderId(String bidderId) {
		List list = this.tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
		TdscBidderPersonApp tdscBidderPersonApp = null;
		if (list != null && list.size() > 0) {
			tdscBidderPersonApp = (TdscBidderPersonApp) list.get(0);
		}
		return tdscBidderPersonApp;
	}

	public String getMaxNoticeNo() {
		String noticeNo = this.tdscBlockTranAppDao.getMaxNoticeNo();
		return noticeNo;
	}

	public List getTdscBlockRemisemoneyDefrayList(String blockId) {
		List list = tdscBlockRemisemoneyDefrayDao.getTdscBlockRemisemoneyDefrayList(blockId);
		return list;
	}

	public List queryBankDicList() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		List retList = new ArrayList();
		// 获取保证金账户字典表 DIC_BANK
		String sql = "select * from DIC_BANK";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				TdscDicBean tb = new TdscDicBean();
				tb.setDicCode(rs.getString("DIC_CODE"));
				tb.setDicValue(rs.getString("DIC_VALUE"));
				tb.setDicNum(rs.getString("DIC_NUM"));
				tb.setDicDescribe(StringUtil.ISO88591toGBK(rs.getString("DIC_DESCRIBE")));
				tb.setIsValidity(rs.getString("IS_VALIDITY"));
				retList.add(tb);
			}

		} catch (Exception e) {
			// Nothing to do
		}

		return retList;
	}
	
	public List queryGeologyAssessUintDicList() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		List retList = new ArrayList();
		String sql = "select * from DIC_GEOLOGY_ASSESS_UINT";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				TdscDicBean tb = new TdscDicBean();
				tb.setDicCode(rs.getString("DIC_CODE"));
				tb.setDicValue(StringUtil.ISO88591toGBK(rs.getString("DIC_VALUE")));
				tb.setDicNum(rs.getString("DIC_NUM"));
				tb.setDicDescribe(StringUtil.ISO88591toGBK(rs.getString("DIC_DESCRIBE")));
				tb.setIsValidity(rs.getString("IS_VALIDITY"));
				retList.add(tb);
			}

		} catch (Exception e) {
			// Nothing to do
		}

		return retList;
	}

	public String getMaxNoticeNoBlockNoticeNoPrefix(String startindex,String blockNoticeNoPrefix) {
		String noticeNo = this.tdscBlockTranAppDao.getMaxNoticeNoBlockNoticeNoPrefix(startindex,blockNoticeNoPrefix);
		return noticeNo;
	}

	public TdscBlockPart getBlockInfoPart(String blockId) {
		List tmpList = tdscBlockPartDao.getTdscBlockPartList(blockId);
		if (tmpList != null && tmpList.size() > 0)
			return (TdscBlockPart) tmpList.get(0);
		return null;

	}
	/**
	 * 根据业务ID和业务类型获取文件数据列表
	 * @param presellId
	 * @param catalog
	 * @return
	 */
	public List findFileRefByBusId(String blockId, String catalog){
		if (StringUtils.isNotEmpty(blockId)) {
			List list = fileRefDao.findListFileRefByCondition(blockId, catalog);
			return list;
		}
		return null;
		
	}

	public FileRefDao getFileRefDao() {
		return fileRefDao;
	}

	public void setFileRefDao(FileRefDao fileRefDao) {
		this.fileRefDao = fileRefDao;
	}
	
	public TdscBlockTranApp getBlockTranAppByBlockId(String blockId){
		return this.tdscBlockTranAppDao.getTdscBlockTranAppInfo(blockId);
	}
	public FileRef uploadFile(String appId, byte[] bytes, String fileName,String fileType, String catalog) throws Exception {
		FileRef fileRef = new FileRef();
		fileRef.setBusId(appId);
		fileRef.setFileId(null);
		fileRef.setFileCatalog(catalog);
		fileRef.setFileName(fileName);
		fileRef.setFileType(fileType);
		fileRef = (FileRef)fileRefDao.saveOrUpdate(fileRef);
		String filePath = PATH + File.separator + fileRef.getFileId() + "." + fileType;
		File file = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		HttpClientRemoteFactory.getInstance().sendRemote(filePath, HTTPURL);
		return fileRef;
	}
	
	public void saveMakeupData(String blockId,String zhoubian,Object[] fileList,String[] fileNameList) throws Exception{
		String fileType = "";
		if(fileList!=null&&fileList.length>0){
			for(int i=0;i<fileList.length;i++){
				FormFile file = (FormFile)fileList[i];
				fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
				String fileName = fileNameList[i];
				byte[] bytes = file.getFileData();
				this.uploadFile(blockId, bytes, fileName,fileType, GlobalConstants.BLOCK_FILE);
			}
		}
		TdscBlockTranApp tranApp = this.getBlockTranAppByBlockId(blockId);
		tranApp.setTextMemo(zhoubian);
		this.tdscBlockTranAppDao.update(tranApp);
	}
	public void delFileRefById(String fileId) {
		if (StringUtils.isNotEmpty(fileId)) {
			fileRefDao.deleteById(fileId);
		}
	}
	/**
	 * 保存附件
	 * @param blockId
	 * @param fileList
	 * @param fileNameList
	 * @throws Exception
	 */
	public void saveBlockFile(String blockId,Object[] fileList,String[] fileNameList) throws Exception{
		String fileType = "";
		if(fileList!=null&&fileList.length>0){
			for(int i=0;i<fileList.length;i++){
				FormFile file = (FormFile)fileList[i];
				fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
				String fileName = fileNameList[i];
				byte[] bytes = file.getFileData();
				this.uploadFile(blockId, bytes, fileName,fileType, GlobalConstants.BLOCK_FILE);
			}
		}
		TdscBlockTranApp tranApp = this.getBlockTranAppByBlockId(blockId);
		
		this.tdscBlockTranAppDao.update(tranApp);
	}
	
	public List findMarkupDiJiaList(){
		return this.tdscBlockTranAppDao.findDijiaList();
	}
	
	public void saveTdscBlockTranApp(TdscBlockTranApp tranApp){
		this.tdscBlockTranAppDao.update(tranApp);
	}
	
	/**
	 * 查找挂牌数据
	 * @param appId
	 * @return
	 */
	public TdscListingInfo findListingInfo(String appId){
		List list = this.tdscListingInfoDao.findListingInfo(appId);
		if(list!=null&&list.size()>0){
			return (TdscListingInfo)list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 查找所有处于确认底价中的地块
	 * @return
	 */
	public List findSelectingDijiaList(){
		List list = this.tdscBlockTranAppDao.findListByTranResult("03");
		return list;
	}

	/**
	 * 查找所有交易结束的地块
	 * @return
	 */
	public PageList findTradeEndList(int pageSize,int pageCurrent){
		return this.tdscBlockTranAppDao.findTradeEndList(pageSize,pageCurrent);
	}
	
	
	public PersonInfo getWxPersonInfo(String bidderName){
		return wxPersonInfoDao.getWxPersonInfo(bidderName);
	}
	
	public Long genIncrementId(String name){
		return genIncrementId(name, 1);
	}
	
	public Long genIncrementId(String name, int increment){
		long r = 0L;
		IncrementId x = incrementIdDao.getIncrement(name);
		if(x != null){
			x.setCurrentValue(x.getCurrentValue() + (long)increment);
			incrementIdDao.update(x);
			r = x.getCurrentValue();
		}else{
			r = 1L;
			x = new IncrementId();
			x.setCurrentValue(r);
			x.setIdName(name);
			incrementIdDao.save(x);
		}
		return new Long(r);
//		return idSpringManager.getIncrementId(name);
	}

	/**
	 * 根据appId和号牌查单一竞买人
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppIdAndConNum(String appId,	String conNum) {
		return tdscBidderAppDao.getBidderAppByAppIdConNo(appId, conNum);
	}
}
