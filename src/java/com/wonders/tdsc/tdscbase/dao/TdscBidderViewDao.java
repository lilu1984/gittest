package com.wonders.tdsc.tdscbase.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;

public class TdscBidderViewDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {

		return TdscBidderView.class;
	}

	public List queryBidderByNameKahao(TdscBidderCondition bidderCond) {

		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		if (!StringUtils.isEmpty(bidderCond.getYktBh()))
			sql.append(" and a.yktBh='").append(bidderCond.getYktBh()).append("'");
		if (!StringUtils.isEmpty(bidderCond.getBidderName()))
			sql.append(" and a.bidderName like '%").append((bidderCond.getBidderName())).append("%'");

		List list = this.findList(sql.toString());
		return list;

	}

	public TdscBidderView getBidderViewByPersonName(String bidderName) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		if (!StringUtils.isEmpty(bidderName))
			sql.append(" and a.bidderName like '%").append(StringUtil.GBKtoISO88591(bidderName)).append("%'");

		List list = this.findList(sql.toString());
		if (list != null && list.size() > 0)
			return (TdscBidderView) list.get(0);
		return null;
	}

	public TdscBidderView queryBidderViewInfo(TdscBidderCondition bidderCond) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		if (!StringUtils.isEmpty(bidderCond.getBidderId()))
			sql.append(" and a.bidderId='").append(bidderCond.getBidderId()).append("'");
		List list = this.findList(sql.toString());
		if (list != null && list.size() > 0)
			return (TdscBidderView) list.get(0);
		return null;
	}
	
	public TdscBidderView getBidderView(String bidderId) {
		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setBidderId(bidderId);
		return queryBidderViewInfo(bidderCondition);
	}

	public TdscBidderView getYixiangNameLikeAppId(String appId) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		sql.append(" and a.purposeAppId like '%").append(appId).append("%'");
		List list = this.findList(sql.toString());
		if (list != null && list.size() > 0)
			return (TdscBidderView) list.get(0);
		return null;
	}
	
	public TdscBidderView getBidderViewByCertNo(String certNo) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		sql.append(" and a.certNo ='").append(certNo).append("'");
		List list = this.findList(sql.toString());
		if (list != null && list.size() > 0)
			return (TdscBidderView) list.get(0);
		return null;
	}
	
	public List getBidderViewListByUserId(String userId) {
		List list = getBidderViewByAppId(null, userId);
		return list;
	}
	
	public List getBidderViewByAppId(String appId, String userId) {
		StringBuffer sql = new StringBuffer("select a from TdscBidderView a,TdscBlockTranApp b,TdscNoticeApp c where 1=1 and a.appId=b.appId and b.noticeId=c.noticeId ");
		if (StringUtils.isNotEmpty(userId)) {
			sql.append(" and a.userId ='").append(userId).append("'");
		}
		if (StringUtils.isNotEmpty(appId)) {
			sql.append(" and a.appId ='").append(appId).append("'");
		}
		sql.append(" and a.certNo is not null and a.conNum is not null and a.ifCommit = '1' order by c.noticeDate desc,b.blockNoticeNo asc");
		List list = this.findList(sql.toString());
		return list;
	}

	public List queryBiddersByAppId(String appId) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		sql.append(" and a.appId like '%").append(appId).append("%'");
		return this.findList(sql.toString());
	}
	
	public List queryBidderViewListByPersonName(String bidderName) {
		StringBuffer sql = new StringBuffer("from TdscBidderView a where 1=1");
		if (!StringUtils.isEmpty(bidderName)){
			sql.append(" and a.bidderName = '").append(StringUtil.GBKtoISO88591(bidderName)).append("'");			
		}

		return this.findList(sql.toString());
	}
	
	/**
     * 查找已付保证金的所有竞买人
     * @return
    */
    public List findPayBzjBidderList(String appId){
    	StringBuffer hql = new StringBuffer("from TdscBidderView t where 1=1");
    	hql.append(" and t.appId like '%").append(appId).append("%' and t.sgNumber is not null");
    	List list = this.findList(hql.toString());
    	return list;
    }

    /**
     * 查找我的交易
     * @param appId
     * @param bidderId
     * @return
     */
	public TdscBidderView getMyTrade(String appId, String bidderId) {
		StringBuffer hql = new StringBuffer(" from TdscBidderView a where 1=1");
		hql.append(" and a.appId = '").append(appId).append("' and a.bidderId = '").append(bidderId).append("'");
		List list = this.findList(hql.toString());
		if(list != null && list.size() > 0)
			return (TdscBidderView) list.get(0);
		return null;
	} 
    /**
     * 根据userId取得申购列表
     * @param userId
     * @return
     */
    public List queryBidderOrdersList(String userId){
    	StringBuffer hql=new StringBuffer("from TdscBidderView t where 1=1");
    	hql.append(" and t.userId= '").append(userId).append("'").append(" order by t.sgDate desc");
    	List list=this.findList(hql.toString());
    	return list;
    }
}
