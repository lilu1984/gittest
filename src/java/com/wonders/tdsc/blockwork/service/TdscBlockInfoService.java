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
	 * ���ݵؿ�Id���������Ϣ
	 * 
	 * @param blockId
	 *            ������Ϣ
	 * @return TdscBlockInfo����
	 */
	public TdscBlockInfo findInfoById(String blockId) {
		return (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
	}

	/**
	 * ��ѯ�����б�
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
	 * ��ѯָ��clobId���ļ�
	 * 
	 * @param clobId
	 * @return
	 */
	public TdscEsClob findClobContent(String clobId) {
		return (TdscEsClob) tdscEsClobDao.get(clobId);
	}

	/**
	 * ��ѯָ��blockId�����ػ�����Ϣ
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
	 * �����ӵؿ���Ϣ
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
	 * ͨ���������ĺŲ�ѯ�ؿ�
	 * 
	 * @param auditeNum
	 *            �������ĺ�
	 * @return
	 */
	public TdscBlockInfo blockInfoByAuditeNum(String auditeNum) {
		return tdscBlockInfoDao.blockInfoByAuditeNum(auditeNum);
	}

	/**
	 * �����ӵؿ�partId��ѯ�ӵؿ���Ϣ
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
	 * ��ѯblockId�������ӵؿ���Ϣ
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
	 * ����BlockCode��ѯ�����ӵؿ���Ϣ
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
	 * ����blockId��������ӵؿ�ĵؿ���
	 * 
	 * @param blockId
	 * @return
	 */
	public String getBlockCodeByBlockId(String blockId) {
		// ��ѯblockId�������ӵؿ���Ϣ
		List blockPartList = (List) this.getTdscBlockPartList(blockId);
		// ����ؿ���
		if (blockPartList != null && blockPartList.size() > 0) {
			StringBuffer blockCodes = new StringBuffer();
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(i);
				if (tdscBlockPart != null && !"".equals(tdscBlockPart.getBlockCode())) {
					if (i < blockPartList.size() - 1) {
						blockCodes.append(tdscBlockPart.getBlockCode()).append("��");
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
	 * ����blockCode���ָ���ؿ��ŵ������ӵؿ���������֮��
	 * 
	 * @param blockCode
	 * @return
	 */
	public BigDecimal getAllBlockAreaByBlockCode(String blockId, String blockCode) {

		BigDecimal allBlockArea = new BigDecimal(0);// �ؿ����֮��

		// ��ѯblockId�������ӵؿ���Ϣ
		List blockPartList = (List) this.getblockPartListByBlockCode(blockId, blockCode);
		// ����ؿ���
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
	 * ����blockId��������ӵؿ�Ĺ滮�������֮��
	 * 
	 * @param blockId
	 * @return
	 */
	public BigDecimal getAllBlockBuildingAreaByBlockCode(String blockId, String blockCode) {

		BigDecimal allBlockBuildingArea = new BigDecimal(0);// �ؿ����֮��

		// ��ѯblockId�������ӵؿ���Ϣ
		List blockPartList = (List) this.getblockPartListByBlockCode(blockId, blockCode);
		// ����ؿ���
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
	 * ɾ���ӵؿ���Ϣ
	 * 
	 * @param tdscBlockPart
	 */
	public void deleteTdscBlockPart(TdscBlockPart tdscBlockPart) {
		if (tdscBlockPart != null)
			this.tdscBlockPartDao.delete(tdscBlockPart);
	}

	/**
	 * �޸��ӵؿ���Ϣ
	 * 
	 * @param tdscBlockPart
	 */
	public TdscBlockPart updateTdscBlockPart(TdscBlockPart tdscBlockPart) {
		if (tdscBlockPart != null) {
			return (TdscBlockPart) this.tdscBlockPartDao.saveOrUpdate(tdscBlockPart);
		}
		return null;
	}

	// ����appId����ƻ���Ϣ
	public TdscListingInfo findBlistingInfo(String appId) {
		return tdscListingInfoDao.getTdscListingInfoByAppId(appId);
	}

	// ����status������õؿ�
	public List findBlockIdList(String status) {
		return tdscBlockInfoDao.blockInfoList(status);
	}

	// ����ƻ���Ϣ�б�
	public List findBlistingInfoList(TdscListingInfoCondition condition) {
		return tdscListingInfoDao.findAppList(condition);
	}

	/*
	 * ȡ������
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
		// �������ػ�����Ϣ
		String blockId = tdscBlockInfo.getBlockId();
		if (StringUtils.isNotBlank(blockId)) {
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		} else {
			tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_NOTTRADE);
			tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
			blockId = tdscBlockInfo.getBlockId();
		}
		// ɾ��ԭ���ӵؿ���Ϣ
		delBlockPartByBlockId(blockId);
		// �����ӵؿ���Ϣ
		if (blockPartList != null && blockPartList.size() > 0) {

			for (int j = 0; j < blockPartList.size(); j++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);

				tdscBlockPart.setBlockId(blockId);
				tdscBlockPartDao.save(tdscBlockPart);
			}
		}
	}

	/**
	 * ���湤ҵ��ҵ������Ϣ
	 * 
	 * @param tdscBlockInfo
	 * @param tdscBlockTranApp
	 * @param list
	 */
	// public Map saveRemise(TdscBlockInfo tdscBlockInfo, TdscBlockTranApp tdscBlockTranApp, List list,String saveType,SysUser user,List blockPartList,TdscBlockQqjcInfo
	// tdscBlockQqjcInfo,TdscEsClob tdscEsClob ) {
	// // ���淵��ֵ
	// Map retMap = new HashMap();
	// // �������ػ�����Ϣ
	// String blockId = tdscBlockInfo.getBlockId();
	// if (StringUtils.isNotBlank(blockId)) {
	// tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
	// } else {
	// tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
	// blockId = tdscBlockInfo.getBlockId();
	// }
	// retMap.put("tdscBlockInfo", tdscBlockInfo);
	// //���滮���Ҫ�㡱�ֶ���ΪCLOB����
	// String bzGhsjyd = (String)tdscBlockTranApp.getBzGhsjyd();
	// if(tdscBlockTranApp != null ){
	// //��ѯԭ���ݣ���á��滮���Ҫ�㡱��Ӧ�ģ��ڡ�TDSC_ES_CLOB�����е�ID;
	// //TdscBlockTranApp oldTranApp = (TdscBlockTranApp)tdscBlockTranAppDao.get(tdscBlockTranApp.getAppId());
	// // if(StringUtils.isNotBlank(oldTranApp.getBzGhsjyd())){
	// // //��ѯʱ������ԭID��ѯ
	// // TdscEsClob clob = (TdscEsClob)tdscEsClobDao.get(oldTranApp.getBzGhsjyd());
	// // if(clob != null){
	// // //����ʱ�������µ���������
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
	// // ͨ������TdscBlockInfo���ص�BOȡ������ Ȼ����TdscBlockTranApp����������
	// tdscBlockTranApp.setBlockId(blockId);
	// String appId = tdscBlockTranApp.getAppId();
	// if (StringUtils.isNotBlank(appId)) {
	// tdscBlockTranAppDao.update(tdscBlockTranApp);
	// } else {
	// tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
	// appId = tdscBlockTranApp.getAppId();
	// }
	// //�滮���Ҫ�㣺ҳ����ʾ��ʱ����ʾ�ı�����
	// tdscBlockTranApp.setBzGhsjyd(bzGhsjyd);
	// retMap.put("tdscBlockTranApp", tdscBlockTranApp);
	// // ͨ������TdscBlockTranApp���ص�BOȡ������ Ȼ����TdscBlockMaterial����������
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
	// //ɾ��ԭ���ӵؿ���Ϣ
	// delBlockPartByBlockId(blockId);
	// //�����ӵؿ���Ϣ
	// if(blockPartList != null && blockPartList.size()>0){
	// for(int j=0;j<blockPartList.size();j++){
	// TdscBlockPart tdscBlockPart = (TdscBlockPart)blockPartList.get(j);
	// tdscBlockPart.setBlockId(blockId);
	// tdscBlockPartDao.save(tdscBlockPart);
	// }
	// }
	// retMap.put("blockPartList", blockPartList);
	// //�����µ�ʵ��tdscBlockQqjcInfo��blockId��
	// tdscBlockQqjcInfo.setBlockId(blockId);
	// //ɾ��ԭ�С�ǰ�ڼ����Ϣ������Ϣ��������
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

		// ����佨��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) parameterMap.get("tdscBlockPjxxInfo");
		// �����佨��Ϣ
		if (StringUtils.isNotEmpty(tdscBlockPjxxInfo.getPjxxInfoId())) {
			tdscBlockPjxxInfoDao.update(tdscBlockPjxxInfo);
		} else {
			tdscBlockPjxxInfo.setPjxxInfoId(null);
			tdscBlockPjxxInfoDao.save(tdscBlockPjxxInfo);
		}

		// ����"�滮���Ҫ��"
		tdscEsClob = (TdscEsClob) tdscEsClobDao.saveOrUpdate(tdscEsClob);
		// tdscEsClob.setClobContent(Base64.encode(tdscEsClob.getClobContent()));
		// ���챣�淵��ֵ��MAP
		Map retMap = new HashMap();
		// �������ػ�����Ϣ
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
		// �滮���Ҫ�㣺ҳ����ʾ��ʱ����ʾ�ı�����
		tdscBlockTranApp.setBzGhsjyd(tdscEsClob.getClobId());
		// ͨ������TdscBlockInfo���ص�BOȡ������ Ȼ����TdscBlockTranApp����������
		tdscBlockTranApp.setBlockId(blockId);
		String appId = tdscBlockTranApp.getAppId();
		if (StringUtils.isNotBlank(appId)) {
			tdscBlockTranAppDao.update(tdscBlockTranApp);
		} else {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
			appId = tdscBlockTranApp.getAppId();
		}

		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		// �佨��Ϣ
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);
		// ͨ������TdscBlockTranApp���ص�BOȡ������ Ȼ����TdscBlockMaterial����������
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
		// ɾ��ԭ���ӵؿ���Ϣ
		delBlockPartByBlockId(blockId);
		// �����ӵؿ���Ϣ
		if (blockPartList != null && blockPartList.size() > 0) {
			for (int j = 0; j < blockPartList.size(); j++) {
				TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);
				tdscBlockPart.setBlockId(blockId);
				tdscBlockPartDao.save(tdscBlockPart);
			}
		}
		retMap.put("blockPartList", blockPartList);

		// ɾ��ԭ�еĵؿ���ý�֧����Ϣ
		delTdscBlockRemisemoneyDefrayByBlockId(blockId);
		// �����ؿ���ý�֧����Ϣ
		if (tdscBlockRemisemoneyDefrayList != null && tdscBlockRemisemoneyDefrayList.size() > 0) {
			for (int j = 0; j < tdscBlockRemisemoneyDefrayList.size(); j++) {
				TdscBlockRemisemoneyDefray tdscBlockRemisemoneyDefray = (TdscBlockRemisemoneyDefray) tdscBlockRemisemoneyDefrayList.get(j);
				tdscBlockRemisemoneyDefray.setBlockId(blockId);
				tdscBlockRemisemoneyDefrayDao.save(tdscBlockRemisemoneyDefray);
			}
		}
		retMap.put("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

		// �����µ�ʵ��tdscBlockQqjcInfo��blockId��
		tdscBlockQqjcInfo.setBlockId(blockId);
		// ɾ��ԭ�С�ǰ�ڼ����Ϣ������Ϣ��������
		if (blockId != null && !"".equals(blockId)) {
			TdscBlockQqjcInfo QqjcInfoOld = (TdscBlockQqjcInfo) tdscBlockQqjcInfoDao.getTdscBlockQqjcInfo(blockId);
			if (QqjcInfoOld != null) {
				tdscBlockQqjcInfoDao.delete(QqjcInfoOld);
			}
		}
		tdscBlockQqjcInfoDao.save(tdscBlockQqjcInfo);

		// ���� ��������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) parameterMap.get("tdscBidderApp");
		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) parameterMap.get("tdscBidderPersonApp");
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockTranApp.getTransferMode())
				&& GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(tdscBlockTranApp.getIsPurposeBlock())) {
			// ���Ϊ������������
			if (StringUtils.isNotEmpty(tdscBidderPersonApp.getBidderPersonId())) {
				// ֻ��Ҫ����bidderperson��
				TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) this.tdscBidderPersonAppDao.get(tdscBidderPersonApp.getBidderPersonId());
				bidderPersonApp.setBidderName(tdscBidderPersonApp.getBidderName());
				bidderPersonApp.setOrgNo(tdscBidderPersonApp.getOrgNo());
				bidderPersonApp = (TdscBidderPersonApp) this.tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);
			} else {
				// ����bidderapp ���� bidderpersonapp
				tdscBidderApp.setAppId(appId);
				tdscBidderApp.setBidderId(null);
				tdscBidderApp = (TdscBidderApp) this.tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
				tdscBidderPersonApp.setBidderPersonId(null);
				tdscBidderPersonApp.setBidderId(tdscBidderApp.getBidderId());
				// ������Ѿ����ڵ������ˣ�ȡ��ԭ����������ѡ��� appid +��,�� �� �� set �� purposeAppId ��
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
	 * ��������
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
	 * �������
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
	 * ��ѯ����ʹ����Ϣ�б�
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryTdscBlockUsedInfoListByBlockId(String blockId) {
		return tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
	}

	/**
	 * ɾ��appIdָ����TdscBlockMaterial����Ϣ
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
	 * ���TdscBlockMaterial���б�
	 * 
	 * @param condition
	 * @return
	 */
	public List getTdscBlockMaterialList(TdscBlockMaterialCondition condition) {
		return tdscBlockMaterialDao.findMaterialList(condition);
	}

	/**
	 * ����appId��ѯ�����б�
	 * 
	 * @param appId
	 * @return
	 */
	public List findMaterialListByAppId(String appId) {
		return tdscBlockMaterialDao.findMaterialListByAppId(appId);
	}

	/**
	 * ���TdscBlockMaterial���б�
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
	 * ������ػ�����Ϣ�б�
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getTdscBlockUsedInfoList(TdscBlockUsedInfoCondition condition) {
		return tdscBlockUsedInfoDao.findPageList(condition);
	}

	public TdscBlockUsedInfo findTdscBlockUsedInfo(String blockUsedId) {
		// ������;��Ϣ��ѯ
		return tdscBlockUsedInfoDao.findTdscBlockUsedInfo(blockUsedId);
	}

	// д����Ƴɽ���Ϣ
	public void modifyStatus(String appId, BigDecimal topPrice) {
		// ������Ϣ��
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscBlockTranAppDao tdscBlockTranAppDao = new TdscBlockTranAppDao();
		tdscBlockTranApp.setAppId(appId);
		// �����Ӧ�ؿ�Id
		tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(tdscBlockTranApp);
		String blockId = tdscBlockTranApp.getBlockId();
		// ������Ϣ���д���۸�
		tdscBlockTranApp.setResultPrice(topPrice);
		tdscBlockTranAppDao.save(tdscBlockTranApp);
		// �ؿ���Ϣ��
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		tdscBlockInfo.setBlockId(blockId);
		tdscBlockInfo.setResultPrice(topPrice);
		tdscBlockInfo.setStatus("02");
		TdscBlockConInfoDao tdscBlockConInfoDao = new TdscBlockConInfoDao();
		tdscBlockConInfoDao.save(tdscBlockInfo);
	}

	/**
	 * ��ѯָ��appId�����ؽ�����Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public TdscBlockTranApp findTdscBlockTranApp(String appId) {
		return tdscBlockTranAppDao.findTdscBlockTranApp(appId);
	}

	/**
	 * ��ѯ��ӦblockId�����ػ�����Ϣ
	 */
	public TdscBlockInfo findTdscBlockInfo(String blockId) {
		return tdscBlockInfoDao.findBlockInfo(blockId);
	}

	/**
	 * �ϴ�����
	 * 
	 * @param upLoadFile
	 * @return
	 */
	public String upLoadFile(FormFile upLoadFile) {
		return tdscBlockInfoDao.upLoadFile(upLoadFile);
	}

	/** *********************add by huangjiawei************************** */
	/**
	 * ͨ��APPID����TdscBlockTranApp
	 */
	public TdscBlockTranApp getTdscBlockTranAppByAppId(String appId) {
		return (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
	}

	/**
	 * ͨ��BLOCKID����TdscBlockInfo
	 */
	public TdscBlockInfo getTdscBlockInfoByBlockId(String blockId) {
		return (TdscBlockInfo) this.tdscBlockInfoDao.get(blockId);
	}

	/**
	 * �޸Ľ��׽�������Tdsc_Block_Tran_App��
	 */
	public void updateTdscBlockTranApp(TdscBlockTranApp tdscBlockTranApp) {
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);
	}

	/**
	 * �޸Ľ��׽�������Tdsc_Block_Info��
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
	 * ������ƽ��׽��
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
	 * ����������ϵͳ��������������Ϣ��������ݿ� TdscBlockInfo.java TdscBlockUsedInfo.java
	 * 
	 * author:weedlu 20071222
	 * 
	 * @param tdscBlockInfo
	 */
	public void addAuditedBlockInfo(TdscBlockInfo tdscBlockInfo) {
		// 1����������״̬
		tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_NOTTRADE);

		// 2�����ؿ������Ϣ�е��ļ���Ϣ���浽�ļ��У����ļ�·���������ݿ�
		// String path = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.DIRECTORY_RECEIPT_AUDITED);
		// �õ�����
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
		// ���ط���
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

		// 3�����㽨���������������������
		List usedInfoList = tdscBlockInfo.getUsedInfoList();
		if (usedInfoList != null && usedInfoList.size() > 0) {
			double totalBuildingArea = 0.0;
			List blockTypeList = new ArrayList();
			for (int i = 0; i < usedInfoList.size(); i++) {
				TdscBlockUsedInfo usedInfo = (TdscBlockUsedInfo) usedInfoList.get(i);

				// ���㽨�������
				double buildingArea = usedInfo.getBuildingArea() == null ? 0.0 : usedInfo.getBuildingArea().doubleValue();
				totalBuildingArea += buildingArea;

				// ������������
				if (usedInfo.getLandUseType() != null) {
					Map landUseMap = DicDataUtil.getInstance().getComplexDicItemMap(GlobalConstants.DIC_ID_PLAN_COMPLEX_USE, usedInfo.getLandUseType());
					String blockType = null;
					if (landUseMap != null)
						blockType = (String) landUseMap.get("DIC_DESCRIBE");

					if (blockType != null && !blockTypeList.contains(blockType))
						blockTypeList.add(blockType);
				}

			}
			// ���㽨�������
			tdscBlockInfo.setTotalBuildingArea(new BigDecimal(totalBuildingArea));

			// ������������
			if (blockTypeList.size() == 1) {
				tdscBlockInfo.setBlockType((String) blockTypeList.get(0));
			} else {
				logger.warn("======��������ϵͳ��������;ת��Ϊ������������======");
				logger.warn(blockTypeList);
			}
		}

		// 4���ؿ������Ϣ��
		TdscBlockInfo savedBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);

		// 5���ؿ���;��Ϣ��
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
	 * ����˫���ṩ�����غ�ͬ��Ϣ
	 * 
	 * @param tdscBlockConInfo
	 */
	public void addBlockConInfo(TdscBlockConInfo tdscBlockConInfo) {
		this.tdscBlockConInfoDao.save(tdscBlockConInfo);
	}

	/**
	 * �޸�ʵ��� ����״̬����¼����ʱ��
	 * 
	 * @param appId
	 */
	public void modIfPublish(String appId, String transferMode, SysUser user) {
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		// ���÷���״̬
		tdscBlockTranApp.setIfPublish("1");
		// ��¼����ʱ��
		Date publishDate = new Date(System.currentTimeMillis());
		tdscBlockTranApp.setPublishDate(publishDate);
		tdscBlockTranAppDao.update(tdscBlockTranApp);
		// �ƶ�����
		try {
			appFlowService.saveOpnn(appId, transferMode, user);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * ����noticeId��noitceNo��ѯTdscBlockTranApp�б�
	 * 
	 * @param noticeId
	 * @param noitceNo
	 * @return
	 */
	public List findTdscBlockTranAppByNoticeNo(String noticeId, String noitceNo) {
		return tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, noitceNo);
	}

	/**
	 * ����PlanId��ѯTdscBlockTranApp�б�
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
	 * ����滮��; �����ӵؿ���Ϣ
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
				// �滮��; �ӵؿ���;
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
					tmpUseType.append("��");
					if (StringUtils.isNotEmpty(tdscBlockPart.getLandUseType())) {
						tmpUseType.append(DicDataUtil.getInstance().getDic(GlobalConstants.DIC_LAND_CODETREE_T).get(tdscBlockPart.getLandUseType()));
					}
				}
			}
		}

		return tmpUseType.toString();
	}

	/**
	 * ����滮��; �����ӵؿ���Ϣ
	 * 
	 * @param tdscBlockAppView
	 * @return
	 */

	public TdscBlockAppView tidyTdscBlockAppViewByBlockId(String appId) {
		if (appId == null)
			return null;

		TdscBlockAppView blockAppView = new TdscBlockAppView();
		// �ؿ齻����Ϣ
		TdscBlockTranApp blockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);

		// �ؿ������Ϣ
		TdscBlockInfo blockinfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockTranApp.getBlockId());

		// �ӵؿ���Ϣ
		List blockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(blockinfo.getBlockId());

		// ˫��ؿ���Ϣ
		List blockUsedInfoList = (List) tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockinfo.getBlockId());

		// ===������������===
		if ("101".equals(blockinfo.getBlockType())) {
			blockinfo.setTransferLife("50��");
		}

		// ===0.����Ϊ�ؿ���;����===
		StringBuffer tmpUsedAll = new StringBuffer("");
		if (blockUsedInfoList != null && blockUsedInfoList.size() > 0)
			for (int i = 0; i < blockUsedInfoList.size(); i++) {
				TdscBlockUsedInfo blockUsedInfo = (TdscBlockUsedInfo) blockUsedInfoList.get(i);
				tmpUsedAll.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_ID_PLAN_USE, blockUsedInfo.getLandUseType())).append("��");
			}

		// ȥ��ĩβ�ġ�������
		if (!"".equals(tmpUsedAll.toString()))
			blockAppView.setTempStr2(tmpUsedAll.substring(0, tmpUsedAll.length() - 1));

		// ===1.����Ϊ�ӵؿ���Ϣ����===
		// ��ʱ����
		StringBuffer tmpRangeSide = new StringBuffer("");
		StringBuffer tmpUseType = new StringBuffer("");
		StringBuffer tmpVolumeRate = new StringBuffer("");
		StringBuffer tmpTransferLife = new StringBuffer("");
		StringBuffer tmpGreenRate = new StringBuffer("");
		StringBuffer tmpArea = new StringBuffer("");

		if (blockPartList != null && blockPartList.size() > 0)
			for (int i = 0; i < blockPartList.size(); i++) {
				TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
				// �ӵؿ�����
				String blockName = "P" + i;
				if (StringUtils.isNotEmpty(blockPart.getBlockName())) {
					blockName = blockPart.getBlockName();
				}

				// ������Χ �ܵؿ�������Χ���ֵؿ�������Χ
				tmpRangeSide.append(blockName).append("��");
				if (StringUtils.isNotEmpty(blockPart.getRangeEast())) {
					tmpRangeSide.append("����").append(blockPart.getRangeEast()).append("��");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeSouth())) {
					tmpRangeSide.append("����").append(blockPart.getRangeSouth()).append("��");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeWest())) {
					tmpRangeSide.append("����").append(blockPart.getRangeWest()).append("��");
				}
				if (StringUtils.isNotEmpty(blockPart.getRangeNorth())) {
					tmpRangeSide.append("����").append(blockPart.getRangeNorth()).append("��");
				}

				// ȥ��������
				if ("��".equals(tmpRangeSide.substring(tmpRangeSide.length() - 1)))
					tmpRangeSide = new StringBuffer(tmpRangeSide.substring(0, tmpRangeSide.length() - 1));

				tmpRangeSide.append("��");

				// ȥ����������
				if (tmpRangeSide.indexOf("����") > 0)
					tmpRangeSide = new StringBuffer("");

				// �滮��; �ӵؿ���;
				tmpUseType.append(blockName).append("��");
				if (StringUtils.isNotEmpty(blockPart.getLandUseType())) {
					tmpUseType.append(blockPart.getLandUseType());
				}
				tmpUseType.append("��");

				// ȥ����������
				if (tmpUseType.indexOf("����") > 0)
					tmpUseType = new StringBuffer("");

				// �ݻ��� �ӵؿ�
				tmpVolumeRate.append(blockName).append("��");
				if (StringUtils.isNotEmpty(blockPart.getVolumeRate())) {
					tmpVolumeRate.append(blockPart.getVolumeRate());
				}
				tmpVolumeRate.append("��");

				// ȥ����������
				if (tmpVolumeRate.indexOf("����") > 0)
					tmpVolumeRate = new StringBuffer("");

				// �̻���
				tmpGreenRate.append(blockName).append(":");
				if (StringUtils.isNotEmpty(blockPart.getGreeningRate())) {
					tmpGreenRate.append(blockPart.getGreeningRate());
				}
				tmpGreenRate.append("��");

				// ȥ����������
				if (tmpGreenRate.indexOf("����") > 0)
					tmpGreenRate = new StringBuffer("");

				// ������� �ӵؿ�
				tmpArea.append(blockName).append("��");
				if (blockPart.getTotalLandArea() != null) {
					tmpArea.append(blockPart.getTotalLandArea());
				}
				tmpArea.append("��");

				// ȥ����������
				if (tmpArea.indexOf("����") > 0)
					tmpArea = new StringBuffer("");

				// �������� �ӵؿ�
				tmpTransferLife.append(blockName).append("��");
				if (StringUtils.isNotEmpty(blockPart.getTransferLife())) {
					tmpTransferLife.append(blockPart.getTransferLife());
				}
				tmpTransferLife.append("��");

				// ȥ����������
				if (tmpTransferLife.indexOf("����") > 0)
					tmpTransferLife = new StringBuffer("");

			}

		// ===========������Χ �ܵؿ�������Χ���ֵؿ�������Χ========
		StringBuffer sbTmp = new StringBuffer("�ܷ�Χ��");

		if (StringUtils.isNotEmpty(blockinfo.getRangeEast())) {
			sbTmp.append("����").append(blockinfo.getRangeEast()).append("��");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeSouth())) {
			sbTmp.append("����").append(blockinfo.getRangeSouth()).append("��");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeWest())) {
			sbTmp.append("����").append(blockinfo.getRangeWest()).append("��");
		}
		if (StringUtils.isNotEmpty(blockinfo.getRangeNorth())) {
			sbTmp.append("����").append(blockinfo.getRangeNorth()).append("��");
		}

		// ȥ��������
		if ("��".equals(sbTmp.substring(sbTmp.length() - 1)))
			sbTmp = new StringBuffer(sbTmp.substring(0, sbTmp.length() - 1));

		sbTmp.append("��");

		// ȥ����������
		if (sbTmp.indexOf("����") > 0)
			sbTmp = new StringBuffer("");

		if (!"".equals(tmpRangeSide.toString()))
			sbTmp.append(tmpRangeSide);

		if (!"".equals(sbTmp.toString()))
			blockAppView.setRangeEast(sbTmp.toString());

		// ==========�滮��; �ӵؿ���;===========
		if (!"".equals(tmpUseType.toString()))
			blockAppView.setTempStr2(tmpUseType.toString());

		// ==========�ݻ��� �ӵؿ�============
		if (!"".equals(tmpVolumeRate.toString())) {
			blockAppView.setVolumeRate(tmpVolumeRate.toString());
		} else {
			blockAppView.setVolumeRate(blockinfo.getVolumeRate());
		}

		// =========�������� �ӵؿ�=======
		if (!"".equals(tmpTransferLife.toString())) {
			blockAppView.setTransferLife(tmpTransferLife.toString());
		} else {
			blockAppView.setTransferLife(blockinfo.getTransferLife());
		}

		// =========�̻��� �ӵؿ�=======
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
	 * ����blockIdɾ���ӵؿ���Ϣ
	 * 
	 * @param blockId
	 */
	public void delBlockPartByBlockId(String blockId) {
		// ��ѯԭ����ϢLIST
		List oldBlockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
		if (oldBlockPartList != null && oldBlockPartList.size() > 0) {
			for (int i = 0; i < oldBlockPartList.size(); i++) {
				tdscBlockPartDao.delete(oldBlockPartList.get(i));
			}
		}
	}

	/**
	 * ����blockIdɾ���õؿ�ĳ��ý�֧����Ϣ
	 * 
	 * @param blockId
	 */
	public void delTdscBlockRemisemoneyDefrayByBlockId(String blockId) {
		// ��ѯԭ����ϢLIST
		List oldtdscBlockRemisemoneyDefrayList = (List) tdscBlockRemisemoneyDefrayDao.getTdscBlockRemisemoneyDefrayList(blockId);
		if (oldtdscBlockRemisemoneyDefrayList != null && oldtdscBlockRemisemoneyDefrayList.size() > 0) {
			for (int i = 0; i < oldtdscBlockRemisemoneyDefrayList.size(); i++) {
				tdscBlockRemisemoneyDefrayDao.delete(oldtdscBlockRemisemoneyDefrayList.get(i));
			}
		}
	}

	/**
	 * ����blockIdɾ���ؿ鼰�ӵؿ���Ϣ
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
	 * ��json�ַ�������MAP
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
	 * ����blockId����ؿ��ţ����ظ�
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
					retBuffer.append(tdscBlockPartList.get(i)).append("��");
				}
			}
			if (retBuffer != null) {
				retString = retBuffer.substring(0, retBuffer.length() - 1);
			}
		}
		return retString;
	}

	/**
	 * ����blockId�������в��ظ�������;
	 */
	public String tidyLandUseTypeByBlockId(String blockId) {
		List objList = (List) tdscBlockPartDao.getDistinctUseTypeAndTransferLifeList(blockId);
		StringBuffer retBuffer = new StringBuffer();
		String retString = "";
		if (objList != null && objList.size() > 0) {
			for (int i = 0; i < objList.size(); i++) {
				Object[] obj = (Object[]) objList.get(i);
				if (obj[0] != null && !"".equals(obj[0]))
					retBuffer.append(obj[0].toString()).append("��");
			}
		}

		if (retBuffer != null && !"".equals(retBuffer)) {
			retString = retBuffer.substring(0, retBuffer.length() - 1);
		}
		return retString;
	}

	/**
	 * ����blockId�������в��ظ� ������;+��������+���ꡱ
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
					retBuffer.append(obj[1].toString()).append("�ꡢ");
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
	 * Ϊ�����ļ��͹������������Ϣ����ָ����ʽ����
	 * 
	 * @param blockidList
	 */
	public List makeDataForFile(List blockidList) {

		// �ӵؿ���Ϣ��ѯ�б�
		List returnPartList = new ArrayList();

		if (blockidList != null && blockidList.size() > 0) {
			for (int n = 0; n < blockidList.size(); n++) {
				String blockId2 = (String) blockidList.get(n);
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) this.getTdscBlockInfoByBlockId(blockId2);
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getTdscBlockTranAppInfo(blockId2);

				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					String blockCodes = this.tidyBlockCodeByBlockId(blockId2);
					if (blockCodes != null && blockCodes.length() > 0) {
						String[] blockCodes2 = StringUtils.split(blockCodes, "��");
						for (int a = 0; a < blockCodes2.length; a++) {
							String blockCode = blockCodes2[a];
							List partList = this.getblockPartListByBlockCode(blockId2, blockCode);

							if (partList != null && partList.size() > 1) {

								BigDecimal allBlockArea = new BigDecimal(0);// �ؿ����֮��
								BigDecimal allBlockBuildingArea = new BigDecimal(0);// �滮�������֮��
								for (int k = 0; k < partList.size(); k++) {
									TdscBlockPart tdscBlockPart = (TdscBlockPart) partList.get(k);
									TdscBlockPart retBlockPart = new TdscBlockPart();

									// ����word�ĸ�ʽ����ָ������������
									// ÿ���ؿ�ĵ�һ���ӵؿ鲻���п����ݣ�����ȫֵΪ��������
									if (k == 0) {
										allBlockArea = this.getAllBlockAreaByBlockCode(blockId2, blockCode);
										allBlockBuildingArea = this.getAllBlockBuildingAreaByBlockCode(blockId2, blockCode);

										// ��Ŵ���BlockName���ؿ鹫���
										retBlockPart.setBlockName(tdscBlockTranApp.getXuHao());
										retBlockPart.setBlockCode(tdscBlockPart.getBlockCode());
										// �ؿ�λ�ñ�����RangeEast��,�ؿ鹫��ű�����RangeNorth��
										retBlockPart.setRangeEast(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()), "����"));
										retBlockPart.setRangeNorth(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()), "����"));
										// ������;,�ӵؿ��������������������
										retBlockPart.setLandUseType(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()), "����"));
										retBlockPart.setBlockArea(allBlockArea);
										retBlockPart.setTransferLife(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()), "����"));
										// �ݻ���,�����ܶ�,�̵���,
										retBlockPart.setVolumeRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()), "����"));
										retBlockPart.setDensity(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getDensity()), "����"));
										retBlockPart.setGreeningRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()), "����"));
										// �滮�������������֤��setMemo�����ļ�setBlockDetailedUsed
										retBlockPart.setPlanBuildingArea(allBlockBuildingArea);
										retBlockPart.setMemo(NumberUtil.numberDisplay(tdscBlockTranApp.getMarginAmount(), 2));
										retBlockPart.setBlockDetailedUsed(NumberUtil.numberDisplay(tdscBlockTranApp.getInitPrice(), 2));

									} else {

										// �ؿ��ţ�������;,��������
										retBlockPart.setBlockCode("");
										retBlockPart.setLandUseType(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()));
										retBlockPart.setTransferLife(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()));
										// �ݻ���,�����ܶ�,�̵���,
										retBlockPart.setVolumeRate(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()));
										retBlockPart.setDensity(StringUtils.trimToEmpty(tdscBlockPart.getDensity()));
										retBlockPart.setGreeningRate(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()));

										// �ݻ���,�����ܶ�,�̵���,Ҫ���ϸ��Ƚ�
										TdscBlockPart shangBlockPart = (TdscBlockPart) partList.get(k - 1);
										// ��1�������Ϊ�գ����Һ��ϸ��ӵؿ�ֵ��ͬ��������ϲ�
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
										// ��2�����Ϊ�գ����Һ��ϸ��ӵؿ��ֵ��Ϊ�գ�������ϲ�
										if (StringUtils.isNotEmpty(shangBlockPart.getVolumeRate()) && StringUtils.trimToEmpty(shangBlockPart.getVolumeRate()) != "����"
												&& StringUtils.isEmpty(tdscBlockPart.getVolumeRate())) {
											retBlockPart.setVolumeRate("����");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getDensity()) && StringUtils.trimToEmpty(shangBlockPart.getDensity()) != "����"
												&& StringUtils.isEmpty(tdscBlockPart.getDensity())) {
											retBlockPart.setDensity("����");
										}
										if (StringUtils.isNotEmpty(shangBlockPart.getGreeningRate()) && StringUtils.trimToEmpty(shangBlockPart.getGreeningRate()) != "����"
												&& StringUtils.isEmpty(tdscBlockPart.getGreeningRate())) {
											retBlockPart.setGreeningRate("����");
										}
									}

									// ����������������
									retBlockPart.setRangeSouth(NumberUtil.numberDisplay(tdscBlockPart.getBlockArea(), 2) + "");
									retBlockPart.setRangeWest(NumberUtil.numberDisplay(tdscBlockPart.getPlanBuildingArea(), 2) + "");
									returnPartList.add(retBlockPart);
								}

								// ���ֻ��1���ӵؿ飬����������͹滮���2���������һ����ʾ���ϲ�
							} else if (partList != null && partList.size() == 1) {

								TdscBlockPart tdscBlockPart = (TdscBlockPart) partList.get(0);
								TdscBlockPart retBlockPart = new TdscBlockPart();

								// ��Ŵ���BlockName���ؿ鹫���
								retBlockPart.setBlockName(tdscBlockTranApp.getXuHao());
								retBlockPart.setBlockCode(tdscBlockPart.getBlockCode());
								// �ؿ�λ�ñ�����RangeEast��,�ؿ鹫��ű�����RangeNorth��
								retBlockPart.setRangeEast(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()), "����"));
								retBlockPart.setRangeNorth(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()), "����"));
								// ������;,����������ӵؿ��������������BlockArea�У���������
								retBlockPart.setLandUseType(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getLandUseType()), "����"));
								retBlockPart.setRangeSouth("__" + NumberUtil.numberDisplay(tdscBlockPart.getBlockArea(), 2));
								retBlockPart.setBlockArea(tdscBlockPart.getBlockArea());
								retBlockPart.setTransferLife(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getTransferLife()), "����"));
								// �ݻ���,�����ܶ�,�̵���,
								retBlockPart.setVolumeRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()), "����"));
								retBlockPart.setDensity(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getDensity()), "����"));
								retBlockPart.setGreeningRate(StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()), "����"));
								// �滮����������滮�������������֤��setMemo�����ļ�setBlockDetailedUsed
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
	 * �����佨��ϢID��������Ϣ
	 * 
	 * @param pjxxInfoId
	 * @return
	 */
	public TdscBlockPjxxInfo getTdscBlockPjxxInfo(String pjxxInfoId) {
		return (TdscBlockPjxxInfo) tdscBlockPjxxInfoDao.get(pjxxInfoId);
	}

	/**
	 * ����blockId��������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockPjxxInfo getTdscBlockPjxxInfoByBlockId(String blockId) {
		return (TdscBlockPjxxInfo) tdscBlockPjxxInfoDao.getTdscBlockPjxxInfoByBlockId(blockId);
	}

	/**
	 * ����appID ���Ҷ�Ӧ��bidderapp
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
	 * ����bidderId���Ҷ�Ӧ��personapp
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
		// ��ȡ��֤���˻��ֵ�� DIC_BANK
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
	 * ����ҵ��ID��ҵ�����ͻ�ȡ�ļ������б�
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
	 * ���渽��
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
	 * ���ҹ�������
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
	 * �������д���ȷ�ϵ׼��еĵؿ�
	 * @return
	 */
	public List findSelectingDijiaList(){
		List list = this.tdscBlockTranAppDao.findListByTranResult("03");
		return list;
	}

	/**
	 * �������н��׽����ĵؿ�
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
	 * ����appId�ͺ��Ʋ鵥һ������
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppIdAndConNum(String appId,	String conNum) {
		return tdscBidderAppDao.getBidderAppByAppIdConNo(appId, conNum);
	}
}
