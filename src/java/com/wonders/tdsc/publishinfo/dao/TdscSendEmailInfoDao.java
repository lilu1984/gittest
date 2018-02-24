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
     * �����ļ�ID��noticeId����ѯList
     * @param noticeId
     * @return
     */
    public List querySendEmailList(String noticeId){
		StringBuffer hql = new StringBuffer("from TdscSendEmailInfo a where a.noticeId = '").append(noticeId).append("' order by a.emailId desc");

		return findList(hql.toString());
    }
    
    /**
     * �����ļ�ID��noticeId���͹�������NOTICE_TYPE��ѯList
     * @param noticeId��NOTICE_TYPE
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
     * �ʼ�����  ����noticeId��ѯ������ڣ�ȫɾ����д�롣
     * @param tdscSendEmailList
     * @return
     */
    public void saveSendEmailList(List tdscSendEmailList, String noticeId, String noticeType){
    	//�ȸ���noticeIdɾ��ԭ�е�EMAIL
    	if(noticeId!=null&&!"".equals(noticeId)){
    		List tdscSendEmailOldList = this.querySendEmailListByType(noticeId,noticeType);
    		
	    	if(tdscSendEmailOldList!=null&&tdscSendEmailOldList.size()>0){	
	    	for(int j=0;j<tdscSendEmailOldList.size();j++){
		    	delete(tdscSendEmailOldList.get(j));
		    }
	    	}
    	}
    	
    	//�����µ�email�б�
		if(tdscSendEmailList!=null&&tdscSendEmailList.size()>0){
			for(int i=0;i<tdscSendEmailList.size();i++){
				TdscSendEmailInfo tdscSendEmailInfo = (TdscSendEmailInfo)tdscSendEmailList.get(i);
				save(tdscSendEmailInfo);
			}
		}
    }
    
    
}
