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
     * 通过appId查询答疑会信息表信息
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
     * 上传答疑会信息文件
     * @param upLoadFile 文件名fileName
     * @return outFileName相对文件名
     * 20071122*
     */
    public String upLoad(FormFile upLoadFile,String fileName) {
        // 传入文件名
        String fromFileName = upLoadFile.getFileName();
        String outFileName = fileName+".doc";
        String filePath = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.UPLOAD_REPLY_FILE);
        
//      查看目录是否存在
        File directory = new File(filePath);
        if (!directory.exists())
            directory.mkdirs();

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
//          上传文件出错
            System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
        return (fromFileName);
    }
    

}
