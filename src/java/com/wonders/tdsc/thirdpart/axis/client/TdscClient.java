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
     * �����׽����Ϣ
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

            // 1����ȡTDSCAPPVIEW�е���Ϣ
            CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext()
                    .getBean("commonQueryService");
            TdscBidderAppService tdscBidderAppService = (TdscBidderAppService) AppContextUtil.getInstance()
                    .getAppContext().getBean("tdscBidderAppService");
            TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance()
                    .getAppContext().getBean("tdscBlockInfoService");
            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            condition.setAppId(appId);
            TdscBlockAppView blockAppView = commonQueryService.getTdscBlockAppView(condition);

            // 2����þ��õ��õؿ�ľ�������Ϣ
            TdscBidderApp bidderApp = tdscBidderAppService.getBidderAppByCertNo(blockAppView.getResultCert());
            if (bidderApp == null) {
                bidderApp = new TdscBidderApp();
            }

            List blockPartlist = tdscBlockInfoService.getTdscBlockPartList(blockAppView.getBlockId());

            List partList = new ArrayList();

            TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(blockAppView.getBlockId());

            TdscBlockTransferInfo transferInfo = new TdscBlockTransferInfo();
            // �ؿ�����
            transferInfo.setLandName(blockAppView.getBlockName());
            // ���÷�ʽ
            if ("3104".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("����");
            } else if ("3103".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("����");
            } else if ("3107".equals(blockAppView.getTransferMode())) {
                transferInfo.setTransferMode("�б�");
            }

            // �ؿ鹫���
            transferInfo.setBlockNoticeNo(blockAppView.getBlockNoticeNo());
            // �������ĺ�
            transferInfo.setAuditedNum(blockAppView.getAuditedNum());
            // �ر�Լ��
            transferInfo.setSpecialPromise(blockAppView.getSpecialPromise());

            // �ɽ��ܼ�
            transferInfo.setResultPrice(new Double(blockAppView.getResultPrice().doubleValue()));
            // �ɽ�����
            transferInfo.setResultDate(blockAppView.getResultDate());
            // ��ʱ����
            StringBuffer tmpRangeSide = new StringBuffer();
            StringBuffer tmpGreenRate = new StringBuffer();
            int index = 0;
            // 3������ӵؿ���Ϣ
            if (null != blockPartlist && blockPartlist.size() > 0) {
                for (int i = 0; i < blockPartlist.size(); i++) {
                    index++;
                    String blockName = "p" + index;
                    if (StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getBlockName())) {
                        blockName = new String(((TdscBlockPart) blockPartlist.get(i)).getBlockName());
                    }
                    tmpRangeSide.append(blockName).append("��");
                    // �ӵؿ�������Χ
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeWest() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeWest())) {
                        tmpRangeSide.append("����").append(((TdscBlockPart) blockPartlist.get(i)).getRangeWest()).append(
                                "��");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeEast() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeEast())) {
                        tmpRangeSide.append("����").append(((TdscBlockPart) blockPartlist.get(i)).getRangeEast()).append(
                                "��");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeSouth() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeSouth())) {
                        tmpRangeSide.append("����").append(((TdscBlockPart) blockPartlist.get(i)).getRangeSouth())
                                .append("��");
                    }
                    if (((TdscBlockPart) blockPartlist.get(i)).getRangeNorth() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getRangeNorth())) {
                        tmpRangeSide.append("����").append(((TdscBlockPart) blockPartlist.get(i)).getRangeNorth())
                                .append("��");
                    }
                    if ("��".equals(tmpRangeSide.substring(tmpRangeSide.length() - 1))) {
                        tmpRangeSide = new StringBuffer(tmpRangeSide.substring(0, tmpRangeSide.length() - 1));
                    }
                    tmpRangeSide.append("��");
                    if (tmpRangeSide.indexOf("����") > 0) {
                        tmpRangeSide = new StringBuffer("");
                    }
                    // �ӵؿ��̵���
                    if (((TdscBlockPart) blockPartlist.get(i)).getGreeningRate() != null
                            && StringUtils.isNotEmpty(((TdscBlockPart) blockPartlist.get(i)).getGreeningRate())) {
                    	tmpGreenRate.append(blockName).append("��");
                        tmpGreenRate.append(((TdscBlockPart) blockPartlist.get(i)).getGreeningRate()).append("��");
                    }
                    if (tmpGreenRate.indexOf("����") > 0) {
                        tmpGreenRate = new StringBuffer("");
                    }
                }

                //transferInfo.setFourSide(tmpRangeSide.toString());
                //transferInfo.setGreeningRate(tmpGreenRate.toString());
                transferInfo.setBlockPartList(blockPartlist);

            } else {
                TdscBlockPart TdscBlockPart = new TdscBlockPart();
                // ���ؿ�滮�ݻ���
                if (tdscBlockInfo.getVolumeRate() != null && StringUtils.isNotEmpty(tdscBlockInfo.getVolumeRate())) {
                    TdscBlockPart.setVolumeRate(tdscBlockInfo.getVolumeRate());
                }
                // ���ؿ��������
                if (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY == Integer.parseInt((tdscBlockInfo.getBlockType()))) {
                    TdscBlockPart.setTransferLife("50");
                } else if (GlobalConstants.DIC_BLOCK_TYPE_COMMERCE == Integer.parseInt(tdscBlockInfo.getBlockType())
                        && tdscBlockInfo.getTransferLife() != null
                        && StringUtils.isNotEmpty(tdscBlockInfo.getTransferLife())) {
                    TdscBlockPart.setTransferLife(tdscBlockInfo.getTransferLife());
                }
                // ���ؿ�������;
                List tdscUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(tdscBlockInfo
                        .getBlockId());

                if (tdscUsedInfoList != null && tdscUsedInfoList.size() > 0) {
                    TdscBlockPart.setLandUseType(((TdscBlockUsedInfo) tdscUsedInfoList.get(0)).getLandUseType());
                }
                // ���ؿ�������
                if (tdscBlockInfo.getTotalBuildingArea() != null
                        && StringUtils.isNotEmpty(tdscBlockInfo.getTotalBuildingArea().toString())) {
                    TdscBlockPart.setTotalLandArea(tdscBlockInfo.getTotalBuildingArea());
                }
                partList.add(TdscBlockPart);
                transferInfo.setBlockPartList(partList);
            }

	            StringBuffer preRangeSide = new StringBuffer();
	            StringBuffer returnRangeSide = new StringBuffer();
	            // ���ؿ�������Χ
	            if (tdscBlockInfo.getRangeWest() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeWest())) {
	            	preRangeSide.append("����").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeWest())).append("��");
	            }
	            if (tdscBlockInfo.getRangeEast() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeEast())) {
	            	preRangeSide.append("����").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeEast())).append("��");
	            }
	            if (tdscBlockInfo.getRangeSouth() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeSouth())) {
	            	preRangeSide.append("����").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeSouth()))
	                        .append("��");
	            }
	            if (tdscBlockInfo.getRangeNorth() != null && StringUtils.isNotEmpty(tdscBlockInfo.getRangeNorth())) {
	            	preRangeSide.append("����").append(StringUtils.trimToEmpty(tdscBlockInfo.getRangeNorth()))
	                        .append("��");
	            //�жϸ��ؿ�������Χ�Ƿ�Ϊ��	
	        	if(!"".equals(preRangeSide.toString())){
	        		returnRangeSide.append(tdscBlockInfo.getBlockName()).append("��").append(preRangeSide.toString());
	        	}
                transferInfo.setFourSide(returnRangeSide.toString()+tmpRangeSide.toString());
            }
            // ���ؿ��̻���
            StringBuffer preGreeningRate=new StringBuffer() ;
            if (tdscBlockInfo.getGreeningRate() != null && StringUtils.isNotEmpty(tdscBlockInfo.getGreeningRate())) {
            	preGreeningRate.append(tdscBlockInfo.getBlockName()).append("��").append(tdscBlockInfo.getGreeningRate()).append("��");
            }                

            transferInfo.setGreeningRate(preGreeningRate.toString()+tmpGreenRate.toString());
            // 4���������б�
            List bidderPersonList = new ArrayList();
            List list = tdscBidderAppService.queryBidderPersonList(bidderApp.getBidderId());
            for (int j = 0; j < list.size(); j++) {
                TdscBidderPersonApp personApp = (TdscBidderPersonApp) list.get(j);

                // ����������
                personApp.setBidderPropertyName(personApp.getBidderProperty());

                bidderPersonList.add(personApp);
            }
            transferInfo.setBidderList(bidderPersonList);

            // 5���ɽ�ȷ���飨Base64��

            // /--------------�ֶ����ֵ,����velocityģ��-----------------------------///
            Map velocityMap = new HashMap();
            // �ɽ��۸�
            velocityMap.put("resultPrice", blockAppView.getResultPrice());

            // �����ص�
            velocityMap.put("auctionLoc", DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_ID_HOUSE_PM,
                    blockAppView.getAuctionLoc()));

            // ����������
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

            // �ʸ�֤����
            velocityMap.put("certNo", StringUtils.trimToEmpty(bidderApp.getCertNo()));

            // �ؿ�����
            velocityMap.put("blockName", StringUtils.trimToEmpty(blockAppView.getBlockName()));

            // �ؿ鹫���
            velocityMap.put("blockNoticeNo", StringUtils.trimToEmpty(blockAppView.getBlockNoticeNo()));

            // ����������
            velocityMap.put("resultName", StringUtils.trimToEmpty(blockAppView.getResultName()));

            // ���׷�ʽ
            velocityMap.put("transferMode", StringUtils.trimToEmpty(blockAppView.getTransferMode()));

            // ��������
            velocityMap.put("blockType", StringUtils.trimToEmpty(DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_BLOCK_TYPE, blockAppView.getBlockType())));

            // �ؿ鹫���
            velocityMap.put("blockNoticeNo", StringUtils.trimToEmpty(blockAppView.getBlockNoticeNo()));

            // ����id
            velocityMap.put("districtId", blockAppView.getDistrictId());

            // ������
            velocityMap.put("remisePerson", StringUtils.trimToEmpty(DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_ID_DISTRICT_ORGAN, blockAppView.getRemisePerson())));

            // �б�����
            if (blockAppView.getResultDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                velocityMap.put("resultDate", sdf.format(blockAppView.getResultDate()));

                // ���б����ں�timeDiff������
                Calendar c = Calendar.getInstance();
                c.setTime(blockAppView.getResultDate());

                int timeDiff = 0;
                if (blockAppView.getTimeDiff() != null) {
                    timeDiff = Integer.parseInt(blockAppView.getTimeDiff());
                }
                c.add(Calendar.DAY_OF_MONTH, timeDiff);

                velocityMap.put("resultDateAfter10Days", sdf.format(c.getTime()));

                // ���������
                velocityMap.put("year", new Integer(c.get(Calendar.YEAR)));
            }

            // �б�۸�
            velocityMap.put("resultPrice", blockAppView.getResultPrice());

            // �б�۸��д
            if (blockAppView.getResultPrice() != null)
                velocityMap.put("resultPriceToUpperCase", MoneyUtils.NumToRMBStr(blockAppView.getResultPrice()
                        .doubleValue()));

            // ����
            velocityMap.put("district", DicDataUtil.getInstance().getDic(GlobalConstants.DIC_DISTRICT).get(
                    blockAppView.getDistrictId()));

            // ���Ĺ�
            velocityMap.put("transferMode", DicDataUtil.getInstance().getDicItemName(
                    GlobalConstants.DIC_BLOCK_TRANSFER, blockAppView.getTransferMode()));

            // �ؿ鹫���
            velocityMap.put("blockNoticeNo", blockAppView.getBlockNoticeNo());

            // ��������
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

            // ת����xml
            xmlTransferInfo = CastorFactory.marshalBean(transferInfo, mapPath);

        } catch (Exception e) {
            logger.error(e);
        }

        return xmlTransferInfo;
    }

    /**
     * �����ؽ�����Ϣ���͸�����ϵͳ
     * 
     * @param appId
     * @return
     */
    public Integer sendTransferInfo(String appId) {
        Integer rtn = null;

        try {
            String endpoint = (String) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.WebServiceInvokeEndpoint);
            logger.info("[����˫��ӿ�][��ʼ][endpoint]��" + endpoint);

            Service service = new Service();
            Call call = (Call) service.createCall();

            // call.getMessageContext().setUsername("landaudit"); // �û�����
            // call.getMessageContext().setPassword("landaudit1"); // ����

            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName("callBlock");

            String strXml = tidyTransferInfo(appId);

            logger.info("[����˫��ӿ�][appId]��" + appId);
            logger.debug("[����˫��ӿ�][��˫�������Ϊ]��" + strXml);

            rtn = (Integer) call.invoke(new Object[] { strXml });

            logger.info("[����˫��ӿ�][����][����ֵ]��" + rtn);

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
