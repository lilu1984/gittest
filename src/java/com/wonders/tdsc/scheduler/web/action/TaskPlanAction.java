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
     * ���
     */
    public ActionForward toMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ȡ�����ݿ��еļƻ������б�
        List taskPlanList = taskPlanManager.loadTaskPlan();

        // ȡ��ʱ�����е�����
        Hashtable ht = Scheduler.getInstance().getTaskPool();

        // ����״̬����ʱ�����е�����������е���״̬Ϊ����ִ�У�û�е�Ϊֹͣ
        if (taskPlanList != null && taskPlanList.size() > 0) {
            for (int i = 0; i < taskPlanList.size(); i++) {
                TaskPlan taskPlan = (TaskPlan) taskPlanList.get(i);

                if (ht.get(taskPlan.getId()) != null)
                    taskPlan.setStatus(SchedulerConstants.TASK_SCHEDULED);
                else
                    taskPlan.setStatus(SchedulerConstants.TASK_STOPPED);

            }
        }

        // �ŵ�request��
        request.setAttribute("taskPlanList", taskPlanList);

        return mapping.findForward("taskPlanList");
    }

    /**
     * ����֮ǰ
     */
    public ActionForward toAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("taskPlan");
    }

    /**
     * ����
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
     * �޸�֮ǰ
     */
    public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String taskPlanId = request.getParameter("taskPlanId");

        // �����ݿ����������
        TaskPlan taskPLan = taskPlanManager.queryTaskPlanById(taskPlanId);

        // �ŵ�request��
        request.setAttribute("taskPlan", taskPLan);

        return mapping.findForward("taskPlan");
    }

    /**
     * �޸�
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
     * ɾ��
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // �����ݿ����������
        taskPlanManager.removeTaskPlan(taskPlanId);

        return mapping.findForward("seccess");
    }

    /**
     * ִ������
     */
    public ActionForward scheduledTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // �����ݿ����������
        TaskPlan taskPLan = taskPlanManager.queryTaskPlanById(taskPlanId);

        // �����һ����ҵ
        TaskPlanEntry entry = TaskPlanFactory.arrangeTaskPlan(taskPLan);

        // ����ʱ����ִ��
        Scheduler.getInstance().scheduler((SchedulerTask) entry.getTaskObj(), (ScheduleIterator) entry.getPlanObj());

        return mapping.findForward("seccess");
    }

    /**
     * ֹͣ����
     */
    public ActionForward stopTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String taskPlanId = request.getParameter("taskPlanId");

        // ֹͣ����
        Scheduler.getInstance().cancelSchedulerTask(taskPlanId);

        return mapping.findForward("seccess");
    }

}
