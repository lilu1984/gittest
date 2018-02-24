package com.wonders.wsjy.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.localtrade.dao.TdscListingAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;

public class JobServer {
	
	private Logger	logger	= Logger.getLogger(getClass());
	private TdscBlockTranAppDao tdscBlockTranAppDao = (TdscBlockTranAppDao) AppContextUtil.getInstance().getAppContext().getBean("tdscBlockTranAppDao");
	private TdscBidderAppDao tdscBidderAppDao = (TdscBidderAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppDao");
	private TdscBidderPersonAppDao tdscBidderPersonAppDao = (TdscBidderPersonAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscBidderPersonAppDao");
	private TdscListingInfoDao tdscListingInfoDao = (TdscListingInfoDao)AppContextUtil.getInstance().getAppContext().getBean("tdscListingInfoDao");
	private TdscListingAppDao tdscListingAppDao = (TdscListingAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscListingAppDao");
	/**
	 * ���������ؿ��ڹ��ƽ�ֹʱ���Ƿ��������˱�֤��
	 * ��δ���ڽ��ɱ�֤����������ؿ�ת��Ϊ������ؿ�
	 */
	public void checkYiXiangBlockInListingEndDate(){
		List list = this.tdscBlockTranAppDao.findYxBlockList();
		for(int i=0;i<list.size();i++){
			//����������תΪ������			
			TdscBlockTranApp tranApp = (TdscBlockTranApp)list.get(i);
			logger.info("������ؿ�appIdΪ"+tranApp.getAppId());
			TdscBidderApp bidderApp = this.tdscBidderAppDao.getPurposeBidderAppByAppId(tranApp.getAppId());
			if(bidderApp!=null){
				String bidderId = bidderApp.getBidderId();
				TdscBidderPersonApp personApp = this.tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
				TdscListingInfo listingInfo = this.tdscListingInfoDao.getTdscListingInfoByAppId(bidderApp.getAppId());
				logger.info("�ʸ�֤����Ϊ:"+bidderApp.getCertNo()+",����Ϊ:"+bidderApp.getConNum()+"��ȡ����ʱ��Ϊ:"+DateUtil.timestamp2String(bidderApp.getConTime(), DateUtil.FORMAT_DATETIME));
				logger.info("��֯��������Ϊ:"+personApp.getOrgNo());
				this.tdscBidderPersonAppDao.delete(personApp);
				this.tdscBidderAppDao.delete(bidderApp);
			}
			tranApp.setIsPurposeBlock("0");
			tdscBlockTranAppDao.update(tranApp);
			CoreService.reloadClientPipeByAppId(tranApp.getAppId());
		}
	}
}
