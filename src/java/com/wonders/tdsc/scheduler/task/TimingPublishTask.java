package com.wonders.tdsc.scheduler.task;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.tdsc.publish.service.PublishService;

public class TimingPublishTask extends SchedulerTask {

    /** ��־ */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        try {
            // "0"��ʾ��ÿ5���Ӷ�ʱ����
            // "1"��ʾ��ÿ��17:00��ʱ����
            // "2"��ʾ��ÿ��15:00��ʱ����
            // "3"��ʾ��ÿ��Сʱ��ʱ����
            // "4"��ʾ��ÿ15���Ӷ�ʱ����
            long start = System.currentTimeMillis();
            logger.info("===[ִ����Ϣ����]��ʼ===");
            String timingType = (String) getJsonObject().get("timingType");
            PublishService.getPublishInstance().timingPublish(timingType);
            logger.info("===[ִ����Ϣ����][�������" + timingType + "]����-������[" + (System.currentTimeMillis() - start) + "]����===");
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }
}