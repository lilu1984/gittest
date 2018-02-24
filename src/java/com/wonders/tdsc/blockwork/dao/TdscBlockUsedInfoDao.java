package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.bo.condition.TdscBlockUsedInfoCondition;

public class TdscBlockUsedInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockUsedInfo.class;
	}
	
	public void saveTdscBlockUsedInfo(TdscBlockUsedInfo tdscBlockUsedInfo)
	{		
		save(tdscBlockUsedInfo);		
	}	
	
	public void deleteTdscBlockUsedInfo(TdscBlockUsedInfo tdscBlockUsedInfo)
	{
		delete(tdscBlockUsedInfo);
	}
	

	public PageList findPageList(TdscBlockUsedInfoCondition condition) {
	    // 用户信息查询
		StringBuffer hql = new StringBuffer(
				"from TdscBlockUsedInfo a where 1 = 1");
		
		// 组装条件语句
		hql.append(makeWhereClause(condition));
		
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + "";
        
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(),
                condition.getCurrentPage());
		
		return pageList;
	}
	
	
	public TdscBlockUsedInfo findTdscBlockUsedInfo(String blockUsedId) {
	    // 用户信息查询
		return (TdscBlockUsedInfo)this.get(blockUsedId);
	}
	
    /**
     * 根据用户查询条件对象组装条件语句
     * 
     * @param condition
     *            用户查询条件对象
     * @return String 条件语句
     */
	private String makeWhereClause(TdscBlockUsedInfoCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getBlockUsedId())) {
			whereClause.append(" and a.blockUsedId like '%").append(
					condition.getBlockUsedId().trim()).append("%' ");
		}	
		
		if (StringUtils.isNotEmpty(condition.getBlockId())) {
			whereClause.append(" and a.blockId like '%").append(
					condition.getBlockId().trim()).append("%' ");
		}			
		return whereClause.toString();
	}	
    
    /**
     * 通过地块ID来查询土地用途信息
     * @param blockId
     * @return
     * 20071117*
     */
    public List queryTdscBlockUsedInfoListByBlockId(String blockId){
        StringBuffer hql = new StringBuffer("from TdscBlockUsedInfo a where a.blockId = '").append(blockId).append("'");
        return this.findList(hql.toString());
    }
}
