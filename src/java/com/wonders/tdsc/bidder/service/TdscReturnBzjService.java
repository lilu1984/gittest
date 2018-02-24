package com.wonders.tdsc.bidder.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.tdscbase.dao.CommonQueryDao;

public class TdscReturnBzjService extends BaseSpringManagerImpl {

	private CommonQueryDao			commonQueryDao;

	private TdscBlockPlanTableDao	tdscBlockPlanTableDao;

	private TdscBlockTranAppDao		tdscBlockTranAppDao;

	private TdscBidderAppDao		tdscBidderAppDao;

	private TdscBidderPersonAppDao	tdscBidderPersonAppDao;

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setCommonQueryDao(CommonQueryDao commonQueryDao) {
		this.commonQueryDao = commonQueryDao;
	}

	public List queryPlanOfEnd() {
		List list = this.commonQueryDao.queryTdscPlanListOfEnd();
		return list;
	}

	public List queryBlockPlanTableByPlanIdList(List list) {
		return this.tdscBlockPlanTableDao.queryBlockPlanTableByPlanIdList(list);
	}

	public String queryNoticeIdByPlanId(String planId) {
		if (StringUtils.isEmpty(planId))
			return null;
		List list = this.tdscBlockTranAppDao.findAppListByPlanId(planId);
		if (list == null || list.size() == 0)
			return null;
		TdscBlockTranApp tranApp = (TdscBlockTranApp) list.get(0);
		return tranApp.getNoticeId();
	}

	public Map queryReturnBzjList(String noticeId) {
		if (StringUtils.isEmpty(noticeId))
			return null;
		List list = this.tdscBidderAppDao.findSql(noticeId);
		Map resultMap = new HashMap();
		// list[0]:appId,list[1]:ykt_bh,list[2]:bzj_dzse,list[3]:result_price,list[4]:cert_no,list[5]:bidder_name
		// list[0]:app_id, list[1]:ykt_bh, list[2]:bzj_dzse, list[3]:is_convert, list[4]:result_price, list[5]:result_cert, list[6]:bidder_name
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			String appId = (String) objs[0];
			String yktBh = (String) objs[1];
			BigDecimal dzse = (BigDecimal) objs[2];
			Character isConvert = (Character) objs[3];
			String cert = (String) objs[5];
			String bidderName = (String) objs[6];
			if (StringUtils.isEmpty(cert)) {
				if (resultMap.containsKey(yktBh)) {
					TdscBidderApp temp = (TdscBidderApp) resultMap.get(yktBh);
					BigDecimal value = temp.getDzse();
					if (value != null && dzse != null) {
						value = value.add(dzse);
						temp.setDzse(value);
						resultMap.put(yktBh, temp);
					}
				} else {
					TdscBidderApp temp = new TdscBidderApp();
					TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
					temp.setAppId(appId);
					temp.setYktBh(yktBh);
					temp.setDzse(dzse);
					if (null != isConvert)
						temp.setIsConvert(isConvert + "");
					tdscBidderPersonApp.setBidderName(StringUtil.ISO88591toGBK(bidderName));
					temp.setAloneTdscBidderPersonApp(tdscBidderPersonApp);
					resultMap.put(yktBh, temp);
				}
			}
		}
		return resultMap;
	}

	public void updateBidderListConvertByYktNoticeId(String yktBh, String noticeId) {
		TdscBidderCondition condition = new TdscBidderCondition();
		condition.setYktBh(yktBh);
		condition.setNoticeId(noticeId);
		List list = this.tdscBidderAppDao.findBidderAppListByCondition(condition);
		for (int i = 0; list != null && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			app.setIsConvert("1");
			app.setAcceptDate(new Timestamp(System.currentTimeMillis()));

			tdscBidderAppDao.update(app);
		}
	}

	// public List queryBidderListByNoticeId(String noticeId) {
	// if (StringUtils.isEmpty(noticeId)) return null;
	// TdscBidderCondition condition = new TdscBidderCondition();
	// condition.setNoticeId(noticeId);
	// List bidderList = this.tdscBidderAppDao.getTdscBidderAppList(condition);
	// for (int i = 0 ; i < bidderList.size(); i++) {
	// TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderList.get(i);
	// }
	// return null;
	// }

}
