package com.wonders.tdsc.blockwork.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBlockPart;

public class TdscBlockPartDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        // TODO Auto-generated method stub
        return TdscBlockPart.class;
    }
    /**
     * 根据地块的Id查询所有子地块信息
     * @param blockId
     * @return
     */
    public List getTdscBlockPartList(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("from TdscBlockPart a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
    
    /**
     * 根据BlockCode查询所有子地块信息
     * @param blockCode
     * @return
     */
    public List getblockPartListByBlockCode(String blockId, String blockCode)
    { 
        if(null!=blockCode&&!"".equals(blockCode)){
           blockCode = StringUtil.GBKtoISO88591(blockCode);
           StringBuffer hql = new StringBuffer("from TdscBlockPart a where a.blockCode ='").append(blockCode).append("' and a.blockId = '").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
    
    /**
     * 通过条件来查询子地块信息
     * 
     * @param blockId
     * @return
     */
    public List queryBidderInfoListByAppId(List blockidList) {
    	List list = new ArrayList();
        if (blockidList != null && blockidList.size() > 0) {
            StringBuffer hql = new StringBuffer("from TdscBlockPart a where a.blockId in (:blockidList) order by a.blockId asc");
            try {
                Query query = this.getSession().createQuery(hql.toString());
                 query.setParameterList("blockidList", blockidList);
                list = (query.list());
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            return list;
        } else {
            return null;
        }
    }
    
    /**
     * 根据blockId查询出不重复的子地块的地块编号
     * @param blockId
     * @return
     */
    public List getDistinctBlockPartList(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("select distinct a.blockCode from TdscBlockPart a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
    
    
    /**
     * 根据blockId查询出所有土地用途和出让年限不重复的子地块列表
     * @param blockId
     * @return
     */
    public List getDistinctUseTypeAndTransferLifeList(String blockId)
    {
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("select distinct a.landUseType, a.transferLife from TdscBlockPart a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
    
    /**
	  *  根据block_id查询BlockCode字段不重复的子地块信息，暂时用于取出某个地块的所有不重复地块编号BlockCode
	  * @param blockId
	  * @return
	  */
    public List getTdscBlockPartListInBlockCode(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("select distinct a.blockCode from TdscBlockPart a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
}
