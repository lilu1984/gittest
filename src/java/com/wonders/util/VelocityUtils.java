package com.wonders.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class VelocityUtils {
    /** 模板后缀 */
    public static final String TEMPLATE_POSTFIX_EXT = ".vm";
    // 应用环境目录
    private static String appPath = PropertiesUtil.getInstance().getProperty("app.path");
    /** 模板目录 */
    public static final String TEMPLATE_PATH = appPath + "\\tdsc\\vm";
    
    /**
     * 初始化velocity
     */
    static {
        // 设置velocity的log
        Velocity.setProperty(Velocity.RUNTIME_LOG, TEMPLATE_PATH + "\\velocity.log");
        System.out.println(TEMPLATE_PATH);
        // 设置velocity的输入输出编码
        Velocity.setProperty(Velocity.INPUT_ENCODING, "GBK");
        Velocity.setProperty(Velocity.OUTPUT_ENCODING, "GBK");

        // 设置velocity的模板路径（必要）
        Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, TEMPLATE_PATH);
        // 初始化velocity引擎
        try {
            Velocity.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param templateVO
     * @param templateName
     * @param vmFileName
     * @return 文件的内容
     * @throws Exception
     */

    public static String merge(Map velocityMap, String vmFileName) throws Exception {
        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext();

        // 取得velocity的模版
        Template template = Velocity.getTemplate(vmFileName + TEMPLATE_POSTFIX_EXT);

        // 输出流
        StringWriter stringWriter = new StringWriter();

        // 把数据填入上下文
        Set keySet = velocityMap.keySet();
        Iterator it = keySet.iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
            context.put(key, velocityMap.get(key));
        }

        if (template != null) {
            template.merge(context, stringWriter);
        }
        return stringWriter.toString();
    }

}
