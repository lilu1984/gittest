package com.wonders.tdsc.scheduler.task2job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wonders.tdsc.out.service.PushDataToEsb;

public class PushDataToEsbJob implements Job {
	private Logger	logger	= Logger.getLogger(getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long start = System.currentTimeMillis();

		//每天凌晨5点执行  , 0 0 5 * * ?
		logger.info("===[PushDataToEsbJob 向监察平台发送数据，开始]===");
		
		PushDataToEsb pushDataToEsb = new PushDataToEsb();
		pushDataToEsb.pushItNew();
		
		logger.info("===[PushDataToEsbJob 向监察平台发送数据，结束] 耗时[" + (System.currentTimeMillis() - start) + "]毫秒===");
	}

}
