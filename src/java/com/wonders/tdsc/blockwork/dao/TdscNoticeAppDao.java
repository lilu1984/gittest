package com.wonders.tdsc.blockwork.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscNoticeAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscNoticeApp.class;
	}

	public PageList findPageList(TdscNoticeAppCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1");
		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.noticeId desc";
		PageList pageList = findPageListWithHql(countHql, queryHql, condition
				.getPageSize(), condition.getCurrentPage());

		return pageList;
	}

	private String makeWhereClause(TdscNoticeAppCondition condition) {

		StringBuffer whereClause = new StringBuffer();

		if (StringUtils.isNotEmpty(condition.getNoticeNo())) {
			whereClause.append(" and a.noticeNo like '%").append(
					condition.getNoticeNo()).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getNoticeName())) {
			whereClause.append(" and a.noticeName like '%").append(
					condition.getNoticeName()).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getNoticeStatus())) {
			whereClause.append(" and a.noticeStatus != '").append(
					condition.getNoticeStatus()).append("' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getIfReleased())) {
			whereClause.append(" and a.ifReleased = '").append(
					condition.getIfReleased()).append("' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getTradeNum())) {
			whereClause.append(" and a.tradeNum like '%").append(
					condition.getTradeNum()).append("%' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getUniteBlockName())) {
			whereClause.append(" and a.uniteBlockName like '%").append(
					condition.getUniteBlockName()).append("%' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getLandLocation())) {
			whereClause.append(" and a.landLocation like '%").append(
					condition.getLandLocation()).append("%' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getTransferMode())) {
			whereClause.append(" and a.transferMode = '").append(
					condition.getTransferMode()).append("' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getUserId())) {
			whereClause.append(" and a.userId = '").append(
					condition.getUserId()).append("' ");
		}
		
		if(condition.getNoticeStatusList() != null && condition.getNoticeStatusList().size() > 0){
			String statusList = "";
			for(int i = 0; i < condition.getNoticeStatusList().size(); i++){
				statusList = statusList + "'" + (String)condition.getNoticeStatusList().get(i) + "'" + ",";
			}
			statusList = statusList.substring(0, statusList.length()-1);
			whereClause.append(" and a.noticeStatus in (").append(statusList).append(") ");
		}
		
		return whereClause.toString();
	}

	public String upLoadFile(FormFile upLoadFile, String fileName) {
		// 传入文件名
		String fromFileName = upLoadFile.getFileName();
		String outFileName = fileName + ".doc";
		String filePath = (String) DicPropertyUtil.getInstance()
				.getPropertyValue(GlobalConstants.UPLOAD_BLOCK_NOTICE);

		// 查看目录是否存在
		File directory = new File(filePath);
		if (!directory.exists())
			directory.mkdirs();

		try {
			File outFile = new File(
			// 存放文件的服务器路径
					filePath + File.separator + outFileName);
			if (outFile.exists()) {
				outFile.delete();
			}
			outFile.createNewFile();

			byte[] fileByte = upLoadFile.getFileData();
			if (fileByte.length > 0) {
				// 在服务器的相应目录下复制上传的文件内容
				OutputStream os = new FileOutputStream(outFile);
				os.write(fileByte);
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			return null;
		}
		return (outFileName);
	}

	public void delFile(String fileName) {
		String filePath = GlobalConstants.UPLOAD_BLOCK_NOTICE;
		// 存放文件的服务器路径
		File outFile = new File(filePath + File.separator + fileName);
		if (outFile.exists()) {
			outFile.delete();
		}
	}

	public List findTdscBlockAppViewList(TdscBaseQueryCondition condition,
			String noticeNo) {

		// 用户信息查询
		StringBuffer hql = new StringBuffer(
				"from TdscBlockAppView a where 1 = 1");
		// 组装条件语句
		hql.append(makeBlockAppViewWhereClause(condition, noticeNo));

		String queryHql = hql.toString() + " order by a.blockNoticeNo asc";

		List list = new ArrayList();
		try {
			Query query = this.getSession().createQuery(queryHql.toString());
			if (condition.getIssueStartDate() != null) {

				query.setParameter("issueStartDate", condition
						.getIssueStartDate());
			}

			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		// return this.findList(queryHql);
		return list;
	}

	private String makeBlockAppViewWhereClause(
			TdscBaseQueryCondition condition, String noticeNo) {

		StringBuffer whereClause = new StringBuffer();

		if (StringUtils.isNotEmpty(condition.getNodeId())) {
			whereClause.append(" and a.nodeId = '").append(
					condition.getNodeId()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getBlockName())) {
			whereClause.append(" and a.blockName like '%").append(
					condition.getBlockName()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getTransferMode())) {
			whereClause.append(" and a.transferMode = '").append(
					condition.getTransferMode()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getStatusId())) {
			whereClause.append(" and a.statusId = '").append(
					condition.getStatusId()).append("' ");
		}

		if (condition.getIssueStartDate() != null) {
			// whereClause.append(" and a.issueStartDate =
			// ").append(condition.getIssueStartDate()).append(" ");
			whereClause.append(" and a.issueStartDate =:issueStartDate ");
		}

		if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
			whereClause.append(" and a.noitceNo is  null");
		}

		if (StringUtils.isNotEmpty(noticeNo)) {
			whereClause.append(" or a.noitceNo like '%").append(noticeNo)
					.append("%' ");
		}

		return whereClause.toString();
	}

	public List findBlockAppViewList(TdscBaseQueryCondition condition,
			String noticeNo, String issueStartDate) {

		// 用户信息查询
		StringBuffer hql = new StringBuffer(
				"from TdscBlockAppView a where 1 = 1");
		// 组装条件语句
		hql.append(makeAppViewWhereClause(condition, noticeNo, issueStartDate));

		String queryHql = hql.toString() + " order by a.blockNoticeNo asc";

		List list = this.findList(queryHql);

		// return this.findList(queryHql);
		return list;
	}

	private String makeAppViewWhereClause(TdscBaseQueryCondition condition,
			String noticeNo, String issueStartDate) {

		StringBuffer whereClause = new StringBuffer();

		if (StringUtils.isNotEmpty(condition.getNodeId())) {
			whereClause.append(" and a.nodeId = '").append(
					condition.getNodeId()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getBlockName())) {
			whereClause.append(" and a.blockName like '%").append(
					condition.getBlockName()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getTransferMode())) {
			whereClause.append(" and a.transferMode = '").append(
					condition.getTransferMode()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getBlockType())) {
			whereClause.append(" and a.blockType = '").append(
					condition.getBlockType()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getStatusId())) {
			whereClause.append(" and a.statusId = '").append(
					condition.getStatusId()).append("' ");
		}

		if (issueStartDate != null) {
			// whereClause.append(" and a.issueStartDate =
			// ").append(condition.getIssueStartDate()).append(" ");
			whereClause.append(" and a.issueStartDate = TO_DATE('").append(
					issueStartDate).append("','yyyy-mm-dd') ");
		}

		if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
			whereClause.append(" and a.noitceNo is  null");
		}

		if (StringUtils.isNotEmpty(noticeNo)) {
			whereClause.append(" or a.noitceNo like '%").append(noticeNo)
					.append("%' ");
		}

		return whereClause.toString();
	}

	/**
	 * 查询TDSC_NOTICE_APP表中NOTICE_NO的list 20080514
	 */
	public List findNoticeNo() {
		Date nowDate = new Date();
		int nowYear = nowDate.getYear() + 1900;
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1=1");
		hql.append(" and a.noticeNo like '%").append(nowYear).append("%'");
		String queryHql = hql.toString() + " order by a.noticeNo";
		return this.findList(queryHql);
	}
	
	/**
	 * 查询TDSC_NOTICE_APP表中NOTICE_NO的list
	 */
	public List findTdscNoticeAppListByNoticeNo(String noticeNo) {
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1=1");
		hql.append(" and a.noticeNo = '").append(noticeNo).append("'");
		
		String queryHql = hql.toString() + " order by a.noticeId";
		return this.findList(queryHql);
	}

	public List queryNoticeIdListInTrade() {
		//查询出让文件（公告）已经发布，但公告交易结果尚未发布的公告信息
		StringBuffer hql = new StringBuffer(
				"select a.noticeId from TdscNoticeApp a where a.ifReleased='1' " +
				"and (a.ifResultPublish !='1' or a.ifResultPublish is null)  " +
				"order by a.noticeNo asc");
		List list = this.findList(hql.toString());
		return list;
	}
	
	
	public List queryNoticeIdListPublish() {
		//查询出让文件（公告）已经发布，但公告交易结果尚未发布的公告信息
		StringBuffer hql = new StringBuffer("select a.noticeId from TdscNoticeApp a where a.ifReleased='1'");
		List list = this.findList(hql.toString());
		return list;
	}
	
	
	/**
	 * 根据noticeId查询TdscBlockAppView列表
	 * @param noticeId
	 * @return
	 */
	public List queryAppViewListByNoticeId(String noticeId) {
		// 用户信息查询
		StringBuffer queryHql = new StringBuffer(
				"from TdscBlockAppView a where  a.noticeId ='").append(noticeId).append("' order by a.blockNoticeNo asc");

		List list = this.findList(queryHql.toString());
		return list;
	}

	/**
	 * 根据TdscNoticeAppCondition查询tdscnoticeapp
	 * @param condition
	 * @return
	 */
	public List findNoticeAppList(TdscNoticeAppCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1");
		
		List list = new ArrayList();
		
		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getNoticeId())) {
			hql.append(" and a.noticeId = :noticeId ");
		}
		if (condition.getNoticeIdList() != null && condition.getNoticeIdList().size() > 0) {
            hql.append(" and a.noticeId in (:noticeIdList) ");
        }
		if (StringUtils.isNotEmpty(condition.getTradeNum())) {
			hql.append(" and a.tradeNum like :tradeNum ");
		}
		if (StringUtils.isNotEmpty(condition.getNoticeNo())) {
			hql.append(" and a.noticeNo like :noticeNo ");
		}
		if (StringUtils.isNotEmpty(condition.getUniteBlockName())) {
			hql.append(" and a.uniteBlockName like :uniteBlockName ");
		}
		if (StringUtils.isNotEmpty(condition.getTransferMode())) {
			hql.append(" and a.transferMode = :transferMode ");
		}
		if (StringUtils.isNotEmpty(condition.getIfResultPublish())) {
			hql.append(" and a.ifResultPublish = :ifResultPublish ");
		}
		
		 if (StringUtils.isNotEmpty(condition.getOrderKey())) {
             hql.append(" order by a.").append(condition.getOrderKey()).append(" ");
             if(StringUtils.isNotEmpty(condition.getOrderType()))
             	hql.append(condition.getOrderType());
             else
             	hql.append("asc");
         } else {
             hql.append(" order by a.noticeId desc");
         }
		
		String queryHql = hql.toString();
		
		try {
            Query query = this.getSession().createQuery(queryHql.toString());
            
            if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                query.setParameter("noticeId", condition.getNoticeId());
            }
            if (condition.getNoticeIdList() != null && condition.getNoticeIdList().size() > 0) {
                query.setParameterList("noticeIdList", condition.getNoticeIdList());
            }
            if (StringUtils.isNotEmpty(condition.getTradeNum())) {
                query.setParameter("tradeNum", "%" + (condition.getTradeNum()) + "%");
            }
            if (StringUtils.isNotEmpty(condition.getNoticeNo())) {
                query.setParameter("noticeNo", "%" + (condition.getNoticeNo()) + "%");
            }
            if (StringUtils.isNotEmpty(condition.getUniteBlockName())) {
                query.setParameter("uniteBlockName", "%" + (condition.getUniteBlockName()) + "%");
            }
            if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                query.setParameter("transferMode", condition.getTransferMode());
            }
            if (StringUtils.isNotEmpty(condition.getIfResultPublish())) {
                query.setParameter("ifResultPublish", condition.getIfResultPublish());
            }
            
            list = (query.list());
        } catch (HibernateException ex) {
            throw new RuntimeException(ex);
        }
        if (list == null) {
            list = new ArrayList();
        }
		
		return list;
	}

	public String getCurrNoticeNoByNoticeNoPrefix(String startindex,String noticeNoPrefix) {
		StringBuffer hql = new StringBuffer("select max(substr(a.noticeNo,"+startindex+",2)) from TdscNoticeApp a where 1=1");
		hql.append(" and a.noticeNo like '%").append(StringUtil.GBKtoISO88591(noticeNoPrefix)).append("%'");
		String queryHql = hql.toString();
		List list = this.findList(queryHql);
		
		if (list == null || list.size() == 0) 
			return "0";
		if (list.size() > 0) {
			Object obj = list.get(0);
			if (obj == null)
				return "0";
			else 
				return (String)list.get(0);
		}
		return "0";
	}
}
