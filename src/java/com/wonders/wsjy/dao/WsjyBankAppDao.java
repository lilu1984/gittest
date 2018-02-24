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
	 * �������е���ˮ�Ų������ݿ��¼
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
	 * �������е����ʺŲ������ݿ��¼
	 * @param inACCT
	 * @return
	 */
	public List findPayListByInacct(String inacct){
		StringBuffer hql = new StringBuffer("from WsjyBankApp a where a.inmemo = '").append(inacct).append("'");
		List list = this.findList(hql.toString());
		return list;
		
	}
}
