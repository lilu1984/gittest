package com.wonders.tdsc.bo;

import java.math.BigDecimal;

/**
 * TdscBlockPjxxInfoId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TdscBlockPjxxInfo implements java.io.Serializable {

	private String pjxxInfoId;
	private String blockId;
	private BigDecimal priceXdfj;
	private Integer countLzfpj;
	private Integer countJjsyf;
	private BigDecimal areaLandLjf;
	private BigDecimal areaLandLzf;
	private BigDecimal areaLandJjf;
	private BigDecimal areaBuildXjf;
	private BigDecimal areaBuildLzf;
	private BigDecimal areaBuildJjf;
	private String pjlxs;
	private String pjxxMemo;

	public String getPjxxInfoId() {
		return pjxxInfoId;
	}

	public void setPjxxInfoId(String pjxxInfoId) {
		this.pjxxInfoId = pjxxInfoId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public BigDecimal getPriceXdfj() {
		return priceXdfj;
	}

	public void setPriceXdfj(BigDecimal priceXdfj) {
		this.priceXdfj = priceXdfj;
	}

	public Integer getCountLzfpj() {
		return countLzfpj;
	}

	public void setCountLzfpj(Integer countLzfpj) {
		this.countLzfpj = countLzfpj;
	}

	public Integer getCountJjsyf() {
		return countJjsyf;
	}

	public void setCountJjsyf(Integer countJjsyf) {
		this.countJjsyf = countJjsyf;
	}

	public BigDecimal getAreaLandLjf() {
		return areaLandLjf;
	}

	public void setAreaLandLjf(BigDecimal areaLandLjf) {
		this.areaLandLjf = areaLandLjf;
	}

	public BigDecimal getAreaLandLzf() {
		return areaLandLzf;
	}

	public void setAreaLandLzf(BigDecimal areaLandLzf) {
		this.areaLandLzf = areaLandLzf;
	}

	public BigDecimal getAreaLandJjf() {
		return areaLandJjf;
	}

	public void setAreaLandJjf(BigDecimal areaLandJjf) {
		this.areaLandJjf = areaLandJjf;
	}

	public BigDecimal getAreaBuildXjf() {
		return areaBuildXjf;
	}

	public void setAreaBuildXjf(BigDecimal areaBuildXjf) {
		this.areaBuildXjf = areaBuildXjf;
	}

	public BigDecimal getAreaBuildLzf() {
		return areaBuildLzf;
	}

	public void setAreaBuildLzf(BigDecimal areaBuildLzf) {
		this.areaBuildLzf = areaBuildLzf;
	}

	public BigDecimal getAreaBuildJjf() {
		return areaBuildJjf;
	}

	public void setAreaBuildJjf(BigDecimal areaBuildJjf) {
		this.areaBuildJjf = areaBuildJjf;
	}

	public String getPjxxMemo() {
		return pjxxMemo;
	}

	public void setPjxxMemo(String pjxxMemo) {
		this.pjxxMemo = pjxxMemo;
	}

	public String getPjlxs() {
		return pjlxs;
	}

	public void setPjlxs(String pjlxs) {
		this.pjlxs = pjlxs;
	}

}