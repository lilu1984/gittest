package com.wonders.wsjy.dao;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.wsjy.bo.WsjyBankLog;

public class WsjyBankLogDao extends BaseHibernateDaoImpl{
	protected Class getEntityClass() {
		return WsjyBankLog.class;
	}
}
