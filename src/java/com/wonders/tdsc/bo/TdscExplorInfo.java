package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscExplorInfo implements Serializable {

    /** identifier field */
    private String explorId;

    /** nullable persistent field */
    private BigDecimal attendNum;

    /** nullable persistent field */
    private String explorStat;

    /** nullable persistent field */
    private String staff;
    
    /** identifier field */
    private String recordId;
    
    /** identifier field */
    private String fileName;

    /** full constructor */
    public TdscExplorInfo(String explorId, BigDecimal attendNum, String explorStat, String staff, String recordId,String fileName) {
        this.explorId = explorId;
        this.attendNum = attendNum;
        this.explorStat = explorStat;
        this.staff = staff;
        this.recordId = recordId;
        this.fileName = fileName;
    }

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** default constructor */
    public TdscExplorInfo() {
    }

    /** minimal constructor */
    public TdscExplorInfo(String explorId) {
        this.explorId = explorId;
    }

    public String getExplorId() {
        return this.explorId;
    }

    public void setExplorId(String explorId) {
        this.explorId = explorId;
    }

    public BigDecimal getAttendNum() {
        return this.attendNum;
    }

    public void setAttendNum(BigDecimal attendNum) {
        this.attendNum = attendNum;
    }

    public String getExplorStat() {
        return this.explorStat;
    }

    public void setExplorStat(String explorStat) {
        this.explorStat = explorStat;
    }

    public String getStaff() {
        return this.staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("explorId", getExplorId())
            .toString();
    }

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

}
