package com.wonders.tdsc.presell.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.FileRef;

public class FileRefDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return FileRef.class;
	}

	/**
	 * 查找符合条件的对象列表
	 * @param condition
	 * @return
	 */
	public List findListFileRefByCondition(String busId, String catalog) {
		StringBuffer hql = new StringBuffer("FROM FileRef f WHERE 1=1 ");
		if (StringUtils.isNotEmpty(busId)) {
			hql.append(" and f.busId = '" + busId +  "'");
		}
		if (StringUtils.isNotEmpty(catalog)) {
			hql.append(" and f.fileCatalog = '" + catalog +  "'");
		}
		return this.findList(hql.toString());
	} 
	
	
}
