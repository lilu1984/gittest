package com.wonders.tdsc.kyq.dao;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.kyq.bo.KyqListing;

public class KyqListingDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return KyqListing.class;
	}

}
