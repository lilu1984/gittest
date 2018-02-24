package com.wonders.tdsc.retbail.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.retbail.dao.TdscReturnBailDao;
import com.wonders.tdsc.retbail.web.form.TdscReturnBailForm;

public class TdscReturnBailService {
	private TdscReturnBailDao	tdscReturnBailDao;

	public void setTdscReturnBailDao(TdscReturnBailDao tdscReturnBailDao) {
		this.tdscReturnBailDao = tdscReturnBailDao;
	}

	public TdscReturnBail getTdscReturnBailByAppIdBidderId(String appId, String bidderId) {
		return tdscReturnBailDao.getTdscReturnBailByAppIdBidderId(appId, bidderId);
	}

	public void saveTdscReturnBail(TdscReturnBailForm retForm, SysUser user) {
		String[] bidders = retForm.getBidderIds();
		String[] planIds = retForm.getPlanIds();
		String[] appIds = retForm.getAppIds();
		String[] blockIds = retForm.getBlockIds();
		String[] bails = retForm.getBidderBails();
		String[] bailId = retForm.getBailIds();

		for (int i = 0; i < bidders.length; i++) {
			TdscReturnBail bailBo = (TdscReturnBail)tdscReturnBailDao.get(bailId[i]);
			bailBo.setActionUser(user.getUserId());
			bailBo.setActionDate(new Timestamp(System.currentTimeMillis()));
			bailBo.setIfReturn("01");

			tdscReturnBailDao.saveOrUpdate(bailBo);
		}

	}

	public String getLastReturnBailActionDateByAppId(String tdscAppId) {

		Long initTime = new Long("19000101010101");
		Long varTime = null;
		List retList = tdscReturnBailDao.getLastReturnBailActionDateByAppId(tdscAppId);
		if (retList != null && retList.size() > 0) {
			for (int i = 0; i < retList.size(); i++) {
				TdscReturnBail bail = (TdscReturnBail) retList.get(i);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String actionDate = df.format(bail.getActionDate());
				varTime = new Long(actionDate);
				if (varTime.compareTo(initTime) > 0)
					initTime = varTime;
			}
			return initTime + "";
		}
		return null;
	}

	
	public List findTdscReturnBailListByPlanId(String planId) {
		return (List)tdscReturnBailDao.findTdscReturnBailListByPlanId(planId);
	}

}
