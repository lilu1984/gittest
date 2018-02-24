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
	 * ��tdsc_listing_app���м���һ�ι��Ƽ�¼
	 * @param listCert
	 * @param listingPrice
	 */
	public void addTdscListingAppList(String listCert,String listingPrice) {
		//����֤����ȡ�ù����˵�ȫ����Ϣ
		TdscListingApp tdscListingApp = new TdscListingApp();
		
		tdscListingApp.setListCert(listCert);
		//���ӹ��ƴ���
		int round = tdscListingApp.getPriceNum().intValue();
		round = round++;
		tdscListingApp.setPriceNum(new BigDecimal(round));
		//��¼�����˵Ĺ��Ƽ۸�
		if (StringUtils.isNotEmpty(listingPrice))
			tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		//����һ�ι��Ƽ�¼
		this.tdscListingAppDao.save(tdscListingApp);
	}
	
	/**
	 * �ӹ�����Ϣ����ȡ�ö�Ӧ�ļ�¼
	 * @param listingId
	 * @return
	 */
	public TdscListingApp queryListingAppInfo(String listingId){
		TdscListingApp tdscListingApp = new TdscListingApp();
		tdscListingApp = (TdscListingApp)this.tdscListingAppDao.get(listingId);
		return tdscListingApp;
	}
	
	/**
	 * �Ӿ�����������Ϣ����ȡ�þ���������
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
	 * �Ӿ���������Ϣ����ȡ���ʸ�֤����
	 */
	public String getCertNo(TdscBidderCondition condition){
		TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppDao.getTdscBidderApp(condition);
		String certNo=tdscBidderApp.getCertNo();
		
		return certNo;
	}
    /**����yktBh��ѯ �ô������ �������ReviewOpnn
     * 
     * @param yktBh
     * @return
     */
    public String getReviewOpnn(String yktBh){
        String reviewOpnn=(String)tdscBidderAppDao.getReviewOpnnByYktBh(yktBh);
        
        return reviewOpnn;
    }
    /**
     * ����������ѯ��������Ϣ
     * @param condition
     * @return
     */
	public TdscBidderApp getTdscBidderApp(TdscBidderCondition condition){
		return (TdscBidderApp)tdscBidderAppDao.getTdscBidderApp(condition);
	}
    
    
}
