package com.wonders.common.simplequery.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.simplequery.bo.QueryColumn;
import com.wonders.common.simplequery.bo.QueryCon;
import com.wonders.common.simplequery.bo.QuerySave;
import com.wonders.common.simplequery.bo.QueryTheme;
import com.wonders.common.simplequery.service.SimpleQueryService;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.jdbc.page.PageList;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;

public class SimpleQueryAction extends BaseAction {

    SimpleQueryService simpleQueryService = SimpleQueryService.getInstance();

    // ������ѡ��ҳ��
    public ActionForward toTheme(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List themeIdList = new ArrayList();
        List themeList = new ArrayList();
        List saveList = new ArrayList();

        themeList = simpleQueryService.queryThemeList(themeIdList);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

        saveList = simpleQueryService.querySaveList(String.valueOf(userInfo.getUserId()));

        request.getSession().setAttribute("queryThemeList", themeList);
        request.getSession().setAttribute("userSaveList", saveList);

        return mapping.findForward("queryTheme");
    }

    // �������ҳ���ѡ��
    public ActionForward toDefine(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String themeId = request.getParameter("themeId");
        request.getSession().setAttribute("themeId", themeId);

        List columnList = new ArrayList();
        columnList = simpleQueryService.queryColumnList(request.getSession().getAttribute("themeId").toString());

        request.getSession().setAttribute("columnList", columnList);
        return mapping.findForward("queryDefine");
    }

    // �����һ��
    public ActionForward toCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String[] colConSelect = null;
        String[] colRsSelect = null;
        String[] colSortSelect = null;
        StringBuffer sortStr = new StringBuffer();
        if (request.getParameterValues("conSelect") != null) {
            colConSelect = request.getParameterValues("conSelect");
        }
        if (request.getParameterValues("resultSelect") != null) {
            colRsSelect = request.getParameterValues("resultSelect");
        }
        if (request.getParameterValues("sortSelect") != null) {
            colSortSelect = request.getParameterValues("sortSelect");
        }

        StringBuffer conditionCol = new StringBuffer();
        StringBuffer resultCol = new StringBuffer();
        StringBuffer sortCol = new StringBuffer();
        if (colConSelect != null)
            for (int i = 0; i < colConSelect.length; i++) {
                if (i == 0)
                    conditionCol.append(colConSelect[i]);
                else
                    conditionCol.append(",").append(colConSelect[i]);
            }
        // ��column�ֶ��б�����ʾ���ֶκͲ���ʾ���ڳ������ֶ���ӵ� ����ַ�����
        List columnList = (List) request.getSession().getAttribute("columnList");
        for (int j = 0; j < columnList.size(); j++) {
            QueryColumn qc = (QueryColumn) columnList.get(j);
            if (qc.getColumnType().equals("1") || qc.getColumnType().equals("3"))

                resultCol.append(qc.getColId()).append(",");
        }
        if (colRsSelect != null)
            for (int i = 0; i < colRsSelect.length; i++) {
                if (i == 0)
                    resultCol.append(colRsSelect[i]);
                else
                    resultCol.append(",").append(colRsSelect[i]);
            }
        if (colSortSelect != null)
            for (int i = 0; i < colSortSelect.length; i++) {
                sortStr.append(colSortSelect[i]).append("&");
                if (i == 0)
                    sortCol.append(colSortSelect[i].substring(0, 4));
                else
                    sortCol.append(",").append(colSortSelect[i].substring(0, 4));
            }
        System.out.println(resultCol);

        System.out.println(sortStr);
        System.out.println(sortCol);
        // �Ѽ����б����(sortId,acs&sortID,desc)����session
        request.getSession().setAttribute("sortStr", String.valueOf(sortStr));
        // ������������������ֶε�id ����session
        request.getSession().setAttribute("conditionCol", conditionCol.toString());
        request.getSession().setAttribute("resultCol", resultCol.toString());
        request.getSession().setAttribute("sortCol", sortCol.toString());
        // �����ֶε���ϸ��Ϣ
        request.getSession().setAttribute("conditionColList",
                simpleQueryService.queryColumnList(simpleQueryService.getNewString(conditionCol.toString())));

        return mapping.findForward("queryCondition");
    }

    // �������
    public ActionForward toSave(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // ����id�õ�lolumn�б�
        List conList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("conditionCol"))));
        List resultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("resultCol"))));
        List sortList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("sortCol"))));

        StringBuffer conBuffer = new StringBuffer();
        StringBuffer resultBuffer = new StringBuffer();
        StringBuffer sortBuffer = new StringBuffer();
        // �Ѹ����ֶε�id���ص�column�б�ѭ��ȡ��name,�ö��Ÿ���ƴ���ַ���
        for (int i = 0; i < conList.size(); i++) {
            if (i == 0)
                conBuffer.append(((QueryColumn) conList.get(i)).getName());
            else
                conBuffer.append(",").append(((QueryColumn) conList.get(i)).getName());

        }
        for (int i = 0; i < resultList.size(); i++) {
            if (i == 0)
                resultBuffer.append(((QueryColumn) resultList.get(i)).getName());
            else
                resultBuffer.append(",").append(((QueryColumn) resultList.get(i)).getName());

        }
        for (int i = 0; i < sortList.size(); i++) {
            if (i == 0)
                sortBuffer.append(((QueryColumn) sortList.get(i)).getName());
            else
                sortBuffer.append(",").append(((QueryColumn) sortList.get(i)).getName());

        }
        // �Ѳ�ѯ����������˼�ŵ�request�����ڽ�����ʾ
        request.setAttribute("conForSaveStr", conBuffer);
        request.setAttribute("resultForSaveStr", resultBuffer);
        request.setAttribute("sortForSaveStr", sortBuffer);

        return mapping.findForward("querySave");
    }

    // �����ѯ
    public ActionForward toQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        StringBuffer rsBuffer = new StringBuffer();
        StringBuffer conBuffer = new StringBuffer();
        StringBuffer sortForDbStr = new StringBuffer();

        List conForResultList = null;
        List rsForResultList = null;

        // ������ʾ����������û��Լ��������������ʾ��Ϣ
        StringBuffer conForResultStr = new StringBuffer();

        if (request.getSession().getAttribute("conditionCol") != null) {
            conForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(request.getSession()
                    .getAttribute("conditionCol").toString()));
            // ��ŷ�ʱ�����͵�Parameter��value
            HashMap map = new HashMap();
            // ���ʱ������Parameter��value
            HashMap dateMap = new HashMap();
            // ������ݿ��е�ʱ�����͵Ĳ�ѯ�ֶ�
            List dateList = new ArrayList();

            // �õ��������������е�����ParameterNames
            Enumeration n = request.getParameterNames();
            // �ѷ�ʱ����ӵ���ͼ��ѯ->�����ַ�����
            while (n.hasMoreElements()) {
                String element = n.nextElement().toString();

                String val = request.getParameter(element);

                if (!(element.equals("method") || element.equals("query")) && (!val.trim().equals(""))) {
                    if (element.substring(element.length() - 4, element.length()).equals("DATE")) {
                        dateMap.put(element, val);
                        if (element.substring(element.length() - 7, element.length() - 4).equals("sta")) {
                            dateList.add(element.substring(0, element.length() - 7));
                        }
                    } else if (element.equals("DISTRICT_ID")) {
                        conBuffer.append(" and ").append(element).append(" = '").append(val).append("' ");

                        map.put(element, val);
                    } else {
                        conBuffer.append(" and ").append(element).append(" like '%").append(val).append("%' ");

                        map.put(element, val);

                    }
                }

            }
            // ��ʱ����ӵ���ͼ��ѯ->�����ַ�����
            for (int i = 0; i < dateList.size(); i++) {
                conBuffer.append(" and ").append(dateList.get(i)).append(" between to_date('").append(
                        dateMap.get(dateList.get(i).toString() + "staDATE")).append(
                        " 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date('");
                conBuffer.append(dateMap.get(dateList.get(i).toString() + "endDATE")).append(
                        " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
            }

            // ��������ʾ�������ֶκ�ֵ��
            for (int i = 0; i < conForResultList.size(); i++) {
                QueryColumn queryColumn = (QueryColumn) conForResultList.get(i);
                if (queryColumn.getEditType().equals("3")) {
                    conForResultStr.append(queryColumn.getName()).append(":").append(
                            dateMap.get(queryColumn.getNameLetter() + "staDATE"));
                    conForResultStr.append("��").append(dateMap.get(queryColumn.getNameLetter() + "endDATE")).append(
                            ". ");
                } else {
                    String dicName = DicDataUtil.getInstance().getDicItemName(queryColumn.getDicId().intValue(),
                            map.get(queryColumn.getNameLetter()));
                    if (dicName != null)
                        conForResultStr.append(queryColumn.getName()).append(":").append(dicName).append(". ");
                }
            }
        }

        if (request.getSession().getAttribute("resultCol") != null) {
            // ������ʾ�������ϸ��Ϣ
            rsForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                    .getSession().getAttribute("resultCol"))));

            // �õ�session�б���Ľ��Idƴ�������ַ���
            String resultStr = String.valueOf(request.getSession().getAttribute("resultCol"));
            // ����session�е�ֵ�õ�columnList
            List resultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(resultStr));

            // ��ӵ���ͼ��ѯ->����ַ�����
            for (int i = 0; i < resultList.size(); i++) {
                QueryColumn qc = (QueryColumn) resultList.get(i);
                if (i == 0)
                    rsBuffer.append(qc.getNameLetter());
                else
                    rsBuffer.append(",").append(qc.getNameLetter());
            }

        }
        if (request.getSession().getAttribute("sortCol") != null
                && request.getSession().getAttribute("sortStr") != null) {
            // ����session�У�sortid,asc&sortid,desc���ַ���
            String sortStr = request.getSession().getAttribute("sortStr").toString();
            String[] spitSortStr = sortStr.split("&");

            HashMap mapChange = new HashMap();
            if (sortStr != null && sortStr.length() > 0)
                for (int i = 0; i < spitSortStr.length; i++) {
                    String[] spitSubStr = spitSortStr[i].split(",");
                    mapChange.put(spitSubStr[0], spitSubStr[1]);
                }

            List sortByIdList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String
                    .valueOf(request.getSession().getAttribute("sortCol"))));
            // ����������ͼ��ѯ�е�order by ����ַ��� for->db

            for (int i = sortByIdList.size() - 1; i > -1; i--) {
                QueryColumn queryColumn = (QueryColumn) sortByIdList.get(i);
                if (i == sortByIdList.size() - 1)
                    sortForDbStr.append(queryColumn.getNameLetter()).append(" ").append(
                            mapChange.get(queryColumn.getColId())).append(" ");
                else
                    sortForDbStr.append(",").append(queryColumn.getNameLetter()).append(" ").append(
                            mapChange.get(queryColumn.getColId())).append(" ");
            }
        }

        List queryTheme = simpleQueryService.getQueryThemeById(String.valueOf(request.getSession().getAttribute(
                "themeId")));
        String viewName = ((QueryTheme) queryTheme.get(0)).getViewName();

        StringBuffer querySql = new StringBuffer();

        querySql.append("select ").append(rsBuffer).append(" from ").append(viewName).append(" where 1=1 ").append(
                conBuffer);
        if (!sortForDbStr.toString().trim().equals(""))
            querySql.append(" order by ").append(sortForDbStr);
        // Ϊ��ҳ����querySql�ַ���
        request.setAttribute("querySql", querySql);
        System.out.println(querySql);

        int currentPage = 0;
        if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }
        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        String countSql = "select count(*) from " + viewName + " where 1=1 " + conBuffer;
        // Ϊ��ҳ����countSql�ַ���
        request.setAttribute("countSql", countSql);
        QueryCon queryCon = new QueryCon();

        queryCon.setCurrentPage(currentPage);
        queryCon.setPageSize(pageSize);
        queryCon.setCountSql(countSql);
        queryCon.setQuerySql(querySql.toString());
        PageList rsList = new PageList();
        rsList = (PageList) simpleQueryService.queryFineResultList(queryCon);
        // �������
        request.setAttribute("rsList", rsList);
        // ����ֶ���Ϣ
        request.setAttribute("rsForResultList", rsForResultList);
        // ��������������ʾ��Ϣ
        request.getSession().setAttribute("conForResultStr", conForResultStr);
        return mapping.findForward("queryResult");
    }

    // ��ҳ
    public ActionForward querySubPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List rsForResultList = null;
        int currentPage = 0;
        if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }
        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

        QueryCon queryCon = new QueryCon();

        String countSql = String.valueOf(request.getParameter("countSql"));
        String querySql = String.valueOf(request.getParameter("querySql"));
        queryCon.setCurrentPage(currentPage);
        queryCon.setPageSize(pageSize);
        queryCon.setCountSql(countSql);
        queryCon.setQuerySql(querySql);
        PageList rsList = new PageList();
        rsForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("resultCol"))));
        rsList = (PageList) simpleQueryService.queryFineResultList(queryCon);

        // �������
        request.setAttribute("rsList", rsList);
        request.setAttribute("countSql", countSql);
        request.setAttribute("querySql", querySql);
        request.setAttribute("rsForResultList", rsForResultList);

        return mapping.findForward("queryResult");

    }

    // �������������
    public ActionForward saveQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String temp = request.getParameter("save_text");
        // String saveText = "";
        // saveText = new String(temp.getBytes("ISO-8859-1"),"GBK");
        // ����query
        QuerySave query = new QuerySave();
        query.setName(temp);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userId = String.valueOf(userInfo.getUserId());
        query.setUserId(userId);
        query.setConCols(String.valueOf(request.getSession().getAttribute("conditionCol")));
        query.setRsCols(String.valueOf(request.getSession().getAttribute("resultCol")));
        query.setSortCols(String.valueOf(request.getSession().getAttribute("sortStr")));
        query.setThemeId(String.valueOf(request.getSession().getAttribute("themeId")));

        // ��ǰʱ������һ��themeId
        Date date = new Date();
        query.setSaveId((date.getTime()) + "");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String saveDate = formatter.format(date);
        query.setSaveDate(DateUtil.string2Timestamp(saveDate, DateUtil.FORMAT_DATE));

        simpleQueryService.addQuerySave(query);

        List saveList = new ArrayList();
        saveList = simpleQueryService.querySaveList(userId);
        request.getSession().setAttribute("userSaveList", saveList);
        return mapping.findForward("queryTheme");
    }

    // ���ѡ������ҳ���ɾ��
    public ActionForward toRemove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String deleteSaveId = request.getParameter("saveId");
        simpleQueryService.removeQuerySave(deleteSaveId);

        List saveList = new ArrayList();
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userId = String.valueOf(userInfo.getUserId());
        saveList = simpleQueryService.querySaveList(userId);
        request.getSession().setAttribute("userSaveList", saveList);

        return mapping.findForward("queryTheme");

    }

    // �������ҳ��Ĳ�ѯ
    public ActionForward toDefineCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List querySaveList = new ArrayList();
        List conditionColList = null;

        String saveId = request.getParameter("saveId");
        querySaveList = simpleQueryService.querySave(saveId);

        if (((QuerySave) querySaveList.get(0)).getConCols() != null)
            conditionColList = simpleQueryService.queryColumnList(simpleQueryService
                    .getNewString(((QuerySave) querySaveList.get(0)).getConCols()));

        // �����ݿ��д�ŵ�sortId,asc&sortId,desc���ַ����и��sortId,sortId���͵��ַ���
        StringBuffer sortCol = new StringBuffer();
        if (((QuerySave) querySaveList.get(0)).getSortCols() != null) {
            String[] spitSortStr = ((QuerySave) querySaveList.get(0)).getSortCols().split("&");
            for (int i = 0; i < spitSortStr.length; i++) {
                sortCol.append(spitSortStr[i].substring(0, 4)).append(",");

            }
        }

        request.getSession().setAttribute("sortCol", sortCol);
        request.getSession().setAttribute("conditionCol", ((QuerySave) querySaveList.get(0)).getConCols());
        request.getSession().setAttribute("resultCol", ((QuerySave) querySaveList.get(0)).getRsCols());

        request.getSession().setAttribute("sortStr", ((QuerySave) querySaveList.get(0)).getSortCols());

        request.getSession().setAttribute("themeId", ((QuerySave) querySaveList.get(0)).getThemeId());
        request.getSession().setAttribute("conditionColList", conditionColList);

        return mapping.findForward("queryCondition");

    }

    // ����ȫ����ѯ���
    public ActionForward resultToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        QueryCon queryCon = new QueryCon();
        List rsForResultList = null;
        queryCon.setQuerySql(String.valueOf(request.getParameter("querySql")));
        List rsAllList = new ArrayList();
        rsAllList = (List) simpleQueryService.queryAllResultList(queryCon);
        // ���н������
        request.setAttribute("rsAllList", rsAllList);
        rsForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("resultCol"))));
        request.setAttribute("rsForResultList", rsForResultList);
        return mapping.findForward("saveAllQuery");
    }

}
