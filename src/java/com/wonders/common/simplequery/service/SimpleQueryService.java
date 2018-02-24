package com.wonders.common.simplequery.service;

import java.util.List;
import java.util.ArrayList;
import com.wonders.jdbc.page.PageList;

import com.wonders.common.simplequery.bo.QueryCon;
import com.wonders.common.simplequery.bo.QuerySave;
import com.wonders.common.simplequery.bo.QueryTheme;
import com.wonders.jdbc.BaseService;

public class SimpleQueryService extends BaseService {
	private static SimpleQueryService instance = null;
	public static synchronized SimpleQueryService getInstance(){
		if(instance == null)
			instance = new SimpleQueryService();
		return instance;
	}
	public List getQueryThemeById(String themeId){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TDSC_QUERY_THEME where theme_id = '").append(themeId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),"com.wonders.common.simplequery.bo.QueryTheme");
		return queryThemeList;
	}
	
	//有权限查询的主题
	public List queryThemeList(List themeIdList){
	
		StringBuffer querySql = new StringBuffer();
		querySql.append("select * from TDSC_QUERY_THEME ");
		List queryThemeList = new ArrayList();
		//TODO 根据权限进行过滤
		
		queryThemeList = getResultPojoList(querySql.toString(),"com.wonders.common.simplequery.bo.QueryTheme");
		
		logger.debug("");
		return queryThemeList;
		
	}
	//用户保存的查询
	public List querySaveList(String userId){
		StringBuffer querySql = new StringBuffer();
		querySql.append("select * from TDSC_QUERY_SAVE where user_Id = '").append(userId).append("'");
		List querySaveList = new ArrayList();
		
		querySaveList = getResultPojoList(querySql.toString(),"com.wonders.common.simplequery.bo.QuerySave");
		
		return querySaveList ;
	}
	// 主题对应的字段
	public List queryColumnList(String themeId){
		
		StringBuffer querySql = new StringBuffer();
		querySql.append("select * from TDSC_QUERY_COLUMN where theme_id = ").append(themeId);
		List columnList = new ArrayList();
		columnList = getResultPojoList(querySql.toString(), "com.wonders.common.simplequery.bo.QueryColumn");		
		
		return columnList;
	}
	//TODO 条件字段列表
	public List queryColumnList(String [] colIds ){
		StringBuffer querySql = new StringBuffer();
		querySql.append("select * from TDSC_QUERY_COLUMN where col_id in (");
		for(int i=0;i<colIds.length-1;i++)
		{			
			querySql.append("'").append(colIds[i]).append("',");
		}
		querySql.append("'").append(colIds[colIds.length-1]).append("')");

		List columnList = new ArrayList();
		columnList = getResultPojoList(querySql.toString(), "com.wonders.common.simplequery.bo.QueryColumn");
		
		return columnList;
	}
	//新增一条查询定义
	public void addQuerySave(QuerySave query){
		
		StringBuffer querySaveSql = new StringBuffer();
		querySaveSql.append("insert into TDSC_QUERY_SAVE values('").append(query.getSaveId()).append("','").append(query.getThemeId()).append("','").append(query.getUserId()).append("','").append(query.getName());
		querySaveSql.append("','").append(query.getConCols()).append("','").append(query.getRsCols()).append("','").append(query.getSortCols()).append("', to_date('").append(query.getSaveDate().toString().substring(0, 18)).append("' , 'yyyy-mm-dd hh24:mi:ss'))");
		
		execUpdate(querySaveSql.toString());	
		
	}
	//查询结果
	public PageList queryFineResultList(QueryCon queryCon){		
		
		PageList querySaveList = new PageList();
		querySaveList = getResultMapPageList(queryCon.getCountSql(),queryCon.getQuerySql(),queryCon.getPageSize(),queryCon.getCurrentPage());	
		
		return querySaveList;
	}
	//全部查询结果
	public List queryAllResultList(QueryCon queryCon){		
		
		List querySaveList = new ArrayList();
		querySaveList = getResultMapList(queryCon.getQuerySql());	
		
		return querySaveList;
	}
	//删除保存的查询定义
	public void removeQuerySave(String saveId){
		StringBuffer querySql = new StringBuffer();
		querySql.append("delete from TDSC_QUERY_SAVE where save_Id = ").append(saveId);
		execUpdate(querySql.toString());	
	}
	//得到查询保存的对象
	public List querySave(String saveId){
		StringBuffer querySql = new StringBuffer();
		querySql.append("select *  from TDSC_QUERY_SAVE where save_Id = ").append(saveId);
		List querySaveList = new ArrayList();
		querySaveList = getResultPojoList(querySql.toString(), "com.wonders.common.simplequery.bo.QuerySave");		
		
		return querySaveList;
	}
	//根据一个字符串生成一个字符数组
	public String [] getNewString (String oldString){
		String [] newString = null;
		newString = oldString.split(",");		
		return newString ;
	}

	
}












