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
     * 根据条件获得用户列表
     * 
     * @param condition查询条件对象
     * @return PageList对象
     */
    public PageList findPageList(TdscBidderCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1 = 1");
        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.bidderPersonId";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }
    /**根据条件查询TdscBidderPersonAppList
     * @param condition
     * @return
     */
    public List findTdscBidderPersonAppListByCondition(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where 1=1 ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**根据条件查询
     * @param condition
     * @return
     */
    public PageList findFundQueryList(TdscBidderCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer(
                "b.bidderName,b.bidderId,b.bzjDzqk,b.bzjDzsj,b.bzjDzse,b.bidderBzj from TDSC_BIDDER_APP a, TDSC_BIDDER_PERSON_APP b where a.BIDDER_ID = b.BIDDER_ID and a.APP_ID =")
                .append(condition.getAppId()).append("'");     
        // 组装条件语句
        hql.append(makeWhereClause(condition));
        
        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by b.bidderId";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;        
    }

    /**
     * 根据bidderId查出竞买人的列表
     * 
     * @param bidderId
     * @return
     */
    public List findTdscBidderPersonDzqkList(String bidderId) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderId = '").append(bidderId).append("'");
        return this.findList(hql.toString());
    }
    /**
     * 根据到账情况为“无历史操作” 查询竞买人列表 
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
    /**根据条件查询  （list_dzzjgl.jsp 查询按钮）
     * @param condition
     * @return
     */
    public List findByBiDPerDzqk(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk like '%1000%' ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**根据条件查询  （list_bidchange.jsp 查询按钮）
     * @param condition
     * @return
     */
    public List findChangePerDzqk(TdscBidderCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bzjDzqk not like '%1000%' ");
        hql.append(makeWhereClause(condition));
        return this.findList(hql.toString());
    }
    /**
     * 根据用户查询条件对象组装条件语句
     * 
     * @param condition
     *            用户查询条件对象
     * @return String 条件语句
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
     * 通过bidderId获得一个竞买信息
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
     * 通过appId获得一个竞买信息
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
     * 通过Bidder_ID获得一个竞买信息,返回竞买人名称
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
     * 根据竞买人名称查询竞买信息ID的LIST集合
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
	 *  得到竞买人信息
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkJmrName(String bidderName) {
		StringBuffer hql = new StringBuffer("from TdscBidderPersonApp a where a.bidderName = '").append(StringUtil.GBKtoISO88591(bidderName)).append("' ");
		return this.findList(hql.toString());
	}
	
}
