package com.wonders.tdsc.flowadapter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockScheduleTableDao;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscAppWorkflowInstanceRel;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscFlowConfig;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.dao.TdscAppNodeStatDao;
import com.wonders.tdsc.flowadapter.dao.TdscAppWorkflowInstanceRelDao;
import com.wonders.tdsc.flowadapter.dao.TdscFlowConfigDao;

public class CommonFlowService extends BaseSpringManagerImpl {

    private TdscFlowConfigDao flowConfigDao;

    private TdscAppNodeStatDao appNodeStatDao;

    private TdscBlockPlanTableDao blockPlanTableDao;

    private TdscBlockScheduleTableDao blockScheduleTableDao;

    private TdscAppWorkflowInstanceRelDao appWorkflowInstanceRelDao;

    /** ******** �������̳��� ********* */

    /** Ĭ������-ҵ������ */
    public static final String DEFAULT_FLOW = "01";

    /** �ڵ��ʼ������-�����ڵ� */
    public static final String NORMAL_NODE = "0";

    /** �ڵ��ʼ������-��ʼ�ڵ� */
    public static final String START_NODE = "1";

    /** �ڵ��ʼ������-�����ڵ� */
    public static final String END_NODE = "2";

    /** ����ִ�б�ָ����·�ʽ-���̿��� */
    public static final String SCHEDULE_TYPE_FLOW = "01";

    /** ����ִ�б�ָ����·�ʽ-�ƻ����� */
    public static final String SCHEDULE_TYPE_PLAN = "02";

    public void setFlowConfigDao(TdscFlowConfigDao flowConfigDao) {
        this.flowConfigDao = flowConfigDao;
    }

    public void setAppNodeStatDao(TdscAppNodeStatDao appNodeStatDao) {
        this.appNodeStatDao = appNodeStatDao;
    }

    public void setBlockPlanTableDao(TdscBlockPlanTableDao blockPlanTableDao) {
        this.blockPlanTableDao = blockPlanTableDao;
    }

    public void setBlockScheduleTableDao(TdscBlockScheduleTableDao blockScheduleTableDao) {
        this.blockScheduleTableDao = blockScheduleTableDao;
    }

    public void setAppWorkflowInstanceRelDao(TdscAppWorkflowInstanceRelDao appWorkflowInstanceRelDao) {
        this.appWorkflowInstanceRelDao = appWorkflowInstanceRelDao;
    }

    /**
     * ��ѯ�ӽڵ�˫������ģ���
     * 
     * @param appId
     *            ҵ��ID
     * @param subFlowStandId
     *            ����ģ��ID
     */
    public TdscAppWorkflowInstanceRel findWorkInstanceInfo(String appId, String subFlowStandId) throws Exception {
        return this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowStandId);
    }

    /**
     * ��ʼ��ҵ��ڵ�״̬
     * 
     * @param appId
     *            ҵ��ID
     * @param transferType
     *            ���÷�ʽ
     */
    public void initAppFlow(String appId, String transferType, String userId) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferType);
        condition.setNodeInitType("01");

        try {
            List initNodeList = flowConfigDao.findFlowConfigList(condition); // ��ѯ���÷�ʽ��Ӧ������ģ��
            if (initNodeList != null && initNodeList.size() > 0) {
                Iterator it = initNodeList.iterator();
                while (it.hasNext()) {
                    TdscFlowConfig initNode = (TdscFlowConfig) it.next();
                    TdscAppNodeStat appNode = new TdscAppNodeStat();
                    appNode.setAppId(appId);
                    appNode.setFlowId(initNode.getFlowId());
                    appNode.setNodeId(initNode.getNodeId());
                    appNode.setNodeName(initNode.getNodeName());
                    if (START_NODE.equals(initNode.getIsStartNode())) { // ���ڵ�Ϊ��ʼ�ڵ㣬���ڵ�״̬���óɻ��
                        appNode.setNodeStat(FlowConstants.STAT_ACTIVE);
                        appNode.setStartDate(new Date(System.currentTimeMillis()));
                    } else {
                        appNode.setNodeStat(FlowConstants.STAT_INIT); // һ��ڵ�״̬����Ϊδ�
                    }
                    
                    if ("1".equals(initNode.getHasSubFlow()) && START_NODE.equals(initNode.getIsStartNode())) {
                        //initSubFlow(appId, initNode.getSubFlowStandId(), userId); // ���ڵ�Ϊ��ʼ�ڵ��ҽڵ㺬�������̣���ʼ��˫��������
                        appNode.setSubFlowId(initNode.getSubFlowStandId());
                    }
                    
                    appNode.setHasSubFlow(initNode.getHasSubFlow());
                    appNodeStatDao.save(appNode);

                    if (START_NODE.equals(initNode.getIsStartNode())) { // ��¼��ʼ�ڵ��ִ�����
                        String colName = initNode.getStartScheduleCol();
                        String colType = initNode.getStartScheduleInfoType();
                        TdscBlockScheduleTable scheduleInfo = this.bindScheduleInfo(appId, colName, colType);
                        scheduleInfo.setTransferMode(transferType);

                        this.blockScheduleTableDao.save(scheduleInfo);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * �ر�ҵ����ض��ڵ�ļ���״̬,�򿪺����ڵ�ļ���״̬
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @param transferType
     *            ���÷�ʽ
     */
    public void postAppNode(String appId, String nodeId, String transferType, String userId) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        condition.setTransferMode(transferType);

        try {
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            String postNodeStr = nodeConfig.getPostNode();

            String colName = nodeConfig.getEndScheduleCol();
            String colType = nodeConfig.getEndScheduleInfoType();
            TdscBlockScheduleTable scheduleInfo = this.bindScheduleInfo(appId, colName, colType);

            this.blockScheduleTableDao.saveOrUpdate(scheduleInfo); // ���½ڵ�Ľ���ִ�����

            closeNode(appId, nodeId, transferType); // ��ָ���ڵ�״̬����Ϊ�ر�
            if (!"END".equals(postNodeStr)) { // ��ָ���ڵ�ǹرսڵ�
                List postNodeList = getPostNodeList(postNodeStr); // ��TDSC_FLOW_CONFIG�����õĺ����ڵ��ַ���ת��Ϊ�����ڵ��б�
                if (postNodeList != null) {
                    Iterator it = postNodeList.iterator();
                    while (it.hasNext()) { // ���������ڵ��б�
                        String postNode = (String) it.next();
                        TdscFlowCondition postCondition = new TdscFlowCondition();
                        postCondition.setAppId(appId);
                        postCondition.setFlowId(DEFAULT_FLOW);
                        postCondition.setNodeId(postNode);
                        postCondition.setTransferMode(transferType);
                        TdscAppNodeStat postAppNode = appNodeStatDao.findAppNodeInfo(postCondition); // ��ȡԤ���ɵĺ����ڵ���Ϣ
                        TdscFlowConfig postConfig = flowConfigDao.findFlowConfigInfo(postCondition); // ��ȡԤ���ɵĺ����ڵ�������Ϣ

                        String finalStr = postConfig.getPostNode();
                        if ("FINAL".equals(finalStr)) { // ������ڵ�Ϊ�����ڵ�
                            postAppNode.setNodeStat(FlowConstants.STAT_END);
                            postAppNode.setStartDate(new Date(System.currentTimeMillis()));
                            postAppNode.setEndDate(new Date(System.currentTimeMillis()));
                            appNodeStatDao.update(postAppNode); // �������ڵ�״̬����Ϊ�ر�
                        } else {
                            postAppNode.setNodeStat(FlowConstants.STAT_ACTIVE);
                            postAppNode.setStartDate(new Date(System.currentTimeMillis()));

                            if ("1".equals(postConfig.getHasSubFlow())) {
                                initSubFlow(appId, postConfig.getSubFlowStandId(), userId);
                                postAppNode.setSubFlowId(postConfig.getSubFlowStandId());
                            }

                            appNodeStatDao.update(postAppNode); // �򿪺����ڵ㲢��ʼ�������ڵ�����ں���������Ϣ

                            colName = postConfig.getStartScheduleCol();
                            colType = postConfig.getStartScheduleInfoType();
                            scheduleInfo = this.bindScheduleInfo(appId, colName, colType);

                            this.blockScheduleTableDao.saveOrUpdate(scheduleInfo); // ���º����ڵ㿪ʼִ����Ϣ
                        }
                    }
                }
            }
            autoCloseNodes(appId, transferType); // �Զ��رշ��Ϲر��������Զ��رսڵ�
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * �ر�ҵ����ض��ڵ�ļ���״̬,�����ڵ����ֹ,��ҵ�����
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @param transferType
     *            ���÷�ʽ
     */
    public void specialClose(String appId, String nodeId, String transferType) throws Exception {

        closeNode(appId, nodeId, transferType); // �ر�ָ���ڵ�

        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setAppId(appId);
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferType);
        condition.setNodeStat(FlowConstants.STAT_INIT);

        try {
            List unstartNodeList = appNodeStatDao.findNodeList(condition); // ��ѯ����δ��ʼ�Ľڵ�
            if (unstartNodeList != null && unstartNodeList.size() > 0) {
                Iterator it = unstartNodeList.iterator();
                while (it.hasNext()) { // ������δ��ʼ�ڵ�״̬����Ϊ��ֹ
                    TdscAppNodeStat appNode = (TdscAppNodeStat) it.next();
                    appNode.setNodeStat(FlowConstants.STAT_TERMINATE);
                    appNode.setEndDate(new Date(System.currentTimeMillis()));
                    appNodeStatDao.update(appNode);
                }
            }
            TdscFlowCondition finalNodeCondition = new TdscFlowCondition();
            finalNodeCondition.setFlowId(DEFAULT_FLOW);
            finalNodeCondition.setTransferMode(transferType);
            finalNodeCondition.setIsStartNode(END_NODE);
            TdscFlowConfig finalNodeConfig = flowConfigDao.findFlowConfigInfo(finalNodeCondition);
            String finalNodeId = "";
            if (finalNodeConfig != null)
                finalNodeId = finalNodeConfig.getNodeId(); // ��ѯ�����ڵ�

            TdscFlowCondition closeNodeCondition = new TdscFlowCondition();
            closeNodeCondition.setAppId(appId);
            closeNodeCondition.setNodeId(finalNodeId);
            TdscAppNodeStat finalNode = appNodeStatDao.findAppNodeInfo(condition);
            if (finalNode != null) {
                finalNode.setNodeStat(FlowConstants.STAT_END);
                appNodeStatDao.update(finalNode); // �������ڵ�״̬����Ϊ�ѽ���
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * �ر�ҵ����ض��ڵ�ļ���״̬,���͵�ָ���ڵ�
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @param targetNodeId
     *            Ŀ��ڵ�ID
     * @param transferType
     *            ���÷�ʽ
     */
    public void specialSend(String appId, String nodeId, String targetNodeId, String transferType) throws Exception {

        Map targetMap = this.getTerminateNodeList(nodeId, targetNodeId, transferType); // ��ѯ�����Ͳ�������ֹ�Ľڵ��б�
        Boolean findTarget = (Boolean) targetMap.get("findEndNodeTag"); // �ж�Ŀ��ڵ��Ƿ�ɱ����͵���(��Ŀ��ڵ��Ƿ�Ϊָ���ڵ�ĺ����ڵ�)
        if (Boolean.TRUE.equals(findTarget)) { // ��Ŀ��ڵ�ɱ����͵���
            List closeNodeList = (List) targetMap.get("NodeList"); // ��ȡ���������͹����б������Ľڵ��б�
            Iterator it = closeNodeList.iterator();
            while (it.hasNext()) {
                String closeNodeId = (String) it.next();

                TdscFlowCondition condition = new TdscFlowCondition();
                condition.setAppId(appId);
                condition.setNodeId(closeNodeId);
                TdscAppNodeStat appNode = this.appNodeStatDao.findAppNodeInfo(condition);
                appNode.setNodeStat(FlowConstants.STAT_TERMINATE);
                appNode.setEndDate(new Date(System.currentTimeMillis()));
                this.appNodeStatDao.update(appNode); // ��ֹ���б������Ľڵ�
            }

            closeNode(appId, nodeId, transferType); // �رյ�ǰ�ڵ�

            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setAppId(appId);
            condition.setNodeId(targetNodeId);
            TdscAppNodeStat appNode = this.appNodeStatDao.findAppNodeInfo(condition);
            appNode.setNodeStat(FlowConstants.STAT_ACTIVE);
            appNode.setStartDate(new Date(System.currentTimeMillis()));
            this.appNodeStatDao.update(appNode); // ������Ŀ��ڵ�״̬��Ϊ�Ѵ�
        }
    }

    /**
     * ��ѯ�ڵ㼰�ڵ�����״̬��Ӧҵ���б�
     * 
     * @param nodeId
     *            �ڵ�ID
     * @param statusId
     *            ״̬ID
     * @param endlessTag
     *            �Ƿ��Ӧ�ѽ����ýڵ��ҵ��
     * @return List ҵ���б�
     */
    public List queryAppList(String nodeId, String statusId, String endlessTag, String plusConditionTag) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        if (FlowConstants.FLOW_NODE_FINISH.equals(nodeId)) {
            condition.setNodeStat(FlowConstants.STAT_END); // ���ѯ�����ڵ㣬�����д��ڽ����ڵ��ҵ����ڵ�״̬��Ϊ�ѹر�
        } else {
            condition.setNodeStat(FlowConstants.STAT_ACTIVE); // ��ѯһ��ڵ㣬��״̬��Ϊ�Ѵ�
        }
        if (statusId != null && !"".equals(statusId.trim())) {
            condition.setStatusId(statusId); // �����нڵ��ڲ�״̬��ѯ�������������Ӧ����
        }
        if (endlessTag != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(endlessTag.trim())) {
            condition.setEndlessTag(GlobalConstants.QUERY_ENDLESS_TAG); // �Ƿ��ѯ�ýڵ��ѹرյ�ҵ��
        }

        try {
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            String plusCondition = nodeConfig.getPlusCondition();
            if (plusCondition != null && !"".equals(plusCondition) && !GlobalConstants.QUERY_WITHOUTPLUS_TAG.equals(plusConditionTag)) { // �Ƿ��ո���������ѯ
                condition.setPlusCondition(plusCondition); // ���ø��Ӳ�ѯ����
            }
            returnList = appNodeStatDao.findAppList(condition);
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * ��ѯ�ڵ㼰�ڵ�����״̬��Ӧҵ���б�
     * 
     * @param nodeId
     *            �ڵ�ID
     * @param statusId
     *            ״̬ID
     * @return List ҵ���б�
     */
    public List queryAppList(String nodeId, String statusId, String endlessTag) throws Exception {
        return this.queryAppList(nodeId, statusId, endlessTag, null);
    }

    /**
     * ��ѯ�ڵ㼰�ڵ�����״̬��Ӧҵ���б�
     * 
     * @param nodeId
     *            �ڵ�ID
     * @param statusId
     *            ״̬ID
     * @return List ҵ���б�
     */
    public List queryAppList(String nodeId, String statusId) throws Exception {
        return this.queryAppList(nodeId, statusId, null, null);
    }

    /**
     * ��ѯ�ڵ��Ӧҵ���б�
     * 
     * @param nodeId
     *            �ڵ�ID
     * @return List ҵ���б�
     */
    public List queryAppList(String nodeId) throws Exception {
        return this.queryAppList(nodeId, null, null, null);
    }

    /**
     * ��ѯҵ������нڵ��Ӧ״̬�б�
     * 
     * @param appId
     *            ҵ��ID
     * @return List �ڵ��б�
     */
    public List getAppNodeStat(String appId) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // ��ѯҵ���Ӧ���нڵ���TDSC_APP_NODE_STAT���еļ�¼
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) { // �������нڵ�
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // ���ݽڵ㸽��������ѯ�ڵ�ʵ��״̬
                    returnList.add(appStat);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * �����ʵ�Ļ�ڵ��б�
     * 
     * @author weedlu
     * @since 2008-06-16
     * @param appId
     * @return
     * @throws Exception
     */
    public List getAppActiveNodeList(String appId) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findActiveNodeList(condition); // ��ѯҵ���Ӧ���нڵ���TDSC_APP_NODE_STAT���еļ�¼
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) {
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // ���ݽڵ㸽��������ѯ�ڵ�ʵ��״̬
                    if (FlowConstants.STAT_ACTIVE.equals(appStat.getNodeStat())
                            && (FlowConstants.FLOW_NODE_AUDIT.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_MAKE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_RELEASE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_GET.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_PREVIEW.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_QUESTION_GATHER.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_ANSWER.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FAQ_RELEASE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BIDDER_APP.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BIDDER_REVIEW.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID_EVA.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_AUCTION.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_LISTING.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_LISTING_SENCE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_RESULT_SHOW.equals(appStat.getNodeId()) || FlowConstants.FLOW_NODE_FINISH
                                    .equals(appStat.getNodeId()))) {
                        returnList.add(appStat); // ֻ������ͼ�а�������״̬Ϊ�Ѵ򿪵Ľڵ���뷵���б�
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * ��ѯҵ�����������ͼ�а������Ѵ򿪽ڵ��б�
     * 
     * ========================================================
     * weedlu 2008-06-17
     * </P>
     * ��FlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeStat())
     * </P>
     * ����ΪFlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeId())
     * ========================================================
     * 
     * @param appId
     *            ҵ��ID
     * @return List �ڵ��б�
     */
    public List getAppDiagramActiveNodeList(String appId) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // ��ѯҵ���Ӧ���нڵ���TDSC_APP_NODE_STAT���еļ�¼
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) {
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // ���ݽڵ㸽��������ѯ�ڵ�ʵ��״̬
                    if (FlowConstants.STAT_ACTIVE.equals(appStat.getNodeStat())
                            && (FlowConstants.FLOW_NODE_AUDIT.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_MAKE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_RELEASE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FILE_GET.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_PREVIEW.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_QUESTION_GATHER.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_ANSWER.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_FAQ_RELEASE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BIDDER_APP.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BIDDER_REVIEW.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_BID_EVA.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_AUCTION.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_LISTING.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_LISTING_SENCE.equals(appStat.getNodeId())
                                    || FlowConstants.FLOW_NODE_RESULT_SHOW.equals(appStat.getNodeId()) || FlowConstants.FLOW_NODE_FINISH
                                    .equals(appStat.getNodeId()))) {
                        returnList.add(appStat); // ֻ������ͼ�а�������״̬Ϊ�Ѵ򿪵Ľڵ���뷵���б�
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * ��ѯҵ������нڵ��Ӧ״̬�б�
     * 
     * @param appId
     *            ҵ��ID
     * @return Map �ڵ��б�
     */
    public Map getAppNodeStatMap(String appId) throws Exception {
        Map returnMap = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // ��ѯҵ���Ӧ���нڵ���TDSC_APP_NODE_STAT���еļ�¼
            if (tempList != null && tempList.size() > 0) {
                returnMap = new HashMap();
                Iterator it = tempList.iterator();
                while (it.hasNext()) { // �������нڵ�
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // ���ݽڵ㸽��������ѯ�ڵ�ʵ��״̬
                    returnMap.put(appStat.getNodeId(), appStat);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnMap;
    }

    /**
     * ��ѯָ��ҵ���Ӧ�ڵ�״̬��Ϣ
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @return TdscAppNodeStat �ڵ�״̬��Ϣ
     */
    public TdscAppNodeStat findAppNodeStat(String appId, String nodeId) throws Exception {
        TdscAppNodeStat appStat = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);
        condition.setNodeId(nodeId);
        appStat = appNodeStatDao.findAppNodeInfo(condition); // ��ѯҵ���ָ���ڵ���TDSC_APP_NODE_STAT���м�¼

        if (appStat != null && (FlowConstants.STAT_ACTIVE.equals(appStat.getNodeStat()))) { // ���ҵ���Ѵ��ڴ�״̬
            List returnList = null;
            TdscFlowCondition configCondition = new TdscFlowCondition();
            configCondition.setFlowId(DEFAULT_FLOW);
            configCondition.setNodeId(nodeId);
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            System.out.println("nodeId================>"+nodeId);
            String plusCondition = nodeConfig.getPlusCondition(); // ��ѯ�ڵ��Ӧ��ѯ��������
            System.out.println("nodeId===>"+nodeId+nodeConfig.getPlusCondition());
            condition.setNodeStat(FlowConstants.STAT_ACTIVE);
            if (plusCondition != null && !"".equals(plusCondition)) {
                condition.setPlusCondition(plusCondition);
            }
            returnList = appNodeStatDao.findAppList(condition);
            if (returnList == null || returnList.size() < 1) {
                appStat.setNodeStat(FlowConstants.STAT_INIT); // ����ϸ��������޷��鵽ָ���ڵ㣬��˵��ʵ�ʽڵ㻹δ��ʼ�����صĽڵ���Ϣ���ڵ�״̬�û�δ��ʼ
            }
            String closeCondition = nodeConfig.getCloseCondition(); // ��ѯ�ڵ��Ӧ�Ĺر�����
            if (closeCondition != null && !"".equals(closeCondition)) {
                condition.setPlusCondition(closeCondition);
                returnList = appNodeStatDao.findAppList(condition); // ���ڵ㺬�йر�����
                if (returnList != null && returnList.size() > 0) {
                    appStat.setNodeStat(FlowConstants.STAT_END); // ����Ϲر������ɲ鵽ָ���ڵ㣬˵���ڵ�ʵ��Ӧ���ѹرգ��п�����Ϊ�Զ��رյ��ͺ���δ�رգ������صĽڵ���Ϣ���ڵ�״̬��Ϊ�ѹر�
                }
            }
        }

        return appStat;
    }

    /**
     * ��ֹҵ�񣬽�δ�رյĽڵ㶼��ֹ
     * 
     * @param appId
     *            ҵ��ID
     */
    public void terminateAppNode(String appId) throws Exception {
        List nodeList = getAppNodeStat(appId);

        try {
            if (nodeList != null && nodeList.size() > 0) {
                Iterator it = nodeList.iterator();
                while (it.hasNext()) { // �������нڵ�
                    TdscAppNodeStat appNode = (TdscAppNodeStat) it.next();
                    if (FlowConstants.STAT_INIT.equals(appNode.getNodeStat())) {
                        appNode.setNodeStat(FlowConstants.STAT_TERMINATE);
                        appNodeStatDao.update(appNode); // ���ڵ���δ��ʼ������״̬��Ϊ����ֹ������
                    }
                }
            }

            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setIsStartNode(END_NODE);
            TdscFlowConfig finalNodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            String finalNodeId = "";
            if (finalNodeConfig != null)
                finalNodeId = finalNodeConfig.getNodeId(); // ������ڵ�

            condition.setAppId(appId);
            condition.setNodeId(finalNodeId);
            TdscAppNodeStat finalNode = appNodeStatDao.findAppNodeInfo(condition);
            if (finalNode != null) {
                finalNode.setNodeStat(FlowConstants.STAT_END);
                appNodeStatDao.update(finalNode); // �������ڵ�״̬��Ϊ�ѹر�
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ��ѯҵ���Ƿ������ڲ���Ĳ���
     * 
     * @param appId
     *            ҵ��ID
     * @return boolean �Ƿ���
     */
    public boolean scheduleWarning(String appId) {
        return false;
    }

    /**
     * ��ѯҵ����Ҫ�Զ��رյĽڵ��б�
     * 
     * @param appId
     *            ҵ��ID
     * @param transferType
     *            ���÷�ʽ
     * @return List ���Զ��رսڵ��б�
     */
    private List queryAppSelfCloseNodeList(String appId, String transferType) throws Exception {
        List returnList = new ArrayList();

        try {
            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setCloseType("01");
            condition.setTransferMode(transferType);
            List selfCloseList = flowConfigDao.findFlowConfigList(condition); // ��ѯָ��ҵ��Ԥ���ɵ�������Ҫ�Զ��رյĽڵ�
            if (selfCloseList != null && selfCloseList.size() > 0) {
                int i = 0;
                Iterator it = selfCloseList.iterator();
                while (it.hasNext()) { // ����������Ҫ�Զ��رյĽڵ�
                    TdscFlowConfig nodeConfig = (TdscFlowConfig) it.next();
                    if (nodeConfig != null) {
                        String nodeId = nodeConfig.getNodeId();
                        boolean closeTag = testAppCloseCondition(appId, nodeId, transferType); // ���Խڵ��Ƿ��ѷ����Զ��ر�����
                        if (closeTag) {
                            returnList.add(i, nodeId); // ���ѷ��ϣ��򽫽ڵ���ӵ������б�
                            i++;
                        }
                    }
                }
            }
            if (returnList.size() <= 0) {
                returnList = null;
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * ��ѯҵ��ĳһ�ڵ��Ƿ���Ҫ�ر�
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @param ���÷�ʽ
     *            ���÷�ʽ
     * @return boolean �Ƿ���Ҫ�ر�
     */
    private boolean testAppCloseCondition(String appId, String nodeId, String transferType) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        condition.setTransferMode(transferType);

        try {
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition); // ��ѯ�ڵ��Ӧ������Ϣ
            if (nodeConfig != null) {
                String closeCondition = nodeConfig.getCloseCondition();

                TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
                planCondition.setAppId(appId);
                planCondition.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
                planCondition.setPlusCondition(closeCondition);

                TdscBlockPlanTable planInfo = blockPlanTableDao.findBlockPlanTableInfo(planCondition); // ���ݹر�������ѯ���Ȱ��ű�

                if (planInfo != null) {
                    return true; // �����Ȱ��ű��ѯ����ǿգ�˵���ýڵ���Ϲر�����
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    /**
     * �Զ��ر�ҵ��ﵽ�ر��������Զ��رսڵ�
     * 
     * @param appId
     *            ҵ��ID
     * @param transferType
     *            ���÷�ʽ
     * @throws Exception
     */
    private void autoCloseNodes(String appId, String transferType) throws Exception {
        List waitCloseList = queryAppSelfCloseNodeList(appId, transferType); // ��ѯ������Ҫ�رյ��Զ��رսڵ��б�
        if (waitCloseList != null && waitCloseList.size() > 0) {
            Iterator it = waitCloseList.iterator();
            while (it.hasNext()) {
                String nodeId = (String) it.next();
                closeNode(appId, nodeId, transferType); // �رշ��������Ľڵ�
            }
        }

    }

    /**
     * �رսڵ�
     * 
     * @param appId
     *            ҵ��ID
     * @param nodeId
     *            �ڵ�ID
     * @param transferType
     *            ���÷�ʽ
     * @throws Exception
     */
    private void closeNode(String appId, String nodeId, String transferType) throws Exception {
        try {
            TdscFlowCondition nodeCondition = new TdscFlowCondition();
            nodeCondition.setAppId(appId);
            nodeCondition.setFlowId(DEFAULT_FLOW);
            nodeCondition.setNodeId(nodeId);
            TdscAppNodeStat appNode = appNodeStatDao.findAppNodeInfo(nodeCondition); // ��ѯָ���Ľڵ���Ϣ
            if (appNode != null) {
                appNode.setNodeStat(FlowConstants.STAT_END);
                appNode.setEndDate(new Date(System.currentTimeMillis()));
                appNodeStatDao.update(appNode); // ���ڵ�״̬����Ϊ�ѹرղ���¼�ر�ʱ��
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * �Զ��˽�������
     * 
     * @param appId
     *            ҵ��ID
     * @param subFlowStandId
     *            ������ģ��ID
     * @param userId
     *            �û�ID
     */
    private void initSubFlow(String appId, String subFlowStandId, String userId) throws Exception {
//        try {
//            TdscAppWorkflowInstanceRel relInfo = this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowStandId);
//            if (relInfo == null) {
//                // ��ȡ�ӿ�
//                WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();
//                Map map = new HashMap();
//                MessageData data = null;
//                data = workFlowProcess.wmStartProcessInstance(subFlowStandId, appId, userId, map); // �˽�������ʵ��
//                String instanceId = data.getProcessInstanceId();
//                if (data.isSuccess()) {
//                    logger.debug("���ͳɹ���");
//                    TdscAppWorkflowInstanceRel newRel = new TdscAppWorkflowInstanceRel();
//                    newRel.setProcessInstanceId(instanceId);
//                    newRel.setAppId(appId);
//                    newRel.setBusinessCodeId(subFlowStandId);
//                    this.appWorkflowInstanceRelDao.save(newRel); // �ڱ��ر���ҵ���˫����������Ϣ�Ķ�Ӧ��ϵ
//                } else {
//                    logger.debug("Error:" + data.getErrorMsg());
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        }
    }

    /**
     * ��ѯ����ҵ���ָ���ڵ��Ӧ˫��������ʵ��ID
     * 
     * @param appId
     *            ҵ��ID
     * @param transferMode
     *            ���÷�ʽ
     * @param nodeId
     *            �ڵ�ID
     * @return String ʵ��ID
     */
    public String getAppInstanceId(String appId, String transferMode, String nodeId) throws Exception {
        try {
            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setNodeId(nodeId);
            condition.setTransferMode(transferMode);

            TdscFlowConfig flowConfig = this.flowConfigDao.findFlowConfigInfo(condition);

            if (flowConfig != null && flowConfig.getSubFlowStandId() != null) {
                String subFlowId = flowConfig.getSubFlowStandId(); // ��ѯָ���ڵ��Ӧ��������ID

                TdscAppWorkflowInstanceRel relInfo = this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowId); // ����ҵ��ID��������ID��ѯ��Ӧ��˫��������ʵ��ID
                if (relInfo != null && relInfo.getProcessInstanceId() != null) {
                    return relInfo.getProcessInstanceId();
                }
            }

            return null;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ���غ����ڵ��б�
     * 
     * @param postNodeStr
     *            �����ڵ��ַ���
     * @return List �����ڵ��б�
     */
    private List getPostNodeList(String postNodeStr) {
        ArrayList returnList = new ArrayList();
        int i = 0;

        if (postNodeStr == null) {
            return null;
        }

        String tempStr = postNodeStr; // TDSC_FLOW_CONFIG�������õĺ����ڵ��ַ���
        for (int pos = tempStr.indexOf("~t"); pos != -1; pos = tempStr.indexOf("~t")) {
            String postNode = tempStr.substring(0, pos);
            tempStr = tempStr.substring(pos + 2, tempStr.length());
            returnList.add(i, postNode); // ���ַ���ת��Ϊ�б�
            i++;
        }
        returnList.add(i, tempStr);

        return returnList;
    }

    /**
     * ����specialSend����ֹ�Ľڵ��б�
     * 
     * @param nodeId
     *            ��ʼ�ڵ�ID
     * @param endNodeId
     *            Ŀ��ڵ�ID
     * @param transferType
     *            ���÷�ʽ
     * @return Map ����ֹ�Ľڵ��б�
     */
    private Map getTerminateNodeList(String nodeId, String endNodeId, String transferType) {
        Boolean findEndNode = new Boolean(false);
        ArrayList returnList = new ArrayList();
        Map returnMap = new HashMap();

        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        condition.setTransferMode(transferType);

        TdscFlowConfig configInfo = this.flowConfigDao.findFlowConfigInfo(condition);
        if (configInfo != null) {
            String postNodeStr = configInfo.getPostNode(); // ��ѯ�����ڵ�
            if (!"END".equals(postNodeStr)) {
                List postNodeList = getPostNodeList(postNodeStr); // ��TDSC_FLOW_CONFIG���к����ڵ��ַ���ת��Ϊ�б�
                Iterator it = postNodeList.iterator();
                while (it.hasNext()) {
                    String nextNode = (String) it.next(); // ���������ڵ��б�
                    if (endNodeId.equals(nextNode)) {
                        findEndNode = new Boolean(true); // �Ƿ��ѯ������Ŀ��ڵ㣬�����ݹ����
                    } else {
                        Map tempMap = this.getTerminateNodeList(nextNode, endNodeId, transferType); // �ݹ���ñ���ѯ����
                        Boolean tempTag = (Boolean) tempMap.get("findEndNodeTag");
                        if (Boolean.TRUE.equals(tempTag)) {
                            findEndNode = new Boolean(true); // ���ݹ�����к�����Ŀ��ڵ㣬������־
                        }
                        List tempList = (List) tempMap.get("NodeList");
                        Iterator tempIt = tempList.iterator();
                        while (tempIt.hasNext()) {
                            String tempNode = (String) tempIt.next();
                            returnList.add(tempNode); // �����ݹ�����в�ѯ���Ľڵ��б�
                        }
                    }
                }
                returnList.add(nodeId);
            } else {
                returnList.add(nodeId);
            }
        }

        returnMap.put("findEndNodeTag", findEndNode);
        returnMap.put("NodeList", returnList);

        return returnMap;
    }

    /**
     * ��ȡ��Ա�Ŀɲ����Ľ����б�ֻ����������ʹ�ã�
     * 
     * @param userId
     *            ��ԱId
     * @return List �ɲ����Ľ����б�
     */
    public List getNodeListByUser(String userId) {
       
            return null;

    }
    
    /**
     * ��ȡ�ڵ��Ӧ�����̴���
     * 
     * @param nodeId
     *            �ڵ�Id
     * @return String �����̴���
     */
    public String getNodeSubFlowName(String nodeId) {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setNodeId(nodeId);
        TdscFlowConfig flowConfig = flowConfigDao.findFlowConfigInfo(condition);

        if (flowConfig != null && flowConfig.getSubFlowStandId() != null) {
            return flowConfig.getSubFlowStandId(); // ȡ�ڵ���TDSC_FLOW_CONFIG�������õ�������ID
        } else {
            return null;
        }

    }

    /**
     * ������������ģ��
     * 
     * @param appId
     *            ҵ��ID
     * @param transferMode
     *            ���÷�ʽ
     * @param appDate
     *            ҵ����������
     */
    public void modifyAppFlow(String appId, String transferMode, Date appDate) {
        try {
            this.physicalDeleteNodeStatInfo(appId); // ����ɾ����ǰ�ڵ�Ԥ������Ϣ
            this.reInitAppFlow(appId, transferMode, appDate); // �س�ʼ������
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ɾ�����̽ڵ���Ϣ
     * 
     * @param appId
     *            ҵ��Id
     */
    private void physicalDeleteNodeStatInfo(String appId) {
        try {
            List nodeList = this.getAppNodeStat(appId); // ��ѯ����Ԥ���ɽڵ��б�
            Iterator it = nodeList.iterator();
            while (it.hasNext()) {
                TdscAppNodeStat nodeStat = (TdscAppNodeStat) it.next();
                appNodeStatDao.delete(nodeStat); // ����ɾ���ڵ�
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ҵ��ڵ�״̬ģ��
     * 
     * @param appId
     *            ҵ��ID
     * @param transferType
     *            ���÷�ʽ
     * @param appDate
     *            ҵ����������
     */
    private void reInitAppFlow(String appId, String transferMode, Date appDate) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferMode);
        condition.setNodeInitType("01");

        try {
            List initNodeList = flowConfigDao.findFlowConfigList(condition); // ��ѯԤ���ɽڵ�ģ��
            if (initNodeList != null && initNodeList.size() > 0) {
                Iterator it = initNodeList.iterator();
                while (it.hasNext()) {
                    TdscFlowConfig initNode = (TdscFlowConfig) it.next();
                    TdscAppNodeStat appNode = new TdscAppNodeStat();
                    appNode.setAppId(appId);
                    appNode.setFlowId(initNode.getFlowId());
                    appNode.setNodeId(initNode.getNodeId());
                    appNode.setNodeName(initNode.getNodeName());
                    if (START_NODE.equals(initNode.getIsStartNode())) {
                        appNode.setNodeStat(FlowConstants.STAT_ACTIVE);
                        appNode.setStartDate(appDate); // ���ڵ�Ϊ��ʼ�ڵ㣬���ڵ���ʼʱ����Ϊԭ�ڵ���ʼʱ��
                    } else {
                        appNode.setNodeStat(FlowConstants.STAT_INIT);
                    }
                    
                    if ("1".equals(initNode.getHasSubFlow()) && START_NODE.equals(initNode.getIsStartNode())) {
                        appNode.setSubFlowId(initNode.getSubFlowStandId());
                    }
                    appNode.setHasSubFlow(initNode.getHasSubFlow());
                    appNodeStatDao.save(appNode);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ��������������Ϣ��������µĽ���ִ�б����ݶ���
     * 
     * @param appId
     *            ҵ��Id
     * @param colName
     *            ������ֶ�����
     * @param colType
     *            ������ֶ���Ϣ��Դ����
     * @return TdscBlockScheduleTable ����ִ�б�����
     */
    private TdscBlockScheduleTable bindScheduleInfo(String appId, String colName, String colType) throws Exception {
        TdscBlockScheduleTable scheduleInfo = new TdscBlockScheduleTable();
        TdscBlockScheduleTable tempScheduleInfo = this.blockScheduleTableDao.findScheduleInfo(appId); // ��ѯ����ִ����Ϣ
        if (tempScheduleInfo != null) {
            scheduleInfo = tempScheduleInfo; // ���Ѵ��ڽ���ִ����Ϣ�����ȡ
        } else {
            scheduleInfo.setAppId(appId); // �������ڽ���ִ����Ϣ�����½�
        }
        if (colName != null && !"".equals(colName)) { // ����Ҫ���½���ִ�б�
            if (SCHEDULE_TYPE_FLOW.equals(colType)) { // ����ִ�б���Ϣ��ԴΪϵͳʱ��
                Timestamp tempTime = new Timestamp(System.currentTimeMillis());
                PropertyUtils.setProperty(scheduleInfo, colName, tempTime); // ����ϵͳʱ��
            } else if (SCHEDULE_TYPE_PLAN.equals(colType)) { // ����ִ�б���Ϣ��ԴΪ�ƻ����ű�
                TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
                planCondition.setAppId(appId);
                TdscBlockPlanTable appPlan = this.blockPlanTableDao.findBlockPlanTableInfo(planCondition);
                Timestamp tempTime = new Timestamp(System.currentTimeMillis());
                if (appPlan != null) {
                    tempTime = (Timestamp) PropertyUtils.getProperty(appPlan, colName); // ��ȡ���Ȱ��ű�����ֶε���Ϣ
                }
                PropertyUtils.setProperty(scheduleInfo, colName, tempTime); // ���ý��Ȱ��ű�ʱ��
            }
        }

        return scheduleInfo;
    }

}