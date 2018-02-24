package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.util.CharsetConvertUtil;
import com.wonders.wsjy.bo.PersonInfo;

public class WxPersonInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return PersonInfo.class;
	}

	public PersonInfo getWxPersonInfo(String bidderName) {
		StringBuffer hql = new StringBuffer("from PersonInfo a where a.bidderName ='").append(
				(String) CharsetConvertUtil.gbkToIso(bidderName)
				).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (PersonInfo) list.get(0);
		}

		return null;
	}

}
