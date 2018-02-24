package com.wonders.tdsc.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wonders.tdsc.common.GlobalConstants;

/**
 * Ȩ�޼�鹤���ࡣ
 * <p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class AuthorityCheckUtil {

    private AuthorityCheckUtil() {
    }

    /**
     * �жϵ�¼�û��Ƿ�ӵ��ĳ�˵���Ȩ��
     * 
     * @param request
     *            HttpServletRequest
     * @param menuId
     *            �˵���Դ���
     * @return true �У� false û��
     * @throws Exception
     */
    public static boolean hasMenuPriv(HttpServletRequest request, String menuId) throws Exception {

        // //����ǳ����û��������в˵�Ȩ��
        // UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // if (user.getUserId().equals(AuthorityConstants.SUPER_USER_ID)) {
        // return true;
        // }

        // ��Session��ȡ���û��Ĳ˵�Ȩ���б�
        List list = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU);
        if (list == null) {
            return false;
        }

        // �ж��û��Ƿ�ӵ�иò˵�Ȩ��
        if (list.contains(menuId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ȡ���û���Ȩ�޵����ش����б�
     * 
     * @param request
     *            HttpServletRequest request
     * @return �����б�
     * @throws Exception
     */
    public static List getQxList(HttpServletRequest request) throws Exception {
        // //����ǳ����û�������������Ȩ��
        // UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // if (user.getUserId().equals(AuthorityConstants.SUPER_USER_ID)) {
        // Map map = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_QX);
        // List list = new ArrayList();
        //            
        // if (map != null && map.size() > 0) {
        // Iterator iter = map.keySet().iterator();
        // while(iter.hasNext()) {
        // list.add(iter.next());
        // }
        // }
        //            
        // return list;
        // }

        return (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
    }

}
