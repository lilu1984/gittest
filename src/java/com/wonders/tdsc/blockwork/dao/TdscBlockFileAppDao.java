package com.wonders.tdsc.blockwork.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.common.GlobalConstants;

public class TdscBlockFileAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscBlockFileApp.class;
	}

	public String upLoadFile(FormFile upLoadFile, String fileName) {
		// �����ļ���
		String fromFileName = upLoadFile.getFileName();
		String outFileName = fileName + ".doc";
		String filePath = (String) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.UPLOAD_BLOCK_FILE);

		// �鿴Ŀ¼�Ƿ����
		File directory = new File(filePath);
		if (!directory.exists())
			directory.mkdirs();

		try {
			File outFile = new File(
			// ����ļ��ķ�����·��
					filePath + File.separator + outFileName);
			if (outFile.exists()) {
				outFile.delete();
			}
			outFile.createNewFile();

			byte[] fileByte = upLoadFile.getFileData();
			if (fileByte.length > 0) {
				// �ڷ���������ӦĿ¼�¸����ϴ����ļ�����
				OutputStream os = new FileOutputStream(outFile);
				os.write(fileByte);
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			// �ϴ��ļ�����
			System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return null;
		}
		return (fromFileName);
	}

	public TdscBlockFileApp getBlockFileAppByRecordId(String recordId) {
		StringBuffer hql = new StringBuffer("from TdscBlockFileApp a where a.recordId ='").append(recordId).append("'");
		List list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (TdscBlockFileApp) list.get(0);
		}

		return null;
	}

}
