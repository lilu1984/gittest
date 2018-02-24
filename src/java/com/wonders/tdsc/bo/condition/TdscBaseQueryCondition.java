package com.wonders.tdsc.bo.condition;

import java.util.Date;
import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBaseQueryCondition extends BaseCondition {

	//土地供地批文号
	private String auditedNum;
	// 地块交易信息表ID的LIST
	private List appIdList;

	// 地块交易信息表ID
	private String appId;

	// 地块交易信息表ID
	private String blockId;

	// 地块名称
	private String blockName;

	// 地块交易方式
	private String transferMode;
    
    //入室审核当前状态
    private String blockAuditStatus;
    
    //审批区县
    private String endorseDistrict;

	// 地块类型
	private String blockType;

	// 出让公告号
	private String noitceNo;

	// 地块公告号
	private String blockNoticeNo;

	// 进入页面入口
	private String enterWay;

	// 地块状态
	private String status;

	// 地块状态的List
	private List statusList;

	// 区县
	private String districtId;

	// 区县
    private List districtIdList;

	// 当前页
	private int currentPage;

	// 状态ID
	private String statusId;

	// 状态ID的List	
	private List statusIdList;
	
	//状态ID的List2
	
	private List statusIdList2;
	
	// 节点ID
	private String nodeId;

	// 节点ID的List
	private List nodeList;

	// 是否包含已结束的业务
    private String endlessTag;

	// 是否不包含附加查询条件
    private String plusConditionTag;

	// 流程查询条件的List
	private List flowConditonList;

	//系统用户
	private SysUser user;
	
    
    //登陆用户
    private String appPerson;


	// 公告ID
	private String noticeId;
    
    // 发布公告起始时间
    private Date issueStartDate;

	// 排序关键字
	private String orderKey;
	
	// 排序关键字类型 升序还是降序
	private String orderType;

	// 排序关键字
	private String tranResult;

	// 是否抽选主持人
    private String hasSelectCompere;

	// 是否抽选B类公证处
    private String hasSelectBNotary;

	// 是否抽选C类公证处
    private String hasSelectCNotary;

	// 是否抽选专家
    private String hasSelectSpecialist;

	// 抽选专家的公证处用户ID
    private String selectSpecialistNotaryID;

	// 是否发布交易结果
    private String ifPublish;

	// 进度安排表ID
    private String planId;
    
    //合同编号
    private String contractNum;
    
    //地块坐落
    private String landLocation;
    
    //受让人
    private String acceptPerson;
    
    //招拍挂编号
    private String tradeNum;
    
    private String noticeNo;
    
    //动作时间
    private Date actionDate;
    
    private String userId;
    
    private String ifOnLine;
    
    
    
	public String getIfOnLine() {
		return ifOnLine;
	}

	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public void setStatusIdList(List statusIdList) {
		this.statusIdList = statusIdList;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public List getAppIdList() {
		return appIdList;
	}

	public void setAppIdList(List appIdList) {
		this.appIdList = appIdList;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}


	public String getEnterWay() {
		return enterWay;
	}

	public void setEnterWay(String enterWay) {
		this.enterWay = enterWay;
	}

	public String getNoitceNo() {
		return noitceNo;
	}

	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public List getStatusList() {
        return statusList;
    }
    
    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }
    
    public List getStatusIdList() {
        return statusIdList;
    }
    
    public void setStatusIDList(List statusIdList) {
        this.statusIdList = statusIdList;
    }
    
	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

    public List getDistrictIdList() {
        return districtIdList;
    }
    
    public void setDistrictIdList(List districtIdList) {
        this.districtIdList = districtIdList;
    }
    
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

    public Date getIssueStartDate() {
        return issueStartDate;
    }

    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

	public List getNodeList() {
		return nodeList;
	}

	public void setNodeList(List nodeList) {
		this.nodeList = nodeList;
	}

    public List getFlowConditonList() {
        return flowConditonList;
    }
    
    public void setFlowConditonList(List flowConditonList) {
        this.flowConditonList = flowConditonList;
    }
    
    public String getOrderKey() {
        return orderKey;
    }
    
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

	public String getAuditedNum() {
		return auditedNum;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

    public String getTranResult() {
        return tranResult;
    }
    
    public void setTranResult(String tranResult) {
        this.tranResult = tranResult;
    }
    
    public String getHasSelectBNotary() {
        return hasSelectBNotary;
    }
    
    public void setHasSelectBNotary(String hasSelectBNotary) {
        this.hasSelectBNotary = hasSelectBNotary;
    }
    
    public String getHasSelectCNotary() {
        return hasSelectCNotary;
    }
    
    public void setHasSelectCNotary(String hasSelectCNotary) {
        this.hasSelectCNotary = hasSelectCNotary;
    }
    
    public String getHasSelectCompere() {
        return hasSelectCompere;
    }
    
    public void setHasSelectCompere(String hasSelectCompere) {
        this.hasSelectCompere = hasSelectCompere;
    }
    
    public String getHasSelectSpecialist() {
        return hasSelectSpecialist;
    }
    
    public void setHasSelectSpecialist(String hasSelectSpecialist) {
        this.hasSelectSpecialist = hasSelectSpecialist;
    }
    
    public String getSelectSpecialistNotaryID() {
        return selectSpecialistNotaryID;
    }
    
    public void setSelectSpecialistNotaryID(String selectSpecialistNotaryID) {
        this.selectSpecialistNotaryID = selectSpecialistNotaryID;
    }
    
    public String getEndlessTag() {
        return endlessTag;
    }
    
    public void setEndlessTag(String endlessTag) {
        this.endlessTag = endlessTag;
    }
    
    public String getPlusConditionTag() {
        return plusConditionTag;
    }
    
    public void setPlusConditionTag(String plusConditionTag) {
        this.plusConditionTag = plusConditionTag;
    }
    
    public String getIfPublish() {
        return ifPublish;
    }
    
    public void setIfPublish(String ifPublish) {
        this.ifPublish = ifPublish;
    }

    public String getEndorseDistrict() {
        return endorseDistrict;
    }

    public void setEndorseDistrict(String endorseDistrict) {
        this.endorseDistrict = endorseDistrict;
    }

    public String getBlockAuditStatus() {
        return blockAuditStatus;
    }

    public void setBlockAuditStatus(String blockAuditStatus) {
        this.blockAuditStatus = blockAuditStatus;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getAppPerson() {
        return appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }

	public List getStatusIdList2() {
		return statusIdList2;
	}

	public void setStatusIdList2(List statusIdList2) {
		this.statusIdList2 = statusIdList2;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(String acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
}
