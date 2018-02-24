package com.wonders.tdsc.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class FileUtils {
	private static final Logger	logger	= Logger.getLogger(FileUtils.class);

	/**
	 * ���ַ������浽�ļ�
	 * 
	 * @param str
	 * @param filePathName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveStringToFile(String str, String path, String name) {
		try {
			// �鿴Ŀ¼�Ƿ����
			File directory = new File(path);
			if (!directory.exists())
				directory.mkdirs();

			InputStream is = new ByteArrayInputStream(str.getBytes());
			OutputStream os = new FileOutputStream(path + File.separator + name);

			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			os.close();
			is.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * BASE64���룬������ֽ����飬����������ַ�����
	 * 
	 * @param data
	 * @return
	 */
	public static char[] encode(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		//
		// ���ֽڽ���Base64����,ÿ�����ֽ�ת��Ϊ4���ַ�.
		// ��������ܱ�4������ż�����ַ�
		//
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * BASE64�����룬������ַ����飬����������ֽ�����
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decode(char[] data) {
		// ���������ж�����лس����ո�ȷǷ��ַ�����Ҫȥ����Щ�ַ�
		// �����Ͳ������������������
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || codes[data[ix]] < 0)
				--tempLen; // ȥ����Ч���ַ�
		}
		// ����byte�ĳ���
		// -- ÿ�ĸ���Ч�ַ���������ֽڵ�����
		// -- ����ж����3���ַ�����Ҫ����2���ֽ�,
		// ��������ж����2���ַ�����Ҫ����1���ֽ�
		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3)
			len += 2;
		if ((tempLen % 4) == 2)
			len += 1;
		byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		// һ��һ���ַ��ؽ��루ע���õĲ���tempLen��ֵ����ѭ����
		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];
			if (value >= 0) // ������Ч�ַ�
			{
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}
		// ������鳤�Ⱥ�ʵ�ʳ��Ȳ����ϣ���ô�׳�����
		if (index != out.length) {
			throw new Error("���ݳ��Ȳ�һ��(ʵ��д���� " + index + "�ֽڣ�����ϵͳָʾ��" + out.length + "�ֽ�)");
		}
		return out;
	}

	//
	// ���ڱ�����ַ�
	//
	static private char[]	alphabet	= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

	//
	// ���ڽ�����ֽڣ�0-255��
	//
	private static byte[]	codes		= new byte[256];
	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}

	/**
	 * ��ԭ·���� copy ָ�����ļ���Ŀ��·�����ɶ������ļ�����
	 * 
	 * @param src
	 *            ԭ�ļ�·�� + ԭ�ļ���
	 * @param dest
	 *            Ŀ��·��
	 * @param fileName
	 *            ���ļ���
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest, String fileName) {
		try {

			FileInputStream in = new FileInputStream(src);
			File file = new File(dest);
			if (!file.exists())
				file.mkdirs();
			File fff = new File(dest + fileName);
			if (!fff.exists())
				fff.createNewFile();
			FileOutputStream out = new FileOutputStream(fff);
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	
	public static boolean isExistsedFile(String src, String fileName) {
			File file = new File(src+fileName);
			if (!file.exists())
				return false;
			else
				return true;
	}
}
