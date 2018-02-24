package com.wonders.tdsc.out.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.out.jdbc.EsbConnection;
import com.wonders.tdsc.out.service.bean.TdzpBaseInfoBean;
import com.wonders.tdsc.out.service.bean.TdzpBiddingInfoBean;
import com.wonders.tdsc.out.service.bean.TdzpExceptionInfoBean;
import com.wonders.tdsc.out.service.bean.TdzpTransactInfoBean;
import com.wonders.tdsc.out.service.impl.UUIDHexGenerator;
import com.wonders.tdsc.retbail.service.TdscReturnBailService;

public class PushDataToEsb {

	//private Connection					localConn					= ConnectionManager.getInstance().getConnection();
	//private Connection					monitorConn					= EsbConnection.getConnection();
	// private Connection monitorConn = ConnectionManager.getInstance().getConnection();

	private TdscBidderAppService		tdscBidderAppService		= (TdscBidderAppService) AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppService");
	private TdscBlockInfoService		tdscBlockInfoService		= (TdscBlockInfoService) AppContextUtil.getInstance().getAppContext().getBean("tdscBlockInfoService");
	private TdscScheduletableService	tdscScheduletableService	= (TdscScheduletableService) AppContextUtil.getInstance().getAppContext().getBean("tdscScheduletableService");

	private TdscReturnBailService		tdscReturnBailService		= (TdscReturnBailService) AppContextUtil.getInstance().getAppContext().getBean("tdscReturnBailService");

	private static String				GUPAI						= "3104";
	private static String				PAIMAI						= "3103";
	private static String				ZHAOBIAO					= "3107";

	public void pushIt() {
		// TDZP_BASEINFO 基本信息表
		// TDZP_JMXXB 竞买信息表
		// TDZP_CJXXB 成交信息表
		// TDZP_XMYCXX 异常信息表
	}

	public void pushItNew() {

		// if node_id= '90' and status = '9001' 成交完毕，
		// if(TDSC_BLOCK_TRAN_APP.TRAN_RESULT 流拍 or 终止交易) 插入异常信息表
		// else (成交公告发布后，插入 其它 2 张表)， 更新 (基本信息表的 状态 )
		// if node_id= '90' and status = '9004' 交易终止, 如果成交异常，则插入异常信息表
		// and TDSC_APP.push_monitor_end='Y'
		try {

			List appInfoList = queryAllTdscApp();
			if (appInfoList != null && appInfoList.size() > 0) {
				for (int i = 0; i < appInfoList.size(); i++) {
					TdscApp appInfo = (TdscApp) appInfoList.get(i);
					String tdscAppId = appInfo.getTdscAppId();
					TdscBlockTranApp tranApp = tdscBidderAppService.findTdscBlockTranApp(tdscAppId);

					if (tranApp != null) {
						System.out.println(tdscAppId);

						List bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(tdscAppId);
						
						TdzpBaseInfoBean bean = (TdzpBaseInfoBean) queryTdzpView1(tdscAppId);
						insertTdzpBaseinfo(bean);
						insertBidder(appInfo, tranApp, bidderAppList);
						insertOther(appInfo, tdscAppId, tranApp);
						updateTdscAppPushMonitorEnd(tdscAppId, "Y");
						
						// TdscBidderApp bidderApp = getBidderPerson(appInfo.getTdscAppId());

						// 1. 判断监控系统是否存在该 appId
//						if (!isExistInBaseInfo(tdscAppId)) {
//							if ("00".equals(tranApp.getTranResult()) && !StringUtil.isEmpty(tranApp.getBlockNoticeNo())) {
//								// 未交易的,但已经发布公告的地块
//								// if ("04".equals(appInfo.getNodeId()) && "0401".equals(appInfo.getStatusId()) && "01".equals(appInfo.getAppResult())) {
//								// if ("16".equals(appInfo.getNodeId()) && "1601".equals(appInfo.getStatusId()) && "01".equals(appInfo.getAppResult())) {
//								// if node_id= '04' and status = '0401' and app_result = '01' 出让公告发布完毕, 插入基本信息表；
//								TdzpBaseInfoBean bean = (TdzpBaseInfoBean) queryTdzpView1(tdscAppId);
//								insertTdzpBaseinfo(bean);
//
//								insertBidder(appInfo, tranApp, bidderAppList);
//
//								insertOther(appInfo, tdscAppId, tranApp);
//							}
//						} else {
//							insertBidder(appInfo, tranApp, bidderAppList);
//							insertOther(appInfo, tdscAppId, tranApp);
//							updateTdscAppPushMonitorEnd(tdscAppId, "Y");
//							//localConn.commit();
//						}
						//monitorConn.commit();

						// 设置 TDSC_APP.Push_Monitor_End = 'Y'，表示发送监控系统完毕
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertBidder(TdscApp appInfo, TdscBlockTranApp tranApp, List bidderAppList) {

		TdscListingInfo gpInfo = tdscBlockInfoService.findBlistingInfo(appInfo.getTdscAppId());
		if (bidderAppList != null && bidderAppList.size() > 0) {
			for (int i = 0; i < bidderAppList.size(); i++) {

				TdscBidderApp bidderInfo = (TdscBidderApp) bidderAppList.get(i);
				TdscBidderPersonApp bidderPersonInfo = tdscBidderAppService.getTdscBidderPersonByBidderId(bidderInfo.getBidderId());
				TdscReturnBail tdscReturnBail = tdscReturnBailService.getTdscReturnBailByAppIdBidderId(appInfo.getTdscAppId(), bidderInfo.getBidderId());

				// TdscBidderPersonApp bidderPersonInfo = tdscBlockInfoService.getTdscBidderPersonAppByBidderId(bidderApp.getBidderId());
				// ///////////************************************************//////////////////
				// 竞买人信息
				TdzpBiddingInfoBean bean = new TdzpBiddingInfoBean();
				UUIDHexGenerator uuidb = new UUIDHexGenerator();
				bean.setId(uuidb.generate());
				bean.setBaseinfoid(appInfo.getTdscAppId());
				bean.setSqdw(StringUtil.GBKtoISO88591(bidderPersonInfo.getBidderName()));// 竞买单位;
				bean.setSqsj(DateUtil.timestamp2String(tranApp.getResultDate(), "yyyy-MM-dd hh:mm:ss"));// 竞买申请时间
				bean.setFddbr(StringUtil.GBKtoISO88591(bidderPersonInfo.getCorpFr()));
				bean.setWtdbr(StringUtil.GBKtoISO88591(bidderInfo.getWtrName()));// 委托人
				if (tdscReturnBail != null && tdscReturnBail.getBidderBail() != null)
					bean.setJnje(tdscReturnBail.getBidderBail() + "");
				bean.setJnsj(null);// 保证金缴纳时间，系统无该字段
				bean.setCsbj(tranApp.getInitPrice() + "");// 初始报价
				if (gpInfo != null)
					bean.setBjcs(gpInfo.getCurrRound() + "");// 报价次数

				if (tdscReturnBail != null && tdscReturnBail.getActionDate() != null && "01".equals(tdscReturnBail.getIfReturn()))
					bean.setBzjsj(tdscReturnBail.getActionDate() + ""); // 保证金退还时间
				insertTdzpJmxxb(bean);
				// ///////////************************************************//////////////////
			}
		}
	}

	private void insertOther(TdscApp appInfo, String tdscAppId, TdscBlockTranApp tranApp) {
		if ("90".equals(appInfo.getNodeId())) {
			if ("9001".equals(appInfo.getStatusId())) {
				// need get TDSC_BLOCK_TRAN_APP.TRAN_RESULT
				String tranResult = tranApp.getTranResult();
				if ("01".equals(tranResult)) {

					TdscListingInfo gpInfo = tdscBlockInfoService.findBlistingInfo(appInfo.getTdscAppId());

					// 成交信息
					TdzpTransactInfoBean tbean = new TdzpTransactInfoBean();
					UUIDHexGenerator uuidt = new UUIDHexGenerator();
					tbean.setId(uuidt.generate());
					tbean.setBaseinfoid(appInfo.getTdscAppId());
					tbean.setCsbj(tranApp.getInitPrice() + "");
					tbean.setBjfd(tranApp.getAddPriceRange() + "");

					// TdscListingInfo gpInfo = tdscBlockInfoService.findBlistingInfo(appInfo.getTdscAppId());
					if (gpInfo != null)
						tbean.setBjcs(gpInfo.getCurrRound() + "");// 报价次数
					tbean.setCjje(tranApp.getResultPrice() + "");// 成交价格
					tbean.setJdr(StringUtil.GBKtoISO88591(tranApp.getResultName()));// 竞得人
					tbean.setCjfs(getJmfs(tranApp.getTransferMode()));
					tbean.setCjsj(DateUtil.timestamp2String(tranApp.getResultDate(), "yyyy-MM-dd hh:mm:ss"));

					TdscBlockPlanTableCondition cond = new TdscBlockPlanTableCondition();
					cond.setAppId(appInfo.getTdscAppId());
					TdscBlockPlanTable planInfo = tdscScheduletableService.findBlockPlanTableInfo(cond);

					if (GUPAI.equals(appInfo.getTransferMode()))
						tbean.setQrssj(DateUtil.date2String(planInfo.getListEndDate(), "yyyy-MM-dd"));// 确认书发布时间

					if (PAIMAI.equals(appInfo.getTransferMode())) {
						tbean.setQrssj(DateUtil.date2String(planInfo.getAuctionDate(), "yyyy-MM-dd"));// 确认书发布时间
					}
					if (ZHAOBIAO.equals(appInfo.getTransferMode())) {
						tbean.setQrssj(DateUtil.date2String(planInfo.getBidEvaDate(), "yyyy-MM-dd"));// 确认书发布时间
					}

					String retDate = tdscReturnBailService.getLastReturnBailActionDateByAppId(tdscAppId);
					if (!StringUtil.isEmpty(retDate))
						tbean.setBzjsj(retDate); // 保证金退还时间

					insertTdzpCjxxb(tbean);
				}
				if ("02".equals(tranResult)) {
					// 流标
					UUIDHexGenerator uuid = new UUIDHexGenerator();
					TdzpExceptionInfoBean exBean = new TdzpExceptionInfoBean();
					exBean.setId(uuid.generate());
					exBean.setBaseinfoid(appInfo.getTdscAppId());
					exBean.setZdbh(StringUtil.GBKtoISO88591(tranApp.getBlockNoticeNo()));
					exBean.setYclx(StringUtil.GBKtoISO88591("由市国土局收回"));
					exBean.setYccs_time(DateUtil.timestamp2String(tranApp.getResultDate(), "yyyy-MM-dd hh:mm:ss"));
					exBean.setYc_info(StringUtil.GBKtoISO88591("由市国土局收回"));
					exBean.setYcclxx(StringUtil.GBKtoISO88591("由市国土局收回"));
					exBean.setYccl_time(DateUtil.timestamp2String(tranApp.getResultDate(), "yyyy-MM-dd hh:mm:ss"));
					insertTdzpXmycxx(exBean);
					// 更新 base info 表的状态为异常
					updateTdzpBaseinfoState(tdscAppId);

				}
			}

			if ("9004".equals(appInfo.getStatusId()) || "9002".equals(appInfo.getStatusId())) {
				UUIDHexGenerator uuid = new UUIDHexGenerator();
				TdzpExceptionInfoBean exBean = new TdzpExceptionInfoBean();
				exBean.setId(uuid.generate());
				exBean.setBaseinfoid(appInfo.getTdscAppId());
				exBean.setZdbh(tranApp.getBlockNoticeNo());
				exBean.setYclx(StringUtil.GBKtoISO88591("业务终止"));
				exBean.setYccs_time(DateUtil.timestamp2String(appInfo.getStatusDate(), "yyyy-MM-dd hh:mm:ss"));
				exBean.setYc_info(StringUtil.GBKtoISO88591("业务员取消交易"));
				exBean.setYcclxx(StringUtil.GBKtoISO88591("取消"));
				exBean.setYccl_time(DateUtil.timestamp2String(appInfo.getStatusDate(), "yyyy-MM-dd hh:mm:ss"));

				insertTdzpXmycxx(exBean);
				// 更新 base info 表的状态为异常
				updateTdzpBaseinfoState(tdscAppId);
			}

		}
	}

	private String getJmfs(String tranMode) {
		String str = "";
		if (GUPAI.equals(tranMode))
			str = "3";
		if (PAIMAI.equals(tranMode))
			str = "2";
		if (ZHAOBIAO.equals(tranMode))
			str = "1";
		return str;
		// return StringUtil.GBKtoISO88591(str);
	}

	private TdzpBaseInfoBean queryTdzpView1(String tdscAppId){
		
		Connection conn = ConnectionManager.getInstance().getConnection();
		Statement st = null;		
		ResultSet rs = null;
		
		TdzpBaseInfoBean retBean = null;
		String sql = "SELECT * FROM TDZP_BASEINFO_VIEW WHERE APPID='" + tdscAppId + "'";
		
		try {
			st = conn.createStatement();		
			rs = st.executeQuery(sql);
			if (rs.next())
				retBean = TdzpBaseInfoBean.rs2Bean(rs);			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retBean;
	}

	private boolean updateTdzpBaseinfoState(String tdscAppId) {
		Connection monitorConn = EsbConnection.getConnection();
		Statement st = null;
		try {
			String sql = "Update TDZP_BASEINFO Set state='1' Where trim(id)='" + tdscAppId + "'";
			st = monitorConn.createStatement();
			return st.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(st != null){
					st.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean insertTdzpXmycxx(TdzpExceptionInfoBean bean) {
		
		Connection monitorConn = EsbConnection.getConnection();
		PreparedStatement prestmt = null;
		
		StringBuffer sql = new StringBuffer("");
		sql.append("Insert into TDZP_XMYCXX (");
		sql.append(" ID, BASEINFOID, ZDBH, YCLX, YCCS_TIME, YC_INFO, YCCLXX, YCCL_TIME)");
		sql.append(" Values (?,?,?,?,?,?,?,?)");

		try {

			prestmt = monitorConn.prepareStatement(sql.toString());
			prestmt.setString(1, bean.getId());
			prestmt.setString(2, bean.getBaseinfoid());
			if (bean.getZdbh() != null)
				prestmt.setString(3, bean.getZdbh());
			else
				prestmt.setString(3, " ");
			if (bean.getYclx() != null)
				prestmt.setString(4, bean.getYclx());
			else
				prestmt.setString(4, " ");

			if (bean.getYccs_time() != null)
				prestmt.setString(5, bean.getYccs_time());
			else
				prestmt.setString(5, " ");

			if (bean.getYc_info() != null)
				prestmt.setString(6, bean.getYc_info());
			else
				prestmt.setString(6, " ");

			if (bean.getYcclxx() != null)
				prestmt.setString(7, bean.getYcclxx());
			else
				prestmt.setString(7, " ");

			if (bean.getYccl_time() != null)
				prestmt.setString(8, bean.getYccl_time());
			else
				prestmt.setString(8, " ");

			return prestmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(prestmt != null){
					prestmt.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 竞买信息有多条，通过 appid 查询 TDSC_BIDDER_APP 本交易竞买次数，循环 insert 数据
	 * 
	 * @param bean
	 * @return
	 */
	private boolean insertTdzpJmxxb(TdzpBiddingInfoBean bean) {

		Connection monitorConn = EsbConnection.getConnection();
		PreparedStatement prestmt = null;
		
		StringBuffer sql = new StringBuffer("");
		sql.append("Insert into TDZP_JMXXB (");
		sql.append(" ID, BASEINFOID, SQDW, SQSJ, FDDBR, WTDBR, JNJE, JNSJ, CSBJ, BJCS, BZJSJ, JMSJ)");
		sql.append(" Values (?,?,?,?,?,?,?,?,?,?, ?,?)");

		try {

			prestmt = monitorConn.prepareStatement(sql.toString());
			prestmt.setString(1, bean.getId());
			prestmt.setString(2, bean.getBaseinfoid());

			if (bean.getSqdw() != null)
				prestmt.setString(3, bean.getSqdw());
			else
				prestmt.setString(3, " ");

			if (bean.getSqsj() != null)
				prestmt.setString(4, bean.getSqsj());
			else
				prestmt.setString(4, " ");
			if (bean.getFddbr() != null)
				prestmt.setString(5, bean.getFddbr());
			else
				prestmt.setString(5, " ");

			if (bean.getWtdbr() != null)
				prestmt.setString(6, bean.getWtdbr());
			else
				prestmt.setString(6, " ");

			if (bean.getJnje() != null)
				prestmt.setString(7, bean.getJnje());
			else
				prestmt.setString(7, " ");

			if (bean.getJnsj() != null)
				prestmt.setString(8, bean.getJnsj());
			else
				prestmt.setString(8, " ");
			if (bean.getCsbj() != null)
				prestmt.setString(9, bean.getCsbj());
			else
				prestmt.setString(9, " ");
			if (bean.getBjcs() != null)
				prestmt.setString(10, bean.getBjcs());
			else
				prestmt.setString(10, " ");
			if (bean.getBzjsj() != null)
				prestmt.setString(11, bean.getBzjsj());
			else
				prestmt.setString(11, " ");
			if (bean.getJmsj() != null)
				prestmt.setString(12, bean.getJmsj());
			else
				prestmt.setString(12, " ");

			return prestmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(prestmt != null){
					prestmt.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	private boolean insertTdzpCjxxb(TdzpTransactInfoBean bean) {
		
		Connection monitorConn = EsbConnection.getConnection();
		PreparedStatement prestmt = null;
		
		StringBuffer sql = new StringBuffer("");
		sql.append("Insert into TDZP_CJXXB (");
		sql.append(" ID, BASEINFOID, CSBJ, BJFD, BJCS, CJJE, JDR, CJFS, CJSJ, QRSSJ, BZJSJ)");
		sql.append(" Values (?,?,?,?,?,?,?,?,?,?, ?)");

		try {
			// UUIDHexGenerator uuid = new UUIDHexGenerator();
			prestmt = monitorConn.prepareStatement(sql.toString());

			prestmt.setString(1, bean.getId());
			prestmt.setString(2, bean.getBaseinfoid());

			if (bean.getCsbj() != null)
				prestmt.setString(3, bean.getCsbj());
			else
				prestmt.setString(3, " ");

			if (bean.getBjfd() != null)
				prestmt.setString(4, bean.getBjfd());
			else
				prestmt.setString(4, " ");

			if (bean.getBjcs() != null)
				prestmt.setString(5, bean.getBjcs());
			else
				prestmt.setString(5, " ");
			if (bean.getCjje() != null)
				prestmt.setString(6, bean.getCjje());
			else
				prestmt.setString(6, " ");

			if (bean.getJdr() != null)
				prestmt.setString(7, bean.getJdr());
			else
				prestmt.setString(7, " ");

			if (bean.getCjfs() != null)
				prestmt.setString(8, bean.getCjfs());
			else
				prestmt.setString(8, " ");

			if (bean.getCjsj() != null)
				prestmt.setString(9, bean.getCjsj());
			else
				prestmt.setString(9, " ");

			if (bean.getQrssj() != null)
				prestmt.setString(10, bean.getQrssj());
			else
				prestmt.setString(10, " ");

			if (bean.getBzjsj() != null)
				prestmt.setString(11, bean.getBzjsj());
			else
				prestmt.setString(11, " ");

			return prestmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(prestmt != null){
					prestmt.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean insertTdzpBaseinfo(TdzpBaseInfoBean bean) {
		
		Connection monitorConn = EsbConnection.getConnection();
		PreparedStatement prestmt = null;
		
		StringBuffer sql = new StringBuffer("");
		sql.append("Insert into TDZP_BASEINFO (");
		sql.append(" ID, ZDBH, XMMC, YDQY, YDLX,");
		sql.append(" YDXZ, YDMJ, YDWZ, JSMJ, DLYDMJ,");
		sql.append(" LDYDMJ, TDPG, GHXK, TDLX, FALX,");
		sql.append(" JMSQSJ, JMJZSJ, JMKSSJ, JMJSSJ, GGFBSJ, BZJ, STATE)");
		sql.append(" Values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?)");

		try {
			prestmt = monitorConn.prepareStatement(sql.toString());
			prestmt.setString(1, bean.getId());
			if (bean.getZdbh() != null)
				prestmt.setString(2, bean.getZdbh());
			else
				prestmt.setString(2, " ");

			if (bean.getXmmc() != null)
				prestmt.setString(3, bean.getXmmc());
			else
				prestmt.setString(3, " ");

			if (bean.getYdqy() != null)
				prestmt.setString(4, bean.getYdqy());
			else
				prestmt.setString(4, " ");

			if (bean.getYdlx() != null)
				prestmt.setString(5, bean.getYdlx());
			else
				prestmt.setString(5, " ");

			if (bean.getYdxz() != null)
				prestmt.setString(6, bean.getYdxz());
			else
				prestmt.setString(6, " ");

			if (bean.getYdmj() != null)
				prestmt.setString(7, bean.getYdmj());
			else
				prestmt.setString(7, " ");

			if (bean.getYdwz() != null)
				prestmt.setString(8, bean.getYdwz());
			else
				prestmt.setString(8, " ");

			if (bean.getJsmj() != null)
				prestmt.setString(9, bean.getJsmj());
			else
				prestmt.setString(9, " ");

			if (bean.getDlydmj() != null)
				prestmt.setString(10, bean.getDlydmj());
			else
				prestmt.setString(10, " ");

			if (bean.getLdydmj() != null)
				prestmt.setString(11, bean.getLdydmj());
			else
				prestmt.setString(11, " ");

			if (bean.getTdpg() != null)
				prestmt.setString(12, bean.getTdpg());
			else
				prestmt.setString(12, " ");

			if (bean.getGhxk() != null)
				prestmt.setString(13, bean.getGhxk());
			else
				prestmt.setString(13, " ");

			if (bean.getTdlx() != null)
				prestmt.setString(14, bean.getTdlx());
			else
				prestmt.setString(14, " ");

			if (bean.getFalx() != null)
				prestmt.setString(15, bean.getFalx());
			else
				prestmt.setString(15, " ");

			if (bean.getJmsqsj() != null)
				prestmt.setString(16, (bean.getJmsqsj() + "").substring(0, 16));
			else
				prestmt.setString(16, " ");

			if (bean.getJmjzsj() != null)
				prestmt.setString(17, (bean.getJmjzsj() + "").substring(0, 16));
			else
				prestmt.setString(17, " ");

			if (bean.getJmkssj() != null)
				prestmt.setString(18, (bean.getJmkssj() + "").substring(0, 16));
			else
				prestmt.setString(18, " ");

			if (bean.getJmjssj() != null)
				prestmt.setString(19, (bean.getJmjssj() + "").substring(0, 16));
			else
				prestmt.setString(19, " ");

			if (bean.getGgfbsj() != null)
				prestmt.setString(20, (bean.getGgfbsj() + "").substring(0, 16));
			else
				prestmt.setString(20, " ");

			if (bean.getBzj() != null)
				prestmt.setString(21, bean.getBzj());
			else
				prestmt.setString(21, " ");

			if (bean.getState() != null)
				prestmt.setString(22, bean.getState());
			else
				prestmt.setString(22, " ");

			if (prestmt.execute()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(prestmt != null){
					prestmt.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private void updateTdscAppPushMonitorEnd(String tdscAppId, String value) {
		
		Connection localConn = ConnectionManager.getInstance().getConnection();
		Statement st = null;
		
		String sql = "update TDSC_APP set PUSH_MONITOR_END='" + value + "' where TDSC_APP_ID='" + tdscAppId + "'";
		try {
			st = localConn.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(st != null){
					st.close();
				}
				if(localConn != null){
					localConn.commit();
					localConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isExistInBaseInfo(String tdscAppId) {
		
		Connection monitorConn = EsbConnection.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM TDZP_BASEINFO WHERE ID='" + tdscAppId.trim() + "'";

		try {
			st = monitorConn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(st != null){
					st.close();
				}
				if(rs != null){
					rs.close();
				}
				if(monitorConn != null){
					monitorConn.commit();
					monitorConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private List queryAllTdscApp() {
		
		Connection localConn = ConnectionManager.getInstance().getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM TDSC_APP WHERE (PUSH_MONITOR_END IS NULL OR PUSH_MONITOR_END = 'N') AND NODE_ID = '90'";
		List appInfoList = new ArrayList();
		try {
			st = localConn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {

				// NODE_ID:90 | STATUS_ID:9001 | APP_RESULT:10 交易正常结束
				// NODE_ID:90 | STATUS_ID:9004 交易终止，可能异常

				TdscApp appInfo = new TdscApp();

				appInfo.setTdscAppId(rs.getString("TDSC_APP_ID"));
				appInfo.setNodeId(rs.getString("NODE_ID"));
				appInfo.setStatusId(rs.getString("STATUS_ID"));
				appInfo.setAppResult(rs.getString("APP_RESULT"));

				appInfoList.add(appInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(st != null){
					st.close();
				}
				if(rs != null){
					rs.close();
				}
				if(localConn != null){
					localConn.commit();
					localConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return appInfoList;
	}
}
