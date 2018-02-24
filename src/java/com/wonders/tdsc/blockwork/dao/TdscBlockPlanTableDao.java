package com.wonders.tdsc.blockwork.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBlockPlanTableDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBlockPlanTable.class;
    }

    public TdscBlockPlanTable findBlockPlanTableInfo(TdscBlockPlanTableCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.lastActionDate desc";

        List result = this.findList(queryHql.toString());
        if (result != null && result.size() > 0) {
            return (TdscBlockPlanTable) result.get(0);
        } else {
            return null;
        }
    }

    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     *            condtion�����δע����ѯ�Ľ��ȱ�״̬��Ĭ�ϲ�ѯ������Ч�ġ�����״̬��ѯ��״̬����������Ч����ʷ�汾�����߽Բ飩
     * @return String �������
     */
    private String makeWhereClause(TdscBlockPlanTableCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getIfPCommit())) {
            whereClause.append(" and a.ifPCommit = '").append(condition.getIfPCommit()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getTransferMode())) {
        	whereClause.append(" and a.transferMode  = '").append(condition.getTransferMode()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getAppId())) {
            whereClause.append(" and a.appId = '").append(condition.getAppId()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getPlanId())) {
            whereClause.append(" and a.planId = '").append(condition.getPlanId()).append("' ");
        }
        if (condition.getPlanNum() != null) {
            whereClause.append(" and a.planNum = ").append(condition.getPlanNum()).append(" ");
        }

        if (StringUtils.isNotEmpty(condition.getStatus())) {
            if (!GlobalConstants.PLAN_TABLE_ALL.equals(condition.getStatus()))
                whereClause.append(" and a.status = '").append(condition.getStatus()).append("' ");
        } else {
            whereClause.append(" and a.status = '01' ");
        }

        if (StringUtils.isNotEmpty(condition.getPlusCondition())) {
        	String plusCondition = condition.getPlusCondition();
        	plusCondition = plusCondition.replaceAll("TdscBlockPlanTable", "a");
            whereClause.append(" and ").append(plusCondition).append(" ");
        }

        if (StringUtils.isNotEmpty(condition.getPlanId())) {
            whereClause.append(" and a.planId = '").append(condition.getPlanId()).append("' ");
        }

        return whereClause.toString();
    }
    
    /**
     * �ж��Ƿ�ʼ����
     * @return
     */
    public String isStartDate(){
    	String isStart = "true";
    	StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.listStartDate > to_date('' and a.status='"+GlobalConstants.PLAN_TABLE_ACTIVE+"'");
    	List list = this.findList(hql.toString());
    	if(list.size() == 0){
    		isStart = "false";
    	}
    	return isStart;
    }
    /**
     * �ж��Ƿ�ʼ����
     * @return
     */
    public List findListStatusList(){
    	Date date = new Date();
    	StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where (a.listEndDate > " + getNowTime(date) + " and a.listStartDate < " + getNowTime(date) + ") or (a.onLineStatDate is not null and a.onLineEndDate is null)");
    	return this.findList(hql.toString());
    }

    /**
     * �ж��Ƿ�ʼ����,���ǲ�Ϊ��ʼ����
     * @return
     */
    public List findTradeNoticeNotStart(){
    	Date date = new Date();
    	StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.listEndDate > " + getNowTime(date) + " and a.listStartDate < " + getNowTime(date) + "");
    	return this.findList(hql.toString());
    }
    
   /**
    * �ж��Ƿ�����ѽ���
    * @return
    */
    public String isEndDate(){
    	String isEnd = "true";
    	Date date = new Date();
    	StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.listEndDate > " + getNowTime(date) + " and a.status='"+GlobalConstants.PLAN_TABLE_ACTIVE+"'");
    	List list = this.findList(hql.toString());
    	if(list.size() == 0){
    		isEnd = "false";
    	}
    	return isEnd;
    }
    
    /**
     * ͨ��appId��ѯ���õؿ���Ȱ��ű���Ϣ
     * @param appId
     * 20071201*
     */
    public TdscBlockPlanTable findPlanTableInfo(String appId){
        
        StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.appId = '").append(appId).append("'");
        hql.append(" and a.status = '01' ");
        String queryHql = hql.toString() + " order by a.lastActionDate desc";
        List planInfoList = this.findList(queryHql.toString());
        if(planInfoList != null && planInfoList.size() > 0){
            return (TdscBlockPlanTable) planInfoList.get(0);
        }
        return null;
    }
    
    public List queryUnitePlanTable(){
        StringBuffer queryHql = new StringBuffer("from TdscBlockPlanTable a where a.status = '01' order by a.lastActionDate desc");
//        String queryHql = hql.toString() + " order by a.unitePlanId";
        List result = this.findList(queryHql.toString());
    	return result;
    }
    
    
    public List findBlockPlanTableList(TdscBlockPlanTableCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where 1 = 1");
        if (StringUtils.isNotEmpty(condition.getBlockName())) {
        	hql.append(" and a.blockName like :blockName ");
        	
        }
        if (StringUtils.isNotEmpty(condition.getTradeNum())) {
        	hql.append(" and a.tradeNum like :tradeNum ");
        }
        if (StringUtils.isNotEmpty(condition.getLandLocation())) {
        	hql.append(" and a.landLocation like :landLocation ");
        }
        if (StringUtils.isNotEmpty(condition.getUniteBlockCode())) {
        	hql.append(" and a.uniteBlockCode like :uniteBlockCode ");
        }
        
        if(condition.getStatusFlowList()!=null&&condition.getStatusFlowList().size()>0){
        	hql.append(" and a.statusFlow in (:statusFlowList)");
        }
        if (StringUtils.isNotEmpty(condition.getUserId())) {
        	hql.append(" and a.userId =:userId ");
        }
        // ��װ�������
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.lastActionDate desc";
        
        Query query = this.getSession().createQuery(queryHql.toString());
        
        if (StringUtils.isNotEmpty(condition.getBlockName())) {
            query.setParameter("blockName", "%" + (StringUtil.GBKtoISO88591(condition.getBlockName())) + "%");
        }
        if (StringUtils.isNotEmpty(condition.getTradeNum())) {
            query.setParameter("tradeNum", "%" + (StringUtil.GBKtoISO88591(condition.getTradeNum())) + "%");
        }
        if (StringUtils.isNotEmpty(condition.getLandLocation())) {
            query.setParameter("landLocation", "%" + (StringUtil.GBKtoISO88591(condition.getLandLocation())) + "%");
        }
        if (StringUtils.isNotEmpty(condition.getUniteBlockCode())) {
            query.setParameter("uniteBlockCode", "%" + (StringUtil.GBKtoISO88591(condition.getUniteBlockCode())) + "%");
        }
        if(condition.getStatusFlowList()!=null&&condition.getStatusFlowList().size()>0){
        	query.setParameterList("statusFlowList", condition.getStatusFlowList());
        }
        if (StringUtils.isNotEmpty(condition.getUserId())) {
            query.setParameter("userId", condition.getUserId());
        }
        List result = (List)query.list();
        //List result = this.findList(queryHql.toString());
        return result;
    }
    /**
     * У�����Ĺұ���Ƿ��ظ�ʹ��
     * @param tradeNum
     * @return
     */
    public boolean checkTradeNumAjax(String tradeNum,String planId){
        StringBuffer queryHql = new StringBuffer("from TdscBlockPlanTable a where a.tradeNum = '").append(StringUtil.GBKtoISO88591(tradeNum)).append("'");
        if(planId!=null && !"".equals(planId)){
        	queryHql.append(" and a.planId not in ('").append(planId).append("')");
        }
        
        List result = this.findList(queryHql.toString());
        if(result==null || result.size()==0){
        	return true;
        }else{
        	return false;
        }
    }
    public boolean checkListingEndDate(String listingEndDate,String planId){
    	
    	String sql = "from TdscBlockPlanTable a where to_char(a.listEndDate,'yyyy-MM-dd')='"+listingEndDate+"'";
    	if(StringUtils.isNotBlank(planId)){
    		sql+=" and a.planId not in ('"+planId+"')";
    	}
    	List result = this.findList(sql);
        if(result==null || result.size()==0){
        	return true;
        }else{
        	return false;
        }
    }
    
    /**
     * ����planId�б���
     * @param planIdList
     * @return
     */
    public List queryBlockPlanTableByPlanIdList(List planIdList) {
    	String sql = "from TdscBlockPlanTable t where t.status = '01' and t.planId in (:planIdList) order by t.tradeNum";
    	Query query = this.getSession().createQuery(sql);
    	query.setParameterList("planIdList", planIdList);
    	List result = query.list();
    	return result;
    }
    
    public String getCurrTradeNum(String startindex,String tradeNumPrefix){
		String sql = "select max(substr(t.tradeNum,"+startindex+",3)) from TdscBlockPlanTable t where t.tradeNum is not null and t.tradeNum like '%"+StringUtil.GBKtoISO88591(tradeNumPrefix)+"%'";
    	List list = this.findList(sql);
		if (list == null || list.size() == 0) return "0";
		if (list.size() > 0) {
			Object obj = list.get(0);
			if (obj == null) return "0";
			else return (String)list.get(0);
		}
		return "0";
    }
   //ͨ�����Ƶõ��ؿ���Ϣ 
    public List findPlanTableInfoByName(String blockName){
        
        StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.blockName = '").append(blockName).append("'");
        hql.append(" and a.status = '01' ");
        String queryHql = hql.toString() + " order by a.lastActionDate desc";
        List planInfoList = this.findList(queryHql.toString());
        return planInfoList;
    }

	public List findPlanTableListInTrade() {
		Date date = new Date();
		StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.ifOnLine = '1' and a.issueStartDate < " + getNowTime(date) + " and a.onLineEndDate is null");
        hql.append(" and a.status = '01' ");
        String queryHql = hql.toString() + " order by a.lastActionDate desc";
        List planInfoList = this.findList(queryHql.toString());
        return planInfoList;
	}
	
	private String getNowTime(Date nowDate) {
		String getNowDateStr = "to_date('" + DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss")+ "','yyyy-MM-dd HH24:MI:SS')";
		return getNowDateStr;
	}

	public List findPlanTableListInPublicTrading() {
		Date date = new Date();
		StringBuffer hql = new StringBuffer("from TdscBlockPlanTable a where a.ifOnLine = '1' and a.issueStartDate < " + getNowTime(date) + " and to_char(a.listEndDate, 'yyyy-MM-dd') >= '" + DateUtil.date2String(date, "yyyy-MM-dd") +"'");
        String queryHql = hql.toString() + " order by a.issueStartDate";
        List planInfoList = this.findList(queryHql.toString());
        return planInfoList;
	}
    
}