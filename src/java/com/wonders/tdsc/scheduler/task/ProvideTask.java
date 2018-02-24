package com.wonders.tdsc.scheduler.task;

import org.apache.log4j.Logger;

import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.tdsc.provide.BidderProvideTask;

public class ProvideTask extends SchedulerTask {
    /** ��־ */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        long start = System.currentTimeMillis();
        logger.info("===[ִ������������Ϸ������ݵ���]��ʼ===");
        BidderProvideTask.getInstance().saveProvide();
        logger.info("===[ִ������������Ϸ������ݵ���]����-������[" + (System.currentTimeMillis() - start) + "]����===");
    }
}