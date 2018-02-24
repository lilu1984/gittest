package com.wonders.engine.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ClientNode {

	/**
     * 通道对应号牌
     */
    private String conNum = "0";
    /**
     * 通道对应卡号--可以不设置
     */
    private String yktBh = "0";
    /**
     * 手机号码
     */
    private String mobile = null;
    /**
     * 购买的地块信息，appId用逗号隔开
     */
    private String bidderAppIds = null;
    /**
     * 购买的地块信息，appId用逗号隔开
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
	 * 是否购买了某此地块.
	 * @param appId 地块申请主键
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
	 * 是否购买了某此公告.
	 * @param noticeId 地块申请公告主键
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
