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
	 * 查询某一个窗口受理某一个地块的数量 queryNo为截取的字符串
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
	 * 查询某一个地块的审查证书编号 审查证书编号= 地块公告号+3位流水号 blockNpticeNo为地块公告号
	 * 
	 * @param blockNpticeNo
	 * @return
	 */
	public List getCretNo(String blockNpticeNo) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.certNo like '%").append(blockNpticeNo).append("%'");
		return findList(hql.toString());
	}

	/**
	 * 通过appId 获得所有竞买信息列表
	 * 
	 * @param appId
	 * @return
	 */
	public List findBidderAppListByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'").append(" order by a.acceptNo ");
		return this.findList(hql.toString());
	}

	/**
	 * 通过appId 获得所有通过机审的竞买信息列表
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("' and a.reviewResult = '1' ");
		return this.findList(hql.toString());
	}

	/**
	 * 通过appId获得已经下载过资格证书的竞买人信息
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListDownloadZgzs(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("' and a.ifDownloadZgzs = '1' and a.reviewResult = '1' ");
		return this.findList(hql.toString());
	}

	/**
	 * 根据条件查询（list_jsjg.jsp 查询按钮）
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
	 * 通过交易卡编号获得一个竞买信息
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
	 * 通过资格证书编号获得一个竞买信息
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
	 * 通过appId和号牌号码获得一个竞买信息
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
	 * 通过appId和一卡通序号获得一个竞买信息
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
	 * 通过appId和资格证书编号获得一个竞买信息
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
	 * 通过bidderId和一卡通序号获得一个竞买信息
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
	 * 通过bidderId和一卡通卡号获得一个竞买信息
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
	 * 通过bidderId获得一个竞买信息
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
	 * 新增竞买人--保存TDSC_BIDDER_APP表中的信息
	 * 
	 */
	/*
	 * public TdscBidderApp saveTdscBidderApp(TdscBidderApp tdscBidderApp){ return (TdscBidderApp)this.getHibernateTemplate().saveOrUpdateCopy(tdscBidderApp); }
	 */

	/**
	 * 通过APPID找到一共有多少人参与了拍卖,即有多少人换领了号牌
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
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("'");
		return this.findList(hql.toString());
	}

	public List findPageBidderListByUserId(String appId, String appUserId) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId='").append(appId).append("'").append("and a.appUserId='").append(appUserId).append("'")
				.append("and a.ifCommit='1'");
		return this.findList(hql.toString());
	}

	public List findPageList(TdscBidderCondition condition) {
		// 用户信息查询
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
		// 用户信息查询
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
	 * 查询所有一卡通编号
	 * 
	 * @param condition
	 * @return
	 */
	public PageList findAllYktPageList(TdscBidderCondition condition) {
		// 用户信息查询

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
	 * 根据用户查询条件对象组装条件语句
	 * 
	 * @param condition
	 *            用户查询条件对象
	 * @return String 条件语句
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
	 * 通过bidderId 获得该块招拍挂的appId
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
	 * 通过appId 获得该块地块招拍挂的所有申请信息
	 * 
	 * @param appId
	 * @return
	 */
	public List findBidderIdsByAppId(String appId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.appId = '").append(appId).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 根据一卡通账号密码查询TdscBidderApp，一卡对一个地块
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
	 * 根据一卡通账号密码(密码可不填)查询TdscBidderApp，一卡对多个地块
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
	 * 从tdscBidderApp表中得出主键bidderId
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
	 * 检验是否已过审查时间
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
	 * 通过app_Id,YKT_BH,YKT_MM来获取Bidder_Id
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBidderApp getTdscBidderApp(TdscBidderCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1 = 1");
		// 组装条件语句
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
	 * 通过app_Id,YKT_BH,YKT_MM来获取Bidder_Id
	 * 
	 * @param condition
	 * @return
	 */
	public List getTdscBidderAppList(TdscBidderCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where 1 = 1");
		// 组装条件语句
		hql.append(makeWhereClause(condition));

		List list = this.findList(hql.toString());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 查询评标编号
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
	 * 根据评标编号查询资格证书编号
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
	 * 通过appId和tenderId获得一个竞买信息
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
	 * 校验 一卡通编号是否已经使用过
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktXh(String yktXh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktXh = '").append(yktXh).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 校验 一卡通卡号是否已经使用过
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktBh(String yktBh) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.yktBh = '").append(yktBh).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 通过交易卡卡号获得一个竞买信息
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
	 * 根据appId和conNum查询yktBh
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
	 * 根据appId和conNum查询certNo
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
	 * 根据appId和conNum查询certNo
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
	 * 查询竞买人信息（机审通过）
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
	 * 查询竞买人信息
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
	 * 查询所有竞买人信息
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
	 * 查询所有竞买人信息
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
	 * 根据appIdList取得该公告中所有不同交易卡对应的竞买人信息
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
	 * 取得该号牌号在公告中所有对应的竞买人信息（机审通过）
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
	 * 取得交易卡编号在公告中所有对应的竞买人信息列表（机审通过）
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
	 * 取得该号牌号在公告中所有对应的竞买人信息（机审通过）
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
	 * 查询竞买人信息（机审通过）
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
	 * 查询资格审查结果列表
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
	 * 根据appIdList取得所有不重复的公告id列表
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

		if (StringUtils.isNotEmpty(condition.getAcceptNo())) {// 受理编号
			hql.append(" and ba.acceptNo like :acceptNo escape '/'");
			paralist.add(new HqlParameter("acceptNo", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getAcceptNo().trim())) + "%"));
		}
		if (StringUtils.isNotEmpty(condition.getYktBh())) {// 交易卡编号
			hql.append(" and ba.yktBh like :yktBh  escape '/'");
			paralist.add(new HqlParameter("yktBh", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktBh().trim())) + "%"));
		}
		if (StringUtils.isNotEmpty(condition.getYktXh())) {// 交易卡卡号
			hql.append(" and ba.yktXh like :yktXh  escape '/'");
			paralist.add(new HqlParameter("yktXh", "%" + StringUtil.GBKtoISO88591(sqlFilter_first(condition.getYktXh().trim())) + "%"));
		}
		if (condition.getBidderIdList() != null && condition.getBidderIdList().size() > 0) {// 竞买信息ID
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
	 * 查询竞买人信息
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
	 * 使用交易卡芯片号码，得到bidderApp，唯一一个bidder
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
	 *  根据竞买人信息得到竞买人是否为意向竞买人
	 * 
	 * @param yktXh
	 * @return
	 */
	public List findJmrByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBidderApp a where a.bidderId = '").append(bidderId).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 根据子账号获取竞买信息
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
	 * 根据公告ID和用户id查找竞买人.
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
	 * 获取取得竞买资格的竞买人列表
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
