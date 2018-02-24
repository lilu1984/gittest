package com.wonders.tdsc.publishinfo.service;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.publishinfo.dao.TdscReplyRecordDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscTransResultService extends BaseSpringManagerImpl {

    //private TdscNoticeAppDao tdscNoticeAppDao;
    
    private TdscReplyRecordDao tdscReplyConfInfoDao;
    
    private CommonQueryService commonQueryService;

    private TdscBlockTranAppDao tdscBlockTranAppDao;

    public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
        this.tdscBlockTranAppDao = tdscBlockTranAppDao;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setTdscReplyConfInfoDao(TdscReplyRecordDao tdscReplyConfInfoDao) {
        this.tdscReplyConfInfoDao = tdscReplyConfInfoDao;
    }

}
