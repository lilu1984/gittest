package com.wonders.tdsc.jzdj.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.protocol.impl.HttpClientRemoteFactory;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.ConvertImageUtil;
import com.wonders.tdsc.presell.dao.FileRefDao;
import com.wonders.util.PropertiesUtil;

public class JzdjService extends BaseSpringManagerImpl{
	private FileRefDao fileRefDao;
	private String PATH = PropertiesUtil.getInstance().getProperty("file.save.path");
	private String HTTPURL = PropertiesUtil.getInstance().getProperty("httpclient_fileuploadfile_url");

	public FileRefDao getFileRefDao() {
		return fileRefDao;
	}

	public void setFileRefDao(FileRefDao fileRefDao) {
		this.fileRefDao = fileRefDao;
	}
	
	public List findJzdjFile(){
		List list = this.fileRefDao.findListFileRefByCondition("", GlobalConstants.JZDJ_FILE);
		return list;
	}
	public void delFileRefById(String fileId) {
		if (StringUtils.isNotEmpty(fileId)) {
			fileRefDao.deleteById(fileId);
		}
	}
	/**
	 * 保存上传的基准地价文件(并且生成缩略图)
	 * @param appId
	 * @param bytes
	 * @param fileName
	 * @param fileType
	 * @param catalog
	 * @return
	 * @throws Exception
	 */
	public FileRef uploadFile(byte[] bytes, String fileName,String fileType, String catalog) throws Exception {
		FileRef fileRef = new FileRef();
		fileRef.setFileId(null);
		fileRef.setFileCatalog(catalog);
		fileRef.setFileName(fileName);
		fileRef.setFileType(fileType);
		fileRef.setUpdateTime(new Date());
		fileRef = (FileRef)fileRefDao.saveOrUpdate(fileRef);
		String filePath = PATH + File.separator + fileRef.getFileId() + "." + fileType;
		//生成缩略图的路径
		String s_filePath = PATH + File.separator + "s_" + fileRef.getFileId() + "." + fileType;
		File file = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		ConvertImageUtil.convert(filePath, s_filePath);
		HttpClientRemoteFactory.getInstance().sendRemote(filePath, HTTPURL);
		HttpClientRemoteFactory.getInstance().sendRemote(s_filePath, HTTPURL);
		//生成缩略图
		return fileRef;
	}
}
