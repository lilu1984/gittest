package com.wonders.tdsc.flowadapter;


/**
 * 该类定义全局常量。
 */

public class FlowConstants {

    /** ******** 定义流程常量 ********* */

    /** 出让方式：招标 */
    public static final String FLOW_TRANSFER_TENDER = "01";

    /** 出让方式：拍卖 */
    public static final String FLOW_TRANSFER_AUCTION = "02";

    /** 出让方式：挂牌 */
    public static final String FLOW_TRANSFER_LISTING = "03";

    /** 状态-未启动 */
    public static final String STAT_INIT = "00";

    /** 状态-活动中 */
    public static final String STAT_ACTIVE = "01";

    /** 状态-已关闭 */
    public static final String STAT_END = "02";

    /** 状态-已中止 */
    public static final String STAT_TERMINATE = "03";

    /** 节点：接收审核要素材料 */
    public static final String FLOW_NODE_AUDIT = "01";

    /** 节点：制订进度安排表 */
    public static final String FLOW_NODE_SCHEDULE_PLAN = "02";

    /** 节点：制作出让公告和文件 */
    public static final String FLOW_NODE_FILE_MAKE = "03";

    /** 节点：发布公告 */
    public static final String FLOW_NODE_FILE_RELEASE = "04";

    /** 节点：获取出让文件和申请表 */
    public static final String FLOW_NODE_FILE_GET = "05";

    /** 节点：现场勘察会 */
    public static final String FLOW_NODE_PREVIEW = "06";

    /** 节点：收集提问 */
    public static final String FLOW_NODE_QUESTION_GATHER = "07";

    /** 节点：答疑会 */
    public static final String FLOW_NODE_ANSWER = "08";

    /** 节点：发布答疑纪要 */
    public static final String FLOW_NODE_FAQ_RELEASE = "09";

    /** 节点：受理竞买申请 */
    public static final String FLOW_NODE_BIDDER_APP = "10";

    /** 节点：资格审查会 */
    public static final String FLOW_NODE_BIDDER_REVIEW = "11";

    /** 节点：投标 */
    public static final String FLOW_NODE_BID = "12";

    /** 节点：开标/定标会 */
    public static final String FLOW_NODE_BID_OPENNING_APPROVAL = "13";

    /** 节点：评标会 */
    public static final String FLOW_NODE_BID_EVA = "14";

    /** 节点：拍卖会 */
    public static final String FLOW_NODE_AUCTION = "15";

    /** 节点：挂牌 */
    public static final String FLOW_NODE_LISTING = "16";

    /** 节点：挂牌现场竞价 */
    public static final String FLOW_NODE_LISTING_SENCE = "17";

    /** 节点：结果公示 */
    public static final String FLOW_NODE_RESULT_SHOW = "18";

    /** 节点：B类公证处抽选(招标) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_TENDER = "19";

    /** 节点：B类公证处抽选(拍卖) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_AUCTION = "20";

    /** 节点：B类公证处抽选(挂牌) */
    public static final String FLOW_NODE_SELECT_B_NOTARY_LISTING = "21";

    /** 节点：C类公证处抽选 */
    public static final String FLOW_NODE_SELECT_C_NOTARY = "22";

    /** 节点：评标专家抽选 */
    public static final String FLOW_NODE_SELECT_SPECAILIST = "23";

    /** 节点：主持人抽选(招标) */
    public static final String FLOW_NODE_SELECT_COMPERE_TENDER = "24";

    /** 节点：主持人抽选(拍卖) */
    public static final String FLOW_NODE_SELECT_COMPERE_AUCTION = "25";

    /** 节点：主持人抽选(挂牌) */
    public static final String FLOW_NODE_SELECT_COMPERE_LISTING = "26";

    /** 节点：资金到账管理(招标) */
    public static final String FLOW_NODE_BIDDER_FUND_TENDER = "27";

    /** 节点：资金到账管理(拍卖) */
    public static final String FLOW_NODE_BIDDER_FUND_AUCTION = "28";

    /** 节点：资金到账管理(挂牌) */
    public static final String FLOW_NODE_BIDDER_FUND_LISTING = "29";
    
    /** 节点：交易结束 */
    public static final String FLOW_NODE_FINISH = "90";

    /** 状态：待申请 */
    public static final String FLOW_STATUS_AUDIT_INIT = "0101";
    
    /** 状态：待受理 */
    public static final String FLOW_STATUS_AUDIT_ACCEPT = "0102";
    
    /** 状态：待审核 */
    public static final String FLOW_STATUS_AUDIT_VERIFY = "0103";  
    
    /** 状态：待审批 */
    public static final String FLOW_STATUS_AUDIT_ENDORSE = "0104"; 
    
    /** 状态：待接收 */
    public static final String FLOW_STATUS_AUDIT_RECIEVE = "0105"; 
    
    /** 状态：退回修改 */
    public static final String FLOW_STATUS_AUDIT_MODIFY = "0106";
    
    /** 状态：待初审 */
    public static final String FLOW_STATUS_AUDIT_INITTRY = "0107";
    
    /** 状态：待制定进度安排表 */
    public static final String FLOW_STATUS_SCHEDULETABLE_MAKE = "0201";
    
    /** 状态：待制作出让文件 */
    public static final String FLOW_STATUS_FILE_MAKE = "0301";
    
    /** 状态：制作出让公告 */
    public static final String FLOW_STATUS_FILE_VERIFY = "0302";
    
    /** 状态：审核出让公告 */
    public static final String FLOW_STATUS_NOTICE_MAKE = "0303";
    
    /** 状态：退回重制出让文件 */
    public static final String FLOW_STATUS_FILE_MODIFY = "0304";
    
    /** 状态：退回重制出让公告 */
    public static final String FLOW_STATUS_NOTICE_MODIFY = "0305";
    
    /** 状态：待发布出让公告 */
    public static final String FLOW_STATUS_NOTICE_PUBLISH = "0401";
    
    /** 状态：待录入勘察会信息 */
    public static final String FLOW_STATUS_PREVIEW_WRITE = "0601";
    
    /** 状态：待录入问题 */
    public static final String FLOW_STATUS_QUESTION_WRITE = "0701";
    
    /** 状态：待转发问题 */
    public static final String FLOW_STATUS_QUESTION_TRANSMIT = "0702";
    
    /** 状态：待归类问题 */
    public static final String FLOW_STATUS_QUESTION_CLASSIFY = "0703";
    
    /** 状态：待录入答疑信息 */
    public static final String FLOW_STATUS_ANSWER_WRITE = "0801";
    
    /** 状态：待录入开标结果 */
    public static final String FLOW_STATUS_BID_OPENNING_WRITE = "1301";
    
    /** 状态：待换牌（拍卖会） */
    public static final String FLOW_STATUS_AUCTION_CHANGE = "1501";
    
    /** 状态：待录入拍卖结果 */
    public static final String FLOW_STATUS_AUCTION_RESULT = "1502";
    
    /** 状态：待确认挂牌结果 */
    public static final String FLOW_STATUS_LISTING_CONFIRM = "1601";
    
    /** 状态：待录入交易结果 */
    public static final String FLOW_STATUS_LISTING_RESULT = "1602";
    
    /** 状态：待换牌（挂牌现场竞价） */
    public static final String FLOW_STATUS_LISTING_SENCE_CHANGE = "1701";
    
    /** 状态：待录入挂牌现场竞价结果 */
    public static final String FLOW_STATUS_LISTING_SENCE_RESULT = "1702";
     
    /** 状态：结果公示 */
    public static final String FLOW_STATUS_RESULT_SHOW = "1801";
    
    /** 状态：交易终止 */
    public static final String FLOW_STATUS_END_TRADE = "9004";
    
    /** 结果：挂牌结束，打印密封报价单 */
    public static final String FLOW_LISTING_RESULT_SENCE_PRINT = "160103";
    
    /** 结果：挂牌结束，不转入现场竞价 */
    public static final String FLOW_LISTING_RESULT_NO_SENCE = "160101";
    
    /** 结果：挂牌结束，换领号牌 */
    public static final String FLOW_LISTING_RESULT_SENCE_CHANGE = "160102";
    
    /** 结果：取消申请 */
    public static final String FLOW_AUDIT_RESULT_CANCEL_APP = "010102";
    
    /** 统一流程引擎business_code：入市交易 */
    public static final String FLOW_BUSINESS_CODE_AUDIT = "TDSC_AUDIT";

    /** 统一流程引擎business_code：进度安排表制作 */
    public static final String FLOW_BUSINESS_CODE_FILE = "TDSC_FILE";
}
