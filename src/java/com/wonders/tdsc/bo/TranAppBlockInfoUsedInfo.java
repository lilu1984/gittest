package com.wonders.tdsc.bo;

import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;

public class TranAppBlockInfoUsedInfo extends BaseBO{

	//���ؽ�����Ϣ��
	private TdscBlockTranApp tdscBlockTranApp;

	//���ػ�����Ϣ��
	private TdscBlockInfo tdscBlockInfo;
	
	//���Ȱ��ű�
	private TdscBlockPlanTable tdscBlockPlanTable;
	
	//������;��
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
