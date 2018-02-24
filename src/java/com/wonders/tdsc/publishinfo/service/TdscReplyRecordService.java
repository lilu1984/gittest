package com.wonders.tdsc.publishinfo.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.model.Pager;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscReplyConfInfoCondition;
import com.wonders.tdsc.publishinfo.dao.TdscReplyRecordDao;
import com.wonders.tdsc.tdscbase.dao.CommonQueryDao;

public class TdscReplyRecordService extends BaseSpringManagerImpl {

    //private TdscNoticeAppDao tdscNoticeAppDao;
    
    private TdscReplyRecordDao tdscReplyRecordDao;
    
    private CommonQueryDao commonQueryDao;
   
    private TdscBlockTranAppDao tdscBlockTranAppDao;

    public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
        this.tdscBlockTranAppDao = tdscBlockTranAppDao;
    }
    
    public void setCommonQueryDao(CommonQueryDao commonQueryDao) {
        this.commonQueryDao = commonQueryDao;
    }  
    
    public void setTdscReplyRecordDao(TdscReplyRecordDao tdscReplyRecordDao) {
        this.tdscReplyRecordDao = tdscReplyRecordDao;
    }
    
    /**����������ѯ
     * 
     * @param condition
     * @return
     */
    public PageList findPageList(TdscReplyConfInfoCondition condition)
    {
        PageList pagelist = (PageList)tdscReplyRecordDao.findPageList(condition);
        PageList retpagelist = new PageList();
        if(pagelist!=null){
            List tempList = new ArrayList();
            List returnList = new ArrayList();
            tempList = pagelist.getList();
            for(int i=0;i<tempList.size();i++){
                TdscReplyConfInfo tdscReplyConfInfo = (TdscReplyConfInfo)tempList.get(i);
                TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)tdscBlockTranAppDao.findTdscBlockTranApp(tdscReplyConfInfo.getAppId());
                if(tdscBlockTranApp!=null){
                    TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
                    tdscBaseQueryCondition.setAppId(tdscReplyConfInfo.getAppId());
                    TdscBlockAppView tdscBlockAppView = (TdscBlockAppView)commonQueryDao.getTdscBlockAppView(tdscBaseQueryCondition);
                    
                    if(tdscBlockAppView!=null&&tdscBlockAppView.getNodeId()!=null){
                    	if(Integer.parseInt(tdscBlockAppView.getNodeId())>=9){
	                        //1.���÷���ʵ��� �ؿ�����
	                        if(tdscBlockAppView.getBlockName()!=null){
	                            tdscReplyConfInfo.setBlockName(tdscBlockAppView.getBlockName());
	                        }
	                        //2.���÷���ʵ��� ���÷�ʽ
	                        if(tdscBlockAppView.getTransferMode()!=null){
	                            tdscReplyConfInfo.setTransferMode(tdscBlockAppView.getTransferMode());
	                        }
	                        //3.���÷���ʵ��� ��������
	                        if(tdscBlockAppView.getBlockType()!=null){
	                            tdscReplyConfInfo.setBlockType(tdscBlockAppView.getBlockType());
	                        }
	                        //4.���÷���ʵ��� �������� 
	                        if(tdscBlockAppView.getEndorseDistrict()!=null){
	                            tdscReplyConfInfo.setEndorseDistrict(tdscBlockAppView.getEndorseDistrict());
	                        }
	                        returnList.add(tdscReplyConfInfo);
                    	}
                    }
                }
                //returnList.add(tdscReplyConfInfo);
            }
            Pager pager=new Pager(returnList.size(),condition.getPageSize(),condition.getCurrentPage());
            retpagelist.setPager(pager);
            retpagelist.setList(returnList);
            retpagelist.setPager(pagelist.getPager());
        }
        return retpagelist;
    }
    /**�޸�ʵ��� ����״̬����¼����ʱ��
     * 
     * @param appId
     */
    public void modTdscReplyConfInfo(String appId){
        //��ѯʵ��
        TdscReplyConfInfo tdscReplyConfInfo = (TdscReplyConfInfo)tdscReplyRecordDao.get(appId);
        //�޸ķ���״̬        
        tdscReplyConfInfo.setIfPublish("1");
        //��¼����ʱ��
        Date publishDate = new Date(System.currentTimeMillis());
        tdscReplyConfInfo.setPublishDate(publishDate);
        tdscReplyRecordDao.update(tdscReplyConfInfo);
    }
    
    
    
    public PageList findPageListByCondition(TdscReplyConfInfoCondition condition)
    {
        //���ݵؿ����� ��ѯ�����б�LIST
        TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
        tdscBaseQueryCondition.setBlockName(condition.getBlockName());
        List tdscBlockAppViewList = (List)commonQueryDao.findTdscBlockAppViewListWithoutNodeId(tdscBaseQueryCondition);
        if(tdscBlockAppViewList!=null){
            //List tdscBlockAppViewList = pageBlockAppViewList.getList();
            
            List retList = new ArrayList();
            if(tdscBlockAppViewList!=null&&tdscBlockAppViewList.size()>0){
                for(int i=0;i<tdscBlockAppViewList.size();i++){
                    TdscBlockAppView tdscBlockAppView = (TdscBlockAppView)tdscBlockAppViewList.get(i);
                    TdscReplyConfInfo  tdscReplyConfInfo = (TdscReplyConfInfo)tdscReplyRecordDao.get(tdscBlockAppView.getAppId());
                    if(tdscReplyConfInfo!=null&&tdscBlockAppView.getNodeId()!=null){
                    	if(Integer.parseInt(tdscBlockAppView.getNodeId())>=9){
                            //1.���÷���ʵ��� �ؿ�����
                            if(tdscBlockAppView.getBlockName()!=null){
                                tdscReplyConfInfo.setBlockName(tdscBlockAppView.getBlockName());
                            }
                            //2.���÷���ʵ��� ���÷�ʽ
                            if(tdscBlockAppView.getTransferMode()!=null){
                                tdscReplyConfInfo.setTransferMode(tdscBlockAppView.getTransferMode());
                            }
                            //3.���÷���ʵ��� ��������
                            if(tdscBlockAppView.getBlockType()!=null){
                                tdscReplyConfInfo.setBlockType(tdscBlockAppView.getBlockType());
                            }
                            //4.���÷���ʵ��� ��������
                            if(tdscBlockAppView.getEndorseDistrict()!=null){
                                tdscReplyConfInfo.setEndorseDistrict(tdscBlockAppView.getEndorseDistrict());
                            }
                            if (StringUtils.isNotEmpty(condition.getIfPublish())) {
                            	if(condition.getIfPublish().equals(tdscReplyConfInfo.getIfPublish())){
                                    retList.add(tdscReplyConfInfo);
                                }else if("0".equals(condition.getIfPublish())&&tdscReplyConfInfo.getIfPublish()==null){
                                	retList.add(tdscReplyConfInfo);
                                }
                            }else{
                                retList.add(tdscReplyConfInfo);
                            }  
                    	}
                    }
                }    
            }
            if(retList!=null&&retList.size()>0){
                PageList pageList = new PageList();
                //����ѯ������·�װ��PageList
                Pager pager=new Pager(retList.size(),condition.getPageSize(),condition.getCurrentPage());
                pageList.setPager(pager);
                List tempList=new ArrayList();
                int currentPage=condition.getCurrentPage();
                if(retList!=null&&retList.size()>0){
                    for(int i=0;i<retList.size();i++){
                        if(retList.size()>((currentPage)*10+i)) tempList.add(retList.get((currentPage)*10+i));
                        if(tempList.size()==condition.getPageSize()) break;
                    }
                }
                pageList.setList(tempList);
                return pageList;
            } 
        }

       
        return null;
    }
    
    
}
