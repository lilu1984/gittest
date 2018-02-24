package com.wonders.tdsc.scheduler.task2job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wonders.tdsc.out.service.PublishToScreen;

public class PublishToScreenJob implements Job {
	private Logger	logger	= Logger.getLogger(getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long start = System.currentTimeMillis();

		// 5����ִ��һ�Σ�ʱ����ɳ������
		logger.info("===[PublishToScreenJob ������������ݣ���ʼ]===");
		// PushDataToMonitor pd = new PushDataToMonitor();
		// pd.pushData();
		// 2¥����2����Ļ��ʾ ��߱���/�ֳ�����/�ɽ����
		// 2¥�����ֳ���ʾ ��߱���/�ֳ�����/�ɽ����
		PublishToScreen publishToScreen = new PublishToScreen();
		//2¥�����м����Ļ�����еؿ���Ϣ��
		//���淢���󼴿����ɣ�һ��
		publishToScreen.publishIt();
		
		// 2¥�������ұ���߱���, ����ÿ�������Ӧ�ؿ飬��߱��۵�xml �ļ������� /zgbj Ŀ¼
		// ����ʱ��������ֳ�����ǰ�����ɱ��ι��Ƶ���߱���
		publishToScreen.publishZgbj();
		
		//1¥��ǰ���Ʊ���,���ƿ�ʼ�������ƽ���ǰ�����ļ������ƿ�ʼǰ�����������
		publishToScreen.publishDqZgBj();
		logger.info("===[PublishToScreenJob ������������ݣ�����] ��ʱ[" + (System.currentTimeMillis() - start) + "]����===");
	}

}
