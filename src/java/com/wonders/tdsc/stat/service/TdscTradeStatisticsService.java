package com.wonders.tdsc.stat.service;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.stat.web.form.TdscTradeStatisticsForm;
import com.wonders.tdsc.tdscbase.dao.CommonQueryDao;

public class TdscTradeStatisticsService extends BaseSpringManagerImpl {
	private CommonQueryDao	commonQueryDao;

	public void setCommonQueryDao(CommonQueryDao commonQueryDao) {
		this.commonQueryDao = commonQueryDao;
	}

	/**
	 * 整理异常信息表的字段
	 * 
	 * @param appInfo
	 * @param blockPart
	 * @param tranApp
	 * @param noticeApp
	 * @param auctionCounts
	 * @return TdscTradeStatisticsForm
	 */
	public TdscTradeStatisticsForm getExceptionInfo(TdscBlockAppView appInfo, TdscBlockPart blockPart, TdscBlockTranApp tranApp, TdscNoticeApp noticeApp, String auctionCounts) {
		TdscTradeStatisticsForm retBean = new TdscTradeStatisticsForm();

		String currTime = DateConvertor.getCurrentDateWithTimeZone();
		String currDate = currTime.substring(0, 4) + "年" + currTime.substring(4, 6) + "月" + currTime.substring(6, 8) + "日";

		retBean.setReportDate(currDate);
		retBean.setBlockName(appInfo.getBlockName());
		retBean.setBidderName(tranApp.getResultName());// 竞得人
		retBean.setBlockLocation(appInfo.getLandLocation());
		retBean.setBlockUsed(blockPart.getLandUseType());

		BigDecimal bd = appInfo.getTotalLandArea().divide(new BigDecimal(10000), 10, BigDecimal.ROUND_UP);
		retBean.setBlockArea(bd.doubleValue() + "");// 宗地情况、面积

		bd = appInfo.getTotalBuildingArea().divide(new BigDecimal(10000), 10, BigDecimal.ROUND_UP);
		retBean.setBuildArea(bd.doubleValue() + "");// 建筑面积
		retBean.setBuildUsed("");// 房屋用途，系统未记录改字段，所以为空
		
		String volumeRateSign = "";
		String volumeRateStr = "";
		if(StringUtils.isNotBlank(blockPart.getVolumeRateSign())){
			volumeRateSign = DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,blockPart.getVolumeRateSign());
		}
		volumeRateStr = volumeRateSign + StringUtils.trimToEmpty(blockPart.getVolumeRate());
		if("介于".equals(volumeRateSign)){
			volumeRateStr += "—" + StringUtils.trimToEmpty(blockPart.getVolumeRate2());
		}
		
		retBean.setGreenRate(StringUtils.trimToEmpty(blockPart.getGreeningRate()));
		retBean.setVolumeRate(volumeRateStr);
		retBean.setDensityRate(StringUtils.trimToEmpty(blockPart.getDensity()));

		retBean.setNoticeNo(noticeApp.getNoticeNo());
		retBean.setNoticeDate(DateUtil.date2String(noticeApp.getNoticeDate(), "yyyy年MM月dd日"));
		retBean.setAuctionCount(auctionCounts);// 竞价轮数
		retBean.setTranTime(DateUtil.date2String(tranApp.getResultDate(), "yyyy年MM月dd日"));// 成交确认书签订时间
		
		
		//初始  = 交易底价
		//交易底价，起始价
		bd = tranApp.getInitPrice().divide(new BigDecimal(10000), 10,BigDecimal.ROUND_UP);
		retBean.setInitPrice(bd.doubleValue() + "");
		
		//成交总价
		bd = tranApp.getResultPrice().divide(new BigDecimal(10000), 10,BigDecimal.ROUND_UP);
		retBean.setTranPrice(bd.doubleValue() + "");
		
		//初始地面地价
		;
		
		retBean.setiBlockPrice(tranApp.getInitPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalLandArea(), 2, BigDecimal.ROUND_UP) + "");
		//初始楼面地价
		retBean.setiBuildPrice(tranApp.getInitPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalBuildingArea(), 2, BigDecimal.ROUND_UP) + "");
		
		//成交地面地价
		retBean.settBlockPrice(tranApp.getResultPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalLandArea(), 2, BigDecimal.ROUND_UP) + "");
		//成交楼面地价
		retBean.settBuildPrice(tranApp.getResultPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalBuildingArea(), 2, BigDecimal.ROUND_UP) + "");
		
		
		retBean.setTranMode(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TRANSFER, tranApp.getTransferMode()));
		return retBean;
	}
}
