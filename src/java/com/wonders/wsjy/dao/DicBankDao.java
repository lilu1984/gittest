package com.wonders.wsjy.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.wsjy.bo.DicBank;

public class DicBankDao extends BaseHibernateDaoImpl{

	protected Class getEntityClass() {
		return DicBank.class;
	}
	/**
	 * 根据id获取数据
	 *
	 */
	public DicBank getDataById(String id){
		String sql = "from DicBank a where a.dicCode='"+id+"'";
		List list = this.findList(sql);
		if(list.size()>0){
			return (DicBank)list.get(0);
		}else{
			return null;
		}
	}
}
