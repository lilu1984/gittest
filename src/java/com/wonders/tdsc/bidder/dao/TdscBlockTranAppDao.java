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
     * �����������һҳ���û��б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList����
     */
    public PageList findPageList(TdscBidderCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.blockNoticeNo";

        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
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

        // ��װ�������
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
     * ����appId��ѯ �ؿ�� ��֤�����ʱ�� MARGIN_END_DATE
     * 
     */
    public TdscBlockTranApp findMarginEndDate(String appId) {
        // �û���Ϣ��ѯ
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
     * ͨ��appId ������вμӵؿ�����List
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
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where 1=1 ");

        // ��װ�������
        hql.append(makeBidderWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.blockNoticeNo";

        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }

    private String makeBidderWhereClause(TdscBaseQueryCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
            whereClause.append(" and a.blockNoticeNo like '%").append(condition.getBlockNoticeNo().trim())
                    .append("%' ");
        }

        // �ؿ�����
        if (StringUtils.isNotEmpty(condition.getBlockName())) {
            whereClause.append(" and a.blockName like '%").append(condition.getBlockName()).append("%' ");
        }
        /**
         * //���׷�ʽ ��������ת�� ������import org.apache.commons.lang.NumberUtils; ���� �� if(StringUtils.isNotEmpty(condition.getTransferMode())){
         * whereClause.append(" and a.blockName like '%").append(condition.getBidderName()).append("%' "); }
         */
        return whereClause.toString();
    }

    public String updateAppSupNum(String appId) {
        String retVal = "";
        TdscBlockTranApp tdscBlockTranApp = null;
        try {
            // ��Ψһ�������ɱ��л�õ�ǰ��Ψһ���ж��󣬲��ҽ�����ס
            String queryHql = "from TdscBlockTranApp a where a.appId = ?";
            Query query = getSession().createQuery(queryHql);
            query.setParameter(0, appId);
            query.setLockMode("a", LockMode.UPGRADE);

            // Ψһ���ж����������ݲ�������Ψһ���кţ���������Ψһ���ж���
            tdscBlockTranApp = (TdscBlockTranApp) query.uniqueResult();

            if (tdscBlockTranApp == null) {
                // �����ؿ齻����Ϣ���һ����¼��������Ϸ�������Ϊ��
                // tdscBlockTranApp = new TdscBlockTranApp();
                // tdscBlockTranApp.setAppSupNum(new BigDecimal(1));
                // save(tdscBlockTranApp);
            } else {
                // ���µؿ齻����Ϣ��������Ϸ�����
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
     * @param isStatus �Ƿ��� ������ʱ���۵ĵؿ�
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
     * @param isStatus �Ƿ��� ������ʱ���۵ĵؿ�
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

//	ͨ��appId��ѯTdscBlockTranApp�����Ϣ
    public TdscBlockTranApp findTdscBlockTranAppInfo(String appId){
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId = '").append(appId).append("'");
        //hql.append(" order by appId");
        List tranAppInfo = this.findList(hql.toString());
        if(tranAppInfo != null && tranAppInfo.size() > 0){
            return (TdscBlockTranApp)tranAppInfo.get(0);
        }
        return null;
    }

    //ͨ��noticeId��ѯblockIdList
    public List queryBlockIdListByPlanId(String planId) {
        StringBuffer hql = new StringBuffer(
                "select a.blockId from TdscBlockTranApp a where a.planId = '").append(planId).append("' order by a.xuHao asc");
        return this.findList(hql.toString());
    }
    /**
     * ���������֤Ψһ��ʶ���Ҿ�������ǰ���е�������ؿ顣
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
     * ���Ҿ�������ǰ���е�������ؿ�
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
     * ���ҿ�֧����֤������еؿ�
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
