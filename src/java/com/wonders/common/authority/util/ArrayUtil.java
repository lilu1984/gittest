package com.wonders.common.authority.util;

public class ArrayUtil {
    /**
     * 将String类型的数组转变为逗号分隔的字符串
     * 
     * @param strArray
     *            String类型的数组
     * @return 逗号分隔的字符串
     */
    public static String strArray2StrComma(String[] strArray) {
        StringBuffer strComma = new StringBuffer();

        if (strArray != null && strArray.length > 0) {
            for (int i = 0; i < strArray.length; i++) {
                if (i < strArray.length - 1)
                    strComma.append("'").append(strArray[i]).append("',");
                else
                    strComma.append("'").append(strArray[i]).append("'");
            }
        } else
            strComma.append("''");

        return strComma.toString();
    }
}
