package com.wonders.tdsc.bank.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.tdsc.bank.dao.TdscBankAppDao;
import com.wonders.tdsc.bo.TdscBankApp;
import com.wonders.tdsc.bo.condition.TdscBankAppCondition;

public class TdscBankAppService {
	
	private TdscBankAppDao tdscBankAppDao;
	
	private IdSpringManager idSpringManager;

	
	public void setTdscBankAppDao(TdscBankAppDao tdscBankAppDao) {
		this.tdscBankAppDao = tdscBankAppDao;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}


	
	public List queryTdscBankAppListByBidderId(String bidderId) {
		return tdscBankAppDao.queryTdscBankAppListByBidderId(bidderId);
	}
	
	public PageList queryTdscBankAppList(TdscBankAppCondition condition) {
		return tdscBankAppDao.queryTdscBankAppList(condition);
	}
	
	public List findTdscBankAppList(TdscBankAppCondition condition) {
		return tdscBankAppDao.findTdscBankAppList(condition);
	}

	public TdscBankApp getTdscBankAppById(String tdscBankAppId) {
		return tdscBankAppDao.getTdscBankAppById(tdscBankAppId);
	}

	public TdscBankApp saveOrUpdateTdscBankApp(TdscBankApp tdscBankApp) {
		if(StringUtils.isNotBlank(tdscBankApp.getId())){
			tdscBankAppDao.saveOrUpdate(tdscBankApp);
			return tdscBankApp;			
		}else{
			tdscBankApp.setStatus("0");// ״̬��0Ϊδ����
			tdscBankApp.setSerialNum(genAppNum("Bank", idSpringManager));//ҵ����ˮ��
			return (TdscBankApp)tdscBankAppDao.save(tdscBankApp);
		}
	}

	/**
	 * ����ҵ����ˮ��
	 * 
	 * @param busType
	 *            ҵ������
	 * @param incrementIdManager
	 *            ˳���������
	 * @return ���ɺõ�ҵ����ˮ��(ҵ������+���+˳���)
	 */
	public String genAppNum(String busType, IdSpringManager idSpringManager) {
		// ȡ�õ�ǰ���
		String curYear = DateUtil.date2String(new Date(), "yyyy");
		// ��ȡ�õı�����ó���Ч�ַ���
		String longBookId = ("00000" + idSpringManager.getIncrementId(busType + curYear));
		// ȡ����Ч�ַ���
		String bookId = longBookId.substring(longBookId.length() - 5, longBookId.length());
		// �������ɺõ������(ҵ���+���+˳���)
		return busType + curYear + bookId;
	}
}
