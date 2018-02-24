package com.wonders.tdsc.bo.condition;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscReplyConfInfoCondition extends BaseCondition {
    
    /** identifier field */
    private String appId;

    /** nullable persistent field */
    
    private String ifPublish;

    /** nullable persistent field */
    private Date publishDate;
    
    /** µØ¿éÃû³Æ*/
    private String blockName;

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIfPublish() {
        return ifPublish;
    }

    public void setIfPublish(String ifPublish) {
        this.ifPublish = ifPublish;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
