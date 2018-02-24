package com.wonders.tdsc.user.model;

import com.wonders.esframework.common.model.BaseModel;

/**
 * 资源对象
 */
public class AuthorityResource extends BaseModel {

	/** 编码 */
	private String resourceId;

	/** 名称 */
	private String resourceName;

	/** 级别 */
	private Integer resourceLevel;

	/** 父编码 */
	private String parentId;

	/** 排序编码 */
	private Integer sortOrder;

	/** 类别 */
	private String type;
	
	/** 实际的编码 */
	private String value;
	
    /** 对应URL */
    private String resourceUrl;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(Integer resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
