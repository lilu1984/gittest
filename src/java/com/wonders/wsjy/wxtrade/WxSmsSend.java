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
 * 无锡发送短信的线程.
 * @author sunxin
 *
 */
public class WxSmsSend implements Runnable {
	
	private Logger logger = Logger.getLogger(WxSmsSend.class);
	/**
	 * 发送URl
	 */
	StringBuffer strUrl = new StringBuffer("http://www.4001185185.com/sdk/smssdk!mt.action?sdk=13961837814&code=gt888888&resulttype=xml&subcode=263");
	/**
	 * 发送结果
	 */
	String result = "";
	/**
	 * 命令形式的命令代码
	 */
	String commandNo;
	/**
	 * 命令形式发送字符串参数
	 */
	Map paramMap; 
	/**
	 * 非命令方式发送的字符串
	 */
	String noCommandString; 
	/**
	 * 是否是命令方式发送
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
	 *生成发送字符串信息.
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
		//是否是命令方式
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
						logger.info("发送短信:向" + msgInfo[i + 1] + "发送\"" + msgInfo[i] + "\"消息");
					}
				}
			} else {
				for (int i = 0 ; i < msgInfo.length; i += 2) {
					if (StringUtils.isNotEmpty(msgInfo[i + 1]) && StringUtils.isNotEmpty(msgInfo[i])) {
						String sendInfo = genMsgInfo(msgInfo[i + 1], msgInfo[i]);
						sendMessage(sendInfo);
						logger.info("发送短信:向" + msgInfo[i + 1] + "发送\"" + msgInfo[i] + "\"消息");
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
	 * 发送短信
	 * @param sendInfo 短信信息字符串
	 */
	public void sendMessage(String sendInfo){
		try {
			URL U = new URL(sendInfo);
			URLConnection connection = U.openConnection();
			connection.connect();
			InputStream in = connection.getInputStream();
			byte[] b = new byte[in.available()];
			in.read(b);
			System.out.println("短信【"+sendInfo+"】发送结果 : " + new String(b,"UTF-8"));
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发送短消息，非命令方式，直接发送.
	 */
	public void sendSms(String mobiles, String msg) {
		noCommandString = genMsgInfo(mobiles, msg);
	}

	/**
	 * 生成发送消息内容和接受消息的电话信息.
	 * 
	 * @param commandNo
	 *            命令编号
	 * @param paramMap
	 *            参数列表
	 * @return 0：消息内容；1：接收的人
	 */
	private String[] genMsgInfo(String commandNo, Map paramMap) {
		if (TradeConsts.ORDER21.equals(commandNo)) {
			String msg = "尊敬的竞买人，您好!您在" + DateUtil.date2String(new Date(), "yyyy年MM月dd日HH时mm分") + "，成功登录无锡市国土资源局网上交易系统。";
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
				String msg = "尊敬的竞买人，您所竞买的" + tdscBlockTranApp.getBlockNoticeNo() + "地块，正处于限时竞价阶段，目前第" + tradePrice.getPriceNum() + "轮次报价为" + tradePrice.getPrice() + "万元," +"楼面地价为"+ StringUtils.trimToEmpty(buildingPrice) + "元/平方米。";
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
						msg1 = "尊敬的竞买人，您在" + DateUtil.date2String(new Date(), "yyyy年MM月dd日HH时mm分") + "，对正在挂牌的" + tdscBlockTranApp.getBlockNoticeNo() + "地块报价" + tradePrice.getPrice() + "万元。";
						list.add(msg1);
						list.add(mobiles1);
					}
				}
				String msg = "尊敬的竞买人，您所申购的" + tdscBlockTranApp.getBlockNoticeNo() + "地块，当前报价为" + tradePrice.getPrice() + "万元。";
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
			

			// 取得所有竞买人
			List bidderAppList = wxDbService.queryBidderAppListLikeAppId(tradeBlock.getAppId());
			// 如果竞价轮数大于0的话，说明有人参与竞价
			if (listingAppList != null && listingAppList.size() > 0) {
				if (bidderAppList != null) {
					if (bidderAppList.size() == 1) {
						//只有一个竞买人的时候
						TdscBidderApp tdscBidderApp = (TdscBidderApp)bidderAppList.get(0);
						// 取得该竞买人 向该竞买人发2
						// 取得意向人 向意向人发 2
						if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(tdscBidderApp.getIsAccptSms())) {
							String returnStr = returnString(blockInfo);
							String msg = "尊敬的竞得人，恭喜您已成功竞得" +tradeBlock.getBlockNoticeNo()+ "地块的土地使用权。请按出让文件规定的相关要求缴纳交易服务费" + returnStr + "后，持《成交确认书》到无锡市国土资源局签订《无锡市国有建设用地使用权出让合同》。";
							list.add(msg);
							list.add(tdscBidderApp.getPushSmsPhone());
						}
					} else {
						// 取得竞得人
						TdscBidderView tdscBidderView = wxDbService.getBidderViewByCertNo(tradeBlock.getTopClientNo());
						
						//1. 群发1
						String mobiles = (String) parmMap.get(tradeBlock.getAppId());
						mobiles = StringUtils.trimToEmpty(mobiles);
						if (StringUtils.isNotEmpty(mobiles)) {
							String buildingPrice = getBuildingPrice(new BigDecimal(tradeBlock.getTopPrice()), blockInfo);
							String msg = "尊敬的竞买人，" +tradeBlock.getBlockNoticeNo()+ "地块经过" + tradeBlock.getPriceNum()+ "轮次竞价最高报价为" +NumberUtil.numberDisplay(tradeBlock.getTopPrice(), 2) + "溢价率" + overPriceRate(new BigDecimal(tradeBlock.getTopPrice()), tdscBlockTranApp.getInitPrice())+ "，楼面地价" +StringUtils.trimToEmpty(buildingPrice)+ "元/平方米，"+"经系统确认" +tradeBlock.getTopConNum()+ "号竞买人取得该地块竞得资格" ;
							
							list.add(msg);
							list.add(mobiles);
						}
						//2. 竞得人发2
						String successMobile = StringUtils.trimToEmpty(tdscBidderView.getPushSmsPhone());
						if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(tdscBidderView.getIsAccptSms())) {
							String returnStr = returnString(blockInfo);
							String msg2 = "经系统确认，恭喜您已成功竞得" +tradeBlock.getBlockNoticeNo()+ "地块的竞得资格。请按出让须知的第十条第（八）款的规定办理竞得资格审查等手续。";
							list.add(msg2);
							list.add(successMobile);
						}
						//3. 没有竞得人发3
						if (StringUtils.isNotEmpty(mobiles)) {
							String msg3 = "尊敬的竞买人，很遗憾您未竞得" +tradeBlock.getBlockNoticeNo()+ "地块的竞得资格。请按出让文件规定的相关要求带好所需证件，凭竞买保证金收据原件到无锡市国土资源局二楼206室办理退还手续。电话：0510-85726711，联系人：徐聪。";
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
				String message = "尊敬的竞买人，" + StringUtils.trimToEmpty(noticeApp.getNoticeNo())+ "国有建设用地使用权挂牌出让活动已经结束，感谢您对我市土地市场的参与和支持，希望您继续关注我市土地市场动态。";
				String mobile = mobileBuffer.toString();
				if (StringUtils.isNotEmpty(mobile))
					mobile = mobile.substring(0,mobile.length()- 1);
				msglist.add(message);
				msglist.add(mobile);
				//CoreService.getSmsSupport().sendSms(mobile, message);
				// 发送意向成交地块信息
				
				List tranList = wxDbService.findBlockAppList(noticeId);
				for (int i = 0 ; null != tranList && i < tranList.size(); i++) {
					TdscBlockTranApp tranApp = (TdscBlockTranApp)tranList.get(i);
					int count = wxDbService.getTradeListCount(tranApp.getAppId());
					if (count == 0 && "01".equals(tranApp.getTranResult())){
						// 为意向成交地块或者限时竞价环节无人出价的地块
						TdscBidderView bidderView = wxDbService.getYixiangNameLikeAppId(tranApp.getAppId());
						if (null != bidderView && StringUtils.isNotBlank(bidderView.getConNum()) && bidderView.getConNum().equals(tranApp.getResultCert())) {
							// 如果该意向人存在，且该意向人为竞得人，则向该意向人发送信息2
							if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(bidderView.getIsAccptSms())) {
								TdscBlockInfo blockInfo = wxDbService.getBlockInfoByBlockId(tranApp.getBlockId());
								String returnStr = returnString(blockInfo);
								String msg = "尊敬的竞得人，恭喜您已成功竞得" +tranApp.getBlockNoticeNo()+ "地块的土地使用权。请按出让文件规定的相关要求缴纳交易服务费" + returnStr + "后，持《成交确认书》到无锡市国土资源局签订《无锡市国有建设用地使用权出让合同》。";
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
	 * 计算楼面地价
	 * 
	 * @param decimal
	 * @param totalBuildingArea
	 * @return
	 */
	private String getBuildingPrice(BigDecimal decimal, TdscBlockInfo blockInfo) {
		String blockQuality = blockInfo.getBlockQuality();
		if ("101".equals(blockQuality)) {
			return "亩均价为" + NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(666.67)).divide(blockInfo.getTotalLandArea(), BigDecimal.ROUND_HALF_UP),2) + "万元/亩";
		} else if ("102".equals(blockQuality)) {
			return "楼面地价为" + NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(10000)).divide(blockInfo.getTotalBuildingArea(), BigDecimal.ROUND_HALF_UP),2) + "元/平方米";
		}
		return null;
	}
	
	private String overPriceRate(BigDecimal topPrice, BigDecimal initPrice) {
		// （当前的价格-起始价）/起始价 *100%
		BigDecimal result = topPrice.subtract(initPrice).divide(initPrice,4, BigDecimal.ROUND_HALF_UP);
		result = result.multiply(new BigDecimal("100"));
		return NumberUtil.numberDisplay(result, 2) + '%';
	}

	private String returnString(TdscBlockInfo tdscBlockInfo) {
		BigDecimal d2 = new BigDecimal("0");
		if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(d2) > 0)
			return "和地质灾害危险性评估费";
		return "";
	}
	
	
}
