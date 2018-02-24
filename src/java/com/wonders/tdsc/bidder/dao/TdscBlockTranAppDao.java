package com.wonders.tdsc.bidder.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.types.Date;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.exception.DataAccessException;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;

public class TdscBlockTranAppDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBlockTranApp.class;
    }

    /**
     * 根据条件获得一页的用户列表
     * 
     * @param condition
     *            查询条件对象
     * @return PageList对象
     */
    public PageList findPageList(TdscBidderCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.blockNoticeNo";

        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
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

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
            whereClause.append(" and a.blockNoticeNo like '%").append(condition.getBlockNoticeNo().trim())
                    .append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getAppId())) {
            whereClause.append(" and a.appId = '").append(condition.getAppId()).append("'");
        }
        return whereClause.toString();
    }

    /**
     * 根据appId查询 地块的 保证金截至时间 MARGIN_END_DATE
     * 
     */
    public TdscBlockTranApp findMarginEndDate(String appId) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId ='").append(appId).append("'");
        return (TdscBlockTranApp) this.findList(hql.toString());
    }

    public TdscBlockTranApp getMarginEndDate(String appId) {
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId = '").append(appId).append("'");
        List list = findList(hql.toString());
        if (list != null && list.size() > 0) {
            TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) list.get(0);
            return tdscBlockTranApp;
        } else {
            return null;
        }
    }

    public TdscBlockTranApp queryByBlockNoticeNo(String blockNoticeNo) {
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.blockNoticeNo = '").append(blockNoticeNo)
                .append("'");
        List list = findList(hql.toString());
        if (list != null && list.size() > 0) {
            TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) list.get(0);
            return tdscBlockTranApp;
        } else {
            return null;
        }
    }

    /**
     * 通过appId 获得所有参加地块申请List
     * 
     * @param appId
     * @return
     */
    public List findBiddCountByAppId(String appId) {
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId = '").append(appId).append("'");
        List list = findList(hql.toString());
        return list;
    }

    public PageList findBidderPageList(TdscBaseQueryCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where 1=1 ");

        // 组装条件语句
        hql.append(makeBidderWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.blockNoticeNo";

        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }

    private String makeBidderWhereClause(TdscBaseQueryCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
            whereClause.append(" and a.blockNoticeNo like '%").append(condition.getBlockNoticeNo().trim())
                    .append("%' ");
        }

        // 地块名称
        if (StringUtils.isNotEmpty(condition.getBlockName())) {
            whereClause.append(" and a.blockName like '%").append(condition.getBlockName()).append("%' ");
        }
        /**
         * //交易方式 数字类型转换 这里用import org.apache.commons.lang.NumberUtils; 不对 ？ if(StringUtils.isNotEmpty(condition.getTransferMode())){
         * whereClause.append(" and a.blockName like '%").append(condition.getBidderName()).append("%' "); }
         */
        return whereClause.toString();
    }

    public String updateAppSupNum(String appId) {
        String retVal = "";
        TdscBlockTranApp tdscBlockTranApp = null;
        try {
            // 从唯一序列生成表中获得当前的唯一序列对象，并且将表锁住
            String queryHql = "from TdscBlockTranApp a where a.appId = ?";
            Query query = getSession().createQuery(queryHql);
            query.setParameter(0, appId);
            query.setLockMode("a", LockMode.UPGRADE);

            // 唯一序列对象存在则根据步长更新唯一序列号，否则新增唯一序列对象
            tdscBlockTranApp = (TdscBlockTranApp) query.uniqueResult();

            if (tdscBlockTranApp == null) {
                // 新增地块交易信息表的一条记录，申请材料发放数设为１
                // tdscBlockTranApp = new TdscBlockTranApp();
                // tdscBlockTranApp.setAppSupNum(new BigDecimal(1));
                // save(tdscBlockTranApp);
            } else {
                // 更新地块交易信息表－申请材料发放数
                BigDecimal appSupNum = tdscBlockTranApp.getAppSupNum();

                if (appSupNum == null || appSupNum.intValue() == 0) {
                    tdscBlockTranApp.setAppSupNum(new BigDecimal(1));
                    retVal = "1";
                } else {
                    tdscBlockTranApp.setAppSupNum(new BigDecimal(tdscBlockTranApp.getAppSupNum().intValue() + 1));
                    retVal = tdscBlockTranApp.getAppSupNum().toString();

                }

                update(tdscBlockTranApp);
            }
        } catch (HibernateException e) {
            throw new DataAccessException(e.getMessage());
        }
        return retVal;
    }
    
    /**
     * 
     * @param noticeId
     * @param isStatus 是否是 进入限时竞价的地块
     * @return
     */
	public List findAppListByNoticeId(String noticeId) {
		StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.noticeId = '").append(noticeId).append("'");
		hql.append(" order by a.xuHao");
        List list = findList(hql.toString());
        return list;
	}

    /**
     * 
     * @param noticeId
     * @param isStatus 是否是 进入限时竞价的地块
     * @return
     */
	public List findAppListByNoticeId(String noticeId, boolean isStatus) {
		StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.noticeId = '").append(noticeId).append("'");
		if (isStatus) {
			hql.append(" and (a.tradeStatus = '1' or a.tradeStatus = '2' or a.tradeStatus = '3')");
		}
		hql.append(" order by a.xuHao");
        List list = findList(hql.toString());
        return list;
	}
	
	public List findAppListByPlanId(String planId) {
		StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.planId = '").append(planId).append("'");
		hql.append(" order by a.xuHao");
        List list = findList(hql.toString());
        return list;
	}

//	通过appId查询TdscBlockTranApp表的信息
    public TdscBlockTranApp findTdscBlockTranAppInfo(String appId){
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId = '").append(appId).append("'");
        //hql.append(" order by appId");
        List tranAppInfo = this.findList(hql.toString());
        if(tranAppInfo != null && tranAppInfo.size() > 0){
            return (TdscBlockTranApp)tranAppInfo.get(0);
        }
        return null;
    }

    //通过noticeId查询blockIdList
    public List queryBlockIdListByPlanId(String planId) {
        StringBuffer hql = new StringBuffer(
                "select a.blockId from TdscBlockTranApp a where a.planId = '").append(planId).append("' order by a.xuHao asc");
        return this.findList(hql.toString());
    }
    /**
     * 根据身份认证唯一标识查找竞买受理前所有的有意向地块。
     * @return
     */
    public List findYxBlockListByOrgNo(String orgNo){
    	StringBuffer hql = new StringBuffer("select a from TdscBlockTranApp a,TdscBlockPlanTable b,TdscAppNodeStat c,TdscBidderApp d,TdscBidderPersonApp e");
    	hql.append(" where a.appId=c.appId and a.planId=b.planId and d.appId=a.appId and d.bidderId=e.bidderId");
    	hql.append(" and a.isPurposeBlock='1' and c.nodeId='10' and c.nodeStat='01' and b.listStartDate>sysdate and a.blockNoticeNo is not null and d.certNo is null");
    	hql.append(" and e.orgNo='"+orgNo+"'");
    	List list = this.findList(hql.toString());
    	return list;
    }
    /**
     * 查找竞买受理前所有的有意向地块
     * @return
     */
    public List findYxBlockList(){
    	StringBuffer hql = new StringBuffer("select a from TdscBlockTranApp a,TdscBlockPlanTable b,TdscAppNodeStat c,TdscBidderApp d,TdscBidderPersonApp e");
    	hql.append(" where a.appId=c.appId and a.planId=b.planId and d.appId=a.appId and d.bidderId=e.bidderId");
    	hql.append(" and a.isPurposeBlock='1' and c.nodeId='10' and c.nodeStat='01' and b.listStartDate<=sysdate and a.blockNoticeNo is not null and d.isPurposePerson='1' and d.certNo is null");
    	List list = this.findList(hql.toString());
    	return list;
    }
    /**
     * 查找可支付保证金的所有地块
     * @return
     */
    public PageList findPayBzjBlockList(int pageSize,int currentPage){
    	String query = "select a ";
    	String countquery = "select count(a) ";
    	StringBuffer hql = new StringBuffer("from TdscBlockTranApp a,TdscBlockPlanTable b,TdscAppNodeStat c");
    	hql.append(" where a.appId=c.appId and a.planId=b.planId");
    	hql.append(" and c.nodeId='10' and c.nodeStat='01' and b.listEndDate>=sysdate and a.blockNoticeNo is not null");
    	//hql.append(" order by to_number(substr(a.blockNoticeNo,18,length(a.blockNoticeNo))) desc");
    	PageList pageList = this.findPageListWithHql(countquery + hql.toString(), query + hql.toString(), pageSize, currentPage);
    	return pageList;
    }
    
    
}
