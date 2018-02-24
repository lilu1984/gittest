package com.wonders.tdsc.selfhelp.dao;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscListingApp;

public class TdscListingAppDao extends BaseHibernateDaoImpl{
	
	public Class getEntityClass() {
        return TdscListingApp.class;
    }

}
