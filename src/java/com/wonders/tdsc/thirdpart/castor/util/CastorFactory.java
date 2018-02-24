package com.wonders.tdsc.thirdpart.castor.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

public class CastorFactory {
	private static final Logger logger = Logger.getLogger(CastorFactory.class);

	private static String XML_ENCODING = "GBK";

	/**
	 * ��beanת���xml
	 * 
	 * @param bean
	 * @param mapFile
	 * @param xmlFile
	 */
	public static void marshalBean(Object bean, String mapFile, String xmlFile) {

		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();

			map.loadMapping(mapFile);

			File file = new File(xmlFile);
			if (!file.exists()) {
				Writer writer = new FileWriter(file);

				Marshaller marshaller = new Marshaller(writer);
				marshaller.setEncoding(XML_ENCODING);
				marshaller.setMapping(map);
				marshaller.marshal(bean);

				writer.close();
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * ��beanת���xml
	 * 
	 * @param bean
	 * @param mapFile
	 * @param xmlFile
	 */
	public static void marshalBeanOverWriteXml(Object bean, String mapFile,
			String xmlFile) {

		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();

			map.loadMapping(mapFile);

			File file = new File(xmlFile);
			Writer writer = new FileWriter(file);

			Marshaller marshaller = new Marshaller(writer);
			marshaller.setEncoding(XML_ENCODING);
			marshaller.setMapping(map);
			marshaller.marshal(bean);

			writer.close();

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * ��beanת���xml�ַ���
	 * 
	 * @param bean
	 * @param mapFile
	 * @param xmlFile
	 */
	public static String marshalBean(Object bean, String mapFile) {
		Writer writer = new StringWriter();
		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();
			map.loadMapping(mapFile);

			Marshaller marshaller = new Marshaller(writer);
			marshaller.setEncoding(XML_ENCODING);
			marshaller.setMapping(map);
			marshaller.marshal(bean);

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

		return writer.toString();
	}
	/**
	 * ��beanת���xml�ַ���
	 * 
	 * @param bean
	 * @param mapFile
	 * @param xmlFile
	 */
	public static String marshalBeanByEncoding(Object bean, String mapFile,String encoding) {
		Writer writer = new StringWriter();
		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();
			map.loadMapping(mapFile);

			Marshaller marshaller = new Marshaller(writer);
			marshaller.setEncoding(encoding);
			marshaller.setMapping(map);
			marshaller.marshal(bean);

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

		return writer.toString();
	}
	/**
	 * ��xml�ļ�ת���bean
	 * 
	 * @param mapFile
	 * @param xmlFile
	 * @return
	 */
	public static Object unMarshalBean(String mapFile, File xmlFile) {
		Object bean = null;

		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();
			map.loadMapping(mapFile);

			Reader reader = new FileReader(xmlFile);
			Unmarshaller unmarshaller = new Unmarshaller(map);
			bean = unmarshaller.unmarshal(reader);

			reader.close();

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

		return bean;
	}

	/**
	 * ��xml�ַ���ת���bean
	 * 
	 * @param mapFile
	 * @param xmlFile
	 * @return
	 */
	public static Object unMarshalBean(String mapFile, String xmlStr) {
		Object bean = null;

		try {
			// װ��mapping�ļ�
			Mapping map = new Mapping();
			map.loadMapping(mapFile);

			Reader reader = new StringReader(xmlStr);
			Unmarshaller unmarshaller = new Unmarshaller(map);
			bean = unmarshaller.unmarshal(reader);

			reader.close();

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (MappingException e) {
			logger.error(e.getMessage());
		} catch (MarshalException e) {
			logger.error(e.getMessage());
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}

		return bean;
	}
}
