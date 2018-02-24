package com.wonders.wsjy.dao;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.wsjy.bo.PersonInfo;

public class PersonInfoDAO extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return PersonInfo.class;
	}

	public PersonInfo getPersonInfo(String bidderId) {
		return (PersonInfo)get(bidderId);
	}
	
	public PersonInfo persistentPersonInfo(PersonInfo personInfo) {
		PersonInfo info = (PersonInfo)saveOrUpdate(personInfo);
		return info;
	}
}
