package com.wonders.common.simplequery.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class QuerySave implements Serializable {

    /** identifier field */
    private String saveId;

    /** nullable persistent field */
    private String themeId;

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String conCols;

    /** nullable persistent field */
    private String rsCols;

    /** nullable persistent field */
    private String sortCols;
    /** nullable persistent field */
    private Date saveDate;

    /** full constructor */
    public QuerySave(String saveId, String themeId, String userId, String name, String conCols, String rsCols, String sortCols,Date saveDate) {
        this.saveId = saveId;
        this.themeId = themeId;
        this.userId = userId;
        this.name = name;
        this.conCols = conCols;
        this.rsCols = rsCols;
        this.sortCols = sortCols;
        this.saveDate = saveDate;
        
    }

    /** default constructor */
    public QuerySave() {
    }

    /** minimal constructor */
    public QuerySave(String saveId) {
        this.saveId = saveId;
    }

    public String getSaveId() {
        return this.saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public String getThemeId() {
        return this.themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConCols() {
        return this.conCols;
    }

    public void setConCols(String conCols) {
        this.conCols = conCols;
    }

    public String getRsCols() {
        return this.rsCols;
    }

    public void setRsCols(String rsCols) {
        this.rsCols = rsCols;
    }

    public String getSortCols() {
        return this.sortCols;
    }

    public void setSortCols(String sortCols) {
        this.sortCols = sortCols;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("saveId", getSaveId())
            .toString();
    }

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

}
