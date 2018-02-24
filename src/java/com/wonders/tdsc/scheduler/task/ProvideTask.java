package com.wonders.tdsc.scheduler.task;

import org.apache.log4j.Logger;

import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.tdsc.provide.BidderProvideTask;

public class ProvideTask extends SchedulerTask {
    /** 日志 */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        long start = System.currentTimeMillis();
        logger.info("===[执行外网申请材料发放数据导入]开始===");
        BidderProvideTask.getInstance().saveProvide();
        logger.info("===[执行外网申请材料发放数据导入]结束-共花费[" + (System.currentTimeMillis() - start) + "]毫秒===");
    }
}