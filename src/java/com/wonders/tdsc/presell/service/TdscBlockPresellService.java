package com.wonders.tdsc.presell.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.wonders.common.protocol.impl.HttpClientRemoteFactory;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBlockPresell;
import com.wonders.tdsc.bo.condition.TdscBlockPresellCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.presell.dao.FileRefDao;
import com.wonders.tdsc.presell.dao.TdscBlockPresellDao;
import com.wonders.util.PropertiesUtil;

public class TdscBlockPresellService extends BaseSpringManagerImpl {

	private static Logger logger = Logger.getLogger(TdscBlockPresellService.class);
	
	private String PATH = PropertiesUtil.getInstance().getProperty("file.save.path");
	
	private String HTTPURL = PropertiesUtil.getInstance().getProperty("httpclient_fileuploadfile_url");

	private TdscBlockPresellDao tdscBlockPresellDao;
	
	private FileRefDao fileRefDao;
	
	public void setFileRefDao(FileRefDao fileRefDao) {
		this.fileRefDao = fileRefDao;
	}

	public void setTdscBlockPresellDao(TdscBlockPresellDao tdscBlockPresellDao) {
		this.tdscBlockPresellDao = tdscBlockPresellDao;
	}
	public void saveBlockPresell(TdscBlockPresell tdscBlockPresell,List fileList,String[] fileNameList,FormFile nominateImageFile) throws Exception{
		
		String appId = tdscBlockPresell.getPresellId();
		String nominateImageFileName = null;
		byte[] nominateImageBbytes = null;
		if (nominateImageFile != null) {
			nominateImageFileName = nominateImageFile.getFileName();
			nominateImageBbytes = nominateImageFile.getFileData();
		}
		String fileType = "";
		if(fileList!=null&&fileList.size()>0){
			for(int i=0;i<fileList.size();i++){
				FormFile file = (FormFile)fileList.get(i);
				fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
				String fileName = fileNameList[i];
				byte[] bytes = file.getFileData();
				uploadFile(appId, bytes, fileName,fileType, GlobalConstants.PRESELL_FILE);
			}
		}
		if (StringUtils.isEmpty(tdscBlockPresell.getAvailable())) {
			tdscBlockPresell.setAvailable("0");
		}
		if (StringUtils.isNotBlank(tdscBlockPresell.getPresellId())) {
			tdscBlockPresellDao.update(tdscBlockPresell);
		} else {
			tdscBlockPresell.setCreateTime(new Date());
			tdscBlockPresellDao.save(tdscBlockPresell);
		}
		if (StringUtils.isNotEmpty(nominateImageFileName) && nominateImageBbytes != null) {
			fileType = nominateImageFileName.substring(nominateImageFileName.lastIndexOf(".")+1); 
			FileRef fileRef = uploadFile(appId, nominateImageBbytes, nominateImageFileName,fileType, GlobalConstants.PRESELL_TYPE_TJ);
			tdscBlockPresell.setNominateImageId(fileRef.getFileId());
		}
		
	}

	public PageList findPageList(TdscBlockPresellCondition condition) {
		return tdscBlockPresellDao.findPageList(condition);
	}

	public TdscBlockPresell getBlockPresellById(String presellId) {
		return (TdscBlockPresell) tdscBlockPresellDao.get(presellId);
	}

	public void delPresellById(String presellId) {
		TdscBlockPresell tdscBlockPresell = getBlockPresellById(presellId);
		if (StringUtils.isEmpty(presellId)) return;
			tdscBlockPresellDao.deleteById(presellId);
		FileRef tj = getFileRefById(tdscBlockPresell.getNominateImageId());
		if (tj != null)
			fileRefDao.delete(tj);
		List list = fileRefDao.findListFileRefByCondition(presellId, null);
		for (int i = 0 ; i < list.size(); i++) {
			FileRef fileRef = (FileRef)list.get(i);
			fileRefDao.delete(fileRef);
		}
	}

	public FileRef uploadFile(String appId, byte[] bytes, String fileName,String fileType, String catalog) throws Exception {
		FileRef fileRef = new FileRef();
		fileRef.setBusId(appId);
		fileRef.setFileId(null);
		fileRef.setFileCatalog(catalog);
		fileRef.setFileName(fileName);
		fileRef.setFileType(fileType);
		fileRef = (FileRef)fileRefDao.saveOrUpdate(fileRef);
		String filePath = PATH + File.separator + fileRef.getFileId() + "." + fileType;
		File file = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		HttpClientRemoteFactory.getInstance().sendRemote(filePath, HTTPURL);
		return fileRef;
	}

	/**
	 *  根据业务编号取得对应的规划图附件
	 * @param presellId
	 * @return
	 */
	public FileRef getFileRefByBusId(String presellId, String catalog) {
		if (StringUtils.isNotEmpty(presellId)) {
			List list = fileRefDao.findListFileRefByCondition(presellId, catalog);
			if (list != null && list.size() == 1) 
				return (FileRef)list.get(0);
			return null;
		}
		return null;
	}
	/**
	 * 根据业务ID和业务类型获取文件数据列表
	 * @param presellId
	 * @param catalog
	 * @return
	 */
	public List findFileRefByBusId(String presellId, String catalog){
		if (StringUtils.isNotEmpty(presellId)) {
			List list = fileRefDao.findListFileRefByCondition(presellId, catalog);
			return list;
		}
		return null;
		
	}
	public void delFileRefById(String fileId) {
		if (StringUtils.isNotEmpty(fileId)) {
			fileRefDao.deleteById(fileId);
		}
	}

	public FileRef getFileRefById(String nominateImageId) {
		if(nominateImageId!=null){
			return (FileRef)fileRefDao.get(nominateImageId);
		}else{
			return null;
		}
	}

	public void fabu(List list) {
		if (list == null || list.size() < 1) return;
		List result = tdscBlockPresellDao.findByIds(list);
		for (int i = 0 ; null != result && i < result.size(); i++) {
			TdscBlockPresell blockPresell = (TdscBlockPresell)result.get(i);
			blockPresell.setAvailable("1");
		}
	}
}
