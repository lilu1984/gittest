package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;


/**
 * FileRef entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FileRef implements Serializable {
	
	// Fields
	private String fileId;
	private String busId;
	private String fileBinId;
	private String fileType;
	private String fileCatalog;
	private String filePath;
	private String fileName;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public FileRef() {
	}

	/** full constructor */
	public FileRef(String busId, String fileBinId, String fileType,
			String filePath, String fileName) {
		this.busId = busId;
		this.fileBinId = fileBinId;
		this.fileType = fileType;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	// Property accessors

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getBusId() {
		return this.busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getFileBinId() {
		return this.fileBinId;
	}

	public void setFileBinId(String fileBinId) {
		this.fileBinId = fileBinId;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFileCatalog() {
		return fileCatalog;
	}

	public void setFileCatalog(String fileCatalog) {
		this.fileCatalog = fileCatalog;
	}
}