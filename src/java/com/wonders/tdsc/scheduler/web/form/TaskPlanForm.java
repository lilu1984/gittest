package com.wonders.tdsc.scheduler.web.form;

import org.apache.struts.action.ActionForm;

public class TaskPlanForm extends ActionForm {

    // ���
    private String id;

    // ��������
    private String schedulerName;

    // ����ʵ����
    private String taskClass;

    // �������
    private String taskParameters;

    // �ƻ�����
    private String planId;

    // �����죨���ŷָ���ִ������磺1,3,5��
    private String schedulerDays;

    // ����Сʱ
    private Integer schedulerHour;

    // �������
    private Integer schedulerMinute;

    // ��������
    private Integer schedulerSecond;

    // ��Ч��
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
