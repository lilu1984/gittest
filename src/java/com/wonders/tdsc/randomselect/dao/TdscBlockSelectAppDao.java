package com.wonders.tdsc.randomselect.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBlockSelectAppDao extends BaseHibernateDaoImpl {

	public Class getEntityClass() {
		return TdscBlockSelectApp.class;
	}
	
	public List findTdscBlockSelectAppListByAppId(String appId){
		StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId = '").append(appId).append("'") ;
        String queryHql = hql.toString() + " order by a.selectNum";
		return this.findList(queryHql.toString());
	}
	public List findSelectAppListbyAppId(String appId){
	    StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId='").append(appId).append("'");
	    hql.append(" and a.replyStatus ='02'or a.replyStatus ='04' or a.replyStatus ='05'");
	    hql.append(" and a.isValid ='1' ");
	    return this.findList(hql.toString());
	   
	}
	public List findSelectAppbyAppId(String appId){
		StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId='").append(appId).append("'");
        return this.findList(hql.toString());

	}
    public List findSelectAppbyAppId(String appId,String isValid){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId='").append(appId).append("'");
        hql.append("and a.isValid ='01'");
        List list = findList(hql.toString());

        return list;
    }
    public List findSelectspecialist(String selectType){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.selectType='05'");
        List list = findList(hql.toString());

        return list;
    }
	/**
	 * 查出已选过的对象的Id
	 * @param appId
	 * @return
	 */
	public List findselectedId(String appId){
		StringBuffer hql = new StringBuffer("select a.selectedId from TdscBlockSelectApp a where a.appId ='").append(appId).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 查出已选过的对象的Id
	 * @param selectType
	 * @return
	 */
	public List findNotary(String selectType){
		StringBuffer hql = new StringBuffer("select distinct a.selectedId from TdscBlockSelectApp a where a.selectType ='").append(selectType).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 查询所有为公证处抽选的记录
	 * @param selectType
	 * @return
	 */
	public List findSpilthList(Timestamp nowTime,String appId){
		StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.selectDate =? and a.appId ='").append(appId).append("'");
		List paramList = new ArrayList();
		paramList.add(nowTime);
		
		List list = findList(hql.toString(),paramList);		
		
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
    
    /**
     * 查询公证处抽选的记录
     * @param selectType 
     * @param String appId
     * @return LIST
     */
    public List findBlockSelectList(String selectType,String appId){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.selectType ='").append(selectType).append("' and a.appId ='").append(appId).append("'");
        List list = findList(hql.toString());
        if(null != list && list.size()>0){
            return list;
        }else{
            return null;
        }
    }
	/**
	 * 查出某一地块已选过的对象的Id
	 */
	public List findSecondStageList(String appId){
		StringBuffer hql = new StringBuffer("select a.selectedId from TdscBlockSelectApp a where a.appId ='").append(appId).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 查出今天已选的对象的Id
	 * @param nowTime
	 * @return
	 */
	public List findTodaySelectedIdList(Date nowTime){
		StringBuffer hql = new StringBuffer("select a.selectedId from TdscBlockSelectApp a where a.selectDate = ?'");
		
		List paramList = new ArrayList();
		paramList.add(nowTime);
		
		List list = findList(hql.toString(),paramList);		
		
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
    
    public int getCountByAppId(String appId,String replyStatus[],String selectType){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId = '").append(appId).append("'") ;
        if(replyStatus!=null&&replyStatus.length>0){
            if(!"".equals(replyStatus[0])) hql.append("and ( a.replyStatus='").append(replyStatus[0]).append("'");
            if(!"".equals(replyStatus[1])) hql.append("or a.replyStatus='").append(replyStatus[1]).append("'");
            if(!"".equals(replyStatus[2])) hql.append("or a.replyStatus='").append(replyStatus[2]).append("')");
            else hql.append(")");
        }        
        if(!"".equals(selectType)) hql.append("and a.selectType='").append(selectType).append("'");
        String countHql = "select count(*) " + hql.toString();
        return this.getCount(countHql);
    }
    
    public List findListByAppIdAndisValid (String appId){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId = '").append(appId).append("'") ;
        hql.append(" and a.isValid ='").append(GlobalConstants.SELECT_RESULT_VALID).append("'");
        String queryHql = hql.toString() + " order by a.selectNum";
        return this.findList(queryHql.toString());
    }
    
    public List findSelectAppbyAppIdAndSelectType(String appId,String selectType){
        StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId='").append(appId).append("'");
        hql.append("and a.selectType='").append(selectType).append("'");
        return this.findList(hql.toString());

    }
    
    public TdscBlockSelectApp findSelectAppByAppIdSelectNum(String appId,String selectNum){
    	 StringBuffer hql = new StringBuffer("from TdscBlockSelectApp a where a.appId='").append(appId).append("'");
         hql.append("and a.selectNum='").append(selectNum).append("'");
         List list = findList(hql.toString());
 		if(null != list && list.size()>0){
 			return (TdscBlockSelectApp)list.get(0);
 		}else{
 			return null;
 		}
    }
}
