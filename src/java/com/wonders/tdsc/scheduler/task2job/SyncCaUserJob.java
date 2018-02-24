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

		//ÿ������23��ִ��  , 0 0 23 * * ?
		logger.info("===[SyncCaUserJob ͬ��CA�û���ҵ��ϵͳ����ʼ]===");
		SyncUserTable sut = new SyncUserTable();
		sut.syncUser();
		logger.info("===[SyncCaUserJob ͬ��CA�û���ҵ��ϵͳ������] ��ʱ[" + (System.currentTimeMillis() - start) + "]����===");
	}

}
