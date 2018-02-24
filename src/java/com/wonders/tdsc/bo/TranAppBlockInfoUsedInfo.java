package com.wonders.tdsc.bo;

import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;

public class TranAppBlockInfoUsedInfo extends BaseBO{

	//土地交易信息表
	private TdscBlockTranApp tdscBlockTranApp;

	//土地基本信息表
	private TdscBlockInfo tdscBlockInfo;
	
	//进度安排表
	private TdscBlockPlanTable tdscBlockPlanTable;
	
	//土地用途表
	private List tdscBlockUsedInfoList;
	
	
	public TdscBlockInfo getTdscBlockInfo() {
		return tdscBlockInfo;
	}

	public void setTdscBlockInfo(TdscBlockInfo tdscBlockInfo) {
		this.tdscBlockInfo = tdscBlockInfo;
	}

	public List getTdscBlockUsedInfoList() {
		return tdscBlockUsedInfoList;
	}

	public void setTdscBlockUsedInfoList(List tdscBlockUsedInfoList) {
		this.tdscBlockUsedInfoList = tdscBlockUsedInfoList;
	}

	public TdscBlockTranApp getTdscBlockTranApp() {
		return tdscBlockTranApp;
	}

	public void setTdscBlockTranApp(TdscBlockTranApp tdscBlockTranApp) {
		this.tdscBlockTranApp = tdscBlockTranApp;
	}

	public TdscBlockPlanTable getTdscBlockPlanTable() {
		return tdscBlockPlanTable;
	}

	public void setTdscBlockPlanTable(TdscBlockPlanTable tdscBlockPlanTable) {
		this.tdscBlockPlanTable = tdscBlockPlanTable;
	}
	
	
}
