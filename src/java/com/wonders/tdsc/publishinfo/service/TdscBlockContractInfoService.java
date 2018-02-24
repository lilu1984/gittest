package com.wonders.tdsc.publishinfo.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockContractInfo;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockContractInfoCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.publishinfo.dao.TdscBlockContractInfoDao;
import com.wonders.tdsc.publishinfo.dao.TdscNoticeAppDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBlockContractInfoService extends BaseSpringManagerImpl{
	
	private TdscBlockContractInfoDao tdscBlockContractInfoDao;

	public void setTdscBlockContractInfoDao(
			TdscBlockContractInfoDao tdscBlockContractInfoDao) {
		this.tdscBlockContractInfoDao = tdscBlockContractInfoDao;
	}



	/**
     * 根据地块Id获得土地信息
     * 
     * @param blockId
     *            土地信息
     * @return TdscBlockContractInfo对象
     */
    public TdscBlockContractInfo findContractInfoByBlockId(String blockId) {
        return (TdscBlockContractInfo) tdscBlockContractInfoDao.findTdscBlockContractInfoByBlockId(blockId);
    }
    
    public Object deleteContractInfo(String blockId) {
    	TdscBlockContractInfo tdscBlockContractInfo = (TdscBlockContractInfo) tdscBlockContractInfoDao.findTdscBlockContractInfoByBlockId(blockId);
    	
    	if(tdscBlockContractInfo!=null){
    		tdscBlockContractInfo.setIfValidity("0");
    	    tdscBlockContractInfoDao.update(tdscBlockContractInfo);
    	}
    	return null;
    }



	public void save(TdscBlockContractInfo tdscBlockContractInfo) {
		// TODO Auto-generated method stub
		
    	if(tdscBlockContractInfo.getContractInfoId()!=null&&!"".equals(tdscBlockContractInfo.getContractInfoId())){
    	    tdscBlockContractInfoDao.update(tdscBlockContractInfo);
    	}else{
		    tdscBlockContractInfoDao.save(tdscBlockContractInfo);
    	}
		
	}



	public List queryTdscBlockAppViewListWithoutNode(
			TdscBaseQueryCondition condition) {
		// TODO Auto-generated method stub
		List tdscBlockAppViewList = this.tdscBlockContractInfoDao.findTdscBlockAppViewListWithoutNodeId(condition);
        return tdscBlockAppViewList;
	}
	
	/**
	 * 查询出让合同列表from视图TDSC_CONTRACK_INFO_VIEW    lz+   20090713
	 * @param condition
	 * @return
	 */
	public List queryContractInfoListFromView(
			TdscBaseQueryCondition condition) {
		// TODO Auto-generated method stub
		List tdscBlockContractInfoList = this.tdscBlockContractInfoDao.findTdscContractInfoList(condition);
        return tdscBlockContractInfoList;
	}
    
}
