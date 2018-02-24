package com.wonders.tdsc.localtrade.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.condition.TdscListingAppCondition;


public class TdscListingAppDao extends BaseHibernateDaoImpl{

	public Class getEntityClass() {
        return TdscListingApp.class;
    }
	
	   /**
     * �����������һҳ���û��б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList����
     */
	public PageList findPageList(TdscListingAppCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscListingApp a where 1 = 1");		
		// ��װ�������
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.listingAppId";
        PageList pageList = findPageListWithHql(countHql,queryHql, condition.getPageSize(),condition.getCurrentPage());   	
		return pageList;
	}
	
	public BigDecimal findTopPrice(String listingId){
		StringBuffer hql = new StringBuffer("select a.Max(listPrice) from TdscListingApp a where listingId = '").append(listingId).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (BigDecimal)list.get(0);
		}else{
			return null;
		}
	}
	
	public BigDecimal findTopPrice(String appId, List certNoList, String priceType){
		StringBuffer hql = new StringBuffer("select Max(a.listPrice) from TdscListingApp a where a.appId = '").append(appId).append("'");
		if (certNoList != null && certNoList.size() > 0) {
			hql.append(" and a.listCert in(:listCert)");
		}
		if (StringUtils.isNotEmpty(priceType)) {
			hql.append(" and a.priceType = '").append(priceType).append("'");
		}
		Query query = this.getSession().createQuery(hql.toString());
		if (certNoList != null && certNoList.size() > 0) {
			query.setParameterList("listCert", certNoList);
		}
		List list = (query.list());
		if(null != list && list.size()>0){
			return (BigDecimal)list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * �ӹ�����Ϣ���в��֤���
	 * @param listingId
	 * @return
	 */
	public String findListCertByListingId(String listingId){
		StringBuffer hql = new StringBuffer("select a.listCert from TdscListingApp a where a.listingId='").append(listingId).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (String)list.get(list.size()-1);
		}else{
			return null;
		}
	}
	
	public String findListingAppId(String listingId,String listCert){
		StringBuffer hql = new StringBuffer("select a.listingAppId from TdscListingApp a where a.listingId = '").append(listingId).append("'and a.listCert ='").append(listCert).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (String)list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * ȡ�����еĹ�����Ϣ
	 * @param listingId
	 * @return
	 */
	public List findTdscListingAppListByListingId(String listingId){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listingId='").append(listingId).append("' order by a.listDate desc");
		return this.findList(hql.toString());
	}

	/**
	 * �ҳ��ֳ����۲��ֵĹ����б�
	 * @param listingId
	 * @return
	 */
	public List findTdscListingAppLocalSenseList(String listingId){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listNo is not null and a.listingId='").append(listingId).append("'");
		return this.findList(hql.toString());
	}
	
	/**
	 * �ҳ�����¼�� �����б�����б�
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppInGCLR(String listingId){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listNo is not null and a.listingId='").append(listingId).append("'");
		return this.findList(hql.toString());
	}
	
	/**
	 * ��ѯĳһ�����˵Ĺ��Ƽ�¼
	 * @param listingId
	 * @param listCert
	 * @return
	 */
	public List findListingAppInfo(String listingId,String listCert){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listingId ='").append(listingId).append("' and a.listCert ='").append(listCert).append("' order by a.listDate desc");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	public List queryListingAppByYktXh(String yktXh){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.yktXh ='").append(yktXh).append("'").append("order by a.listDate desc");;
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	public List queryByListCert(String listCert, String appId){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listCert ='").append(listCert).append("'");
		hql.append(" and a.appId = '").append(appId).append("'");
		hql.append(" order by a.listDate desc");
		List list = findList(hql.toString());
		return list;
	}

	public List queryTdscListingAppListByYKTXHAndAppId(String yktXh, String appId) {
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.yktXh ='").append(yktXh).append("'");
		hql.append(" and a.appId = '").append(appId).append("'");
		hql.append(" order by a.listDate desc");
		List list = findList(hql.toString());
		return list;
	}
	
	public List getListingAppListByAppId( String appId,String certNo, String priceType){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where 1=1");
		if (StringUtils.isNotEmpty(certNo)) {
			hql.append(" a.listCert ='").append(certNo).append("'");
		}
		if (StringUtils.isNotEmpty(appId)) {
			hql.append(" and a.appId = '").append(appId).append("'");
		}
		if (StringUtils.isNotEmpty(priceType)) {
			hql.append(" and a.priceType = '").append(priceType).append("'");
		}
		hql.append(" order by a.listDate desc");
		List list = findList(hql.toString());
		return list;
	}
	
	public List queryListingAppListByAppId( String appId){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where 1=1");
		if (StringUtils.isNotEmpty(appId)) {
			hql.append(" and a.appId = '").append(appId).append("'");
		}
		hql.append(" order by a.listDate desc");
		List list = findList(hql.toString());
		return list;
	}

	public List findTdscListingAppListByListingId(String listingId, String priceType) {
		StringBuffer hql = new StringBuffer("from TdscListingApp a where a.listingId='").append(listingId).append("' and a.priceType='").append(priceType).append("' order by a.listDate desc");
		return this.findList(hql.toString());
	}

	public TdscListingApp getListingAppOfPurposePerson(String appId, String certNo) {
		StringBuffer hql = new StringBuffer("from TdscListingApp a where 1=1 and a.listingSer ='1' and a.appId ='").append(appId).append("' and a.listCert = '").append(certNo).append("'");
		
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (TdscListingApp)list.get(0);
		}else{
			return null;
		}
	}
	public List findListingAppListByConNum(String appId,String conNum, String priceType){
		StringBuffer hql = new StringBuffer("from TdscListingApp a where 1=1");
		if (StringUtils.isNotEmpty(conNum)) {
			hql.append(" and a.listNo ='").append(conNum).append("'");
		}
		if (StringUtils.isNotEmpty(appId)) {
			hql.append(" and a.appId = '").append(appId).append("'");
		}
		if (StringUtils.isNotEmpty(priceType)) {
			hql.append(" and a.priceType = '").append(priceType).append("'");
		}
		hql.append(" order by a.listDate desc");
		List list = findList(hql.toString());
		return list;
	}
}

