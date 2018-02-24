package com.wonders.tdsc.lob.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.lob.bo.TdscEsClob;

public class TdscEsClobDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscEsClob.class;
    }
   
}
