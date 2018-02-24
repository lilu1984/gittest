package com.wonders.common.simplequery.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class QueryColumn implements Serializable {

    /** identifier field */
    private String colId;

    /** nullable persistent field */
    private String themeId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String nameLetter;

    /** nullable persistent field */
    private String editType;

    /** nullable persistent field */
    private Integer dicId;

    /** nullable persistent field */
    private String columnType;

    /** nullable persistent field */
    private String drillLink;

    /** nullable persistent field */
    private String validity;

    /** full constructor */
    public QueryColumn(String colId, String themeId, String name, String nameLetter, String editType, Integer dicId, String columnType, String drillLink, String validity) {
        this.colId = colId;
        this.themeId = themeId;
        this.name = name;
        this.nameLetter = nameLetter;
        this.editType = editType;
        this.dicId = dicId;
        this.columnType = columnType;
        this.drillLink = drillLink;
        this.validity = validity;
    }

    /** default constructor */
    public QueryColumn() {
    }

    /** minimal constructor */
    public QueryColumn(String colId) {
        this.colId = colId;
    }

    public String getColId() {
        return this.colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getThemeId() {
        return this.themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLetter() {
        return this.nameLetter;
    }

    public void setNameLetter(String nameLetter) {
        this.nameLetter = nameLetter;
    }

    public String getEditType() {
        return this.editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public Integer getDicId() {
        return this.dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public String getColumnType() {
        return this.columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDrillLink() {
        return this.drillLink;
    }

    public void setDrillLink(String drillLink) {
        this.drillLink = drillLink;
    }

    public String getValidity() {
        return this.validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("colId", getColId())
            .toString();
    }

}
