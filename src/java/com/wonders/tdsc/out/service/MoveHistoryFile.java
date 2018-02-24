package com.wonders.tdsc.out.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.ext.DateConverter;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.util.PropertiesUtil;

public class MoveHistoryFile {

	private static final String	fromPath	= PropertiesUtil.getInstance().getProperty("daping_path");
	private static final String	toPath		= PropertiesUtil.getInstance().getProperty("daping_history_path");
	private static final String	subFolders	= PropertiesUtil.getInstance().getProperty("daping_sub_path");

	// private static final String fromPath = "E:\\aaa";
	// private static final String toPath = "E:\\ccc";
	// private static final String subFolders = "aa,fda";

//	public static void main(String[] args) {
//		try {
//
//			MoveHistoryFile m = new MoveHistoryFile();
//			m.copyFile("E:/aaa/aa/1.txt", "E:/ccc/nnn/", "222.txt");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// �ļ������ɹ���: [Z/P/G]_NoticeNo_yyyyMMddHHmmss.xml

	// /crgg ���ù���Ŀ¼ ��Ϣ������
	// /zxdy ��ѯ���� ��Ϣ������
	// /cjgg �ɽ����� ��Ϣ������
	// /cjqk �ɽ���� ��Ϣ������ .1
	// /cjqklist �ɽ����list ��Ϣ������ .2
	// /xcjj �ֳ����� ÿ������ƶ�����ʷĿ¼

	// �Թ��淢����������Ϊ�����㣬�����ǰ���ڴ��ڵ��� Ŀ¼���ļ������ڣ����ƶ�����ʷĿ¼
	public void moveIt() {
		String[] subPath = subFolders.split(",");
		if (subPath != null && subPath.length > 0)
			for (int i = 0; i < subPath.length; i++) {
				String[] filesName = getAllFileName(subPath[i]);
				if (filesName != null && filesName.length > 0)
					for (int j = 0; j < filesName.length; j++) {
						if (isExpireDate(filesName[j])) {
							// �ж��Ƿ����
							moveFile(subPath[i], filesName[j]);
						}
					}
			}
	}

	private boolean isExpireDate(String fileName) {
		String fileTime = "";
		if(fileName.lastIndexOf(".") >= 26){//��ʱ��fileNameΪ���硰G_201103051400000000000000.xml��֮��,����"Z/P/G" + "_" + "yyyymmddhhmmss" + "000" + "0000000" + ".html/.xml"���
			fileTime = fileName.substring(fileName.lastIndexOf(".")-24, fileName.lastIndexOf(".")-10);
		} else{//��ʱ��fileNameΪ���硰G_nullnull.xml������"G_20110304.xml"֮��
			fileTime = fileName.substring(fileName.lastIndexOf(".")-8, fileName.lastIndexOf("."));
		}
		//String fileTime = fileName.substring(fileName.lastIndexOf(".")-29, fileName.lastIndexOf(".")-15);
		//String nowTime = DateUtil.date2String(new java.util.Date(), "yyyyMMddHHmmss");
		String nowTime = DateConvertor.getCurrentDateWithTimeZone();
		Long f = new Long(fileTime);
		Long n = new Long(nowTime);
		if (f.longValue() <= n.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * �õ���Ŀ¼�������ļ�
	 * 
	 * @return String []
	 */
	private String[] getAllFileName(String subPath) {
		String[] ret = null;
		File file = new File(fromPath + "/" + subPath + "/");
		File[] f = file.listFiles();
		if (f != null && f.length > 0) {
			ret = new String[f.length];
			for (int i = 0; i < f.length; i++) {
				File ff = f[i];
				ret[i] = ff.getName();

			}
		}
		return ret;
	}

	private void moveFile(String folder, String filename) {
		File oldfile = new File(fromPath + "/" + folder + "/" + filename);
		File newfile = new File(toPath + "/" + folder + "/");
		if (!newfile.exists())
			newfile.mkdirs();
		newfile = new File(toPath + "/" + folder + "/" + filename);
		oldfile.renameTo(newfile);
	}

	

}
