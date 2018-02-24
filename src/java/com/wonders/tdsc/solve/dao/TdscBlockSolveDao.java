package com.wonders.tdsc.solve.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockSolve;

public class TdscBlockSolveDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscBlockSolve.class;
	}

	public List queryAllSolveByBlockId(String blockId) {
		StringBuffer hql = new StringBuffer("");
		hql.append("from TdscBlockSolve a where a.blockId='");
		hql.append(blockId.trim());
		hql.append("'");
		return findList(hql.toString());
	}

}
