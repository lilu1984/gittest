package com.wonders.wsjy.wxtrade;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.wsjy.TradeConsts;

/**
 * �������Ͷ��ŵ��߳�.
 * @author sunxin
 *
 */
public class WxSmsSend implements Runnable {
	
	private Logger logger = Logger.getLogger(WxSmsSend.class);
	/**
	 * ����URl
	 */
	StringBuffer strUrl = new StringBuffer("http://www.4001185185.com/sdk/smssdk!mt.action?sdk=13961837814&code=gt888888&resulttype=xml&subcode=263");
	/**
	 * ���ͽ��
	 */
	String result = "";
	/**
	 * ������ʽ���������
	 */
	String commandNo;
	/**
	 * ������ʽ�����ַ�������
	 */
	Map paramMap; 
	/**
	 * �����ʽ���͵��ַ���
	 */
	String noCommandString; 
	/**
	 * �Ƿ������ʽ����
	 */
	boolean isCommond = false; 
	
	public WxSmsSend() {
		this.isCommond = false;
	}
	public WxSmsSend(String commandNo, Map paramMap) {
		this.commandNo = commandNo;
		this.paramMap = paramMap;
		this.isCommond = true;
	}

	/**
	 *���ɷ����ַ�����Ϣ.
	 */
	public String genMsgInfo(String mobiles, String msg) {
		StringBuffer result = new StringBuffer(strUrl.toString());
		result.append("&phones=").append(mobiles);
		try {
			result.append("&msg=").append(URLEncoder.encode(msg.replaceAll(" ", ""),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public void run() {
		//�Ƿ������ʽ
		if(this.isCommond){
			String[] msgInfo = this.genMsgInfo(commandNo, paramMap);
			if (msgInfo == null) {
				return;
			}
			if (TradeConsts.ORDER13.equals(commandNo)) {
				for (int i = 0 ; i < msgInfo.length; i += 2) {
					if (StringUtils.isNotEmpty(msgInfo[i + 1]) && StringUtils.isNotEmpty(msgInfo[i])) {
						String sendInfo = genMsgInfo(msgInfo[i + 1], msgInfo[i]);
						sendMessage(sendInfo);
						logger.info("���Ͷ���:��" + msgInfo[i + 1] + "����\"" + msgInfo[i] + "\"��Ϣ");
					}
				}
			} else {
				for (int i = 0 ; i < msgInfo.length; i += 2) {
					if (StringUtils.isNotEmpty(msgInfo[i + 1]) && StringUtils.isNotEmpty(msgInfo[i])) {
						String sendInfo = genMsgInfo(msgInfo[i + 1], msgInfo[i]);
						sendMessage(sendInfo);
						logger.info("���Ͷ���:��" + msgInfo[i + 1] + "����\"" + msgInfo[i] + "\"��Ϣ");
					}
					
				}
			}
		}else{
			if(StringUtils.isNotBlank(noCommandString)){
				sendMessage(noCommandString);
			}
		}
	}
	
	/**
	 * ���Ͷ���
	 * @param sendInfo ������Ϣ�ַ���
	 */
	public void sendMessage(String sendInfo){
		try {
			URL U = new URL(sendInfo);
			URLConnection connection = U.openConnection();
			connection.connect();
			InputStream in = connection.getInputStream();
			byte[] b = new byte[in.available()];
			in.read(b);
			System.out.println("���š�"+sendInfo+"�����ͽ�� : " + new String(b,"UTF-8"));
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ���Ͷ���Ϣ�������ʽ��ֱ�ӷ���.
	 */
	public void sendSms(String mobiles, String msg) {
		noCommandString = genMsgInfo(mobiles, msg);
	}

	/**
	 * ���ɷ�����Ϣ���ݺͽ�����Ϣ�ĵ绰��Ϣ.
	 * 
	 * @param commandNo
	 *            ������
	 * @param paramMap
	 *            �����б�
	 * @return 0����Ϣ���ݣ�1�����յ���
	 */
	private String[] genMsgInfo(String commandNo, Map paramMap) {
		if (TradeConsts.ORDER21.equals(commandNo)) {
			String msg = "�𾴵ľ����ˣ�����!����" + DateUtil.date2String(new Date(), "yyyy��MM��dd��HHʱmm��") + "���ɹ���¼�����й�����Դ�����Ͻ���ϵͳ��";
			String mobile = (String) paramMap.get("mobile");
			if (StringUtils.isNotBlank(mobile)) {
				return new String[] { msg, mobile };
			}
		} else if (TradeConsts.ORDER11.equals(commandNo)) {
			TradePrice tradePrice = (TradePrice) paramMap.get("tradePrice");
			WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
			TdscBlockTranApp tdscBlockTranApp = wxDbService.getTranAppByAppId(tradePrice.getAppId());
			if ("2".equals(tradePrice.getPhase())) {
				TdscBlockInfo blockInfo = wxDbService.getBlockInfoByBlockId(tdscBlockTranApp.getBlockId());
				String buildingPrice = getBuildingPrice(new BigDecimal(tradePrice.getPrice()), blockInfo);
				String msg = "�𾴵ľ����ˣ����������" + tdscBlockTranApp.getBlockNoticeNo() + "�ؿ飬��������ʱ���۽׶Σ�Ŀǰ��" + tradePrice.getPriceNum() + "�ִα���Ϊ" + tradePrice.getPrice() + "��Ԫ," +"¥��ؼ�Ϊ"+ StringUtils.trimToEmpty(buildingPrice) + "Ԫ/ƽ���ס�";
				Map parmMap = wxDbService.getSmsInfoByAppId(null, tradePrice.getAppId());
				String mobiles = (String) parmMap.get(tradePrice.getAppId());
				if (StringUtils.isNotBlank(mobiles)) {
					return new String[] { msg, mobiles };
				}
			} else {
				List list = new ArrayList();
				String certNo = tradePrice.getClientNo();
				TdscBidderView tdscBidderView = wxDbService.getBidderViewByCertNo(certNo);
				String mobiles1 = "";
				String msg1 = "";
				if (null != tdscBidderView) {
					if (GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(tdscBidderView.getIsAccptSms())) {
						mobiles1 = tdscBidderView.getPushSmsPhone();
						msg1 = "�𾴵ľ����ˣ�����" + DateUtil.date2String(new Date(), "yyyy��MM��dd��HHʱmm��") + "�������ڹ��Ƶ�" + tdscBlockTranApp.getBlockNoticeNo() + "�ؿ鱨��" + tradePrice.getPrice() + "��Ԫ��";
						list.add(msg1);
						list.add(mobiles1);
					}
				}
				String msg = "�𾴵ľ����ˣ������깺��" + tdscBlockTranApp.getBlockNoticeNo() + "�ؿ飬��ǰ����Ϊ" + tradePrice.getPrice() + "��Ԫ��";
				Map parmMap = wxDbService.getSmsInfoByAppId(certNo, tradePrice.getAppId());
				String mobiles = (String) parmMap.get(tradePrice.getAppId());
				if (StringUtils.isNotEmpty(mobiles)) {
					mobiles = StringUtils.trimToEmpty(mobiles);
					String otherMobile = mobiles.equals(mobiles1)?"":mobiles.endsWith(mobiles1)?mobiles.replaceAll("," + mobiles1, ""):mobiles.replaceAll(mobiles1 + ",", "");
					list.add(msg);
					list.add(otherMobile);
				}
				String[] strArray = new String[list.size()];
				for (int i = 0 ; i < strArray.length; i++) {
					strArray[i] = (String)list.get(i);
				}
				return strArray;
			}
		} else if (TradeConsts.ORDER13.equals(commandNo)) {
			TradeBlock tradeBlock = (TradeBlock) paramMap.get("tradeBlock");
			WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
			TdscBlockTranApp tdscBlockTranApp = wxDbService.getTranAppByAppId(tradeBlock.getAppId());
			TdscBlockInfo blockInfo = wxDbService.getBlockInfoByBlockId(tdscBlockTranApp.getBlockId());
			
			Map parmMap = wxDbService.getSmsInfoByAppId(null, tradeBlock.getAppId());
			
			List listingAppList = wxDbService.queryListingAppList(tradeBlock.getAppId());
			
			List list = new ArrayList();
			

			// ȡ�����о�����
			List bidderAppList = wxDbService.queryBidderAppListLikeAppId(tradeBlock.getAppId());
			// ���������������0�Ļ���˵�����˲��뾺��
			if (listingAppList != null && listingAppList.size() > 0) {
				if (bidderAppList != null) {
					if (bidderAppList.size() == 1) {
						//ֻ��һ�������˵�ʱ��
						TdscBidderApp tdscBidderApp = (TdscBidderApp)bidderAppList.get(0);
						// ȡ�øþ����� ��þ����˷�2
						// ȡ�������� �������˷� 2
						if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(tdscBidderApp.getIsAccptSms())) {
							String returnStr = returnString(blockInfo);
							String msg = "�𾴵ľ����ˣ���ϲ���ѳɹ�����" +tradeBlock.getBlockNoticeNo()+ "�ؿ������ʹ��Ȩ���밴�����ļ��涨�����Ҫ����ɽ��׷����" + returnStr + "�󣬳֡��ɽ�ȷ���顷�������й�����Դ��ǩ���������й��н����õ�ʹ��Ȩ���ú�ͬ����";
							list.add(msg);
							list.add(tdscBidderApp.getPushSmsPhone());
						}
					} else {
						// ȡ�þ�����
						TdscBidderView tdscBidderView = wxDbService.getBidderViewByCertNo(tradeBlock.getTopClientNo());
						
						//1. Ⱥ��1
						String mobiles = (String) parmMap.get(tradeBlock.getAppId());
						mobiles = StringUtils.trimToEmpty(mobiles);
						if (StringUtils.isNotEmpty(mobiles)) {
							String buildingPrice = getBuildingPrice(new BigDecimal(tradeBlock.getTopPrice()), blockInfo);
							String msg = "�𾴵ľ����ˣ�" +tradeBlock.getBlockNoticeNo()+ "�ؿ龭��" + tradeBlock.getPriceNum()+ "�ִξ�����߱���Ϊ" +NumberUtil.numberDisplay(tradeBlock.getTopPrice(), 2) + "�����" + overPriceRate(new BigDecimal(tradeBlock.getTopPrice()), tdscBlockTranApp.getInitPrice())+ "��¥��ؼ�" +StringUtils.trimToEmpty(buildingPrice)+ "Ԫ/ƽ���ף�"+"��ϵͳȷ��" +tradeBlock.getTopConNum()+ "�ž�����ȡ�øõؿ龺���ʸ�" ;
							
							list.add(msg);
							list.add(mobiles);
						}
						//2. �����˷�2
						String successMobile = StringUtils.trimToEmpty(tdscBidderView.getPushSmsPhone());
						if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(tdscBidderView.getIsAccptSms())) {
							String returnStr = returnString(blockInfo);
							String msg2 = "��ϵͳȷ�ϣ���ϲ���ѳɹ�����" +tradeBlock.getBlockNoticeNo()+ "�ؿ�ľ����ʸ��밴������֪�ĵ�ʮ���ڣ��ˣ���Ĺ涨�������ʸ�����������";
							list.add(msg2);
							list.add(successMobile);
						}
						//3. û�о����˷�3
						if (StringUtils.isNotEmpty(mobiles)) {
							String msg3 = "�𾴵ľ����ˣ����ź���δ����" +tradeBlock.getBlockNoticeNo()+ "�ؿ�ľ����ʸ��밴�����ļ��涨�����Ҫ���������֤����ƾ����֤���վ�ԭ���������й�����Դ�ֶ�¥206�Ұ����˻��������绰��0510-85726711����ϵ�ˣ���ϡ�";
							String otherMobile = mobiles.equals(successMobile)?"":mobiles.endsWith(successMobile)?mobiles.replaceAll("," + successMobile, ""):mobiles.replaceAll(successMobile + ",", "");
							list.add(msg3);
							list.add(otherMobile);
						}
					}
				}
				//}				 
			} 
			
			String[] strArray = new String[list.size()];
			for (int i = 0 ; i < strArray.length; i++) {
				strArray[i] = (String)list.get(i);
			}
			return strArray;
		} else if (TradeConsts.ORDER15.equals(commandNo)) {
			List msglist = new ArrayList();
			
			String noticeId = (String)paramMap.get("noticeId");
			if (StringUtils.isNotEmpty(noticeId)) {
				WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
				TdscNoticeApp noticeApp = wxDbService.getNoticeAppByNoticeId(noticeId);
				List list = wxDbService.findBidderAppList(noticeId);
				StringBuffer mobileBuffer = new StringBuffer();
				int length = list.size();
				for (int i = 0 ; i < length; i++) {
					TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
					if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(bidderApp.getIsAccptSms())) {
						mobileBuffer.append(bidderApp.getPushSmsPhone()).append(",");;
					}
				}
				String message = "�𾴵ľ����ˣ�" + StringUtils.trimToEmpty(noticeApp.getNoticeNo())+ "���н����õ�ʹ��Ȩ���Ƴ��û�Ѿ���������л�������������г��Ĳ����֧�֣�ϣ����������ע���������г���̬��";
				String mobile = mobileBuffer.toString();
				if (StringUtils.isNotEmpty(mobile))
					mobile = mobile.substring(0,mobile.length()- 1);
				msglist.add(message);
				msglist.add(mobile);
				//CoreService.getSmsSupport().sendSms(mobile, message);
				// ��������ɽ��ؿ���Ϣ
				
				List tranList = wxDbService.findBlockAppList(noticeId);
				for (int i = 0 ; null != tranList && i < tranList.size(); i++) {
					TdscBlockTranApp tranApp = (TdscBlockTranApp)tranList.get(i);
					int count = wxDbService.getTradeListCount(tranApp.getAppId());
					if (count == 0 && "01".equals(tranApp.getTranResult())){
						// Ϊ����ɽ��ؿ������ʱ���ۻ������˳��۵ĵؿ�
						TdscBidderView bidderView = wxDbService.getYixiangNameLikeAppId(tranApp.getAppId());
						if (null != bidderView && StringUtils.isNotBlank(bidderView.getConNum()) && bidderView.getConNum().equals(tranApp.getResultCert())) {
							// ����������˴��ڣ��Ҹ�������Ϊ�����ˣ�����������˷�����Ϣ2
							if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(bidderView.getIsAccptSms())) {
								TdscBlockInfo blockInfo = wxDbService.getBlockInfoByBlockId(tranApp.getBlockId());
								String returnStr = returnString(blockInfo);
								String msg = "�𾴵ľ����ˣ���ϲ���ѳɹ�����" +tranApp.getBlockNoticeNo()+ "�ؿ������ʹ��Ȩ���밴�����ļ��涨�����Ҫ����ɽ��׷����" + returnStr + "�󣬳֡��ɽ�ȷ���顷�������й�����Դ��ǩ���������й��н����õ�ʹ��Ȩ���ú�ͬ����";
								msglist.add(msg);
								msglist.add(bidderView.getPushSmsPhone());
							}
						}
					}
				}
			}
			String[] strArray = new String[msglist.size()];
			for (int i = 0 ; i < strArray.length; i++) {
				strArray[i] = (String)msglist.get(i);
			}
			return strArray;
		}
		return null;

	}

	/**
	 * ����¥��ؼ�
	 * 
	 * @param decimal
	 * @param totalBuildingArea
	 * @return
	 */
	private String getBuildingPrice(BigDecimal decimal, TdscBlockInfo blockInfo) {
		String blockQuality = blockInfo.getBlockQuality();
		if ("101".equals(blockQuality)) {
			return "Ķ����Ϊ" + NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(666.67)).divide(blockInfo.getTotalLandArea(), BigDecimal.ROUND_HALF_UP),2) + "��Ԫ/Ķ";
		} else if ("102".equals(blockQuality)) {
			return "¥��ؼ�Ϊ" + NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(10000)).divide(blockInfo.getTotalBuildingArea(), BigDecimal.ROUND_HALF_UP),2) + "Ԫ/ƽ����";
		}
		return null;
	}
	
	private String overPriceRate(BigDecimal topPrice, BigDecimal initPrice) {
		// ����ǰ�ļ۸�-��ʼ�ۣ�/��ʼ�� *100%
		BigDecimal result = topPrice.subtract(initPrice).divide(initPrice,4, BigDecimal.ROUND_HALF_UP);
		result = result.multiply(new BigDecimal("100"));
		return NumberUtil.numberDisplay(result, 2) + '%';
	}

	private String returnString(TdscBlockInfo tdscBlockInfo) {
		BigDecimal d2 = new BigDecimal("0");
		if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(d2) > 0)
			return "�͵����ֺ�Σ����������";
		return "";
	}
	
	
}
