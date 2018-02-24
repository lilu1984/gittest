package com.wonders.tdsc.exchange.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.exchange.bo.BASIC;
import com.wonders.tdsc.exchange.bo.BLOCK;
import com.wonders.tdsc.exchange.bo.CJGGZW;
import com.wonders.tdsc.exchange.bo.CJQRS;
import com.wonders.tdsc.exchange.bo.CRGG;
import com.wonders.tdsc.exchange.bo.CRGGZW;
import com.wonders.tdsc.exchange.bo.CRJG;
import com.wonders.tdsc.exchange.bo.CRJSJ;
import com.wonders.tdsc.exchange.bo.CRWJZW;
import com.wonders.tdsc.exchange.bo.Capsule;
import com.wonders.tdsc.exchange.bo.Document;
import com.wonders.tdsc.exchange.bo.HTZX;
import com.wonders.tdsc.exchange.bo.JMRZG;
import com.wonders.tdsc.exchange.bo.NODE;
import com.wonders.tdsc.exchange.bo.SSFA;
import com.wonders.tdsc.exchange.bo.WJBZ;
import com.wonders.tdsc.exchange.bo.ZPGQQ;
import com.wonders.tdsc.exchange.bo.ZPGXC;
import com.wonders.tdsc.exchange.castor.CastorFactory;
import com.wonders.util.PropertiesUtil;

public class JianChaService {
    private static final Logger logger = Logger.getLogger(JianChaService.class);
    
    private static final String htmlSavePath=PropertiesUtil.getInstance().getProperty("html.save.path");
    private static final String appPath=PropertiesUtil.getInstance().getProperty("ws_mapping_file_path");
    private static final String saveXmlPath=PropertiesUtil.getInstance().getProperty("ws_save_xml_path");
    private static final String ifSaveXml=PropertiesUtil.getInstance().getProperty("if.save.xml");
    private static final String ifLoggerDebug=PropertiesUtil.getInstance().getProperty("if.logger.debug");
    
    
    /** ������Ŀ��Ϣ */
    public static final int WS_BLOCK = 1;
    /** ����������Ϣ */
    public static final int WS_BASIC = 2;
    /** ���Ĺ�ǰ�ڼ������� */
    public static final int WS_ZPGQQ = 3;
    /** ��֯ʵʩ������� */
    public static final int WS_SSFA = 4;
    /** ���ù����� */
    public static final int WS_CRGG = 5;
    /** �����ļ������뷢�ۼ�� */
    public static final int WS_WJBZ = 6;
    /** �������ʸ������ */
    public static final int WS_JMRZG = 7;
    /** ���Ĺ��ֳ���� */
    public static final int WS_ZPGXC = 8;
    /** �ɽ�ȷ������ */
    public static final int WS_CJQRS = 9;
    /** ���ý�������� */
    public static final int WS_CRJG = 10;
    /** ��ִͬ�м�� */
    public static final int WS_HTZX = 11;
    /** ���س��ý��սɼ�� */
    public static final int WS_CRJSJ = 12;
    /** ���ù����� */
    public static final int WS_CRGGZW = 13;
    /** �����ļ���� */
    public static final int WS_CRWJZW = 14;
    /** �ɽ������� */
    public static final int WS_CJGGZW = 15;   
    /** �ؿ鵱ǰ�ڵ� */
    public static final int WS_NODE = 16;
    
    /**
     * ��ȡ��ѯ���ַ���
     * @param type ����
     * @return ���ض�Ӧ����ַ���
     */
    private String getQueryColString(int type){
    	if(WS_BLOCK ==type){
    		return "GUID,BLOCK_ID,BLOCK_NAME";
    	}else if(WS_BASIC ==type){
    		return "distinct block_id,guid,project_guid,action_date_block,land_location";
    	}else if(WS_ZPGQQ ==type){
    		return "AUDITED_NUM,GUID,PROJECT_GUID,BLOCK_ID,IF_FARMLAND,IF_FARM_TO_USED,GONGDI_TYPE,GONGDI_NUM,IF_HAS_PROCEDURES,IF_GRANT_CRFA,IF_GROUP_ASSESS,PG_ORG_NAME";
    	}else if(WS_SSFA ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,MARGIN_AMOUNT,INIT_PRICE,ISSUE_START_DATE,GET_FILE_START_DATE,GET_FILE_END_DATE,ACC_APP_STAT_DATE,ACC_APP_END_DATE,MARGIN_END_DATE,INSP_DATE,INSP_LOC,AUCTION_DATE,AUCTION_LOC,LIST_START_DATE,LIST_END_DATE,SCE_BID_DATE,SCE_BID_LOC,PURVEYOR,BZ_XBJG";
    	}else if(WS_CRGG ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOITCE_NO,MARGIN_AMOUNT,INIT_PRICE,ISSUE_START_DATE,GET_FILE_START_DATE,GET_FILE_END_DATE,ACC_APP_STAT_DATE,ACC_APP_END_DATE,MARGIN_END_DATE,INSP_DATE,INSP_LOC,AUCTION_DATE,AUCTION_LOC,LIST_START_DATE,LIST_END_DATE,SCE_BID_DATE,SCE_BID_LOC,PURVEYOR,BZ_XBJG";
    	}else if(WS_WJBZ ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOITCE_NO,ISSUE_START_DATE,GET_FILE_START_DATE,GET_FILE_END_DATE";
    	}else if(WS_JMRZG ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID";
    	}else if(WS_ZPGXC ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOITCE_NO";
    	}else if(WS_CJQRS ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,RESULT_DATE,RESULT_NAME,RESULT_CERT,RESULT_PRICE,INIT_PRICE,REMISE_PERSON";
    	}else if(WS_CRJG ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOITCE_NO,RESULT_DATE,RESULT_NAME,RESULT_PRICE,TRANSFER_MODE";
    	}else if(WS_HTZX ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOITCE_NO";
    	}else if(WS_CRJSJ ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID";
    	}else if(WS_CRGGZW ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,FILE_RECORD_ID,NOTICE_ID";
    	}else if(WS_CRWJZW ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,NOTICE_RECORD_ID";
    	}else if(WS_CJGGZW ==type){
    		return "GUID,PROJECT_GUID,BLOCK_ID,RESULT_RECORD_ID";
    	}else if (WS_NODE == type) {
    	    return "APP_ID AS GUID, BLOCK_ID AS PROJECT_GUID, BLOCK_ID, STATUS";
    	}
		return null;
    	
    }

    /**
    * ��ȡ���е�������Ŀ��Ϣ
    * @return
    */
     public String  GetBlock()throws Exception{
     	List list = JianChaBaseService.getInstance().queryPlanResultAll(BLOCK.class.getName(),"*");
    	return creatDocument(list,"block.xml");
     }
     /**
      * ����BLOCK_ID��ȡ���е�������Ŀ��Ϣ
      * @param BLOCK_ID
      * @return
      * @throws Exception
      */
     public String GetBlock(String BLOCK_ID)throws Exception{
     	List list = JianChaBaseService.getInstance().queryPlanResultById(BLOCK.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"block.xml");
     }
     /**
      * ����ʱ��λ�ȡ������Ŀ��Ϣ
      */
     public String  GetBlock(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.BLOCK", getQueryColString(1), beginTime, endTime);
    	return creatDocument(list,"block.xml");
     }

    /**
     * ��ȡ����������Ϣ
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockBasic(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryBlockInfoById(Capsule.class.getName(),getQueryColString(2),BLOCK_ID);
    	return creatDocument(tidyObjList(list),"basic.xml");
    }
    
    /**
     * �������ر�Ż�ȡ������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockBasic() throws Exception{
    	List list = JianChaBaseService.getInstance().queryBolckInfoAll(Capsule.class.getName(),getQueryColString(2));
    	return creatDocument(tidyObjList(list),"basic.xml");
    }
    
    /**
     * ����ʱ������������Ϣ
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockBasic(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryBlockInfoByTime("com.wonders.tdsc.exchange.bo.Capsule", getQueryColString(2), beginTime, endTime);
		return creatDocument(tidyObjList(list),"basic.xml");
    }
    
    /**
     * 
     * @param basiclist
     * @return
     */
    public List tidyObjList(List objlist){
    	if(objlist != null && objlist.size() > 0){
    		List list = new ArrayList();
    		for(int i = 0; i < objlist.size(); i++){
    			Capsule capsule = (Capsule)objlist.get(i);
    			if( !"".equals(capsule.getBlockId()) && capsule.getBlockId() != null){
    				if(list != null ){
    					list = new ArrayList();
    				}
    				list = JianChaBaseService.getInstance().queryBlockInfoById(BASIC.class.getName(),"*",capsule.getBlockId());
    				if(list != null && list.size() > 0){
    					capsule.setList(list);
    				}
    			}
    		}
    	}
    	return objlist;
    }
    
    
    

    /**
     * �������ر�Ż�ȡ���Ĺ�ǰ������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockZPGQQ(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryQqjcInfoById(ZPGQQ.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"zpgqq.xml");
    }
    
    /**
     * ��ȡ�������Ĺ�ǰ�ڼ�������
     * @return
     * @throws Exception
     */
    public String GetBlockZPGQQ() throws Exception{
    	List list = JianChaBaseService.getInstance().queryQqjcInfoAll(ZPGQQ.class.getName(),"*");
    	return creatDocument(list,"zpgqq.xml");
    }
    
    /**
     * ����ʱ���ȡ���Ĺ�ǰ�ڼ�������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockZPGQQ(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryQqjcInfoByTime("com.wonders.tdsc.exchange.bo.ZPGQQ", getQueryColString(3), beginTime, endTime);
		return creatDocument(list,"zpgqq.xml");
    }
    

    /**
     * �������ر�Ż�ȡ��֯ʵʩ�������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockSSFA(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(SSFA.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"ssfa.xml");
    }
    
    /**
     * ��ȡ������֯ʵʩ�������
     * @return
     * @throws Exception
     */
    public String GetBlockSSFA() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(SSFA.class.getName(),"*");
    	return creatDocument(list,"ssfa.xml");
    }
    
    /**
     * ����ʱ���ȡ��֯ʵʩ�������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockSSFA(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.SSFA", getQueryColString(4), beginTime, endTime);
    	return creatDocument(list,"ssfa.xml");
    }
    

    /**
     * �������ر�Ż�ȡ���ù�����
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRGG(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(CRGG.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"crgg.xml");
    }
    
    /**
     * ��ȡ���г��ù�������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockCRGG() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(CRGG.class.getName(),"*");
    	return creatDocument(list,"crgg.xml");
    }
    
    /**
     * ����ʱ���ȡ���ù�����
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRGG(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.CRGG", getQueryColString(5), beginTime, endTime);
		return creatDocument(list,"crgg.xml");
    }

    /**
     * �������ر�Ż�ȡ�����ļ������뷢�ۼ����Ϣ
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockWJBZ(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(WJBZ.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"wjbz.xml");
    }
    
    /**
     * ��ȡ���г����ļ������뷢�ۼ����Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockWJBZ() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(WJBZ.class.getName(),"*");
    	return creatDocument(list,"wjbz.xml");
    }
    
    /**
     * ����ʱ���ȡ�����ļ������뷢�ۼ����Ϣ
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockWJBZ(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.WJBZ", getQueryColString(6), beginTime, endTime);
		return creatDocument(list,"wjbz.xml");
    }
    
    

    /**
     * �������ر�Ż�ȡ�������ʸ����������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockJMRZG(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(JMRZG.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"jmrzg.xml");
    }
    
    /**
     * ��ȡ���о������ʸ��������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockJMRZG() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(JMRZG.class.getName(),"*");
    	return creatDocument(list,"jmrzg.xml");
    }
    
    /**
     * ����ʱ���ȡ�������ʸ����������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockJMRZG(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.JMRZG", getQueryColString(7), beginTime, endTime);
		return creatDocument(list,"jmrzg.xml");
    }
    

    /**
     * �������ر�Ż�ȡ���Ĺ��ֳ��������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockZPGXC(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(ZPGXC.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"zpgxc.xml");
    }
    
    /**
     * ��ȡ�������Ĺ��ֳ������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockZPGXC() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(ZPGXC.class.getName(),"*");
    	return creatDocument(list,"zpgxc.xml");
    }
    
    /**
     * ����ʱ���ȡ���Ĺ��ֳ��������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockZPGXC(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.ZPGXC", getQueryColString(8), beginTime, endTime);
		return creatDocument(list,"zpgxc.xml");
    }
    

    /**
     * �������ر�Ż�ȡ�ɽ�ȷ����������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockCJQRS(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(CJQRS.class.getName(),getQueryColString(9),BLOCK_ID);
    	return creatDocument(list,"cjqrs.xml");
    }
    
    /**
     * ��ȡ���гɽ�ȷ��������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockCJQRS() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(CJQRS.class.getName(),getQueryColString(9));
    	if(list != null && list.size() > 0){
    		for(int i = 0; i< list.size(); i++){
    			CJQRS cjqrs = (CJQRS)list.get(i);
    			List blockPartList = (List)JianChaBaseService.getInstance().queryTdscBlockPartByBlockId(cjqrs.getBlockId());
    	    	if(blockPartList != null && blockPartList.size() > 0){
    	    		StringBuffer blockCode = new StringBuffer("");
    	    		for(int j = 0; j< blockPartList.size(); j++){
    	    			TdscBlockPart tdscBlockPart = (TdscBlockPart)blockPartList.get(j);
    	    			if(j < blockPartList.size()-1){
    	    				blockCode.append(tdscBlockPart.getBlockCode()).append("��");
    	    			}else{
    	    				blockCode.append(tdscBlockPart.getBlockCode());
    	    			}
    	    		}
    	    		cjqrs.setBlockCode(blockCode.toString());
    	    	}
    		}
    	}
    	return creatDocument(list,"cjqrs.xml");
    	
    }
    
    /**
     * ����ʱ���ȡ�ɽ�ȷ����������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockCJQRS(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.CJQRS", getQueryColString(9), beginTime, endTime);
		return creatDocument(list,"cjqrs.xml");
    }
    

    /**
     * �������ر�Ż�ȡ���ý������������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRJG(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(CRJG.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"crjg.xml");
    	
    }
    
    /**
     * ��ȡ���г��ý����������Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockCRJG() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(CRJG.class.getName(),"*");
    	return creatDocument(list,"crjg.xml");
    }
    
    /**
     * ����ʱ���ȡ���ý������������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRJG(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.CRJG", getQueryColString(10), beginTime, endTime);
		return creatDocument(list,"crjg.xml");
    }
    

    /**
     * �������ر�Ż�ȡ��ִͬ�м������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String  GetBlockHTZX(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(HTZX.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"htzx.xml");
    	
    }
    
    /**
     * ��ȡ���к�ִͬ�м����Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockHTZX() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(HTZX.class.getName(),"*");
    	return creatDocument(list,"htzx.xml");
    }
    
    /**
     * ����ʱ���ȡ��ִͬ�м������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockHTZX(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.HTZX", getQueryColString(11), beginTime, endTime);
		return creatDocument(list,"htzx.xml");
    }
    

    /**
     * �������ر�Ż�ȡ���س��ý��սɼ������
     * @param BLOCK_ID �ؿ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRJSJ(String BLOCK_ID)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultById(CRJSJ.class.getName(),"*",BLOCK_ID);
    	return creatDocument(list,"crjsj.xml");
    }
    
    /**
     * ��ȡ�������س��ý��սɼ����Ϣ
     * @return
     * @throws Exception
     */
    public String GetBlockCRJSJ() throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultAll(CRJSJ.class.getName(),"*");
    	return creatDocument(list,"crjsj.xml");
    }
    
    /**
     * ����ʱ���ȡ���س��ý��սɼ������
     * @param beginTime ��ʼʱ��
     * @param endTime ����ʱ��
     * @return
     * @throws Exception
     */
    public String GetBlockCRJSJ(String beginTime,String endTime)throws Exception{
    	List list = JianChaBaseService.getInstance().queryPlanResultByTime("com.wonders.tdsc.exchange.bo.CRJSJ", getQueryColString(12), beginTime, endTime);
		return creatDocument(list,"crjsj.xml");
    }
    /**
     * ���ķ���
     * 1.����XML�ļ����ݵ��ַ���
     * 2.�����õ�����XML�ļ�
     * 3.�����õ��ڿ���̨��ӡ����XML�ļ�����
     * @param list
     * @param mappingfileName
     * @return
     * @throws Exception
     */
    public String creatDocument(List list,String mappingfileName)throws Exception{
     	Document document = new Document();
    	document.setDataList(list);

    	String result = CastorFactory.marshalBean2Str(document, appPath+mappingfileName,"gb2312");
    	//����wonders_init.properties �е� ifSaveXml ��ʶ���ж��Ƿ�����XML�ļ�
    	if("1".equals(ifSaveXml)){
        	//�������
        	String className = "";
        	if(list != null && list.size() > 0){
        	    className = list.get(0).getClass().getName();
        	    className = className.substring(className.lastIndexOf(".")+1,className.length());
        	}
        	//ƴ��XML����·��
        	String xmlSP = saveXmlPath+"xmlbak\\" + className + ".xml";
        	File dir = new File(saveXmlPath+"xmlbak\\");
        	if(!dir.exists()){
        		dir.mkdir();
        	}
        	//�����ļ�
        	CastorFactory.marshalBean(document, appPath+mappingfileName, xmlSP);
    	}
    	if("1".equals(ifLoggerDebug)){
    		logger.debug(result);
    	}
		return result;
    }
 
	/**
	 * ��ȡ���г��ù���
	 * @return
	 */
	public String  GetBlockCRGGZW()throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeAll(CRGGZW.class.getName(),getQueryColString(13));
		convertCRGGZW2Xml(list);
		return creatDocument(list,"crggzw.xml");
	}
	  /**
	   * ���ݵؿ�ID��ȡ���ù���
	   * @param BLOCK_ID
	   * @return
	   * @throws Exception
	   */
	public String GetBlockCRGGZW(String BLOCK_ID)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeById(CRGGZW.class.getName(),getQueryColString(13),BLOCK_ID);
		convertCRGGZW2Xml(list);
		return creatDocument(list,"crggzw.xml");
	}
	  /**
	   * ����ʱ���ȡ���ù���
	   */
	public String  GetBlockCRGGZW(String beginTime,String endTime)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeByTime(CRGGZW.class.getName(), getQueryColString(13), beginTime, endTime,"FILE_DATE");
		convertCRGGZW2Xml(list);
		return creatDocument(list,"crggzw.xml");
	}
	/**
	 * ��ȡ���г����ļ�
	 * @return
	 */
	public String  GetBlockCRWJZW()throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeAll(CRWJZW.class.getName(),getQueryColString(14));
		convertCRWJZW2Xml(list);
		return creatDocument(list,"crwjzw.xml");
	}  
	  /**
	   * ���ݵؿ�ID��ȡ�����ļ�
	   * @param BLOCK_ID
	   * @return
	   * @throws Exception
	   */
	public String GetBlockCRWJZW(String BLOCK_ID)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeById(CRWJZW.class.getName(),getQueryColString(14),BLOCK_ID);
		convertCRWJZW2Xml(list);
		return creatDocument(list,"crwjzw.xml");
	}
	  /**
	   * ����ʱ���ȡ�����ļ�
	   */
	public String  GetBlockCRWJZW(String beginTime,String endTime)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeByTime(CRWJZW.class.getName(), getQueryColString(14), beginTime, endTime,"NOTICE_DATE");
		convertCRWJZW2Xml(list);
		return creatDocument(list,"crwjzw.xml");
	}
	
	/**
	 * ��ȡ���гɽ�����
	 * @return
	 */
	public String  GetBlockCJGGZW()throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeAll(CJGGZW.class.getName(),getQueryColString(15));
		convertCJGGZW2Xml(list);
		return creatDocument(list,"cjggzw.xml");
	}  
	  /**
	   * ���ݵؿ�ID��ȡ�ɽ�����
	   * @param BLOCK_ID
	   * @return
	   * @throws Exception
	   */
	public String GetBlockCJGGZW(String BLOCK_ID)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeById(CJGGZW.class.getName(),getQueryColString(15),BLOCK_ID);
		convertCJGGZW2Xml(list);
		return creatDocument(list,"cjggzw.xml");
	}
	  /**
	   * ����ʱ���ȡ�ɽ�����
	   */
	public String  GetBlockCJGGZW(String beginTime,String endTime)throws Exception{
		List list = JianChaBaseService.getInstance().queryFileNoticeByTime(CJGGZW.class.getName(), getQueryColString(14), beginTime, endTime,"RESULT_PUBLISH_DATE");
		convertCJGGZW2Xml(list);
		return creatDocument(list,"cjggzw.xml");
	}
	
    /**
     * ������ù���LIST�������ù���HTML����д�� bean (CRGGZW)
     * @param list
     */  
	private void convertCRGGZW2Xml(List list) {
		if (null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				CRGGZW crggzw = (CRGGZW) list.get(i);
				if (crggzw.getFileRecordId() != null && crggzw.getFileRecordId() != "") {
				//��ó��ù����HTML�ļ�����·��
				String htmlPath = htmlSavePath + crggzw.getFileRecordId() + ".htm";
				File file = new File(htmlPath);
				//�ļ����������ת��
					if(file.exists()){
						String xmlContant = JianChaBaseService.getInstance().convertWord2String(htmlPath);
						crggzw.setCrggzw(xmlContant);
					}
				}
			}
		}
	}
    /**
     * ��������ļ�LIST���������ļ�HTML����д�� bean (CRWJZW)
     * @param list
     */  
	private void convertCRWJZW2Xml(List list) {
		if (null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				CRWJZW crwjzw = (CRWJZW) list.get(i);
				if (crwjzw.getNoticeRecordId() != null && crwjzw.getNoticeRecordId() != "") {
				//��ó����ļ���HTML�ļ�����·��
				String htmlPath = htmlSavePath + crwjzw.getNoticeRecordId() + ".htm";
				File file = new File(htmlPath);
				//�ļ����������ת��
					if(file.exists()){
						String xmlContant = JianChaBaseService.getInstance().convertWord2String(htmlPath);
						crwjzw.setCrwjzw(xmlContant);
					}
				}
			}
		}
	}  
	
    /**
     * ����ɽ�����LIST�����ɽ�����HTML����д�� bean (CJGGZW)
     * @param list
     */  
	private void convertCJGGZW2Xml(List list) {
		if (null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				CJGGZW cjggzw = (CJGGZW) list.get(i);
				if (cjggzw.getResultRecordId() != null && cjggzw.getResultRecordId() != "") {
				//��óɽ������HTML�ļ�����·��
				String htmlPath = htmlSavePath + cjggzw.getResultRecordId() + ".htm";
				File file = new File(htmlPath);
				//�ļ����������ת��
					if(file.exists()){
						String xmlContant = JianChaBaseService.getInstance().convertWord2String(htmlPath);
						cjggzw.setCjggzw(xmlContant);
					}
				}
			}
		}
	}
    /**
     * ����ؿ鵱ǰ�ڵ�LIST�����ɽ�����HTML����д�� bean (NODE)
     * @param list
     */  
	public String GetBlockNode() throws Exception{
		List list = JianChaBaseService.getInstance().queryAppNodeAll(NODE.class.getName(), getQueryColString(16));
		tidyBlockNodeList(list);
		return creatDocument(list, "node.xml");
	}
    /**
     * ����ؿ鵱ǰ�ڵ�LIST�����ɽ�����HTML����д�� bean (NODE)
     * @param list
     */  
	public String GetBlockNode(String BLOCK_ID)throws Exception{
		List list = JianChaBaseService.getInstance().queryAppNodeById(NODE.class.getName(), getQueryColString(16), BLOCK_ID);
		tidyBlockNodeList(list);
		return creatDocument(list, "node.xml");
	}
    /**
     * ����ؿ鵱ǰ�ڵ�LIST�����ɽ�����HTML����д�� bean (NODE)
     * @param list
     */  
	public String GetBlockNode(String beginTime, String endTime)throws Exception{
		List list = JianChaBaseService.getInstance().queryAppNodeByTime(NODE.class.getName(), getQueryColString(16), beginTime, endTime);
		tidyBlockNodeList(list);
		return creatDocument(list, "node.xml");
	}
    /**
     * ����ؿ鵱ǰ�ڵ�LIST
     * @param list
     */  
	public void tidyBlockNodeList(List blockNodeList){
		if ((blockNodeList != null) && (blockNodeList.size() > 0)) {
			List nodeStatList = new ArrayList();
			TdscAppNodeStat app = new TdscAppNodeStat();
			StringBuffer strNode = null;
			for (int i = 0; i < blockNodeList.size(); ++i) {
			    NODE nodeApp = (NODE)blockNodeList.get(i);
			    if ("00".equals(nodeApp.getStatus())) {
			    	nodeApp.setStatus("00");
			    }else if ("01".equals(nodeApp.getStatus())){
			    	nodeStatList = JianChaBaseService.getInstance().queryNodeListByAppId(TdscAppNodeStat.class.getName(), nodeApp.getGuid());
				    if ((nodeStatList != null) && (nodeStatList.size() > 0)) {
					    strNode = new StringBuffer("");
					    for (int j = 0; j < nodeStatList.size(); ++j) {
						   app = (TdscAppNodeStat)nodeStatList.get(j);
						
						    if (j < nodeStatList.size() - 1){
						    	strNode.append(app.getNodeId()).append("|");
						    }else {
						        strNode.append(app.getNodeId());
						    }
						    nodeApp.setStatus(strNode.toString());
					    }
				    }
				}else {
				  nodeApp.setStatus("90");
				}
			}
		}
	}


    public static void main(String[] args) {
        try {
        	String BLOCK_ID = "4028874f24bdf3070124bdf48a260001"; 
        	String beginTime = "2009-11-04 14:20:20";
        	String endTime ="2009-11-04 15:00:00";
        	new JianChaService().GetBlockNode();
        	//new JianChaService().GetBlock();
        	//new JianChaService().GetBlock(BLOCK_ID);
        	//new JianChaService().GetBlock(beginTime, endTime);
        	
        	//new JianChaService().GetBlockBasic();
        	//new JianChaService().GetBlockBasic(BLOCK_ID);
        	//new JianChaService().GetBlockBasic(null, null);
        	
        	//new JianChaService().GetBlockCJQRS();
        	//new JianChaService().GetBlockCJQRS(BLOCK_ID);
        	//new JianChaService().GetBlockCJQRS(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCRGG();
        	//new JianChaService().GetBlockCRGG(BLOCK_ID);
        	//new JianChaService().GetBlockCRGG(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCRJG();
        	//new JianChaService().GetBlockCRJG(BLOCK_ID);
        	//new JianChaService().GetBlockCRJG(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCRJSJ();
        	//new JianChaService().GetBlockCRJSJ(BLOCK_ID);
        	//new JianChaService().GetBlockCRJSJ(beginTime, endTime);
        	
        	//new JianChaService().GetBlockHTZX();
        	//new JianChaService().GetBlockHTZX(BLOCK_ID);
        	//new JianChaService().GetBlockHTZX(beginTime, endTime);
        	
        	//new JianChaService().GetBlockJMRZG();
        	//new JianChaService().GetBlockJMRZG(BLOCK_ID);
        	//new JianChaService().GetBlockJMRZG(beginTime, endTime);
        	
        	//new JianChaService().GetBlockSSFA();
        	//new JianChaService().GetBlockSSFA(BLOCK_ID);
        	//new JianChaService().GetBlockSSFA(beginTime, endTime);
        	
        	//new JianChaService().GetBlockWJBZ();
        	//new JianChaService().GetBlockWJBZ(BLOCK_ID);
        	//new JianChaService().GetBlockWJBZ(beginTime, endTime);
        	
        	//new JianChaService().GetBlockZPGQQ();
        	//new JianChaService().GetBlockZPGQQ(BLOCK_ID);
        	//new JianChaService().GetBlockZPGQQ(beginTime, endTime);
        	
        	//new JianChaService().GetBlockZPGXC();
        	//new JianChaService().GetBlockZPGXC(BLOCK_ID);
        	//new JianChaService().GetBlockZPGXC(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCRGGZW();
        	//new JianChaService().GetBlockCRGGZW(BLOCK_ID);
        	//new JianChaService().GetBlockCRGGZW(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCRWJZW();
        	//new JianChaService().GetBlockCRWJZW(BLOCK_ID);
        	//new JianChaService().GetBlockCRWJZW(beginTime, endTime);
        	
        	//new JianChaService().GetBlockCJGGZW();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
