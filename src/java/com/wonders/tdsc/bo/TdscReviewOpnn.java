package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscReviewOpnn implements Serializable {

    /** identifier field */
    private String reviewId;

    /** nullable persistent field */
    private String bidderAppNo;

    /** nullable persistent field */
    private String specId;

    /** nullable persistent field */
    private String specName;

    /** nullable persistent field */
    private String reviewResult;

    /** nullable persistent field */
    private String reviewOpnn;

    /** nullable persistent field */
    private Date reviewDate;

    /** full constructor */
    public TdscReviewOpnn(String reviewId, String bidderAppNo, String specId, String specName, String reviewResult, String reviewOpnn, Date reviewDate) {
        this.reviewId = reviewId;
        this.bidderAppNo = bidderAppNo;
        this.specId = specId;
        this.specName = specName;
        this.reviewResult = reviewResult;
        this.reviewOpnn = reviewOpnn;
        this.reviewDate = reviewDate;
    }

    /** default constructor */
    public TdscReviewOpnn() {
    }

    /** minimal constructor */
    public TdscReviewOpnn(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getBidderAppNo() {
        return this.bidderAppNo;
    }

    public void setBidderAppNo(String bidderAppNo) {
        this.bidderAppNo = bidderAppNo;
    }

    public String getSpecId() {
        return this.specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return this.specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getReviewResult() {
        return this.reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewOpnn() {
        return this.reviewOpnn;
    }

    public void setReviewOpnn(String reviewOpnn) {
        this.reviewOpnn = reviewOpnn;
    }

    public Date getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reviewId", getReviewId())
            .toString();
    }

}
