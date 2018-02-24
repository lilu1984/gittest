package com.wonders.tdsc.credit.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.bo.hibernate.HqlParameter;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscCorpInfo;
import com.wonders.tdsc.bo.condition.TdscCorpInfoCondition;

public class TdscCorpInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscCorpInfo.class;
	}

	private List paralist = null; 
	
	
	public TdscCorpInfo getYsqsCorpInfoByCondition(TdscCorpInfoCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
		hql.append(makeWhereClause(condition));
		List list = this.findList(hql.toString(), paralist);
		return list != null && list.size() > 0?(TdscCorpInfo)list.get(0):null;
	}
	
	private String makeWhereClause(TdscCorpInfoCondition condition) {
		StringBuffer hql = new StringBuffer();
		if (paralist != null) paralist.clear();
		else paralist = new ArrayList();
		if (StringUtils.isNotEmpty(condition.getBidderName())) {
			hql.append(" and yci.bidderName = ?");
			paralist.add(condition.getBidderName());
		}
		if (StringUtils.isNotEmpty(condition.getBidderNameLike())) {
			hql.append(" and yci.bidderName like ?");
			paralist.add("%"+sqlFilter_first(condition.getBidderNameLike())+"%");
		}
		if (StringUtils.isNotEmpty(condition.getBidderZjhm())) {
			hql.append(" and yci.bidderZjhm = ?");
			paralist.add(condition.getBidderZjhm());
		}
		if (StringUtils.isNotEmpty(condition.getBidderZjlx())) {
			hql.append(" and yci.bidderZjlx = ?");
			paralist.add(condition.getBidderZjlx());
		}
		if (StringUtils.isNotEmpty(condition.getCorpFrZjhm())) {
			hql.append(" and yci.corpFrZjhm = ?");
			paralist.add(condition.getCorpFrZjhm());
		}
		if (StringUtils.isNotEmpty(condition.getCorpFrZjlx())) {
			hql.append(" and yci.corpFrZjlx = ?");
			paralist.add(condition.getCorpFrZjlx());
		}
		if (StringUtils.isNotEmpty(condition.getBidderProperty())) {
            hql.append(" and yci.bidderProperty = ?");
            paralist.add(condition.getBidderProperty());
        }
		if (StringUtils.isNotEmpty(condition.getValidity())) {
            hql.append(" and yci.validity = ?");
            paralist.add(condition.getValidity());
        }
		return hql.toString();
	}
	
	/**
	 * 根据查询条件查询黑名单列表
	 * @param condition
	 * @return
	 */
	
	public PageList queryCreditListByCondition(TdscCorpInfoCondition condition) {
		
		StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
		
		//组装条件语句
		if (paralist != null) paralist.clear();
        else paralist = new ArrayList();
		
		//在黑名单中
		hql.append(" and yci.ifInBlockList=1 ");
		
		//有效性
		hql.append(" and yci.validity=1 ");
		
		//企业或自然人名称
		if(StringUtils.isNotEmpty(condition.getBidderName())){
			hql.append(" and yci.bidderName like :bidderName");
			paralist.add(new HqlParameter("bidderName","%"+ sqlFilter_first(condition.getBidderName()) +"%"));
		}
		if(StringUtils.isNotEmpty(condition.getBidderProperty())){
            hql.append(" and yci.bidderProperty = :bidderProperty");
            paralist.add(new HqlParameter("bidderProperty", condition.getBidderProperty()));
        }
		
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by yci.corpId desc";
			
		PageList pageList = this.findPageListWithHqlAdvance(countHql, queryHql, paralist, 10, condition.getCurrentPage());
		
		return pageList;
	}
	
	/**
	 * 根据查询条件查询不在黑名单中的预申请人列表
	 * @param condition
	 * @return
	 */
	
	public List queryCorpInfoListByCondition(TdscCorpInfoCondition condition){
	    
	    StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
        hql.append(makeWhereClause(condition));
        //不在黑名单中
        hql.append(" and (yci.ifInBlockList=0 or yci.ifInBlockList is null) ");
        //非黑名单管理加入的YsqsCorpInfo表中的数据
        /* 注释后查询列表中也能查出通过黑名单添加后又删除的数据
         * hql.append(" and yci.addType=0 ");
         */        
        //有效性
        hql.append(" and yci.validity=1 ");
        hql.append(" order by yci.corpId asc");
        List list = this.findList(hql.toString(), paralist);
        return list;	    
	}
	
	/**
     * 根据查询条件查询预申请人列表
     * @param condition
     * @return
     */
	public List queryAllCorpInfoList(TdscCorpInfoCondition condition){
	    StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
        hql.append(makeWhereClause(condition));
        List list = this.findList(hql.toString(), paralist);
        return list;        
	}
	
	/**
	 * 根据条件查询黑名单
	 * @param condition
	 * @return
	 */
	public List queryInBlockList(TdscCorpInfoCondition condition){
	    
	    StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
        hql.append(makeWhereClause(condition));
        //在黑名单中
        hql.append(" and yci.ifInBlockList=1 ");
        //有效性
        hql.append(" and yci.validity=1 ");
        hql.append(" order by yci.corpId asc");
        List list = this.findList(hql.toString(), paralist);
        return list;	    
	}
	
	
	

	public  String sqlFilter_first(String hql){
        hql = hql.replaceAll("%", "/%");
        hql = hql.replaceAll("_", "/_");
        return hql;
    }

	public TdscCorpInfo getCorpByName(String name) {
		String sql = " from TdscCorpInfo a where a.bidderName = '" + name + "' "
				+ "and sysdate < a.endDate";
		List<TdscCorpInfo> l = this.findList(sql);
		if(l != null && l.size() > 0)
			return l.get(0);
		return null;
	}
}
