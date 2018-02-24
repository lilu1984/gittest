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

    /** ******** 定义流程常量 ********* */

    /** 默认流程-业务流程 */
    public static final String DEFAULT_FLOW = "01";

    /** 节点初始化类型-正常节点 */
    public static final String NORMAL_NODE = "0";

    /** 节点初始化类型-开始节点 */
    public static final String START_NODE = "1";

    /** 节点初始化类型-结束节点 */
    public static final String END_NODE = "2";

    /** 进度执行表指标更新方式-流程控制 */
    public static final String SCHEDULE_TYPE_FLOW = "01";

    /** 进度执行表指标更新方式-计划控制 */
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
     * 查询子节点双杨流程模版号
     * 
     * @param appId
     *            业务ID
     * @param subFlowStandId
     *            流程模版ID
     */
    public TdscAppWorkflowInstanceRel findWorkInstanceInfo(String appId, String subFlowStandId) throws Exception {
        return this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowStandId);
    }

    /**
     * 初始化业务节点状态
     * 
     * @param appId
     *            业务ID
     * @param transferType
     *            出让方式
     */
    public void initAppFlow(String appId, String transferType, String userId) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferType);
        condition.setNodeInitType("01");

        try {
            List initNodeList = flowConfigDao.findFlowConfigList(condition); // 查询出让方式对应的流程模版
            if (initNodeList != null && initNodeList.size() > 0) {
                Iterator it = initNodeList.iterator();
                while (it.hasNext()) {
                    TdscFlowConfig initNode = (TdscFlowConfig) it.next();
                    TdscAppNodeStat appNode = new TdscAppNodeStat();
                    appNode.setAppId(appId);
                    appNode.setFlowId(initNode.getFlowId());
                    appNode.setNodeId(initNode.getNodeId());
                    appNode.setNodeName(initNode.getNodeName());
                    if (START_NODE.equals(initNode.getIsStartNode())) { // 若节点为起始节点，将节点状态设置成活动中
                        appNode.setNodeStat(FlowConstants.STAT_ACTIVE);
                        appNode.setStartDate(new Date(System.currentTimeMillis()));
                    } else {
                        appNode.setNodeStat(FlowConstants.STAT_INIT); // 一般节点状态设置为未活动
                    }
                    
                    if ("1".equals(initNode.getHasSubFlow()) && START_NODE.equals(initNode.getIsStartNode())) {
                        //initSubFlow(appId, initNode.getSubFlowStandId(), userId); // 若节点为起始节点且节点含有子流程，初始化双杨子流程
                        appNode.setSubFlowId(initNode.getSubFlowStandId());
                    }
                    
                    appNode.setHasSubFlow(initNode.getHasSubFlow());
                    appNodeStatDao.save(appNode);

                    if (START_NODE.equals(initNode.getIsStartNode())) { // 记录初始节点的执行情况
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
     * 关闭业务的特定节点的激活状态,打开后续节点的激活状态
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @param transferType
     *            出让方式
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

            this.blockScheduleTableDao.saveOrUpdate(scheduleInfo); // 更新节点的进度执行情况

            closeNode(appId, nodeId, transferType); // 将指定节点状态设置为关闭
            if (!"END".equals(postNodeStr)) { // 若指定节点非关闭节点
                List postNodeList = getPostNodeList(postNodeStr); // 将TDSC_FLOW_CONFIG表配置的后续节点字符串转化为后续节点列表
                if (postNodeList != null) {
                    Iterator it = postNodeList.iterator();
                    while (it.hasNext()) { // 遍历后续节点列表
                        String postNode = (String) it.next();
                        TdscFlowCondition postCondition = new TdscFlowCondition();
                        postCondition.setAppId(appId);
                        postCondition.setFlowId(DEFAULT_FLOW);
                        postCondition.setNodeId(postNode);
                        postCondition.setTransferMode(transferType);
                        TdscAppNodeStat postAppNode = appNodeStatDao.findAppNodeInfo(postCondition); // 获取预生成的后续节点信息
                        TdscFlowConfig postConfig = flowConfigDao.findFlowConfigInfo(postCondition); // 获取预生成的后续节点配置信息

                        String finalStr = postConfig.getPostNode();
                        if ("FINAL".equals(finalStr)) { // 如后续节点为结束节点
                            postAppNode.setNodeStat(FlowConstants.STAT_END);
                            postAppNode.setStartDate(new Date(System.currentTimeMillis()));
                            postAppNode.setEndDate(new Date(System.currentTimeMillis()));
                            appNodeStatDao.update(postAppNode); // 将后续节点状态设置为关闭
                        } else {
                            postAppNode.setNodeStat(FlowConstants.STAT_ACTIVE);
                            postAppNode.setStartDate(new Date(System.currentTimeMillis()));

                            if ("1".equals(postConfig.getHasSubFlow())) {
                                initSubFlow(appId, postConfig.getSubFlowStandId(), userId);
                                postAppNode.setSubFlowId(postConfig.getSubFlowStandId());
                            }

                            appNodeStatDao.update(postAppNode); // 打开后续节点并初始化后续节点打开日期和子流程信息

                            colName = postConfig.getStartScheduleCol();
                            colType = postConfig.getStartScheduleInfoType();
                            scheduleInfo = this.bindScheduleInfo(appId, colName, colType);

                            this.blockScheduleTableDao.saveOrUpdate(scheduleInfo); // 更新后续节点开始执行信息
                        }
                    }
                }
            }
            autoCloseNodes(appId, transferType); // 自动关闭符合关闭条件的自动关闭节点
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 关闭业务的特定节点的激活状态,后续节点均中止,将业务结束
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @param transferType
     *            出让方式
     */
    public void specialClose(String appId, String nodeId, String transferType) throws Exception {

        closeNode(appId, nodeId, transferType); // 关闭指定节点

        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setAppId(appId);
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferType);
        condition.setNodeStat(FlowConstants.STAT_INIT);

        try {
            List unstartNodeList = appNodeStatDao.findNodeList(condition); // 查询所有未开始的节点
            if (unstartNodeList != null && unstartNodeList.size() > 0) {
                Iterator it = unstartNodeList.iterator();
                while (it.hasNext()) { // 将所有未开始节点状态设置为中止
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
                finalNodeId = finalNodeConfig.getNodeId(); // 查询结束节点

            TdscFlowCondition closeNodeCondition = new TdscFlowCondition();
            closeNodeCondition.setAppId(appId);
            closeNodeCondition.setNodeId(finalNodeId);
            TdscAppNodeStat finalNode = appNodeStatDao.findAppNodeInfo(condition);
            if (finalNode != null) {
                finalNode.setNodeStat(FlowConstants.STAT_END);
                appNodeStatDao.update(finalNode); // 将结束节点状态设置为已结束
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 关闭业务的特定节点的激活状态,特送到指定节点
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @param targetNodeId
     *            目标节点ID
     * @param transferType
     *            出让方式
     */
    public void specialSend(String appId, String nodeId, String targetNodeId, String transferType) throws Exception {

        Map targetMap = this.getTerminateNodeList(nodeId, targetNodeId, transferType); // 查询因特送操作需中止的节点列表
        Boolean findTarget = (Boolean) targetMap.get("findEndNodeTag"); // 判断目标节点是否可被特送到达(即目标节点是否为指定节点的后续节点)
        if (Boolean.TRUE.equals(findTarget)) { // 若目标节点可被特送到达
            List closeNodeList = (List) targetMap.get("NodeList"); // 获取所有在特送过程中被跳过的节点列表
            Iterator it = closeNodeList.iterator();
            while (it.hasNext()) {
                String closeNodeId = (String) it.next();

                TdscFlowCondition condition = new TdscFlowCondition();
                condition.setAppId(appId);
                condition.setNodeId(closeNodeId);
                TdscAppNodeStat appNode = this.appNodeStatDao.findAppNodeInfo(condition);
                appNode.setNodeStat(FlowConstants.STAT_TERMINATE);
                appNode.setEndDate(new Date(System.currentTimeMillis()));
                this.appNodeStatDao.update(appNode); // 中止所有被跳过的节点
            }

            closeNode(appId, nodeId, transferType); // 关闭当前节点

            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setAppId(appId);
            condition.setNodeId(targetNodeId);
            TdscAppNodeStat appNode = this.appNodeStatDao.findAppNodeInfo(condition);
            appNode.setNodeStat(FlowConstants.STAT_ACTIVE);
            appNode.setStartDate(new Date(System.currentTimeMillis()));
            this.appNodeStatDao.update(appNode); // 将特送目标节点状态置为已打开
        }
    }

    /**
     * 查询节点及节点下属状态对应业务列表
     * 
     * @param nodeId
     *            节点ID
     * @param statusId
     *            状态ID
     * @param endlessTag
     *            是否对应已结束该节点的业务
     * @return List 业务列表
     */
    public List queryAppList(String nodeId, String statusId, String endlessTag, String plusConditionTag) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        if (FlowConstants.FLOW_NODE_FINISH.equals(nodeId)) {
            condition.setNodeStat(FlowConstants.STAT_END); // 如查询结束节点，则所有处于结束节点的业务其节点状态必为已关闭
        } else {
            condition.setNodeStat(FlowConstants.STAT_ACTIVE); // 查询一般节点，其状态必为已打开
        }
        if (statusId != null && !"".equals(statusId.trim())) {
            condition.setStatusId(statusId); // 若含有节点内部状态查询条件，则进行相应设置
        }
        if (endlessTag != null && GlobalConstants.QUERY_ENDLESS_TAG.equals(endlessTag.trim())) {
            condition.setEndlessTag(GlobalConstants.QUERY_ENDLESS_TAG); // 是否查询该节点已关闭的业务
        }

        try {
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            String plusCondition = nodeConfig.getPlusCondition();
            if (plusCondition != null && !"".equals(plusCondition) && !GlobalConstants.QUERY_WITHOUTPLUS_TAG.equals(plusConditionTag)) { // 是否按照附加条件查询
                condition.setPlusCondition(plusCondition); // 设置附加查询条件
            }
            returnList = appNodeStatDao.findAppList(condition);
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * 查询节点及节点下属状态对应业务列表
     * 
     * @param nodeId
     *            节点ID
     * @param statusId
     *            状态ID
     * @return List 业务列表
     */
    public List queryAppList(String nodeId, String statusId, String endlessTag) throws Exception {
        return this.queryAppList(nodeId, statusId, endlessTag, null);
    }

    /**
     * 查询节点及节点下属状态对应业务列表
     * 
     * @param nodeId
     *            节点ID
     * @param statusId
     *            状态ID
     * @return List 业务列表
     */
    public List queryAppList(String nodeId, String statusId) throws Exception {
        return this.queryAppList(nodeId, statusId, null, null);
    }

    /**
     * 查询节点对应业务列表
     * 
     * @param nodeId
     *            节点ID
     * @return List 业务列表
     */
    public List queryAppList(String nodeId) throws Exception {
        return this.queryAppList(nodeId, null, null, null);
    }

    /**
     * 查询业务的所有节点对应状态列表
     * 
     * @param appId
     *            业务ID
     * @return List 节点列表
     */
    public List getAppNodeStat(String appId) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // 查询业务对应所有节点在TDSC_APP_NODE_STAT表中的记录
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) { // 遍历所有节点
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // 根据节点附加条件查询节点实际状态
                    returnList.add(appStat);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * 获得真实的活动节点列表
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
            List tempList = appNodeStatDao.findActiveNodeList(condition); // 查询业务对应所有节点在TDSC_APP_NODE_STAT表中的记录
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) {
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // 根据节点附加条件查询节点实际状态
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
                        returnList.add(appStat); // 只将流程图中包括的且状态为已打开的节点加入返回列表
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * 查询业务的所有流程图中包括的已打开节点列表
     * 
     * ========================================================
     * weedlu 2008-06-17
     * </P>
     * 将FlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeStat())
     * </P>
     * 调整为FlowConstants.FLOW_NODE_SCHEDULE_PLAN.equals(appStat.getNodeId())
     * ========================================================
     * 
     * @param appId
     *            业务ID
     * @return List 节点列表
     */
    public List getAppDiagramActiveNodeList(String appId) throws Exception {
        List returnList = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // 查询业务对应所有节点在TDSC_APP_NODE_STAT表中的记录
            if (tempList != null && tempList.size() > 0) {
                returnList = new ArrayList();
                Iterator it = tempList.iterator();
                while (it.hasNext()) {
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // 根据节点附加条件查询节点实际状态
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
                        returnList.add(appStat); // 只将流程图中包括的且状态为已打开的节点加入返回列表
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * 查询业务的所有节点对应状态列表
     * 
     * @param appId
     *            业务ID
     * @return Map 节点列表
     */
    public Map getAppNodeStatMap(String appId) throws Exception {
        Map returnMap = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);

        try {
            List tempList = appNodeStatDao.findNodeList(condition); // 查询业务对应所有节点在TDSC_APP_NODE_STAT表中的记录
            if (tempList != null && tempList.size() > 0) {
                returnMap = new HashMap();
                Iterator it = tempList.iterator();
                while (it.hasNext()) { // 遍历所有节点
                    TdscAppNodeStat appStat = (TdscAppNodeStat) it.next();
                    appStat = this.findAppNodeStat(appId, appStat.getNodeId()); // 根据节点附加条件查询节点实际状态
                    returnMap.put(appStat.getNodeId(), appStat);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnMap;
    }

    /**
     * 查询指定业务对应节点状态信息
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @return TdscAppNodeStat 节点状态信息
     */
    public TdscAppNodeStat findAppNodeStat(String appId, String nodeId) throws Exception {
        TdscAppNodeStat appStat = null;
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setAppId(appId);
        condition.setNodeId(nodeId);
        appStat = appNodeStatDao.findAppNodeInfo(condition); // 查询业务的指定节点在TDSC_APP_NODE_STAT表中记录

        if (appStat != null && (FlowConstants.STAT_ACTIVE.equals(appStat.getNodeStat()))) { // 如果业务已处于打开状态
            List returnList = null;
            TdscFlowCondition configCondition = new TdscFlowCondition();
            configCondition.setFlowId(DEFAULT_FLOW);
            configCondition.setNodeId(nodeId);
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            System.out.println("nodeId================>"+nodeId);
            String plusCondition = nodeConfig.getPlusCondition(); // 查询节点对应查询附加条件
            System.out.println("nodeId===>"+nodeId+nodeConfig.getPlusCondition());
            condition.setNodeStat(FlowConstants.STAT_ACTIVE);
            if (plusCondition != null && !"".equals(plusCondition)) {
                condition.setPlusCondition(plusCondition);
            }
            returnList = appNodeStatDao.findAppList(condition);
            if (returnList == null || returnList.size() < 1) {
                appStat.setNodeStat(FlowConstants.STAT_INIT); // 如加上附加条件无法查到指定节点，则说明实际节点还未开始，返回的节点信息将节点状态置回未开始
            }
            String closeCondition = nodeConfig.getCloseCondition(); // 查询节点对应的关闭条件
            if (closeCondition != null && !"".equals(closeCondition)) {
                condition.setPlusCondition(closeCondition);
                returnList = appNodeStatDao.findAppList(condition); // 若节点含有关闭条件
                if (returnList != null && returnList.size() > 0) {
                    appStat.setNodeStat(FlowConstants.STAT_END); // 如加上关闭条件可查到指定节点，说明节点实际应该已关闭（有可能因为自动关闭的滞后尚未关闭），返回的节点信息将节点状态置为已关闭
                }
            }
        }

        return appStat;
    }

    /**
     * 中止业务，将未关闭的节点都中止
     * 
     * @param appId
     *            业务ID
     */
    public void terminateAppNode(String appId) throws Exception {
        List nodeList = getAppNodeStat(appId);

        try {
            if (nodeList != null && nodeList.size() > 0) {
                Iterator it = nodeList.iterator();
                while (it.hasNext()) { // 遍历所有节点
                    TdscAppNodeStat appNode = (TdscAppNodeStat) it.next();
                    if (FlowConstants.STAT_INIT.equals(appNode.getNodeStat())) {
                        appNode.setNodeStat(FlowConstants.STAT_TERMINATE);
                        appNodeStatDao.update(appNode); // 若节点尚未开始，将其状态置为已中止并更新
                    }
                }
            }

            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setIsStartNode(END_NODE);
            TdscFlowConfig finalNodeConfig = flowConfigDao.findFlowConfigInfo(condition);
            String finalNodeId = "";
            if (finalNodeConfig != null)
                finalNodeId = finalNodeConfig.getNodeId(); // 查结束节点

            condition.setAppId(appId);
            condition.setNodeId(finalNodeId);
            TdscAppNodeStat finalNode = appNodeStatDao.findAppNodeInfo(condition);
            if (finalNode != null) {
                finalNode.setNodeStat(FlowConstants.STAT_END);
                appNodeStatDao.update(finalNode); // 将结束节点状态置为已关闭
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询业务是否有逾期不办的操作
     * 
     * @param appId
     *            业务ID
     * @return boolean 是否有
     */
    public boolean scheduleWarning(String appId) {
        return false;
    }

    /**
     * 查询业务需要自动关闭的节点列表
     * 
     * @param appId
     *            业务ID
     * @param transferType
     *            出让方式
     * @return List 需自动关闭节点列表
     */
    private List queryAppSelfCloseNodeList(String appId, String transferType) throws Exception {
        List returnList = new ArrayList();

        try {
            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setCloseType("01");
            condition.setTransferMode(transferType);
            List selfCloseList = flowConfigDao.findFlowConfigList(condition); // 查询指定业务预生成的所有需要自动关闭的节点
            if (selfCloseList != null && selfCloseList.size() > 0) {
                int i = 0;
                Iterator it = selfCloseList.iterator();
                while (it.hasNext()) { // 遍历所有需要自动关闭的节点
                    TdscFlowConfig nodeConfig = (TdscFlowConfig) it.next();
                    if (nodeConfig != null) {
                        String nodeId = nodeConfig.getNodeId();
                        boolean closeTag = testAppCloseCondition(appId, nodeId, transferType); // 测试节点是否已符合自动关闭条件
                        if (closeTag) {
                            returnList.add(i, nodeId); // 如已符合，则将节点添加到返回列表
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
     * 查询业务某一节点是否需要关闭
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @param 出让方式
     *            出让方式
     * @return boolean 是否需要关闭
     */
    private boolean testAppCloseCondition(String appId, String nodeId, String transferType) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setNodeId(nodeId);
        condition.setTransferMode(transferType);

        try {
            TdscFlowConfig nodeConfig = flowConfigDao.findFlowConfigInfo(condition); // 查询节点对应配置信息
            if (nodeConfig != null) {
                String closeCondition = nodeConfig.getCloseCondition();

                TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
                planCondition.setAppId(appId);
                planCondition.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
                planCondition.setPlusCondition(closeCondition);

                TdscBlockPlanTable planInfo = blockPlanTableDao.findBlockPlanTableInfo(planCondition); // 根据关闭条件查询进度安排表

                if (planInfo != null) {
                    return true; // 若进度安排表查询结果非空，说明该节点符合关闭条件
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    /**
     * 自动关闭业务达到关闭条件的自动关闭节点
     * 
     * @param appId
     *            业务ID
     * @param transferType
     *            出让方式
     * @throws Exception
     */
    private void autoCloseNodes(String appId, String transferType) throws Exception {
        List waitCloseList = queryAppSelfCloseNodeList(appId, transferType); // 查询所有需要关闭的自动关闭节点列表
        if (waitCloseList != null && waitCloseList.size() > 0) {
            Iterator it = waitCloseList.iterator();
            while (it.hasNext()) {
                String nodeId = (String) it.next();
                closeNode(appId, nodeId, transferType); // 关闭符合条件的节点
            }
        }

    }

    /**
     * 关闭节点
     * 
     * @param appId
     *            业务ID
     * @param nodeId
     *            节点ID
     * @param transferType
     *            出让方式
     * @throws Exception
     */
    private void closeNode(String appId, String nodeId, String transferType) throws Exception {
        try {
            TdscFlowCondition nodeCondition = new TdscFlowCondition();
            nodeCondition.setAppId(appId);
            nodeCondition.setFlowId(DEFAULT_FLOW);
            nodeCondition.setNodeId(nodeId);
            TdscAppNodeStat appNode = appNodeStatDao.findAppNodeInfo(nodeCondition); // 查询指定的节点信息
            if (appNode != null) {
                appNode.setNodeStat(FlowConstants.STAT_END);
                appNode.setEndDate(new Date(System.currentTimeMillis()));
                appNodeStatDao.update(appNode); // 将节点状态更新为已关闭并记录关闭时间
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 自动兴建子流程
     * 
     * @param appId
     *            业务ID
     * @param subFlowStandId
     *            子流程模版ID
     * @param userId
     *            用户ID
     */
    private void initSubFlow(String appId, String subFlowStandId, String userId) throws Exception {
//        try {
//            TdscAppWorkflowInstanceRel relInfo = this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowStandId);
//            if (relInfo == null) {
//                // 获取接口
//                WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();
//                Map map = new HashMap();
//                MessageData data = null;
//                data = workFlowProcess.wmStartProcessInstance(subFlowStandId, appId, userId, map); // 兴建子流程实例
//                String instanceId = data.getProcessInstanceId();
//                if (data.isSuccess()) {
//                    logger.debug("发送成功！");
//                    TdscAppWorkflowInstanceRel newRel = new TdscAppWorkflowInstanceRel();
//                    newRel.setProcessInstanceId(instanceId);
//                    newRel.setAppId(appId);
//                    newRel.setBusinessCodeId(subFlowStandId);
//                    this.appWorkflowInstanceRelDao.save(newRel); // 在本地保存业务和双杨子流程信息的对应关系
//                } else {
//                    logger.debug("Error:" + data.getErrorMsg());
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        }
    }

    /**
     * 查询申请业务的指定节点对应双杨子流程实例ID
     * 
     * @param appId
     *            业务ID
     * @param transferMode
     *            出让方式
     * @param nodeId
     *            节点ID
     * @return String 实例ID
     */
    public String getAppInstanceId(String appId, String transferMode, String nodeId) throws Exception {
        try {
            TdscFlowCondition condition = new TdscFlowCondition();
            condition.setFlowId(DEFAULT_FLOW);
            condition.setNodeId(nodeId);
            condition.setTransferMode(transferMode);

            TdscFlowConfig flowConfig = this.flowConfigDao.findFlowConfigInfo(condition);

            if (flowConfig != null && flowConfig.getSubFlowStandId() != null) {
                String subFlowId = flowConfig.getSubFlowStandId(); // 查询指定节点对应的子流程ID

                TdscAppWorkflowInstanceRel relInfo = this.appWorkflowInstanceRelDao.findRelInfo(appId, subFlowId); // 根据业务ID和子流程ID查询对应的双杨子流程实例ID
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
     * 返回后续节点列表
     * 
     * @param postNodeStr
     *            后续节点字符串
     * @return List 后续节点列表
     */
    private List getPostNodeList(String postNodeStr) {
        ArrayList returnList = new ArrayList();
        int i = 0;

        if (postNodeStr == null) {
            return null;
        }

        String tempStr = postNodeStr; // TDSC_FLOW_CONFIG表中配置的后续节点字符串
        for (int pos = tempStr.indexOf("~t"); pos != -1; pos = tempStr.indexOf("~t")) {
            String postNode = tempStr.substring(0, pos);
            tempStr = tempStr.substring(pos + 2, tempStr.length());
            returnList.add(i, postNode); // 将字符串转化为列表
            i++;
        }
        returnList.add(i, tempStr);

        return returnList;
    }

    /**
     * 返回specialSend需中止的节点列表
     * 
     * @param nodeId
     *            起始节点ID
     * @param endNodeId
     *            目标节点ID
     * @param transferType
     *            出让方式
     * @return Map 需中止的节点列表
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
            String postNodeStr = configInfo.getPostNode(); // 查询后续节点
            if (!"END".equals(postNodeStr)) {
                List postNodeList = getPostNodeList(postNodeStr); // 将TDSC_FLOW_CONFIG表中后续节点字符串转化为列表
                Iterator it = postNodeList.iterator();
                while (it.hasNext()) {
                    String nextNode = (String) it.next(); // 遍历后续节点列表
                    if (endNodeId.equals(nextNode)) {
                        findEndNode = new Boolean(true); // 是否查询到特送目标节点，结束递归调用
                    } else {
                        Map tempMap = this.getTerminateNodeList(nextNode, endNodeId, transferType); // 递归调用本查询方法
                        Boolean tempTag = (Boolean) tempMap.get("findEndNodeTag");
                        if (Boolean.TRUE.equals(tempTag)) {
                            findEndNode = new Boolean(true); // 若递归调用中含特送目标节点，则保留标志
                        }
                        List tempList = (List) tempMap.get("NodeList");
                        Iterator tempIt = tempList.iterator();
                        while (tempIt.hasNext()) {
                            String tempNode = (String) tempIt.next();
                            returnList.add(tempNode); // 保留递归调用中查询到的节点列表
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
     * 获取人员的可操作的结点的列表（只限于子流程使用）
     * 
     * @param userId
     *            人员Id
     * @return List 可操作的结点的列表
     */
    public List getNodeListByUser(String userId) {
       
            return null;

    }
    
    /**
     * 获取节点对应子流程代码
     * 
     * @param nodeId
     *            节点Id
     * @return String 子流程代码
     */
    public String getNodeSubFlowName(String nodeId) {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setNodeId(nodeId);
        TdscFlowConfig flowConfig = flowConfigDao.findFlowConfigInfo(condition);

        if (flowConfig != null && flowConfig.getSubFlowStandId() != null) {
            return flowConfig.getSubFlowStandId(); // 取节点在TDSC_FLOW_CONFIG表中配置的子流程ID
        } else {
            return null;
        }

    }

    /**
     * 重新生成流程模版
     * 
     * @param appId
     *            业务ID
     * @param transferMode
     *            出让方式
     * @param appDate
     *            业务申请日期
     */
    public void modifyAppFlow(String appId, String transferMode, Date appDate) {
        try {
            this.physicalDeleteNodeStatInfo(appId); // 物理删除当前节点预生成信息
            this.reInitAppFlow(appId, transferMode, appDate); // 重初始化流程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 物理删除流程节点信息
     * 
     * @param appId
     *            业务Id
     */
    private void physicalDeleteNodeStatInfo(String appId) {
        try {
            List nodeList = this.getAppNodeStat(appId); // 查询所有预生成节点列表
            Iterator it = nodeList.iterator();
            while (it.hasNext()) {
                TdscAppNodeStat nodeStat = (TdscAppNodeStat) it.next();
                appNodeStatDao.delete(nodeStat); // 物理删除节点
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置业务节点状态模版
     * 
     * @param appId
     *            业务ID
     * @param transferType
     *            出让方式
     * @param appDate
     *            业务申请日期
     */
    private void reInitAppFlow(String appId, String transferMode, Date appDate) throws Exception {
        TdscFlowCondition condition = new TdscFlowCondition();
        condition.setFlowId(DEFAULT_FLOW);
        condition.setTransferMode(transferMode);
        condition.setNodeInitType("01");

        try {
            List initNodeList = flowConfigDao.findFlowConfigList(condition); // 查询预生成节点模版
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
                        appNode.setStartDate(appDate); // 若节点为起始节点，将节点起始时间置为原节点起始时间
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
     * 根据流程配置信息返回需更新的进度执行表数据对象
     * 
     * @param appId
     *            业务Id
     * @param colName
     *            需更新字段名称
     * @param colType
     *            需更新字段信息来源类型
     * @return TdscBlockScheduleTable 进度执行表数据
     */
    private TdscBlockScheduleTable bindScheduleInfo(String appId, String colName, String colType) throws Exception {
        TdscBlockScheduleTable scheduleInfo = new TdscBlockScheduleTable();
        TdscBlockScheduleTable tempScheduleInfo = this.blockScheduleTableDao.findScheduleInfo(appId); // 查询进度执行信息
        if (tempScheduleInfo != null) {
            scheduleInfo = tempScheduleInfo; // 若已存在进度执行信息，则读取
        } else {
            scheduleInfo.setAppId(appId); // 若不存在进度执行信息，则新建
        }
        if (colName != null && !"".equals(colName)) { // 若需要更新进度执行表
            if (SCHEDULE_TYPE_FLOW.equals(colType)) { // 进度执行表信息来源为系统时间
                Timestamp tempTime = new Timestamp(System.currentTimeMillis());
                PropertyUtils.setProperty(scheduleInfo, colName, tempTime); // 设置系统时间
            } else if (SCHEDULE_TYPE_PLAN.equals(colType)) { // 进度执行表信息来源为计划安排表
                TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
                planCondition.setAppId(appId);
                TdscBlockPlanTable appPlan = this.blockPlanTableDao.findBlockPlanTableInfo(planCondition);
                Timestamp tempTime = new Timestamp(System.currentTimeMillis());
                if (appPlan != null) {
                    tempTime = (Timestamp) PropertyUtils.getProperty(appPlan, colName); // 读取进度安排表相关字段的信息
                }
                PropertyUtils.setProperty(scheduleInfo, colName, tempTime); // 设置进度安排表时间
            }
        }

        return scheduleInfo;
    }

}