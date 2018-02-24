package com.wonders.tdsc.selfhelp.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.selfhelp.dao.TdscListingAppDao;

public class SelfhelpService extends BaseSpringManagerImpl {

	private TdscListingAppDao tdscListingAppDao;

	private TdscBidderAppDao tdscBidderAppDao;
	
	private TdscBidderPersonAppDao tdscBidderPersonAppDao;
	
	public void setTdscBidderPersonAppDao(
			TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscListingAppDao(TdscListingAppDao tdscListingAppDao) {
		this.tdscListingAppDao = tdscListingAppDao;
	}
	
	/**
	 * 在tdsc_listing_app表中加入一次挂牌记录
	 * @param listCert
	 * @param listingPrice
	 */
	public void addTdscListingAppList(String listCert,String listingPrice) {
		//根据证书编号取得挂牌人的全部信息
		TdscListingApp tdscListingApp = new TdscListingApp();
		
		tdscListingApp.setListCert(listCert);
		//增加挂牌次数
		int round = tdscListingApp.getPriceNum().intValue();
		round = round++;
		tdscListingApp.setPriceNum(new BigDecimal(round));
		//记录挂牌人的挂牌价格
		if (StringUtils.isNotEmpty(listingPrice))
			tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		//保存一次挂牌记录
		this.tdscListingAppDao.save(tdscListingApp);
	}
	
	/**
	 * 从挂牌信息表中取得对应的记录
	 * @param listingId
	 * @return
	 */
	public TdscListingApp queryListingAppInfo(String listingId){
		TdscListingApp tdscListingApp = new TdscListingApp();
		tdscListingApp = (TdscListingApp)this.tdscListingAppDao.get(listingId);
		return tdscListingApp;
	}
	
	/**
	 * 从竞买人申请信息表中取得竞买人名称
	 * @param condition
	 * @return
	 */
	public String getBidderName(TdscBidderCondition condition){
		
		TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppDao.getTdscBidderApp(condition);

		String bidderId=tdscBidderApp.getBidderId();
		
		List list=tdscBidderPersonAppDao.getBidderNameByBidderId(bidderId);
		StringBuffer  tempName=new StringBuffer("");
		int checkNum=0;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				checkNum++;
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp)list.get(i);
				if(checkNum<list.size()){
					tempName.append(tdscBidderPersonApp.getBidderName()).append(",");
				}else{
					tempName.append(tdscBidderPersonApp.getBidderName());
				}
				
			}	
		}
		String retName=tempName.toString();
		return retName;
	}
	/*
	 * 从竞买申请信息表中取得资格证书编号
	 */
	public String getCertNo(TdscBidderCondition condition){
		TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppDao.getTdscBidderApp(condition);
		String certNo=tdscBidderApp.getCertNo();
		
		return certNo;
	}
    /**根据yktBh查询 该次申请的 机审意见ReviewOpnn
     * 
     * @param yktBh
     * @return
     */
    public String getReviewOpnn(String yktBh){
        String reviewOpnn=(String)tdscBidderAppDao.getReviewOpnnByYktBh(yktBh);
        
        return reviewOpnn;
    }
    /**
     * 根据条件查询竞买人信息
     * @param condition
     * @return
     */
	public TdscBidderApp getTdscBidderApp(TdscBidderCondition condition){
		return (TdscBidderApp)tdscBidderAppDao.getTdscBidderApp(condition);
	}
    
    
}
