package com.wonders.tdsc.scheduler.web.form;

import org.apache.struts.action.ActionForm;

public class TaskPlanForm extends ActionForm {

    // 序号
    private String id;

    // 任务名称
    private String schedulerName;

    // 任务实现类
    private String taskClass;

    // 任务参数
    private String taskParameters;

    // 计划类型
    private String planId;

    // 具体天（逗号分割的字串，例如：1,3,5）
    private String schedulerDays;

    // 具体小时
    private Integer schedulerHour;

    // 具体分钟
    private Integer schedulerMinute;

    // 具体秒钟
    private Integer schedulerSecond;

    // 有效性
    private String validity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getSchedulerDays() {
        return schedulerDays;
    }

    public void setSchedulerDays(String schedulerDays) {
        this.schedulerDays = schedulerDays;
    }

    public Integer getSchedulerHour() {
        return schedulerHour;
    }

    public void setSchedulerHour(Integer schedulerHour) {
        this.schedulerHour = schedulerHour;
    }

    public Integer getSchedulerMinute() {
        return schedulerMinute;
    }

    public void setSchedulerMinute(Integer schedulerMinute) {
        this.schedulerMinute = schedulerMinute;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public Integer getSchedulerSecond() {
        return schedulerSecond;
    }

    public void setSchedulerSecond(Integer schedulerSecond) {
        this.schedulerSecond = schedulerSecond;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTaskParameters() {
        return taskParameters;
    }

    public void setTaskParameters(String taskParameters) {
        this.taskParameters = taskParameters;
    }
}
