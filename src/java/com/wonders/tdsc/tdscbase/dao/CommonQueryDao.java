package com.wonders.tdsc.tdscbase.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.bo.hibernate.HqlParameter;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.CharsetConvertUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscListingInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class CommonQueryDao extends BaseHibernateDaoImpl {

    public Class getEntityClass() {
        return TdscBlockAppView.class;
    }

    /**
     * 根据查询条件返回 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
     * 
     * @param condition
     * @return
     */

    public TdscBlockAppView getTdscBlockAppView(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (StringUtils.isNotEmpty(condition.getAppId())) {
                hql.append(" and a.appId = :appId ");
            }
            try {
                Query query = this.getSession().createQuery(hql.toString());

                if (StringUtils.isNotEmpty(condition.getAppId())) {
                    query.setParameter("appId", condition.getAppId());
                }
                list = query.list();
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }

            TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();

            if (list != null && list.size() > 0) {
                tdscBlockAppView = (TdscBlockAppView) list.get(0);
                List tdscBlockUsedInfoList = queryTdscBlockUsedInfoListByBlockId(tdscBlockAppView.getBlockId());
                tdscBlockAppView.setTdscBlockUsedInfoList(tdscBlockUsedInfoList);
            }
            return tdscBlockAppView;
        } else {
            return null;
        }
    }

    /**
     * 根据查询条件返回 地块交易信息,地块基本信息,进度安排表,地块用途信息列表
     * 
     * @param blockId
     * @return
     */

    public TdscBlockAppView getTdscBlockAppViewByBlockId(String blockId) {
        if (blockId != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (StringUtils.isNotEmpty(blockId)) {
                hql.append(" and a.blockId = :blockId ");
            }
           
            try {
                Query query = this.getSession().createQuery(hql.toString());

                if (StringUtils.isNotEmpty(blockId)) {
                    query.setParameter("blockId", blockId);
                }
                list = query.list();
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }

            TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();

            if (list != null && list.size() > 0) {
                tdscBlockAppView = (TdscBlockAppView) list.get(0);
                List tdscBlockUsedInfoList = queryTdscBlockUsedInfoListByBlockId(tdscBlockAppView.getBlockId());
                tdscBlockAppView.setTdscBlockUsedInfoList(tdscBlockUsedInfoList);
            }
            return tdscBlockAppView;
        } else {
            return null;
        }
    }
    
    
    /**
     * 通过地块ID来查询土地用途信息
     * 
     * @param blockId
     * @return
     */
    public List queryTdscBlockUsedInfoListByBlockId(String blockId) {
        StringBuffer hql = new StringBuffer("from TdscBlockUsedInfo a where a.blockId = '").append(blockId).append("'");
        return this.findList(hql.toString());
    }

    /**
     * 通过条件来查询挂牌信息
     * 
     * @param blockId
     * @return
     */
    public List queryTdscListingInfoListByAppId(TdscListingInfoCondition condition) {
        if (condition != null && condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
            StringBuffer hql = new StringBuffer("from TdscListingInfo a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                hql.append(" and a.appId in (:appIdList) ");
            }
            return this.findList(hql.toString());
        } else {
            return null;
        }
    }

    public List findTdscBlockAppViewList(TdscBaseQueryCondition condition) {
        if (condition != null && condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                hql.append(" and a.appId in (:appIdList) ");
                if (StringUtils.isNotEmpty(condition.getBlockName())) {
                    hql.append(" and a.blockName like :blockName ");
                }
                if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                    hql.append(" and a.transferMode = :transferMode ");
                }

                if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                    hql.append(" and a.blockNoticeNo like :blockNoticeNo ");
                }
                if (condition.getNoitceNo() != null && condition.getNoitceNo().equals("null")) {
                    hql.append(" and a.noitceNo is null");
                } else if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
                    hql.append(" and a.noitceNo like :noitceNo ");
                }
                if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                    hql.append(" and a.noticeId = :noticeId ");
                }
                if (StringUtils.isNotEmpty(condition.getPlanId())) {
                    hql.append(" and a.planId = :planId ");
                }
                if (StringUtils.isNotEmpty(condition.getStatusId())) {
                    hql.append(" and a.statusId =:statusId ");
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
                if (StringUtils.isNotEmpty(condition.getTranResult())) {
                    hql.append(" and a.tranResult = :tranResult ");
                }
                if (condition.getIssueStartDate() != null) {
                    hql.append(" and to_date(to_char(a.issueStartDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:issueStartDate ");
                }
                if (StringUtils.isNotEmpty(condition.getBlockType())) {
                    hql.append(" and a.blockType =:blockType ");
                }
                if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                    hql.append(" and a.auditedNum like :auditedNum ");
                }
                if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                    hql.append(" and a.ifPublish like :ifPublish ");
                }
                if (StringUtils.isNotEmpty(condition.getUserId())) {
                    hql.append(" and a.userId =:userId ");
                }
                if (StringUtils.isNotEmpty(condition.getIfOnLine())) {
                    hql.append(" and a.ifOnLine =:ifOnLine ");
                }
            }

            if (StringUtils.isNotEmpty(condition.getOrderKey())) {
                hql.append(" order by a.").append(condition.getOrderKey()).append(" asc");
            } else {
                hql.append(" order by a.blockId asc");
            }
            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                    query.setParameterList("appIdList", condition.getAppIdList());
                    if (StringUtils.isNotEmpty(condition.getBlockName())) {
                        query.setParameter("blockName", "%" + (condition.getBlockName()) + "%");
                    }
                    if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                        query.setParameter("transferMode", condition.getTransferMode());
                    }
                    if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                        query.setParameter("blockNoticeNo", "%" + condition.getBlockNoticeNo() + "%");
                    }
                    if (condition.getNoitceNo() != null && !condition.getNoitceNo().equals("null")) {
                        query.setParameter("noitceNo", "%" + (CharsetConvertUtil.gbkToIso(condition.getNoitceNo())) + "%");
                    }
                    if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                    	query.setParameter("noticeId", condition.getNoticeId());
                    }
                    if (StringUtils.isNotEmpty(condition.getPlanId())) {
                    	query.setParameter("planId", condition.getPlanId());
                    }
                    if (StringUtils.isNotEmpty(condition.getStatusId())) {
                        query.setParameter("statusId", condition.getStatusId());
                    }
                    if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                        query.setParameterList("statusIdList", condition.getStatusIdList());
                    }
                    if (StringUtils.isNotEmpty(condition.getTranResult())) {
                        query.setParameter("tranResult", condition.getTranResult());
                    }
                    if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                        query.setParameter("districtId", condition.getDistrictId());
                    }
                    if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                        query.setParameterList("districtIdList", condition.getDistrictIdList());
                    }
                    if (condition.getIssueStartDate() != null) {
                        query.setParameter("issueStartDate", condition.getIssueStartDate());
                    }
                    if (StringUtils.isNotEmpty(condition.getBlockType())) {
                        query.setParameter("blockType", condition.getBlockType());
                    }
                    if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                        query.setParameter("auditedNum", "%" + (condition.getAuditedNum()) + "%");
                    }
                    if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                        query.setParameter("ifPublish", condition.getIfPublish());
                    }
                    if (StringUtils.isNotEmpty(condition.getUserId())) {
                        query.setParameter("userId", condition.getUserId());
                    }
                    if (StringUtils.isNotEmpty(condition.getIfOnLine())) {
                        query.setParameter("ifOnLine", condition.getIfOnLine());
                    }
                }
                list = (query.list());
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
                    List tdscBlockUsedInfoList = queryTdscBlockUsedInfoListByBlockId(tdscBlockAppView.getBlockId());
                    tdscBlockAppView.setTdscBlockUsedInfoList(tdscBlockUsedInfoList);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public List findTdscBlockAppViewListWithoutNodeId(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0){
                hql.append(" and a.appId in (:appIdList) ");
            }
            if (StringUtils.isNotEmpty(condition.getAppId())) {
                hql.append(" and a.appId = :appId ");
            }
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
                hql.append(" and a.noticeId = :noticeId ");
            }
            if (StringUtils.isNotEmpty(condition.getPlanId())) {
                hql.append(" and a.planId =:planId ");
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
                if (StringUtils.isNotEmpty(condition.getAppId())) {
                    query.setParameter("appId", condition.getAppId());
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
                if (StringUtils.isNotEmpty(condition.getPlanId())) {
                    query.setParameter("planId", condition.getPlanId());
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
                if (condition.getIssueStartDate() != null) {
                    query.setParameter("issueStartDate", condition.getIssueStartDate());
                }

                list = (query.list());
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            // TdscListingInfoCondition condition1=new TdscListingInfoCondition();
            // condition1.setAppIdList(condition.getAppIdList());
            // List tdscListingInfoList = this.queryTdscListingInfoListByAppId(condition1);
            // List list1=new ArrayList();
            // if (list != null && list.size() > 0) {
            // for(int i=0;i<list.size();i++){
            // Map map=new HashMap();
            // TdscBlockAppView tdscBlockAppView = (TdscBlockAppView)list.get(i);
            // map.put("tdscBlockAppView", tdscBlockAppView);
            // if(tdscListingInfoList!=null && tdscListingInfoList.size()>0)
            // {
            // TdscListingInfo tdscListingInfo = (TdscListingInfo)tdscListingInfoList.get(i);
            // if(tdscBlockAppView.getAppId()==tdscListingInfo.getAppId())
            // {
            // map.put("tdscListingInfo", tdscListingInfo);
            // }
            // else
            // {
            // map.put("tdscListingInfo", new TdscListingInfo());
            // }
            // }
            // list1.add(map);
            // }
            // }
            return list;
        } else {
            return null;
        }
    }
    
    
    public List queryTdscBlockAppViewListWithoutNodeOrderByDistrictAndName(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0){
                hql.append(" and a.appId in (:appIdList) ");
            }
            if (StringUtils.isNotEmpty(condition.getAppId())) {
                hql.append(" and a.appId = :appId ");
            }
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
                hql.append(" and a.noticeId = :noticeId ");
            }
            if (StringUtils.isNotEmpty(condition.getPlanId())) {
                hql.append(" and a.planId =:planId ");
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
            if (condition.getIssueStartDate() != null) {
                hql.append(" and to_date(to_char(a.issueStartDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:issueStartDate ");
            }

            hql.append(" order by a.districtId asc, a.blockName asc");
            
            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                    query.setParameterList("appIdList", condition.getAppIdList());
                }
                if (StringUtils.isNotEmpty(condition.getAppId())) {
                    query.setParameter("appId", condition.getAppId());
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
                if (StringUtils.isNotEmpty(condition.getPlanId())) {
                    query.setParameter("planId", condition.getPlanId());
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
           
    
    public List findTdscBlockAppViewListByPlanId(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            
            if (StringUtils.isNotEmpty(condition.getStatus())) {
                hql.append(" and a.status = '"+condition.getStatus()+"'");
            }
            
            if (StringUtils.isNotEmpty(condition.getPlanId())) {
                hql.append(" and a.planId = '"+condition.getPlanId()+"' ");
            }
            
            if (StringUtils.isNotEmpty(condition.getNoticeId())) {
            	hql.append(" and a.noticeId = '"+ condition.getNoticeId()+"'");
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
            	list = this.getSession().createQuery(hql.toString()).list();
            	//list = this.findList(hql.toString());
               
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            return list;
        } else {
            return null;
        }
    }

    public PageList findTdscBlockAppViewPageList(TdscBaseQueryCondition condition) {
        if (condition != null && condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (StringUtils.isNotEmpty(condition.getSelectSpecialistNotaryID())) {
                hql = new StringBuffer("from TdscBlockAppView a, TdscBlockSelectApp b where 1=1");
            }
            List paralist = null;
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                paralist = new ArrayList();

                hql.append(" and a.appId in (:appIdList) ");
                paralist.add(new HqlParameter("appIdList", condition.getAppIdList()));

                if (StringUtils.isNotEmpty(condition.getSelectSpecialistNotaryID())) {
                    hql.append(" and a.appId=b.appId and b.selectType='04' and b.selectedId = :selectedId ");
                    paralist.add(new HqlParameter("selectedId", condition.getSelectSpecialistNotaryID()));
                }
                if (StringUtils.isNotEmpty(condition.getBlockName())) {
                    hql.append(" and a.blockName like :blockName ");
                    paralist.add(new HqlParameter("blockName", "%" + (condition.getBlockName()) + "%"));
                    
                }
                if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                    hql.append(" and a.auditedNum like :auditedNum ");
                    paralist.add(new HqlParameter("auditedNum", "%" + (condition.getAuditedNum()) + "%"));
                }
                if (StringUtils.isNotEmpty(condition.getTransferMode())) {
                    hql.append(" and a.transferMode = :transferMode ");
                    paralist.add(new HqlParameter("transferMode", condition.getTransferMode()));
                }
                if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                    hql.append(" and a.blockNoticeNo like :blockNoticeNo ");
                    paralist.add(new HqlParameter("blockNoticeNo", "%" + (condition.getBlockNoticeNo()) + "%"));
                }
                if (condition.getNoitceNo() != null && condition.getNoitceNo().equals("null")) {
                    hql.append(" and a.noitceNo is null");
                } else if (StringUtils.isNotEmpty(condition.getNoitceNo())) {
                    hql.append(" and a.noitceNo like :noitceNo ");
                    paralist.add(new HqlParameter("noitceNo", "%" + (condition.getNoitceNo()) + "%"));
                }
                if (StringUtils.isNotEmpty(condition.getTranResult())) {
                    hql.append(" and a.tranResult =:tranResult ");
                    paralist.add(new HqlParameter("tranResult", condition.getTranResult()));
                }
                if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                    hql.append(" and a.ifPublish =:ifPublish ");
                    paralist.add(new HqlParameter("ifPublish", condition.getIfPublish()));
                }
                if (StringUtils.isNotEmpty(condition.getStatusId())) {
                    hql.append(" and a.statusId =:statusId ");
                    paralist.add(new HqlParameter("statusId", condition.getStatusId()));
                }
                if (StringUtils.isNotEmpty(condition.getStatus())) {
                    hql.append(" and a.status =:status ");
                    paralist.add(new HqlParameter("status", condition.getStatus()));
                }
                if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                    hql.append(" and a.statusId in (:statusIdList)");
                    paralist.add(new HqlParameter("statusIdList", condition.getStatusIdList()));
                }
                if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                    hql.append(" and a.districtId = :districtId ");
                    paralist.add(new HqlParameter("districtId", condition.getDistrictId()));
                }
                if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
                    hql.append(" and a.districtId in (:districtIdList)");
                    paralist.add(new HqlParameter("districtIdList", condition.getDistrictIdList()));
                }
                if (condition.getIssueStartDate() != null) {
                    hql.append(" and to_date(to_char(a.issueStartDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:issueStartDate ");
                    paralist.add(new HqlParameter("issueStartDate", condition.getIssueStartDate()));
                }
                if (StringUtils.isNotEmpty(condition.getBlockType())) {
                    hql.append(" and a.blockType =:blockType ");
                    paralist.add(new HqlParameter("blockType", condition.getBlockType()));
                }
                if (StringUtils.isNotEmpty(condition.getHasSelectCompere())) {
                    hql.append(" and a.hasSelectCompere =:hasSelectCompere ");
                    paralist.add(new HqlParameter("hasSelectCompere", condition.getHasSelectCompere()));
                }
                if (StringUtils.isNotEmpty(condition.getHasSelectBNotary())) {
                    hql.append(" and a.hasSelectBNotary =:hasSelectBNotary ");
                    paralist.add(new HqlParameter("hasSelectBNotary", condition.getHasSelectBNotary()));
                }
                if (StringUtils.isNotEmpty(condition.getHasSelectCNotary())) {
                    hql.append(" and a.hasSelectCNotary =:hasSelectCNotary ");
                    paralist.add(new HqlParameter("hasSelectCNotary", condition.getHasSelectCNotary()));
                }
                if (StringUtils.isNotEmpty(condition.getHasSelectSpecialist())) {
                    hql.append(" and a.hasSelectSpecialist =:hasSelectSpecialist ");
                    paralist.add(new HqlParameter("hasSelectSpecialist", condition.getHasSelectSpecialist()));
                }
                if (StringUtils.isNotEmpty(condition.getUserId())) {
                    hql.append(" and a.userId = :userId ");
                    paralist.add(new HqlParameter("userId", condition.getUserId()));
                }
                if (StringUtils.isNotEmpty(condition.getIfOnLine())) {
                    hql.append(" and a.ifOnLine = :ifOnLine ");
                    paralist.add(new HqlParameter("ifOnLine", condition.getIfOnLine()));
                }
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

            // List list = new ArrayList();
            // PageList pageList = new PageList();
            // try {
            // Query query = this.getSession().createQuery(hql.toString());
            // if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
            // query.setParameterList("appIdList", condition.getAppIdList());
            // if (StringUtils.isNotEmpty(condition.getBlockName())) {
            // query.setParameter("blockName", "%" + (condition.getBlockName()) + "%");
            // }
            // if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
            // query.setParameter("auditedNum", condition.getAuditedNum());
            // }
            // if (StringUtils.isNotEmpty(condition.getTransforMode())) {
            // query.setParameter("transferMode", condition.getTransforMode());
            // }
            // if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
            // query.setParameter("blockNoticeNo", "%" + condition.getBlockNoticeNo() + "%");
            // }
            // if (condition.getNoitceNo() != null && !condition.getNoitceNo().equals("null")) {
            // query.setParameter("noitceNo", "%" + (condition.getNoitceNo()) + "%");
            // }
            // if (StringUtils.isNotEmpty(condition.getStatusId())) {
            // query.setParameter("statusId", condition.getStatusId());
            // }
            // if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
            // query.setParameterList("statusIdList", condition.getStatusIdList());
            // }
            // if (StringUtils.isNotEmpty(condition.getDistrictId())) {
            // query.setParameter("districtId", condition.getDistrictId());
            // }
            // if (condition.getDistrictIdList() != null && condition.getDistrictIdList().size() > 0) {
            // query.setParameterList("districtIdList", condition.getDistrictIdList());
            // }
            // if (condition.getIssueStartDate() != null) {
            // query.setParameter("issueStartDate", condition.getIssueStartDate());
            // }
            // if (StringUtils.isNotEmpty(condition.getBlockType())) {
            // query.setParameter("blockType", condition.getBlockType());
            // }
            // }
            //
            // // TODO 分页优化
            // // query.setFirstResult(startRow);
            // // query.setMaxResults(pageSize);
            //                
            // list = (query.list());
            // } catch (HibernateException ex) {
            // throw new RuntimeException(ex);
            // }
            //
            // if (list == null) {
            // list = new ArrayList();
            // }
            // pageList.setList(list);
            // // 拼装分页信息
            // String pageNo = String.valueOf(condition.getCurrentPage());
            int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
            // int currentPage = 0;
            // if (pageNo != null && pageNo.trim().equals("") == false) {
            // currentPage = Integer.parseInt(pageNo);
            // }
            // if (pageNo == null || Integer.parseInt(pageNo) < 1) {
            // currentPage = 1;
            // }
            // pageList = PageUtil.getPageList(list, pageSize, currentPage);
            //
            // List pageSizeList = null;
            // if (pageList == null) {
            // pageSizeList = new ArrayList();
            // } else {
            // pageSizeList = pageList.getList();
            //
            // }
            //
            // if (pageSizeList != null && pageSizeList.size() > 0) {
            // for (int i = 0; i < pageSizeList.size(); i++) {
            // TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) pageSizeList.get(i);
            // List tdscBlockUsedInfoList = queryTdscBlockUsedInfoListByBlockId(tdscBlockAppView.getBlockId());
            // tdscBlockAppView.setTdscBlockUsedInfoList(tdscBlockUsedInfoList);
            // }
            // }
            // if (pageList != null) {
            // pageList.setList(pageSizeList);
            // }
            if (StringUtils.isNotEmpty(condition.getSelectSpecialistNotaryID())) {
                String queryHql = "select a " + hql.toString();
                return this.findPageListWithHqlAdvance("select count(*) " + hql.toString(), queryHql, paralist,
                        pageSize, condition.getCurrentPage());
            } else {
                return this.findPageListWithHqlAdvance("select count(*) " + hql.toString(), hql.toString(), paralist,
                        pageSize, condition.getCurrentPage());
            }
        } else {
            return null;
        }
    }

    public PageList findTdscBlockAppViewPageListWithoutNodeId(TdscBaseQueryCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            PageList pageList = new PageList();
            StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                hql.append(" and a.appId in (:appIdList) ");
            }
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
                hql.append(" and a.statusId <> :statusId ");
            }
            if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                hql.append(" and a.statusId in (:statusIdList)");
            }
            if (condition.getStatusIdList2() != null && condition.getStatusIdList2().size() > 0) {
                hql.append(" and a.statusId not in (:statusIdList2)");
            }
            if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                hql.append(" and a.districtId = :districtId ");
            }
            if (StringUtils.isNotEmpty(condition.getEndorseDistrict())) {
                hql.append(" and a.endorseDistrict = :endorseDistrict ");
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
            if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                hql.append(" and a.status in (:statusList)");
            }
            if (StringUtils.isNotEmpty(condition.getStatus())) {
                hql.append(" and a.status = :status ");
            }
            if (StringUtils.isNotEmpty(condition.getNoticeId())) {
                hql.append(" and a.noticeId =:noticeId ");
            }
            if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
                hql.append(" and a.auditedNum like :auditedNum ");
            }
            if (StringUtils.isNotEmpty(condition.getTranResult())) {
                hql.append(" and a.tranResult = :tranResult ");
            }
            if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                hql.append(" and a.ifPublish = :ifPublish ");
            }
            if (StringUtils.isNotEmpty(condition.getBlockAuditStatus())) {
                if ("00".equals(condition.getBlockAuditStatus())) {
                    hql.append(" and (a.statusId is null or a.statusId ='0101')");
                }
                if ("01".equals(condition.getBlockAuditStatus())) {
                    hql.append(" and a.statusId = '0102' ");
                }
                if ("02".equals(condition.getBlockAuditStatus())) {
                    hql.append(" and a.nodeId ='01' and a.statusId <> '0102' ");
                }
            }
            if (condition.getIssueStartDate() != null) {
                hql.append(" and to_date(to_char(a.issueStartDate, 'yyyy-mm-dd'), 'yyyy-mm-dd') =:issueStartDate ");
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
                //hql.append(" order by a.blockId asc");
                hql.append(" order by a.actionDateBlock desc");
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
                if (StringUtils.isNotEmpty(condition.getStatusId())) {
                    query.setParameter("statusId", condition.getStatusId());
                }
                if (condition.getStatusIdList() != null && condition.getStatusIdList().size() > 0) {
                    query.setParameterList("statusIdList", condition.getStatusIdList());
                }
                if (condition.getStatusIdList2() != null && condition.getStatusIdList2().size() > 0) {
                    query.setParameterList("statusIdList2", condition.getStatusIdList2());
                }
                if (StringUtils.isNotEmpty(condition.getBlockType())) {
                    query.setParameter("blockType", condition.getBlockType());
                }
                if (StringUtils.isNotEmpty(condition.getDistrictId())) {
                    query.setParameter("districtId", condition.getDistrictId());
                }
                if (StringUtils.isNotEmpty(condition.getEndorseDistrict())) {
                    query.setParameter("endorseDistrict", condition.getEndorseDistrict());
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
                if (condition.getStatusList() != null && condition.getStatusList().size() > 0) {
                    query.setParameterList("statusList", condition.getStatusList());
                }
                if (StringUtils.isNotEmpty(condition.getStatus())) {
                    query.setParameter("status", condition.getStatus());
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
                if (condition.getIssueStartDate() != null) {
                    query.setParameter("issueStartDate", condition.getIssueStartDate());
                }
                if (StringUtils.isNotEmpty(condition.getUserId())) {
                    query.setParameter("userId", condition.getUserId());
                }
                list = (query.list());
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            if (list == null) {
                list = new ArrayList();
            }
            pageList.setList(list);
            // 拼装分页信息
            String pageNo = String.valueOf(condition.getCurrentPage());
            int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
            int currentPage = 0;
            if (pageNo != null && pageNo.trim().equals("") == false) {
                currentPage = Integer.parseInt(pageNo);
            }
            if (pageNo == null || Integer.parseInt(pageNo) < 1) {
                currentPage = 1;
            }
            pageList = PageUtil.getPageList(list, pageSize, currentPage);
            List pageSizeList = null;
            if (pageList == null) {
                pageSizeList = new ArrayList();
            } else {
                pageSizeList = pageList.getList();

            }
            if (pageSizeList != null && pageSizeList.size() > 0) {
                for (int i = 0; i < pageSizeList.size(); i++) {
                    TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) pageSizeList.get(i);
                    if (null != tdscBlockAppView) {
                        List tdscBlockUsedInfoList = queryTdscBlockUsedInfoListByBlockId(tdscBlockAppView.getBlockId());
                        tdscBlockAppView.setTdscBlockUsedInfoList(tdscBlockUsedInfoList);
                    }
                }
            }
            if (pageList != null) {
                pageList.setList(pageSizeList);
            }
            return pageList;
        } else {
            return null;
        }
    }

    /**
     * 查找全部结束的方案
     * @return
     */
	public List queryTdscPlanListOfEnd() {
		String sql = "select t.plan_id from (select count(t.plan_id) as num, t.plan_id from wxlandtrade.tdsc_block_app_view t where t.plan_id is not null and t.node_id = '90' group by t.plan_id) t, (select count(t.plan_id) as num, t.plan_id from wxlandtrade.tdsc_block_app_view t where t.plan_id is not null group by t.plan_id) m where t.plan_id = m.plan_id and t.num = m.num";
		//String sql = "select t.planId from (select count(t.planId) num, t.planId from TdscBlockAppView t where t.status = '01' and t.planId is not null and t.nodeId = '90' group by t.planId) t, (select count(t.planId) num, t.planId from TdscBlockAppView t where  t.status = '01' and t.planId is not null group by t.planId) m where t.planId = m.planId and t.num = m.num";
		Query query = this.getSession().createSQLQuery(sql);
    	List result = query.list();
		return result;
	}

	
	/**
     * 通过条件来查询挂牌信息
     * 
     * @param blockId
     * @return
     */
    public PageList queryTdscBlockAppInNoticeId(List noticeId, int currentPage, String noitceNo, String blockNoticeNo, String blockName) {
    	if (noticeId == null || noticeId.size() == 0) return null;
    	StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1=1");
    	List param = new ArrayList();
        if (noticeId != null || noticeId.size() > 0) {
        	hql.append(" and a.noticeId in (:noticeId) ");
        	param.add(new HqlParameter("noticeId", noticeId));
        }
        if (StringUtils.isNotEmpty(noitceNo)) {
        	hql.append(" and a.noitceNo like '%" + (String) CharsetConvertUtil.gbkToIso(noitceNo) + "%'");
        }
        if (StringUtils.isNotEmpty(blockNoticeNo)) {
        	hql.append(" and a.blockNoticeNo like '%" + (String) CharsetConvertUtil.gbkToIso(blockNoticeNo) + "%'");
        }
        if (StringUtils.isNotEmpty(blockName)) {
        	hql.append(" and a.blockName like '%" + (String) CharsetConvertUtil.gbkToIso(blockName) + "%'");
        }
        hql.append(" and a.status <> '00' and a.tranResult <> '04'");
        
        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        
        hql.append(" order by a.noitceNo desc, a.xuHao");
        return this.findPageListWithHqlAdvance("select count(*) " + hql.toString(), hql.toString(), param,
                pageSize, currentPage);
    }
}