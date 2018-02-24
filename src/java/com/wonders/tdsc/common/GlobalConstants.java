package com.wonders.tdsc.common;

import com.wonders.esframework.FrameworkConstants;

/**
 * 该类定义全局常量。
 */

public class GlobalConstants {

    /** *****************应用代码************************** */

    public static final int APPLICATION_ID = 18;

    /** ******** 定义字典编号 ********* */

    /** 是否 */
    public static final int DIC_ID_YESNO = 18101;
 
    /** 性别 */
    public static final int DIC_ID_SEX = 18102;

    /** 有效性 */
    public static final int DIC_ID_VALIDITY = 18103;

    /** 计划字典 */
    public static final int DIC_ID_PLAN = 18104;

    /** 任务字典 */
    public static final int DIC_ID_TASK = 18105;

    /** 任务计划状态 */
    public static final int DIC_SCHEDULER_STATUS = 18106;

    /** 区县 */
    public static final int DIC_DISTRICT = 18217;

    /** 交易方式 */
    public static final int DIC_BLOCK_TRANSFER = 18201;

    /** 土地类型 */
    public static final int DIC_BLOCK_TYPE = 18202;

    /** 地块状态 */
    public static final int DIC_BLOCK_STATUS = 18230;

    /** 工业土地类型 */
    public static final int DIC_BLOCK_TYPE_INDUSTRY = 101;

    /** 商业土地类型 */
    public static final int DIC_BLOCK_TYPE_COMMERCE = 102;

    /** 到账情况 */
    public static final int DIC_ID_BIDDERDZQK = 18203;

    /** 竞买人属性_境内 */
    public static final int DIC_ID_BIDDER_PRO_JN = 18204;

    /** 竞买人属性_境外 */
    public static final int DIC_ID_BIDDER_PRO_JW = 18205;    

    /** 申请签章类型 */
    public static final int DIC_ID_APP_STAMP_TYPE = 18206;

    /** 提交材料-工业性用地 */
    public static final int DIC_ID_TJCL_GYX = 18208;

    /** 提交村料-经营性用地 */
    public static final int DIC_ID_TJCL_JYX = 18207;

    /** 提交材料-收件类别 */
    public static final int DIC_ID_TJCL_TYPE = 18209;

    /** 竞买方式 */
    public static final int DIC_ID_BIDDER_TYPE = 18210;

    /** 窗口业务类型 */
    public static final int DIC_ID_BUSINESS_RECORD_TYPE = 18211;

    /** ****配套条件********* */
    public static final int DIC_ID_EQUIP_CONDITION = 18212;

    /** 答疑会-问题关键字 */
    public static final int DIC_ID_QUESTION_KEY = 18213;

    /** 抽选阶段 */
    public static final int DIC_ID_SELECT_STAGE = 18215;

    /** 抽选类型 */
    public static final int DIC_ID_SELECT_TYPE = 18216;

    /** 区县类型 */
    public static final int DIC_ID_DISTRICT_TYPE = 18217;

    /** 复杂区县类型 */
    public static final int DIC_ID_DISTRICT_COMPLEX_TYPE = 18218;

    /** 拍卖 */
    public static final int DIC_ID_HOUSE_PM = 18219;

    /** 挂牌竞价 */
    public static final int DIC_ID_HOUSE_GP = 18220;

    /** 投、开、定标会 */
    public static final int DIC_ID_HOUSE_TKDB = 18221;

    /** 评标会 */
    public static final int DIC_ID_HOUSE_PB = 18222;

    /** 现场交易失败原因 */
    public static final int DIC_ID_AUCTION_FAILED_REASON = 18223;
    
    /** 现场交易失败原因_招标 */
    public static final int DIC_ID_AUCTION_FAILED_REASON_ZB = 18246;
    
    /** 现场交易失败原因_挂牌，拍卖 */
    public static final int DIC_ID_AUCTION_FAILED_REASON_PG = 18247;
    
    /** 公证处名称信息*/
    public static final int DIC_ID_NOTARY_NAME = 18248;
    
    /** 公证处联系人*/
    public static final int DIC_ID_NOTARY_LINKMAN = 18249;
    
    /** 公证处联系电话*/
    public static final int DIC_ID_NOTARY_LINKMOBILE = 18250;
    
    /** 土地供地方式*/
    public static final int DIC_ID_GONGDI_TYPE = 18251;
    
    /** 土地供地方式,默认值为：招拍挂出让（02）*/
    public static final int DEFAULT_GONGDI_TYPE = 02;

    /** 机审结果 */
    public static final int DIC_ID_REVIEW_RESULT = 18224;

    /** 受托人证件类型 */
    public static final int DIC_ID_WTR_ZJLX = 18225;

    /** 竞买/投标人证件类型 法人 */
    public static final int DIC_ID_BIDDER_ZJLX_FR = 18226;

    /** 房地产开发资质等级 */
    public static final int DIC_ID_FANGDI_KFZZDJ = 18227;

    /** 资信等级 */
    public static final int DIC_ID_FANGDI_ZXDJ = 18228;

    /** 近5年类似楼盘开发经验 */
    public static final int DIC_ID_FANGDI_LPKFJY = 18229;

    /** 规划用途 */
    public static final int DIC_ID_PLAN_USE = 18231;

    /** 复杂规划用途 */
    public static final int DIC_ID_PLAN_COMPLEX_USE = 18232;

    /** 区县房地局 */
    public static final int DIC_ID_DISTRICT_ORGAN = 18233;

    /** 交易结果 */
    public static final int DIC_ID_TRANSFER_RESULT = 18234;

    /** 挂牌结果 */
    public static final int DIC_ID_LISTING_RESULT = 18235;

    /** 现场竞价结果 */
    public static final int DIC_ID_SCENE_RESULT = 18236;

    /** 现状条件 */
    public static final int DIC_ID_BLOCK_ACTUAL_CONDITION = 18237;

    /** 准入产业类型 */
    public static final int DIC_ID_BLOCK_DOMAIN_PERMIT = 18238;

    /** 现场竞价方式 */
    public static final int DIC_ID_LOCAL_TRADE_TYPE = 18239;

    /** 招标类型 */
    public static final int DIC_ID_INVITE_TYPE = 18240;

    /** 被抽选对象答复状态 */
    public static final int DIC_ID_REPLY_STATUS = 18241;

    /** 修改抽选答复状态 */
    public static final int DIC_ID_MODIFY_REPLY_STATUS = 18242;

    /** 专家类别 */
    public static final int DIC_ID_SPECIALIST_TYPE = 18243;
    
    /** 竞买人属性 */
    public static final int DIC_ID_BIDDER_PRO = 18244;
    
    /** 数据交换任务 */
    public static final int DIC_DATA_TRANSFER_TASK = 18245;

    /** 流程动作 */
    public static final int DIC_ID_FLOW_ACTION = 18301;

    /** 流程状态 */
    public static final int DIC_ID_FLOW_STATUS = 18302;

    /** 流程节点 */
    public static final int DIC_ID_FLOW_NODE = 18303;

    /** 节点状态 */
    public static final int DIC_ID_NODE_STAT = 18304;
    
    /** 发文单位 */
    public static final int DIC_MATERIAL_ORGAN = 18305;  
    
    /** 入室审核当前状态 */
    public static final int DIC_BLOCK_AUDIT_STATUS_ID = 18306; 
    
    /** 协办机构批次状态 */
    public static final int DIC_ORG_INFO_STATUS = 18307;
    
    /** 发布范围 */
    public static final int DIC_PUBLISH_RANGE = 18252; 
    
    /** 所属区县 */
    public static final int DIC_DISTRICT_BELONG = 18253;

    /** 配建类型 */
    public static final int DIC_DISTRICT_PJLX = 18254;

    /** *****************按钮权限************************** */

    /** ****新建入市审核********* */
    public static final String BUTTON_ID_NEW_AUDIT = "1820001";

    /** ****问题信息录入********* */
    public static final String BUTTON_ID_QUESTION_WRITE = "1820002";

    /** ****问题信息转发********* */
    public static final String BUTTON_ID_QUESTION_TRANSMIT = "1820003";

    /** ****问题信息归类********* */
    public static final String BUTTON_ID_QUESTION_CLASSIFY = "1820004";

    /** ****竞买申请窗口受理********* */
    public static final String BUTTON_ID_JMSQCKSL = "1820005";

    /** ****竞买申请竞买人管理********* */
    public static final String BUTTON_ID_JMSQJMRGL = "1820006";

    /** ****竞买申请补卡换卡********* */
    public static final String BUTTON_ID_JMSQBKHK = "1820007";

    /** ****竞买人到帐管理记录********* */
    public static final String BUTTON_ID_JMRDZGLJL = "1820008";

    /** ****竞买人到帐管理打印********* */
    public static final String BUTTON_ID_JMRDZGLDY = "1820009";

    /** ****竞买人到帐管理机审结果查询********* */
    public static final String BUTTON_ID_JMRJSJGCX = "1820010";

    /** ****制定进度安排表********* */
    public static final String BUTTON_ID_SCHEDULE_MAKE = "1820011";

    /** ****进度安排表日期管理********* */
    public static final String BUTTON_ID_SCHEDULE_DATE_MODIFY = "1820012";

    /** ****进度安排表场次管理********* */
    public static final String BUTTON_ID_SCHEDULE_FIELD_MODIFY = "1820013";

    /** ****专家抽选********* */
    public static final String BUTTON_ID_SELECT_SPECAILIST = "1820014";

    /** ****专家回复结果管理********* */
    public static final String BUTTON_ID_MANAGE_SPECAILIST_REPLY = "1820015";

    /** ****受理部总受理********* */
    public static final String BUTTON_ID_JMSQZSL = "1820016";

    /** ****审核实施方案********* */
    public static final String BUTTON_ID_SCHEDULE_SHENHE = "1820017";
    
    /** ****土地招拍挂系统管理员********* */
    public static final String BUTTON_ID_TDSC_ADMIN = "1820018";
    
    /** *****************流程权限************************** */
    
    /** ****制作出让文件********* */
    public static final String FLOW_ID_MAKE_FILE = "0301";
    
    /** ****制作出让公告********* */
    public static final String FLOW_ID_CHECK_FILE = "0302";
    /** *****************流程权限************************** */
    
    /** *****************地块状态************************** */

    /** ****未交易********* */
    public static final String DIC_ID_STATUS_NOTTRADE = "00";

    /** ****交易中********* */
    public static final String DIC_ID_STATUS_TRADING = "01";

    /** ****交易成功********* */
    public static final String DIC_ID_STATUS_TRADESUCCESS = "02";

    /** ****交易失败********* */
    public static final String DIC_ID_STATUS_TRADEFAILURE = "03";

    /** ****交易中止********* */
    public static final String DIC_ID_STATUS_TRADEEND = "04";

    /** *****************交易结果************************** */

    /** ****交易中********* */
    public static final String DIC_TRANSFER_RESULT_INIT = "00";

    /** ****交易成功********* */
    public static final String DIC_TRANSFER_RESULT_SUCCESS = "01";

    /** ****交易失败********* */
    public static final String DIC_TRANSFER_RESULT_FAIL = "02";

    /** ****交易取消********* */
    public static final String DIC_TRANSFER_RESULT_CANCEL = "03";

    /** ****挂牌结束，转入现场竞价********* */
    public static final String DIC_LISTING_SCENE = "01";

    /** ****挂牌成功，不转入现场竞价********* */
    public static final String DIC_LISTING_SUCCESS = "02";

    /** ****挂牌失败，不转入现场竞价********* */
    public static final String DIC_LISTING_FAIL = "03";

    /** ****现场竞价成功********* */
    public static final String DIC_SCENE_SUCCESS = "01";

    /** ****现场竞价失败********* */
    public static final String DIC_SCENE_FAIL = "02";

    /** *****************现场竞价方式************************** */

    /** ****一翻一瞪眼********* */
    public static final String DIC_SCENE_TYPE_NO_CHANGE = "1";

    /** ****举牌竞价********* */
    public static final String DIC_SCENE_TYPE_CHANGE = "2";

    /** ******到账情况****************1000=无历史操作|1001=未足额到账|1002=逾期到账|1003=按期到账|1004=未到账 */
    /** 到账情况：无历史操作 */
    public static final String DIC_ID_BIDDERDZQK_NOACTION = "1000";

    /** 到账情况：未足额到账 */
    public static final String DIC_ID_BIDDERDZQK_SHORTAGE = "1001";

    /** 到账情况：逾期到账 */
    public static final String DIC_ID_BIDDERDZQK_OVERDUE = "1002";

    /** 到账情况：按期到账 */
    public static final String DIC_ID_BIDDERDZQK_ONTIME = "1003";

    /** 到账情况：未到账 */
    public static final String DIC_ID_BIDDERDZQK_NULL = "1004";

    /** *******机审结果************1=合格|0=不合格 */
    /** 机审结果：合格 */
    public static final String DIC_ID_REVIEW_RESULT_YES = "1";

    /** 机审结果：不合格 */
    public static final String DIC_ID_BREVIEW_RESULT_NO = "0";

    /** ******** 定义字典值 ********* */

    /** 字典值：是 */
    public static final String DIC_VALUE_YESNO_YES = FrameworkConstants.FLAG_YES; // "1"

    /** 字典值：否 */
    public static final String DIC_VALUE_YESNO_NO = FrameworkConstants.FLAG_NO; // "0";

    /** 字典值：有效 */
    public static final String DIC_VALUE_VALID_VALIDITY = FrameworkConstants.FLAG_VALIDITY; // "1";

    /** 字典值：无效 */
    public static final String DIC_VALUE_VALID_INVALIDITY = FrameworkConstants.FLAG_INVALIDITY; // "0";

    /** ******** 定义出让方式字典值 ********* */

    /** 出让方式：招标 */
    public static final String DIC_TRANSFER_TENDER = "3107";

    /** 出让方式：拍卖 */
    public static final String DIC_TRANSFER_AUCTION = "3103";

    /** 出让方式：挂牌 */
    public static final String DIC_TRANSFER_LISTING = "3104";

    /** ******** 定义文件上传下载路径 ********* */

    /** 出让文件上传 */
    public static final String UPLOAD_BLOCK_FILE = "tdscUploadBlockFile";

    /** 出让文件下载 */
    public static final String DOWNLOAD_BLOCK_FILE = "tdscDownloadBlockFile";

    /** 分析报告上传 */
    public static final String UPLOAD_ANALYSIS_REPORT = "tdscUploadAnalysisReport";

    /** 分析报告下载 */
    public static final String DOWNLOAD_ANALYSIS_REPORT = "tdscDownloadAnalysisReport";

    /** 出让公告上传 */
    public static final String UPLOAD_BLOCK_NOTICE = "tdscUploadBlockNotice";

    /** 出让公告下载 */
    public static final String DOWNLOAD_BLOCK_NOTICE = "tdscDownloadBlockNotice";
    
    /**双阳WebService调用地址*/
    public static final String WebServiceInvokeEndpoint = "webServiceInvokeEndpoint";

    /** 答疑会文件上传 */
    public static final String UPLOAD_REPLY_FILE = "tdscUploadReplyFile";

    /** 答疑会文件下载 */
    public static final String DOWNLOAD_REPLY_FILE = "tdscDownloadReplyFile";

    /** 用地批文和供地方案接受文件夹 */
    public static final String DIRECTORY_RECEIPT_AUDITED = "tdscReceiptAuditedDoc";

    /** 用地批文和供地方案下载文件夹 */
    public static final String DOWNLOAD_DIRECTORY_RECEIPT_AUDITED = "tdscDownloadReceiptAuditedDoc";

    public static final String LAND_AUDITED_PREFIX = "land_audited";

    public static final String SUPPLY_CASE_PREFIX = "supply_case";

    /** ******** 定义查询标志常量 ********* */

    /** 查询已结束业务标志 */
    public static final String QUERY_ENDLESS_TAG = "TRUE";

    /** 不含补充条件查询业务标志 */
    public static final String QUERY_WITHOUTPLUS_TAG = "NO_PLUS";

    /** ******** 定义抽选状态 ********* */

    /** 状态：已选 */
    public static final String SELECT_DONE = "01";

    /** 状态：未选 */
    public static final String SELECT_NOT_DONE = "00";

    /** ******** 定义抽选结果有效性 ********* */

    /** 状态：有效 */
    public static final String SELECT_RESULT_VALID = "01";

    /** 状态：无效 */
    public static final String SELECT_RESULT_NOT_VALID = "00";

    /** ******** 定义进度安排表状态 ********* */

    /** 状态：激活有效 */
    public static final String PLAN_TABLE_ACTIVE = "01";

    /** 状态：历史版本 */
    public static final String PLAN_TABLE_HISTORY = "00";

    /** 状态：全部 */
    public static final String PLAN_TABLE_ALL = "ALL";

    /** ******** 定义应用配置代码信息 ********* */

    /** 默认分页记录数 */
    public static final String PROPERTY_DEFAULT_PAGE_NUMBER = "tdscDefaultPageNumber";

    /** 页面title显示的文字信息 */
    public static final String PROPERTY_PAGE_TITLE_STRING = "tdscPageTitleStr";

    /** 用户账户锁定的连续登录次数 */
    public static final String PROPERTY_USER_lOCK_LOGON_TIMES = "tdscUserLockLogonTimes";

    /** 用户账户锁定后的自动解锁分钟数 */
    public static final String PROPERTY_USER_UNLOCK_MINUTES = "tdscUserUnlockMinutes";

    /** 短信通知平台访问地址 */
    public static final String PROPERTY_SHORTMESSAGE_URL = "tdscSmsUrl";
    
    /**专家短信发送信息*/
    public static final String PROPERTY_SHORTMESSAGE_SPECIALIST = "tdscSpecialistMessage";
    
    /**公证处短信发送信息**/
    public static final String PROPERTY_SHORTMESSAGE_NOTARY = "tdscNotaryMessage";

    /** ******** 定义 Struts Global Forwards 名称 ********* */

    /** Struts Action中定义的错误页面名称 */
    public static final String FORWARD_PAGE_ERROR = "tdscError";

    /** Struts Action中定义的错误页面名称 */
    public static final String FORWARD_PAGE_LOGIN = "tdscLogin";

    /** ******** 定义 Session 中属性名称 ********* */

    /** Session中登录用户的用户信息 */
    public static final String SESSION_USER_INFO = "logonUserInfoInSession";

    /** Session中NK登录用户的用户信息 */
    public static final String SESSION_NK_USER_INFO = "user";

    /** Session中登录用户的菜单权限信息列表 */
    public static final String SESSION_USER_AUTHORITY_MENU = "userAuthorityMenuInSession";

    /** Session中登录用户的工作流权限信息列表 */
    public static final String SESSION_USER_WORK_FLOW = "userWorkFlowInSession";

    /** Session中登录用户的区县信息列表 */
    public static final String SESSION_USER_QX = "userQxInSession";

    /** Session中登录用户的按钮信息列表 */
    public static final String SESSION_USER_BUTTON_MAP = "userButtonMapInSession";

    /** ******** 定义 HttpServletRequest 中属性名称 ********* */

    /** 传递到JSP页面的错误信息属性名称 */
    public static final String REQUEST_ERROR_MESSAGE = "errorMessageInReqeust";

    public static final String REQUEST_ERROR_REAL_REASON = "errorRealReasonMessageInReqeust";

    /** *********定义业务记录(TDSC_BUSINESS_RECORD)类型*************** */
    // 申请材料发放
    public static final String RECORD_BIDDER_MATERIALS = "1001";

    // 竞买申请受理
    public static final String RECORD_BIDDER_APPLY = "1002";

    /** ==================自动编号生成前缀==================== */
    // 公告号
    public static final String INCREMENT_ID_NOTICE = "NOTICE";

    // 土地公告号
    public static final String INCREMENT_ID_BLOCKNOTICE = "BLOCK_NOTICE";

    // 竞买人申请材料编号
    public static final String INCREMENT_ID_BIDDER_MATER = "BIDDER_MATER";

    // 申请材料受理编号
    public static final String INCREMENT_ID_ACCEPT_NUMBER = "ACCEPT_NUMBER";

    // 竞买人受理号
    public static final String INCREMENT_ID_BIDDER_APP = "BIDDER_APP";
    
    /** 发布到网站,大屏,门禁,所要用到的代码*/
    
    /**地块业务流程信息*/
    public static final String PUB_SCREEN_BLOCKINFOFLOW = "001";
    
    /**领取材料数和竞买人数信息*/ 
    public static final String PUB_SCREEN_BLOCKBIDDERNUMBER = "002";
    
    /**大屏公告信息*/ 
    public static final String PUB_SCREEN_NOTICENUMBERlIST = "003";
    
    /**现场竞价直播*/
    public static final String PUB_SCREEN_TRADERESULT = "004";
    
    /**挂牌信息*/
    public static final String PUB_SCREEN_BLOCKLISTING = "005";
    
    /**挂牌当前最新报价*/
    public static final String PUB_SCREEN_CURRENTLISTINGINFO = "006";
    
    /**挂牌结束后信息*/
    public static final String PUB_SCREEN_ENDBLOCKLISTING = "007";
    
    /**会场安排信息*/
    public static final String PUB_SCREEN_BLOCKMEETING = "008";
    
    /**公告信息*/
    public static final String PUB_WEBSITE_NOTICEINFO = "101";
    
    /**地块信息*/
    public static final String PUB_WEBSITE_BLOCKINFO = "102";
    
    /**进度安排表信息*/
    public static final String PUB_WEBSITE_PLANINFO = "103";
    
    /**实时交易信息*/
    public static final String PUB_WEBSITE_REALTIMEINFO = "104";
    
    /**答疑信息*/
    public static final String PUB_WEBSITE_ANSWERINFO = "105";
    
    /**出让文件*/
    public static final String PUB_WEBSITE_YXSCCRWJ = "106";
    
    /**实时交易信息*/
    public static final String PUB_WEBSITE_TDGLGGJG = "107";
    
    /**答疑信息*/
    public static final String PUB_WEBSITE_YXSCDKJG = "108";  
    
    

    /** ******** 定义固定活动场地 ********* */

    /** 挂牌场地 */
    public static final int LIST_MEETING_POSITION = 1003;
    
    /**土地用途类型**/
    public static final  int DIC_LAND_CODETREE_T = 18107;
    
    /**字地块用途类型**/
    public static final  int DIC_CHILDBLOCK_USED_TYPE = 18108;
    
    /** 统计用途 */
    public static final int DIC_COUNT_BLOCK_TYPE = 18308;
    
    /** 土地类型 */
    public static final int DIC_BLOCK_CATALOG = 18309;
    
    /** 号牌号 */
    public static final int DIC_CON_NUM = 18310;
    
    /** 评估单位 */
    public static final int DIC_EVALUATE_UNIT = 18311;
    
    /** 评估方法 */
    public static final int DIC_EVALUATE_MEANS = 18312;
    
    /** 数学符号 */
    public static final int DIC_MATHEMATICS_SIGN = 18313;
    
    /** 保证金账户 */
    public static final int DIC_BANK = 18314;
    
    /** *******交易方式************1=网上交易|0=现场交易 */
    public static final int DIC_IF_ON_LINE = 18315;
    
    /** 地质灾害评估单位 */
    public static final int DIC_GEOLOGY_ASSESS_UINT = 18316;
    
    /** 推荐图 */
    public static final String PRESELL_TYPE_TJ = "01";
    /** 预公告附件 */
    public static final String PRESELL_FILE = "02";
    /**地块附件**/
    public static final String BLOCK_FILE="03";
    /**基准地价附件**/
    public static final String JZDJ_FILE = "04";
    /**联合竞买附件**/
    public static final String LHJM_FILE = "05";
    /**竞买申请附件**/
    public static final String JMSQ_FILE = "06";
    /**公司成立附件**/
    public static final String GSCL_FILE = "07";
    
    /** *******竞买类型************ */
    /**单独竞买**/
    public static final String BIDDER_TYPE_SINGLE = "1";
    /**联合竞买**/	
    public static final String BIDDER_TYPE_UNION = "2";
    
    
    /** 公告资源交易平台接口 */
    /** 成交行为信息 */
    public static final String TRADE_RESULT_INFO = "tradeResultInfo";
    /** 成交宗地信息 */
    public static final String RESULT_BLOCK_INFO = "resultBlockInfo";
    /** 附件集结构 */
    public static final String TRADE_MATERIALS = "tradeMaterials";
    
    /** 文件保存地址 */
    public static final String XML_PATH = "E:/AttachXML/";
    /** 压缩文件保存地址 */
    public static final String ZIP_PATH = "E:/AttachFiles/";
    
    /** 接口序列号 */
    public static final String SERIAL_INTERFACE = "serialInterface";
}
