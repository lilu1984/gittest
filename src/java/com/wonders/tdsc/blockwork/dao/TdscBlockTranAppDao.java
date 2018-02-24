package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBlockTranAppCondition;

public class TdscBlockTranAppDao extends BaseHibernateDaoImpl {
	
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockTranApp.class;
	}
	
	public void saveBlockTranApp(TdscBlockTranApp tdscBlockTranApp)
	{
		save(tdscBlockTranApp);		
	}	
	
	public void deleteBlockTranApp(TdscBlockTranApp tdscBlockTranApp)
	{
		delete(tdscBlockTranApp);
	}


	public PageList findPageList(TdscBlockTranAppCondition condition) {
	    // �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer(
				"from TdscBlockTranApp a where 1 = 1");
		
		// ��װ�������
		hql.append(makeWhereClause(condition));
		
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + "";
        
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(),
                condition.getCurrentPage());
		
		return pageList;
	}
	
	
	public TdscBlockTranApp findTdscBlockTranApp(String appId) {
	    // �û���Ϣ��ѯ
		return (TdscBlockTranApp)this.get(appId);
	}
	
    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     * @return String �������
     */
	private String makeWhereClause(TdscBlockTranAppCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// ��װ�������
		if (StringUtils.isNotEmpty(condition.getAppId())) {
			whereClause.append(" and a.blockUsedId like '%").append(
					condition.getAppId().trim()).append("%' ");
		}	
		
		if (StringUtils.isNotEmpty(condition.getBlockId())) {
			whereClause.append(" and a.blockId like '%").append(
					condition.getBlockId().trim()).append("%' ");
		}		
		
        if (StringUtils.isNotEmpty(condition.getPlanId())) {
            whereClause.append(" and a.planId = '").append(condition.getPlanId()).append("' ");
        }
		return whereClause.toString();
	}	
    
    //ͨ��noticeId��noitceNo��ѯ�б�
    public List findTdscBlockTranAppByNoticeNo(String noticeId,String noitceNo) {
        StringBuffer hql = new StringBuffer(
                "from TdscBlockTranApp a where 1 = 1");
        if (StringUtils.isNotEmpty(noticeId)) {
            hql.append(" and a.noticeId = '").append(
                    noticeId).append("' ");
        }
//        if (StringUtils.isNotEmpty(noitceNo)) {
//            hql.append(" and a.noitceNo = '").append(
//                    noitceNo.).append("' ");
//        }        

        return this.findList(hql.toString());
    }
    
    public List findTranAppByNoticeId(String noticeId) {
        StringBuffer hql = new StringBuffer(
                "from TdscBlockTranApp a where a.noticeId = '").append(
                    noticeId).append("' ");
        return this.findList(hql.toString());
    }    
    
    //ͨ��blockId��ѯapplist�б�
    public List findAppListByBlockId(String blockId) {
        StringBuffer hql = new StringBuffer(
                "select a.appId from TdscBlockTranApp a where 1 = 1");
        if (StringUtils.isNotEmpty(blockId)) {
            hql.append(" and a.blockId = '").append(
            		blockId).append("' ");
        }
       
        return  this.findList(hql.toString());
    }
    
    //ͨ��appId��ѯTdscBlockTranApp�����Ϣ
    public TdscBlockTranApp findTdscBlockTranAppInfo(String appId){
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.appId = '").append(appId).append("'");
        //hql.append(" order by appId");
        List tranAppInfo = this.findList(hql.toString());
        if(tranAppInfo != null && tranAppInfo.size() > 0){
            return (TdscBlockTranApp)tranAppInfo.get(0);
        }
        return null;
    }
    public List findTranAppList(TdscBlockTranAppCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));
        
        if (StringUtils.isNotEmpty(condition.getOrderKey())) {
            hql.append(" order by a.").append(condition.getOrderKey()).append(" asc");
        } else {
            hql.append(" order by a.planId");
        }

        String queryHql = hql.toString();
        List result = this.findList(queryHql.toString());
        return result;

    }
    
    public List findTranAppList(String planId) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.planId = '").append(planId).append("'");

        // ��װ�������
        String queryHql = hql.toString() + " order by a.planId";
        List result = this.findList(queryHql.toString());
        return result;

    }
    
    
    
    //ͨ��noticeId��ѯappIdList
    public List queryAppIdListByNoticeId(String noticeId) {
        StringBuffer hql = new StringBuffer(
                "select a.appId from TdscBlockTranApp a where a.noticeId = '").append(
                    noticeId).append("' order by a.appId asc");
        return this.findList(hql.toString());
    }
    
    public TdscBlockTranApp getTdscBlockTranAppInfo(String blockId){
        StringBuffer hql = new StringBuffer("from TdscBlockTranApp a where a.blockId = '").append(blockId).append("'");
        //hql.append(" order by appId");
        List tranAppInfo = this.findList(hql.toString());
        if(tranAppInfo != null && tranAppInfo.size() > 0){
            return (TdscBlockTranApp)tranAppInfo.get(0);
        }
        return null;
    }
    
    //ͨ��noticeId��ѯblockIdList
    public List queryBlockIdListByNoticeId(String noticeId) {
        StringBuffer hql = new StringBuffer(
                "select a.blockId from TdscBlockTranApp a where a.noticeId = '").append(
                    noticeId).append("' order by a.xuHao asc");
        return this.findList(hql.toString());
    }
    
    //ͨ��noticeId��ѯblockIdList
    public List queryBlockIdListByPlanId(String planId) {
        StringBuffer hql = new StringBuffer(
                "select a.blockId from TdscBlockTranApp a where a.planId = '").append(planId).append("' order by a.xuHao asc");
        return this.findList(hql.toString());
    }

	public String getMaxNoticeNo() {
		// TODO Auto-generated method stub ������2010-01
		String sql = "select max(substr(t.blockNoticeNo,12,3)) from TdscBlockTranApp t where t.blockNoticeNo is not null";
		List list = this.findList(sql);
		if (list == null || list.size() == 0) return "0";
		if (list.size() > 0) {
			Object obj = list.get(0);
			if (obj == null) return "0";
			else return (String)list.get(0);
		}
		return "0";
	}

	public String getMaxNoticeNoBlockNoticeNoPrefix(String startindex,String blockNoticeNoPrefix) {
		String sql = "select max(LPAD(substr(t.blockNoticeNo,"+startindex+"),3,'0')) from TdscBlockTranApp t where t.blockNoticeNo is not null and t.blockNoticeNo like '%"+StringUtil.GBKtoISO88591(blockNoticeNoPrefix)+"%'";
		
		List list = this.findList(sql);
		if (list == null || list.size() == 0) return "0";
		if (list.size() > 0) {
			Object obj = list.get(0);
			if (obj == null) return "0";
			else return (String)list.get(0);
		}
		return "0";
	}
	/**
	 * ��ȡ���ý����ʾǰ,���ƽ�����ĵؿ�����
	 * @return
	 */
	public List findResultList(){
		String sql = "select a from TdscBlockTranApp a,TdscBlockPlanTable b where a.planId=b.planId and b.resultShowDate>sysdate and b.listEndDate<sysdate";
		return this.findList(sql.toString());
	}
	/**
	 * ��ȡ�ڹ��ƽ���ʱ��֮ǰ�����еؿ�
	 * @return
	 */
	public List findDijiaList(){
		String sql = "select a from TdscBlockTranApp a,TdscBlockPlanTable b where a.planId=b.planId and b.listEndDate>sysdate";
		return this.findList(sql.toString());
	}
	
	public List findListByTranResult(String tranResult){
		String sql = "select a from TdscBlockTranApp a where a.tranResult='"+tranResult+"'";
		return this.findList(sql.toString());
	}
	
    /**
     * ���ҿ�֧����֤������еؿ�
     * @return
     */
    public PageList findTradeEndList(int pageSize,int currentPage){
    	String query = "select a ";
    	String countquery = "select count(a) ";
		String sql = " from TdscBlockTranApp a where a.tranResult<>'00' order by a.marginEndDate desc,a.resultDate desc";
		query += sql;
		countquery += sql;
		return this.findPageListWithHql(countquery,query,pageSize,currentPage);
    }
}
