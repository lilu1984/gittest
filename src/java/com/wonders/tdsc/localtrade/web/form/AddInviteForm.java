package com.wonders.tdsc.localtrade.web.form;

import org.apache.struts.action.ActionForm;

public class AddInviteForm extends ActionForm{
	
	/**资格证书号 */
    private String certNo;
    
    /**评标编号 */
    private String tenderNo;
    
    /**投标人姓名 */
    private String inviteName;
    
    /**投标价格 */
    private String price;
    
    /**商务标得分 */
    private String businessScore;
    
    /**技术标得分 */
    private String techScore;
    
    /**总分 */
    private String totalScore;
    
    /**备注 */
    private String inviteMemo;
    

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public String getInviteName() {
		return inviteName;
	}

	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBusinessScore() {
		return businessScore;
	}

	public void setBusinessScore(String businessScore) {
		this.businessScore = businessScore;
	}

	public String getTechScore() {
		return techScore;
	}

	public void setTechScore(String techScore) {
		this.techScore = techScore;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getInviteMemo() {
		return inviteMemo;
	}

	public void setInviteMemo(String inviteMemo) {
		this.inviteMemo = inviteMemo;
	}

}
