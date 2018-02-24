package com.wonders.tdsc.thirdpart.axis.client;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockTransferInfo;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.common.util.MoneyUtils;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.tdsc.utils.velocity.VelocityUtils;

public class TdscClient {
    private static final Logger logger = Logger.getLogger(TdscClient.class);

    /**
     * 整理交易结果信息
     * 
     * @param appId
     * @return
     */
    public String tidyTransferInfo(String appId) {
        if (appId == null || "".equals(appId))
            return null;

        String xmlTransferInfo = null;
        try {
            String mapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator
                    + "tdscServiceMap.xml";
            // logger.debug("mapPath:" + mapPath);

            // 1、获取TDSCAPPVIEW中的信息
            CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext()
                    .getBean("commonQueryService");
            TdscBidderAppService tdscBidderAppService = (TdscBidderAppService) AppContextUtil.getInstance()
                    .getAppContext().getBean("tdscBidderAppService");
            TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance()
                    .getAppContext().getBean("tdscBlockInfoService");
            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            condition.setAppId(appId);
            TdscBlockAppView blockAppView = commonQueryService.getTdscBlockAppView(condition);

            // 2、获得竞得到该地块的竞买人信息
            TdscBidderApp bidderApp = tdscBidderAppService.getBidderAppByCertNo(blockAppView.getResultCert());
            if (bidderApp == null) {
                bidderApp = new TdscBidderApp();
            }

            List blockPartlist = tdscBlockInfoService.getTdscBlockPartList(blockAppView.getBlockId());

            List partList = new ArrayList();

            TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(blockAppView.getBlockId());

            TdscBlockTransferInfo transferInfo = new TdscBlockTransferInfo();
            // 地块名称
            transferInfo.setLandName(blockAppView.getBlockName());
            // 出让方式
            if ("3104".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("挂牌");
            } else if ("3103".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("拍卖");
            } else if ("3107".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("招标");
            }

            // 地块公告号
            transferInfo.setBlockNoticeNo(blockAppView.getBlockNoticeNo());
            // 供地批文号
            transferInfo.setAuditedNum(blockAppView.getAuditedNum());
            // 特别约定
            transferInfo.setSpecialPromise(blockAppView.getSpecialPromise());

            // 成交总价
            transferInfo.setResultPrice(new Double(blockAppView.getResultPrice().doubleValue()));
            // 成交日期
            transferInfo.setResultDate(blockAppView.getResultDate());
            // 临时变量
            StringBuffer tmpRangeSide = new StringBuffer();
            StringBuffer tmpGreenRate = new StringBuffer();
            int index = 0;
            // 3、获得子地块信息
            if (null != blockPartlist && blockPartlist.size() > 0) {
                for (int i = 0; i < blockPartlist.size(); i++) {
                    index++;
                    String blockName = "p" + index;
                    if (StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getBlockName())) {
                        blockName = new String(((TdscBlockPart) blockPartlist.get(i)).getBlockName());
                    }
                    tmpRangeSide.append(blockName).append("：");
                    // 子地块四至范围
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeWest() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeWest())) {
                        tmpRangeSide.append("西至").append(((TdscBlockPart) blockPartlist.get(i)).getRangeWest()).append(
                                "，");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeEast() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeEast())) {
                        tmpRangeSide.append("东至").append(((TdscBlockPart) blockPartlist.get(i)).getRangeEast()).append(
                                "，");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeSouth() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeSouth())) {
                        tmpRangeSide.append("南至").append(((TdscBlockPart) blockPartlist.get(i)).getRangeSouth())
                                .append("，");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeNorth() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeNorth())) {
                        tmpRangeSide.append("北至").append(((TdscBlockPart) blockPartlist.get(i)).getRangeNorth())
                                .append("，");
                    }
                    if ("，".equals(tmpRangeSide.substring(tmpRangeSide.length() - 1))) {
                        tmpRangeSide = new StringBuffer(tmpRangeSide.substring(0, tmpRangeSide.length() - 1));
                    }
                    tmpRangeSide.append("；");
                    if (tmpRangeSide.indexOf("：；") > 0) {
                        tmpRangeSide = new StringBuffer("");
                    }
                    // 子地块绿地率
                    if (((TdscBlockPart) blockPartlist.get(i)).getGreeningRate() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getGreeningRate())) {
                    	tmpGreenRate.append(blockName).append("：");
                        tmpGreenRate.append(((TdscBlockPart) blockPartlist.get(i)).getGreeningRate()).append("；");
                    }
                    if (tmpGreenRate.indexOf("：；") > 0) {
                        tmpGreenRate = new StringBuffer("");
                    }
                }

                //transferInfo.setFourSide(tmpRangeSide.toString());
                //transferInfo.setGreeningRate(tmpGreenRate.toString());
                transferInfo.setBlockPartList(blockPartlist);

            } else {
                TdscBlockPart TdscBlockPart = new TdscBlockPart();
                // 父地块规划容积率
                if (tdscBlockInfo.getVolumeRate() != null && StringUtils.isNotEmpty(tdscBlockInfo.getVolumeRate())) {
                    TdscBlockPart.setVolumeRate(tdscBlockInfo.getVolumeRate());
                }
                // 父地块出让年限
                if (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY == Integer.parseInt((tdscBlockInfo.getBlockType()))) {
                    TdscBlockPart.setTransferLife("50");
                } else if (GlobalConstants.DIC_BLOCK_TYPE_COMMERCE == Integer.parseInt(tdscBlockInfo.getBlockType())
                        && tdscBlockInfo.getTransferLife() != null
                        && StringUtils.isNotEmpty(tdscBlockInfo.getTransferLife())) {
                    TdscBlockPart.setTransferLife(tdscBlockInfo.getTransferLife());
                }
                // 父地块土地用途
                List tdscUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(tdscBlockInfo
                        .getBlockId());

                if (tdscUsedInfoList != null && tdscUsedInfoList.size() > 0) {
                    TdscBlockPart.setLandUseType(((TdscBlockUsedInfo) tdscUsedInfoList.get(0)).getLandUseType());
                }
                // 父地块出让面积
                if (tdscBlockInfo.getTotalBuildingArea() != null
                        && StringUtils.isNotEmpty(tdscBlockInfo.getTotalBuildingArea().toString())) {
                    TdscBlockPart.setTotalLandArea(tdscBlockInfo.getTotalBuildingArea());
                }
                partList.add(TdscBlockPart);
                transferInfo.setBlockPartList(partList);
            }

	            StringBuffer preRangeSide = new StringBuffer();
	            StringBuffer returnRangeSide = new StringBuffer();
	            // 父地块四至范围
	            if (tdscBlockInfo.getRangeWest() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeWest())) {
	            	preRangeSide.append("西至").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeWest())).append("，");
	            }
	            if (tdscBlockInfo.getRangeEast() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeEast())) {
	            	preRangeSide.append("东至").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeEast())).append("，");
	            }
	            if (tdscBlockInfo.getRangeSouth() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeSouth())) {
	            	preRangeSide.append("南至").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeSouth()))
	                        .append("，");
	            }
	            if (tdscBlockInfo.getRangeNorth() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeNorth())) {
	            	preRangeSide.append("北至").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeNorth()))
	                        .append("；");
	            //判断父地块四至范围是否为空	
	        	if(!"".equals(preRangeSide.toString())){
	        		returnRangeSide.append(tdscBlockInfo.getBlockName()).append("：").append(preRangeSide.toString());
	        	}
                transferInfo.setFourSide(returnRangeSide.toString()+tmpRangeSide.toString());
            }
            // 父地块绿化率
            StringBuffer preGreeningRate=new StringBuffer() ;
            if (tdscBlockInfo.getGreeningRate() != null && StringUtils.isNotEmpty(tdscBlockInfo.getGreeningRate())) {
            	preGreeningRate.append(tdscBlockInfo.getBlockName()).append("：").append(tdscBlockInfo.getGreeningRate()).append("；");
            }                

            transferInfo.setGreeningRate(preGreeningRate.toString()+tmpGreenRate.toString());
            // 4、竞买人列表
            List bidderPersonList = new ArrayList();
            List list = tdscBidderAppService.queryBidderPersonList(bidderApp.getBidderId());
            for (int j = 0; j < list.size(); j++) {
                TdscBidderPersonApp personApp = (TdscBidderPersonApp) list.get(j);

                // 竞买人属性
                personApp.setBidderPropertyName(personApp.getBidderProperty());

                bidderPersonList.add(personApp);
            }
            transferInfo.setBidderList(bidderPersonList);

            // 5、成交确认书（Base64）

            // /--------------手动添加值,制作velocity模板-----------------------------///
            Map velocityMap = new HashMap();
            // 成交价格
            velocityMap.put("resultPrice", blockAppView.getResultPrice());

            // 拍卖地点
            velocityMap.put("auctionLoc", DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_ID_HOUSE_PM,
                    blockAppView.getAuctionLoc()));

            // 竞得人名称
            String bidderName = "";
            List templist = tdscBidderAppService.queryBidderPersonList(bidderApp.getBidderId());
            for (int j = 0; j < templist.size(); j++) {
                TdscBidderPersonApp personApp = (TdscBidderPersonApp) templist.get(j);
                bidderName += personApp.getBidderName() + ",";
            }
            if (!"".equals(bidderName) && bidderName.length() > 0) {
                bidderName = bidderName.substring(0, bidderName.length() - 1);
            }
            velocityMap.put("bidderName", StringUtils.trimToEmpty(bidderName));

            // 资格证书编号
            velocityMap.put("certNo", StringUtils.trimToEmpty(bidderApp.getCertNo()));

            // 地块名称
            velocityMap.put("blockName", StringUtils.trimToEmpty(blockAppView.getBlockName()));

            // 地块公告号
            velocityMap.put("blockNoticeNo", StringUtils.trimToEmpty(blockAppView.getBlockNoticeNo()));

            // 竞得人姓名
            velocityMap.put("resultName", StringUtils.trimToEmpty(blockAppView.getResultName()));

            // 交易方式
            velocityMap.put("transferMode", StringUtils.trimToEmpty(blockAppView.getTransferMode()));

            // 土地类型
            velocityMap.put("blockType", StringUtils.trimToEmpty(DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_BLOCK_TYPE, blockAppView.getBlockType())));

            // 地块公告号
            velocityMap.put("blockNoticeNo", StringUtils.trimToEmpty(blockAppView.getBlockNoticeNo()));

            // 区县id
            velocityMap.put("districtId", blockAppView.getDistrictId());

            // 出让人
            velocityMap.put("remisePerson", StringUtils.trimToEmpty(DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_ID_DISTRICT_ORGAN, blockAppView.getRemisePerson())));

            // 中标日期
            if (blockAppView.getResultDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                velocityMap.put("resultDate", sdf.format(blockAppView.getResultDate()));

                // 在中标日期后timeDiff的日期
                Calendar c = Calendar.getInstance();
                c.setTime(blockAppView.getResultDate());

                int timeDiff = 0;
                if (blockAppView.getTimeDiff() != null) {
                    timeDiff = Integer.parseInt(blockAppView.getTimeDiff());
                }
                c.add(Calendar.DAY_OF_MONTH, timeDiff);

                velocityMap.put("resultDateAfter10Days", sdf.format(c.getTime()));

                // 沪告字年号
                velocityMap.put("year", new Integer(c.get(Calendar.YEAR)));
            }

            // 中标价格
            velocityMap.put("resultPrice", blockAppView.getResultPrice());

            // 中标价格大写
            if (blockAppView.getResultPrice() != null)
                velocityMap.put("resultPriceToUpperCase", MoneyUtils.NumToRMBStr(blockAppView.getResultPrice()
                        .doubleValue()));

            // 区县
            velocityMap.put("district", DicDataUtil.getInstance().getDic(GlobalConstants.DIC_DISTRICT).get(
                    blockAppView.getDistrictId()));

            // 招拍挂
            velocityMap.put("transferMode", DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_BLOCK_TRANSFER, blockAppView.getTransferMode()));

            // 地块公告号
            velocityMap.put("blockNoticeNo", blockAppView.getBlockNoticeNo());

            // 土地类型
            velocityMap.put("blockType", DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TYPE,
                    blockAppView.getBlockType()));

            String afterVelocityMerge = "";
            if (GlobalConstants.DIC_TRANSFER_TENDER.equals(blockAppView.getTransferMode())) {
                afterVelocityMerge = VelocityUtils.merge(velocityMap, "qrtzs_zb");
            } else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(blockAppView.getTransferMode())) {
                afterVelocityMerge = VelocityUtils.merge(velocityMap, "qrtzs_pm");
            } else {
                afterVelocityMerge = VelocityUtils.merge(velocityMap, "qrtzs_gp");
            }
            transferInfo.setResultDoc(new String(Base64.encode(afterVelocityMerge.getBytes())));

            // 转化成xml
            xmlTransferInfo = CastorFactory.marshalBean(transferInfo, mapPath);

        } catch (Exception e) {
            logger.error(e);
        }

        return xmlTransferInfo;
    }

    /**
     * 将土地交易信息发送给审批系统
     * 
     * @param appId
     * @return
     */
    public Integer sendTransferInfo(String appId) {
        Integer rtn = null;

        try {
            String endpoint = (String) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.WebServiceInvokeEndpoint);
            logger.info("[调用双杨接口][开始][endpoint]：" + endpoint);

            Service service = new Service();
            Call call = (Call) service.createCall();

            // call.getMessageContext().setUsername("landaudit"); // 用户名。
            // call.getMessageContext().setPassword("landaudit1"); // 密码

            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName("callBlock");

            String strXml = tidyTransferInfo(appId);

            logger.info("[调用双杨接口][appId]：" + appId);
            logger.debug("[调用双杨接口][向双杨发送数据为]：" + strXml);

            rtn = (Integer) call.invoke(new Object[] { strXml });

            logger.info("[调用双杨接口][结束][返回值]：" + rtn);

        } catch (ServiceException e) {
            logger.error(e);
        } catch (MalformedURLException e) {
            logger.error(e);
        } catch (RemoteException e) {
            logger.error(e);
        }
        return rtn;
    }
}
