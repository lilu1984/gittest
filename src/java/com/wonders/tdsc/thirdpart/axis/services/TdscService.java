package com.wonders.tdsc.thirdpart.axis.services;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockConInfo;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;

public class TdscService {
    private static final Logger logger = Logger.getLogger(TdscService.class);

    /**
     * ������������ϵͳ�����������ػ�����Ϣ��������������Ϣ�����صĲ�汨����Ϣ
     * 
     * @param xmlBlockInfo
     *            ����������ϵͳЭ���xml�ִ�
     * @return 1�ɹ� 0ʧ��
     */
    public int receiptBlockInfo(String xmlBlockInfo) {
        logger.debug("[TdscService.receiptBlockInfo][xmlBlockInfo]:\r\n" + xmlBlockInfo);

        try {
            String mapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "tdscServiceMap.xml";
            logger.debug("mapPath:" + mapPath);

            // 1��ʹ��castor��xml�ַ���ת���javabean
            TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) CastorFactory.unMarshalBean(mapPath, xmlBlockInfo);

            // ===============================
            // 2008-06-10 weedlu
            // �����ֶ�Ϊ����֤�����ˡ������ƻ��ĺš�
            // ===============================
            validateBlockInfo(tdscBlockInfo);

            // 2�������ݱ��浽���ݿ⣬���ļ����浽Ӳ��
            TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance().getAppContext().getBean(
                    "tdscBlockInfoService");

            tdscBlockInfoService.addAuditedBlockInfo(tdscBlockInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return 0;
        }

        return 1;
    }

    /**
     * �����ֶ�Ϊ����֤�����ˡ������ƻ��ĺš�
     * @param tdscBlockInfo
     */
    private void validateBlockInfo(TdscBlockInfo tdscBlockInfo) {
        Validate.notNull(tdscBlockInfo, "���ػ�����Ϣ����Ϊnull��");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getAuditedNum()), "�������ĺŲ���Ϊ�գ�");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getBlockName()), "�ؿ����Ʋ���Ϊ�գ�");
        Validate.notNull(tdscBlockInfo.getDistrictId(), "�������ز���Ϊ�գ�");
        Validate.notNull(tdscBlockInfo.getAuditedDate(), "�����������ڲ���Ϊ�գ�");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getProgramAuditedOrg()), "�滮���ز���Ϊ�գ�");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getProgramNum()), "�滮�ĺŲ���Ϊ�գ�");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getPlanAuditedOrg()), "�ƻ����ز���Ϊ�գ�");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getPlanNum()), "�ƻ��ĺŲ���Ϊ�գ�");

        if (null != tdscBlockInfo.getUsedInfoList() && tdscBlockInfo.getUsedInfoList().size() > 0) {
            for (int i = 0; i < tdscBlockInfo.getUsedInfoList().size(); i++) {
                TdscBlockUsedInfo usedInfo = (TdscBlockUsedInfo) tdscBlockInfo.getUsedInfoList().get(i);

                Validate.notNull(StringUtils.trimToNull(usedInfo.getSurveyId()), "��汨��ţ�surveyId������Ϊ�գ�");
                Validate.notNull(StringUtils.trimToNull(usedInfo.getReportId()), "��汨��ţ�reportId������Ϊ�գ�");
                Validate.notNull(StringUtils.trimToNull(usedInfo.getLandUseType()), "������;����Ϊ�գ�");
                Validate.notNull(usedInfo.getBuildingArea(), "�����õ��������Ϊ�գ�");
            }
        }
    }

    /**
     * ������������ϵͳ�������ĺ�ͬ��Ϣ
     * 
     * @param xmlBlockInfo
     *            ����������ϵͳЭ���xml�ִ�
     * @return 1�ɹ� 0ʧ��
     */
    public int receiptContractInfo(String xmlContractInfo) {
        logger.debug("[TdscService.receiptBlockInfo][receiptContractInfo]:\r\n" + xmlContractInfo);

        try {
            String mapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "tdscServiceMap.xml";
            logger.debug("mapPath:" + mapPath);

            // 1��ʵ����Service
            TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance().getAppContext().getBean(
                    "tdscBlockInfoService");

            // 2��ʹ��castor��xml�ַ���ת���javabean
            // List blockConInfoList = (List) CastorFactory.unMarshalBean(mapPath, xmlContractInfo);
            TdscBlockConInfo tdscBlockConInfo = (TdscBlockConInfo) CastorFactory.unMarshalBean(mapPath, xmlContractInfo);

            // for (int i = 0; i < blockConInfoList.size(); i++) {
            // TdscBlockConInfo tdscBlockConInfo = (TdscBlockConInfo) blockConInfoList.get(i);
            // 3�����ݹ������ĺ�,��λһ��������Ϣ,ȡ��block_id
            TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByAuditedNum(tdscBlockConInfo.getAuditedNum());

            if (tdscBlockInfo != null) {
                // 4��ƴװ���µ����غ�ͬ��
                tdscBlockConInfo.setBlockId(tdscBlockInfo.getBlockId());
                tdscBlockInfoService.addBlockConInfo(tdscBlockConInfo);
            } else {
                logger.warn("There's no block of " + tdscBlockConInfo.getAuditedNum());
            }
            // }
        } catch (Exception e) {
            logger.error(e);

            return 0;
        }

        return 1;
    }
}
