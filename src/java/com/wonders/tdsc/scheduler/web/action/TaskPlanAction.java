package com.wonders.tdsc.scheduler.web.action;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.scheduler.SchedulerConstants;
import com.wonders.esframework.scheduler.bo.TaskPlan;
import com.wonders.esframework.scheduler.factory.TaskPlanEntry;
import com.wonders.esframework.scheduler.factory.TaskPlanFactory;
import com.wonders.esframework.scheduler.service.TaskPlanManager;
import com.wonders.esframework.scheduler.timer.ScheduleIterator;
import com.wonders.esframework.scheduler.timer.Scheduler;
import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.scheduler.web.form.TaskPlanForm;

public class TaskPlanAction extends DispatchAction {

    private TaskPlanManager taskPlanManager;

    public void setTaskPlanManager(TaskPlanManager taskPlanManager) {
        this.taskPlanManager = taskPlanManager;
    }

    /**
     * 监控
     */
    public ActionForward toMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 取得数据库中的计划任务列表
        List taskPlanList = taskPlanManager.loadTaskPlan();

        // 取得时间框架中的任务
        Hashtable ht = Scheduler.getInstance().getTaskPool();

        // 整理状态：在时间框架中的任务池中已有的则状态为正在执行，没有的为停止
        if (taskPlanList != null && taskPlanList.size() > 0) {
            for (int i = 0; i < taskPlanList.size(); i++) {
                TaskPlan taskPlan = (TaskPlan) taskPlanList.get(i);

                if (ht.get(taskPlan.getId()) != null)
                    taskPlan.setStatus(SchedulerConstants.TASK_SCHEDULED);
                else
                    taskPlan.setStatus(SchedulerConstants.TASK_STOPPED);

            }
        }

        // 放到request中
        request.setAttribute("taskPlanList", taskPlanList);

        return mapping.findForward("taskPlanList");
    }

    /**
     * 增加之前
     */
    public ActionForward toAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("taskPlan");
    }

    /**
     * 增加
     */
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TaskPlanForm taskPlanForm = (TaskPlanForm) form;

        TaskPlan taskPlan = new TaskPlan();

        BeanUtils.copyProperties(taskPlan, taskPlanForm);

        taskPlanManager.addTaskPlan(taskPlan);

        return mapping.findForward("seccess");
    }

    /**
     * 修改之前
     */
    public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String taskPlanId = request.getParameter("taskPlanId");

        // 从数据库获得任务对象
        TaskPlan taskPLan = taskPlanManager.queryTaskPlanById(taskPlanId);

        // 放到request中
        request.setAttribute("taskPlan", taskPLan);

        return mapping.findForward("taskPlan");
    }

    /**
     * 修改
     */
    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TaskPlanForm taskPlanForm = (TaskPlanForm) form;

        TaskPlan taskPlan = new TaskPlan();

        BeanUtils.copyProperties(taskPlan, taskPlanForm);

        taskPlanManager.modifyTaskPlan(taskPlan);

        return mapping.findForward("seccess");
    }

    /**
     * 删除
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // 从数据库获得任务对象
        taskPlanManager.removeTaskPlan(taskPlanId);

        return mapping.findForward("seccess");
    }

    /**
     * 执行任务
     */
    public ActionForward scheduledTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // 从数据库获得任务对象
        TaskPlan taskPLan = taskPlanManager.queryTaskPlanById(taskPlanId);

        // 整理成一条作业
        TaskPlanEntry entry = TaskPlanFactory.arrangeTaskPlan(taskPLan);

        // 交给时间框架执行
        Scheduler.getInstance().scheduler((SchedulerTask) entry.getTaskObj(), (ScheduleIterator) entry.getPlanObj());

        return mapping.findForward("seccess");
    }

    /**
     * 停止任务
     */
    public ActionForward stopTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // 停止任务
        Scheduler.getInstance().cancelSchedulerTask(taskPlanId);

        return mapping.findForward("seccess");
    }

}
