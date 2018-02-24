package com.wonders.tdsc.user.model;

import com.wonders.esframework.common.model.BaseModel;

/**
 * ��Դ����
 */
public class AuthorityResource extends BaseModel {

	/** ���� */
	private String resourceId;

	/** ���� */
	private String resourceName;

	/** ���� */
	private Integer resourceLevel;

	/** ������ */
	private String parentId;

	/** ������� */
	private Integer sortOrder;

	/** ��� */
	private String type;
	
	/** ʵ�ʵı��� */
	private String value;
	
    /** ��ӦURL */
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
