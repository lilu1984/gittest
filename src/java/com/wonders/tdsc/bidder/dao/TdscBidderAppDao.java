package com.wonders.tdsc.bidder.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;

import com.wonders.esframework.common.bo.hibernate.HqlParameter;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBidderAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscBidderApp.class;
	}

	/**
	 * ��ѯĳһ����������ĳһ���ؿ������ queryNoΪ��ȡ���ַ���
	 * 
	 * @param queryNo
	 * @return
	 */
	public List getProByQueryNo(String queryNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.acceptNo like '%").append(queryNo).append("%'");
		return findList(hql.toString());
	}
	public List getProByAcceptNo(String acceptNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.acceptNo = '").append(acceptNo).append("'");
		return findList(hql.toString());
	}

	public List getProByCertNo(String certNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.certNo = '").append(certNo).append("'");
		return findList(hql.toString());
	}

	public List getMaxByQueryNo(String queryNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.acceptNo like '%").append(queryNo).append("%'").append("order by a.acceptNo desc");

		return findList(hql.toString());
	}

	/**
	 * ��ѯĳһ���ؿ�����֤���� ���֤����= �ؿ鹫���+3λ��ˮ�� blockNpticeNoΪ�ؿ鹫���
	 * 
	 * @param blockNpticeNo
	 * @return
	 */
	public List getCretNo(String blockNpticeNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.certNo like '%").append(blockNpticeNo).append("%'");
		return findList(hql.toString());
	}

	/**
	 * ͨ��appId ������о�����Ϣ�б�
	 * 
	 * @param appId
	 * @return
	 */
	public List findBidderAppListByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'").append(" order by a.acceptNo ");
		return this.findList(hql.toString());
	}

	/**
	 * ͨ��appId �������ͨ������ľ�����Ϣ�б�
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("' and a.reviewResult = '1' ");
		return this.findList(hql.toString());
	}

	/**
	 * ͨ��appId����Ѿ����ع��ʸ�֤��ľ�������Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListDownloadZgzs(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("' and a.ifDownloadZgzs = '1' and a.reviewResult = '1' ");
		return this.findList(hql.toString());
	}

	/**
	 * ����������ѯ��list_jsjg.jsp ��ѯ��ť��
	 * 
	 * @param condition
	 * @return
	 */
	public List findBidderAppListByCondition(TdscBidderCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1");
		hql.append(" and a.appId = '").append(condition.getAppId()).append("' and");
		hql.append(" a.userId = '").append(condition.getUserId()).append("'");
		return this.findList(hql.toString());
	}

	/**
	 * ͨ�����׿���Ż��һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktXh(String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktXh = '").append(yktXh).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ���ʸ�֤���Ż��һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByCertNo(String certNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.certNo = '").append(certNo).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��appId�ͺ��ƺ�����һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdConNo(String appId, String conNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId like '%").append(appId).append("%'");
		hql.append(" and a.conNum = '").append(conNo).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��appId��һ��ͨ��Ż��һ��������Ϣ
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdYktXh(String appId, String yktXh) {

		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'");
		hql.append(" and (a.yktXh = '").append(yktXh).append("' or a.yktBh = '").append(yktXh).append("') ");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��appId���ʸ�֤���Ż��һ��������Ϣ
	 * 
	 * @param appId
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdCertNo(String appId, String certNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'");
		hql.append(" and a.certNo = '").append(certNo).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��bidderId��һ��ͨ��Ż��һ��������Ϣ
	 * 
	 * @param bidderId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktXh(String bidderId, String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");
		hql.append(" and a.yktXh = '").append(yktXh).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��bidderId��һ��ͨ���Ż��һ��������Ϣ
	 * 
	 * @param bidderId
	 * @param yktBh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktBh(String bidderId, String yktBh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");
		hql.append(" and a.yktBh = '").append(yktBh).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��bidderId���һ��������Ϣ
	 * 
	 * @param bidderId
	 * @return TdscBidderApp
	 */
	public List getOneBidderByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");
		List list = findList(hql.toString());
		return list;
	}

	public TdscBidderApp getBidderByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ����������--����TDSC_BIDDER_APP���е���Ϣ
	 * 
	 */
	/*
	 * public TdscBidderApp saveTdscBidderApp(TdscBidderApp tdscBidderApp){ return (TdscBidderApp)this.getHibernateTemplate().saveOrUpdateCopy(tdscBidderApp); }
	 */

	/**
	 * ͨ��APPID�ҵ�һ���ж����˲���������,���ж����˻����˺���
	 * 
	 * @param appId
	 * @return
	 */
	public List findJoinAuctionListByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("'");
		hql.append(" and a.conNum is not null");
		return this.findList(hql.toString());
	}

	public List findPageList(String appId) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("'");
		return this.findList(hql.toString());
	}

	public List findPageBidderListByUserId(String appId, String appUserId) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("'").append("and a.appUserId='").append(appUserId).append("'")
				.append("and a.ifCommit='1'");
		return this.findList(hql.toString());
	}

	public List findPageList(TdscBidderCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1");
		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
			hql.append(" and a.acceptNo like '%").append(condition.getAcceptNo().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getBidderType())) {
			hql.append(" and a.bidderType like '%").append(condition.getBidderType()).append("%' ");
		}
		hql.append(makeWhereClause(condition));
		hql.append(" order by a.acceptNo");
		return this.findList(hql.toString());
	}

	public List findPageListByIfCommit(TdscBidderCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1 and a.ifCommit is null or a.ifCommit=0");
		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
			hql.append(" and a.acceptNo like '%").append(condition.getAcceptNo().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getBidderType())) {
			hql.append(" and a.bidderType like '%").append(condition.getBidderType()).append("%' ");
		}
		hql.append(makeWhereClause(condition));
		return this.findList(hql.toString());
	}

	/**
	 * ��ѯ����һ��ͨ���
	 * 
	 * @param condition
	 * @return
	 */
	public PageList findAllYktPageList(TdscBidderCondition condition) {
		// �û���Ϣ��ѯ

		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1 and a.yktBh is not null and a.yktXh is not null and a.yktMm is not null ");
		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
			hql.append(" and a.acceptNo like '%").append(condition.getAcceptNo().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getYktXh())) {
			hql.append(" and a.yktXh like '%").append(condition.getYktXh().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getBidderType())) {
			hql.append(" and a.bidderType like '%").append(condition.getBidderType()).append("%' ");
		}

		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.acceptDate desc";
		PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
		return pageList;
	}

	/**
	 * �����û���ѯ����������װ�������
	 * 
	 * @param condition
	 *            �û���ѯ��������
	 * @return String �������
	 */

	private String makeWhereClause(TdscBidderCondition condition) {

		StringBuffer whereClause = new StringBuffer();

		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
			whereClause.append(" and a.acceptNo like '%").append(condition.getAcceptNo().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getBidderType())) {
			whereClause.append(" and a.bidderType like '%").append(condition.getBidderType()).append("%' ");
		}
		/*
		 * if (condition.getAcceptDate() != null) { //whereClause.append(" and a.acceptDate like '%").append(condition.getAcceptDate()).append("%' "); whereClause.append(" and
		 * a.acceptDate like '%").append("to_date(to_char(condition.getAcceptDate(), 'yyyy-mm-dd'), 'yyyy-mm-dd')").append("%' "); }
		 */
		if (condition.getBidderId() != null) {
			whereClause.append(" and a.bidderId = '").append(condition.getBidderId()).append("' ");
		}
		if (condition.getAppId() != null) {
			whereClause.append(" and a.appId = '").append(condition.getAppId()).append("' ");
		}
		if (condition.getNoticeId() != null) {
			whereClause.append(" and a.noticeId = '").append(condition.getNoticeId()).append("' ");
		}
		if (condition.getYktBh() != null) {
			whereClause.append(" and a.yktBh = '").append(condition.getYktBh()).append("' ");
		}
		if (condition.getYktMm() != null) {
			whereClause.append(" and a.yktMm = '").append(condition.getYktMm()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getBzjztDzqk())) {
			whereClause.append(" and a.bzjztDzqk = '").append(condition.getBzjztDzqk()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getCertNo())) {
			whereClause.append(" and a.certNo like '%").append(condition.getCertNo()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getAppUserId())) {
			whereClause.append(" and a.appUserId = '").append(condition.getAppUserId()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getUserId())) {
			whereClause.append(" and a.userId = '").append(condition.getUserId()).append("' ");
		}
		return whereClause.toString();
	}

	/**
	 * ͨ��bidderId ��øÿ����Ĺҵ�appId
	 * 
	 * @param bidderId
	 * @return
	 */
	public TdscBidderApp findOneBidderByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��appId ��øÿ�ؿ����Ĺҵ�����������Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List findBidderIdsByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * ����һ��ͨ�˺������ѯTdscBidderApp��һ����һ���ؿ�
	 * 
	 * @param cardNumber
	 * @param password
	 * @return
	 */
	public TdscBidderApp getBidderAppByCardNumberPassword(String cardNumber, String password) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktBh='").append(cardNumber).append("' and a.yktMm='").append(password).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ����һ��ͨ�˺�����(����ɲ���)��ѯTdscBidderApp��һ���Զ���ؿ�
	 * 
	 * @param cardNumber
	 * @param password
	 * @return
	 */
	public List getBidderAppListByCard(String cardNumber, String password) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktBh='").append(cardNumber).append("'");
		if (password != null && !"".equals(password) && !"null".equals(password))
			hql.append(" and a.yktMm='").append(password).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��tdscBidderApp���еó�����bidderId
	 * 
	 * @param appId
	 * @param yktBh
	 * @return
	 */
	public String findBidderId(String appId, String yktXh) {
		StringBuffer hql = new StringBuffer("select a.bidderId from TdscBidderApp a where a.appId = '").append(appId).append("'and a.yktXh ='").append(yktXh).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			String bidderId = (String) list.get(0);
			return bidderId;
		} else {
			return null;
		}
	}

	public String findyktBh(String appId, String yktBh) {
		StringBuffer hql = new StringBuffer("select a.yktBh from TdscBidderApp a where a.appId = '").append(appId).append("'and a.yktBh like '%").append(yktBh).append("%'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			String YktBh = (String) list.get(0);
			return YktBh;
		} else {
			return null;
		}
	}

	public String findBidderIdByYktBh(String appId, String yktBh) {
		StringBuffer hql = new StringBuffer("select a.bidderId from TdscBidderApp a where a.appId = '").append(appId).append("'and a.yktBh ='").append(yktBh).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			String bidderId = (String) list.get(0);
			return bidderId;
		} else {
			return null;
		}
	}

	/**
	 * �����Ƿ��ѹ����ʱ��
	 * 
	 * @return
	 */
	public String isCheckOverpass() {
		String checkDate = "true";
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.acceptDate > sysdate");
		List list = this.findList(hql.toString());
		if (list.size() == 0) {
			checkDate = "false";
		}
		return checkDate;
	}

	/**
	 * ͨ��app_Id,YKT_BH,YKT_MM����ȡBidder_Id
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBidderApp getTdscBidderApp(TdscBidderCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1 = 1");
		// ��װ�������
		hql.append(makeWhereClause(condition));

		List list = this.findList(hql.toString());

		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ͨ��app_Id,YKT_BH,YKT_MM����ȡBidder_Id
	 * 
	 * @param condition
	 * @return
	 */
	public List getTdscBidderAppList(TdscBidderCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1 = 1");
		// ��װ�������
		hql.append(makeWhereClause(condition));

		List list = this.findList(hql.toString());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ������
	 * 
	 * @param appId
	 * @return
	 */
	public List findTenderNoListByAppId(String appId) {
		StringBuffer hql = new StringBuffer("select a.tenderNo from TdscBidderApp a where a.appId='").append(appId).append("'");
		List list = findList(hql.toString());
		if (null != list && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ���������Ų�ѯ�ʸ�֤����
	 * 
	 * @param tenderNo
	 * @return
	 */
	public String findCertNoByTenderNo(String tenderNo) {
		StringBuffer hql = new StringBuffer("select a.certNo from TdscBidderApp a where a.tenderNo='").append(tenderNo).append("'");
		List list = findList(hql.toString());
		if (null != list && list.size() > 0) {
			return (String) list.get(0);
		} else {
			return null;
		}
	}

	public String getReviewOpnnByYktBh(String yktBh) {
		StringBuffer hql = new StringBuffer("select a.reviewOpnn from TdscBidderApp a where a.yktBh='").append(yktBh).append("'");
		List list = findList(hql.toString());
		if (null != list && list.size() > 0) {
			return (String) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * ͨ��appId��tenderId���һ��������Ϣ
	 * 
	 * @param appId
	 * @param tenderId
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdTenderId(String appId, String tenderNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("' and a.tenderNo='").append(tenderNo).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * У�� һ��ͨ����Ƿ��Ѿ�ʹ�ù�
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktXh(String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktXh = '").append(yktXh).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * У�� һ��ͨ�����Ƿ��Ѿ�ʹ�ù�
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktBh(String yktBh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktBh = '").append(yktBh).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * ͨ�����׿����Ż��һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktBh(String yktBh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktBh = '").append(yktBh).append("' or a.yktXh = '").append(yktBh).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) list.get(0);
			return tdscBidderApp;
		} else {
			return null;
		}
	}

	/**
	 * ����appId��conNum��ѯyktBh
	 * 
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public String checkConnumByAppId(String appId, String conNum) {
		StringBuffer hql = new StringBuffer("select a.yktBh from TdscBidderApp a where a.appId='").append(appId).append("' and a.conNum='").append(conNum).append("'");
		List list = findList(hql.toString());
		if (null != list && list.size() > 0) {
			return (String) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * ����appId��conNum��ѯcertNo
	 * 
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public String checkConnumBycertNo(String appId, String conNum) {
		StringBuffer hql = new StringBuffer("select a.certNo from TdscBidderApp a where a.appId='").append(appId).append("' and a.conNum='").append(conNum).append("'");
		List list = findList(hql.toString());
		if (null != list && list.size() > 0) {
			return (String) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * ����appId��conNum��ѯcertNo
	 * 
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public List checkCertNoByAppId(String appId, String certNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("' and a.certNo='").append(certNo).append("'");
		return findList(hql.toString());
	}

	/**
	 * ��ѯ��������Ϣ������ͨ����
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByAppId(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId in (:appIdList) and a.reviewResult = 1 ");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}
	
	public List queryBidderAppListByUserId(String userId) {
		if (StringUtils.isEmpty(userId))return null;
		String hql = "from TdscBidderApp a where 1=1 and a.userId = '" + userId + "' and a.certNo is not null and a.conNum is not null and a.ifCommit = '1'";
		List list = this.findList(hql);
		return list;
	}
	
	public List queryBidderAppListForme(String userId, String noticeId) {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(noticeId))return null;
		String hql = "from TdscBidderApp a where 1=1 and a.userId = '" + userId + "' and a.noticeId = '" + noticeId + "' and a.certNo is not null and a.conNum is not null and a.ifCommit = '1'";
		List list = this.findList(hql);
		return list;
	}

	/**
	 * ��ѯ��������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByCondition(TdscBidderCondition condition) {
		List list = new ArrayList();
		if (condition != null) {
			StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1 ");
			if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {
				hql.append(" and a.bidderId in (:bidderIdList)");
			}
			if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
				hql.append(" and a.appId in (:appIdList)");
			}
			if (StringUtils.isNotEmpty(condition.getNoticeId())) {
				hql.append(" and a.noticeId = :noticeId ");
			}
			if (StringUtils.isNotEmpty(condition.getYktBh())) {
				hql.append(" and a.yktBh = :yktBh ");
			}
			if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
				hql.append(" and a.acceptNo like :acceptNo ");
			}
			if (condition.getCertNoList()!=null && condition.getCertNoList().size() > 0) {
				hql.append(" and a.certNo in (:certNoList)");
			}
			if (StringUtils.isNotEmpty(condition.getBidderType())) {
				hql.append(" and a.bidderType = :bidderType ");
			}
			if (StringUtils.isNotEmpty(condition.getAppUserId())) {
				hql.append(" and a.appUserId = :appUserId ");
			}
			if (StringUtils.isNotEmpty(condition.getUserId())) {
				hql.append(" and a.userId = :userId ");
			}
			if (StringUtils.isNotEmpty(condition.getNotYktBh())) {
				hql.append(" and a.yktBh is null ");
			}
			if (StringUtils.isNotEmpty(condition.getIfCommit())) {
				if ("0".equals(condition.getIfCommit()))
					hql.append(" and (a.ifCommit is null or a.ifCommit=0) ");
				if ("1".equals(condition.getIfCommit()))
					hql.append(" and a.ifCommit=1 ");
			}

			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {
					query.setParameterList("bidderIdList", condition.getBidderIdList());
				}
				if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
					query.setParameterList("appIdList", condition.getAppIdList());
				}
				if (condition.getCertNoList()!=null && condition.getCertNoList().size() > 0) {
					query.setParameterList("certNoList", condition.getCertNoList());
				}
				if (StringUtils.isNotEmpty(condition.getNoticeId())) {
					query.setParameter("noticeId", condition.getNoticeId());
				}
				if (StringUtils.isNotEmpty(condition.getYktBh())) {
					query.setParameter("yktBh", condition.getYktBh());
				}
				if (StringUtils.isNotEmpty(condition.getAppUserId())) {
					query.setParameter("appUserId", condition.getAppUserId());
				}
				if (StringUtils.isNotEmpty(condition.getUserId())) {
					query.setParameter("userId", condition.getUserId());
				}
				if (StringUtils.isNotEmpty(condition.getBidderType())) {
					query.setParameter("bidderType", condition.getBidderType());
				}
				if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
					query.setParameter("acceptNo", "%" + (StringUtil.GBKtoISO88591(condition.getAcceptNo())) + "%");
				}
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ���о�������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByAppIdList(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("select distinct a.yktBh, a.appId, a.bidderId, a.acceptNo from TdscBidderApp a where a.appId in (:appIdList) order by a.acceptNo ");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ���о�������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByNoticeId(String noticeId) {
		List list = new ArrayList();
		List returnList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId)) {
			StringBuffer hql = new StringBuffer("select distinct a.yktBh, a.acceptNo from TdscBidderApp a where a.noticeId = '" + noticeId + "' order by a.acceptNo asc");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				list = (query.list());
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Object[] str = (Object[]) list.get(i);
						if (str[0] != null) {
							String yktBh = String.valueOf(str[0]);
							if (yktBh != null && !"".equals(yktBh))
								returnList.add(yktBh);
						}
					}
				}

			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return returnList;
		} else {
			return null;
		}
	}

	/**
	 * ����appIdListȡ�øù��������в�ͬ���׿���Ӧ�ľ�������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListWithDiffYkt(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer(
					"select distinct a.yktBh, a.yktXh, a.conNum, a.appId, a.bidderId, a.acceptNo from TdscBidderApp a where a.appId in (:appIdList) and a.reviewResult = 1 order by a.acceptNo ");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ȡ�øú��ƺ��ڹ��������ж�Ӧ�ľ�������Ϣ������ͨ����
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByCertNo(List appIdList, String certNo) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId in (:appIdList) and a.conNum = '" + certNo + "' and a.reviewResult = 1 order by a.appId desc");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ȡ�ý��׿�����ڹ��������ж�Ӧ�ľ�������Ϣ�б�����ͨ����
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListByYktBh(List appIdList, String yktBh) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId in (:appIdList) and a.yktBh = '" + yktBh + "' and a.reviewResult = 1 order by a.appId desc");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ȡ�øú��ƺ��ڹ��������ж�Ӧ�ľ�������Ϣ������ͨ����
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryBidderAppListWithNoCertNo(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId in (:appIdList) and a.conNum is null and a.reviewResult = 1 order by a.appId desc");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ��������Ϣ������ͨ����
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryCertNolistByAppId(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("select a.conNum from TdscBidderApp a where a.appId in (:appIdList) and a.reviewResult = 1 group by a.conNum order by a.conNum ");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * ��ѯ�ʸ�������б�
	 * 
	 * @param condition
	 * @return
	 */
	public PageList queryAppViewList(TdscBaseQueryCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscBlockAppView a where a.blockNoticeNo is not null and a.appId is not null ");
		if (StringUtils.isNotEmpty(condition.getBlockName())) {
			hql.append(" and a.blockName like '%").append(StringUtil.GBKtoISO88591(condition.getBlockName())).append("%'");
		}
		if (StringUtils.isNotEmpty(condition.getTransferMode())) {
			hql.append(" and a.transferMode = '").append(condition.getTransferMode()).append("'");
		}
		if (StringUtils.isNotEmpty(condition.getTradeNum())) {
			hql.append(" and a.tradeNum like '%").append(StringUtil.GBKtoISO88591(condition.getTradeNum())).append("%'");
		}
		if (StringUtils.isNotEmpty(condition.getUserId())) {
			hql.append(" and a.userId = '").append(condition.getUserId()).append("'");
		}
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.blockNoticeNo desc";
		PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
		return pageList;
	}

	/**
	 * ����appIdListȡ�����в��ظ��Ĺ���id�б�
	 * 
	 * @param appIdList
	 * @return
	 */
	public List findNoticeIdListByAppIdList(List appIdList) {
		List list = new ArrayList();
		if (appIdList != null && appIdList.size() > 0) {
			StringBuffer hql = new StringBuffer("select distinct a.noticeId from TdscBlockAppView a where a.appId in (:appIdList) order by a.noticeId desc ");
			try {
				Query query = this.getSession().createQuery(hql.toString());
				query.setParameterList("appIdList", appIdList);
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	private List	paralist	= null;

	private String makeWhereClause_advnce(TdscBidderCondition condition) {
		StringBuffer hql = new StringBuffer();
		if (paralist != null)
			paralist.clear();
		else
			paralist = new ArrayList();

		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {// ������
			hql.append(" and ba.acceptNo like :acceptNo escape '/'");
			paralist.add(new HqlParameter("acceptNo", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getAcceptNo().trim())) + "%"));
		}
		if (StringUtils.isNotEmpty(condition.getYktBh())) {// ���׿����
			hql.append(" and ba.yktBh like :yktBh  escape '/'");
			paralist.add(new HqlParameter("yktBh", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktBh().trim())) + "%"));
		}
		if (StringUtils.isNotEmpty(condition.getYktXh())) {// ���׿�����
			hql.append(" and ba.yktXh like :yktXh  escape '/'");
			paralist.add(new HqlParameter("yktXh", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktXh().trim())) + "%"));
		}
		if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {// ������ϢID
			hql.append(" and ba.bidderId in (:bidderIdList) ");
			paralist.add(new HqlParameter("bidderIdList", condition.getBidderIdList()));
		}
		return hql.toString();
	}

	public PageList findListByCondition(TdscBidderCondition condition) {
		StringBuffer qryHql = new StringBuffer("from TdscBidderApp ba where  1=1 ");
		qryHql.append(this.makeWhereClause_advnce(condition));
		String countHql = "select count(*) " + qryHql.toString();
		String queryHql = qryHql.toString() + " order by ba.acceptDate";
		PageList pageList = this.findPageListWithHqlAdvance(countHql.toString(), queryHql, paralist, 10, condition.getCurrentPage());
		return pageList;
	}

	/**
	 * ��ѯ��������Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List queryYktBhList(TdscBidderCondition condition) {
		List list = new ArrayList();
		if (condition != null) {
			StringBuffer hql = new StringBuffer("select distinct a.yktBh from TdscBidderApp a where 1=1 ");
			if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {
				hql.append(" and a.bidderId in (:bidderIdList)");
			}
			if (StringUtils.isNotEmpty(condition.getYktBh())) {
				// hql.append(" and a.yktBh = :yktBh ");
				// hql.append(" and a.yktBh like :yktBh  escape '/'");
				hql.append(" and a.yktBh like '%").append(sqlFilter_first(condition.getYktBh())).append("%'  escape '/'");
			}
			if (StringUtils.isNotEmpty(condition.getYktXh())) {
				// hql.append(" and a.yktBh = :yktBh ");
				// hql.append(" and a.yktXh like :yktXh  escape '/'");
				hql.append(" and a.yktXh like '%").append(sqlFilter_first(condition.getYktXh())).append("%'  escape '/'");
			}
			if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
				// hql.append(" and a.acceptNo like :acceptNo ");
				// hql.append(" and a.acceptNo like :acceptNo ");
				hql.append(" and a.acceptNo like '%").append(sqlFilter_first(condition.getAcceptNo())).append("%'  escape '/'");
			}
			if (StringUtils.isNotEmpty(condition.getCertNo())) {
				hql.append(" and a.certNo like '%").append(sqlFilter_first(condition.getCertNo())).append("%'  escape '/'");
			}
			if (StringUtils.isNotEmpty(condition.getUserId())) {
				hql.append(" and a.userId = '").append(condition.getUserId()).append("'");
			}
			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {
					query.setParameterList("bidderIdList", condition.getBidderIdList());
				}
				// if (StringUtils.isNotEmpty(condition.getYktBh())) {
				// query.setParameter("yktBh", StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktBh())));
				// }
				// if (StringUtils.isNotEmpty(condition.getYktXh())) {
				// query.setParameter("yktXh", StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktXh())));
				// }
				// if (StringUtils.isNotEmpty(condition.getAcceptNo())) {
				// query.setParameter("acceptNo", StringUtil.GBKtoISO88591(condition.getAcceptNo()));
				// }
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	public String sqlFilter_first(String hql) {
		hql = hql.replaceAll("%", "/%");
		hql = hql.replaceAll("_", "/_");
		return hql;
	}

	public TdscBidderApp findYixiangBidderAppByAppId(String appId) {
		if (StringUtils.isEmpty(appId))
			return null;
		String sql = "from TdscBidderApp a where a.appId = '" + appId + "' and a.isPurposePerson = '1'";
		List list = this.findList(sql);
		if (list != null && list.size() > 0)
			return (TdscBidderApp) list.get(0);
		return null;
	}

	public TdscBidderApp findYixiangPersonLikeAppId(String appId) {
		if (StringUtils.isEmpty(appId))
			return null;
		String sql = "from TdscBidderApp a where a.appId like '%" + appId + "%' and a.isPurposePerson = '1'";
		List list = this.findList(sql);
		if (list != null && list.size() > 0)
			return (TdscBidderApp) list.get(0);
		return null;
	}

	public List findBidderAppByAppId(String noticeId) {
		if (StringUtils.isEmpty(noticeId))
			return null;
		String sql = "from TdscBidderApp a where a.noticeId = '" + noticeId + "'";
		List list = this.findList(sql);
		return list;
	}

	public List findBidderYixiangList(String noticeId) {
		String sql = "select a.bidderId from TdscBidderApp a where a.isPurposePerson = '1' and a.noticeId='" + noticeId + "'";
		List list = this.findList(sql);
		return list;
	}

	public List findSql(String noticeId) {
		String sql = "select p.app_id, p.ykt_bh, p.bzj_dzse,p.is_convert, t.result_price, t.result_cert, p.bidder_name from (select m.accept_no, m.ykt_bh, n.bzj_dzse, m.app_id, m.is_convert, n.bidder_name from tdsc_bidder_app m, tdsc_bidder_person_app n where n.bidder_id = m.bidder_id and m.notice_id = '"
				+ noticeId
				+ "') p left outer join (select t.app_id, t.result_price, t.result_cert from tdsc_block_tran_app t where t.notice_id = '"
				+ noticeId
				+ "') t on t.app_id  = p.app_id and t.result_cert = p.accept_no";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}

	public List queryNotDuplicateBidderAppListByNoticeId(String noticeId) {
		if (StringUtils.isEmpty(noticeId))
			return null;
		String sql = "from TdscBidderApp a where a.ifDownloadZgzs='1' and a.noticeId = '" + noticeId + "'";
		List list = this.findList(sql);
		return list;
	}

	public List queryNotYixiangBidderAppListByNoticeId(String noticeId) {
		if (StringUtils.isEmpty(noticeId))
			return null;
		String sql = "from TdscBidderApp a where a.isPurposePerson='0' and a.noticeId = '" + noticeId + "'";
		List list = this.findList(sql);
		return list;
	}

	public TdscBidderApp queryIsYixiangBidderAppList(String noticeId, String appId) {
		if (StringUtils.isEmpty(noticeId))
			return null;
		String sql = "from TdscBidderApp a where a.isPurposePerson='1' and ( a.noticeId = '" + noticeId + "' or a.noticeId is null) and a.appId like '%" + appId + "%'";

		List list = this.findList(sql);
		if (list != null && list.size() > 0)
			return (TdscBidderApp) list.get(0);
		return null;
	}

	public List queryBidderAppListByCertNo(String certNo) {
		if (StringUtils.isEmpty(certNo))
			return null;
		String sql = "from TdscBidderApp a where a.certNo = '" + certNo + "'";
		List list = this.findList(sql);
		return list;
	}

	public List queryBidderAppListLikeAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId like '%").append(appId).append("%'");
		return this.findList(hql.toString());
	}
	
	public List queryMyHaoPaiByNoticeId(String noticeId, String userId) {
		if (StringUtils.isEmpty(noticeId) || StringUtils.isEmpty(userId)) return null;
		String hql = "select a.conNum from TdscBidderApp a where a.noticeId = '" + noticeId + "' and a.userId = '" + userId +"'";
		return this.findList(hql);
	}

	/**
	 * ʹ�ý��׿�оƬ���룬�õ�bidderApp��Ψһһ��bidder
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByYktBh(String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1");
		hql.append(" and a.yktBh='").append(yktXh).append("'");
		List list = this.findList(hql.toString());
		return (TdscBidderApp) list.get(0);
	}

	public TdscBidderApp queryBidderAppListLikeAppIdAndCardNo(String appId, String cardNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId like '%").append(appId).append("%'");
		hql.append(" and a.conNum='").append(cardNo).append("'");
		List list = this.findList(hql.toString());
		return (TdscBidderApp) list.get(0);
	}

	public String getPasswordByCardNoAndYktXh(String cardNo, String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1=1 ");
		hql.append(" and a.yktBh='").append(cardNo).append("'");
		hql.append(" and a.yktXh='").append(yktXh).append("'");
		List list = this.findList(hql.toString());
		String retPwd = "";
		if (list != null && list.size() > 0) {
			TdscBidderApp app = (TdscBidderApp) list.get(0);
			retPwd = app.getYktMm();
		}

		return retPwd;
	}

	public List queryBidderAppListLikeAppIdAndUserId(String appId, String userId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId like '%").append(appId).append("%'");
		hql.append(" and a.userId='").append(userId).append("'");
		return this.findList(hql.toString());
	}

	
	/**
	 *  ���ݾ�������Ϣ�õ��������Ƿ�Ϊ��������
	 * 
	 * @param yktXh
	 * @return
	 */
	public List findJmrByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * �������˺Ż�ȡ������Ϣ
	 * @param inmemo
	 * @return
	 */
	public TdscBidderApp getBidderAppByInMemo(String inmemo){
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bankNumber = '").append(inmemo).append("'");
		List list = this.findList(hql.toString());
		if(list!=null&&list.size()>0){
			return (TdscBidderApp)list.get(0);
		}else{
			return null;
		}
	}
	
	public TdscBidderApp getPurposeBidderAppByAppId(String appId) {
		if (StringUtils.isEmpty(appId))
			return null;
		String sql = "from TdscBidderApp a where a.isPurposePerson='1' and a.appId = '" + appId + "'";
		List list = this.findList(sql);
		if (list != null && list.size() > 0)
			return (TdscBidderApp) list.get(0);
		return null;
	}

	/**
	 * 
	 * @param userId
	 * @param noticeId
	 * @return
	 */
	public List findCertNoList(String userId, String noticeId) {
		StringBuffer sql = new StringBuffer("select distinct(a.cert_no) from tdsc_bidder_app a where 1=1 ");
		if (StringUtils.isNotEmpty(userId)) {
			sql.append(" and a.user_Id = '" + userId + "'");
		}
		if (StringUtils.isNotEmpty(noticeId)) {
			sql.append(" and a.notice_Id = '" + noticeId + "'");
		}
		sql.append(" and a.cert_no is not null and a.con_num is not null and a.if_commit = '1'");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		return query.list();
	}

	public List findValidBidderList(String appId) {
		Criteria criteria = getSession().createCriteria(TdscBidderApp.class);
		criteria.add(Expression.eq("appId", appId));
		criteria.add(Expression.eq("ifCommit", GlobalConstants.DIC_ID_REVIEW_RESULT_YES));
		List list = criteria.list();
		return list;
	}
	/**
	 * 
	 * ���ݹ���ID���û�id���Ҿ�����.
	 * @param noticeId
	 * @param userId
	 * @return
	 */
	public List findSameNoticeBidderListByUserId(String noticeId,String userId){
		Criteria criteria = getSession().createCriteria(TdscBidderApp.class);
		criteria.add(Expression.eq("noticeId", noticeId));
		criteria.add(Expression.eq("userId", userId));
		List list = criteria.list();
		return list;
		
	}
	/**
	 * ��ȡȡ�þ����ʸ�ľ������б�
	 * @param appId
	 * @return
	 */
	public List findHaveCertNoBidderList(String appId){
		Criteria criteria = getSession().createCriteria(TdscBidderView.class);
		criteria.add(Expression.eq("appId", appId));
		criteria.add(Expression.isNotNull("certNo"));
		List list = criteria.list();
		return list;
	}
	
}
