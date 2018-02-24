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
 * Struts Action �����ࡣ
 */

public class BaseAction extends DispatchAction {

    /** ��־ */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * ע������ת�������Ա����������⣺
     * 
     * 1��������ֵ�ͣ���Դ�����е��ֶ�Ϊnull������ת����0�ȣ�
     * 
     * 2������java.util.Date������ת������ʽΪ��yyyy-MM-dd����
     */
    static {
        BeanUtils.registerConverter();
    }

    /**
     * ��FormBean�е�����ͨ��BeanUtils��copyProperties()�󶨵�Object��.
     * 
     * @param object
     *            bo����
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
     * Actionִ�У������˸����еķ������������쳣����
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
            // ����û���Ϣ��֤ʧ�ܣ�ת����¼ҳ��
            if (this.checkUserInfo(request) == false) {
                return mapping.findForward("androidCaLogin");
            }

            // ����ҳ��MEAT����
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
     * ��֤�����Ϸ���
     * 
     * @param request
     *            HttpServletRequest
     * @return true ͨ�� false ��ͨ��
     */
    protected boolean filterParam(String paramName) {
       if(StringUtils.isNotBlank(paramName)){
    	String regex="^[A-Za-z0-9]+$";   //ƥ�������ֺ�26��Ӣ����ĸ��ɵ��ַ���
		boolean result= Pattern.matches(regex, paramName);
        return result;
       }
       return true;
    }
    /**
     * ��֤�û��Ϸ���
     * 
     * @param request
     *            HttpServletRequest
     * @return true ͨ�� false ��ͨ��
     */
    protected boolean checkUserInfo(HttpServletRequest request) {
        String certNo = (String) request.getSession().getAttribute("certNo");
        if (StringUtils.isNotBlank(certNo)) {
            return true;
        }
        return false;
    }
    /**
     * ����ҳ��MEAT����
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
     * ��ʽ��ҵ���߼��쳣��Ϣ
     * 
     * @param errorMessage
     *            ��ʽ��ǰ���쳣��Ϣ
     * @return ��ʽ������쳣��Ϣ
     */
    public static String formatBusinessExceptionMsg(String errorMessage) {
        // ҵ���߼������ǿɿصģ�Ӧ���б������ģ�
        if (StringUtils.isEmpty(errorMessage)) {
            return "ϵͳ�����߼����󣬵�������Ϣδ��������";
        } else {
            return errorMessage.trim();
        }
    }

    /**
     * ��ʽ����ҵ���߼��쳣��Ϣ
     * 
     * @return ��ʽ������쳣��Ϣ
     */
    public static String getExceptionMsg() {
        // �����쳣��ʲôԭ���µģ���Ӧ����ʾ������仰
        return "ϵͳ�����쳣������͹���Ա��ϵ��";
    }
}
