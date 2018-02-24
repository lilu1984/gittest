package com.wonders.tdsc.bidder.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderMaterial;


public class TdscBidderMaterialDao extends BaseHibernateDaoImpl{

    protected Class getEntityClass() {
        return TdscBidderMaterial.class;
    }

    /**
     * ͨ��bidderPersonId���һ��������Ϣ
     * @param bidderPersonId
     * @return  TdscBidderPersonApp
     */
    public List getBidderByBidderId(String bidderPersonId){
        StringBuffer hql = new StringBuffer("from TdscBidderMaterial a where a.bidderPersonId = '").append(bidderPersonId).append("'");
        List list = findList(hql.toString());
        return list;
    }
    
    /**
     * ͨ��bidderPersonId���һ��û��bidderId�ľ�����Ϣ
     * @param bidderPersonId
     * @return  TdscBidderPersonApp
     */
    public List getBidderWithOutBidderId(String bidderPersonId){
        StringBuffer hql = new StringBuffer("from TdscBidderMaterial a where a.bidderId is null and a.bidderPersonId = '").append(bidderPersonId).append("'");
        List list = findList(hql.toString());
        return list;
    }
    
    /**
     * ����bidderPersonId��ѯ�ֶ���ӵĲ�����Ϣ
     * @param bidderPersonId
     * @return
     */
    public List getOtherMateByPersonId(String bidderPersonId){
        StringBuffer hql = new StringBuffer("from TdscBidderMaterial a where a.bidderPersonId = '").append(bidderPersonId).append("'").append(" and a.materialCode is not null");
        List list = findList(hql.toString());
        return list;
    }
    
    public List getOneBidByBidderId(String bidderId){
        StringBuffer hql = new StringBuffer("from TdscBidderMaterial a where a.bidderId = '").append(bidderId).append("'");
        List list = findList(hql.toString());
        return list;
    }
}
