package com.wonders.tdsc.publishinfo.dao;

import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockConInfo;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscSendEmailInfo;

public class TdscSendEmailInfoDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscSendEmailInfo.class;
    }
    
    
    /**
     * 根据文件ID（noticeId）查询List
     * @param noticeId
     * @return
     */
    public List querySendEmailList(String noticeId){
		StringBuffer hql = new StringBuffer("from TdscSendEmailInfo a where a.noticeId = '").append(noticeId).append("' order by a.emailId desc");

		return findList(hql.toString());
    }
    
    /**
     * 根据文件ID（noticeId）和公告类型NOTICE_TYPE查询List
     * @param noticeId，NOTICE_TYPE
     * @return
     */
    public List querySendEmailListByType(String noticeId, String noticeType){
		StringBuffer hql = new StringBuffer("from TdscSendEmailInfo a where a.noticeId = '").append(noticeId);
		if(noticeType!=null&&!"".equals(noticeType))
		hql.append("' and a.noticeType = '").append(noticeType);
		
		hql.append("' order by a.emailId desc");
		return findList(hql.toString());
    }
    
    /**
     * 邮件保存  根据noticeId查询，如存在，全删除再写入。
     * @param tdscSendEmailList
     * @return
     */
    public void saveSendEmailList(List tdscSendEmailList, String noticeId, String noticeType){
    	//先根据noticeId删除原有的EMAIL
    	if(noticeId!=null&&!"".equals(noticeId)){
    		List tdscSendEmailOldList = this.querySendEmailListByType(noticeId,noticeType);
    		
	    	if(tdscSendEmailOldList!=null&&tdscSendEmailOldList.size()>0){	
	    	for(int j=0;j<tdscSendEmailOldList.size();j++){
		    	delete(tdscSendEmailOldList.get(j));
		    }
	    	}
    	}
    	
    	//保存新的email列表
		if(tdscSendEmailList!=null&&tdscSendEmailList.size()>0){
			for(int i=0;i<tdscSendEmailList.size();i++){
				TdscSendEmailInfo tdscSendEmailInfo = (TdscSendEmailInfo)tdscSendEmailList.get(i);
				save(tdscSendEmailInfo);
			}
		}
    }
    
    
}
