package com.wonders.tdsc.randomselect.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscCompereInfo;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscSpecialistInfo;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.randomselect.dao.SpecialistInfoDao;
import com.wonders.tdsc.randomselect.dao.TdscBlockSelectAppDao;
import com.wonders.tdsc.randomselect.dao.TdscCompereInfoDao;
import com.wonders.tdsc.randomselect.dao.TdscNotaryInfoDao;
import com.wonders.tdsc.tdscbase.service.ShortMessageService;

public class TdscSelectService {

    private SpecialistInfoDao specialistInfoDao;

    private TdscBlockSelectAppDao tdscBlockSelectAppDao;

    private TdscNotaryInfoDao tdscNotaryInfoDao;

    private TdscCompereInfoDao tdscCompereInfoDao;

    private TdscBlockTranAppDao tdscBlockTranAppDao;

    private TdscBlockPlanTableDao tdscBlockPlanTableDao;

    private SpecialistInfoDao tdscSpecialistInfoDao;

    private ShortMessageService smsService;

    public void setTdscSpecialistInfoDao(SpecialistInfoDao tdscSpecialistInfoDao) {
        this.tdscSpecialistInfoDao = tdscSpecialistInfoDao;
    }

    public void setSpecialistInfoDao(SpecialistInfoDao specialistInfoDao) {
        this.specialistInfoDao = specialistInfoDao;
    }

    public void setTdscBlockSelectAppDao(TdscBlockSelectAppDao tdscBlockSelectAppDao) {
        this.tdscBlockSelectAppDao = tdscBlockSelectAppDao;
    }

    public void setTdscNotaryInfoDao(TdscNotaryInfoDao tdscNotaryInfoDao) {
        this.tdscNotaryInfoDao = tdscNotaryInfoDao;
    }

    public void setTdscCompereInfoDao(TdscCompereInfoDao tdscCompereInfoDao) {
        this.tdscCompereInfoDao = tdscCompereInfoDao;
    }

    public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
        this.tdscBlockTranAppDao = tdscBlockTranAppDao;
    }

    public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
        this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
    }

    public void setSmsService(ShortMessageService smsService) {
        this.smsService = smsService;
    }

    public List querySpecialistInfoList(Timestamp nowTime) {
        List specailistInfoList = specialistInfoDao.selectSpecialist(nowTime);
        return specailistInfoList;
    }

    public List querySpecialistInfoList() {
        List specailistInfoList = specialistInfoDao.findAll();
        return specailistInfoList;
    }

    public TdscBlockSelectApp saveSelectedSpecialistInfo(TdscBlockSelectApp tdscBlockSelectApp) {
        TdscBlockSelectApp returnApp = (TdscBlockSelectApp) tdscBlockSelectAppDao.save(tdscBlockSelectApp);
        return returnApp;
    }

    // 根据抽选类别查询专家
    public List querySpecialistInfoList(String specialistType) {
        List SpecialistInfo = specialistInfoDao.selectSpecialist(specialistType);
        return SpecialistInfo;
    }

    // 查询抽选出的主持人信息
    public TdscCompereInfo queryCompereInfoByCompereId(String compereId) {
        TdscCompereInfo tdscCompereInfo = this.tdscCompereInfoDao.findCompereInfoByCompereId(compereId);
        return tdscCompereInfo;
    }

    // 查询抽选出的公证处信息
    public TdscNotaryInfo queryNotaryInfoBynotaryId(String notaryId) {
        TdscNotaryInfo tdscNotaryInfo = this.tdscNotaryInfoDao.findNotaryInfoByNotaryId(notaryId);
        return tdscNotaryInfo;
    }

    // 根据公证处用户ID查询抽选出的公证处信息
    public TdscNotaryInfo queryNotaryInfoByUserId(String notaryUserId) {
        TdscNotaryInfo notaryInfo = this.tdscNotaryInfoDao.findNotaryInfoByUserId(notaryUserId);
        return notaryInfo;
    }
    
    //设置回复截止时间
    public String setReplyDeadLine() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String result=""; 
        Date dt = new Date();
        Calendar calendar=Calendar.getInstance(); 
        calendar.setTime(dt); 
        calendar.add(Calendar.DATE , 2); 
        result=format.format(calendar.getTime());     
        return result;
    }

    // 根据专家ID查询抽选出的专家信息
    public TdscSpecialistInfo getSpecialistInfoById(String specialistId) {
        TdscSpecialistInfo specialistInfo = (TdscSpecialistInfo) this.tdscSpecialistInfoDao.get(specialistId);
        return specialistInfo;
    }

    // 通过appId查出selectedId列表
    public List queryselectedId(String appId) {
        List selectedIdList = tdscBlockSelectAppDao.findselectedId(appId);
        return selectedIdList;
    }

    // 通过appId查出tdsc_block_plan_table中的一条记录
    public TdscBlockPlanTable query(String appId) {
        TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) this.tdscBlockPlanTableDao.findPlanTableInfo(appId);
        return tdscBlockPlanTable;
    }

    // 找出已选的抽选信息表中对应土地交易信息表的信息
    // 删除已选的
    public void removeBlockSelectAppListByAppId(String appId) {
        List tdscBlockSelectAppList = this.tdscBlockSelectAppDao.findTdscBlockSelectAppListByAppId(appId);
        for (int i = 0; i < tdscBlockSelectAppList.size(); i++) {
            TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) tdscBlockSelectAppList.get(i);
            this.tdscBlockSelectAppDao.delete(tdscBlockSelectApp);
        }
    }

    // 查询抽选信息
    public List queryBlockSelectAppListByAppId(String appId) {
        List appList = this.tdscBlockSelectAppDao.findTdscBlockSelectAppListByAppId(appId);
        return appList;
    }

    // 修改不可出席的专家的状态
    public void modifyState(TdscBlockSelectApp tdscBlockSelectApp) {
        this.tdscBlockSelectAppDao.update(tdscBlockSelectApp);
    }

    /**
     * 专家抽选
     * 
     * @param specialistType
     * @param flag
     * @return
     */
    public List specialistSelect(Date nowTime) {
        List sublist = new ArrayList();
        // 根据专家类别查出所需专家
        List SpecialistfirstList = specialistInfoDao.selectSpecialist("1001");
        SpecialistfirstList = this.select(SpecialistfirstList, nowTime);
        if (SpecialistfirstList != null && SpecialistfirstList.size() > 0) {
            Random r1 = new Random();
            int irdm1 = 0;
            irdm1 = r1.nextInt(SpecialistfirstList.size());
            sublist.add(SpecialistfirstList.get(irdm1));
        }

        List SpecialistsecondList = specialistInfoDao.selectSpecialist("1002");
        SpecialistfirstList = this.select(SpecialistsecondList, nowTime);
        if (SpecialistsecondList != null && SpecialistsecondList.size() > 0) {
            Random r2 = new Random();
            int irdm2 = 0;
            irdm2 = r2.nextInt(SpecialistsecondList.size());
            sublist.add(SpecialistsecondList.get(irdm2));

        }

        List SpecialistthirdList = specialistInfoDao.selectSpecialist("1003");
        SpecialistfirstList = this.select(SpecialistthirdList, nowTime);
        if (SpecialistthirdList != null && SpecialistthirdList.size() > 0) {
            Random r3 = new Random();
            int irdm3 = 0;
            irdm3 = r3.nextInt(SpecialistthirdList.size());
            sublist.add(SpecialistthirdList.get(irdm3));
        }

        List SpecialistforthList = specialistInfoDao.selectSpecialist("1004");
        SpecialistfirstList = this.select(SpecialistforthList, nowTime);
        if (SpecialistforthList != null && SpecialistforthList.size() > 0) {
            Random r4 = new Random();
            int irdm4 = 0;
            irdm4 = r4.nextInt(SpecialistforthList.size());
            sublist.add(SpecialistforthList.get(irdm4));
        }

        List SpecialistfifthList = specialistInfoDao.selectSpecialist("1005");
        SpecialistfirstList = this.select(SpecialistfifthList, nowTime);
        if (SpecialistfifthList != null && SpecialistfifthList.size() > 0) {
            Random r5 = new Random();
            int irdm5 = 0;
            irdm5 = r5.nextInt(SpecialistfifthList.size());
            sublist.add(SpecialistfifthList.get(irdm5));
        }

        List SpecialistsixList = specialistInfoDao.selectSpecialist("1006");
        SpecialistfirstList = this.select(SpecialistsixList, nowTime);
        if (SpecialistsixList != null && SpecialistsixList.size() > 0) {
            Random r6 = new Random();
            int irdm6 = 0;
            irdm6 = r6.nextInt(SpecialistsixList.size());
            sublist.add(SpecialistsixList.get(irdm6));
        }

        List SpecialistseventhList = specialistInfoDao.selectSpecialist("1007");
        SpecialistfirstList = this.select(SpecialistseventhList, nowTime);
        if (SpecialistseventhList != null && SpecialistseventhList.size() > 0) {
            Random r7 = new Random();
            int irdm7 = 0;
            for (int i = 0; i < 2; i++) {
                if (SpecialistseventhList != null && SpecialistseventhList.size() > 0) {
                    irdm7 = r7.nextInt(SpecialistseventhList.size());
                    sublist.add(SpecialistseventhList.get(irdm7));
                    SpecialistseventhList.remove(irdm7);
                }
            }
        }

        List SpecialisteighthList = specialistInfoDao.selectSpecialist("1008");
        SpecialistfirstList = this.select(SpecialisteighthList, nowTime);
        if (SpecialisteighthList != null && SpecialisteighthList.size() > 0) {
            Random r8 = new Random();
            int irdm8 = 0;
            for (int i = 0; i < 2; i++) {
                if (SpecialisteighthList != null && SpecialisteighthList.size() > 0) {
                    irdm8 = r8.nextInt(SpecialisteighthList.size());
                    sublist.add(SpecialisteighthList.get(irdm8));
                    SpecialisteighthList.remove(irdm8);
                }
            }

        }

        return sublist;
    }

    // 排除已选过的专家
    public List select(List specialistList, Date nowTime) {
        // 排除已选过的
        List selectedIdList = this.tdscBlockSelectAppDao.findAll();
        if (selectedIdList != null && selectedIdList.size() > 0 && specialistList != null && specialistList.size() > 0) {
            for (int i = 0; i < specialistList.size(); i++) {
                TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) specialistList.get(i);
                for (int j = 0; j < selectedIdList.size(); j++) {
                    String selectedId = ((TdscBlockSelectApp) selectedIdList.get(j)).getSelectedId();
                    String specialistType = ((TdscBlockSelectApp) selectedIdList.get(j)).getSpecialistType();
                    if (tdscSpecialistInfo.getSpecialistId().equals(selectedId) && tdscSpecialistInfo.getSpecialistType().equals(specialistType)) {
                        specialistList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
        // 排除当天已选的
        List todaySelectedIdList = (List) this.tdscBlockSelectAppDao.findTodaySelectedIdList(nowTime);
        for (int i = 0; i < specialistList.size(); i++) {
            if (todaySelectedIdList != null && todaySelectedIdList.size() > 0) {
                TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) specialistList.get(i);
                for (int j = 0; j < todaySelectedIdList.size(); j++) {
                    String selectedId = (String) todaySelectedIdList.get(j);
                    if (tdscSpecialistInfo.getSpecialistId().equals(selectedId)) {
                        specialistList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
        return specialistList;
    }

    /**
     * 公证处纵向抽选
     * 
     * @param selectType
     * @param blockId
     * @return
     */
    public TdscNotaryInfo verticalRangeSelect() {

        TdscNotaryInfo tdscNotaryInfo = new TdscNotaryInfo();
       // Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得所有公证处的List
        List notaryInfoList = this.tdscNotaryInfoDao.findAll();
        // 排除当天已选的公证处
//        List todaySelectedIdList = this.tdscBlockSelectAppDao.findTodaySelectedIdList(nowTime);
//        if (notaryInfoList != null) {
//            for (int i = 0; i < notaryInfoList.size(); i++) {
//                tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
//                if (todaySelectedIdList != null) {
//                    for (int j = 0; j < todaySelectedIdList.size(); j++) {
//                        String selectedId = (String) todaySelectedIdList.get(j);
//                        if (tdscNotaryInfo.getNotaryId().equals(selectedId)) {
//                            notaryInfoList.remove(i);
//                            i = 0;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
        if (notaryInfoList != null && notaryInfoList.size() > 0) {
            
            Random r = new Random();
            int irdm = 0;
            irdm = r.nextInt(notaryInfoList.size());
            tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(irdm);
        }
        return tdscNotaryInfo;
    }

    /**
     * 公证处横向抽选
     * 
     * @param selectType
     * @param blockId
     * @param purchaserList
     * @return
     */
    
    
    public String getOrderNo(){ 
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss"); 
        String nowdate = sdf.format(date); 
        return "01"+nowdate; 
    } 
    public List acrossSelect(String selectType, String appId, List purchaserList) {
        // 全部公证处数
        List notatyList = new ArrayList();
        // 所需的公证处数
        // int purchaserNo = purchaserList.size();
        // int notaryNo = purchaserNo / 10 + 1;
        int notaryNo = 1;
        TdscNotaryInfo tdscNotaryInfo = new TdscNotaryInfo();
        // TdscBlockSelectApp tdscBlockSelectApp = new TdscBlockSelectApp();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得全部公证处
        List notaryInfoList = this.tdscNotaryInfoDao.findAll();
        // 删除已选过的公证处
        List selectedNotaryList = this.tdscBlockSelectAppDao.findNotary(selectType);// 要去掉重复的Id
        // if(selectedNotaryList != null){
        // for(int i=0;i<notaryInfoList.size();i++){
        // tdscNotaryInfo = (TdscNotaryInfo)notaryInfoList.get(i);
        // for(int j=0;j<selectedNotaryList.size();j++){
        // String selectedId = (String)selectedNotaryList.get(j);
        // String notaryId = tdscNotaryInfo.getNotaryId();
        // if(notaryId.equals(selectedId)){
        // notaryInfoList.remove(i);
        // i=0;
        // break;
        // }
        // }
        // }
        // }
        if (selectedNotaryList == null) {
            for (int i = 0; i < notaryNo; i++) {
                tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                notatyList.add(tdscNotaryInfo);
            }
        } else {
            // 删除已选过的公证处
            List selectedIdList = this.tdscBlockSelectAppDao.findSecondStageList(appId);
            if (selectedIdList != null) {
                for (int i = 0; i < notaryInfoList.size(); i++) {
                    tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                    for (int j = 0; j < selectedIdList.size(); j++) {
                        String selectedId = (String) selectedIdList.get(j);
                        if (tdscNotaryInfo.getNotaryId().equals(selectedId)) {
                            notaryInfoList.remove(i);
                            i = 0;
                        }
                    }
                }
            }
            // 排除当天已选的公证处
            List todaySelectedIdList = this.tdscBlockSelectAppDao.findTodaySelectedIdList(nowTime);
            for (int i = 0; i < notaryInfoList.size(); i++) {
                tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                for (int j = 0; j < todaySelectedIdList.size(); j++) {
                    String selectedId = (String) todaySelectedIdList.get(j);
                    if (tdscNotaryInfo.getNotaryId().equals(selectedId)) {
                        notaryInfoList.remove(i);
                        i = 0;
                    }
                }
            }
            if (notaryInfoList != null && notaryInfoList.size() >= notaryNo) {
                for (int i = 0; i < notaryNo; i++) {
                    tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                    notatyList.add(tdscNotaryInfo);
                }
            }
        }
        return notatyList;
    }

    /**
     * 为专家和到帐审核抽选公证处
     * 
     * @param selectType
     * @param blockId
     * @param purchaserList
     * @return
     */
    public List acrossSelectToSpecialist(String appId) {

        List notatyList = new ArrayList();
        TdscNotaryInfo tdscNotaryInfo = new TdscNotaryInfo();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得全部公证处
        List notaryInfoList = this.tdscNotaryInfoDao.findAll();

        // 删除已选过的公证处
        List selectedIdList = this.tdscBlockSelectAppDao.findSecondStageList(appId);
        if (selectedIdList != null && selectedIdList.size() > 0 && notaryInfoList != null && notaryInfoList.size() > 0) {
            for (int i = 0; i < notaryInfoList.size(); i++) {
                tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                for (int j = 0; j < selectedIdList.size(); j++) {
                    String selectedId = (String) selectedIdList.get(j);
                    if (tdscNotaryInfo.getNotaryId().equals(selectedId)) {
                        notaryInfoList.remove(i);
                        i = 0;
                    }
                }
            }
        }
        // 排除当天已选的公证处
        List todaySelectedIdList = this.tdscBlockSelectAppDao.findTodaySelectedIdList(nowTime);
        for (int i = 0; i < notaryInfoList.size(); i++) {
            if (todaySelectedIdList != null && todaySelectedIdList.size() > 0) {
                tdscNotaryInfo = (TdscNotaryInfo) notaryInfoList.get(i);
                for (int j = 0; j < todaySelectedIdList.size(); j++) {
                    String selectedId = (String) todaySelectedIdList.get(j);
                    if (tdscNotaryInfo.getNotaryId().equals(selectedId)) {
                        notaryInfoList.remove(i);
                        i = 0;
                    }
                }
            }
        }
        // 随机抽选一位公证处
        Random r = new Random();
        int irdm = 0;
        irdm = r.nextInt(notaryInfoList.size());
        notatyList.add(notaryInfoList.get(irdm));

        return notatyList;
    }

    /**
     * 主持人抽选
     * 
     * @param selectType
     * @param blockId
     * @param purchaserList
     * @return
     */
    public List compereSelect(String selectType, String appId) {

        List compereList = new ArrayList();

        TdscCompereInfo tdscCompereInfo = new TdscCompereInfo();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        // 取得全部主持人

        List compereInfoList = this.tdscCompereInfoDao.findAll();

        // 所需主持人数
        int compereNo = 1;

        // 删除已选过的主持人
        List selectedCompereList = this.tdscBlockSelectAppDao.findNotary(selectType);// 要去掉重复的Id

        if (selectedCompereList == null) {
            for (int i = 0; i < compereNo; i++) {
                if (compereInfoList != null && compereInfoList.size() > 0) {
                    tdscCompereInfo = (TdscCompereInfo) compereInfoList.get(i);
                    compereList.add(tdscCompereInfo);
                }

            }
        } else {
            // 删除已选过的
            List selectedIdList = this.tdscBlockSelectAppDao.findSecondStageList(appId);
            if (selectedIdList != null && selectedIdList.size() > 0) {
                for (int i = 0; i < compereInfoList.size(); i++) {
                    tdscCompereInfo = (TdscCompereInfo) compereInfoList.get(i);
                    for (int j = 0; j < selectedIdList.size(); j++) {
                        String selectedId = (String) selectedIdList.get(j);
                        if (tdscCompereInfo.getCompereId().equals(selectedId)) {
                            compereInfoList.remove(i);
                            i = 0;
                        }
                    }
                }
            }
            // 排除当天已选的
            List todaySelectedIdList = this.tdscBlockSelectAppDao.findTodaySelectedIdList(nowTime);
            if (compereInfoList != null && compereInfoList.size() > 0) {
                for (int i = 0; i < compereInfoList.size(); i++) {
                    tdscCompereInfo = (TdscCompereInfo) compereInfoList.get(i);
                    if (todaySelectedIdList != null && todaySelectedIdList.size() > 0) {
                        for (int j = 0; j < todaySelectedIdList.size(); j++) {
                            String selectedId = (String) todaySelectedIdList.get(j);
                            if (tdscCompereInfo.getCompereId().equals(selectedId)) {
                                compereInfoList.remove(i);
                                i = 0;
                            }
                        }
                    }
                }
            }

            // 随机抽选一位主持人
            Random r = new Random();
            int irdm = 0;
            irdm = r.nextInt(compereInfoList.size());
            compereList.add(compereInfoList.get(irdm));
        }
        return compereList;

    }

    /**
     * 保存抽选结果在抽选情况表中
     * 
     * @param tdscBlockSelectApp
     */
    public void saveBlockSelectApp(TdscBlockSelectApp tdscBlockSelectApp) {
        this.tdscBlockSelectAppDao.save(tdscBlockSelectApp);
    }

    /**
     * 使用抽选类型查询公证处抽选
     * 
     * @param selectType
     * @return
     */
    public List querySpilthList(Timestamp nowTime, String blockId) {
        List spilthList = this.tdscBlockSelectAppDao.findSpilthList(nowTime, blockId);
        return spilthList;
    }
    
    /**
     * 使用抽选类型APPID查询公证处抽选
     * 
     * @param selectType
     * @return
     */
    public List findBlockSelectList(String selectType,String appId) {
        List notaryList = this.tdscBlockSelectAppDao.findBlockSelectList(selectType, appId);
        return notaryList;
    }

    /**
     * 删除多余的公证处抽选记录
     * 
     * @param tdscBlockSelectApp
     */
    public void deleteSpilthList(TdscBlockSelectApp tdscBlockSelectApp) {
        this.tdscBlockSelectAppDao.delete(tdscBlockSelectApp);
    }

    /**
     * 删除更改的专家
     * 
     * @param specialistId
     */
    public void removeChangeSpecialist(TdscBlockSelectApp tdscBlockSelectApp) {
        this.tdscBlockSelectAppDao.delete(tdscBlockSelectApp);
    }

    /**
     * 更改TdscBlockTranApp表中的状态
     * 
     * @param appId
     * @param flag
     */
    public void updatestate(String appId, String flag) {
        TdscBlockTranApp temp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
        if ("1".equals(flag))
            temp.setHasSelectCompere("01");
        else if ("2".equals(flag))
            temp.setHasSelectBNotary("01");
        else if ("3".equals(flag))
            temp.setHasSelectCNotary("01");
        else if ("4".equals(flag))
            temp.setHasSelectSpecialist("01");
        tdscBlockTranAppDao.update(temp);
    }

    public List findselectAppInfo(String appId) {
        return (List) tdscBlockSelectAppDao.findSelectAppbyAppId(appId);
    }

    public List findselectAppInfo(String appId, String isValid) {
        return (List) tdscBlockSelectAppDao.findSelectAppbyAppId(appId, "01");
    }

    public void update(String selectId, String replyStatus) {
        TdscBlockSelectApp temp = (TdscBlockSelectApp) tdscBlockSelectAppDao.get(selectId);
        if ("00".equals(replyStatus)) {
            temp.setReplyStatus("05");
            tdscBlockSelectAppDao.update(temp);
        }
    }

    public List getSpecailistInfoList(String appId) {
        List selectAppList = tdscBlockSelectAppDao.findSelectAppbyAppIdAndSelectType(appId, "05");
        List SpecialistInfoList = new ArrayList();
        List resultList = new ArrayList();
        if (selectAppList != null && selectAppList.size() > 0) {
            for (int i = 0; i < selectAppList.size(); i++) {
                TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) selectAppList.get(i);
                TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) tdscSpecialistInfoDao.get(tdscBlockSelectApp.getSelectedId());
                SpecialistInfoList.add(tdscSpecialistInfo);
            }
        }
        resultList.add(selectAppList);
        resultList.add(SpecialistInfoList);
        if (selectAppList != null && selectAppList.size() > 0) {
            String replyStatus[] = { "01", "03", "" };
            // 确认出席人数
            resultList.add(String.valueOf(tdscBlockSelectAppDao.getCountByAppId(appId, replyStatus, "05")));
            replyStatus[0] = "02";
            replyStatus[1] = "04";
            replyStatus[2] = "05";
            // 确认不出席人数
            resultList.add(String.valueOf(tdscBlockSelectAppDao.getCountByAppId(appId, replyStatus, "05")));
            // 累计抽选人数
            resultList.add(String.valueOf(tdscBlockSelectAppDao.getCountByAppId(appId, null, "05")));
        }
        return resultList;
    }

    public TdscBlockSelectApp getTdscBlockSelectAppBySelectId(String selectId) {
        return (TdscBlockSelectApp) tdscBlockSelectAppDao.get(selectId);
    }

    public void changeStatusBySelectId(String selectId, String replyStatus) {
        TdscBlockSelectApp app = (TdscBlockSelectApp) tdscBlockSelectAppDao.get(selectId);
        app.setReplyStatus(replyStatus);
        app.setReplyDate(DateUtil.string2Timestamp(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATETIME), DateUtil.FORMAT_DATETIME));
        tdscBlockSelectAppDao.update(app);
    }

    public List reSelectSpecailist(String appId, String user) {
        // 取出有效专家（10）
        List specailist = tdscBlockSelectAppDao.findListByAppIdAndisValid(appId);
        List newSpecailistList = new ArrayList();
        List SpecialistInfoList = new ArrayList();
        List selectAppList = new ArrayList();

        TdscBlockPlanTable tdscBlockPlanTable = this.query(appId);
        if (specailist != null && specailist.size() > 0) {
            for (int i = 0; i < specailist.size(); i++) {
                TdscBlockSelectApp temp = (TdscBlockSelectApp) specailist.get(i);
                TdscSpecialistInfo newSpecialist = new TdscSpecialistInfo();
                // 重新抽选
                if ("02".equals(temp.getReplyStatus()) || "04".equals(temp.getReplyStatus()) || "05".equals(temp.getReplyStatus())) {

                    List specilistList = tdscSpecialistInfoDao.findAll();
                    // 取出表中所有已被抽选过的专家
                    List sublist = tdscBlockSelectAppDao.findSelectspecialist("05");
                    // 选出未被选中的专家
                    for (int j = 0; j < specilistList.size(); j++) {
                        TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) specilistList.get(j);
                        String specialistId = tdscSpecialistInfo.getSpecialistId();
                        for (int k = 0; k < sublist.size(); k++) {
                            TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) sublist.get(k);
                            if (specialistId.equals(tdscBlockSelectApp.getSelectedId())) {
                                specilistList.remove(j);
                                j--;
                                sublist.remove(k);
                                k--;
                                break;
                            }
                        }
                    }

                    // 在未被选中的专家里抽出一名专家，替换不符合条件的专家
                    if (specilistList != null && specilistList.size() > 0) {
                        Random r1 = new Random();
                        int irdm1 = 0;
                        irdm1 = r1.nextInt(specilistList.size());
                        newSpecialist = (TdscSpecialistInfo) specilistList.get(irdm1);
                    }
                    if (specilistList != null && specilistList.size() > 0) {
                        // 更新对应的值
                        temp.setIsValid("00");
                        tdscBlockSelectAppDao.update(temp);
                        temp.setIsValid(GlobalConstants.SELECT_RESULT_VALID);
                        temp.setSelectedId(newSpecialist.getSpecialistId());
                        temp.setReplyStatus("00");
                        temp.setReplyDate(null);
                        temp.setSelectDate(DateUtil.string2Timestamp(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE));
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        now.setHours(23);
                        now.setMinutes(59);
                        now.setSeconds(59);
                        temp.setReplyDeadline(now);
                        temp.setSelectUser(user);
                        int count = tdscBlockSelectAppDao.getCountByAppId(appId, null, "") + 1;
                        String selectNum = temp.getSelectNum();
                        selectNum = selectNum.substring(0, selectNum.length() - 2);
                        if (String.valueOf(count).length() == 1)
                            selectNum = selectNum + "0" + String.valueOf(count);
                        else
                            selectNum = selectNum + String.valueOf(count);
                        temp.setSelectNum(selectNum);
                        temp.setSelectedId(newSpecialist.getSpecialistId());
                        tdscBlockSelectAppDao.save(temp);
                        selectAppList.add(temp);
                        TdscSpecialistInfo tdscSpecialistInfo = (TdscSpecialistInfo) tdscSpecialistInfoDao.get(temp.getSelectedId());

                        if (tdscSpecialistInfo.getMobilphone() != null) {
                            String returnMsg = smsService.sendSpecialistMessage(tdscSpecialistInfo, tdscBlockPlanTable, temp);
                        }

                        SpecialistInfoList.add(tdscSpecialistInfo);
                    }

                }
            }
        }

        newSpecailistList.add(selectAppList);
        newSpecailistList.add(SpecialistInfoList);

        return newSpecailistList;
    }

    public String ifBuXuan(String appId) {
        List list = tdscBlockSelectAppDao.findSelectAppbyAppId(appId, "01");
        if (list != null && list.size() > 0) {
            if (list.size() != 10)
                return "1";
            for (int i = 0; i < list.size(); i++) {
                TdscBlockSelectApp temp = (TdscBlockSelectApp) list.get(i);
                String replyStatus = temp.getReplyStatus();
                if ("01".equals(temp.getIsValid()) && ("02".equals(replyStatus) || "05".equals(replyStatus) || "04".equals(replyStatus))) {
                    return "1";
                }
            }
        }
        return "0";
    }

    public List findSelectAppbyAppIdAndSelectType(String appId, String selectType) {
        return (List) tdscBlockSelectAppDao.findSelectAppbyAppIdAndSelectType(appId, selectType);
    }

    /**
     * 进度安排表中的公证处信息
     * 
     * @param appId
     * @param selectNum
     * @return
     */
    public TdscBlockSelectApp queryListingNotaryByAppIdSelectNum(String appId, String selectNum) {
        TdscBlockSelectApp selectApp = new TdscBlockSelectApp();
        selectApp = this.tdscBlockSelectAppDao.findSelectAppByAppIdSelectNum(appId, selectNum);
        return selectApp;
    }

    public String ifSelectAgain(String appId) {
        List list=this.getSpecailistInfoList(appId);
        String buChuxi="";
        String leiJi="";        
        List selectAppList=(List)list.get(0);
        if(selectAppList!=null&&selectAppList.size()>0){
            buChuxi=(String)list.get(3);
            leiJi=(String)list.get(4);
            if(Integer.parseInt(leiJi)-Integer.parseInt(buChuxi)!=10) return "1";  
        }
        return "0";
    }

}