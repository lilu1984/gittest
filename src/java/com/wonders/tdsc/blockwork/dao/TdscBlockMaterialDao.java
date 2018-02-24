package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockMaterial;
import com.wonders.tdsc.bo.condition.TdscBlockMaterialCondition;

public class TdscBlockMaterialDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockMaterial.class;
	}
	
	public void saveTdscBlockMaterial(TdscBlockMaterial tdscBlockMaterial)
	{			
		save(tdscBlockMaterial);	
	}
	
	public void deleteTdscBlockMaterial(TdscBlockMaterial tdscBlockMaterial)
	{
		delete(tdscBlockMaterial);
	}

	public List findMaterialList(TdscBlockMaterialCondition condition) {
	    // 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBlockMaterial a where 1 = 1 ");
		
		// 组装条件语句
		hql.append(makeWhereClause(condition));		
		hql.append(" order by a.materialNum");
		
		return findList(hql.toString());
	}
	
	public TdscBlockMaterial findTdscBlockMaterial(String materialId) {
		return (TdscBlockMaterial)this.get(materialId);
	}
    /**
     * 根据用户查询条件对象组装条件语句
     * 
     * @param condition
     *            用户查询条件对象
     * @return String 条件语句
     */
	private String makeWhereClause(TdscBlockMaterialCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getMaterialId())) {
			whereClause.append(" and a.materialId like '%").append(
					condition.getMaterialId().trim()).append("%' ");
		}	
		
		if (StringUtils.isNotEmpty(condition.getAppId())) {
			whereClause.append(" and a.appId = '").append(
					condition.getAppId().trim()).append("' ");
		}	
		
		if (condition.getMaterialNum()!=null && StringUtils.isNotEmpty(condition.getMaterialNum().toString())) {
			whereClause.append(" and a.materialNum >= ").append(
					condition.getMaterialNum().toString().trim()).append(" ");
		}			
		
		return whereClause.toString();
	}	
	/**
	 * 根据appId查询材料列表
	 * @param appId
	 * @return
	 */
	public List findMaterialListByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBlockMaterial a where a.appId = '").append(appId).append("'");
		return findList(hql.toString());
	}
}
