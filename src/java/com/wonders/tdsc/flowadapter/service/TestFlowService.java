package com.wonders.tdsc.flowadapter.service;

import java.util.HashMap;
import java.util.Map;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

/**
 * @author administrator
 *  
 */
public class TestFlowService extends BaseSpringManagerImpl {

    private TdscBlockInfoDao tdscBlockInfoDao;

    private TdscBlockTranAppDao tdscBlockTranAppDao;

    private AppFlowService appFlowService;

    private CommonQueryService commonQueryService;

    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
        this.tdscBlockInfoDao = tdscBlockInfoDao;
    }

    public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
        this.tdscBlockTranAppDao = tdscBlockTranAppDao;
    }

    /**
     * 根据查询条件返回列表 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
     * 
     * @param condition
     * @return List
     */
    public PageList queryAppList(TdscBaseQueryCondition condition) {
        return this.commonQueryService.queryTdscBlockAppViewPageList(condition);
    }

    /**
     * 根据查询条件返回双杨流程待办列表 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
     * 
     * @param condition
     * @return List
     */
    public PageList queryAppFlowList(TdscBaseQueryCondition condition) {
        return this.commonQueryService.queryTdscBlockAppViewFlowPageList(condition);
    }

    /**
     * 保存地块信息
     * 
     * @param condition
     */
    public Map saveBlockInfo(TdscBlockInfo tdscBlockInfo, TdscBlockTranApp tdscBlockTranApp) {
        if (tdscBlockTranApp.getAppId() != null && !"".equals(tdscBlockTranApp.getAppId())) {
            tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
            tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
        } else {
            tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.save(tdscBlockInfo);
            tdscBlockTranApp.setBlockId(tdscBlockInfo.getBlockId());
            tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.save(tdscBlockTranApp);
        }
        String appId = tdscBlockTranApp.getAppId();

        Map returnMap = new HashMap();
        returnMap.put("tdscBlockInfo", tdscBlockInfo);
        returnMap.put("tdscBlockTranApp", tdscBlockTranApp);

        return returnMap;
    }

    /**
     * 删除地块信息
     * 
     * @param condition
     */
    public void deleteBlockInfo(String appId) {
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
        if (tdscBlockTranApp != null) {
            String blockId = tdscBlockTranApp.getBlockId();
            TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
            if (tdscBlockInfo != null) {
                tdscBlockInfoDao.delete(tdscBlockInfo);
            }
            tdscBlockTranAppDao.delete(tdscBlockTranApp);
        }
    }

    /**
     * 保存意见操作
     * 
     * @param condition
     */
    public void saveYijianInfo(TdscAppFlow appFlow, String saveType) throws Exception{
        try {
            if ("tempSave".equals(saveType)) {
                this.appFlowService.tempSaveOpnn(appFlow);
            } else if ("submitSave".equals(saveType)) {
                this.appFlowService.saveOpnn(appFlow);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询意见页面信息
     * 
     * @param condition
     */
    public Map queryYijianInfo(String appId) {
        Map returnMap = new HashMap();
        if (appId != null) {
            try {
                returnMap = this.appFlowService.queryOpnnInfo(appId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnMap;
    }

    /**
     * 查询地块信息
     * 
     * @param condition
     */
    public Map queryBlockAppInfo(String appId) {
        Map map = new HashMap();
        if (appId != null) {
            TdscBlockTranApp tranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
            if (tranApp != null && tranApp.getBlockId() != null) {
                TdscBlockInfo blockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tranApp.getBlockId());
                if (blockInfo != null) {
                    map.put("tdscBlockInfo", blockInfo);
                    map.put("tdscBlockTranApp", tranApp);
                }
            }
        }
        return map;
    }
}