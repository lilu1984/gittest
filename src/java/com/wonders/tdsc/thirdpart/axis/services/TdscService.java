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
     * 接受土地审批系统传过来的土地基本信息，包括：土地信息和土地的测绘报告信息
     * 
     * @param xmlBlockInfo
     *            与土地审批系统协议的xml字串
     * @return 1成功 0失败
     */
    public int receiptBlockInfo(String xmlBlockInfo) {
        logger.debug("[TdscService.receiptBlockInfo][xmlBlockInfo]:\r\n" + xmlBlockInfo);

        try {
            String mapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "tdscServiceMap.xml";
            logger.debug("mapPath:" + mapPath);

            // 1、使用castor将xml字符串转变成javabean
            TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) CastorFactory.unMarshalBean(mapPath, xmlBlockInfo);

            // ===============================
            // 2008-06-10 weedlu
            // 增加字段为空验证，除了“储备计划文号”
            // ===============================
            validateBlockInfo(tdscBlockInfo);

            // 2、将数据保存到数据库，将文件保存到硬盘
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
     * 增加字段为空验证，除了“储备计划文号”
     * @param tdscBlockInfo
     */
    private void validateBlockInfo(TdscBlockInfo tdscBlockInfo) {
        Validate.notNull(tdscBlockInfo, "土地基本信息对象为null！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getAuditedNum()), "供地批文号不能为空！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getBlockName()), "地块名称不能为空！");
        Validate.notNull(tdscBlockInfo.getDistrictId(), "审批区县不能为空！");
        Validate.notNull(tdscBlockInfo.getAuditedDate(), "供地批文日期不能为空！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getProgramAuditedOrg()), "规划机关不能为空！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getProgramNum()), "规划文号不能为空！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getPlanAuditedOrg()), "计划机关不能为空！");
        Validate.notNull(StringUtils.trimToNull(tdscBlockInfo.getPlanNum()), "计划文号不能为空！");

        if (null != tdscBlockInfo.getUsedInfoList() && tdscBlockInfo.getUsedInfoList().size() > 0) {
            for (int i = 0; i < tdscBlockInfo.getUsedInfoList().size(); i++) {
                TdscBlockUsedInfo usedInfo = (TdscBlockUsedInfo) tdscBlockInfo.getUsedInfoList().get(i);

                Validate.notNull(StringUtils.trimToNull(usedInfo.getSurveyId()), "测绘报告号（surveyId）不能为空！");
                Validate.notNull(StringUtils.trimToNull(usedInfo.getReportId()), "测绘报告号（reportId）不能为空！");
                Validate.notNull(StringUtils.trimToNull(usedInfo.getLandUseType()), "土地用途不能为空！");
                Validate.notNull(usedInfo.getBuildingArea(), "建设用地面积不能为空！");
            }
        }
    }

    /**
     * 接受土地审批系统传过来的合同信息
     * 
     * @param xmlBlockInfo
     *            与土地审批系统协议的xml字串
     * @return 1成功 0失败
     */
    public int receiptContractInfo(String xmlContractInfo) {
        logger.debug("[TdscService.receiptBlockInfo][receiptContractInfo]:\r\n" + xmlContractInfo);

        try {
            String mapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "tdscServiceMap.xml";
            logger.debug("mapPath:" + mapPath);

            // 1、实例化Service
            TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance().getAppContext().getBean(
                    "tdscBlockInfoService");

            // 2、使用castor将xml字符串转变成javabean
            // List blockConInfoList = (List) CastorFactory.unMarshalBean(mapPath, xmlContractInfo);
            TdscBlockConInfo tdscBlockConInfo = (TdscBlockConInfo) CastorFactory.unMarshalBean(mapPath, xmlContractInfo);

            // for (int i = 0; i < blockConInfoList.size(); i++) {
            // TdscBlockConInfo tdscBlockConInfo = (TdscBlockConInfo) blockConInfoList.get(i);
            // 3、根据供地批文号,定位一条土地信息,取出block_id
            TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByAuditedNum(tdscBlockConInfo.getAuditedNum());

            if (tdscBlockInfo != null) {
                // 4、拼装成新的土地合同表
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
