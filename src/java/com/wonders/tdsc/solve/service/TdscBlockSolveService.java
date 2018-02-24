package com.wonders.tdsc.solve.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockSolve;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.common.util.FileUtils;
import com.wonders.tdsc.solve.dao.TdscBlockSolveDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;

public class TdscBlockSolveService {

	private static final String	dapingPath	= PropertiesUtil.getInstance().getProperty("daping_path");

	private TdscBlockSolveDao	tdscBlockSolveDao;

	public void setTdscBlockSolveDao(TdscBlockSolveDao tdscBlockSolveDao) {
		this.tdscBlockSolveDao = tdscBlockSolveDao;
	}

	public void saveIt(TdscBlockSolve tdscBlockSolve, String userId) {

		tdscBlockSolve.setAnswerUserId(userId);

		tdscBlockSolve.setSeqNo(getMaxSeqNo(tdscBlockSolve.getBlockId()) + 1);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new java.util.Date());
		Timestamp ts = Timestamp.valueOf(time);
		tdscBlockSolve.setAnswerDate(ts);

		tdscBlockSolveDao.save(tdscBlockSolve);
	}

	private int getMaxSeqNo(String blockId) {
		List allSolves = (ArrayList) tdscBlockSolveDao.queryAllSolveByBlockId(blockId);
		if (allSolves != null) {
			return allSolves.size();
		}
		return 0;
	}

	public List showSolveHistory(String strBlockId, String userId) {
		return tdscBlockSolveDao.queryAllSolveByBlockId(strBlockId);
	}

	public void saveFileToDaPing(TdscBlockSolve tdscBlockSolve) {

		String fileName = "";

		CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(tdscBlockSolve.getPlanId());
		List appList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

		TdscBlockAppView app = commonQueryService.getTdscBlockAppViewByBlockId(tdscBlockSolve.getBlockId());
		String noticeTitle = app.getNoitceNo();
		if ("3107".equals(app.getTransferMode())) {
			noticeTitle += "���н����õ�ʹ��Ȩ�б������ѯ����";
			fileName = "Z_" + DateUtil.date2String(app.getOpeningDate(), "yyyyMMddHHmmss");
		} else if ("3103".equals(app.getTransferMode())) {
			noticeTitle += "���н����õ�ʹ��Ȩ����������ѯ����";
			fileName = "P_" + DateUtil.date2String(app.getActionDate(), "yyyyMMddHHmmss");
		} else if ("3104".equals(app.getTransferMode())) {
			if ("1".equals(app.getIsPurposeBlock()))
				noticeTitle += "���н����õ�ʹ��Ȩ�����򹫿����Ƴ�����ѯ����";
			else
				noticeTitle += "���н����õ�ʹ��Ȩ�����򹫿����Ƴ�����ѯ����";

			fileName = "G_" + DateUtil.date2String(app.getSceBidDate(), "yyyyMMddHHmmss");
		} else {
			fileName = "0_19000101000000";
		}

		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<root>");
		// 1. ʹ�� block_id ��� title
		sb.append("<noticetitle>").append(noticeTitle).append("</noticetitle>");

		if (appList != null && appList.size() > 0)
			for (int j = 0; j < appList.size(); j++) {
				TdscBlockAppView appView = (TdscBlockAppView) appList.get(j);
				String blockTitle = appView.getBlockNoticeNo() + "�ؿ���ɼ�Ҫ";

				sb.append("<context>");
				sb.append("<blocktitle>").append(blockTitle).append("</blocktitle>");

				List allSolves = tdscBlockSolveDao.queryAllSolveByBlockId(appView.getBlockId());
				if (allSolves != null && allSolves.size() > 0)
					// 2.1 ʹ�� block_id ��ѯ���е�����
					for (int i = 0; i < allSolves.size(); i++) {
						TdscBlockSolve solve = (TdscBlockSolve) allSolves.get(i);
						sb.append("<question>");
						// 2.2 ��֯�ؿ� title
						sb.append("<q>").append(solve.getQuestion()).append("</q>");
						sb.append("<a>").append(solve.getAnswerContext()).append("</a>");
						sb.append("</question>");
					}
				sb.append("</context>");
			}
		sb.append("</root>");

		// /////////////////////////�ַ������ݱ��浽�ļ�///////////////////////////////

		String filePath = dapingPath + "zxdy\\";
		fileName += ".xml";
		FileUtils.saveStringToFile(sb.toString(), filePath, fileName);

	}
}