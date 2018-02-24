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

		// 5分钟执行一次，时间段由程序控制
		logger.info("===[PublishToScreenJob 向大屏发送数据，开始]===");
		// PushDataToMonitor pd = new PushDataToMonitor();
		// pd.pushData();
		// 2楼大厅2块屏幕显示 最高报价/现场竞价/成交结果
		// 2楼拍卖现场显示 最高报价/现场竞价/成交结果
		PublishToScreen publishToScreen = new PublishToScreen();
		//2楼大厅中间的屏幕，所有地块信息；
		//公告发布后即刻生成，一次
		publishToScreen.publishIt();
		
		// 2楼大厅最右边最高报价, 生成每个公告对应地块，最高报价的xml 文件，放入 /zgbj 目录
		// 挂牌时间结束后现场竞价前即生成本次挂牌的最高报价
		publishToScreen.publishZgbj();
		
		//1楼当前挂牌报价,挂牌开始后至挂牌结束前生成文件；挂牌开始前或结束后不生成
		publishToScreen.publishDqZgBj();
		logger.info("===[PublishToScreenJob 向大屏发送数据，结束] 耗时[" + (System.currentTimeMillis() - start) + "]毫秒===");
	}

}
