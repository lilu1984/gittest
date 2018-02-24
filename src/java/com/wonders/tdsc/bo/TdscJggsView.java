package com.wonders.tdsc.bo;



/**
 * TdscJggsView entity. @author MyEclipse Persistence Tools
 */

public class TdscJggsView  implements java.io.Serializable {


    // Fields    

     private String id;
     private String resultName;
     private String blockNoticeNo;
     private String blockName;
     private String noitceNo;
     private String resultPrice;
     private String title;


    // Constructors

    /** default constructor */
    public TdscJggsView() {
    }

    
    /** full constructor */
    public TdscJggsView(String resultName, String blockNoticeNo, String blockName, String noitceNo, String resultPrice) {
        this.resultName = resultName;
        this.blockNoticeNo = blockNoticeNo;
        this.blockName = blockName;
        this.noitceNo = noitceNo;
        this.resultPrice = resultPrice;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getResultName() {
        return this.resultName;
    }
    
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getBlockNoticeNo() {
        return this.blockNoticeNo;
    }
    
    public void setBlockNoticeNo(String blockNoticeNo) {
        this.blockNoticeNo = blockNoticeNo;
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

    public String getResultPrice() {
        return this.resultPrice;
    }
    
    public void setResultPrice(String resultPrice) {
        this.resultPrice = resultPrice;
    }


	public String getTitle() {
		StringBuffer tempTitle = new StringBuffer();
		if (!"".equals(getBlockNoticeNo())
				&& getBlockNoticeNo().length() > 8) {
			tempTitle = new StringBuffer(getBlockNoticeNo().substring(0, 4))
					.append("第");
			int tempNum = Integer.parseInt(getBlockNoticeNo().substring(4, 7)) % 1000;
			tempTitle.append(tempNum)
					.append("号国有建设用地使用权出让公告交易结果公示");
		}
		return tempTitle.toString();
	}


	public void setTitle(String title) {
		this.title = title;
	}

}