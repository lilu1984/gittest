package com.wonders.common.simplequery.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class QueryTheme implements Serializable {

    /** identifier field */
    private String themeId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String viewName;

    /** nullable persistent field */
    private String validity;

    /** full constructor */
    public QueryTheme(String themeId, String name, String viewName, String validity) {
        this.themeId = themeId;
        this.name = name;
        this.viewName = viewName;
        this.validity = validity;
    }

    /** default constructor */
    public QueryTheme() {
    }

    /** minimal constructor */
    public QueryTheme(String themeId) {
        this.themeId = themeId;
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

    public String getViewName() {
        return this.viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getValidity() {
        return this.validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("themeId", getThemeId())
            .toString();
    }

}
