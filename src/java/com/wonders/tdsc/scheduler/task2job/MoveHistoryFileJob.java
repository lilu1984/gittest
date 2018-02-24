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

		// ÿ��23��ִ��;
		logger.info("===[MoveHistoryFileJob �ƶ�������ʷ�ļ�����ʼ]===");
		
		MoveHistoryFile mv = new MoveHistoryFile();
		mv.moveIt();
		logger.info("===[MoveHistoryFileJob �ƶ�������ʷ�ļ�������] ��ʱ[" + (System.currentTimeMillis() - start) + "]����===");
	}

}
