package com.wonders.tdsc.bo;



/**
 * TdscJggsListView entity. @author MyEclipse Persistence Tools
 */

public class TdscJggsListView  implements java.io.Serializable {


    // Fields    

     private String noticeId;
     private String noitceNo;
     private String title;


    // Constructors

    /** default constructor */
    public TdscJggsListView() {
    }

    
    /** full constructor */
    public TdscJggsListView(String noitceNo) {
        this.noitceNo = noitceNo;
    }

   
    // Property accessors

    public String getNoticeId() {
        return this.noticeId;
    }
    
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoitceNo() {
        return this.noitceNo;
    }
    
    public void setNoitceNo(String noitceNo) {
        this.noitceNo = noitceNo;
    }
   
    public String getTitle() {
		StringBuffer tempTitle = new StringBuffer();
		if (!"".equals(getNoitceNo())
				&& getNoitceNo().length() > 8) {
			tempTitle = new StringBuffer(getNoitceNo().substring(7, 11))
					.append("第");
			int tempNum = Integer.parseInt(getNoitceNo().substring(12, 15)) % 1000;
			tempTitle.append(tempNum)
					.append("号国有建设用地使用权出让公告交易结果公示");
		}
		return tempTitle.toString();
	}


	public void setTitle(String title) {
		this.title = title;
	}







}