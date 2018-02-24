package com.wonders.tdsc.bo;



/**
 * TdscActivenodeView entity. @author MyEclipse Persistence Tools
 */

public class TdscActivenodeView  implements java.io.Serializable {


    // Fields    

     private String id;
     private String appId;
     private String nodeName;


    // Constructors

    /** default constructor */
    public TdscActivenodeView() {
    }

    
    /** full constructor */
    public TdscActivenodeView(String appId, String nodeName) {
        this.appId = appId;
        this.nodeName = nodeName;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return this.appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNodeName() {
        return this.nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
   








}