package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockConInfo;
import com.wonders.tdsc.bo.condition.TdscBlockConInfoCondition;

public class TdscBlockConInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockConInfo.class;
	}

	public void saveTdscBlockConInfo(TdscBlockConInfo tdscBlockConInfo)
	{	
		save(tdscBlockConInfo);
	}
	
	public void deleteBlockConInfo(TdscBlockConInfo tdscBlockConInfo)
	{
		delete(tdscBlockConInfo);
	}

	public List findPageList(TdscBlockConInfoCondition condition) {
	    // �ؿ��ͬǩ��������ѯ
		StringBuffer hql = new StringBuffer("from TdscBlockConInfo a where 1 = 1");
		
		// ��װ�������
		hql.append(makeWhereClause(condition));
		
		return findList(hql.toString());		
	}

    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     * @return String �������
     */
	private String makeWhereClause(TdscBlockConInfoCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// ��װ�������
		if (StringUtils.isNotEmpty(condition.getBlockConId())) {
			whereClause.append(" and a.blockConId like '%").append(
					condition.getBlockConId().trim()).append("%' ");
		}	
		
		if (StringUtils.isNotEmpty(condition.getBlockId())) {
			whereClause.append(" and a.blockId like '%").append(
					condition.getBlockId().trim()).append("%' ");
		}			
		return whereClause.toString();
	}
}
