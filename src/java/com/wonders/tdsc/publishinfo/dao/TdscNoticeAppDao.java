package com.wonders.tdsc.publishinfo.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;

public class TdscNoticeAppDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscNoticeApp.class;
    }

    public PageList findPageList(TdscNoticeAppCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.recordId desc";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }

    private String makeWhereClause(TdscNoticeAppCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // ��װ�������
        if (StringUtils.isNotBlank(condition.getIfResultPublish())) {
        	if("1".equals(condition.getIfResultPublish())){
        		whereClause.append(" and a.ifResultPublish = '").append(condition.getIfResultPublish()).append("' ");
        	}else if("0".equals(condition.getIfResultPublish())){
        		whereClause.append(" and (a.ifResultPublish = '0' or a.ifResultPublish is null) ");
        	}
        }
        if (StringUtils.isNotEmpty(condition.getIfReleased())) {
            whereClause.append(" and a.ifReleased = '").append(condition.getIfReleased()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getNoticeStatus())) {
            whereClause.append(" and a.noticeStatus = '").append(condition.getNoticeStatus()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getNoticeNo())) {
            whereClause.append(" and a.noticeNo like '%").append(StringUtil.GBKtoISO88591(condition.getNoticeNo().trim())).append("%' ");
        }
        if (StringUtils.isNotEmpty(condition.getTradeNum())) {
            whereClause.append(" and a.tradeNum like '%").append(StringUtil.GBKtoISO88591(condition.getTradeNum().trim())).append("%' ");
        }
        if (StringUtils.isNotEmpty(condition.getTransferMode())) {
            whereClause.append(" and a.transferMode = '").append(condition.getTransferMode()).append("' ");
        }
		if (StringUtils.isNotEmpty(condition.getUserId())) {
			whereClause.append(" and a.userId = '").append(condition.getUserId()).append("' ");
		}
        return whereClause.toString();
    }
    
    public List queryNoticeList(){
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1 ").append(" order by a.noticeDate desc");

		return findList(hql.toString());
    }
    public List queryNoticeListByCondition(TdscNoticeAppCondition condition){
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1 ");//.append(" order by a.noticeDate desc");
        // ��װ�������
        hql.append(makeWhereClause(condition));
        hql.append(" order by a.noticeId desc");
        
		return findList(hql.toString());
    }
    /**
     * ��ȡ���ù����б�
     * @param condition
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageList getAssignNoticePageList(TdscNoticeAppCondition condition){
		StringBuffer hql=new StringBuffer("from TdscNoticeApp a where 1 = 1 ");
		hql.append(" and a.ifReleased='1' and a.noticeType='01'");
		String countHql="select count(*)"+hql.toString();
		String queryHql=hql.toString()+"order by a.noticeDate desc";
		PageList pageList=findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
		return pageList;
	}
	/**
	 * ��ȡ�ɽ������б�
	 * @param condition
	 * @return
	 */
	public PageList getTradeNoticePageList(TdscNoticeAppCondition condition){
		StringBuffer hql=new StringBuffer("from TdscNoticeApp a where 1 = 1 ");
		hql.append(" and a.ifResultPublish='1' and a.noticeType='01'");
		String countHql="select count(*)"+hql.toString();
		String queryHql=hql.toString()+"order by a.resultPublishDate desc";
		PageList pageList=findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
		return pageList;
	}
}
