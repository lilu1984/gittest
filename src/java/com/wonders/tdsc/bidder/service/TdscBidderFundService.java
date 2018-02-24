package com.wonders.tdsc.bidder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.tdsc.guard.GuardConstants;
import com.wonders.tdsc.guard.bo.CardData;
import com.wonders.tdsc.guard.bo.MessageData;
import com.wonders.tdsc.guard.service.GuardService;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bidder.dao.TdscBusinessRecordDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBidderFundService extends BaseSpringManagerImpl {

    private TdscBlockTranAppDao tdscBlockTranAppDao;

    private TdscBidderPersonAppDao tdscBidderPersonAppDao;

    private TdscBidderAppDao tdscBidderAppDao;

    private TdscBusinessRecordDao tdscBusinessRecordDao;

    private TdscBlockInfoDao tdscBlockInfoDao;

    private TdscBlockPlanTableDao tdscBlockPlanTableDao;
    
    private IdSpringManager idSpringManager;

    public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}
    
    public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
        this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
    }

    public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
        this.tdscBlockInfoDao = tdscBlockInfoDao;
    }

    public void setTdscBusinessRecordDao(TdscBusinessRecordDao tdscBusinessRecordDao) {
        this.tdscBusinessRecordDao = tdscBusinessRecordDao;
    }

    public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
        this.tdscBidderAppDao = tdscBidderAppDao;
    }

    public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
        this.tdscBlockTranAppDao = tdscBlockTranAppDao;
    }

    public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
        this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
    }
    

    /**
     * 根据条件获得一页的用户列表
     * 
     * @param condition
     *            查询条件对象
     * @return PageList对象
     */
    public PageList findPageList(TdscBidderCondition condition) {
        return tdscBlockTranAppDao.findPageList(condition);
    }

    public PageList findBidderPageList(TdscBaseQueryCondition condition) {
        return tdscBlockTranAppDao.findBidderPageList(condition);
    }

    /**
     * 根据条件查询
     * 
     * @param condition
     * @return
     */
    public List findReturnSetByCondition(TdscBidderCondition condition) {
        // 对bidderPersonApp 查询
        if (condition.getBidderName() != null || condition.getBidderZh() != null) {
            if (condition.getAcceptNo() == null && condition.getBidderType() == null) {
                List tempList = (List) findPageFundList(condition);

            }
        }
        // 对bidderApp 查询
        if (condition.getAcceptNo() != null || condition.getBidderType() != null) {
            if (condition.getBidderName() == null && condition.getBidderZh() == null) {
                // PageList tempList=(PageList)tdscBidderAppDao.

            }
        }
        return null;
    }

    /**
     * 根据condition中appId 查询出 TDSC_BIDDER_APP 表中相关的信息
     * 
     * @param condition
     * @return
     */
    public List findPageFundList(TdscBidderCondition condition) {
        /* 获得TdscBidderApp列表. */
        List bidderAppList = (List) tdscBidderAppDao.findBidderIdsByAppId(condition.getAppId());

        if (bidderAppList != null && bidderAppList.size() > 0) {
            /* 构造返回的 List */
            List returnList = new ArrayList();
            /* 循环 bidderAppList，获得多个TdscBidderApp */
            for (int i = 0; i < bidderAppList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();

                condition.setBidderId(bidderId);

                List tdscBidPerAppList = (List) tdscBidderPersonAppDao.findByBiDPerDzqk(condition);
                if (tdscBidPerAppList != null && tdscBidPerAppList.size() > 0) {
                    // 循环 tdscBidPerAppList，获得多个TdscBidderPersonApp.*/
                    for (int j = 0; j < tdscBidPerAppList.size(); j++) {
                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerAppList.get(j);
                        tdscBidderPersonApp.setBidderType(tdscBidderApp.getBidderType());
                        tdscBidderPersonApp.setBzjJnfs(tdscBidderApp.getBzjJnfs());
                        tdscBidderPersonApp.setAcceptNo(tdscBidderApp.getAcceptNo());
                        // 将每个 TdscBidderPersonApp添加到 returnList中.*/
                        // 单独竞买则直接加入list
                        if ("1".equals(tdscBidderApp.getBidderType())) {
                            returnList.add(tdscBidderPersonApp);
                        }
                        // 过滤掉 联合竞买中牵头人交纳保证金交纳方式下的 非牵头人
                        if ("2".equals(tdscBidderApp.getBidderType())) {
                            // 按比例投资的联合竞买 竞买人都加入list
                            if ("0".equals(tdscBidderApp.getBzjJnfs())) {
                                returnList.add(tdscBidderPersonApp);
                            }
                            // 牵头人交纳保证金时 只加牵头人
                            if ("1".equals(tdscBidderApp.getBzjJnfs())) {
                                if ("1".equals(tdscBidderPersonApp.getIsHead())) {
                                    returnList.add(tdscBidderPersonApp);
                                }
                            }
                        }
                    }
                }
            }
            return returnList;
        }
        return null;
    }

    /**
     * 查询到账情况 不为“无历史操作”的列表
     * 
     * @param condition
     * @return
     */
    public List findChangeFundList(TdscBidderCondition condition) {
        /* 获得TdscBidderApp列表. */
        List bidderAppList = (List) tdscBidderAppDao.findBidderIdsByAppId(condition.getAppId());

        if (bidderAppList != null && bidderAppList.size() > 0) {
            /* 构造返回的 List */
            List returnList = new ArrayList();
            /* 循环 bidderAppList，获得多个TdscBidderApp */
            for (int i = 0; i < bidderAppList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();
                condition.setBidderId(bidderId);
                List tdscBidPerAppList = (List) tdscBidderPersonAppDao.findChangePerDzqk(condition);
                if (tdscBidPerAppList != null && tdscBidPerAppList.size() > 0) {
                    // 循环 tdscBidPerAppList，获得多个TdscBidderPersonApp.*/
                    for (int j = 0; j < tdscBidPerAppList.size(); j++) {
                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerAppList.get(j);
                        // 把tdscBidderApp的保证金交纳方式，总体到账情况和竞买方式赋值给 tdscBidderPersonApp
                        tdscBidderPersonApp.setBzjztDzqk(tdscBidderApp.getBzjztDzqk());
                        tdscBidderPersonApp.setBidderType(tdscBidderApp.getBidderType());
                        tdscBidderPersonApp.setBzjJnfs(tdscBidderApp.getBzjJnfs());
                        tdscBidderPersonApp.setAcceptNo(tdscBidderApp.getAcceptNo());
                        // 将每个 TdscBidderPersonApp添加到 returnList中.*/
                        returnList.add(tdscBidderPersonApp);
                    }
                }

            }
            return returnList;
        }
        return null;
    }

    /**
     * 根据条件查询
     * 
     * @param condition
     * @return
     */
    public List findFundQueryList(TdscBidderCondition condition) {

        PageList queryList = tdscBidderPersonAppDao.findFundQueryList(condition);
        if (queryList != null) {
            List retList = queryList.getList();
            return retList;
        }
        return null;
    }

    /**
     * 通过appId 获得竞买人信息列表
     * 
     * @param appId
     * @return
     */
    public List queryBidPreByAppId(String appId) {
        /* 通过appId 获得该块地块招拍挂的所有申请信息. */
        List bidPerByAppIdList = (List) tdscBidderAppDao.findBidderIdsByAppId(appId);

        if (bidPerByAppIdList != null && bidPerByAppIdList.size() > 0) {
            List retList = new ArrayList();
            List bidAllList = new ArrayList();
            for (int i = 0; i < bidPerByAppIdList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidPerByAppIdList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();
                if (bidderId != null) {
                    /* 通过bidderId 获得该块申请的所有申请人信息. */
                    List bidPerDzqkList = (List) tdscBidderPersonAppDao.findTdscBidderPersonDzqkList(bidderId);
                    if (bidPerDzqkList != null && bidPerDzqkList.size() > 0) {
                        for (int j = 0; j < bidPerDzqkList.size(); j++) {
                            TdscBidderPersonApp personApp = (TdscBidderPersonApp) bidPerDzqkList.get(j);
                            /* 把申请人信息添加到 bidAllList中. */
                            bidAllList.add(personApp);
                        }
                    }
                }
            }
            retList.add(bidPerByAppIdList);
            retList.add(bidAllList);
            return retList;
        }
        return null;
    }

    /**
     * 修改竞买人的到账情况
     * 
     * @param personAppList
     */
    public void modifyPersonDzqk(List personAppList) {
        if (personAppList != null && personAppList.size() > 0) {
            // 根据传过来的BidderPersonId 查出原TdscBidderPersonApp的属性(为了查出 TdscBlockTranApp 的到账截至时间)
            TdscBidderPersonApp tmpPersonApp = (TdscBidderPersonApp) personAppList.get(0);
            TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(tmpPersonApp.getBidderPersonId());
            // 根据 bidderId 查出该申请的地块的信息
            String bidderId = (String) personApp.getBidderId();
            if (bidderId != null) {
                /* 构造 TdscBidderApp实体对象 并判断是否为空. */
                TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
                //TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.findPlanTableInfo(tdscBidderApp.getAppId());
                String appId = (String) tdscBidderApp.getAppId();
                if (appId != null) {
                    /* 构造 TdscBlockTranApp实体对象 并判断是否为空. */
                    TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
                    // 查询土地类型
                    TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.findBlockInfo(tdscBlockTranApp.getBlockId());
                    String blockType = (String) tdscBlockInfo.getBlockType();
                    String blockNpticeNo = (String) tdscBlockTranApp.getBlockNoticeNo();
                    Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); // 保证金截至时间
                    BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); // 保证金额度
                    if (tdscBlockTranApp != null) {
                        // 修改页面上传过来的每个竞买人的到账情况
                        for (int i = 0; i < personAppList.size(); i++) {
                            TdscBidderPersonApp persAppList = (TdscBidderPersonApp) personAppList.get(i);
                            TdscBidderPersonApp modpersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(persAppList.getBidderPersonId());
                            /* 若MarginEndDate为空，则只加入竞买人的到账时间和金额. */
                            if (persAppList.getBzjDzse() != null && persAppList.getBzjDzsj() != null) {
                                modpersonApp.setBzjDzse(persAppList.getBzjDzse());
                                modpersonApp.setBzjDzsj(persAppList.getBzjDzsj());
                                // Date bzjDzsj = (Date) persAppList.getBzjDzsj(); //保证金缴纳时间
                                // BigDecimal bzjDzse = (BigDecimal) persAppList.getBzjDzse(); //实际缴纳的数额

                                // String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);
                                // modpersonApp.setBzjDzqk(dzqk);
                                tdscBidderPersonAppDao.update(modpersonApp);
                            }
                        }
                        // 修改竞买的总体到账情况
                        int certNoCount = 0;
                        for (int i = 0; i < personAppList.size(); i++) {
                            TdscBidderPersonApp temppersonApp = (TdscBidderPersonApp) personAppList.get(i);
                            // 获得数据库中的实体对象
                            TdscBidderPersonApp oneBidPer = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(temppersonApp.getBidderPersonId());
                            String bidId = (String) oneBidPer.getBidderId();
                            TdscBidderApp oneBidder = (TdscBidderApp) tdscBidderAppDao.findOneBidderByBidderId(bidId);
                            // 判断竞买类型
                            // 1.单独竞买-- 申请的到账情况 = 竞买人的到账情况
                            if ("1".equals(oneBidder.getBidderType())) {
                                if (tdscBlockTranApp.getMarginEndDate() != null && tdscBlockTranApp.getMarginAmount() != null) {
                                    Date bzjDzsj = (Date) oneBidPer.getBzjDzsj(); // 保证金缴纳时间
                                    BigDecimal bzjDzse = (BigDecimal) oneBidPer.getBzjDzse(); // 实际缴纳的数额
                                    // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //保证金截至时间
                                    // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //保证金额度
                                    String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);

                                    oneBidder.setBzjztDzqk(dzqk); // 设置竞买申请的到账情况
                                    oneBidPer.setBzjDzqk(dzqk); // 设置竞买人的到账情况
                                }

                            }
                            // 2.联合竞买--(按比例交纳保证金) 申请的到账情况优先级 ：1.未到帐;2.未足额到账;3.逾期到账;4.按期到账
                            if ("2".equals(oneBidder.getBidderType()) && "0".equals(oneBidder.getBzjJnfs())) {
                                // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //保证金额度
                                // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //保证金截至时间
                                List tempPersonList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidId);
                                // 获得联合竞买人到账资金的总和,到账时间
                                // BigDecimal bzjDzseZh = new BigDecimal(0);
                                // int bzjDzseZhRet = 0;
                                BigDecimal bzjDzseZhRetText = null;
                                Date bzjDzsj = new Date();
                                if (tempPersonList != null && tempPersonList.size() > 0) {
                                    for (int j = 0; j < tempPersonList.size(); j++) {
                                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tempPersonList.get(j);
                                        // 资金的总和
                                        if (tdscBidderPersonApp.getBzjDzse() == null) {

                                        } else {
                                            if (j == 0) {
                                                bzjDzseZhRetText = tdscBidderPersonApp.getBzjDzse();
                                            } else {
                                                // bzjDzseZhRet+=tdscBidderPersonApp.getBzjDzse().intValue();
                                                bzjDzseZhRetText = tdscBidderPersonApp.getBzjDzse().add(bzjDzseZhRetText);
                                            }

                                            // bzjDzseZh.add(tdscBidderPersonApp.getBzjDzse());

                                        }

                                        if (tdscBidderPersonApp.getBzjDzsj() != null) {
                                            // 获得联合竞买人中到账最晚的一个人的到账时间
                                            if (j == 0) {
                                                bzjDzsj = tdscBidderPersonApp.getBzjDzsj(); // 到账时间
                                            } else {
                                                if (bzjDzsj.compareTo(tdscBidderPersonApp.getBzjDzsj()) == -1) {
                                                    bzjDzsj = tdscBidderPersonApp.getBzjDzsj();
                                                }
                                            }
                                        }

                                    }
                                }
                                // BigDecimal bzjDzseZhBigDecimal = new BigDecimal(bzjDzseZhRet);
                                String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzseZhRetText, marginAmount);
                                oneBidPer.setBzjDzqk(dzqk); // 设置竞买人的到账情况
                                oneBidder.setBzjztDzqk(dzqk); // 设置竞买申请的到账情况
                                // 

                            }
                            // 3.联合竞买--(牵头人交纳保证金)
                            if ("2".equals(oneBidder.getBidderType()) && "1".equals(oneBidder.getBzjJnfs())) {
                                List tdscBidPerList = (List) tdscBidderPersonAppDao.findTdscBidderPersonDzqkList(bidId);
                                if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
                                    // 设置竞买的总体到账情况
                                    for (int k = 0; k < tdscBidPerList.size(); k++) {
                                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(k);
                                        // 判断是否是牵头人
                                        if ("1".equals(tdscBidderPersonApp.getIsHead())) {
                                            Date bzjDzsj = (Date) oneBidPer.getBzjDzsj(); // 保证金缴纳时间
                                            BigDecimal bzjDzse = (BigDecimal) oneBidPer.getBzjDzse(); // 实际缴纳的数额
                                            // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //保证金截至时间
                                            // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //保证金额度
                                            String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);
                                            oneBidPer.setBzjDzqk(dzqk); // 设置竞买人的到账情况
                                            oneBidder.setBzjztDzqk(dzqk); // 设置竞买申请的到账情况

                                        }
                                    }
                                }
                            }
                            // 机审
                            String returnReviewOpnn = returnReviewResult(blockType, tdscBlockTranApp, oneBidder);
                            // String guardMessage = (String) sendGuardMessage(tdscBlockTranApp, tdscBlockPlanTable, tdscBidderApp);
                            // System.out.println("guardMessage====>" + guardMessage);

                            // 机审合格
                            if ("1".equals(returnReviewOpnn.substring(0, 1))) {
                                // 如果原来有资格证书编号则不变，如果没有再新增
                                if (oneBidder.getCertNo() == null) {
                                    certNoCount++;
                                    String certNo = getCertNo(blockNpticeNo);
                                    oneBidder.setCertNo(certNo);
                                }
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
                                oneBidder.setReviewOpnn("");
                            } else {
                                // 不合格
                                oneBidder.setCertNo(""); // 不合格时候清空原来的资格证书编号
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_BREVIEW_RESULT_NO);
                                oneBidder.setReviewOpnn(returnReviewOpnn.substring(1));
                            }
                            tdscBidderAppDao.update(oneBidder);
                            tdscBidderPersonAppDao.update(oneBidPer);
                        }
                    }

                }
            }
        }

    }
    
    
    
    /**
     * 人工修改竞买人的入围情况
     * 
     * @param personAppList
     */
    public void modifyRwqkByPerson(String appId, String bidId, String subtype) {
          if (bidId != null&&subtype!=null&&appId != null) {
          
        	  TdscBidderApp oneBidder = (TdscBidderApp) tdscBidderAppDao.findOneBidderByBidderId(bidId);
        	  
        	  /* 构造 TdscBlockTranApp实体对象 并判断是否为空. */
              TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
                   String blockNpticeNo = (String) tdscBlockTranApp.getBlockNoticeNo();
                           
              
              // 机审合格  subtype=1
                            if ("1".equals(subtype)) {
                                // 如果原来有资格证书编号则不变，如果没有再新增
                                if (oneBidder.getCertNo() == null) {
                                    String certNo = getCertNo(blockNpticeNo);
                                    oneBidder.setCertNo(certNo);
                                }
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
                                oneBidder.setReviewOpnn("");
                            } else {
                                // 不合格  subtype=0
                                oneBidder.setCertNo(""); // 不合格时候清空原来的资格证书编号
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_BREVIEW_RESULT_NO);
                                oneBidder.setReviewOpnn("");
                            }
                            tdscBidderAppDao.update(oneBidder);
                            //tdscBidderPersonAppDao.update(oneBidPer);
                        }
                    }


    /**
     * 生成资格证书编号blockNpticeNo+3位流水号
     * 
     * @param appId
     * @return
     */
    public String getCertNo(String blockNpticeNo, int certNoCount) {
        // 预生成一个资格证书编号
        if (blockNpticeNo == null) {
            blockNpticeNo = "000000000";
        }

        List blockNpticeNoList = (List) tdscBidderAppDao.getCretNo(blockNpticeNo);
        String tempCertNo = "000" + (blockNpticeNoList.size() + certNoCount);
        String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
        // 判断该资格证书编号是否已经存在

        List blNptNoList = new ArrayList();
        String queryCertNo = certNo;
        // 循环判断
        do {
            blNptNoList = (List) tdscBidderAppDao.getCretNo(queryCertNo);
            if (blNptNoList != null && blNptNoList.size() > 0) {
                certNoCount++;
                tempCertNo = "000" + (blockNpticeNoList.size() + certNoCount);
                queryCertNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
            }

        } while (blNptNoList == null);

        return queryCertNo;
    }

    /**
     * 判断 bzjDzsj保证金到账时间与 marginEndDate保证金截至时间的先后
     * 
     * @param bzjDzsj
     *            保证金到账时间
     * @param marginEndDate
     *            保证金截至时间
     * @return
     */
    public boolean timeCompare(Date bzjDzsj, Date marginEndDate) {
        /* timeCompare false 意味 到账时间比要求到账时间晚. */
        int i = bzjDzsj.compareTo(marginEndDate);
        if (i > 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断 bzjDzse竞买人实际交纳保证金与 marginAmount应交纳的保证金的大小
     * 
     * @param bzjDzse
     *            竞买人实际交纳保证金
     * @param marginAmount
     *            应交纳的保证金
     * @return
     */
    public boolean bzjCompare(BigDecimal bzjDzse, BigDecimal marginAmount) {
        int j = bzjDzse.compareTo(marginAmount);
        /* bzjCompare false 意味 金额不足. */
        if (j < 0) {
            return false;
        }
        return true;
    }

    
    /**
     * 根据blockNpticeNo生成资格证书编号
     * @param appId
     * @return
     */
    public String getCertNo(String blockNpticeNo) {
    	//获得一个预生成资格证书编号
    	String certNo=creatCertNo(blockNpticeNo);
        //检查是否唯一
    	while( checkCertNo(certNo)==false){
    		certNo=creatCertNo(blockNpticeNo);
    	}
    	
    	return certNo;
    }
    
    /**
     * 判断某个资格证书编号是否已经存在
     * @param certNo
     * @return
     */
    public boolean checkCertNo(String certNo){
    	List certList = (List)tdscBidderAppDao.getCretNo(certNo);
    	if(certList!=null&&certList.size()>0){
    		return false;
    	}
    	return true;
    }
    
    /**
     * 预生成资格证书编号
     * @param blockNpticeNo
     * @return
     */
    public String creatCertNo(String blockNpticeNo){
    	//预设置地块公告号
        if (blockNpticeNo == null) {
            blockNpticeNo = "000000000";
        }
        //流水号
    	String tempCertNo = "000" + idSpringManager.getIncrementId("CertNo"+blockNpticeNo);
    	//预生成资格证书编号
    	String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
    	return certNo;
    }
    
    /**
     * 判断 到账情况
     * 
     * @param bzjDzsj
     *            保证金到账时间
     * @param marginEndDate
     *            保证金截至时间
     * @param bzjDzse
     *            竞买人实际交纳保证金
     * @param marginAmount
     *            应交纳的保证金
     * @return
     */
    public String judgeDzqk(Date bzjDzsj, Date marginEndDate, BigDecimal bzjDzse, BigDecimal marginAmount) {
        String dzqk = null;
        boolean timeCompare = timeCompare(bzjDzsj, marginEndDate);
        boolean bzjCompare = bzjCompare(bzjDzse, marginAmount);
        /* 未到账 金额为0. */
        if (bzjDzse == null || bzjDzse.compareTo(new BigDecimal(0)) == 0) {
            dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_NULL;
        } else {
            /* 1.未足额到账 到账时间比要求到账时间 早或者晚 并且 金额不足但不为 0 ；（注：晚，只是一种状态，足额到账后可以改为逾期到账）. */
            if ((bzjDzse != null && bzjCompare == false && timeCompare == true) || (bzjCompare == false && timeCompare == false)) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_SHORTAGE;
            }
            /* 2.逾期到账 到账时间比要求到账时间 晚 并且 金额足. */
            if (bzjCompare == true && timeCompare == false) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_OVERDUE;
            }
            /* 3.按期到账 到账时间比要求到账时间 早 并且 金额足. */
            if (bzjCompare == true && timeCompare == true) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_ONTIME;
            }
        }
        return dzqk;
    }

    /**
     * 根据appId 查询 地块的 名称等信息
     * 
     */
    public TdscBlockTranApp queryOneBlockTran(String appId) {
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
        return tdscBlockTranApp;
    }

    /**
     * 根据appId 查出该地块的所有竞买人的机审结果
     * 
     * @param appId
     * @return
     */
    public List queryJsjg(String appId) {
        List jsjgList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);
        return jsjgList;
    }

    /**
     * 根据条件查询（list_jsjg.jsp 查询按钮）
     * 
     * @param condition
     * @return
     */
    public List queryJsjg(TdscBidderCondition condition) {
        List jsjgList = (List) tdscBidderAppDao.findBidderAppListByCondition(condition);
        return jsjgList;
    }

    /**
     * 根据appId,bidderId判断机审结果
     * 
     * @param blockType
     * @param appId
     * @param bidderId
     * @return
     */
    public String returnReviewResult(String blockType, String appId, String bidderId) {
        // 根据appId查询地块信息
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
        // 获得申请的信息（竞买方式）
        TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
        StringBuffer returnOpnn = new StringBuffer();
        String yOrN = "0";
        if (tdscBlockTranApp != null && tdscBidderApp != null) {
            // 获得该地块的 blockId
            String blockId = (String) tdscBlockTranApp.getBlockId();

            int count = 0;
            if (blockId != null) {
                // 单独竞买
                if ("1".equals(tdscBidderApp.getBidderType())) {
                    // TdscBidderPersonApp tdscBidderPersonApp =(TdscBidderPersonApp)tdscBidderPersonAppDao.getBidderByBidderId(bidderId);
                    // 判断
                    String returnReviewResult = judgeReviewResult(blockType, tdscBlockTranApp, tdscBidderApp);

                    if ("0".equals(returnReviewResult.substring(0, 1))) {
                        returnOpnn.append(returnReviewResult);
                        count++;
                    }
                    if ("1".equals(returnReviewResult.substring(0, 1))) {
                        yOrN = "1";
                        returnOpnn.append(yOrN);
                    }
                    return returnOpnn.toString();
                }

                // 联合竞买
                if ("2".equals(tdscBidderApp.getBidderType())) {
                    int countNO = 0; // 计数
                    List bidderPersonAppList = (List) tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
                    if (bidderPersonAppList != null && bidderPersonAppList.size() > 0) {
                        // 构造临时的 StringBuffer
                        StringBuffer tempRreturnOpnn = new StringBuffer();
                        for (int i = 0; i < bidderPersonAppList.size(); i++) {
                            // TdscBidderPersonApp tdscBidderPersonApp =(TdscBidderPersonApp)bidderPersonAppList.get(i);
                            // 判断
                            String returnReviewResult = judgeReviewResult(blockType, tdscBlockTranApp, tdscBidderApp);
                            // 单个竞买人未达到入围条件 生成未入围原因
                            if ("0".equals(returnReviewResult.substring(0, 1))) {
                                countNO++;
                                tempRreturnOpnn.append(returnReviewResult.substring(1));
                            }
                        }
                        // 入围
                        if (countNO == 0) {
                            yOrN = "1";
                            returnOpnn.append(yOrN);
                        } else {
                            returnOpnn.append(yOrN).append(tempRreturnOpnn);
                        }
                        return returnOpnn.toString();
                    }
                }
            }
        }
        return yOrN;
    }

    /**
     * 判断机审结果
     * 
     * @param tdscBlockTranApp
     * @param tdscBidderApp
     * @return
     */
    public String returnReviewResult(String blockType, TdscBlockTranApp tdscBlockTranApp, TdscBidderApp tdscBidderApp) {

        StringBuffer returnOpnn = new StringBuffer();
        String yOrN = "0";
        int count = 0;
        if (tdscBlockTranApp != null && tdscBidderApp != null) {
            String returnReviewResult = judgeReviewResult(blockType, tdscBlockTranApp, tdscBidderApp);
            if ("0".equals(returnReviewResult.substring(0, 1))) {
                returnOpnn.append(returnReviewResult);
                count++;
            }
            if ("1".equals(returnReviewResult.substring(0, 1))) {
                yOrN = "1";
                returnOpnn.append(yOrN);
            }
            return returnOpnn.toString();
        }
        return null;
    }

    /**
     * 根据条件判断该竞买申请是否入围
     * 
     * @param blockType
     * @param tdscBlockTranApp
     * @param tdscBidderPersonApp
     * @return
     */
    public String judgeReviewResult(String blockType, TdscBlockTranApp tdscBlockTranApp, TdscBidderApp tdscBidderApp) {
        StringBuffer returnOpnn = new StringBuffer();
        String yOrN = "0";// 不可以入围
        int count = 0; // 计数
        if (tdscBidderApp != null) {
            if (tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION)
                    || tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_SHORTAGE)
                    || tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_NULL)) {
                returnOpnn.append("到账金额不足或者还未到账,");
                count++;
            }
            if (tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_OVERDUE)) {
                returnOpnn.append("逾期到账,");
                count++;
            }
            TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
            if ("1".equals(tdscBidderApp.getBidderType())) {
                tdscBidderPersonApp = tdscBidderPersonAppDao.getOneBidderByBidderId(tdscBidderApp.getBidderId());
                String returnReviewResult = (String) judgeBidderPerson(blockType, tdscBlockTranApp, tdscBidderPersonApp);
                if ("0".equals(returnReviewResult.substring(0, 1))) {
                    returnOpnn.append(returnReviewResult);
                    count++;
                }
                /*
                 * if("1".equals(returnReviewResult.substring(0,1))){ //yOrN="1"; returnOpnn.append(yOrN); }
                 */
                // return returnOpnn.toString();
            } else if ("2".equals(tdscBidderApp.getBidderType())) {
                List bidderPersonList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
                if (bidderPersonList != null && bidderPersonList.size() > 0) {
                    for (int i = 0; i < bidderPersonList.size(); i++) {
                        tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
                        String returnReviewResult = (String) judgeBidderPerson(blockType, tdscBlockTranApp, tdscBidderPersonApp);
                        if ("0".equals(returnReviewResult.substring(0, 1))) {
                            returnOpnn.append(returnReviewResult.substring(1));
                            count++;
                        }
                    }

                }
            }
        }

        if (count == 0) {
            yOrN = "1";
        }
        yOrN += returnOpnn.toString();
        return yOrN;
    }

    /**
     * 根据条件判断单个竞买人是否符合入围要求
     * 
     * @param blockType
     * @param tdscBlockTranApp
     * @param tdscBidderPersonApp
     * @return
     */
    public String judgeBidderPerson(String blockType, TdscBlockTranApp tdscBlockTranApp, TdscBidderPersonApp tdscBidderPersonApp) {
        StringBuffer opnn = new StringBuffer();
        String yOrN = "0";// 不可以入围
        int count = 0; // 计数

        // 资信等级
        if (tdscBidderPersonApp.getJsQyZxdj() != null && tdscBlockTranApp.getJsQyZxdj() != null) {
            if (Integer.parseInt(tdscBidderPersonApp.getJsQyZxdj()) < Integer.parseInt(tdscBlockTranApp.getJsQyZxdj())) {
                opnn.append("资信等级,");
                count++;
            }
        }
        // 上月净资产（同期个人银行存款数额） 工业和经营性用地
        if (tdscBidderPersonApp.getJsSyjzc() != null && tdscBlockTranApp.getJsSyjzc() != null) {
            if (tdscBidderPersonApp.getJsSyjzc().compareTo(tdscBlockTranApp.getJsSyjzc()) < 0) {
                opnn.append("上月净资产（同期个人银行存款数额）,");
                count++;
            }
        }
        // 近三年主营业务利润
        if (tdscBidderPersonApp.getJsJ3nZyywll() != null && tdscBlockTranApp.getJsJ3nZyywll() != null) {
            if (tdscBidderPersonApp.getJsJ3nZyywll().compareTo(tdscBlockTranApp.getJsJ3nZyywll()) < 0) {
                opnn.append("近三年主营业务利润,");
                count++;
            }
        }
        // 连续从业年数
        if (tdscBidderPersonApp.getJsFdcCyns() != null && tdscBlockTranApp.getJsFdcCyns() != null) {
            if (tdscBidderPersonApp.getJsFdcCyns().compareTo(tdscBlockTranApp.getJsFdcCyns()) < 0) {
                opnn.append("连续从业年数,");
                count++;
            }
        }
        // 产业类型 待定！！！

        // 房地产开发资质等级(境外竞买人没有房地产开发资质要求，对境内竞买人进行判断)
        if (tdscBidderPersonApp.getBidderProperty() != null) {
            // 境内竞买人的DIC_CODE大于20
            if (Integer.parseInt(tdscBidderPersonApp.getBidderProperty()) > 20) {
                if (tdscBidderPersonApp.getJsFdcZzdj() != null && tdscBlockTranApp.getJsFdcZzdj() != null) {
                    if (Integer.parseInt(tdscBlockTranApp.getJsFdcZzdj()) < Integer.parseInt(tdscBidderPersonApp.getJsFdcZzdj())) {
                        opnn.append("房地产开发资质等级,");
                        count++;
                    }
                }
            }
        }

        /** 经营性用地的判断 */
        if ("102".equals(blockType)) {
            // 近五年开发楼盘个数
            if (tdscBidderPersonApp.getJsJ5nKfLpgs() != null && tdscBlockTranApp.getJsJ5nKfLpgs() != null) {
                if (tdscBidderPersonApp.getJsJ5nKfLpgs().compareTo(tdscBlockTranApp.getJsJ5nKfLpgs()) < 0) {
                    opnn.append("近五年开发楼盘个数,");
                    count++;
                }
            }
            // 近五年开发土地面积
            if (tdscBidderPersonApp.getJsJ5nKfTdmj() != null && tdscBlockTranApp.getJsJ5nKfTdmj() != null) {
                if (tdscBidderPersonApp.getJsJ5nKfTdmj().compareTo(tdscBlockTranApp.getJsJ5nKfTdmj()) < 0) {
                    opnn.append("近五年开发土地面积,");
                    count++;
                }
            }
        }
        // 得出结果
        if (count > 0) {
            // 未入围
            StringBuffer returnOpnn = new StringBuffer();
            returnOpnn.append(yOrN).append(tdscBidderPersonApp.getBidderName()).append(",").append(opnn);
            return returnOpnn.toString();
        }
        if (count == 0) {
            StringBuffer returnOpnn = new StringBuffer();
            yOrN = "1";
            returnOpnn.append(yOrN);
            return returnOpnn.toString();
        }
        return null;
    }

    /**
     * 将门禁卡编号发送给门禁系统，其中使用到了在进度安排表阶段得到的会议编号
     * 
     * @param transferMode
     *            交易类型
     * @param cardId
     *            门禁卡编号
     * @param tdscBlockPlan
     *            进度安排表
     */
    //private int excuteTimes = 0;

    private void sendCard2Guard(String transferMode, int cardId, HashMap meetingMap, int cardType) {
        List cardList = new ArrayList();

        // 招标
        if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
            if (meetingMap.get("TENDER_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // 投标会议编号
                tenderCard1.setMeetingId(((Integer) meetingMap.get("TENDER_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }

            if (meetingMap.get("OPENING_MEETING_NO") != null) {
                CardData tenderCard2 = new CardData();
                tenderCard2.setCardId(cardId);
                // 开标/定标会会议编号
                tenderCard2.setMeetingId(((Integer) meetingMap.get("OPENING_MEETING_NO")).intValue());
                tenderCard2.setCardType(cardType);
                cardList.add(tenderCard2);
            }

            if (meetingMap.get("BID_EVAL_MEETING_NO") != null) {
                CardData tenderCard3 = new CardData();
                tenderCard3.setCardId(cardId);
                // 评标会议编号
                tenderCard3.setMeetingId(((Integer) meetingMap.get("BID_EVAL_MEETING_NO")).intValue());
                tenderCard3.setCardType(cardType);
                cardList.add(tenderCard3);
            }
        }
        // 拍卖
        if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
            if (meetingMap.get("AUCTION_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // 拍卖会会议编号
                tenderCard1.setMeetingId(((Integer) meetingMap.get("AUCTION_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }
        }
        // 挂牌
        if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
            if (meetingMap.get("LIST_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // 挂牌会议编号
                tenderCard1.setMeetingId(((Integer) meetingMap.get("LIST_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }

            if (meetingMap.get("SCE_BID_MEETING_NO") != null) {
                CardData tenderCard2 = new CardData();
                tenderCard2.setCardId(cardId);
                // 挂牌现场竞价会议编号
                tenderCard2.setMeetingId(((Integer) meetingMap.get("SCE_BID_MEETING_NO")).intValue());
                tenderCard2.setCardType(cardType);
                cardList.add(tenderCard2);
            }
        }

        // 将门禁卡编号发送给门禁系统
        if (cardList.size() > 0) {
            //logger.info("===开始调用门禁系统接口[" + excuteTimes + "]-sendCard===");
            //long start = System.currentTimeMillis();
            MessageData message = GuardService.getGuardInstance().sendCard(cardList);
            //long end = System.currentTimeMillis();
            //logger.info("调用门禁系统共花费：" + (end - start) + "毫秒");
            //logger.info("===结束调用门禁系统接口[" + excuteTimes + "]-sendCard===");

            if (message.getIsSuccess()) {
                List returnCard = message.getReturnInfoList();
                if (returnCard != null && returnCard.size() > 0) {
                    for (int i = 0; i < returnCard.size(); i++) {
                        CardData tempCard = (CardData) returnCard.get(i);
                        if (!tempCard.getIsSuccess()) {
                            int errorCode = tempCard.getErrorCode();
                            if (errorCode == GuardConstants.ERROR_CARD_CARDNO_ERROR) {
                                // 卡号非法
                                logger.info("[门禁系统调用报错][卡号非法]:" + tempCard.getCardId());
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // 会议编号不存在
                                logger.info("[门禁系统调用报错][会议编号不存在]:" + tempCard.getMeetingId());
                            } else {
                                // 未知错误
                                logger.info("[门禁系统调用报错][未知错误][errorCode]:" + errorCode);
                            }
                        }
                    }
                }
            }else{
            	logger.info("向门禁系统发送信息失败:["+ message.getErrorMsg() +"]===未发送的交易卡号为["+ cardId +"]===");
            }
            // TODO 发送不成功，可以考虑重复发送3次
            // else {
            // logger.info("[门禁系统调用报错][" + excuteTimes + "]：" + message.getErrorMsg());
            // // 发送不成功，可以考虑重复发送3次
            // if (excuteTimes < 3) {
            // sendCard2Guard(transferMode, cardId, meetingMap, cardType);
            // excuteTimes++;
            // }
            // }
        } else {
            logger.info("[门禁系统调用报错][交易类型\"" + transferMode + "\"不存在]或[会议编号不存在]");
        }
    }

    /**
     * 发送门禁卡信息
     * 
     * @param tdscBlockTranApp
     * @param tdscBlockPlan
     * @param tdscBidderApp
     * @return
     */
    public String sendGuardMessage(TdscBlockTranApp tdscBlockTranApp, TdscBlockPlanTable tdscBlockPlan, TdscBidderApp tdscBidderApp) {
        logger.info("===开始调用门禁系统接口-sendGuardMessage===");
        long start = System.currentTimeMillis();

        // 发卡
        GuardService guardService = GuardService.getGuardInstance();
        List meetingList = new ArrayList();
        String retString = null;
        // if (tdscBlockTranApp != null && GlobalConstants.DIC_TRANSFER_TENDER.equals(tdscBlockTranApp.getTransferMode())) {
        // 招标的处理
        if (tdscBlockPlan != null && tdscBlockPlan.getTenderMeetingNo() != null) {
            CardData tenderCard = new CardData();
            tenderCard.setCardId(Integer.parseInt(tdscBidderApp.getYktBh()));
            tenderCard.setMeetingId(tdscBlockPlan.getTenderMeetingNo().intValue());
            tenderCard.setCardType(GuardConstants.CARD_TYPE_NORMAL);
            List cardList = new ArrayList();
            cardList.add(tenderCard);
            MessageData message = guardService.sendCard(cardList);
            if (message.getIsSuccess()) {
                List returnCard = message.getReturnInfoList();
                if (returnCard != null && returnCard.size() > 0) {
                    for (int i = 0; i < returnCard.size(); i++) {
                        CardData tempCard = (CardData) returnCard.get(i);
                        if (!tempCard.getIsSuccess()) {
                            int errorCode = tempCard.getErrorCode();
                            if (errorCode == GuardConstants.ERROR_CARD_CARDNO_ERROR) {
                                // 卡号非法
                                retString = "卡号非法";
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // 会议编号不存在
                                retString = "会议编号不存在";
                            } else {
                                // 未知错误
                                retString = "未知错误";
                            }
                        }
                    }
                }
            } else {
                logger.error("门禁系统调用报错：" + message.getErrorMsg());
                // 网络传输异常
                retString = "===调用门禁系统报错===";
            }
        } else {
            logger.error("暂无门禁会议编号！");
        }

        if (tdscBlockPlan != null && tdscBlockPlan.getOpeningMeetingNo() != null) {
            CardData tenderCard = new CardData();
            tenderCard.setCardId(Integer.parseInt(tdscBidderApp.getYktBh()));
            tenderCard.setMeetingId(tdscBlockPlan.getTenderMeetingNo().intValue());
            tenderCard.setCardType(GuardConstants.CARD_TYPE_NORMAL);
            List cardList = new ArrayList();
            cardList.add(tenderCard);
            MessageData message = guardService.sendCard(cardList);
            if (message.getIsSuccess()) {
                List returnCard = message.getReturnInfoList();
                if (returnCard != null && returnCard.size() > 0) {
                    for (int i = 0; i < returnCard.size(); i++) {
                        CardData tempCard = (CardData) returnCard.get(i);
                        if (!tempCard.getIsSuccess()) {
                            int errorCode = tempCard.getErrorCode();
                            if (errorCode == GuardConstants.ERROR_CARD_CARDNO_ERROR) {
                                // 卡号非法
                                retString = "卡号非法";
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // 会议编号不存在
                                retString = "会议编号不存在";
                            } else {
                                // 未知错误
                                retString = "未知错误";
                            }
                        }
                    }
                }
            } else {
                logger.error("门禁系统调用报错：" + message.getErrorMsg());
                // 网络传输异常
                retString = "===调用门禁系统报错===";
            }
        } else {
            logger.error("暂无门禁会议编号！");
        }

        // } else {
        // retString = "暂无地块信息";
        // }

        long end = System.currentTimeMillis();
        logger.info("调用门禁系统共花费：" + (end - start) + "毫秒");
        logger.info("===结束调用门禁系统接口-sendGuardMessage===");

        return retString;
    }

}
