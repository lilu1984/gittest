package com.wonders.tdsc.bo.condition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscBidderCondition extends BaseBO {
    /** 每页行数 */
	int pageSize;

    /** 当前页数 */
	int currentPage;

    /** 公告号 */
	private String blockNoticeNo;

    /** 地块名称 */
	private String blockName;

    /** 交易方式 */
	private Long transferMode;
	
	/** 通过appId 获得竞买申请信息,拍卖换领号牌时要用*/
	private String appId;
	
	private List appIdList;
	
	/** 公告号 */
	private String noticeId;
    
    /** 竞买人编号*/
    private String appPersonNumb;
     
    /** 竞买人姓名*/
    private String bidderName;
    
    /** 申请ID*/
    private String bidderId;
    
    private List bidderIdList;
    
    /** 竞买保证金到帐情况*/
  
    private String bzjDzqk;
    
    /** 竞买人账号 */
    private String bidderZh;
    
    /** 竞买人ID */
    private String bidderPersonId;
    
    /** 法定代表人 */
    private String corpFr;
    
    /** 是否是牵头人 */
    private String isHead;
    
    /** 保存到Session中用到的数据  */
    /** 身份证编号  */
    private String corpFrZjhm;

    /** 实际缴纳金额 */
    private BigDecimal bzjDzse;

    /** 竞买申请人属性 */
    private String bidderPro;
    
    /** 证件名称 */
    private String materialBhs[];

    /** 收件类别 */
    private String materialTypes[];

    /** 份数 */
    private Integer materialCounts[];
    /** 保存到Session中用到的数据end  */

    /**受理编号*/
    private String acceptNo;  
    
    /**竞买方式 */
    private String bidderType;

    /**申请人名称*/
    private String appPerson;  
    
    /***/
    private Date appDate;
    
    /**申请受理时间*/
    private Date acceptDate;    
    
    /** 一卡通磁条号 */
    private String yktBh;
    
    /** 一卡通卡号 */
    private String yktXh;

    /** 一卡通密码 */
    private String yktMm;

    /** 资格证书编号 */
    private String certNo;
    
    /** 竞买申请的到账情况 */
    private String bzjztDzqk;
    
    /** 组织名称  */
    private String organizName;
    
    /**用户ID*/
    private String appUserId;
    
    private String ifCommit;
    
	/** 设置用户ID */
	private String userId;
	
	private String notYktBh;
	
	private List certNoList;
	
	//意向地块的ID
	private String purposeAppId;
	
	
	
	public String getPurposeAppId() {
		return purposeAppId;
	}

	public void setPurposeAppId(String purposeAppId) {
		this.purposeAppId = purposeAppId;
	}

	public List getCertNoList() {
		return certNoList;
	}

	public void setCertNoList(List certNoList) {
		this.certNoList = certNoList;
	}

	public String getNotYktBh() {
		return notYktBh;
	}

	public void setNotYktBh(String notYktBh) {
		this.notYktBh = notYktBh;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public String getOrganizName() {
		return organizName;
	}

	public void setOrganizName(String organizName) {
		this.organizName = organizName;
	}

	public String getYktBh() {
		return yktBh;
	}

	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}

	public String getYktMm() {
		return yktMm;
	}

	public void setYktMm(String yktMm) {
		this.yktMm = yktMm;
	}

	public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getBidderPersonId() {
        return bidderPersonId;
    }

    public void setBidderPersonId(String bidderPersonId) {
        this.bidderPersonId = bidderPersonId;
    }

    public String getCorpFr() {
        return corpFr;
    }

    public void setCorpFr(String corpFr) {
        this.corpFr = corpFr;
    }

    public String getIsHead() {
        return isHead;
    }

    public void setIsHead(String isHead) {
        this.isHead = isHead;
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

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Long transferMode) {
		this.transferMode = transferMode;
	}

    public String getAppPersonNumb() {
        return appPersonNumb;
    }

    public void setAppPersonNumb(String appPersonNumb) {
        this.appPersonNumb = appPersonNumb;
    }

    public String getBzjDzqk() {
        return bzjDzqk;
    }

    public void setBzjDzqk(String bzjDzqk) {
        this.bzjDzqk = bzjDzqk;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBidderZh() {
        return bidderZh;
    }

    public void setBidderZh(String bidderZh) {
        this.bidderZh = bidderZh;
    }

    public String getBidderPro() {
        return bidderPro;
    }

    public void setBidderPro(String bidderPro) {
        this.bidderPro = bidderPro;
    }

    public BigDecimal getBzjDzse() {
        return bzjDzse;
    }

    public void setBzjDzse(BigDecimal bzjDzse) {
        this.bzjDzse = bzjDzse;
    }

    public String getCorpFrZjhm() {
        return corpFrZjhm;
    }

    public void setCorpFrZjhm(String corpFrZjhm) {
        this.corpFrZjhm = corpFrZjhm;
    }

    public String[] getMaterialBhs() {
        return materialBhs;
    }

    public void setMaterialBhs(String[] materialBhs) {
        this.materialBhs = materialBhs;
    }

    public Integer[] getMaterialCounts() {
        return materialCounts;
    }

    public void setMaterialCounts(Integer[] materialCounts) {
        this.materialCounts = materialCounts;
    }

    public String[] getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String[] materialTypes) {
        this.materialTypes = materialTypes;
    }

    public String getAcceptNo() {
        return acceptNo;
    }

    public void setAcceptNo(String acceptNo) {
        this.acceptNo = acceptNo;
    }

    public String getAppPerson() {
        return appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }

    public String getBidderType() {
        return bidderType;
    }

    public void setBidderType(String bidderType) {
        this.bidderType = bidderType;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBzjztDzqk() {
        return bzjztDzqk;
    }

    public void setBzjztDzqk(String bzjztDzqk) {
        this.bzjztDzqk = bzjztDzqk;
    }

	public String getYktXh() {
		return yktXh;
	}

	public void setYktXh(String yktXh) {
		this.yktXh = yktXh;
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

	public String getIfCommit() {
		return ifCommit;
	}

	public void setIfCommit(String ifCommit) {
		this.ifCommit = ifCommit;
	}

	public List getBidderIdList() {
		return bidderIdList;
	}

	public void setBidderIdList(List bidderIdList) {
		this.bidderIdList = bidderIdList;
	}



}
