package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.util.List;

public class Capsule implements Serializable {

	private static final long serialVersionUID = 1L;

	public Capsule() {
	}

	public List list;
	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 土地位置	LAND_LOCATION */
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
