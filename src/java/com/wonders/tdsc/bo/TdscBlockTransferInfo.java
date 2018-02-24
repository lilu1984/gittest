package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TdscBlockTransferInfo implements Serializable {

	public TdscBlockTransferInfo() {
    }
    private String landName;
    
    private String transferMode;
    
    private String blockNoticeNo;
    
    private Double marginAmount;
    
    private String bidderType;
    
    private String linkMan;
        
    private String auditedNum;

    private Double resultPrice;

    private String supplyName;
    
    private String district;
    
    private String linkManTel;
    
    private Date resultDate;

    private String specialPromise;

    private String greeningRate;

    private String fourSide;

    private BigDecimal totalLandArea;

    private String volumeRate;

    private String transferLife;
    
    private List blockPartList;

    private List bidderList;
    
    private String resultDoc;
        
    private String landUseType;
    
    
    /** 固定资产投资强度 weedlu 080411*/
    private String fixInvestAmount;
    
    public String getAuditedNum() {
        return auditedNum;
    }

    public void setAuditedNum(String auditedNum) {
        this.auditedNum = auditedNum;
    }
    
    public String getBlockNoticeNo() {
        return blockNoticeNo;
    }

    public void setBlockNoticeNo(String blockNoticeNo) {
        this.blockNoticeNo = blockNoticeNo;
    }

    public String getFourSide() {
        return fourSide;
    }

    public void setFourSide(String fourSide) {
        this.fourSide = fourSide;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public String getResultDoc() {
        return resultDoc;
    }

    public void setResultDoc(String resultDoc) {
        this.resultDoc = resultDoc;
    }

    public Double getResultPrice() {
        return resultPrice;
    }

    public void setResultPrice(Double resultPrice) {
        this.resultPrice = resultPrice;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public List getBidderList() {
        return bidderList;
    }

    public void setBidderList(List bidderList) {
        this.bidderList = bidderList;
    }

    public void setVolumeRate(String volumeRate) {
        this.volumeRate = volumeRate;
    }

    public String getVolumeRate() {
        return volumeRate;
    }

    public String getGreeningRate() {
        return greeningRate;
    }

    public void setGreeningRate(String greeningRate) {
        this.greeningRate = greeningRate;
    }
    
    public List getBlockPartList() {
		return blockPartList;
	}

	public void setBlockPartList(List blockPartList) {
		this.blockPartList = blockPartList;
	}

	public String getSpecialPromise() {
		return specialPromise;
	}

	public void setSpecialPromise(String specialPromise) {
		this.specialPromise = specialPromise;
	}

	public String getTransferLife() {
		return transferLife;
	}

	public void setTransferLife(String transferLife) {
		this.transferLife = transferLife;
	}

	public Double getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(Double marginAmount) {
		this.marginAmount = marginAmount;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLinkManTel() {
		return linkManTel;
	}

	public void setLinkManTel(String linkManTel) {
		this.linkManTel = linkManTel;
	}

	public String getBidderType() {
		return bidderType;
	}

	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getFixInvestAmount() {
		return fixInvestAmount;
	}

	public void setFixInvestAmount(String fixInvestAmount) {
		this.fixInvestAmount = fixInvestAmount;
	}

	public String getLandUseType() {
		return landUseType;
	}

	public void setLandUseType(String landUseType) {
		this.landUseType = landUseType;
	}

	public BigDecimal getTotalLandArea() {
		return totalLandArea;
	}

	public void setTotalLandArea(BigDecimal totalLandArea) {
		this.totalLandArea = totalLandArea;
	}

}
