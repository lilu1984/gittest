package com.wonders.tdsc.bidder.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;

public class TdscBidderPersonAppDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBidderPersonApp.class;
    }

    public List findTdscBidderPersonAppList(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderId = '").append(bidderId).append("'");
        return this.findList(hql.toString());
    }

    /**
     * ������������û��б�
     * 
     * @param condition��ѯ��������
     * @return PageList����
     */
    public PageList findPageList(TdscBidderCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1 = 1");
        // ��װ�������
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.bidderPersonId";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }
    /**����������ѯTdscBidderPersonAppList
     * @param condition
     * @return
     */
    public List findTdscBidderPersonAppListByCondition(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1=1 ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**����������ѯ
     * @param condition
     * @return
     */
    public PageList findFundQueryList(TdscBidderCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer(
                "b.bidderName,b.bidderId,b.bzjDzqk,b.bzjDzsj,b.bzjDzse,b.bidderBzj from TDSC_BIDDER_APP a, TDSC_BIDDER_PERSON_APP b where a.BIDDER_ID = b.BIDDER_ID and a.APP_ID =")
                .append(condition.getAppId()).append("'");     
        // ��װ�������
        hql.append(makeWhereClause(condition));
        
        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by b.bidderId";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;        
    }

    /**
     * ����bidderId��������˵��б�
     * 
     * @param bidderId
     * @return
     */
    public List findTdscBidderPersonDzqkList(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderId = '").append(bidderId).append("'");
        return this.findList(hql.toString());
    }
    /**
     * ���ݵ������Ϊ������ʷ������ ��ѯ�������б� 
     * @param bidderId
     * @return
     */
    public List findByBiDPerDzqk(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk like '%1000%' and a.bidderId = '").append(bidderId).append("'");
        return this.findList(hql.toString());
    }
    public List findChangePerDzqk(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk not like '%1000%' and a.bidderId = '").append(bidderId).append("'");
        return this.findList(hql.toString());
    }
    /**����������ѯ  ��list_dzzjgl.jsp ��ѯ��ť��
     * @param condition
     * @return
     */
    public List findByBiDPerDzqk(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk like '%1000%' ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**����������ѯ  ��list_bidchange.jsp ��ѯ��ť��
     * @param condition
     * @return
     */
    public List findChangePerDzqk(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk not like '%1000%' ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     * @return String �������
     */

    private String makeWhereClause(TdscBidderCondition condition) {

        StringBuffer whereClause = new StringBuffer();

        if (StringUtils.isNotEmpty(condition.getBidderZh())) {
            whereClause.append(" and a.bidderZh like '%").append(condition.getBidderZh().trim()).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getBidderName())) {
            whereClause.append(" and a.bidderName like '%").append(condition.getBidderName()).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getBidderId())) {
            whereClause.append(" and a.bidderId like '%").append(condition.getBidderId()).append("%' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getPurposeAppId())) {
            whereClause.append(" and a.purposeAppId like '%").append(condition.getPurposeAppId()).append("%' ");
        }
        return whereClause.toString();
    }

    /**
     * ͨ��bidderId���һ��������Ϣ
     * 
     * @param bidderId
     * @return TdscBidderPersonApp
     */
    public TdscBidderPersonApp getOneBidderByBidderId(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderId = '").append(bidderId).append("'");
        List list = findList(hql.toString());
        if(list!=null && list.size()>0)
        return (TdscBidderPersonApp)list.get(0);
        return null;
    }

//    public TdscBidderPersonApp getBidderByBidderId(String bidderId) {
//        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderId = '").append(bidderId).append("'");
//        List list = findList(hql.toString());
//        if (list != null && list.size() > 0) {
//            TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) list.get(0);
//            return tdscBidderPersonApp;
//        } else {
//            return null;
//        }
//    }

    /**
     * ͨ��appId���һ��������Ϣ
     * 
     * @param appId
     * @return TdscBidderPersonApp
     */
//    public TdscBidderPersonApp getBidderByAppId(String appId) {
//        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.appId = '").append(appId).append("'");
//        List list = findList(hql.toString());
//        if (list != null && list.size() > 0) {
//            TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) list.get(0);
//            return tdscBidderPersonApp;
//        } else {
//            return null;
//        }
//    }
    
    /**
     * ͨ��Bidder_ID���һ��������Ϣ,���ؾ���������
     * @param bidderId
     * @return
     */
    public String getBidderName(String bidderId) {
    	
    	StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1=1");
    	if (StringUtils.isNotEmpty(bidderId)) {
    		hql.append(" and a.bidderId ='").append(bidderId).append("' ");
    	}
		hql.append(" and( a.isHead is null or a.isHead ='1') ");
    	List list = this.findList(hql.toString());
    	
    	TdscBidderPersonApp tdscBidderPersonApp=(TdscBidderPersonApp)list.get(0);
    	return tdscBidderPersonApp.getBidderName();
    }
    /**add by smw*/
    public List getBidderNameByBidderId(String bidderId) {
    	
    	StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1=1");
    	if (StringUtils.isNotEmpty(bidderId)) {
    		hql.append(" and a.bidderId ='").append(bidderId).append("' ");
    	}
    	List list = this.findList(hql.toString());
    	return list;
    }
    
    /**
     * ���ݾ��������Ʋ�ѯ������ϢID��LIST����
     * @param bidderName
     * @return
     */
    public List getBidderIdListByBidderName(String bidderName) {
    	StringBuffer hql = new StringBuffer("select distinct a.bidderId from TdscBidderPersonApp a where  a.bidderName like '%").append(StringUtil.GBKtoISO88591(sqlFilter_first(bidderName).trim())).append("%' escape '/' ");
    	List list = this.findList(hql.toString());
    	return list;
    }
    
	public  String sqlFilter_first(String hql){
        hql = hql.replaceAll("%", "/%");
        hql = hql.replaceAll("_", "/_");
        return hql;
    }

	public List likeTdscBidderPersonAppByBidderName(String bidderName) {
		StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1=1");
    	if (StringUtils.isNotEmpty(bidderName)) {
    		hql.append(" and a.bidderName like '%").append(bidderName).append("%' ");
    	}
    	return  this.findList(hql.toString());
	}   

	/**
	 *  �õ���������Ϣ
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkJmrName(String bidderName) {
		StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderName = '").append(StringUtil.GBKtoISO88591(bidderName)).append("' ");
		return this.findList(hql.toString());
	}
	
}
