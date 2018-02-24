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

    /** ҵ���������-���õؿ� */
    public static final String BLOCK_APP = "01";

    /** ״̬ID-��ʼ��״̬ */
    public static final String STATUS_INIT = "0101";

    /** ����ID-��ʼ������ */
    public static final String ACTION_INIT = "0101";

    /** ����״̬-�ݴ� */
    public static final String APP_RESULT_TEMP = "00";

    /** ����״̬-������ */
    public static final String APP_RESULT_DEALING = "01";

    /** ����״̬-���� */
    public static final String APP_RESULT_END = "10";

    /** ����״̬-��ֹ */
    public static final String APP_RESULT_TERMINATE = "11";

    /** �رսڵ�����-���ر� */
    public static final String CLOSE_NODE_NOT = "00";

    /** �رսڵ�����-�����ر� */
    public static final String CLOSE_NODE_NORMAL = "01";

    /** �رսڵ�����-��ֹ�ر� */
    public static final String CLOSE_NODE_SPECIAL_CLOSE = "02";

    /** �رսڵ�����-ȡ���ر� */
    public static final String CLOSE_NODE_TERMINATE = "03";

    /** �رսڵ�����-���͵���һ�ڵ� */
    public static final String CLOSE_NODE_SPECIAL_SEND = "04";

    /** �����Ƿ�ִ�����-�� */
    public static final String IS_OPT_FALSE = "00";

    /** �����Ƿ�ִ�����-�� */
    public static final String IS_OPT_TRUE = "01";

    /** �Ƿ�Ĭ�Ͻ��-�� */
    public static final String IS_DEFAULT_RESULT_FALSE = "00";

    /** �Ƿ�Ĭ�Ͻ��-�� */
    public static final String IS_DEFAULT_RESULT_TRUE = "01";

    /** �������� */
    public static final String END_ACTION = "END";

    /** ˫�����̶���-�ύ */
    public static final String SY_SEND_FORWARD = "sendForward";

    /** ˫�����̶���-���� */
    public static final String SY_SEND_BACK = "sendBack";

    /**
     * ��ѯ��ʷ����б�
     * 
     * @param condition
     *            ��ʷ������ѯ����bo
     * @return List ��ʷ�����б�
     */
    public List queryAppOpnnList(TdscOpnnCondition condition) {
        condition.setIfOpt(IS_OPT_TRUE); //�趨��ѯ�������Ƿ��Ѱ�Ϊ��
        return tdscOpnnDao.findOpnnList(condition);
    }

    /**
     * ��ѯ��ʷ����б�
     * 
     * @param appId
     *            ҵ��Id
     * @return List ��ʷ�����б�
     */
    public List queryAppOpnnList(String appId) {
        TdscOpnnCondition condition = new TdscOpnnCondition();
        condition.setAppId(appId);
        return this.queryAppOpnnList(condition);
    }

    /**
     * ��ѯδ�ύ�����������Ϣ
     * 
     * @param condition
     *            �����Ϣ��ѯ����bo
     * @return TdscOpnn �����ϸ��Ϣ
     */
    public TdscOpnn queryWaitingOpnnInfo(TdscOpnnCondition condition) {
        condition.setIfOpt(IS_OPT_FALSE); //�趨��ѯ�������Ƿ��Ѱ�Ϊ��
        return tdscOpnnDao.findOpnnInfo(condition);
    }

    /**
     * ��ʼ��ҵ������״̬����ʷ���
     * 
     * @param appId
     *            ҵ��ID
     * @param transferType
     *            ���÷�ʽ
     * @param user
     *            �û���Ϣ
     * @return Map ���ݿⱣ����
     */
    public Map initAppOpnn(String appId, String transferType, SysUser user) throws Exception {
        Map returnMap = new HashMap();

        Timestamp nowDate = new Timestamp(System.currentTimeMillis()); //��ȡ������ϵͳʱ��
        String userOrgan = user.getRegionId(); //�û�����
        String userId = user.getUserId(); //�û�ID
        String userName = user.getDisplayName(); //�û�����

        if (transferType == null || "".equals(transferType.trim()) || "null".equals(transferType.trim())) {
            transferType = GlobalConstants.DIC_TRANSFER_LISTING; //���ڳ�ʼ��ʱ��δȷ�����÷�ʽ����Ĭ��Ϊ����(��ȷ��������½��г�ʼ��)
        }

        TdscApp tdscApp = new TdscApp(); //��ʼ��ҵ��������Ϣ
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

        TdscOpnn tdscOpnn = new TdscOpnn(); //��ʼ��ҵ����ʷ���еĳ�ʼ������Ϣ
        tdscOpnn.setAppId(appId);
        tdscOpnn.setActionNum(new BigDecimal(1));
        tdscOpnn.setActionId(ACTION_INIT);
        tdscOpnn.setFirstDate(nowDate);
        tdscOpnn.setAccDate(nowDate);
        tdscOpnn.setIsOpt(IS_OPT_FALSE);

        try {
            tdscApp = (TdscApp) tdscAppDao.save(tdscApp); //����ҵ��������Ϣ
            tdscOpnn = (TdscOpnn) tdscOpnnDao.save(tdscOpnn); //���������Ϣ
            commonFlowService.initAppFlow(appId, transferType, userId); //��ʼ��ҵ������
            returnMap.put("tdscApp", tdscApp);
            returnMap.put("tdscOpnn", tdscOpnn);
        } catch (Exception e) {
            returnMap.put("errorMessage", e);
            throw e;
        }

        return returnMap;
    }

    /**
     * ��ѯ��������Ľ���б�
     * 
     * @param condition
     *            ���������ѯ����
     * @return List ��������б�
     */
    public List queryResultList(TdscOpnnCondition condition) throws Exception {
        List returnList = null;
        String appId = condition.getAppId();

        condition.setIfOpt(IS_OPT_FALSE); //�趨�����ѯ�������Ƿ��Ѱ�Ϊ��

        try {
            TdscOpnn nextOpnn = tdscOpnnDao.findOpnnInfo(condition); //��ô������

            if (nextOpnn != null && nextOpnn.getActionId() != null) {
                String nextActionId = nextOpnn.getActionId(); //��ô������ID
                TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
                if (tdscApp != null && tdscApp.getTransferMode() != null && tdscApp.getNodeId() != null) {
                    String transferMode = tdscApp.getTransferMode();
                    String nodeId = tdscApp.getNodeId();

                    TdscFlowCondition flowCondition = new TdscFlowCondition();
                    flowCondition.setTransferMode(transferMode);
                    flowCondition.setNodeId(nodeId);
                    flowCondition.setActionId(nextActionId);
                    returnList = tdscFlowPostStatusDao.findStatusConfigList(flowCondition); //��ô�����������в�������б�
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return returnList;
    }

    /**
     * �����û�ѡ��Ĳ����������ҵ������״̬����ʷ���
     * 
     * @param tdscAppFlow
     *            �������bo
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
            if (tdscApp == null) { //��һ�α������
                Map initMap = this.initAppOpnn(appId, transferMode, user); //��ʼ���������
                tdscApp = (TdscApp) initMap.get("tdscApp");
                tdscOpnn = (TdscOpnn) initMap.get("tdscOpnn");
            } else if (transferMode == null || "".equals(transferMode.trim()) || "null".equals(transferMode.trim())) { //�ǵ�һ�α������ 
                transferMode = tdscApp.getTransferMode(); //��ȡ���÷�ʽ
            } else if (FlowConstants.FLOW_NODE_AUDIT.equals(tdscApp.getNodeId()) && !transferMode.equals(tdscApp.getTransferMode())) { //�����˳��÷�ʽ
                tdscApp.setTransferMode(transferMode);
                tdscApp = (TdscApp) this.tdscAppDao.saveOrUpdate(tdscApp);
                commonFlowService.modifyAppFlow(appId, transferMode, tdscApp.getAppDate()); //���³�ʼ������
            }

            TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
            opnnCondition.setAppId(appId);
            opnnCondition.setIfOpt(IS_OPT_FALSE);
            tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //��ѯ���������Ϣ
            if (tdscOpnn != null) {
                boolean signTag = false;
                if (tdscOpnn.getActionUser() != null) {
                    signTag = true; //��ѯ��ǰ�û��Ƿ���ǩ��
                }

                TdscFlowCondition flowCondition = new TdscFlowCondition();
                flowCondition.setTransferMode(transferMode);
                flowCondition.setNodeId(tdscApp.getNodeId());
                flowCondition.setActionId(tdscOpnn.getActionId());
                if (tdscAppFlow.getResultId() != null)
                    flowCondition.setResultId(tdscAppFlow.getResultId());
                else
                    flowCondition.setIsDefaultResult(IS_DEFAULT_RESULT_TRUE); //��δѡ�����������Զ�ѡ��Ĭ�ϲ������
                TdscFlowPostStatus statusConfig = tdscFlowPostStatusDao.findStatusConfigInfo(flowCondition); //��ѯ���������Ӧ����������Ϣ
                String currentNode = tdscApp.getNodeId();

                if (statusConfig != null) {

                    String closeNodeType = statusConfig.getIfCloseNode(); //��ǰ�����Ƿ���Ҫ�رսڵ�

                    //���ݹرսڵ����ͽ���ҵ�����̿���
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

                    //����tdscApp���еĽڵ���Ϣ
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

                    tdscApp = (TdscApp) tdscAppDao.saveOrUpdate(tdscApp); //��������ҵ���
                    /**
                    //����˫��ӿڽ��������̿���
                    if (statusConfig.getSyFlowAction() != null) {
                        WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

                        Map map = new HashMap();
                        MessageData data = null;

                        //��ȡ������˫��ʵ��ID
                        String instanceId = this.commonFlowService.getAppInstanceId(appId, transferMode, currentNode);

                        WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

                        List workItems = new ArrayList();
                        //��ȡ˫��������ʵ��
                        if (!signTag) {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, false);
                        } else {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, true);
                        }

                        WorkItem workItem = new WorkItem();
                        if (workItems != null && workItems.size() > 0) {
                            workItem = (WorkItem) workItems.get(0);
                        }

                        //��ȡ�����̵�ǰ������
                        int currentOrderNum = workItem.getCurrentOrderNum();

                        //ǩ������
                        if (!signTag) {
                            workFlowProcess.signInWorkItem(instanceId, userId, currentOrderNum);
                        }
                        String syAction = statusConfig.getSyFlowAction();
                        //���ݲ������ִ����Ӧ��˫�����̶���
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

                    tdscOpnn = (TdscOpnn) tdscOpnnDao.saveOrUpdate(tdscOpnn); //���²�����¼

                    if (!END_ACTION.equals(statusConfig.getPostActionId())) {
                        TdscOpnn nextOpnn = new TdscOpnn();
                        nextOpnn.setAppId(appId);
                        nextOpnn.setActionNum(this.getNextActionNum(appId));
                        nextOpnn.setActionId(statusConfig.getPostActionId());
                        nextOpnn.setFirstDate(tdscOpnn.getFirstDate());
                        nextOpnn.setAccDate(nowDate);
                        nextOpnn.setIsOpt(IS_OPT_FALSE);

                        nextOpnn = (TdscOpnn) tdscOpnnDao.saveOrUpdate(nextOpnn); //��ʼ����һ������Ϣ
                    }
                }
            }
        }
    }
    /**
     * ���ݳ��÷�ʽ���ؽڵ��б�(����)
     * 
     * @param condition
     * @return
     */
    public List queryNodeListWithBlockTransfer(String transferMode) {
        List transferNodeList = tdscFlowPostStatusDao.findtransferNodeList(transferMode);
        return transferNodeList;
    }
    /**
     * ����Ĭ�ϲ����������ҵ������״̬����ʷ���
     * 
     * @param appId
     *            ҵ��Id
     * @param transferMode
     *            ���÷�ʽ
     * @param user
     *            ϵͳ�û�
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
     * �ݴ�����û�ѡ��Ĳ���������²������
     * 
     * @param tdscAppFlow
     *            �������bo
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
            if (tdscApp == null) { //��һ�α������
                Map initMap = this.initAppOpnn(appId, transferMode, user);
                tdscApp = (TdscApp) initMap.get("tdscApp");
                tdscOpnn = (TdscOpnn) initMap.get("tdscOpnn");
            } else {
                if (transferMode == null || "".equals(transferMode.trim()) || "null".equals(transferMode.trim())) { //��ȡ���÷�ʽ
                    transferMode = tdscApp.getTransferMode();
                } else if (FlowConstants.FLOW_NODE_AUDIT.equals(tdscApp.getNodeId()) && !transferMode.equals(tdscApp.getTransferMode())) { //�޸ĳ��÷�ʽ
                    tdscApp.setTransferMode(transferMode);
                    tdscApp = (TdscApp) this.tdscAppDao.saveOrUpdate(tdscApp);
                    commonFlowService.modifyAppFlow(appId, transferMode, tdscApp.getAppDate()); //���³�ʼ������
                }
                
             
                
                TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
                opnnCondition.setAppId(appId);
                opnnCondition.setIfOpt(IS_OPT_FALSE);
                tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //��ȡ��һ������Ϣ
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
                        flowCondition.setIsDefaultResult(IS_DEFAULT_RESULT_TRUE); //��δѡ�������������Զ�ѡ��Ĭ�Ͻ��
                    TdscFlowPostStatus statusConfig = tdscFlowPostStatusDao.findStatusConfigInfo(flowCondition); //��ѯ���������Ӧ����������Ϣ

                    //���²�����¼
                    if (statusConfig != null) {
                        
                        TdscOpnnCondition condition1= new TdscOpnnCondition();
                        condition1.setAppId(appId);
                        condition1.setNodeId(statusConfig.getActionId());
                        //���ò�ѯ��actionNum ��null
                        condition1.setIsnull(true);
                        //��ѯ������condition1�£���ѯ��ǰ�ڵ��±�����Ϣ
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
                            tdscOpnn1.setResultName("�ݴ�");
                            // ��ʷ���еı��������Ϣ�ı�����޸�
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
                    //����˫��ӿڽ��������̿���
                    if (statusConfig.getSyFlowAction() != null) {
                        WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

                        Map map = new HashMap();
                        MessageData data = null;

                        //��ȡ������˫��ʵ��ID
                        String instanceId = this.commonFlowService.getAppInstanceId(appId, transferMode, tdscApp.getNodeId());

                        WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

                        List workItems = new ArrayList();
                        //��ȡ˫��������ʵ��
                        if (!signTag) {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, false);
                        } else {
                            workItems = workFlowWorkList.wmGetWorkItems(GlobalConstants.APPLICATION_ID, appId, userId, null, true);
                        }

                        WorkItem workItem = new WorkItem();
                        if (workItems != null && workItems.size() > 0) {
                            workItem = (WorkItem) workItems.get(0);
                        }

                        //��ȡ�����̵�ǰ������
                        int currentOrderNum = workItem.getCurrentOrderNum();

                        //ǩ������
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
     * ����Ĭ�ϲ�������ݴ�����û�ѡ��Ĳ���������²������
     * 
     * @param appId
     *            ҵ��Id
     * @param transferMode
     *            ���÷�ʽ
     * @param user
     *            ϵͳ�û�
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
     * ��ѯ�ڵ��Ӧ������ҵ������б���Ӧ��˫�����̵Ľڵ㣩
     * 
     * @param nodeId
     *            �ڵ�Id
     * @param user
     *            ϵͳ�û�
     * @param actionIdList
     *            ����б�
     * @return List ҵ���б�
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
        //��������ҵ����ѯ�����б�---��ǩ�չ���
        TdscAppCondition appCondition = new TdscAppCondition();
        appCondition.setNodeId(nodeId);
        appCondition.setStatusId(statusId);
        appCondition.setAppResult(APP_RESULT_TEMP);
        List tdscAppList = tdscAppDao.findAppList(appCondition);
        if (tdscAppList != null && tdscAppList.size() > 0) {
       	 returnList = new ArrayList();
            for (int i = 0; i < tdscAppList.size(); i++) {
            	TdscApp tdscApp = (TdscApp)tdscAppList.get(i);
                logger.debug("======����appid======"+tdscApp.getTdscAppId());
           	 returnList.add(tdscApp.getTdscAppId());
            }
       }
        **/
//      ��������ҵ����ѯ�����б�---����ǩ�չ���
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
        logger.debug("======��ʼ����˫�����������ô����б�======");

        List workList = null;
        try {
            WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();
            workList = workFlowWorkList.wmGetWorkList(GlobalConstants.APPLICATION_ID, userId); //��ȡ�����г�Ӧ�ö�Ӧ��ȫ������ҵ��
        } catch (Exception e) {
            logger.warn(e);
        }

        long end = System.currentTimeMillis();
        logger.debug("======��������˫�����������ô����б�����ʱ" + (end - begin) + "����======");

        List returnList = null;

        if (workList != null && workList.size() > 0) {
            returnList = new ArrayList();
            for (int i = 0; i < workList.size(); i++) {
                WorkItem workItem = (WorkItem) workList.get(i);
                String appId = workItem.getBusinessId();
                String businessType = workItem.getBusinessType();

                if (businessType.equals(subFlowId)) { //����ѯ����ҵ������������Ϊ����ڵ��Ӧ����ID��ҵ����ӵ������б���
                    returnList.add(appId);
                }
            }
        }
         **/
        if(returnList ==null || returnList.size() ==0) return null;
        return returnList;
    }

    /**
     * ��ѯ���ҳ����Ϣ
     * 
     * @param appId
     *            ҵ��Id
     * @return Map ���ҳ����Ϣ����
     */
    public Map queryOpnnInfo(String appId) throws Exception {
        return this.queryOpnnInfo(appId, null);
    }

    /**
     * ���ڵ��ѯ���ҳ����Ϣ
     * 
     * @param appId
     *            ҵ��Id
     * @param nodeId
     *            �ڵ�Id
     * @return Map ���ҳ����Ϣ����
     */
    public Map queryOpnnInfo(String appId, String nodeId) throws Exception {
        Map returnMap = new HashMap();
        TdscOpnnCondition condition = new TdscOpnnCondition();
        condition.setAppId(appId);

        List resultList = this.queryResultList(condition); //��ѯ������һ������Ӧ��ȫ���������
        TdscOpnn tempOpnn = this.queryWaitingOpnnInfo(condition); //��ѯ�������һ������Ϣ

        if (nodeId != null) {
            condition.setNodeId(nodeId);
        }
        List opnnList = this.queryAppOpnnList(condition); //��ѯ��ʷ�������

        returnMap.put("opnnList", opnnList);
        returnMap.put("resultList", resultList);
        returnMap.put("tempOpnn", tempOpnn);

        return returnMap;
    }

    /**
     * ��ȡ��һ������˳���
     * 
     * @param appId
     *            ҵ��Id
     * @return BigDecimal ��һ������
     */
    private BigDecimal getNextActionNum(String appId) {
        return tdscOpnnDao.getNextActionNum(appId);
    }

    /**
     * ����ɾ��������Ϣ
     * 
     * @param appId
     *            ҵ��Id
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
                tdscOpnnDao.delete(opnn); //����ʷ����ȫ��ɾ��
            }
        }
    }
    
    /**
     * ��ѯ˫��������Ϣ
     * 
     * @param appId
     *            ҵ��Id
     * @param userId
     *            �û�Id
     * @return WorkItem ˫��������Ϣ
     * 
     */
    /**
    public WorkItem getAppFlowInfo(String appId, String userId) {
        TdscOpnn tdscOpnn = new TdscOpnn();
        TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
        opnnCondition.setAppId(appId);
        opnnCondition.setIfOpt(IS_OPT_FALSE);
        tdscOpnn = tdscOpnnDao.findOpnnInfo(opnnCondition); //��ѯ�����Ӧ����һ����
        WorkItem workItem = new WorkItem();
        if (tdscOpnn != null) {
            boolean signTag = false;
            if (tdscOpnn.getActionUser() != null) {
                signTag = true; //�ж��û��Ƿ���ǩ��
            }
            WorkFlowProcessInterface workFlowProcess = WorkFlowManager.getWorkFlowProcessInstance();

            WorkFlowWorkListInterface workFlowWorkList = WorkFlowManager.getWorkFlowWorkListInterface();

            List workItems = new ArrayList();
            //��ѯ�����Ӧ��˫������ʵ��
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