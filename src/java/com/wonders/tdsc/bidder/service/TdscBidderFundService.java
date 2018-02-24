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
     * �����������һҳ���û��б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList����
     */
    public PageList findPageList(TdscBidderCondition condition) {
        return tdscBlockTranAppDao.findPageList(condition);
    }

    public PageList findBidderPageList(TdscBaseQueryCondition condition) {
        return tdscBlockTranAppDao.findBidderPageList(condition);
    }

    /**
     * ����������ѯ
     * 
     * @param condition
     * @return
     */
    public List findReturnSetByCondition(TdscBidderCondition condition) {
        // ��bidderPersonApp ��ѯ
        if (condition.getBidderName() != null || condition.getBidderZh() != null) {
            if (condition.getAcceptNo() == null && condition.getBidderType() == null) {
                List tempList = (List) findPageFundList(condition);

            }
        }
        // ��bidderApp ��ѯ
        if (condition.getAcceptNo() != null || condition.getBidderType() != null) {
            if (condition.getBidderName() == null && condition.getBidderZh() == null) {
                // PageList tempList=(PageList)tdscBidderAppDao.

            }
        }
        return null;
    }

    /**
     * ����condition��appId ��ѯ�� TDSC_BIDDER_APP ������ص���Ϣ
     * 
     * @param condition
     * @return
     */
    public List findPageFundList(TdscBidderCondition condition) {
        /* ���TdscBidderApp�б�. */
        List bidderAppList = (List) tdscBidderAppDao.findBidderIdsByAppId(condition.getAppId());

        if (bidderAppList != null && bidderAppList.size() > 0) {
            /* ���췵�ص� List */
            List returnList = new ArrayList();
            /* ѭ�� bidderAppList����ö��TdscBidderApp */
            for (int i = 0; i < bidderAppList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();

                condition.setBidderId(bidderId);

                List tdscBidPerAppList = (List) tdscBidderPersonAppDao.findByBiDPerDzqk(condition);
                if (tdscBidPerAppList != null && tdscBidPerAppList.size() > 0) {
                    // ѭ�� tdscBidPerAppList����ö��TdscBidderPersonApp.*/
                    for (int j = 0; j < tdscBidPerAppList.size(); j++) {
                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerAppList.get(j);
                        tdscBidderPersonApp.setBidderType(tdscBidderApp.getBidderType());
                        tdscBidderPersonApp.setBzjJnfs(tdscBidderApp.getBzjJnfs());
                        tdscBidderPersonApp.setAcceptNo(tdscBidderApp.getAcceptNo());
                        // ��ÿ�� TdscBidderPersonApp��ӵ� returnList��.*/
                        // ����������ֱ�Ӽ���list
                        if ("1".equals(tdscBidderApp.getBidderType())) {
                            returnList.add(tdscBidderPersonApp);
                        }
                        // ���˵� ���Ͼ�����ǣͷ�˽��ɱ�֤���ɷ�ʽ�µ� ��ǣͷ��
                        if ("2".equals(tdscBidderApp.getBidderType())) {
                            // ������Ͷ�ʵ����Ͼ��� �����˶�����list
                            if ("0".equals(tdscBidderApp.getBzjJnfs())) {
                                returnList.add(tdscBidderPersonApp);
                            }
                            // ǣͷ�˽��ɱ�֤��ʱ ֻ��ǣͷ��
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
     * ��ѯ������� ��Ϊ������ʷ���������б�
     * 
     * @param condition
     * @return
     */
    public List findChangeFundList(TdscBidderCondition condition) {
        /* ���TdscBidderApp�б�. */
        List bidderAppList = (List) tdscBidderAppDao.findBidderIdsByAppId(condition.getAppId());

        if (bidderAppList != null && bidderAppList.size() > 0) {
            /* ���췵�ص� List */
            List returnList = new ArrayList();
            /* ѭ�� bidderAppList����ö��TdscBidderApp */
            for (int i = 0; i < bidderAppList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();
                condition.setBidderId(bidderId);
                List tdscBidPerAppList = (List) tdscBidderPersonAppDao.findChangePerDzqk(condition);
                if (tdscBidPerAppList != null && tdscBidPerAppList.size() > 0) {
                    // ѭ�� tdscBidPerAppList����ö��TdscBidderPersonApp.*/
                    for (int j = 0; j < tdscBidPerAppList.size(); j++) {
                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerAppList.get(j);
                        // ��tdscBidderApp�ı�֤���ɷ�ʽ�����嵽������;���ʽ��ֵ�� tdscBidderPersonApp
                        tdscBidderPersonApp.setBzjztDzqk(tdscBidderApp.getBzjztDzqk());
                        tdscBidderPersonApp.setBidderType(tdscBidderApp.getBidderType());
                        tdscBidderPersonApp.setBzjJnfs(tdscBidderApp.getBzjJnfs());
                        tdscBidderPersonApp.setAcceptNo(tdscBidderApp.getAcceptNo());
                        // ��ÿ�� TdscBidderPersonApp��ӵ� returnList��.*/
                        returnList.add(tdscBidderPersonApp);
                    }
                }

            }
            return returnList;
        }
        return null;
    }

    /**
     * ����������ѯ
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
     * ͨ��appId ��þ�������Ϣ�б�
     * 
     * @param appId
     * @return
     */
    public List queryBidPreByAppId(String appId) {
        /* ͨ��appId ��øÿ�ؿ����Ĺҵ�����������Ϣ. */
        List bidPerByAppIdList = (List) tdscBidderAppDao.findBidderIdsByAppId(appId);

        if (bidPerByAppIdList != null && bidPerByAppIdList.size() > 0) {
            List retList = new ArrayList();
            List bidAllList = new ArrayList();
            for (int i = 0; i < bidPerByAppIdList.size(); i++) {
                TdscBidderApp tdscBidderApp = (TdscBidderApp) bidPerByAppIdList.get(i);
                String bidderId = (String) tdscBidderApp.getBidderId();
                if (bidderId != null) {
                    /* ͨ��bidderId ��øÿ������������������Ϣ. */
                    List bidPerDzqkList = (List) tdscBidderPersonAppDao.findTdscBidderPersonDzqkList(bidderId);
                    if (bidPerDzqkList != null && bidPerDzqkList.size() > 0) {
                        for (int j = 0; j < bidPerDzqkList.size(); j++) {
                            TdscBidderPersonApp personApp = (TdscBidderPersonApp) bidPerDzqkList.get(j);
                            /* ����������Ϣ��ӵ� bidAllList��. */
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
     * �޸ľ����˵ĵ������
     * 
     * @param personAppList
     */
    public void modifyPersonDzqk(List personAppList) {
        if (personAppList != null && personAppList.size() > 0) {
            // ���ݴ�������BidderPersonId ���ԭTdscBidderPersonApp������(Ϊ�˲�� TdscBlockTranApp �ĵ��˽���ʱ��)
            TdscBidderPersonApp tmpPersonApp = (TdscBidderPersonApp) personAppList.get(0);
            TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(tmpPersonApp.getBidderPersonId());
            // ���� bidderId ���������ĵؿ����Ϣ
            String bidderId = (String) personApp.getBidderId();
            if (bidderId != null) {
                /* ���� TdscBidderAppʵ����� ���ж��Ƿ�Ϊ��. */
                TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
                //TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.findPlanTableInfo(tdscBidderApp.getAppId());
                String appId = (String) tdscBidderApp.getAppId();
                if (appId != null) {
                    /* ���� TdscBlockTranAppʵ����� ���ж��Ƿ�Ϊ��. */
                    TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
                    // ��ѯ��������
                    TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.findBlockInfo(tdscBlockTranApp.getBlockId());
                    String blockType = (String) tdscBlockInfo.getBlockType();
                    String blockNpticeNo = (String) tdscBlockTranApp.getBlockNoticeNo();
                    Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); // ��֤�����ʱ��
                    BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); // ��֤����
                    if (tdscBlockTranApp != null) {
                        // �޸�ҳ���ϴ�������ÿ�������˵ĵ������
                        for (int i = 0; i < personAppList.size(); i++) {
                            TdscBidderPersonApp persAppList = (TdscBidderPersonApp) personAppList.get(i);
                            TdscBidderPersonApp modpersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(persAppList.getBidderPersonId());
                            /* ��MarginEndDateΪ�գ���ֻ���뾺���˵ĵ���ʱ��ͽ��. */
                            if (persAppList.getBzjDzse() != null && persAppList.getBzjDzsj() != null) {
                                modpersonApp.setBzjDzse(persAppList.getBzjDzse());
                                modpersonApp.setBzjDzsj(persAppList.getBzjDzsj());
                                // Date bzjDzsj = (Date) persAppList.getBzjDzsj(); //��֤�����ʱ��
                                // BigDecimal bzjDzse = (BigDecimal) persAppList.getBzjDzse(); //ʵ�ʽ��ɵ�����

                                // String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);
                                // modpersonApp.setBzjDzqk(dzqk);
                                tdscBidderPersonAppDao.update(modpersonApp);
                            }
                        }
                        // �޸ľ�������嵽�����
                        int certNoCount = 0;
                        for (int i = 0; i < personAppList.size(); i++) {
                            TdscBidderPersonApp temppersonApp = (TdscBidderPersonApp) personAppList.get(i);
                            // ������ݿ��е�ʵ�����
                            TdscBidderPersonApp oneBidPer = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(temppersonApp.getBidderPersonId());
                            String bidId = (String) oneBidPer.getBidderId();
                            TdscBidderApp oneBidder = (TdscBidderApp) tdscBidderAppDao.findOneBidderByBidderId(bidId);
                            // �жϾ�������
                            // 1.��������-- ����ĵ������ = �����˵ĵ������
                            if ("1".equals(oneBidder.getBidderType())) {
                                if (tdscBlockTranApp.getMarginEndDate() != null && tdscBlockTranApp.getMarginAmount() != null) {
                                    Date bzjDzsj = (Date) oneBidPer.getBzjDzsj(); // ��֤�����ʱ��
                                    BigDecimal bzjDzse = (BigDecimal) oneBidPer.getBzjDzse(); // ʵ�ʽ��ɵ�����
                                    // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //��֤�����ʱ��
                                    // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //��֤����
                                    String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);

                                    oneBidder.setBzjztDzqk(dzqk); // ���þ�������ĵ������
                                    oneBidPer.setBzjDzqk(dzqk); // ���þ����˵ĵ������
                                }

                            }
                            // 2.���Ͼ���--(���������ɱ�֤��) ����ĵ���������ȼ� ��1.δ����;2.δ����;3.���ڵ���;4.���ڵ���
                            if ("2".equals(oneBidder.getBidderType()) && "0".equals(oneBidder.getBzjJnfs())) {
                                // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //��֤����
                                // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //��֤�����ʱ��
                                List tempPersonList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidId);
                                // ������Ͼ����˵����ʽ���ܺ�,����ʱ��
                                // BigDecimal bzjDzseZh = new BigDecimal(0);
                                // int bzjDzseZhRet = 0;
                                BigDecimal bzjDzseZhRetText = null;
                                Date bzjDzsj = new Date();
                                if (tempPersonList != null && tempPersonList.size() > 0) {
                                    for (int j = 0; j < tempPersonList.size(); j++) {
                                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tempPersonList.get(j);
                                        // �ʽ���ܺ�
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
                                            // ������Ͼ������е��������һ���˵ĵ���ʱ��
                                            if (j == 0) {
                                                bzjDzsj = tdscBidderPersonApp.getBzjDzsj(); // ����ʱ��
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
                                oneBidPer.setBzjDzqk(dzqk); // ���þ����˵ĵ������
                                oneBidder.setBzjztDzqk(dzqk); // ���þ�������ĵ������
                                // 

                            }
                            // 3.���Ͼ���--(ǣͷ�˽��ɱ�֤��)
                            if ("2".equals(oneBidder.getBidderType()) && "1".equals(oneBidder.getBzjJnfs())) {
                                List tdscBidPerList = (List) tdscBidderPersonAppDao.findTdscBidderPersonDzqkList(bidId);
                                if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
                                    // ���þ�������嵽�����
                                    for (int k = 0; k < tdscBidPerList.size(); k++) {
                                        TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(k);
                                        // �ж��Ƿ���ǣͷ��
                                        if ("1".equals(tdscBidderPersonApp.getIsHead())) {
                                            Date bzjDzsj = (Date) oneBidPer.getBzjDzsj(); // ��֤�����ʱ��
                                            BigDecimal bzjDzse = (BigDecimal) oneBidPer.getBzjDzse(); // ʵ�ʽ��ɵ�����
                                            // Date marginEndDate = (Date) tdscBlockTranApp.getMarginEndDate(); //��֤�����ʱ��
                                            // BigDecimal marginAmount = (BigDecimal) tdscBlockTranApp.getMarginAmount(); //��֤����
                                            String dzqk = (String) judgeDzqk(bzjDzsj, marginEndDate, bzjDzse, marginAmount);
                                            oneBidPer.setBzjDzqk(dzqk); // ���þ����˵ĵ������
                                            oneBidder.setBzjztDzqk(dzqk); // ���þ�������ĵ������

                                        }
                                    }
                                }
                            }
                            // ����
                            String returnReviewOpnn = returnReviewResult(blockType, tdscBlockTranApp, oneBidder);
                            // String guardMessage = (String) sendGuardMessage(tdscBlockTranApp, tdscBlockPlanTable, tdscBidderApp);
                            // System.out.println("guardMessage====>" + guardMessage);

                            // ����ϸ�
                            if ("1".equals(returnReviewOpnn.substring(0, 1))) {
                                // ���ԭ�����ʸ�֤�����򲻱䣬���û��������
                                if (oneBidder.getCertNo() == null) {
                                    certNoCount++;
                                    String certNo = getCertNo(blockNpticeNo);
                                    oneBidder.setCertNo(certNo);
                                }
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
                                oneBidder.setReviewOpnn("");
                            } else {
                                // ���ϸ�
                                oneBidder.setCertNo(""); // ���ϸ�ʱ�����ԭ�����ʸ�֤����
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
     * �˹��޸ľ����˵���Χ���
     * 
     * @param personAppList
     */
    public void modifyRwqkByPerson(String appId, String bidId, String subtype) {
          if (bidId != null&&subtype!=null&&appId != null) {
          
        	  TdscBidderApp oneBidder = (TdscBidderApp) tdscBidderAppDao.findOneBidderByBidderId(bidId);
        	  
        	  /* ���� TdscBlockTranAppʵ����� ���ж��Ƿ�Ϊ��. */
              TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
                   String blockNpticeNo = (String) tdscBlockTranApp.getBlockNoticeNo();
                           
              
              // ����ϸ�  subtype=1
                            if ("1".equals(subtype)) {
                                // ���ԭ�����ʸ�֤�����򲻱䣬���û��������
                                if (oneBidder.getCertNo() == null) {
                                    String certNo = getCertNo(blockNpticeNo);
                                    oneBidder.setCertNo(certNo);
                                }
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
                                oneBidder.setReviewOpnn("");
                            } else {
                                // ���ϸ�  subtype=0
                                oneBidder.setCertNo(""); // ���ϸ�ʱ�����ԭ�����ʸ�֤����
                                oneBidder.setReviewResult(GlobalConstants.DIC_ID_BREVIEW_RESULT_NO);
                                oneBidder.setReviewOpnn("");
                            }
                            tdscBidderAppDao.update(oneBidder);
                            //tdscBidderPersonAppDao.update(oneBidPer);
                        }
                    }


    /**
     * �����ʸ�֤����blockNpticeNo+3λ��ˮ��
     * 
     * @param appId
     * @return
     */
    public String getCertNo(String blockNpticeNo, int certNoCount) {
        // Ԥ����һ���ʸ�֤����
        if (blockNpticeNo == null) {
            blockNpticeNo = "000000000";
        }

        List blockNpticeNoList = (List) tdscBidderAppDao.getCretNo(blockNpticeNo);
        String tempCertNo = "000" + (blockNpticeNoList.size() + certNoCount);
        String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
        // �жϸ��ʸ�֤�����Ƿ��Ѿ�����

        List blNptNoList = new ArrayList();
        String queryCertNo = certNo;
        // ѭ���ж�
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
     * �ж� bzjDzsj��֤����ʱ���� marginEndDate��֤�����ʱ����Ⱥ�
     * 
     * @param bzjDzsj
     *            ��֤����ʱ��
     * @param marginEndDate
     *            ��֤�����ʱ��
     * @return
     */
    public boolean timeCompare(Date bzjDzsj, Date marginEndDate) {
        /* timeCompare false ��ζ ����ʱ���Ҫ����ʱ����. */
        int i = bzjDzsj.compareTo(marginEndDate);
        if (i > 0) {
            return false;
        }
        return true;
    }

    /**
     * �ж� bzjDzse������ʵ�ʽ��ɱ�֤���� marginAmountӦ���ɵı�֤��Ĵ�С
     * 
     * @param bzjDzse
     *            ������ʵ�ʽ��ɱ�֤��
     * @param marginAmount
     *            Ӧ���ɵı�֤��
     * @return
     */
    public boolean bzjCompare(BigDecimal bzjDzse, BigDecimal marginAmount) {
        int j = bzjDzse.compareTo(marginAmount);
        /* bzjCompare false ��ζ ����. */
        if (j < 0) {
            return false;
        }
        return true;
    }

    
    /**
     * ����blockNpticeNo�����ʸ�֤����
     * @param appId
     * @return
     */
    public String getCertNo(String blockNpticeNo) {
    	//���һ��Ԥ�����ʸ�֤����
    	String certNo=creatCertNo(blockNpticeNo);
        //����Ƿ�Ψһ
    	while( checkCertNo(certNo)==false){
    		certNo=creatCertNo(blockNpticeNo);
    	}
    	
    	return certNo;
    }
    
    /**
     * �ж�ĳ���ʸ�֤�����Ƿ��Ѿ�����
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
     * Ԥ�����ʸ�֤����
     * @param blockNpticeNo
     * @return
     */
    public String creatCertNo(String blockNpticeNo){
    	//Ԥ���õؿ鹫���
        if (blockNpticeNo == null) {
            blockNpticeNo = "000000000";
        }
        //��ˮ��
    	String tempCertNo = "000" + idSpringManager.getIncrementId("CertNo"+blockNpticeNo);
    	//Ԥ�����ʸ�֤����
    	String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
    	return certNo;
    }
    
    /**
     * �ж� �������
     * 
     * @param bzjDzsj
     *            ��֤����ʱ��
     * @param marginEndDate
     *            ��֤�����ʱ��
     * @param bzjDzse
     *            ������ʵ�ʽ��ɱ�֤��
     * @param marginAmount
     *            Ӧ���ɵı�֤��
     * @return
     */
    public String judgeDzqk(Date bzjDzsj, Date marginEndDate, BigDecimal bzjDzse, BigDecimal marginAmount) {
        String dzqk = null;
        boolean timeCompare = timeCompare(bzjDzsj, marginEndDate);
        boolean bzjCompare = bzjCompare(bzjDzse, marginAmount);
        /* δ���� ���Ϊ0. */
        if (bzjDzse == null || bzjDzse.compareTo(new BigDecimal(0)) == 0) {
            dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_NULL;
        } else {
            /* 1.δ���� ����ʱ���Ҫ����ʱ�� ������� ���� ���㵫��Ϊ 0 ����ע����ֻ��һ��״̬�����˺���Ը�Ϊ���ڵ��ˣ�. */
            if ((bzjDzse != null && bzjCompare == false && timeCompare == true) || (bzjCompare == false && timeCompare == false)) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_SHORTAGE;
            }
            /* 2.���ڵ��� ����ʱ���Ҫ����ʱ�� �� ���� �����. */
            if (bzjCompare == true && timeCompare == false) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_OVERDUE;
            }
            /* 3.���ڵ��� ����ʱ���Ҫ����ʱ�� �� ���� �����. */
            if (bzjCompare == true && timeCompare == true) {
                dzqk = GlobalConstants.DIC_ID_BIDDERDZQK_ONTIME;
            }
        }
        return dzqk;
    }

    /**
     * ����appId ��ѯ �ؿ�� ���Ƶ���Ϣ
     * 
     */
    public TdscBlockTranApp queryOneBlockTran(String appId) {
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
        return tdscBlockTranApp;
    }

    /**
     * ����appId ����õؿ�����о����˵Ļ�����
     * 
     * @param appId
     * @return
     */
    public List queryJsjg(String appId) {
        List jsjgList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);
        return jsjgList;
    }

    /**
     * ����������ѯ��list_jsjg.jsp ��ѯ��ť��
     * 
     * @param condition
     * @return
     */
    public List queryJsjg(TdscBidderCondition condition) {
        List jsjgList = (List) tdscBidderAppDao.findBidderAppListByCondition(condition);
        return jsjgList;
    }

    /**
     * ����appId,bidderId�жϻ�����
     * 
     * @param blockType
     * @param appId
     * @param bidderId
     * @return
     */
    public String returnReviewResult(String blockType, String appId, String bidderId) {
        // ����appId��ѯ�ؿ���Ϣ
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
        // ����������Ϣ������ʽ��
        TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
        StringBuffer returnOpnn = new StringBuffer();
        String yOrN = "0";
        if (tdscBlockTranApp != null && tdscBidderApp != null) {
            // ��øõؿ�� blockId
            String blockId = (String) tdscBlockTranApp.getBlockId();

            int count = 0;
            if (blockId != null) {
                // ��������
                if ("1".equals(tdscBidderApp.getBidderType())) {
                    // TdscBidderPersonApp tdscBidderPersonApp =(TdscBidderPersonApp)tdscBidderPersonAppDao.getBidderByBidderId(bidderId);
                    // �ж�
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

                // ���Ͼ���
                if ("2".equals(tdscBidderApp.getBidderType())) {
                    int countNO = 0; // ����
                    List bidderPersonAppList = (List) tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
                    if (bidderPersonAppList != null && bidderPersonAppList.size() > 0) {
                        // ������ʱ�� StringBuffer
                        StringBuffer tempRreturnOpnn = new StringBuffer();
                        for (int i = 0; i < bidderPersonAppList.size(); i++) {
                            // TdscBidderPersonApp tdscBidderPersonApp =(TdscBidderPersonApp)bidderPersonAppList.get(i);
                            // �ж�
                            String returnReviewResult = judgeReviewResult(blockType, tdscBlockTranApp, tdscBidderApp);
                            // ����������δ�ﵽ��Χ���� ����δ��Χԭ��
                            if ("0".equals(returnReviewResult.substring(0, 1))) {
                                countNO++;
                                tempRreturnOpnn.append(returnReviewResult.substring(1));
                            }
                        }
                        // ��Χ
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
     * �жϻ�����
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
     * ���������жϸþ��������Ƿ���Χ
     * 
     * @param blockType
     * @param tdscBlockTranApp
     * @param tdscBidderPersonApp
     * @return
     */
    public String judgeReviewResult(String blockType, TdscBlockTranApp tdscBlockTranApp, TdscBidderApp tdscBidderApp) {
        StringBuffer returnOpnn = new StringBuffer();
        String yOrN = "0";// ��������Χ
        int count = 0; // ����
        if (tdscBidderApp != null) {
            if (tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION)
                    || tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_SHORTAGE)
                    || tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_NULL)) {
                returnOpnn.append("���˽�����߻�δ����,");
                count++;
            }
            if (tdscBidderApp.getBzjztDzqk().equals(GlobalConstants.DIC_ID_BIDDERDZQK_OVERDUE)) {
                returnOpnn.append("���ڵ���,");
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
     * ���������жϵ����������Ƿ������ΧҪ��
     * 
     * @param blockType
     * @param tdscBlockTranApp
     * @param tdscBidderPersonApp
     * @return
     */
    public String judgeBidderPerson(String blockType, TdscBlockTranApp tdscBlockTranApp, TdscBidderPersonApp tdscBidderPersonApp) {
        StringBuffer opnn = new StringBuffer();
        String yOrN = "0";// ��������Χ
        int count = 0; // ����

        // ���ŵȼ�
        if (tdscBidderPersonApp.getJsQyZxdj() != null && tdscBlockTranApp.getJsQyZxdj() != null) {
            if (Integer.parseInt(tdscBidderPersonApp.getJsQyZxdj()) < Integer.parseInt(tdscBlockTranApp.getJsQyZxdj())) {
                opnn.append("���ŵȼ�,");
                count++;
            }
        }
        // ���¾��ʲ���ͬ�ڸ������д����� ��ҵ�;�Ӫ���õ�
        if (tdscBidderPersonApp.getJsSyjzc() != null && tdscBlockTranApp.getJsSyjzc() != null) {
            if (tdscBidderPersonApp.getJsSyjzc().compareTo(tdscBlockTranApp.getJsSyjzc()) < 0) {
                opnn.append("���¾��ʲ���ͬ�ڸ������д�����,");
                count++;
            }
        }
        // ��������Ӫҵ������
        if (tdscBidderPersonApp.getJsJ3nZyywll() != null && tdscBlockTranApp.getJsJ3nZyywll() != null) {
            if (tdscBidderPersonApp.getJsJ3nZyywll().compareTo(tdscBlockTranApp.getJsJ3nZyywll()) < 0) {
                opnn.append("��������Ӫҵ������,");
                count++;
            }
        }
        // ������ҵ����
        if (tdscBidderPersonApp.getJsFdcCyns() != null && tdscBlockTranApp.getJsFdcCyns() != null) {
            if (tdscBidderPersonApp.getJsFdcCyns().compareTo(tdscBlockTranApp.getJsFdcCyns()) < 0) {
                opnn.append("������ҵ����,");
                count++;
            }
        }
        // ��ҵ���� ����������

        // ���ز��������ʵȼ�(���⾺����û�з��ز���������Ҫ�󣬶Ծ��ھ����˽����ж�)
        if (tdscBidderPersonApp.getBidderProperty() != null) {
            // ���ھ����˵�DIC_CODE����20
            if (Integer.parseInt(tdscBidderPersonApp.getBidderProperty()) > 20) {
                if (tdscBidderPersonApp.getJsFdcZzdj() != null && tdscBlockTranApp.getJsFdcZzdj() != null) {
                    if (Integer.parseInt(tdscBlockTranApp.getJsFdcZzdj()) < Integer.parseInt(tdscBidderPersonApp.getJsFdcZzdj())) {
                        opnn.append("���ز��������ʵȼ�,");
                        count++;
                    }
                }
            }
        }

        /** ��Ӫ���õص��ж� */
        if ("102".equals(blockType)) {
            // �����꿪��¥�̸���
            if (tdscBidderPersonApp.getJsJ5nKfLpgs() != null && tdscBlockTranApp.getJsJ5nKfLpgs() != null) {
                if (tdscBidderPersonApp.getJsJ5nKfLpgs().compareTo(tdscBlockTranApp.getJsJ5nKfLpgs()) < 0) {
                    opnn.append("�����꿪��¥�̸���,");
                    count++;
                }
            }
            // �����꿪���������
            if (tdscBidderPersonApp.getJsJ5nKfTdmj() != null && tdscBlockTranApp.getJsJ5nKfTdmj() != null) {
                if (tdscBidderPersonApp.getJsJ5nKfTdmj().compareTo(tdscBlockTranApp.getJsJ5nKfTdmj()) < 0) {
                    opnn.append("�����꿪���������,");
                    count++;
                }
            }
        }
        // �ó����
        if (count > 0) {
            // δ��Χ
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
     * ���Ž�����ŷ��͸��Ž�ϵͳ������ʹ�õ����ڽ��Ȱ��ű�׶εõ��Ļ�����
     * 
     * @param transferMode
     *            ��������
     * @param cardId
     *            �Ž������
     * @param tdscBlockPlan
     *            ���Ȱ��ű�
     */
    //private int excuteTimes = 0;

    private void sendCard2Guard(String transferMode, int cardId, HashMap meetingMap, int cardType) {
        List cardList = new ArrayList();

        // �б�
        if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
            if (meetingMap.get("TENDER_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // Ͷ�������
                tenderCard1.setMeetingId(((Integer) meetingMap.get("TENDER_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }

            if (meetingMap.get("OPENING_MEETING_NO") != null) {
                CardData tenderCard2 = new CardData();
                tenderCard2.setCardId(cardId);
                // ����/����������
                tenderCard2.setMeetingId(((Integer) meetingMap.get("OPENING_MEETING_NO")).intValue());
                tenderCard2.setCardType(cardType);
                cardList.add(tenderCard2);
            }

            if (meetingMap.get("BID_EVAL_MEETING_NO") != null) {
                CardData tenderCard3 = new CardData();
                tenderCard3.setCardId(cardId);
                // ���������
                tenderCard3.setMeetingId(((Integer) meetingMap.get("BID_EVAL_MEETING_NO")).intValue());
                tenderCard3.setCardType(cardType);
                cardList.add(tenderCard3);
            }
        }
        // ����
        if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
            if (meetingMap.get("AUCTION_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // �����������
                tenderCard1.setMeetingId(((Integer) meetingMap.get("AUCTION_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }
        }
        // ����
        if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
            if (meetingMap.get("LIST_MEETING_NO") != null) {
                CardData tenderCard1 = new CardData();
                tenderCard1.setCardId(cardId);
                // ���ƻ�����
                tenderCard1.setMeetingId(((Integer) meetingMap.get("LIST_MEETING_NO")).intValue());
                tenderCard1.setCardType(cardType);
                cardList.add(tenderCard1);
            }

            if (meetingMap.get("SCE_BID_MEETING_NO") != null) {
                CardData tenderCard2 = new CardData();
                tenderCard2.setCardId(cardId);
                // �����ֳ����ۻ�����
                tenderCard2.setMeetingId(((Integer) meetingMap.get("SCE_BID_MEETING_NO")).intValue());
                tenderCard2.setCardType(cardType);
                cardList.add(tenderCard2);
            }
        }

        // ���Ž�����ŷ��͸��Ž�ϵͳ
        if (cardList.size() > 0) {
            //logger.info("===��ʼ�����Ž�ϵͳ�ӿ�[" + excuteTimes + "]-sendCard===");
            //long start = System.currentTimeMillis();
            MessageData message = GuardService.getGuardInstance().sendCard(cardList);
            //long end = System.currentTimeMillis();
            //logger.info("�����Ž�ϵͳ�����ѣ�" + (end - start) + "����");
            //logger.info("===���������Ž�ϵͳ�ӿ�[" + excuteTimes + "]-sendCard===");

            if (message.getIsSuccess()) {
                List returnCard = message.getReturnInfoList();
                if (returnCard != null && returnCard.size() > 0) {
                    for (int i = 0; i < returnCard.size(); i++) {
                        CardData tempCard = (CardData) returnCard.get(i);
                        if (!tempCard.getIsSuccess()) {
                            int errorCode = tempCard.getErrorCode();
                            if (errorCode == GuardConstants.ERROR_CARD_CARDNO_ERROR) {
                                // ���ŷǷ�
                                logger.info("[�Ž�ϵͳ���ñ���][���ŷǷ�]:" + tempCard.getCardId());
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // �����Ų�����
                                logger.info("[�Ž�ϵͳ���ñ���][�����Ų�����]:" + tempCard.getMeetingId());
                            } else {
                                // δ֪����
                                logger.info("[�Ž�ϵͳ���ñ���][δ֪����][errorCode]:" + errorCode);
                            }
                        }
                    }
                }
            }else{
            	logger.info("���Ž�ϵͳ������Ϣʧ��:["+ message.getErrorMsg() +"]===δ���͵Ľ��׿���Ϊ["+ cardId +"]===");
            }
            // TODO ���Ͳ��ɹ������Կ����ظ�����3��
            // else {
            // logger.info("[�Ž�ϵͳ���ñ���][" + excuteTimes + "]��" + message.getErrorMsg());
            // // ���Ͳ��ɹ������Կ����ظ�����3��
            // if (excuteTimes < 3) {
            // sendCard2Guard(transferMode, cardId, meetingMap, cardType);
            // excuteTimes++;
            // }
            // }
        } else {
            logger.info("[�Ž�ϵͳ���ñ���][��������\"" + transferMode + "\"������]��[�����Ų�����]");
        }
    }

    /**
     * �����Ž�����Ϣ
     * 
     * @param tdscBlockTranApp
     * @param tdscBlockPlan
     * @param tdscBidderApp
     * @return
     */
    public String sendGuardMessage(TdscBlockTranApp tdscBlockTranApp, TdscBlockPlanTable tdscBlockPlan, TdscBidderApp tdscBidderApp) {
        logger.info("===��ʼ�����Ž�ϵͳ�ӿ�-sendGuardMessage===");
        long start = System.currentTimeMillis();

        // ����
        GuardService guardService = GuardService.getGuardInstance();
        List meetingList = new ArrayList();
        String retString = null;
        // if (tdscBlockTranApp != null && GlobalConstants.DIC_TRANSFER_TENDER.equals(tdscBlockTranApp.getTransferMode())) {
        // �б�Ĵ���
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
                                // ���ŷǷ�
                                retString = "���ŷǷ�";
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // �����Ų�����
                                retString = "�����Ų�����";
                            } else {
                                // δ֪����
                                retString = "δ֪����";
                            }
                        }
                    }
                }
            } else {
                logger.error("�Ž�ϵͳ���ñ���" + message.getErrorMsg());
                // ���紫���쳣
                retString = "===�����Ž�ϵͳ����===";
            }
        } else {
            logger.error("�����Ž������ţ�");
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
                                // ���ŷǷ�
                                retString = "���ŷǷ�";
                            } else if (errorCode == GuardConstants.ERROR_CARD_MEET_NOT_EXIST) {
                                // �����Ų�����
                                retString = "�����Ų�����";
                            } else {
                                // δ֪����
                                retString = "δ֪����";
                            }
                        }
                    }
                }
            } else {
                logger.error("�Ž�ϵͳ���ñ���" + message.getErrorMsg());
                // ���紫���쳣
                retString = "===�����Ž�ϵͳ����===";
            }
        } else {
            logger.error("�����Ž������ţ�");
        }

        // } else {
        // retString = "���޵ؿ���Ϣ";
        // }

        long end = System.currentTimeMillis();
        logger.info("�����Ž�ϵͳ�����ѣ�" + (end - start) + "����");
        logger.info("===���������Ž�ϵͳ�ӿ�-sendGuardMessage===");

        return retString;
    }

}
