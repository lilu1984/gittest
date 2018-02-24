package com.wonders.engine.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ClientNode {

	/**
     * ͨ����Ӧ����
     */
    private String conNum = "0";
    /**
     * ͨ����Ӧ����--���Բ�����
     */
    private String yktBh = "0";
    /**
     * �ֻ�����
     */
    private String mobile = null;
    /**
     * ����ĵؿ���Ϣ��appId�ö��Ÿ���
     */
    private String bidderAppIds = null;
    /**
     * ����ĵؿ���Ϣ��appId�ö��Ÿ���
     */
    private String noticeIds = null;
    
    private Map appCertNoMap = new HashMap();
    
    private boolean active = false;
    
	public String getBidderAppIds() {
		return bidderAppIds;
	}
	public void setBidderAppIds(String bidderAppIds) {
		this.bidderAppIds = bidderAppIds;
	}
	public String getConNum() {
		return conNum;
	}
	public void setConNum(String conNum) {
		this.conNum = conNum;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNoticeIds() {
		return noticeIds;
	}
	public void setNoticeIds(String noticeIds) {
		this.noticeIds = noticeIds;
	}
	public String getYktBh() {
		return yktBh;
	}
	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void active() {
		setActive(true);
	}
	
	public void disactive() {
		setActive(false);
	}
	
	public String getCertNo(String appid) {
		if (appCertNoMap != null)
			if (appCertNoMap.containsKey(appid)) {
				return (String)appCertNoMap.get(appid);
			}
		return "";
	}
	
	public void addCertNo(String appId, String certNo) {
		if (appCertNoMap == null) appCertNoMap = new HashMap();
		appCertNoMap.put(appId, certNo);
	}
	/**
	 * �Ƿ�����ĳ�˵ؿ�.
	 * @param appId �ؿ���������
	 * @return
	 */
	public boolean isBidderBlock(String appId) {
		if(StringUtils.isNotEmpty(bidderAppIds)){
			if (StringUtils.trimToEmpty(bidderAppIds).indexOf(appId) != -1)
				return true;
			else 
				return false;
		}
		return false;
	}
	
	/**
	 * �Ƿ�����ĳ�˹���.
	 * @param noticeId �ؿ����빫������
	 * @return
	 */
	public boolean isBidderNotice(String noticeId) {
		if(StringUtils.isNotEmpty(noticeId)){
			if (noticeId.equals(StringUtils.trimToEmpty(noticeIds)))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public void addAppId(String appId) {
		if (StringUtils.isEmpty(appId))return;
		if (StringUtils.isEmpty(StringUtils.trimToEmpty(bidderAppIds))) {
			bidderAppIds = appId;
		} else {
			bidderAppIds += "," + appId;
		}
	}
}
