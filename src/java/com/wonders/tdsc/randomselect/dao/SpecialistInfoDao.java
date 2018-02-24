package com.wonders.tdsc.randomselect.dao;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscSpecialistInfo;



public class SpecialistInfoDao extends BaseHibernateDaoImpl{


    public Class getEntityClass() {
        return TdscSpecialistInfo.class;
    }
    
    public List selectSpecialist (Timestamp nowTime) {
		StringBuffer hql = new StringBuffer("from TdscSpecialistInfo b " +
												"where b.specialistId not in " +
												"(select a.selectedId from TdscBlockSelectApp a " +
												"where a.selectDate =?)");		
		List paramList = new ArrayList();
		paramList.add(nowTime);
		
		List list = findList(hql.toString(),paramList);		
		
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
		
	}
    
    public List selectSpecialist(String specialistType){
    	StringBuffer hql = new StringBuffer("from TdscSpecialistInfo a where a.specialistType ='").append(specialistType).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
    }

}
