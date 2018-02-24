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
	 * ���ݲ�ѯ������ѯ�������б�
	 * @param condition
	 * @return
	 */
	
	public PageList queryCreditListByCondition(TdscCorpInfoCondition condition) {
		
		StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
		
		//��װ�������
		if (paralist != null) paralist.clear();
        else paralist = new ArrayList();
		
		//�ں�������
		hql.append(" and yci.ifInBlockList=1 ");
		
		//��Ч��
		hql.append(" and yci.validity=1 ");
		
		//��ҵ����Ȼ������
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
	 * ���ݲ�ѯ������ѯ���ں������е�Ԥ�������б�
	 * @param condition
	 * @return
	 */
	
	public List queryCorpInfoListByCondition(TdscCorpInfoCondition condition){
	    
	    StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
        hql.append(makeWhereClause(condition));
        //���ں�������
        hql.append(" and (yci.ifInBlockList=0 or yci.ifInBlockList is null) ");
        //�Ǻ�������������YsqsCorpInfo���е�����
        /* ע�ͺ��ѯ�б���Ҳ�ܲ��ͨ����������Ӻ���ɾ��������
         * hql.append(" and yci.addType=0 ");
         */        
        //��Ч��
        hql.append(" and yci.validity=1 ");
        hql.append(" order by yci.corpId asc");
        List list = this.findList(hql.toString(), paralist);
        return list;	    
	}
	
	/**
     * ���ݲ�ѯ������ѯԤ�������б�
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
	 * ����������ѯ������
	 * @param condition
	 * @return
	 */
	public List queryInBlockList(TdscCorpInfoCondition condition){
	    
	    StringBuffer hql = new StringBuffer("from TdscCorpInfo yci where 1=1 ");
        hql.append(makeWhereClause(condition));
        //�ں�������
        hql.append(" and yci.ifInBlockList=1 ");
        //��Ч��
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
