package com.wonders.tdsc.exchange.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.bo.TdscBlockPart;


public class JianChaBaseService extends BaseService {
	
	private static JianChaBaseService instance = null;
	public static synchronized JianChaBaseService getInstance(){
		if(instance == null)
			instance = new JianChaBaseService();
		return instance;
	}

	/**
	 * 查询TDSC_BLOCK_INFO_VIEW视图全部数据
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString 查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @return
	 */
	public List queryBolckInfoAll(String bo,String findString){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_BLOCK_INFO_VIEW ");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	
	/**
	 * 通过起止时间查询TDSC_BLOCK_INFO_VIEW视图数据，其中startTime和endTime不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param startTime 开始时间，格式 yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间，格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List queryBlockInfoByTime(String bo,String findString,String startTime,String endTime){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_BLOCK_INFO_VIEW ");
		if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) return null;
		int flag=0;
		if(StringUtils.isNotBlank(startTime)){
		sql.append(" where ACTION_DATE_BLOCK > to_date('").append(startTime).append("','yyyy-mm-dd hh24:mi:ss')");
		flag=1;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(flag == 1)
		     sql.append(" and ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
			else
			 sql.append(" where ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
		}	
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
		
	}
	
	/**
	 * 通过地块编号查询TDSC_BLOCK_INFO_VIEW视图数据，其中blockId不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param blockId 地块编号
	 * @return
	 */
	public List queryBlockInfoById(String bo,String findString,String blockId){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append(" from WS_BLOCK_INFO_VIEW where BLOCK_ID = '").append(blockId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}

	/**
	 * 查询TDSC_PLAN_RESULT_VIEW视图全部数据
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString 查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @return
	 */
	public List queryPlanResultAll(String bo,String findString){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append(" from WS_PLAN_RESULT_VIEW ");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}

	/**
	 * 通过起止时间查询ACTION_DATE_BLOCK视图数据，其中startTime和endTime不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param startTime 开始时间，格式 yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间，格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List queryPlanResultByTime(String bo,String findString,String startTime,String endTime){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_PLAN_RESULT_VIEW ");
		if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) return null;
		int flag=0;
		if(StringUtils.isNotBlank(startTime)){
		sql.append(" where ACTION_DATE_BLOCK > to_date('").append(startTime).append("','yyyy-mm-dd hh24:mi:ss')");
		flag=1;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(flag == 1)
		     sql.append(" and ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
			else
			 sql.append(" where ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
		}	
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}

	/**
	 * 通过地块编号查询TDSC_PLAN_RESULT_VIEW视图数据，其中blockId不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param blockId 地块编号
	 * @return
	 */
	public List queryPlanResultById(String bo,String findString,String blockId){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_PLAN_RESULT_VIEW where BLOCK_ID = '").append(blockId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	/**
	 * 查询WS_BLOCK_QQJC_VIEW视图全部数据
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString 查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @return
	 */
	public List queryQqjcInfoAll(String bo,String findString){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append(" from WS_BLOCK_QQJC_VIEW ");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}

	/**
	 * 通过起止时间查询WS_BLOCK_QQJC_VIEW视图数据，其中startTime和endTime不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param startTime 开始时间，格式 yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间，格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List queryQqjcInfoByTime(String bo,String findString,String startTime,String endTime){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_BLOCK_QQJC_VIEW ");
		if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) return null;
		int flag=0;
		if(StringUtils.isNotBlank(startTime)){
		sql.append(" where ACTION_DATE_BLOCK > to_date('").append(startTime).append("','yyyy-mm-dd hh24:mi:ss')");
		flag=1;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(flag == 1)
		     sql.append(" and ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
			else
			 sql.append(" where ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
		}	
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}

	/**
	 * 通过地块编号查询WS_BLOCK_QQJC_VIEW视图数据，其中blockId不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param blockId 地块编号
	 * @return
	 */
	public List queryQqjcInfoById(String bo,String findString,String blockId){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_BLOCK_QQJC_VIEW where BLOCK_ID = '").append(blockId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	
	

	/**
	 * 查询WS_FILE_NOTICE_VIEW视图全部数据
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString 查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @return
	 */
	public List queryFileNoticeAll(String bo,String findString){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append(" from WS_FILE_NOTICE_VIEW ");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	/**
	 * 通过地块编号查询WS_FILE_NOTICE_VIEW视图数据，其中blockId不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param blockId 地块编号
	 * @return
	 */
	public List queryFileNoticeById(String bo,String findString,String blockId){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_FILE_NOTICE_VIEW where BLOCK_ID = '").append(blockId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	/**
	 * 通过起止时间查询WS_FILE_NOTICE_VIEW视图数据，其中startTime和endTime不能全为空
	 * @param bo 类名 例如"com.wonders.tdsc.exchange.bo.BASIC"
	 * @param findString  查询字段字符串 例如：全查应为"*" ；查部分字段则用逗号隔开 A,B,C
	 * @param startTime 开始时间，格式 yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间，格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List queryFileNoticeByTime(String bo,String findString,String startTime,String endTime,String columnName){
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(findString).append("  from WS_FILE_NOTICE_VIEW ");
		if(StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) return null;
		int flag=0;
		if(StringUtils.isNotBlank(startTime)){
		sql.append(" where ").append(columnName).append(" > to_date('").append(startTime).append("','yyyy-mm-dd hh24:mi:ss')");
		flag=1;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(flag == 1)
		     sql.append(" and  ").append(columnName).append(" < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
			else
			 sql.append(" where ").append(columnName).append(" < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
		}	
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),bo);
		return queryThemeList;
	}
	/**
	 * 将文件转换成字符串
	 * 
	 * @param filePathName
	 * @return
	 */
	public String convertWord2String(String filePathName) {
		String fileStr = null;
		try {
			FileInputStream in = new FileInputStream(filePathName);

			int i = in.available();
			byte[] fb = new byte[i];
			in.read(fb);
			in.close();

			fileStr = new String(fb);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return fileStr;
	}
	
	
	/**
	 * 根据blockId查询子地块信息
	 * @return
	 */
	public List queryTdscBlockPartByBlockId(String blockId){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TDSC_BLOCK_PART where BLOCK_ID = '").append(blockId).append("'");
		List queryThemeList = new ArrayList();
		queryThemeList = getResultPojoList(sql.toString(),TdscBlockPart.class.getName());
		return queryThemeList;
	}
	

	  public List queryAppNodeAll(String bo, String findString)
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select ").append(findString).append("  from TDSC_BLOCK_APP_VIEW ");
	    List queryThemeList = new ArrayList();
	    queryThemeList = getResultPojoList(sql.toString(), bo);
	    return queryThemeList;
	  }

	  public List queryAppNodeByTime(String bo, String findString, String startTime, String endTime)
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select ").append(findString).append("  from TDSC_BLOCK_APP_VIEW ");
	    if ((StringUtils.isBlank(startTime)) && (StringUtils.isBlank(endTime))) return null;
	    int flag = 0;
	    if (StringUtils.isNotBlank(startTime)) {
	      sql.append(" where ACTION_DATE_BLOCK > to_date('").append(startTime).append("','yyyy-mm-dd hh24:mi:ss')");
	      flag = 1;
	    }
	    if (StringUtils.isNotBlank(endTime)) {
	      if (flag == 1)
	        sql.append(" and ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
	      else
	        sql.append(" where ACTION_DATE_BLOCK < to_date('").append(endTime).append("','yyyy-mm-dd hh24:mi:ss')");
	    }
	    List queryThemeList = new ArrayList();
	    queryThemeList = getResultPojoList(sql.toString(), bo);
	    return queryThemeList;
	  }

	  public List queryAppNodeById(String bo, String findString, String blockId)
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select ").append(findString).append(" from TDSC_BLOCK_APP_VIEW where BLOCK_ID = '").append(blockId).append("'");
	    List queryThemeList = new ArrayList();
	    queryThemeList = getResultPojoList(sql.toString(), bo);
	    return queryThemeList;
	  }

	  public List queryNodeListByAppId(String bo, String appId)
	  {
	    StringBuffer sql = new StringBuffer();

	    sql.append("select * from tdsc_app_node_stat where NODE_STAT = '01' and  node_id not in ('19','20','21','22','23','24','25','26','27','28','29') and app_id ='").append(appId).append("'  order by node_id");
	    List queryThemeList = new ArrayList();
	    queryThemeList = getResultPojoList(sql.toString(), bo);
	    return queryThemeList;
	  }
}
