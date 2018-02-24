package com.wonders.tdsc.blockwork.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscReplyConfInfoDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscReplyConfInfo.class;
    }
    
    /**
     * ͨ��appId��ѯ���ɻ���Ϣ����Ϣ
     * @param appId
     * @return 
     * 20071121*
     */
    public TdscReplyConfInfo findReplyConfInfo(String appId){
        
        StringBuffer hql = new StringBuffer("from TdscReplyConfInfo a where a.appId = '").append(appId).append("'");
        String queryHql = hql.toString() + " order by a.appId";
        List replyConfInfoList = this.findList(queryHql.toString());
        if(replyConfInfoList != null && replyConfInfoList.size() > 0){
            return (TdscReplyConfInfo) replyConfInfoList.get(0);
        }
        return null;
    }
    
    /**
     * �ϴ����ɻ���Ϣ�ļ�
     * @param upLoadFile �ļ���fileName
     * @return outFileName����ļ���
     * 20071122*
     */
    public String upLoad(FormFile upLoadFile,String fileName) {
        // �����ļ���
        String fromFileName = upLoadFile.getFileName();
        String outFileName = fileName+".doc";
        String filePath = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.UPLOAD_REPLY_FILE);
        
//      �鿴Ŀ¼�Ƿ����
        File directory = new File(filePath);
        if (!directory.exists())
            directory.mkdirs();

        try {
            File outFile = new File(
            // ����ļ��ķ�����·��
                    filePath + File.separator + outFileName);
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();

            byte[] fileByte = upLoadFile.getFileData();
            if (fileByte.length > 0) {
                // �ڷ���������ӦĿ¼�¸����ϴ����ļ�����
                OutputStream os = new FileOutputStream(outFile);
                os.write(fileByte);
                os.flush();
                os.close();
            }
        } catch (IOException e) {
//          �ϴ��ļ�����
            System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
        return (fromFileName);
    }
    

}
