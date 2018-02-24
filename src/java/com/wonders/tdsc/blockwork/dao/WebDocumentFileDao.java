package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.WebDocumentFile;

public class WebDocumentFileDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return WebDocumentFile.class;
	}

	public WebDocumentFile getWebFileByRecordId(String recordId) {
		StringBuffer hql = new StringBuffer("from WebDocumentFile a where a.recordId ='").append(recordId).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (WebDocumentFile) list.get(0);
		}

		return null;
	}

}
