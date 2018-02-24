package com.wonders.tdsc.bidder.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBusinessRecord;
import com.wonders.tdsc.bo.condition.TdscBusinessRecordCondition;

public class TdscBidderProvideDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBidderProvide.class;
    }
    /**����provideBm�������������Ƿ����
     * 
     * @param provideBm
     * @return
     */
    public List getProByProvideBm(String provideBm) {
        StringBuffer hql = new StringBuffer("from TdscBidderProvide a where a.provideBm = '").append(provideBm).append("'");
        return findList(hql.toString());      
    } 
    
    public List getTdscBidderProvideByCondition(TdscBusinessRecordCondition condition){
        StringBuffer hql = new StringBuffer("from TdscBidderProvide a where 1 = 1");
        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getAppId())) {
            hql.append(" and a.appId = '").append(condition.getAppId()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getUserId())) {
        	hql.append(" and a.userId = '").append(condition.getUserId()).append("' ");
        }
        //��ѯ���
        List recordList = this.findList(hql.toString());

        return recordList;
        //return (recordList != null && recordList.size() > 0) ? (TdscBidderProvide) recordList.get(0) : null;
    }
    public List getTdscBidderProvideByAppId(TdscBusinessRecordCondition condition){
        StringBuffer hql = new StringBuffer("from TdscBidderProvide a where 1 = 1");
        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getAppId())) {
            hql.append(" and a.appId = '").append(condition.getAppId()).append("' ");
        }
        //��ѯ���
        List recordList = this.findList(hql.toString());

        return recordList;
        //return (recordList != null && recordList.size() > 0) ? (TdscBidderProvide) recordList.get(0) : null;
    }
}
