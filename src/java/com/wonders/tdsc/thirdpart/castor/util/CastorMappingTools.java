package com.wonders.tdsc.thirdpart.castor.util;

import java.io.OutputStreamWriter;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.tools.MappingTool;

public class CastorMappingTools {
    /**
     * 生成类的对应xml_mapping文件
     * 
     * @param clazz
     */
    public static void generateMappingXml(Class clazz) {
        MappingTool mt = new MappingTool();
        try {
            mt.addClass(clazz);
            mt.write(new OutputStreamWriter(System.out));
        } catch (MappingException e) {
            e.printStackTrace();
        }
    }
}
