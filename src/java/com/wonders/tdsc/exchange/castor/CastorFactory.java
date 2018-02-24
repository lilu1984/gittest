package com.wonders.tdsc.exchange.castor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;


public class CastorFactory {
    /** 日志 */
    protected static Log logger = LogFactory.getLog(CastorFactory.class);

    /** xml文件解析编码 */
    private static String xmlEncoding = "UTF-8";

    /** xml文件写入编码 */
    private static String fileEncoding = "UTF-8";

    public static void setXmlEncoding(String xmlEncoding) {
        CastorFactory.xmlEncoding = xmlEncoding;
    }

    public static void setFileEncoding(String fileEncoding) {
        CastorFactory.fileEncoding = fileEncoding;
    }

    /**
     * 将bean转变成xml
     * 
     * @param bean
     * @param mapFile
     * @param xmlFile
     * @throws IOException,MappingException,MarshalException,ValidationException
     * 
     */
    public static void marshalBean(Object bean, String mapFile, String xmlFile) throws IOException, MappingException,
            MarshalException, ValidationException {
        FileOutputStream fos = new FileOutputStream(xmlFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, fileEncoding);

        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            Marshaller marshaller = new Marshaller(osw);
            marshaller.setEncoding(xmlEncoding);
            marshaller.setMapping(map);
            marshaller.marshal(bean);

        } catch (IOException e) {
            logger.error(e);
            throw e;
        } catch (MappingException e) {
            logger.error(e);
            throw e;
        } catch (MarshalException e) {
            logger.error(e);
            throw e;
        } catch (ValidationException e) {
            logger.error(e);
            throw e;
        } finally {
            osw.close();
            fos.close();
        }

    }

    /**
     * 将含有CDATA数据的bean转变成xml
     * 
     * @param bean
     * @param mapFile
     * @param xmlFile
     * @param cdata cdata包含的数据字段名称
     * @throws IOException,MappingException,MarshalException,ValidationException
     * 
     */
    public static void marshalBeanForCDATA(Object bean, String mapFile, String xmlFile,String[] cdata) throws IOException, MappingException,
            MarshalException, ValidationException {
        FileOutputStream fos = new FileOutputStream(xmlFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, fileEncoding);

        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            OutputFormat format = new OutputFormat(Method.XML, "UTF-8", true);
            
            // Define the names of the XML elements to put > around
               format.setCDataElements(cdata);
               format.setNonEscapingElements(cdata); // Those elements should NOT be escaped..
               //format.setPreserveSpace(true);
               //format.setIndenting(true);

            // Create the serializer
               XMLSerializer serializer = new XMLSerializer(osw,format);
               
            // Create the document handler
               DocumentHandler handler = serializer.asDocumentHandler();
            // Create the marshaller
               Marshaller marshaller = new Marshaller(handler);

               //marshaller.setEncoding(xmlEncoding);
               marshaller.setMapping(map);
               marshaller.marshal(bean);
            

        } catch (IOException e) {
            logger.error(e);
            throw e;
        } catch (MappingException e) {
            logger.error(e);
            throw e;
        } catch (MarshalException e) {
            logger.error(e);
            throw e;
        } catch (ValidationException e) {
            logger.error(e);
            throw e;
        } finally {
            osw.close();
            fos.close();
        }

    }

    /**
     * 将bean转变成xml字符串
     * 
     * @param bean
     * @param mapFile
     * @param xmlFile
     * @throws IOException,MappingException,MarshalException,ValidationException
     * 
     */
    public static String marshalBean(Object bean, String mapFile) throws IOException, MappingException,
            MarshalException, ValidationException {
        String xmlString = null;

        Writer writer = new StringWriter();
        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            Marshaller marshaller = new Marshaller(writer);
            marshaller.setEncoding(xmlEncoding);
            marshaller.setMapping(map);
            marshaller.marshal(bean);

            xmlString = writer.toString();

        } catch (IOException e) {
            logger.error(e);
            throw e;
        } catch (MappingException e) {
            logger.error(e);
            throw e;
        } catch (MarshalException e) {
            logger.error(e);
            throw e;
        } catch (ValidationException e) {
            logger.error(e);
            throw e;
        } finally {
            writer.close();
        }

        return xmlString;
    }




    /**
     * 将bean转变成xml字符串
     * 
     * @param bean
     * @param mapFile
     * @param xmlFile
     * @param charset
     */
    public static String marshalBean2Str(Object bean, String mapFile, String charset) {
        Writer writer = new StringWriter();
        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            Marshaller marshaller = new Marshaller(writer);
            marshaller.setEncoding(charset);
            marshaller.setMapping(map);
            marshaller.marshal(bean);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (MappingException e) {
            logger.error(e.getMessage(), e);
        } catch (MarshalException e) {
            logger.error(e.getMessage(), e);
        } catch (ValidationException e) {
            logger.error(e.getMessage(), e);
        }

        return writer.toString();
    }

    /**
     * 将xml文件转变成bean
     * 
     * @param mapFile
     * @param xmlFile
     * @return
     */
    public static Object unMarshalBean(String mapFile, File xmlFile) {
        Object bean = null;

        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            Reader reader = new FileReader(xmlFile);
            Unmarshaller unmarshaller = new Unmarshaller(map);
            bean = unmarshaller.unmarshal(reader);

            reader.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (MappingException e) {
            logger.error(e.getMessage(), e);
        } catch (MarshalException e) {
            logger.error(e.getMessage(), e);
        } catch (ValidationException e) {
            logger.error(e.getMessage(), e);
        }

        return bean;
    }

    /**
     * 将xml字符串转变成bean
     * 
     * @param mapFile
     * @param xmlFile
     * @return
     */
    public static Object unMarshalBean(String mapFile, String xmlStr) {
        Object bean = null;

        try {
            // 装载mapping文件
            Mapping map = new Mapping();
            map.loadMapping(mapFile);

            Reader reader = new StringReader(xmlStr);
            Unmarshaller unmarshaller = new Unmarshaller(map);
            bean = unmarshaller.unmarshal(reader);

            reader.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (MappingException e) {
            logger.error(e.getMessage(), e);
        } catch (MarshalException e) {
            logger.error(e.getMessage(), e);
        } catch (ValidationException e) {
            logger.error(e.getMessage(), e);
        }

        return bean;
    }
}
