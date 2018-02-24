package com.wonders.wsjy.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.bo.WsjyBankApp;

public class WsjyBankAppDao extends BaseHibernateDaoImpl{

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return WsjyBankApp.class;
	}
	/**
	 * 根据银行的流水号查找数据库记录
	 * @param inBankFLCode
	 * @return
	 */
	public WsjyBankApp getBankAppByInBankFLCode(String inBankFLCode){
		StringBuffer hql = new StringBuffer("from WsjyBankApp a where a.inbankflcode = '").append(inBankFLCode).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (WsjyBankApp) list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 根据银行的子帐号查找数据库记录
	 * @param inACCT
	 * @return
	 */
	public List findPayListByInacct(String inacct){
		StringBuffer hql = new StringBuffer("from WsjyBankApp a where a.inmemo = '").append(inacct).append("'");
		List list = this.findList(hql.toString());
		return list;
		
	}
}
