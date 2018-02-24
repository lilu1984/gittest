package com.wonders.tdsc.scheduler.task;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.tdsc.publish.service.PublishService;

public class TimingPublishTask extends SchedulerTask {

    /** 日志 */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        try {
            // "0"表示拟每5分钟定时发布
            // "1"表示拟每天17:00定时发布
            // "2"表示拟每天15:00定时发布
            // "3"表示拟每六小时定时发布
            // "4"表示拟每15分钟定时发布
            long start = System.currentTimeMillis();
            logger.info("===[执行信息发布]开始===");
            String timingType = (String) getJsonObject().get("timingType");
            PublishService.getPublishInstance().timingPublish(timingType);
            logger.info("===[执行信息发布][发布编号" + timingType + "]结束-共花费[" + (System.currentTimeMillis() - start) + "]毫秒===");
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }
}