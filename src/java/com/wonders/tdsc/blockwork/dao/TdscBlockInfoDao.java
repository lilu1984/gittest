package com.wonders.tdsc.blockwork.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBlockInfoDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBlockInfo.class;
    }

    public PageList findPageList(TdscBlockInfoCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockInfo a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.blockId desc";

        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }

    public List blockInfoList(String status) {

        StringBuffer hql = new StringBuffer("select a.blockId from TdscBlockInfo a where 1 = 1");
        if (StringUtils.isNotEmpty(status)) {
            hql.append(" and a.status = '").append(status).append("'");
        }
        String queryHql = hql.toString() + " order by a.blockName asc";

        List blockInfoList = this.findList(queryHql);

        // return this.findList(queryHql);
        return blockInfoList;
    }
    
    public TdscBlockInfo blockInfoByAuditeNum(String auditeNum) {
    	TdscBlockInfo tdscBlockInfo = null;
        StringBuffer hql = new StringBuffer("select a.blockId from TdscBlockInfo a where 1 = 1 and a.auditedNum='");
        hql.append(auditeNum).append("'");
        List blockInfoList = this.findList(hql.toString());
        if(blockInfoList !=null && blockInfoList.size() >0)
        	tdscBlockInfo= (TdscBlockInfo)blockInfoList.get(0);
        return tdscBlockInfo;
    }

    public TdscBlockInfo findBlockInfo(String blockId) {
        // 用户信息查询
        return (TdscBlockInfo) this.get(blockId);
    }

    public void saveTdscBlockInfo(TdscBlockInfo tdscBlockInfo) {
        save(tdscBlockInfo);
    }

    public void deleteTdscBlockInfo(TdscBlockInfo tdscBlockInfo) {
        delete(tdscBlockInfo);
    }

    // 根据条件查APPLIST
    public List queryAppList(TdscBaseQueryCondition condition) throws Exception {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("select a.appId from TdscBlockAppView a where 1=1");
            if (StringUtils.isNotEmpty(condition.getBlockName())) {
                hql.append(" and a.blockName like :blockName ");
            }
            if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                hql.append(" and a.blockNoticeNo like :blockNoticeNo ");
            }
            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (StringUtils.isNotEmpty(condition.getBlockName())) {
                    query.setParameter("blockName", "%" + condition.getBlockName() + "%");
                }
                if (StringUtils.isNotEmpty(condition.getBlockNoticeNo())) {
                    query.setParameter("blockNoticeNo", "%" + condition.getBlockNoticeNo() + "%");
                }
                list = query.list();

            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            return list;
        } else
            return null;
    }

    /**
     * 根据用户查询条件对象组装条件语句
     * 
     * @param condition
     *            用户查询条件对象
     * @return String 条件语句
     */
    private String makeWhereClause(TdscBlockInfoCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getBlockName())) {
            whereClause.append(" and a.blockName like '%").append(StringUtil.GBKtoISO88591(condition.getBlockName().trim())).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getAuditedNum())) {
            whereClause.append(" and a.auditedNum like '%").append(StringUtil.GBKtoISO88591(condition.getAuditedNum().trim())).append("%' ");
        }

        if (condition.getStatus() != null && condition.getStatus().length > 0) {
            String statusStr = "";
            String[] status = condition.getStatus();
            for (int i = 0; i < status.length; i++) {
                statusStr += "'" + status[i] + "',";
            }
            statusStr = statusStr.substring(0, statusStr.length() - 1);
            whereClause.append(" and a.status in (").append(statusStr).append(") ");
        }

        if (condition.getDistrictId() != null) {
            whereClause.append(" and a.districtId = ").append(condition.getDistrictId()).append(" ");
        }

       
        if (StringUtils.isNotEmpty(condition.getBlockType())) {
            whereClause.append(" and a.blockType like '%").append(condition.getBlockType().trim()).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getBlockId())) {
            whereClause.append(" and a.blockId like '%").append(condition.getBlockId().trim()).append("%' ");
        }
        //根据用户查询“储备地块录入”信息
        if (StringUtils.isNotEmpty(condition.getUserId())) {
            whereClause.append(" and (a.userId = '").append(condition.getUserId()).append("' or a.userId is null) ");
        }

        return whereClause.toString();
    }

    public String upLoadFile(FormFile upLoadFile) {
        // 传入文件名
        String fromFileName = upLoadFile.getFileName();
        String outFileName = fromFileName;
        String filePath = (String) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.UPLOAD_ANALYSIS_REPORT);

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

    public TdscBlockInfo getTdscBlockInfoByAuditedNum(String auditedNum) {
        StringBuffer hql = new StringBuffer("from TdscBlockInfo a where a.auditedNum='").append(auditedNum).append("'");
        List list = this.findList(hql.toString());
        if (list != null && list.size() > 0) {
            return (TdscBlockInfo) list.get(0);
        } else {
            return null;
        }
    }
    public List getTdscBlockInfoListByAuditedNum(String[] auditedNum) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(TdscBlockInfo.class);
    	if(auditedNum !=null && auditedNum.length>0)
    		criteria.add(Restrictions.in("auditedNum",auditedNum));
    	    criteria.add(Restrictions.eq("status","00"));
    		criteria.addOrder(Order.asc("auditedNum"));
		return getHibernateTemplate().findByCriteria(criteria);
    }
    public List getTdscBlockInfoListByAuditedNum(String auditedNum) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(TdscBlockInfo.class);
    	if(StringUtils.isNotEmpty(auditedNum))
    		criteria.add(Restrictions.like("auditedNum","%"+auditedNum+"%"));
    	    criteria.add(Restrictions.eq("status","00"));
    		criteria.addOrder(Order.asc("auditedNum"));
		return getHibernateTemplate().findByCriteria(criteria);
    }
    
    /**
     * 根据条件查询TdscBlockInfo表
     * @param condition
     * @return
     */
    public List queryBlockInfoList(TdscBlockInfoCondition condition ){
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockInfo a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));
        
        List blockInfoList = this.findList(hql.toString()+ " order by a.actionDateBlock desc");
    	
        return blockInfoList;
    }
    /**
     * 根据条件查询TdscBlockInfo表
     * @param condition
     * @return
     */
    public List queryAppViewList(TdscBlockInfoCondition condition ){
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockAppView a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));
        
        List blockInfoList = this.findList(hql.toString()+ " order by a.actionDateBlock desc");
    	
        return blockInfoList;
    }
    
    public List queryTdscBlockInfoList(TdscBlockPlanTableCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscBlockInfo a where 1 = 1  and result_date is not null ");
        if (condition.getDistrictId()!=null) {
        	hql.append(" and a.districtId='").append(condition.getDistrictId()).append("'");
        	
        }
        if (StringUtils.isNotEmpty(condition.getBlockQuality())) {
        	hql.append(" and a.blockQuality ='").append(condition.getBlockQuality()).append("'");
        }
        if (StringUtils.isNotEmpty(condition.getCountUse())) {
        	hql.append(" and a.countUse ='").append(condition.getCountUse()).append("'");
        }
        if (condition.getResultDate()!=null && condition.getResultDate1()==null) {
        	hql.append(" and a.resultDate>=TO_DATE('").append(DateUtil.date2String(condition.getResultDate(), DateUtil.FORMAT_DATE)).append("', 'yyyy-mm-dd hh24:mi:ss')");
        }
        if(condition.getResultDate1()!=null && condition.getResultDate()==null){
        	hql.append(" and a.resultDate<=TO_DATE('").append(DateUtil.date2String(condition.getResultDate1(), DateUtil.FORMAT_DATE)).append("', 'yyyy-mm-dd hh24:mi:ss')");
        }
        if(condition.getResultDate()!=null && condition.getResultDate1()!=null){
        	hql.append(" and a.resultDate>=TO_DATE('").append(DateUtil.date2String(condition.getResultDate(), DateUtil.FORMAT_DATE)).append("', 'yyyy-mm-dd hh24:mi:ss')").append("  and a.resultDate<=TO_DATE('").append(DateUtil.date2String(condition.getResultDate1(), DateUtil.FORMAT_DATE)).append("', 'yyyy-mm-dd hh24:mi:ss')");
        }
        return this.findList(hql.toString());
    }
    
}
