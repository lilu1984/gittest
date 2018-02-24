package com.wonders.tdsc.flowadapter;


/**
 * ���ඨ��ȫ�ֳ�����
 */

public class FlowConstants {

    /** ******** �������̳��� ********* */

    /** ���÷�ʽ���б� */
    public static final String FLOW_TRANSFER_TENDER = "01";

    /** ���÷�ʽ������ */
    public static final String FLOW_TRANSFER_AUCTION = "02";

    /** ���÷�ʽ������ */
    public static final String FLOW_TRANSFER_LISTING = "03";

    /** ״̬-δ���� */
    public static final String STAT_INIT = "00";

    /** ״̬-��� */
    public static final String STAT_ACTIVE = "01";

    /** ״̬-�ѹر� */
    public static final String STAT_END = "02";

    /** ״̬-����ֹ */
    public static final String STAT_TERMINATE = "03";

    /** �ڵ㣺�������Ҫ�ز��� */
    public static final String FLOW_NODE_AUDIT = "01";

    /** �ڵ㣺�ƶ����Ȱ��ű� */
    public static final String FLOW_NODE_SCHEDULE_PLAN = "02";

    /** �ڵ㣺�������ù�����ļ� */
    public static final String FLOW_NODE_FILE_MAKE = "03";

    /** �ڵ㣺�������� */
    public static final String FLOW_NODE_FILE_RELEASE = "04";

    /** �ڵ㣺��ȡ�����ļ�������� */
    public static final String FLOW_NODE_FILE_GET = "05";

    /** �ڵ㣺�ֳ������ */
    public static final String FLOW_NODE_PREVIEW = "06";

    /** �ڵ㣺�ռ����� */
    public static final String FLOW_NODE_QUESTION_GATHER = "07";

    /** �ڵ㣺���ɻ� */
    public static final String FLOW_NODE_ANSWER = "08";

    /** �ڵ㣺�������ɼ�Ҫ */
    public static final String FLOW_NODE_FAQ_RELEASE = "09";

    /** �ڵ㣺���������� */
    public static final String FLOW_NODE_BIDDER_APP = "10";

    /** �ڵ㣺�ʸ����� */
    public static final String FLOW_NODE_BIDDER_REVIEW = "11";

    /** �ڵ㣺Ͷ�� */
    public static final String FLOW_NODE_BID = "12";

    /** �ڵ㣺����/����� */
    public static final String FLOW_NODE_BID_OPENNING_APPROVAL = "13";

    /** �ڵ㣺����� */
    public static final String FLOW_NODE_BID_EVA = "14";

    /** �ڵ㣺������ */
    public static final String FLOW_NODE_AUCTION = "15";

    /** �ڵ㣺���� */
    public static final String FLOW_NODE_LISTING = "16";

    /** �ڵ㣺�����ֳ����� */
    public static final String FLOW_NODE_LISTING_SENCE = "17";

    /** �ڵ㣺�����ʾ */
    public static final String FLOW_NODE_RESULT_SHOW = "18";

    /** �ڵ㣺B�๫֤����ѡ(�б�) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_TENDER = "19";

    /** �ڵ㣺B�๫֤����ѡ(����) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_AUCTION = "20";

    /** �ڵ㣺B�๫֤����ѡ(����) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_LISTING = "21";

    /** �ڵ㣺C�๫֤����ѡ */
    public static final String FLOW_NODE_SELECT_C_NOTARY = "22";

    /** �ڵ㣺����ר�ҳ�ѡ */
    public static final String FLOW_NODE_SELECT_SPECAILIST = "23";

    /** �ڵ㣺�����˳�ѡ(�б�) */
    public static final String FLOW_NODE_SELECT_COMPERE_TENDER = "24";

    /** �ڵ㣺�����˳�ѡ(����) */
    public static final String FLOW_NODE_SELECT_COMPERE_AUCTION = "25";

    /** �ڵ㣺�����˳�ѡ(����) */
    public static final String FLOW_NODE_SELECT_COMPERE_LISTING = "26";

    /** �ڵ㣺�ʽ��˹���(�б�) */
    public static final String FLOW_NODE_BIDDER_FUND_TENDER = "27";

    /** �ڵ㣺�ʽ��˹���(����) */
    public static final String FLOW_NODE_BIDDER_FUND_AUCTION = "28";

    /** �ڵ㣺�ʽ��˹���(����) */
    public static final String FLOW_NODE_BIDDER_FUND_LISTING = "29";
    
    /** �ڵ㣺���׽��� */
    public static final String FLOW_NODE_FINISH = "90";

    /** ״̬�������� */
    public static final String FLOW_STATUS_AUDIT_INIT = "0101";
    
    /** ״̬�������� */
    public static final String FLOW_STATUS_AUDIT_ACCEPT = "0102";
    
    /** ״̬������� */
    public static final String FLOW_STATUS_AUDIT_VERIFY = "0103";  
    
    /** ״̬�������� */
    public static final String FLOW_STATUS_AUDIT_ENDORSE = "0104"; 
    
    /** ״̬�������� */
    public static final String FLOW_STATUS_AUDIT_RECIEVE = "0105"; 
    
    /** ״̬���˻��޸� */
    public static final String FLOW_STATUS_AUDIT_MODIFY = "0106";
    
    /** ״̬�������� */
    public static final String FLOW_STATUS_AUDIT_INITTRY = "0107";
    
    /** ״̬�����ƶ����Ȱ��ű� */
    public static final String FLOW_STATUS_SCHEDULETABLE_MAKE = "0201";
    
    /** ״̬�������������ļ� */
    public static final String FLOW_STATUS_FILE_MAKE = "0301";
    
    /** ״̬���������ù��� */
    public static final String FLOW_STATUS_FILE_VERIFY = "0302";
    
    /** ״̬����˳��ù��� */
    public static final String FLOW_STATUS_NOTICE_MAKE = "0303";
    
    /** ״̬���˻����Ƴ����ļ� */
    public static final String FLOW_STATUS_FILE_MODIFY = "0304";
    
    /** ״̬���˻����Ƴ��ù��� */
    public static final String FLOW_STATUS_NOTICE_MODIFY = "0305";
    
    /** ״̬�����������ù��� */
    public static final String FLOW_STATUS_NOTICE_PUBLISH = "0401";
    
    /** ״̬����¼�뿱�����Ϣ */
    public static final String FLOW_STATUS_PREVIEW_WRITE = "0601";
    
    /** ״̬����¼������ */
    public static final String FLOW_STATUS_QUESTION_WRITE = "0701";
    
    /** ״̬����ת������ */
    public static final String FLOW_STATUS_QUESTION_TRANSMIT = "0702";
    
    /** ״̬������������ */
    public static final String FLOW_STATUS_QUESTION_CLASSIFY = "0703";
    
    /** ״̬����¼�������Ϣ */
    public static final String FLOW_STATUS_ANSWER_WRITE = "0801";
    
    /** ״̬����¼�뿪���� */
    public static final String FLOW_STATUS_BID_OPENNING_WRITE = "1301";
    
    /** ״̬�������ƣ������ᣩ */
    public static final String FLOW_STATUS_AUCTION_CHANGE = "1501";
    
    /** ״̬����¼��������� */
    public static final String FLOW_STATUS_AUCTION_RESULT = "1502";
    
    /** ״̬����ȷ�Ϲ��ƽ�� */
    public static final String FLOW_STATUS_LISTING_CONFIRM = "1601";
    
    /** ״̬����¼�뽻�׽�� */
    public static final String FLOW_STATUS_LISTING_RESULT = "1602";
    
    /** ״̬�������ƣ������ֳ����ۣ� */
    public static final String FLOW_STATUS_LISTING_SENCE_CHANGE = "1701";
    
    /** ״̬����¼������ֳ����۽�� */
    public static final String FLOW_STATUS_LISTING_SENCE_RESULT = "1702";
     
    /** ״̬�������ʾ */
    public static final String FLOW_STATUS_RESULT_SHOW = "1801";
    
    /** ״̬��������ֹ */
    public static final String FLOW_STATUS_END_TRADE = "9004";
    
    /** ��������ƽ�������ӡ�ܷⱨ�۵� */
    public static final String FLOW_LISTING_RESULT_SENCE_PRINT = "160103";
    
    /** ��������ƽ�������ת���ֳ����� */
    public static final String FLOW_LISTING_RESULT_NO_SENCE = "160101";
    
    /** ��������ƽ������������ */
    public static final String FLOW_LISTING_RESULT_SENCE_CHANGE = "160102";
    
    /** �����ȡ������ */
    public static final String FLOW_AUDIT_RESULT_CANCEL_APP = "010102";
    
    /** ͳһ��������business_code�����н��� */
    public static final String FLOW_BUSINESS_CODE_AUDIT = "TDSC_AUDIT";

    /** ͳһ��������business_code�����Ȱ��ű����� */
    public static final String FLOW_BUSINESS_CODE_FILE = "TDSC_FILE";
}
