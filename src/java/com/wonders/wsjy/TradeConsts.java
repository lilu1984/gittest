package com.wonders.wsjy;
/**
 * 交易常量信息.
 * @author sunxin
 *
 */
public class TradeConsts {
	/**
	 * 发送地块最新报价信息.
	 */
	public static final String ORDER11 ="11";
	/**
	 * 发送一块地交易开始命令
	 */
	public static final String ORDER12 ="12";
	/**
	 * 发送一块地交易结束命令，进入间隔期
	 */
	public static final String ORDER13 ="13";
	/**
	 * 发送一块地交易结果命令---暂不用 信息放到13里*************
	 */
	public static final String ORDER14 ="14";
	/**
	 * 发送一块地交易全部完成命令
	 */
	public static final String ORDER15 ="15";
	/**
	 * 发送当前交易地块信息命令-----暂不用 信息放到13里***********
	 */
	public static final String ORDER16 ="16";
	/**
	 * 切换到交易大厅
	 */
	public static final String ORDER17 ="17";
	/**
	 * 现场挂牌结束
	 */
	public static final String ORDER18 ="18";
	/**
	 * 通知客户端初始化
	 */
	public static final String ORDER19 ="19";
	
	/**
	 * 客户端注册信息命令
	 */
	public static final String ORDER21 ="21";
	/**
	 * 客户端发送地块报价命令
	 */
	public static final String ORDER22 ="22";
	/**
	 * 客户端请求挂牌倒计时命令
	 */
	public static final String ORDER23 ="23";
	/**
	 * 客户端请求竞价倒计时命令
	 */
	public static final String ORDER24 ="24";
	/**
	 * 客户端请求服务器时间命令
	 */
	public static final String ORDER25 ="25";
	
	/**
	 * 向服务器初始化监控的noticeId
	 */
	public static final String ORDER26 ="26";
	
	/**
	 * 客户端向服务器发送底价回馈命令
	 */
	public static final String ORDER27 ="27";
	
	/**
	 * 服务器向客户端发送开始底价确认命令，客户端进入底价确认场景
	 */
	public static final String ORDER28 ="28";
	
	/**
	 * 向客户端发送底价等待倒计时
	 */
	public static final String ORDER29 ="29";
	
	/**
	 * 向客户端发送底价确认倒计时(底价确认中的倒计时)
	 */
	public static final String ORDER30 ="30";
	
	/**
	 * 客户端请求底价确认倒计时命令
	 */
	public static final String ORDER35 ="35";
	
	/**
	 * 服务器向客户端发送底价倒计时信息
	 */
	public static final String ORDER36 ="36";
	
	/**
	 * 底价确认阶段，提交或取消
	 */
	public static final String ORDER37 ="37";
	
	/**
	 * 系统当前时间
	 */
	public static final String ORDER31 ="31";
	/**
	 * 挂牌时间倒计时
	 */
	public static final String ORDER32 ="32";
	/**
	 * 交易地块倒计时
	 */
	public static final String ORDER33 ="33";
	
	/**
	 * 提示底价信息
	 */
	public static final String ORDER34 ="34";
	/**
	 * 向客户端发送已达到最高限价的信息
	 */
	public static final String ORDER38 = "38";
	/**
	 * 接收客户端选择是否参与摇号的信息
	 */
	public static final String ORDER39 = "39";
	/**
	 * 向客户端发送最新的参与摇号的号牌信息
	 */
	public static final String ORDER40 = "40";
	/**
	 * 系统提示消息
	 */
	public static final String ORDER61 ="61";
	/**
	 * 交易过程中错误信息和成功信息
	 */
	public static final String ORDER62 ="62";
	/**
	 * 用户另一个地点登陆，原先登陆账户将被迫下线
	 */
	public static final String ORDER63 ="63";
	
	/**
	 * 交易过程中间隔时间----20秒
	 */
	public static final int TRADE_INTERVAL_TIME = 20;

	/**
	 * 交易过程倒计时时间----240秒
	 */
	public static final int TRADE_LIMIT_TIME = 240;
	

	/**
	 * 未进入交易大厅(竞得和流拍)
	 */
	public static final String TRADE_OUTCOME = "0";

	/**
	 * 竞价交易中
	 */
	public static final String TRADE_ING = "1";

	/**
	 * 等待竞价
	 */
	public static final String TRADE_WAIT = "2";

	/**
	 * 竞价完成
	 */
	public static final String TRADE_FIN = "3";
	/**
	 * 等待摇号
	 */
	public static final String TRADE_YH = "5";

}
