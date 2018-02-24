package com.wonders.tdsc.bo;

import com.wonders.esframework.common.type.AppString;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * TdscBlockTranView entity. @author MyEclipse Persistence Tools
 */

public class TdscBlockTranView  implements java.io.Serializable {


    // Fields    

     private String appId;
     private String blockId;
     private String blockName;
     private String noitceNo;
     private BigDecimal marginAmount;
     private String blockNoticeNo;
     private String districtName;
     private String landLocation;
     private BigDecimal totalLandArea;
     private String volumeRate;
     private BigDecimal totalBuildingArea;
     private String transferMode;
     private String resultDate;
     private String rangeEast;
     private String rangeWest;
     private String rangeNorth;
     private String rangeSouth;
     private String greeningRate;
     private String greeningRateCon;
     private String resultName;
     private BigDecimal initPrice;
     private BigDecimal resultPrice;
     private String status;
     private String density;
     private String currentSituationCondition;
     private String blockType;
     private String transferLife;
     private String nodeId;
     private String tranResult;
     private String flatRate;
     private String accessProductType;
     private String activeNodes;
     private Set activeNode = new HashSet(0);
     private String rangeSide;


    // Constructors

    /** default constructor */
    public TdscBlockTranView() {
    }

    
    /** full constructor */
    public TdscBlockTranView(String blockId, String blockName, String noitceNo, BigDecimal marginAmount, String blockNoticeNo, String districtName, String landLocation, BigDecimal totalLandArea, String volumeRate, BigDecimal totalBuildingArea, String transferMode, String resultDate, String rangeEast, String rangeWest, String rangeNorth, String rangeSouth, String greeningRate, String greeningRateCon, String resultName, BigDecimal initPrice, BigDecimal resultPrice, String status, String density, String currentSituationCondition, String blockType, String transferLife, String nodeId, String tranResult, String flatRate, String accessProductType, Set activeNode) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.noitceNo = noitceNo;
        this.marginAmount = marginAmount;
        this.blockNoticeNo = blockNoticeNo;
        this.districtName = districtName;
        this.landLocation = landLocation;
        this.totalLandArea = totalLandArea;
        this.volumeRate = volumeRate;
        this.totalBuildingArea = totalBuildingArea;
        this.transferMode = transferMode;
        this.resultDate = resultDate;
        this.rangeEast = rangeEast;
        this.rangeWest = rangeWest;
        this.rangeNorth = rangeNorth;
        this.rangeSouth = rangeSouth;
        this.greeningRate = greeningRate;
        this.greeningRateCon = greeningRateCon;
        this.resultName = resultName;
        this.initPrice = initPrice;
        this.resultPrice = resultPrice;
        this.status = status;
        this.density = density;
        this.currentSituationCondition = currentSituationCondition;
        this.blockType = blockType;
        this.transferLife = transferLife;
        this.nodeId = nodeId;
        this.tranResult = tranResult;
        this.flatRate = flatRate;
        this.accessProductType = accessProductType;
        this.activeNode = activeNode;
    }

   
    // Property accessors

    public String getAppId() {
        return this.appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBlockId() {
        return this.blockId;
    }
    
    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return this.blockName;
    }
    
    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getNoitceNo() {
        return this.noitceNo;
    }
    
    public void setNoitceNo(String noitceNo) {
        this.noitceNo = noitceNo;
    }

    public BigDecimal getMarginAmount() {
        return this.marginAmount;
    }
    
    public void setMarginAmount(BigDecimal marginAmount) {
        this.marginAmount = marginAmount;
    }

    public String getBlockNoticeNo() {
        return this.blockNoticeNo;
    }
    
    public void setBlockNoticeNo(String blockNoticeNo) {
        this.blockNoticeNo = blockNoticeNo;
    }

    public String getDistrictName() {
        return this.districtName;
    }
    
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLandLocation() {
        return this.landLocation;
    }
    
    public void setLandLocation(String landLocation) {
        this.landLocation = landLocation;
    }

    public BigDecimal getTotalLandArea() {
        return this.totalLandArea;
    }
    
    public void setTotalLandArea(BigDecimal totalLandArea) {
        this.totalLandArea = totalLandArea;
    }

    public String getVolumeRate() {
        return this.volumeRate;
    }
    
    public void setVolumeRate(String volumeRate) {
        this.volumeRate = volumeRate;
    }

    public BigDecimal getTotalBuildingArea() {
        return this.totalBuildingArea;
    }
    
    public void setTotalBuildingArea(BigDecimal totalBuildingArea) {
        this.totalBuildingArea = totalBuildingArea;
    }

    public String getTransferMode() {
        return this.transferMode;
    }
    
    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getResultDate() {
        return this.resultDate;
    }
    
    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getRangeEast() {
        return this.rangeEast;
    }
    
    public void setRangeEast(String rangeEast) {
        this.rangeEast = rangeEast;
    }

    public String getRangeWest() {
        return this.rangeWest;
    }
    
    public void setRangeWest(String rangeWest) {
        this.rangeWest = rangeWest;
    }

    public String getRangeNorth() {
        return this.rangeNorth;
    }
    
    public void setRangeNorth(String rangeNorth) {
        this.rangeNorth = rangeNorth;
    }

    public String getRangeSouth() {
        return this.rangeSouth;
    }
    
    public void setRangeSouth(String rangeSouth) {
        this.rangeSouth = rangeSouth;
    }

    public String getGreeningRate() {
        return this.greeningRate;
    }
    
    public void setGreeningRate(String greeningRate) {
        this.greeningRate = greeningRate;
    }

    public String getGreeningRateCon() {
        return this.greeningRateCon;
    }
    
    public void setGreeningRateCon(String greeningRateCon) {
        this.greeningRateCon = greeningRateCon;
    }

    public String getResultName() {
        return this.resultName;
    }
    
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public BigDecimal getInitPrice() {
        return this.initPrice;
    }
    
    public void setInitPrice(BigDecimal initPrice) {
        this.initPrice = initPrice;
    }

    public BigDecimal getResultPrice() {
        return this.resultPrice;
    }
    
    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDensity() {
        return this.density;
    }
    
    public void setDensity(String density) {
        this.density = density;
    }

    public String getCurrentSituationCondition() {
        return this.currentSituationCondition;
    }
    
    public void setCurrentSituationCondition(String currentSituationCondition) {
        this.currentSituationCondition = currentSituationCondition;
    }

    public String getBlockType() {
        return this.blockType;
    }
    
    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getTransferLife() {
    	if ("101".equals(getBlockType())) {
    		this.transferLife = "50年";
		}
        return this.transferLife;
    }
    
    public void setTransferLife(String transferLife) {
        this.transferLife = transferLife;
    }

    public String getNodeId() {
        return this.nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTranResult() {
        return this.tranResult;
    }
    
    public void setTranResult(String tranResult) {
        this.tranResult = tranResult;
    }

    public String getFlatRate() {
        return this.flatRate;
    }
    
    public void setFlatRate(String flatRate) {
        this.flatRate = flatRate;
    }

    public String getAccessProductType() {
        return this.accessProductType;
    }
    
    public void setAccessProductType(String accessProductType) {
        this.accessProductType = accessProductType;
    }

    public Set getActiveNode() {
        return this.activeNode;
    }
    
    public void setActiveNode(Set activeNode) {
        this.activeNode = activeNode;
    }


	public String getActiveNodes() {
		StringBuffer tmpDqyw = new StringBuffer("");
		Iterator iterator = activeNode.iterator();
		while(iterator.hasNext()){
			tmpDqyw.append(((TdscActivenodeView)iterator.next()).getNodeName()).append(",");
		}
		activeNodes = "".equals(tmpDqyw.toString()) ? null
				: tmpDqyw.substring(0, tmpDqyw.lastIndexOf(","));
		return activeNodes;
	}


	public void setActiveNodes(String activeNodes) {
		this.activeNodes = activeNodes;
	}


	public String getRangeSide() {
		StringBuffer tmpRangeSide = new StringBuffer("");
		tmpRangeSide.append(blockName).append("：");
		if (StringUtils.isNotEmpty(getRangeEast())) {
			tmpRangeSide.append("东至").append(
					getRangeEast())
					.append("，");
		}
		if (StringUtils.isNotEmpty(getRangeSouth())) {
			tmpRangeSide.append("南至").append(getRangeSouth())
					.append("，");
		}
		if (StringUtils.isNotEmpty(getRangeWest())) {
			tmpRangeSide.append("西至").append(getRangeWest())
					.append("，");
		}
		if (StringUtils.isNotEmpty(getRangeNorth())) {
			tmpRangeSide.append("北至").append(getRangeNorth())
					.append("，");
		}

		// 去掉“，”
		if ("，".equals(tmpRangeSide.substring(tmpRangeSide
				.length() - 1)))
			tmpRangeSide = new StringBuffer(tmpRangeSide
					.substring(0, tmpRangeSide.length() - 1));

		tmpRangeSide.append("；");

		// 去掉“：；”
		if (tmpRangeSide.indexOf("：；") > 0)
			tmpRangeSide = new StringBuffer("");
		return tmpRangeSide.toString();
	}


	public void setRangeSide(String rangeSide) {
		this.rangeSide = rangeSide;
	}
   








}