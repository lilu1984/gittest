package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockFileApp implements Serializable {

    /** identifier field */
    private String fileId;

    /** nullable persistent field */
    private Date fileDate;

    /** nullable persistent field */
    private String filePerson;

    /** nullable persistent field */
    private String fileUrl;
    
    /** nullable persistent field */
    private String recordId;

    /** full constructor */
    public TdscBlockFileApp(String fileId, Date fileDate, String filePerson, String fileUrl,String recordId) {
        this.fileId = fileId;
        this.fileDate = fileDate;
        this.filePerson = filePerson;
        this.fileUrl = fileUrl;
        this.recordId = recordId;
    }

    /** default constructor */
    public TdscBlockFileApp() {
    }

    /** minimal constructor */
    public TdscBlockFileApp(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Date getFileDate() {
        return this.fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getFilePerson() {
        return this.filePerson;
    }

    public void setFilePerson(String filePerson) {
        this.filePerson = filePerson;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fileId", getFileId())
            .toString();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

}
