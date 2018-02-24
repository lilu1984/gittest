package com.wonders.tdsc.lob.bo;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscEsClob extends BaseBO {
    private String clobId;

    private String clobContent;

    public TdscEsClob() {
        super();
    }

    public String getClobId() {
        return clobId;
    }

    public void setClobId(String clobId) {
        this.clobId = clobId;
    }

    public String getClobContent() {
        return clobContent;
    }

    public void setClobContent(String clobContent) {
        this.clobContent = clobContent;
    }

}
