package com.wonders.tdsc.tdscbase.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.tdscbase.dao.TdscBidderViewDao;

public class TdscBidderViewService extends BaseSpringManagerImpl {

	private TdscBidderViewDao	tdscBidderViewDao;

	public void setTdscBidderViewDao(TdscBidderViewDao tdscBidderViewDao) {
		this.tdscBidderViewDao = tdscBidderViewDao;
	}

	public List queryBidderByNameKahao(TdscBidderCondition bidderCond) {
		return tdscBidderViewDao.queryBidderByNameKahao(bidderCond);
	}

	public TdscBidderView getBidderViewByPersonName(String bidderName) {
		return tdscBidderViewDao.getBidderViewByPersonName(bidderName);
	}

	public TdscBidderView queryBidderViewInfo(TdscBidderCondition bidderCond) {
		return tdscBidderViewDao.queryBidderViewInfo(bidderCond);

	}

	public TdscBidderView getYixiangNameLikeAppId(String appId) {
		return tdscBidderViewDao.getYixiangNameLikeAppId(appId);
	}

	/**
	 * 查询该地块所有的竞买人
	 * @param appId
	 * @return
	 */
	public List queryBiddersByAppId(String appId) {
		return tdscBidderViewDao.queryBiddersByAppId(appId);
	}
	
	public List queryBankDicList() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		List retList = new ArrayList();
		// 获取保证金账户字典表 DIC_BANK
		String sql = "select * from DIC_BANK order by dic_num";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				TdscDicBean tb = new TdscDicBean();
				tb.setDicCode(rs.getString("DIC_CODE"));
				tb.setDicValue(rs.getString("DIC_VALUE"));
				tb.setDicNum(rs.getString("DIC_NUM"));
				tb.setDicDescribe(StringUtil.ISO88591toGBK(rs.getString("DIC_DESCRIBE")));
				tb.setIsValidity(rs.getString("IS_VALIDITY"));
				retList.add(tb);
			}

		} catch (Exception e) {
			// Nothing to do
		}

		return retList;
	}
	
	
	public List queryBidderViewListByPersonName(String personName) {
		return tdscBidderViewDao.queryBidderViewListByPersonName(personName);
	}

}
