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
	 * �����쳣��Ϣ����ֶ�
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
		String currDate = currTime.substring(0, 4) + "��" + currTime.substring(4, 6) + "��" + currTime.substring(6, 8) + "��";

		retBean.setReportDate(currDate);
		retBean.setBlockName(appInfo.getBlockName());
		retBean.setBidderName(tranApp.getResultName());// ������
		retBean.setBlockLocation(appInfo.getLandLocation());
		retBean.setBlockUsed(blockPart.getLandUseType());

		BigDecimal bd = appInfo.getTotalLandArea().divide(new BigDecimal(10000), 10, BigDecimal.ROUND_UP);
		retBean.setBlockArea(bd.doubleValue() + "");// �ڵ���������

		bd = appInfo.getTotalBuildingArea().divide(new BigDecimal(10000), 10, BigDecimal.ROUND_UP);
		retBean.setBuildArea(bd.doubleValue() + "");// �������
		retBean.setBuildUsed("");// ������;��ϵͳδ��¼���ֶΣ�����Ϊ��
		
		String volumeRateSign = "";
		String volumeRateStr = "";
		if(StringUtils.isNotBlank(blockPart.getVolumeRateSign())){
			volumeRateSign = DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,blockPart.getVolumeRateSign());
		}
		volumeRateStr = volumeRateSign + StringUtils.trimToEmpty(blockPart.getVolumeRate());
		if("����".equals(volumeRateSign)){
			volumeRateStr += "��" + StringUtils.trimToEmpty(blockPart.getVolumeRate2());
		}
		
		retBean.setGreenRate(StringUtils.trimToEmpty(blockPart.getGreeningRate()));
		retBean.setVolumeRate(volumeRateStr);
		retBean.setDensityRate(StringUtils.trimToEmpty(blockPart.getDensity()));

		retBean.setNoticeNo(noticeApp.getNoticeNo());
		retBean.setNoticeDate(DateUtil.date2String(noticeApp.getNoticeDate(), "yyyy��MM��dd��"));
		retBean.setAuctionCount(auctionCounts);// ��������
		retBean.setTranTime(DateUtil.date2String(tranApp.getResultDate(), "yyyy��MM��dd��"));// �ɽ�ȷ����ǩ��ʱ��
		
		
		//��ʼ  = ���׵׼�
		//���׵׼ۣ���ʼ��
		bd = tranApp.getInitPrice().divide(new BigDecimal(10000), 10,BigDecimal.ROUND_UP);
		retBean.setInitPrice(bd.doubleValue() + "");
		
		//�ɽ��ܼ�
		bd = tranApp.getResultPrice().divide(new BigDecimal(10000), 10,BigDecimal.ROUND_UP);
		retBean.setTranPrice(bd.doubleValue() + "");
		
		//��ʼ����ؼ�
		;
		
		retBean.setiBlockPrice(tranApp.getInitPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalLandArea(), 2, BigDecimal.ROUND_UP) + "");
		//��ʼ¥��ؼ�
		retBean.setiBuildPrice(tranApp.getInitPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalBuildingArea(), 2, BigDecimal.ROUND_UP) + "");
		
		//�ɽ�����ؼ�
		retBean.settBlockPrice(tranApp.getResultPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalLandArea(), 2, BigDecimal.ROUND_UP) + "");
		//�ɽ�¥��ؼ�
		retBean.settBuildPrice(tranApp.getResultPrice().multiply(new BigDecimal(10000)).divide(appInfo.getTotalBuildingArea(), 2, BigDecimal.ROUND_UP) + "");
		
		
		retBean.setTranMode(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TRANSFER, tranApp.getTransferMode()));
		return retBean;
	}
}
