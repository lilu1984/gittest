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

		//ÿ���賿5��ִ��  , 0 0 5 * * ?
		logger.info("===[PushDataToEsbJob ����ƽ̨�������ݣ���ʼ]===");
		
		PushDataToEsb pushDataToEsb = new PushDataToEsb();
		pushDataToEsb.pushItNew();
		
		logger.info("===[PushDataToEsbJob ����ƽ̨�������ݣ�����] ��ʱ[" + (System.currentTimeMillis() - start) + "]����===");
	}

}
