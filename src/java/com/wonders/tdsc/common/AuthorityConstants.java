package com.wonders.tdsc.common;

/**
 * 定义权限相关常量。
 * <p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class AuthorityConstants {
    private AuthorityConstants() {
        ;// this Constructor prevents this class to be instanced.
    }

    /** ******** 用户常量定义 ********* */

    /** 超级用户编号 */
    public static final String SUPER_USER_ID = "tdsc";

    /** 用户初始密码6个1 */
    public static final String DEFAULT_PASSWORD = "123456";

    /** ******** 资源分类常量定义 ********* */

    /** 菜单资源类别编码 * */
    public static final String CATEGORY_ID_MENU = "MENU";

    /** 非菜单权限:区县 * */
    public static final String CATEGORY_ID_QX = "QX";

    /** 非菜单权限:按钮 * */
    public static final String CATEGORY_ID_BUTTON = "BUTTON";
    /** 非菜单权限:工作流 * */
    public static final String CATEGORY_ID_WF = "WF";

    /** ******** 菜单级别定义 ********* */

    /** 一级菜单编号 * */
    public static final int MENU_LEVEL_FIRST = 1;

    /** 二级菜单编号 * */
    public static final int MENU_LEVEL_SECOND = 2;

    /** 三级菜单编号 * */
    public static final int MENU_LEVEL_THIRD = 3;

    /** 四级菜单编号 * */
    public static final int MENU_LEVEL_FOURTH = 4;

    /** ******** 系统菜单定义 ********* */

    /** 一级菜单父菜单值 * */
    public static final String MENU_ID_ROOT = "181";

}
