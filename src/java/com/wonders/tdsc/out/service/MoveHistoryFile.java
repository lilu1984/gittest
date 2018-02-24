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

	// 文件名生成规则: [Z/P/G]_NoticeNo_yyyyMMddHHmmss.xml

	// /crgg 出让公告目录 信息发布二
	// /zxdy 咨询答疑 信息发布三
	// /cjgg 成交公告 信息发布四
	// /cjqk 成交情况 信息发布五 .1
	// /cjqklist 成交情况list 信息发布五 .2
	// /xcjj 现场竞价 每天必须移动到历史目录

	// 以公告发布结束日期为结束点，如果当前日期大于等于 目录下文件名日期，则移动到历史目录
	public void moveIt() {
		String[] subPath = subFolders.split(",");
		if (subPath != null && subPath.length > 0)
			for (int i = 0; i < subPath.length; i++) {
				String[] filesName = getAllFileName(subPath[i]);
				if (filesName != null && filesName.length > 0)
					for (int j = 0; j < filesName.length; j++) {
						if (isExpireDate(filesName[j])) {
							// 判断是否过期
							moveFile(subPath[i], filesName[j]);
						}
					}
			}
	}

	private boolean isExpireDate(String fileName) {
		String fileTime = "";
		if(fileName.lastIndexOf(".") >= 26){//此时的fileName为诸如“G_201103051400000000000000.xml”之类,它由"Z/P/G" + "_" + "yyyymmddhhmmss" + "000" + "0000000" + ".html/.xml"组成
			fileTime = fileName.substring(fileName.lastIndexOf(".")-24, fileName.lastIndexOf(".")-10);
		} else{//此时的fileName为诸如“G_nullnull.xml”或者"G_20110304.xml"之类
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
	 * 得到子目录下所有文件
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
