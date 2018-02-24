package com.wonders.tdsc.scheduler.task;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wonders.esframework.scheduler.timer.SchedulerTask;

public class TestSchedulerTask extends SchedulerTask {
    /** 日志 */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        logger.debug("=========任务计划测试-开始==========");
        logger.debug("Spring In Action ...");
        logger.debug("Hibernate In Action ...");
        logger.debug("Struts In Action ...");

        try {
            logger.debug("name:" + getJsonObject().get("name"));
            logger.debug("age:" + getJsonObject().get("age"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logger.debug("=========任务计划测试-结束==========");
    }

}
