package com.wonders.tdsc.publishinfo.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBlockContractInfo;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;

public class TdscBlockContractInfoDao extends BaseHibernateDaoImpl{

	protected Class getEntityClass() {
        return TdscBlockContractInfo.class;
    }
	
	public TdscBlockContractInfo findTdscBlockContractInfoByBlockId(String blockId){
		StringBuffer hql = new StringBuffer("from TdscBlockContractInfo a where a.ifValidity='1' and a.blockId='").append(blockId).append("'");
        List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (TdscBlockContractInfo)list.get(0);
		}else{
			return null;
		}
	}

	public List findTdscBlockAppViewListWithoutNodeId(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0)
                hql.append(" and a.appId in (:appIdList) ");
            if (StringUtils.isNotEmpty(condition.getBlockName())) {
                hql.append(" and a.blockName like :blockName ");
            }
            if (StringUtils.isNotEmpty(condition.getBlockType())) {
                hql.append(" and a.blockType = :blockType ");
            }
            if (StringUtils.isNotEmpty(condition.getNodeId())) {
                hql.append(" and a.nodeId = :nodeId ");
            }
            if (StringUtils.isNotEmpty(condition.getStatusId())) {
                hql.append(" and a.statusId = :statusId ");
            }
            if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                hql.append(" and a.statusId in (:statusIdList)");
            }
            if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                hql.append(" and a.districtId = :districtId ");
            }
            if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                hql.append(" and a.districtId in (:districtIdList)");
            }
            if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                hql.append(" and a.transferMode = :transferMode ");
            }
            if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                hql.append(" and a.blockNoticeNo like :blockNoticeNo ");
            }
            if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
                hql.append(" and a.noitceNo  like :noitceNo ");
            }
            if (StringUtils.isNotEmpty(condition.getStatus())) {
                hql.append(" and a.status =:status ");
            }
            if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                hql.append(" and a.status in (:statusList)");
            }
            if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                hql.append(" and a.noticeId =:noticeId ");
            }
            if (StringUtils.isNotEmpty(condition.getTranResult())) {
                hql.append(" and a.tranResult =:tranResult ");
            }
            if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                hql.append(" and a.ifPublish =:ifPublish ");
            }
            if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                hql.append(" and a.auditedNum like :auditedNum ");
            }
            if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                hql.append(" and a.landLocation like :landLocation ");
            }
            if (condition.getIssueStartDate() != null) {
                hql.append(" and to_date(to_char(a.issueStartDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:issueStartDate ");
            }

            if (StringUtils.isNotEmpty(condition.getOrderKey())) {
                hql.append(" order by a.").append(condition.getOrderKey()).append(" ");
                if(StringUtils.isNotEmpty(condition.getOrderType()))
                	hql.append(condition.getOrderType());
                else
                	hql.append("asc");
            } else {
                hql.append(" order by a.blockId asc");
            }
            
            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                    query.setParameterList("appIdList", condition.getAppIdList());
                }
                if (StringUtils.isNotEmpty(condition.getBlockName())) {
                    query.setParameter("blockName", "%" + (condition.getBlockName()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                    query.setParameter("transferMode", condition.getTransferMode());
                }
                if (StringUtils.isNotEmpty(condition.getNodeId())) {
                    query.setParameter("nodeId", condition.getNodeId());
                }
                if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                    query.setParameterList("statusIdList", condition.getStatusIdList());
                }
                if (StringUtils.isNotEmpty(condition.getBlockType())) {
                    query.setParameter("blockType", condition.getBlockType());
                }
                if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                    query.setParameter("districtId", condition.getDistrictId());
                }
                if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                    query.setParameterList("districtIdList", condition.getDistrictIdList());
                }
                if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                    query.setParameter("blockNoticeNo", "%" + condition.getBlockNoticeNo() + "%");
                }
                if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
                    query.setParameter("noitceNo", "%" + (condition.getNoitceNo()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getStatus())) {
                    query.setParameter("status", condition.getStatus());
                }
                if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                    query.setParameterList("statusList", condition.getStatusList());
                }
                if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                    query.setParameter("noticeId", condition.getNoticeId());
                }
                if (StringUtils.isNotEmpty(condition.getTranResult())) {
                    query.setParameter("tranResult", condition.getTranResult());
                }
                if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                    query.setParameter("ifPublish", condition.getIfPublish());
                }
                if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                    query.setParameter("auditedNum", "%" + (condition.getAuditedNum()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                    query.setParameter("landLocation", "%" + (condition.getLandLocation()) + "%");
                }
                if (condition.getIssueStartDate() != null) {
                    query.setParameter("issueStartDate", condition.getIssueStartDate());
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
	 * 查询出让成交出让合同列表from视图TDSC_CONTRACK_INFO_VIEW    lz+   20090713
	 * @param condition
	 * @return
	 */
	public List findTdscContractInfoList(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscContrackInfoView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0){
                hql.append(" and a.appId in (:appIdList) ");
            }
            if (StringUtils.isNotEmpty(condition.getBlockName())) {
                hql.append(" and a.blockName like :blockName ");
            }
            if (StringUtils.isNotEmpty(condition.getLandLocation())) {
                hql.append(" and a.landLocation like :landLocation ");
            }
            if (StringUtils.isNotEmpty(condition.getAcceptPerson())) {
                hql.append(" and a.acceptPerson like :acceptPerson ");
            }
            if (StringUtils.isNotEmpty(condition.getContractNum())) {
                hql.append(" and a.contractNum like :contractNum ");
            }
            if (StringUtils.isNotEmpty(condition.getNodeId())) {
                hql.append(" and a.nodeId = :nodeId ");
            }
            if (StringUtils.isNotEmpty(condition.getStatusId())) {
                hql.append(" and a.statusId = :statusId ");
            }
            if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                hql.append(" and a.statusId in (:statusIdList)");
            }
            if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                hql.append(" and a.districtId = :districtId ");
            }
            if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                hql.append(" and a.districtId in (:districtIdList)");
            }
            if (StringUtils.isNotEmpty(condition.getStatus())) {
                hql.append(" and a.status = :status ");
            }
            if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                hql.append(" and a.status in (:statusList)");
            }
            if (StringUtils.isNotEmpty(condition.getTranResult())) {
                hql.append(" and a.tranResult = :tranResult ");
            }
            if (condition.getActionDate() != null) {
                hql.append(" and to_date(to_char(a.actionDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:actionDate ");
            }
            if (StringUtils.isNotEmpty(condition.getUserId())) {
                hql.append(" and a.userId =:userId ");
            }
            
            if (StringUtils.isNotEmpty(condition.getOrderKey())) {
                hql.append(" order by a.").append(condition.getOrderKey()).append(" ");
                if(StringUtils.isNotEmpty(condition.getOrderType()))
                	hql.append(condition.getOrderType());
                else
                	hql.append("asc");
            } else {
                hql.append(" order by a.blockId asc");
            }
            
            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                    query.setParameterList("appIdList", condition.getAppIdList());
                }
                if (StringUtils.isNotEmpty(condition.getBlockName())) {
                    query.setParameter("blockName", "%" + (condition.getBlockName()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getLandLocation())) {
                    query.setParameter("landLocation", "%" + (condition.getLandLocation()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getAcceptPerson())) {
                    query.setParameter("acceptPerson", "%" + (condition.getAcceptPerson()) + "%");
                }
                if (StringUtils.isNotEmpty(condition.getContractNum())) {
                    query.setParameter("contractNum", "%" + (condition.getContractNum())+ "%");
                }
                if (StringUtils.isNotEmpty(condition.getNodeId())) {
                    query.setParameter("nodeId", condition.getNodeId());
                }
                if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                    query.setParameterList("statusIdList", condition.getStatusIdList());
                }
                if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                    query.setParameter("districtId", condition.getDistrictId());
                }
                if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                    query.setParameterList("districtIdList", condition.getDistrictIdList());
                }
                if (StringUtils.isNotEmpty(condition.getStatus())) {
                    query.setParameter("status", condition.getStatus());
                }
                if (StringUtils.isNotEmpty(condition.getTranResult())) {
                    query.setParameter("tranResult", condition.getTranResult());
                }
                if (StringUtils.isNotEmpty(condition.getStatusId())) {
                    query.setParameter("statusId", condition.getStatusId());
                }
                if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                    query.setParameterList("statusList", condition.getStatusList());
                }
                if (condition.getActionDate() != null) {
                    query.setParameter("actionDate", condition.getActionDate());
                }
                if (StringUtils.isNotEmpty(condition.getUserId())) {
                    query.setParameter("userId", condition.getUserId());
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
}
