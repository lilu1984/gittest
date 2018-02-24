package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.util.List;

public class Capsule implements Serializable {

	private static final long serialVersionUID = 1L;

	public Capsule() {
	}

	public List list;
	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** ����λ��	LAND_LOCATION */
    private String landLocation;
    
    
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getProjectGuid() {
		return projectGuid;
	}

	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

}
