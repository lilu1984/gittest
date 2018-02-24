package com.wonders.android.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.common.exception.BusinessException;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.common.GlobalConstants;

/**
 * Struts Action 基础类。
 */

public class BaseAction extends DispatchAction {

    /** 日志 */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * 注册类型转换，可以避免两个问题：
     * 
     * 1、对于数值型，当源对象中的字段为null，避免转换成0等；
     * 
     * 2、增加java.util.Date的类型转换，格式为“yyyy-MM-dd”；
     */
    static {
        BeanUtils.registerConverter();
    }

    /**
     * 将FormBean中的内容通过BeanUtils的copyProperties()绑定到Object中.
     * 
     * @param object
     *            bo对象
     * @param form
     *            ActionForm
     */
    protected void bindObject(Object object, ActionForm form) {
        if (form != null) {
            try {
                BeanUtils.copyProperties(object, form);
            } catch (Exception e) {
                throw new BusinessException(e.getMessage());
            }
        }
    }

    /**
     * Action执行，重载了父类中的方法。增加了异常处理。
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            // 如果用户信息验证失败，转到登录页面
            if (this.checkUserInfo(request) == false) {
                return mapping.findForward("androidCaLogin");
            }

            // 设置页面MEAT属性
            this.setResponseHeader(response);

            return super.execute(mapping, form, request, response);
        } catch (BusinessException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(ex.getMessage(), ex);
            }
            request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, formatBusinessExceptionMsg(ex.getMessage()));
            return mapping.findForward("androidCaLogin");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            request.setAttribute(GlobalConstants.REQUEST_ERROR_MESSAGE, getExceptionMsg());

            StringWriter out = new StringWriter();
            t.printStackTrace(new PrintWriter(out));
            String trace = out.toString();

            request.setAttribute(GlobalConstants.REQUEST_ERROR_REAL_REASON, trace);

            return mapping.findForward("androidCaLogin");
        }
    }

    /**
     * 验证参数合法性
     * 
     * @param request
     *            HttpServletRequest
     * @return true 通过 false 不通过
     */
    protected boolean filterParam(String paramName) {
       if(StringUtils.isNotBlank(paramName)){
    	String regex="^[A-Za-z0-9]+$";   //匹配由数字和26个英文字母组成的字符串
		boolean result= Pattern.matches(regex, paramName);
        return result;
       }
       return true;
    }
    /**
     * 验证用户合法性
     * 
     * @param request
     *            HttpServletRequest
     * @return true 通过 false 不通过
     */
    protected boolean checkUserInfo(HttpServletRequest request) {
        String certNo = (String) request.getSession().getAttribute("certNo");
        if (StringUtils.isNotBlank(certNo)) {
            return true;
        }
        return false;
    }
    /**
     * 设置页面MEAT属性
     * 
     * @param response
     *            HttpServletResponse
     */
    protected void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
    }

    /**
     * 格式化业务逻辑异常信息
     * 
     * @param errorMessage
     *            格式化前的异常信息
     * @return 格式化后的异常信息
     */
    public static String formatBusinessExceptionMsg(String errorMessage) {
        // 业务逻辑错误都是可控的，应用中报出来的，
        if (StringUtils.isEmpty(errorMessage)) {
            return "系统出现逻辑错误，但错误信息未被描述！";
        } else {
            return errorMessage.trim();
        }
    }

    /**
     * 格式化非业务逻辑异常信息
     * 
     * @return 格式化后的异常信息
     */
    public static String getExceptionMsg() {
        // 不管异常是什么原因导致的，都应该显示下面这句话
        return "系统出现异常错误，请和管理员联系！";
    }
}
