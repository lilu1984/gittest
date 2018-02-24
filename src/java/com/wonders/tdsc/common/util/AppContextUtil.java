package com.wonders.tdsc.common.util;

import org.springframework.web.context.WebApplicationContext;

public class AppContextUtil {
    private static AppContextUtil instance = null;

    // 应用发布绝对目录
    private String webDeployedRealPath = null;

    // castorMap目录
    private String castorMapLocation = null;

    // spring应用环境
    private WebApplicationContext wac = null;

    /**
     * 单态获取应用环境
     * 
     * @return 应用环境
     */

    public static synchronized AppContextUtil getInstance() {
        if (instance == null) {
            instance = new AppContextUtil();
        }

        return instance;
    }

    /**
     * 取应用环境
     * 
     * @return WebApplicationContext
     */
    public WebApplicationContext getAppContext() {
        return wac;
    }

    /**
     * 设置应用环境
     * 
     * @return void
     */
    public void setAppContext(WebApplicationContext wac) {
        this.wac = wac;
    }

    /**
     * 取得应用发布绝对目录
     * 
     * @return
     */
    public String getWebDeployedRealPath() {
        return webDeployedRealPath;
    }

    /**
     * 设置应用发布绝对目录
     * 
     * @param webDeployedRealPath
     */
    public void setWebDeployedRealPath(String webDeployedRealPath) {
        this.webDeployedRealPath = webDeployedRealPath;
    }

    /**
     * 取得castorMap目录
     * @return
     */
    public String getCastorMapLocation() {
        return castorMapLocation;
    }

    /**
     * 设置castorMap目录
     * @param castorMapLocation
     */
    public void setCastorMapLocation(String castorMapLocation) {
        this.castorMapLocation = castorMapLocation;
    }
}
