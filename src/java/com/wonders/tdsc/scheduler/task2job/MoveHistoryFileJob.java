package com.wonders.tdsc.scheduler.task2job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wonders.tdsc.out.service.MoveHistoryFile;

public class MoveHistoryFileJob implements Job {
	private Logger	logger	= Logger.getLogger(getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long start = System.currentTimeMillis();

		// 每晚23点执行;
		logger.info("===[MoveHistoryFileJob 移动大屏历史文件，开始]===");
		
		MoveHistoryFile mv = new MoveHistoryFile();
		mv.moveIt();
		logger.info("===[MoveHistoryFileJob 移动大屏历史文件，结束] 耗时[" + (System.currentTimeMillis() - start) + "]毫秒===");
	}

}
