package com.wonders.tdsc.tdscbase.service;

import java.text.SimpleDateFormat;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscSpecialistInfo;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.UrlUtils;
import com.wondertek.util.RandomStr;

public class ShortMessageService extends BaseSpringManagerImpl {


    /**
     * 处理专家短信发送功能
     * 
     * @param condition
     * @return
     */
    public String sendSpecialistMessage(TdscSpecialistInfo specialistInfo, TdscBlockPlanTable planTable, TdscBlockSelectApp tempSelectApp) {
        String pp = RandomStr.getAuthenticationStr();
        String smsUrl = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_SHORTMESSAGE_URL);
        String specialistMessage = (String)DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_SHORTMESSAGE_SPECIALIST);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String msg =specialistMessage;
        msg = msg.replaceAll("<SpecialistName>", specialistInfo.getSpecialistName());
        msg = msg.replaceAll("<OpeningDate>", sdf.format(planTable.getOpeningDate()));
        msg = msg.replaceAll("<ReplyDeadline>", sdf.format(tempSelectApp.getReplyDeadline()));
        smsUrl += "&recver=" + specialistInfo.getMobilphone() + "&msg=" + msg + "&ext=01015&flag=1&pp=" + pp;
        return UrlUtils.visitURL(smsUrl);
    }
    
    /**
     * 处理公证处短信发送功能
     * 
     * @param condition
     * @return
     */
    public String sendNotaryMessage(TdscNotaryInfo tdscNotaryInfo, TdscBlockPlanTable planTable ,TdscBlockSelectApp tempSelectApp) {
        String pp = RandomStr.getAuthenticationStr();
        String smsUrl = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_SHORTMESSAGE_URL);
        String notaryMessage = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_SHORTMESSAGE_NOTARY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String msg = notaryMessage;
        msg = msg.replaceAll("<NotaryName>", tdscNotaryInfo.getNotaryName());
        msg = msg.replaceAll("<BlockName>", planTable.getBlockName());
        msg = msg.replaceAll("<ReplyDeadline>", sdf.format(tempSelectApp.getReplyDeadline()));
        smsUrl += "&recver=" + tdscNotaryInfo.getNotaryContactMobile() + "&msg=" + msg + "&ext=01015&flag=1&pp=" + pp;
        return UrlUtils.visitURL(smsUrl);
    }
}

