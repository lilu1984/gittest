package com.wonders.tdsc.scheduler.task2job;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wonders.wsjy.service.JobServer;
import com.wonders.wsjy.service.TradeServer;
public class CheckYxBlockJob implements Job {
	private Logger	logger	= Logger.getLogger(getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// ÿ5����ִ��һ��
		logger.info("===[CheckYxBlockJob ��������ؿ�ת������ؿ飬��ʼ]===");
		JobServer server = new JobServer();
		server.checkYiXiangBlockInListingEndDate();
		logger.info("===[CheckYxBlockJob ��������ؿ�ת������ؿ飬����]===");
	}
}
