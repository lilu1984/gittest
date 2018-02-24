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
     * ���ݵؿ��Id��ѯ�����ӵؿ���Ϣ
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
     * ����BlockCode��ѯ�����ӵؿ���Ϣ
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
     * ͨ����������ѯ�ӵؿ���Ϣ
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
     * ����blockId��ѯ�����ظ����ӵؿ�ĵؿ���
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
     * ����blockId��ѯ������������;�ͳ������޲��ظ����ӵؿ��б�
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
	  *  ����block_id��ѯBlockCode�ֶβ��ظ����ӵؿ���Ϣ����ʱ����ȡ��ĳ���ؿ�����в��ظ��ؿ���BlockCode
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
