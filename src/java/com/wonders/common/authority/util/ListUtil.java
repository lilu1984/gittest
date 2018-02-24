package com.wonders.common.authority.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ListUtil {
    /** 日志 */
    static Logger logger = Logger.getLogger(ListUtil.class);

    /**
     * 通过id号找到list。
     * 
     * @param methodName
     *            方法名
     * @param id
     *            号码
     * @return 列表
     */
    public static List getSubListFromListById(List list, String methodName, Object id) {
        List rtnList = new ArrayList();

        if (id != null) {
            if (list.size() > 0) {
                // 获得类的方法对象
                Class clazz = list.get(0).getClass();
                Method meth = getMethodFromClassByMethodName(clazz, methodName);

                // 从list中找到list
                if (meth != null) {
                    for (int i = 0; i < list.size(); i++) {
                        try {
                            Object rtnObj = meth.invoke(list.get(i), null);

                            if (id.equals(rtnObj)) {
                                rtnList.add(list.get(i));
                            }
                        } catch (Throwable e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        }

        return rtnList;
    }

    /**
     * 通过id号找到object。
     * 
     * @param methodName
     *            方法名
     * @param id
     *            号码
     * @return 对象
     */
    public static Object getObjectFromListById(List list, String methodName, Object id) {
        Object rtnObject = null;

        if (id != null) {
            if (list.size() > 0) {
                // 获得类的方法对象
                Class cls = list.get(0).getClass();
                Method meth = getMethodFromClassByMethodName(cls, methodName);

                // 从list中找到Object
                if (meth != null) {
                    for (int i = 0; i < list.size(); i++) {
                        try {
                            Object obj = meth.invoke(list.get(i), null);

                            if (id.equals(obj)) {
                                rtnObject = list.get(i);
                                break;
                            }
                        } catch (Throwable e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        }

        return rtnObject;
    }

    /**
     * 从一个list中减去另外一个list
     * 
     * @param fromList
     *            减数
     * @param minuendList
     *            被减数
     * @return list
     */
    public static List listMinus(Collection fromList, Collection minuendList) {
        if (fromList != null && minuendList != null)
            for (Iterator iter = minuendList.iterator(); iter.hasNext();) {
                Object obj = iter.next();

                if (fromList.contains(obj)) {
                    fromList.remove(obj);
                }
            }

        return new ArrayList(fromList);
    }

    /**
     * 将String类型的数组转变成List
     * 
     * @param strArray
     *            String类型的数组
     * @return list
     */
    public static List stringArray2List(String[] strArray) {
        List list = null;

        if (strArray != null && strArray.length > 0) {
            list = new ArrayList();

            for (int i = 0; i < strArray.length; i++)
                list.add(strArray[i]);
        }

        return list;

    }

    /**
     * 通过方法的名称从类中获得方法对象。
     * 
     * @param clazz
     *            类
     * @param methodName
     *            方法名
     * @return Method 方法对象
     */
    private static Method getMethodFromClassByMethodName(Class clazz, String methodName) {
        Method meth = null;

        Method methlist[] = clazz.getDeclaredMethods();
        for (int i = 0; i < methlist.length; i++) {
            if (methodName.equalsIgnoreCase(methlist[i].getName())) {
                meth = methlist[i];
                break;
            }
        }

        return meth;
    }

    /**
     * 将两个Collection对象合并（取交集）
     * 
     * 注：将拷贝对象中的与源中不重复的拷贝到源中
     * 
     * @param cs
     *            源
     * @param cf
     *            拷贝对象
     */
    public static void mergeList(Collection cs, Collection cf) {
        if (cs != null && cf != null)
            for (Iterator iter = cf.iterator(); iter.hasNext();) {
                Object obj = iter.next();
                if (!cs.contains(obj)) {
                    cs.add(obj);
                }
            }
    }
}
