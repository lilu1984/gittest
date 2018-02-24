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
		// 每5分钟执行一次
		logger.info("===[CheckYxBlockJob 处理意向地块转无意向地块，开始]===");
		JobServer server = new JobServer();
		server.checkYiXiangBlockInListingEndDate();
		logger.info("===[CheckYxBlockJob 处理意向地块转无意向地块，结束]===");
	}
}
