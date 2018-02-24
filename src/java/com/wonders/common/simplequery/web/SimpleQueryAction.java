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

    // 打开主题选择页面
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

    // 点击主题页面的选择
    public ActionForward toDefine(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String themeId = request.getParameter("themeId");
        request.getSession().setAttribute("themeId", themeId);

        List columnList = new ArrayList();
        columnList = simpleQueryService.queryColumnList(request.getSession().getAttribute("themeId").toString());

        request.getSession().setAttribute("columnList", columnList);
        return mapping.findForward("queryDefine");
    }

    // 点击下一步
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
        // 把column字段中必须显示的字段和不显示用于超链的字段添加到 结果字符串中
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
        // 把见面中保存的(sortId,acs&sortID,desc)放入session
        request.getSession().setAttribute("sortStr", String.valueOf(sortStr));
        // 把条件，结果，排序字段的id 放入session
        request.getSession().setAttribute("conditionCol", conditionCol.toString());
        request.getSession().setAttribute("resultCol", resultCol.toString());
        request.getSession().setAttribute("sortCol", sortCol.toString());
        // 条件字段的详细信息
        request.getSession().setAttribute("conditionColList",
                simpleQueryService.queryColumnList(simpleQueryService.getNewString(conditionCol.toString())));

        return mapping.findForward("queryCondition");
    }

    // 点击保存
    public ActionForward toSave(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // 根据id得到lolumn列表
        List conList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("conditionCol"))));
        List resultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("resultCol"))));
        List sortList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("sortCol"))));

        StringBuffer conBuffer = new StringBuffer();
        StringBuffer resultBuffer = new StringBuffer();
        StringBuffer sortBuffer = new StringBuffer();
        // 把根据字段的id返回的column列表循环取出name,用逗号隔开拼成字符串
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
        // 把查询到的中文意思放到request中用于界面显示
        request.setAttribute("conForSaveStr", conBuffer);
        request.setAttribute("resultForSaveStr", resultBuffer);
        request.setAttribute("sortForSaveStr", sortBuffer);

        return mapping.findForward("querySave");
    }

    // 点击查询
    public ActionForward toQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        StringBuffer rsBuffer = new StringBuffer();
        StringBuffer conBuffer = new StringBuffer();
        StringBuffer sortForDbStr = new StringBuffer();

        List conForResultList = null;
        List rsForResultList = null;

        // 用于显示结果界面中用户自己定义的条件的提示信息
        StringBuffer conForResultStr = new StringBuffer();

        if (request.getSession().getAttribute("conditionCol") != null) {
            conForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(request.getSession()
                    .getAttribute("conditionCol").toString()));
            // 存放非时间类型的Parameter和value
            HashMap map = new HashMap();
            // 存放时间类型Parameter和value
            HashMap dateMap = new HashMap();
            // 存放数据库中的时间类型的查询字段
            List dateList = new ArrayList();

            // 得到定义条件界面中的所有ParameterNames
            Enumeration n = request.getParameterNames();
            // 把非时间类加到视图查询->条件字符串中
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
            // 把时间类加到视图查询->条件字符串中
            for (int i = 0; i < dateList.size(); i++) {
                conBuffer.append(" and ").append(dateList.get(i)).append(" between to_date('").append(
                        dateMap.get(dateList.get(i).toString() + "staDATE")).append(
                        " 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date('");
                conBuffer.append(dateMap.get(dateList.get(i).toString() + "endDATE")).append(
                        " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
            }

            // 界面中显示条件的字段和值用
            for (int i = 0; i < conForResultList.size(); i++) {
                QueryColumn queryColumn = (QueryColumn) conForResultList.get(i);
                if (queryColumn.getEditType().equals("3")) {
                    conForResultStr.append(queryColumn.getName()).append(":").append(
                            dateMap.get(queryColumn.getNameLetter() + "staDATE"));
                    conForResultStr.append("至").append(dateMap.get(queryColumn.getNameLetter() + "endDATE")).append(
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
            // 保存显示结果的详细信息
            rsForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                    .getSession().getAttribute("resultCol"))));

            // 得到session中保存的结果Id拼起来的字符串
            String resultStr = String.valueOf(request.getSession().getAttribute("resultCol"));
            // 根据session中的值得到columnList
            List resultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(resultStr));

            // 添加到视图查询->结果字符串中
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
            // 保存session中（sortid,asc&sortid,desc）字符串
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
            // 保存最后的视图查询中的order by 后的字符串 for->db

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
        // 为分页保存querySql字符串
        request.setAttribute("querySql", querySql);
        System.out.println(querySql);

        int currentPage = 0;
        if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }
        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        String countSql = "select count(*) from " + viewName + " where 1=1 " + conBuffer;
        // 为分页保存countSql字符串
        request.setAttribute("countSql", countSql);
        QueryCon queryCon = new QueryCon();

        queryCon.setCurrentPage(currentPage);
        queryCon.setPageSize(pageSize);
        queryCon.setCountSql(countSql);
        queryCon.setQuerySql(querySql.toString());
        PageList rsList = new PageList();
        rsList = (PageList) simpleQueryService.queryFineResultList(queryCon);
        // 结果集合
        request.setAttribute("rsList", rsList);
        // 结果字段信息
        request.setAttribute("rsForResultList", rsForResultList);
        // 结果界面的条件提示信息
        request.getSession().setAttribute("conForResultStr", conForResultStr);
        return mapping.findForward("queryResult");
    }

    // 分页
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

        // 结果集合
        request.setAttribute("rsList", rsList);
        request.setAttribute("countSql", countSql);
        request.setAttribute("querySql", querySql);
        request.setAttribute("rsForResultList", rsForResultList);

        return mapping.findForward("queryResult");

    }

    // 保存界面点击保存
    public ActionForward saveQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String temp = request.getParameter("save_text");
        // String saveText = "";
        // saveText = new String(temp.getBytes("ISO-8859-1"),"GBK");
        // 保存query
        QuerySave query = new QuerySave();
        query.setName(temp);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userId = String.valueOf(userInfo.getUserId());
        query.setUserId(userId);
        query.setConCols(String.valueOf(request.getSession().getAttribute("conditionCol")));
        query.setRsCols(String.valueOf(request.getSession().getAttribute("resultCol")));
        query.setSortCols(String.valueOf(request.getSession().getAttribute("sortStr")));
        query.setThemeId(String.valueOf(request.getSession().getAttribute("themeId")));

        // 当前时间生成一个themeId
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

    // 点击选择主题页面的删除
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

    // 点击主题页面的查询
    public ActionForward toDefineCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List querySaveList = new ArrayList();
        List conditionColList = null;

        String saveId = request.getParameter("saveId");
        querySaveList = simpleQueryService.querySave(saveId);

        if (((QuerySave) querySaveList.get(0)).getConCols() != null)
            conditionColList = simpleQueryService.queryColumnList(simpleQueryService
                    .getNewString(((QuerySave) querySaveList.get(0)).getConCols()));

        // 把数据库中存放的sortId,asc&sortId,desc的字符串切割成sortId,sortId类型的字符串
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

    // 保存全部查询结果
    public ActionForward resultToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        QueryCon queryCon = new QueryCon();
        List rsForResultList = null;
        queryCon.setQuerySql(String.valueOf(request.getParameter("querySql")));
        List rsAllList = new ArrayList();
        rsAllList = (List) simpleQueryService.queryAllResultList(queryCon);
        // 所有结果集合
        request.setAttribute("rsAllList", rsAllList);
        rsForResultList = simpleQueryService.queryColumnList(simpleQueryService.getNewString(String.valueOf(request
                .getSession().getAttribute("resultCol"))));
        request.setAttribute("rsForResultList", rsForResultList);
        return mapping.findForward("saveAllQuery");
    }

}
