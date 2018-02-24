package com.wonders.tdsc.scheduler.task2job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wonders.tdsc.out.service.SyncUserTable;

public class SyncCaUserJob implements Job {
	private Logger	logger	= Logger.getLogger(getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long start = System.currentTimeMillis();

		//每天晚上23点执行  , 0 0 23 * * ?
		logger.info("===[SyncCaUserJob 同步CA用户到业务系统，开始]===");
		SyncUserTable sut = new SyncUserTable();
		sut.syncUser();
		logger.info("===[SyncCaUserJob 同步CA用户到业务系统，结束] 耗时[" + (System.currentTimeMillis() - start) + "]毫秒===");
	}

}
