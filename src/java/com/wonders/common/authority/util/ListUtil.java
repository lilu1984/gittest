package com.wonders.common.authority.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ListUtil {
    /** ��־ */
    static Logger logger = Logger.getLogger(ListUtil.class);

    /**
     * ͨ��id���ҵ�list��
     * 
     * @param methodName
     *            ������
     * @param id
     *            ����
     * @return �б�
     */
    public static List getSubListFromListById(List list, String methodName, Object id) {
        List rtnList = new ArrayList();

        if (id != null) {
            if (list.size() > 0) {
                // �����ķ�������
                Class clazz = list.get(0).getClass();
                Method meth = getMethodFromClassByMethodName(clazz, methodName);

                // ��list���ҵ�list
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
     * ͨ��id���ҵ�object��
     * 
     * @param methodName
     *            ������
     * @param id
     *            ����
     * @return ����
     */
    public static Object getObjectFromListById(List list, String methodName, Object id) {
        Object rtnObject = null;

        if (id != null) {
            if (list.size() > 0) {
                // �����ķ�������
                Class cls = list.get(0).getClass();
                Method meth = getMethodFromClassByMethodName(cls, methodName);

                // ��list���ҵ�Object
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
     * ��һ��list�м�ȥ����һ��list
     * 
     * @param fromList
     *            ����
     * @param minuendList
     *            ������
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
     * ��String���͵�����ת���List
     * 
     * @param strArray
     *            String���͵�����
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
     * ͨ�����������ƴ����л�÷�������
     * 
     * @param clazz
     *            ��
     * @param methodName
     *            ������
     * @return Method ��������
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
     * ������Collection����ϲ���ȡ������
     * 
     * ע�������������е���Դ�в��ظ��Ŀ�����Դ��
     * 
     * @param cs
     *            Դ
     * @param cf
     *            ��������
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
