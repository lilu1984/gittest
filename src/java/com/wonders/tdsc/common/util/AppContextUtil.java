package com.wonders.tdsc.common.util;

import org.springframework.web.context.WebApplicationContext;

public class AppContextUtil {
    private static AppContextUtil instance = null;

    // Ӧ�÷�������Ŀ¼
    private String webDeployedRealPath = null;

    // castorMapĿ¼
    private String castorMapLocation = null;

    // springӦ�û���
    private WebApplicationContext wac = null;

    /**
     * ��̬��ȡӦ�û���
     * 
     * @return Ӧ�û���
     */

    public static synchronized AppContextUtil getInstance() {
        if (instance == null) {
            instance = new AppContextUtil();
        }

        return instance;
    }

    /**
     * ȡӦ�û���
     * 
     * @return WebApplicationContext
     */
    public WebApplicationContext getAppContext() {
        return wac;
    }

    /**
     * ����Ӧ�û���
     * 
     * @return void
     */
    public void setAppContext(WebApplicationContext wac) {
        this.wac = wac;
    }

    /**
     * ȡ��Ӧ�÷�������Ŀ¼
     * 
     * @return
     */
    public String getWebDeployedRealPath() {
        return webDeployedRealPath;
    }

    /**
     * ����Ӧ�÷�������Ŀ¼
     * 
     * @param webDeployedRealPath
     */
    public void setWebDeployedRealPath(String webDeployedRealPath) {
        this.webDeployedRealPath = webDeployedRealPath;
    }

    /**
     * ȡ��castorMapĿ¼
     * @return
     */
    public String getCastorMapLocation() {
        return castorMapLocation;
    }

    /**
     * ����castorMapĿ¼
     * @param castorMapLocation
     */
    public void setCastorMapLocation(String castorMapLocation) {
        this.castorMapLocation = castorMapLocation;
    }
}
