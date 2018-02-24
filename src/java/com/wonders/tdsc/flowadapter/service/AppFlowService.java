package com.wonders.tdsc.flowadapter.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscFlowPostStatus;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.bo.condition.TdscOpnnCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.dao.TdscAppDao;
import com.wonders.tdsc.flowadapter.dao.TdscFlowPostStatusDao;
import com.wonders.tdsc.flowadapter.dao.TdscOpnnDao;

public class AppFlowService extends BaseSpringManagerImpl {

    private TdscAppDao tdscAppDao;

    private TdscOpnnDao tdscOpnnDao;

    private TdscFlowPostStatusDao tdscFlowPostStatusDao;

    private CommonFlowService commonFlowService;

    public void setTdscAppDao(TdscAppDao tdscAppDao) {
        this.tdscAppDao = tdscAppDao;
    }

    public void setTdscOpnnDao(TdscOpnnDao tdscOpnnDao) {
        this.tdscOpnnDao = tdscOpnnDao;
    }

    public void setTdscFlowPostStatusDao(TdscFlowPostStatusDao tdscFlowPostStatusDao) {
        this.tdscFlowPostStatusDao = tdscFlowPostStatusDao;
    }

    public void setCommonFlowService(CommonFlowService commonFlowService) {
        this.commonFlowService = commonFlowService;
    }

    /** 业务对象类型-初让地块 */
    public static final String BLOCK_APP = "01";

    /** 状态ID-初始化状态 */
    public static final String STATUS_INIT = "0101";

    /** 操作ID-初始化操作 */
    public static final String ACTION_INIT = "0101";

    /** 申请状态-暂存 */
    public static final String APP_RESULT_TEMP = "00";

    /** 申请状态-处理中 */
    public static final String APP_RESULT_DEALING = "01";

    /** 申请状态-结束 */
    public static final String APP_RESULT_END = "10";

    /** 申请状态-中止 */
    public static final String APP_RESULT_TERMINATE = "11";

    /** 关闭节点类型-不关闭 */
    public static final String CLOSE_NODE_NOT = "00";

    /** 关闭节点类型-正常关闭 */
    public static final String CLOSE_NODE_NORMAL = "01";

    /** 关闭节点类型-中止关闭 */
    public static final String CLOSE_NODE_SPECIAL_CLOSE = "02";

    /** 关闭节点类型-取消关闭 */
    public static final String CLOSE_NODE_TERMINATE = "03";

    /** 关闭节点类型-特送到下一节点 */
    public static final String CLOSE_NODE_SPECIAL_SEND = "04";

    /** 操作是否执行完毕-否 */
    public static final String IS_OPT_FALSE = "00";

    /** 操作是否执行完毕-是 */
    public static final String IS_OPT_TRUE = "01";

    /** 是否默认结果-否 */
    public static final String IS_DEFAULT_RESULT_FALSE = "00";

    /** 是否默认结果-是 */
    public static final String IS_DEFAULT_RESULT_TRUE = "01";

    /** 结束动作 */
    public static final String END_ACTION = "END";

    /** 双杨流程动作-提交 */
    public static final String SY_SEND_FORWARD = "sendForward";

    /** 双杨流程动作-回退 */
    public static final String SY_SEND_BACK = "sendBack";

    /**
     * 查询历史意见列表
     * 
     * @param condition
     *            历史操作查询条件bo
     * @return List 历史操作列表
     */
    public List queryAppOpnnList(TdscOpnnCondition condition) {
        condition.setIfOpt(IS_OPT_TRUE); //设定查询条件中是否已办为是
        return tdscOpnnDao.findOpnnList(condition);
    }

    /**
     * 查询历史意见列表
     * 
     * @param appId
     *            业务Id
     * @return List 历史操作列表
     */
    public List queryAppOpnnList(String appId) {
        TdscOpnnCondition condition = new TdscOpnnCondition();
        condition.setAppId(appId);
        return this.queryAppOpnnList(condition);
    }

    /**
     * 查询未提交操作的意见信息
     * 
     * @param condition
     *            意见信息查询条件bo
     * @return TdscOpnn 意见详细信息
     */
    public TdscOpnn queryWaitingOpnnInfo(TdscOpnnCondition condition) {
        condition.setIfOpt(IS_OPT_FALSE); //设定查询条件中是否已办为否
        return tdscOpnnDao.findOpnnInfo(condition);
    }

    /**
     * 初始化业务申请状态和历史意见
     * 
     * @param appId
     *            业务ID
     * @param transferType
     *            出让方式
     * @param user
     *            用户信息
     * @return Map 数据库保存结果
     */
    public Map initAppOpnn(String appId, String transferType, SysUser user) throws Exception {
        Map returnMap = new HashMap();

        Timestamp nowDate = new Timestamp(System.currentTimeMillis()); //获取服务器系统时间
        String userOrgan = user.getRegionId(); //用户区县
        String userId = user.getUserId(); //用户ID
        String userName = user.getDisplayName(); //用户名称

        if (transferType == null || "".equals(transferType.trim()) || "null".equals(transferType.trim())) {
            transferType = GlobalConstants.DIC_TRANSFER_LISTING; //如在初始化时尚未确定出让方式，则默认为挂牌(在确定后可重新进行初始化)
        }

        TdscApp tdscApp = new TdscApp(); //初始化业务申请信息
        tdscApp.setTdscAppId(appId);
        tdscApp.setAppType(BLOCK_APP);
        tdscApp.setTransferMode(transferType);
        tdscApp.setAppDate(nowDate);
        tdscApp.setAppOrgan(userOrgan);
        tdscApp.setAppPersonId(userId);
        tdscApp.setAppPerson(userName);
        tdscApp.setNodeId(FlowConstants.FLOW_NODE_AUDIT);
        tdscApp.setStatusId(STATUS_INIT);
        tdscApp.setNodeDate(nowDate);
        tdscApp.setStatusDate(nowDate);
        tdscApp.setAppResult(APP_RESULT_TEMP);
        tdscApp.setLastActnOrgan(userOrgan);
        tdscApp.setLastActnUserId(userId);
        tdscApp.setLastActnUser(userName);

        TdscOpnn tdscOpnn = new TdscOpnn(); //初始化业务历史表中的初始操作信息
        tdscOpnn.setAppId(appId);
        tdscOpnn.setActionNum(new BigDecimal(1));
        tdscOpnn.setActionId(ACTION_INIT);
        tdscOpnn.setFirstDate(nowDate);
        tdscOpnn.setAccDate(nowDate);
        tdscOpnn.setIsOpt(IS_OPT_FALSE);

        try {
            tdscApp = (TdscApp) tdscAppDao.save(tdscApp); //保存业务申请信息
            tdscOpnn = (TdscOpnn) tdscOpnnDao.save(tdscOpnn); //保存意见信息
            commonFlowService.initAppFlow(appId, transferType, userId); //初始化业务流程
            returnMap.put("tdscApp", tdscApp);
            returnMap.put("tdscOpnn", tdscOpnn);
        } catch (Exception e) {
            returnMap.put("errorMessage", e);
            throw e;
        }

        return returnMap;
    }

    /**
     * 查询待办操作的结果列表
     * 
     * @param condition
     *            操作结果查询条件
     * @return List 操作结果列表
     */
    public List queryResultList(TdscOpnnCondition condition) throws Exception {
        List returnList = null;
        String appId = condition.getAppId();

        condition.setIfOpt(IS_OPT_FALSE); //设定意见查询条件中是否已办为否

        try {
            TdscOpnn nextOpnn = tdscOpnnDao.findOpnnInfo(condition); //查得待办操作

            if (nextOpnn != null && nextOpnn.getActionId() != null) {
                String nextActionId = nextOpnn.getActionId(); //查得待办操作ID
                TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
                if (tdscApp != null && tdscApp.getTransferMode() != null && tdscApp.getNodeId() != null) {
                    String transferMode = tdscApp.getTransferMode();
                    String nodeId = tdscApp.getNodeId();

                    TdscFlowCondition flowCondition = new TdscFlowCondition();
                    flowCondition.setTransferMode(transferMode);
                    flowCondition.setNodeId(nodeId);
                    flowCondition.setActionId(nextActionId);
                    returnList = tdscFlowPostStatusDao.findStatusConfigList(flowCondition); //查得待办操作的所有操作结果列表
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * 根据用户选择的操作结果更新业务申请状态和历史意见
     * 
     * @param tdscAppFlow
     *            意见数据bo
     */
    public void saveOpnn(TdscAppFlow tdscAppFlow) throws Exception {
        if (tdscAppFlow != null && tdscAppFlow.getAppId() != null) {
        	Timestamp nowDate = new Timestamp(System.currentTimeMillis());
            String appId = tdscAppFlow.getAppId();
            SysUser user = tdscAppFlow.getUser();
            String userOrgan = user.getRegionId();
            String userId = user.getUserId();
            String userName = user.getDisplayName();

            TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
            String transferMode = tdscAppFlow.getTransferMode();
            TdscOpnn tdscOpnn = new TdscOpnn();
            if (tdscApp == null) { //第一次保存意见
                Map initMap = this.initAppOpnn(appId, transferMode, user); //初始化操作意见
                tdscApp = (TdscApp) initMap.get("tdscApp");
                tdscOpnn = (TdscOpnn) initMap.get("tdscOpnn");
            } else if (transferMode == null || "".equals(transferMode.trim()) || "null".equals(transferMode.trim())) { //非第一次保存意见 
                transferMode = tdscApp.getTransferMode(); //获取出让方式
            } else if (FlowConstants.FLOW_NODE_AUDIT.equals(tdscApp.getNodeId()) && !transferMode.equals(tdscApp.getTransferMode())) { //更改了出让方式
                tdscApp.setTransferMode(transferMode);
                tdscApp = (TdscApp) this.tdscAppDao.saveOrUpdate(tdscApp);
                commonFlowService.modifyAppFlow(appId, transferMode, tdscApp.getAppDate()); //重新初始化流程
            }

            TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
            opnnCondition.setAppId(appId);
            opnnCondition.setIfOpt(IS_OPT_FALSE);
            tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //查询待办操作信息
            if (tdscOpnn != null) {
                boolean signTag = false;
                if (tdscOpnn.getActionUser() != null) {
                    signTag = true; //查询当前用户是否已签收
                }

                TdscFlowCondition flowCondition = new TdscFlowCondition();
                flowCondition.setTransferMode(transferMode);
                flowCondition.setNodeId(tdscApp.getNodeId());
                flowCondition.setActionId(tdscOpnn.getActionId());
                if (tdscAppFlow.getResultId() != null)
                    flowCondition.setResultId(tdscAppFlow.getResultId());
                else
                    flowCondition.setIsDefaultResult(IS_DEFAULT_RESULT_TRUE); //如未选择操作结果，自动选择默认操作结果
                TdscFlowPostStatus statusConfig = tdscFlowPostStatusDao.findStatusConfigInfo(flowCondition); //查询操作结果对应流程配置信息
                String currentNode = tdscApp.getNodeId();

                if (statusConfig != null) {

                    String closeNodeType = statusConfig.getIfCloseNode(); //当前操作是否需要关闭节点

                    //根据关闭节点类型进行业务流程控制
                    if (CLOSE_NODE_NORMAL.equals(closeNodeType)) {
                        commonFlowService.postAppNode(appId, tdscApp.getNodeId(), tdscApp.getTransferMode(), userId);
                    } else if (CLOSE_NODE_SPECIAL_CLOSE.equals(closeNodeType)) {
                        commonFlowService.specialClose(appId, tdscApp.getNodeId(), tdscApp.getTransferMode());
                    } else if (CLOSE_NODE_TERMINATE.equals(closeNodeType)) {
                        commonFlowService.terminateAppNode(appId);
                    } else if (CLOSE_NODE_SPECIAL_SEND.equals(closeNodeType)) {
                        String targetNode = statusConfig.getPostNodeId();
                        commonFlowService.specialSend(appId, tdscApp.getNodeId(), targetNode, tdscApp.getTransferMode());
                    }

                    //更新tdscApp表中的节点信息
                    if (!CLOSE_NODE_NOT.equals(closeNodeType)) {
                        tdscApp.setNodeId(statusConfig.getPostNodeId());
                        tdscApp.setNodeDate(nowDate);
                        if (CLOSE_NODE_SPECIAL_CLOSE.equals(closeNodeType)) {
                            tdscApp.setAppResult(APP_RESULT_END);
                        } else if (CLOSE_NODE_TERMINATE.equals(closeNodeType)) {
                            tdscApp.setAppResult(APP_RESULT_TERMINATE);
                        } else if (FlowConstants.FLOW_NODE_FINISH.equals(statusConfig.getPostNodeId())) {
                            tdscApp.setAppResult(APP_RESULT_END);
                        } else {
                            tdscApp.setAppResult(APP_RESULT_DEALING);
                        }
                    }
                    tdscApp.setStatusId(statusConfig.getPostStatusId());
                    tdscApp.setStatusDate(nowDate);
                    tdscApp.setLastActnOrgan(userOrgan);
                    tdscApp.setLastActnUserId(userId);
                    tdscApp.setLastActnUser(userName);

                    tdscApp = (TdscApp) tdscAppDao.saveOrUpdate(tdscApp); //更新申请业务表
                    /**
                    //调用双杨接口进行子流程控制
                    if (statusConfig.getSyFlowAction() != null) {
                        WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

                        Map map = new HashMap();
                        MessageData data = null;

                        //获取子流程双杨实例ID
                        String instanceId = this.commonFlowService.getAppInstanceId(appId, transferMode, currentNode);

                        WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

                        List workItems = new ArrayList();
                        //获取双杨子流程实例
                        if (!signTag) {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, false);
                        } else {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, true);
                        }

                        WorkItem workItem = new WorkItem();
                        if (workItems != null && workItems.size() > 0) {
                            workItem = (WorkItem) workItems.get(0);
                        }

                        //获取子流程当前操作数
                        int currentOrderNum = workItem.getCurrentOrderNum();

                        //签收申请
                        if (!signTag) {
                            workFlowProcess.signInWorkItem(instanceId, userId, currentOrderNum);
                        }
                        String syAction = statusConfig.getSyFlowAction();
                        //根据操作结果执行相应的双杨流程动作
                        if (SY_SEND_FORWARD.equals(syAction)) {
                            data = workFlowProcess.sendForward(instanceId, currentOrderNum, userId, map);
                        } else if (SY_SEND_BACK.equals(syAction)) {
                            data = workFlowProcess.sendBack(instanceId, currentOrderNum, userId);
                        }
                    }
					**/
                    tdscOpnn.setActionOrgan(userOrgan);
                    tdscOpnn.setActionUserId(userId);
                    tdscOpnn.setActionUser(userName);
                    tdscOpnn.setActionDate(new Timestamp(System.currentTimeMillis()));
                    tdscOpnn.setResultId(statusConfig.getResultId());
                    tdscOpnn.setResultName(statusConfig.getResultName());
                    tdscOpnn.setTextOpen(tdscAppFlow.getTextOpen());
                    tdscOpnn.setIsOpt(IS_OPT_TRUE);

                    tdscOpnn = (TdscOpnn) tdscOpnnDao.saveOrUpdate(tdscOpnn); //更新操作记录

                    if (!END_ACTION.equals(statusConfig.getPostActionId())) {
                        TdscOpnn nextOpnn = new TdscOpnn();
                        nextOpnn.setAppId(appId);
                        nextOpnn.setActionNum(this.getNextActionNum(appId));
                        nextOpnn.setActionId(statusConfig.getPostActionId());
                        nextOpnn.setFirstDate(tdscOpnn.getFirstDate());
                        nextOpnn.setAccDate(nowDate);
                        nextOpnn.setIsOpt(IS_OPT_FALSE);

                        nextOpnn = (TdscOpnn) tdscOpnnDao.saveOrUpdate(nextOpnn); //初始化下一操作信息
                    }
                }
            }
        }
    }
    /**
     * 根据出让方式返回节点列表(联动)
     * 
     * @param condition
     * @return
     */
    public List queryNodeListWithBlockTransfer(String transferMode) {
        List transferNodeList = tdscFlowPostStatusDao.findtransferNodeList(transferMode);
        return transferNodeList;
    }
    /**
     * 根据默认操作结果更新业务申请状态和历史意见
     * 
     * @param appId
     *            业务Id
     * @param transferMode
     *            出让方式
     * @param user
     *            系统用户
     */
    public void saveOpnn(String appId, String transferMode, SysUser user) throws Exception {
        TdscAppFlow tdscAppFlow = new TdscAppFlow();
        tdscAppFlow.setAppId(appId);
        tdscAppFlow.setTransferMode(transferMode);
        tdscAppFlow.setUser(user);

        try {
            this.saveOpnn(tdscAppFlow);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 暂存根据用户选择的操作结果更新操作意见
     * 
     * @param tdscAppFlow
     *            意见数据bo
     */
    public void tempSaveOpnn(TdscAppFlow tdscAppFlow) throws Exception {
        if (tdscAppFlow != null && tdscAppFlow.getAppId() != null) {
        	Timestamp nowDate = new Timestamp(System.currentTimeMillis());
            String appId = tdscAppFlow.getAppId();
            SysUser user = tdscAppFlow.getUser();
            String userOrgan = user.getRegionId();
            String userId = user.getUserId();
            String userName = user.getDisplayName();
            String nodeId = null;

            TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
            String transferMode = tdscAppFlow.getTransferMode();
            TdscOpnn tdscOpnn = new TdscOpnn();
            if (tdscApp == null) { //第一次保存意见
                Map initMap = this.initAppOpnn(appId, transferMode, user);
                tdscApp = (TdscApp) initMap.get("tdscApp");
                tdscOpnn = (TdscOpnn) initMap.get("tdscOpnn");
            } else {
                if (transferMode == null || "".equals(transferMode.trim()) || "null".equals(transferMode.trim())) { //获取出让方式
                    transferMode = tdscApp.getTransferMode();
                } else if (FlowConstants.FLOW_NODE_AUDIT.equals(tdscApp.getNodeId()) && !transferMode.equals(tdscApp.getTransferMode())) { //修改出让方式
                    tdscApp.setTransferMode(transferMode);
                    tdscApp = (TdscApp) this.tdscAppDao.saveOrUpdate(tdscApp);
                    commonFlowService.modifyAppFlow(appId, transferMode, tdscApp.getAppDate()); //重新初始化流程
                }
                
             
                
                TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
                opnnCondition.setAppId(appId);
                opnnCondition.setIfOpt(IS_OPT_FALSE);
                tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //获取下一操作信息
                if (tdscOpnn != null) {
                    boolean signTag = false;
                    if (tdscOpnn.getActionUser() != null) {
                        signTag = true;
                    }

                    TdscFlowCondition flowCondition = new TdscFlowCondition();
                    flowCondition.setTransferMode(transferMode);
                    flowCondition.setNodeId(tdscApp.getNodeId());
                    flowCondition.setActionId(tdscOpnn.getActionId());
                    if (tdscAppFlow.getResultId() != null)
                        flowCondition.setResultId(tdscAppFlow.getResultId());
                    else
                        flowCondition.setIsDefaultResult(IS_DEFAULT_RESULT_TRUE); //如未选择操作结果，则自动选择默认结果
                    TdscFlowPostStatus statusConfig = tdscFlowPostStatusDao.findStatusConfigInfo(flowCondition); //查询操作结果对应流程配置信息

                    //更新操作记录
                    if (statusConfig != null) {
                        
                        TdscOpnnCondition condition1= new TdscOpnnCondition();
                        condition1.setAppId(appId);
                        condition1.setNodeId(statusConfig.getActionId());
                        //设置查询的actionNum 是null
                        condition1.setIsnull(true);
                        //查询条件在condition1下，查询当前节点下保存信息
                        TdscOpnn tdscOpnn1 = tdscOpnnDao.findOpnnInfo(condition1); 
                        if(tdscOpnn1==null){
                            tdscOpnn1 =new TdscOpnn();
                        }
                            tdscOpnn1.setAppId(appId);
                            tdscOpnn1.setActionId(statusConfig.getActionId());
                            tdscOpnn1.setFirstDate(nowDate);
                            tdscOpnn1.setAccDate(nowDate);
                            tdscOpnn1.setIsOpt(IS_OPT_TRUE);
                            tdscOpnn1.setActionOrgan(userOrgan);
                            tdscOpnn1.setActionUserId(userId);
                            tdscOpnn1.setActionUser(userName);
                            tdscOpnn1.setActionDate(new Timestamp(System.currentTimeMillis()));
                            tdscOpnn1.setResultId(statusConfig.getResultId());
                            tdscOpnn1.setResultName("暂存");
                            // 历史表中的保存操作信息的保存或修改
                            tdscOpnnDao.saveOrUpdate(tdscOpnn1);
                      
                        
                        
                        tdscOpnn.setActionOrgan(userOrgan);
                        tdscOpnn.setActionUserId(userId);
                        tdscOpnn.setActionUser(userName);
                        tdscOpnn.setActionDate(new Timestamp(System.currentTimeMillis()));
                        tdscOpnn.setResultId(statusConfig.getResultId());
                        tdscOpnn.setResultName(statusConfig.getResultName());
                        tdscOpnn.setTextOpen(tdscAppFlow.getTextOpen());

                        tdscOpnn = (TdscOpnn) tdscOpnnDao.saveOrUpdate(tdscOpnn);
                    }
                   /**
                    //调用双杨接口进行子流程控制
                    if (statusConfig.getSyFlowAction() != null) {
                        WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

                        Map map = new HashMap();
                        MessageData data = null;

                        //获取子流程双杨实例ID
                        String instanceId = this.commonFlowService.getAppInstanceId(appId, transferMode, tdscApp.getNodeId());

                        WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

                        List workItems = new ArrayList();
                        //获取双杨子流程实例
                        if (!signTag) {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, false);
                        } else {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, true);
                        }

                        WorkItem workItem = new WorkItem();
                        if (workItems != null && workItems.size() > 0) {
                            workItem = (WorkItem) workItems.get(0);
                        }

                        //获取子流程当前操作数
                        int currentOrderNum = workItem.getCurrentOrderNum();

                        //签收申请
                        if (!signTag) {
                            workFlowProcess.signInWorkItem(instanceId, userId, currentOrderNum);
                        }
                    }
                    **/
                }
            }
        }
    }

    /**
     * 根据默认操作结果暂存根据用户选择的操作结果更新操作意见
     * 
     * @param appId
     *            业务Id
     * @param transferMode
     *            出让方式
     * @param user
     *            系统用户
     */
    public void tempSaveOpnn(String appId, String transferMode, SysUser user) throws Exception {
        TdscAppFlow tdscAppFlow = new TdscAppFlow();
        tdscAppFlow.setAppId(appId);
        tdscAppFlow.setTransferMode(transferMode);
        tdscAppFlow.setUser(user);

        try {
            this.tempSaveOpnn(tdscAppFlow);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 查询节点对应的流程业务待办列表（对应含双杨流程的节点）
     * 
     * @param nodeId
     *            节点Id
     * @param user
     *            系统用户
     * @param actionIdList
     *            结点列表
     * @return List 业务列表
     */
    public List queryFlowList(String nodeId, SysUser user,List actionIdList) throws Exception {
        String actionId =null;
        if(actionIdList ==null || actionIdList.size() ==0) return null;
        
        for(int i=0;i<actionIdList.size();i++){
        	if(!((String)actionIdList.get(i)).startsWith(nodeId)) continue;
        	if(actionId ==null) actionId="'"+(String)actionIdList.get(i)+"'";
        	else actionId=actionId+",'"+(String)actionIdList.get(i)+"'";
        }
        logger.debug(nodeId+"------nodeId======actionId------"+actionId);
        List returnList = null;
        /**
        //根据申请业务表查询待办列表---无签收功能
        TdscAppCondition appCondition = new TdscAppCondition();
        appCondition.setNodeId(nodeId);
        appCondition.setStatusId(statusId);
        appCondition.setAppResult(APP_RESULT_TEMP);
        List tdscAppList = tdscAppDao.findAppList(appCondition);
        if (tdscAppList != null && tdscAppList.size() > 0) {
       	 returnList = new ArrayList();
            for (int i = 0; i < tdscAppList.size(); i++) {
            	TdscApp tdscApp = (TdscApp)tdscAppList.get(i);
                logger.debug("======待办appid======"+tdscApp.getTdscAppId());
           	 returnList.add(tdscApp.getTdscAppId());
            }
       }
        **/
//      根据申请业务表查询待办列表---存在签收功能
        TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
        opnnCondition.setNodeId(actionId);
        opnnCondition.setIfOpt(IS_OPT_FALSE);
        opnnCondition.setActionUser(user.getUserId());
        List opnnList = tdscOpnnDao.findFlowOpnnList(opnnCondition);

        if (opnnList != null && opnnList.size() > 0) {
        	 returnList = new ArrayList();
             for (int i = 0; i < opnnList.size(); i++) {
            	 TdscOpnn tdscOpnn = (TdscOpnn)opnnList.get(i);
                 logger.debug("======appid======"+tdscOpnn.getAppId());
            	 returnList.add(tdscOpnn.getAppId());
             }
        }
        
        
        /**
        long begin = System.currentTimeMillis();
        logger.debug("======开始调用双杨流程引擎获得待办列表======");

        List workList = null;
        try {
            WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();
            workList = workFlowWorkList.wmGetWorkList(GlobalConstants.APPLICATION_ID, userId); //获取有形市场应用对应的全部待办业务
        } catch (Exception e) {
            logger.warn(e);
        }

        long end = System.currentTimeMillis();
        logger.debug("======结束调用双杨流程引擎获得待办列表，共耗时" + (end - begin) + "毫秒======");

        List returnList = null;

        if (workList != null && workList.size() > 0) {
            returnList = new ArrayList();
            for (int i = 0; i < workList.size(); i++) {
                WorkItem workItem = (WorkItem) workList.get(i);
                String appId = workItem.getBusinessId();
                String businessType = workItem.getBusinessType();

                if (businessType.equals(subFlowId)) { //将查询到的业务中流程类型为所查节点对应流程ID的业务添加到返回列表中
                    returnList.add(appId);
                }
            }
        }
         **/
        if(returnList ==null || returnList.size() ==0) return null;
        return returnList;
    }

    /**
     * 查询意见页面信息
     * 
     * @param appId
     *            业务Id
     * @return Map 意见页面信息集合
     */
    public Map queryOpnnInfo(String appId) throws Exception {
        return this.queryOpnnInfo(appId, null);
    }

    /**
     * 按节点查询意见页面信息
     * 
     * @param appId
     *            业务Id
     * @param nodeId
     *            节点Id
     * @return Map 意见页面信息集合
     */
    public Map queryOpnnInfo(String appId, String nodeId) throws Exception {
        Map returnMap = new HashMap();
        TdscOpnnCondition condition = new TdscOpnnCondition();
        condition.setAppId(appId);

        List resultList = this.queryResultList(condition); //查询申请下一操作对应的全部操作结果
        TdscOpnn tempOpnn = this.queryWaitingOpnnInfo(condition); //查询申请的下一操作信息

        if (nodeId != null) {
            condition.setNodeId(nodeId);
        }
        List opnnList = this.queryAppOpnnList(condition); //查询历史操作意见

        returnMap.put("opnnList", opnnList);
        returnMap.put("resultList", resultList);
        returnMap.put("tempOpnn", tempOpnn);

        return returnMap;
    }

    /**
     * 获取下一个操作顺序号
     * 
     * @param appId
     *            业务Id
     * @return BigDecimal 下一操作数
     */
    private BigDecimal getNextActionNum(String appId) {
        return tdscOpnnDao.getNextActionNum(appId);
    }

    /**
     * 物理删除流程信息
     * 
     * @param appId
     *            业务Id
     */
    private void physicalDeleteAppFlowInfo(String appId) {
        TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
        if (tdscApp != null && tdscApp.getTdscAppId() != null) {
            tdscAppDao.delete(tdscApp);
            TdscOpnnCondition condition = new TdscOpnnCondition();
            condition.setAppId(appId);
            List opnnList = tdscOpnnDao.findOpnnList(condition);
            Iterator it = opnnList.iterator();
            while (it.hasNext()) {
                TdscOpnn opnn = (TdscOpnn) it.next();
                tdscOpnnDao.delete(opnn); //将历史操作全部删除
            }
        }
    }
    
    /**
     * 查询双杨流程信息
     * 
     * @param appId
     *            业务Id
     * @param userId
     *            用户Id
     * @return WorkItem 双杨流程信息
     * 
     */
    /**
    public WorkItem getAppFlowInfo(String appId, String userId) {
        TdscOpnn tdscOpnn = new TdscOpnn();
        TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
        opnnCondition.setAppId(appId);
        opnnCondition.setIfOpt(IS_OPT_FALSE);
        tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //查询申请对应的下一操作
        WorkItem workItem = new WorkItem();
        if (tdscOpnn != null) {
            boolean signTag = false;
            if (tdscOpnn.getActionUser() != null) {
                signTag = true; //判断用户是否已签收
            }
            WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

            WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

            List workItems = new ArrayList();
            //查询申请对应的双杨流程实例
            if (!signTag) {
                workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, false);
            } else {
                workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, true);
            }

            if (workItems != null && workItems.size() > 0) {
                workItem = (WorkItem) workItems.get(0);
            }
        }
        return workItem;
    }
    **/
}