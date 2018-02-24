package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.bo.condition.TdscBlockUsedInfoCondition;

public class TdscBlockUsedInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockUsedInfo.class;
	}
	
	public void saveTdscBlockUsedInfo(TdscBlockUsedInfo tdscBlockUsedInfo)
	{		
		save(tdscBlockUsedInfo);		
	}	
	
	public void deleteTdscBlockUsedInfo(TdscBlockUsedInfo tdscBlockUsedInfo)
	{
		delete(tdscBlockUsedInfo);
	}
	

	public PageList findPageList(TdscBlockUsedInfoCondition condition) {
	    // �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer(
				"from TdscBlockUsedInfo a where 1 = 1");
		
		// ��װ�������
		hql.append(makeWhereClause(condition));
		
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + "";
        
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(),
                condition.getCurrentPage());
		
		return pageList;
	}
	
	
	public TdscBlockUsedInfo findTdscBlockUsedInfo(String blockUsedId) {
	    // �û���Ϣ��ѯ
		return (TdscBlockUsedInfo)this.get(blockUsedId);
	}
	
    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     * @return String �������
     */
	private String makeWhereClause(TdscBlockUsedInfoCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// ��װ�������
		if (StringUtils.isNotEmpty(condition.getBlockUsedId())) {
			whereClause.append(" and a.blockUsedId like '%").append(
					condition.getBlockUsedId().trim()).append("%' ");
		}	
		
		if (StringUtils.isNotEmpty(condition.getBlockId())) {
			whereClause.append(" and a.blockId like '%").append(
					condition.getBlockId().trim()).append("%' ");
		}			
		return whereClause.toString();
	}	
    
    /**
     * ͨ���ؿ�ID����ѯ������;��Ϣ
     * @param blockId
     * @return
     * 20071117*
     */
    public List queryTdscBlockUsedInfoListByBlockId(String blockId){
        StringBuffer hql = new StringBuffer("from TdscBlockUsedInfo a where a.blockId = '").append(blockId).append("'");
        return this.findList(hql.toString());
    }
}
