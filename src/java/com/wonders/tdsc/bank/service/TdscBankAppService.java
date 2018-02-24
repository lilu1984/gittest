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
			tdscBankApp.setStatus("0");// 状态，0为未关联
			tdscBankApp.setSerialNum(genAppNum("Bank", idSpringManager));//业务流水号
			return (TdscBankApp)tdscBankAppDao.save(tdscBankApp);
		}
	}

	/**
	 * 生成业务流水号
	 * 
	 * @param busType
	 *            业务类型
	 * @param incrementIdManager
	 *            顺序号生成器
	 * @return 生成好的业务流水号(业务类型+年份+顺序号)
	 */
	public String genAppNum(String busType, IdSpringManager idSpringManager) {
		// 取得当前年份
		String curYear = DateUtil.date2String(new Date(), "yyyy");
		// 将取得的编号设置成有效字符串
		String longBookId = ("00000" + idSpringManager.getIncrementId(busType + curYear));
		// 取得有效字符串
		String bookId = longBookId.substring(longBookId.length() - 5, longBookId.length());
		// 返回生成好的申请号(业务号+年份+顺序号)
		return busType + curYear + bookId;
	}
}
