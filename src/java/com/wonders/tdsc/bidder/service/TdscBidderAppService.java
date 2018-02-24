package com.wonders.tdsc.bidder.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.common.util.ext.BeanUtils;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderMaterialDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderProvideDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bidder.dao.TdscBusinessRecordDao;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscNoticeAppDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderMaterial;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.retbail.dao.TdscReturnBailDao;

public class TdscBidderAppService extends BaseSpringManagerImpl {

	private TdscBlockTranAppDao		tdscBlockTranAppDao;

	private TdscBidderAppDao		tdscBidderAppDao;

	private TdscBidderPersonAppDao	tdscBidderPersonAppDao;

	private TdscBusinessRecordDao	tdscBusinessRecordDao;

	private TdscBidderMaterialDao	tdscBidderMaterialDao;

	private TdscBidderProvideDao	tdscBidderProvideDao;

	private IdSpringManager			idSpringManager;

	private TdscBlockPartDao		tdscBlockPartDao;

	private TdscNoticeAppDao		tdscNoticeAppDao;

	private TdscReturnBailDao		tdscReturnBailDao;

	public void setTdscReturnBailDao(TdscReturnBailDao tdscReturnBailDao) {
		this.tdscReturnBailDao = tdscReturnBailDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}

	public void setTdscBidderProvideDao(TdscBidderProvideDao tdscBidderProvideDao) {
		this.tdscBidderProvideDao = tdscBidderProvideDao;
	}

	public void setTdscBidderMaterialDao(TdscBidderMaterialDao tdscBidderMaterialDao) {
		this.tdscBidderMaterialDao = tdscBidderMaterialDao;
	}

	public void setTdscBusinessRecordDao(TdscBusinessRecordDao tdscBusinessRecordDao) {
		this.tdscBusinessRecordDao = tdscBusinessRecordDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}

	/**
	 * ����������þ������б�
	 */
	public List findPageBidderList(String appId) {
		// ��������б�
		return tdscBidderAppDao.findPageList(appId);
	}

	/**
	 * ����appid,appUserId��þ������б�
	 */
	public List findPageBidderListByUserId(String appId, String appUserId) {
		// ��������б�
		return tdscBidderAppDao.findPageBidderListByUserId(appId, appUserId);
	}

	/**
	 * ����������þ������б� list_jmrgl.jsp��Action��
	 */
	public List findBidderList(TdscBidderCondition condition) {
		// ��������б�
		List bidderAppList = (List) tdscBidderAppDao.findPageListByIfCommit(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// ���÷��ص�List
			List retList = new ArrayList();
			List personAppList = new ArrayList();
			TdscBidderApp tdscBidderApp = new TdscBidderApp();
			for (int i = 0; i < bidderAppList.size(); i++) {
				tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
				// ��ѯ������������
				if (personAppList != null && personAppList.size() > 0) {
					tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
				}
				retList.add(tdscBidderApp);
			}
			return retList;
		}
		return null;
	}

	/**
	 * ����������þ������б�
	 */
	public List queryBidderAppListByCondition(TdscBidderCondition condition) {
		// ��������б�
		List bidderAppList = (List) tdscBidderAppDao.queryBidderAppListByCondition(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// ���÷��ص�List
			List retList = new ArrayList();
			List personAppList = new ArrayList();
			TdscBidderApp tdscBidderApp = new TdscBidderApp();
			TdscBidderCondition condition2 = new TdscBidderCondition();
			for (int i = 0; i < bidderAppList.size(); i++) {
				tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);

				condition2.setBidderId(tdscBidderApp.getBidderId());
				// condition2.setBidderName(condition.getBidderName());
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppListByCondition(condition2);
				String personName = this.getPersonNameByBidderId(tdscBidderApp.getBidderId());
				// ��ѯ������������
				if (personAppList != null && personAppList.size() > 0) {
					tdscBidderApp.setAcceptPeople(personName);
					tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
				}
				retList.add(tdscBidderApp);
			}
			return retList;
		}
		return null;
	}

	public List findBidderListByJMSQZSL(TdscBidderCondition condition) {
		// ��������б�
		List bidderAppList = (List) tdscBidderAppDao.findPageList(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// ���÷��ص�List
			List retList = new ArrayList();
			List personAppList = new ArrayList();
			TdscBidderApp tdscBidderApp = new TdscBidderApp();
			for (int i = 0; i < bidderAppList.size(); i++) {
				tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
				// ��ѯ������������
				if (personAppList != null && personAppList.size() > 0) {
					tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
				}
				retList.add(tdscBidderApp);
			}
			return retList;
		}
		return null;
	}

	// ����appIdListȡ�����о�������Ϣ�б�
	public List findBidderListByAppIdList(List appIdList) {
		// ȡ�ý��׿���Ų��ظ���app
		List retList = new ArrayList();
		List yktBhList = (List) tdscBidderAppDao.queryBidderAppListByAppIdList(appIdList);
		if (yktBhList != null && yktBhList.size() > 0) {
			for (int j = 0; j < yktBhList.size(); j++) {
				String appId = "";
				String bidderId = "";
				Object[] object = (Object[]) yktBhList.get(j);
				if (object != null) {
					appId = object[1].toString();
					bidderId = object[2].toString();
				}

				// ��������б�
				// List bidderAppList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);

				// ���÷��ص�List
				List personAppList = new ArrayList();
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
				// ƴ�����Ͼ���������,��������UnionCount�ֶ���
				String personName = "";
				if (personAppList != null && personAppList.size() > 0) {
					personName = this.getPersonNameByBidderId(bidderId);
					tdscBidderApp.setAcceptPeople(personName);
					tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
				}
				retList.add(tdscBidderApp);
			}
		}
		return retList;
	}

	// ����noticeIdȡ������yktbh����ͬ�ľ�������Ϣ�б�
	public List queryBidderAppListByNoticeId(String noticeId) {
		// ȡ�ý��׿���Ų��ظ���yktBh
		List retList = new ArrayList();
		List yktBhList = (List) tdscBidderAppDao.queryBidderAppListByNoticeId(noticeId);
		if (yktBhList != null && yktBhList.size() > 0) {
			for (int j = 0; j < yktBhList.size(); j++) {
				String yktBh = (String) yktBhList.get(j);

				// ���÷��ص�List
				List personAppList = new ArrayList();
				TdscBidderCondition condition = new TdscBidderCondition();
				condition.setNoticeId(noticeId);
				condition.setYktBh(yktBh);
				List bidderAppList = (List) tdscBidderAppDao.getTdscBidderAppList(condition);
				if (bidderAppList != null && bidderAppList.size() > 0) {
					// ��������������һ���ģ����ȡһ������
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(0);
					if (tdscBidderApp != null) {
						personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
						// ƴ�����Ͼ���������,��������UnionCount�ֶ���
						String personName = "";
						if (personAppList != null && personAppList.size() > 0) {
							personName = this.getPersonNameByBidderId(tdscBidderApp.getBidderId());
							tdscBidderApp.setAcceptPeople(personName);
							tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
						}
						retList.add(tdscBidderApp);
					}
				}
			}
		}
		return retList;
	}

	// ����bidderIdȡ�����о���������
	public String getPersonNameByBidderId(String bidderId) {
		String personName = "";
		if (bidderId != null && !"".equals(bidderId)) {
			List personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
			if (personAppList != null && personAppList.size() > 0) {
				for (int k = 0; k < personAppList.size(); k++) {
					TdscBidderPersonApp tdscBidderPerson = (TdscBidderPersonApp) personAppList.get(k);
					personName += tdscBidderPerson.getBidderName() + "��";
				}
				if (personName != null && personName.length() > 0)
					personName = personName.substring(0, personName.length() - 1);
			}
		}
		return personName;
	}

	/**
	 * ����appId�������������Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public TdscBlockTranApp findTdscBlockTranApp(String appId) {
		return tdscBlockTranAppDao.getMarginEndDate(appId);
	}

	/**
	 * ���Ͼ��������һ��������
	 * 
	 * @param tdscBidderPersonApp
	 *            ��Action��
	 * @param tdscBidMatList
	 * @return
	 */
	public TdscBidderPersonApp addOneBidderPerson(TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// ���Ӿ�����ʱ�� �������ĵ������ Ϊ�޲�����
		tdscBidderPersonApp.setBzjDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		TdscBidderPersonApp rtnPerson = (TdscBidderPersonApp) tdscBidderPersonAppDao.save(tdscBidderPersonApp);
		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(i);
				tdscBidderMaterial.setBidderPersonId(rtnPerson.getBidderPersonId());
				tdscBidderMaterialDao.save(tdscBidderMaterial);
			}
		}

		return rtnPerson;
	}

	/**
	 * ͨ��appId ������вμӵؿ����������
	 * 
	 * @param appId
	 * @return
	 */
	public List findBiddCountByAppId(String appId) {
		List countList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);
		return countList;
	}

	/**
	 * �����������һҳ���û��б�Action��
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * @return PageList����
	 */
	public PageList findPersonPageList(TdscBidderCondition condition) {
		return tdscBidderPersonAppDao.findPageList(condition);
	}

	/**
	 * ��������ɾ��һ����¼��Action��
	 * 
	 * @param tdscBidderPersonAppDao
	 * 
	 */
	public void removeById(String id) {
		tdscBidderPersonAppDao.deleteById(id);
	}

	/**
	 * ͨ��APPID�ҵ�һ���ж����˲���������,���ж����˻����˺���
	 * 
	 * @param appId
	 * @return
	 */
	public List queryJoinAuctionList(String appId) {
		return this.tdscBidderAppDao.findJoinAuctionListByAppId(appId);
	}

	/**
	 * ͨ��bidderPersonId���һ����������Ϣ
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public TdscBidderPersonApp queryOneBiddPer(String bidderPersonId) {
		TdscBidderPersonApp rtnPerson = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(bidderPersonId);
		return rtnPerson;
	}

	/**
	 * ͨ��bidderPersonId���һ���������ύ������Ϣ
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryOnePerMat(String bidderPersonId) {
		List perMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(bidderPersonId);
		return perMatList;
	}

	/**
	 * �޸����Ͼ����� ���������˵���Ϣ
	 * 
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 * @return
	 */
	public TdscBidderPersonApp modifyBidPerXX(TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// ���ԭ���ı�֤����ʱ�估�䵽�����
		// TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao
		// .get(tdscBidderPersonApp.getBidderPersonId());
		// // TdscBidderPersonApp
		// // personApp=(TdscBidderPersonApp)tdscBidderPersonAppDao.get(tdscBidderPersonApp.getBidderPersonId());
		// if (personApp != null) {
		// // ��ֵ���µ�ʵ�����
		// if (personApp.getBzjDzqk() != null && personApp.getBzjDzqk() != "") {
		// tdscBidderPersonApp.setBzjDzqk(personApp.getBzjDzqk());
		// }
		// if (personApp.getBzjDzsj() != null) {
		// tdscBidderPersonApp.setBzjDzsj(personApp.getBzjDzsj());
		// }
		// if (personApp.getBzjDzse() != null) {
		// tdscBidderPersonApp.setBzjDzse(personApp.getBzjDzse());
		// }
		// }

		tdscBidderPersonAppDao.saveOrUpdate(tdscBidderPersonApp);
		TdscBidderPersonApp retBidPerApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(tdscBidderPersonApp.getBidderPersonId());
		// ɾ�� bidderPersonId �������ύ�Ĳ���
		List bidMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(tdscBidderPersonApp.getBidderPersonId());
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
		// ��� ��¼�����Ϣ
		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(i);
				tdscBidderMaterial.setBidderId(retBidPerApp.getBidderId());
				tdscBidderMaterial.setBidderPersonId(retBidPerApp.getBidderPersonId());
				tdscBidderMaterialDao.save(tdscBidderMaterial);
			}
		}
		return retBidPerApp;
	}

	/**
	 * ���� bidderPersonId��ѯ�����˵Ĳ�����Ϣ
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryMatList(String bidderPersonId) {
		List queryMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(bidderPersonId);
		return queryMatList;
	}

	/**
	 * ���� bidderPersonId��ѯ�ֶ����ӵĲ�����Ϣ
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryOtherMatList(String bidderPersonId) {
		List queryMatList = (List) tdscBidderMaterialDao.getOtherMateByPersonId(bidderPersonId);
		return queryMatList;
	}

	/**
	 * ɾ��һ�������˼��������Ϣ
	 * 
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 */
	public void removeOneBidPer(TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {

		tdscBidderPersonAppDao.delete(tdscBidderPersonApp);

		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				tdscBidderMaterialDao.delete(tdscBidMatList.get(i));
			}
		}

	}

	/**
	 * ��������-����������--���� TDSC_BIDDER_APP�� TDSC_BIDDER_PERSON_APP�� TDSC_BIDDER_MATERIAL��
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 */
	public String addBidder(String provideBm, TdscBidderApp tdscBidderApp, TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// ���� tdscBidderApp ����� bidderId��

		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
		if (tdscBidderApp.getAcceptNo() == null) {
			String acceptNo = createAcceptNo(tdscBidderApp.getAppId());
			tdscBidderApp.setAcceptNo(acceptNo);
		}
		if (tdscBidderApp.getBzjztDzqk() == null || tdscBidderApp.getBzjztDzqk() == "") {
			tdscBidderApp.setBzjztDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		if (StringUtils.isEmpty(tdscBidderApp.getBidderId())) {
			tdscBidderApp.setBidderId(null);
		}

		tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
		String bidderId = (String) tdscBidderApp.getBidderId();

		// String appId = tdscBidderApp.getAppId();
		// TdscBlockTranApp tdscBlockTranApp = this.findTdscBlockTranApp(appId);
		//
		// if (tdscBidderApp.getCertNo() == null) {
		// String certNo = getCertNo(tdscBlockTranApp.getBlockNoticeNo());
		// tdscBidderApp.setCertNo(certNo);
		// }

		tdscBidderApp.setReviewResult(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
		tdscBidderApp.setReviewOpnn("");

		// ���� tdscBidderPersonApp ����� bidderPersonId
		tdscBidderPersonApp.setBidderId(bidderId);
		if (tdscBidderPersonApp.getBzjDzqk() == null || tdscBidderPersonApp.getBzjDzqk() == "") {
			tdscBidderPersonApp.setBzjDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		if (StringUtils.isEmpty(tdscBidderPersonApp.getBidderPersonId())) {
			tdscBidderPersonApp.setBidderPersonId(null);
		}
		tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.saveOrUpdate(tdscBidderPersonApp);
		String bidderPersonId = (String) tdscBidderPersonApp.getBidderPersonId();
		// ���� tdscBidderMaterial
		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(i);
				tdscBidderMaterial.setBidderId(bidderId);
				tdscBidderMaterial.setBidderPersonId(bidderPersonId);
				tdscBidderMaterialDao.save(tdscBidderMaterial);
			}
		}
		// �޸� Tdsc_Bidder_Provide ���в��ϱ��ΪprovideBm�� ifApp ֵΪ ��1��
		// if (provideBm != null) {
		// List provideList = (List) tdscBidderProvideDao
		// .getProByProvideBm(provideBm);
		// if (provideList != null && provideList.size() > 0) {
		// TdscBidderProvide tdscBidderProvide = (TdscBidderProvide) provideList
		// .get(0);
		// tdscBidderProvide.setIfApp("1");
		// tdscBidderProvide.setAppDate(new Date(System
		// .currentTimeMillis()));
		// tdscBidderProvideDao.update(tdscBidderProvide);
		// }
		// }
		return bidderId;
	}

	/**
	 * ����blockNpticeNo�����ʸ�֤����
	 * 
	 * @param appId
	 * @return
	 */
	public String getCertNo(String blockNpticeNo) {
		// ���һ��Ԥ�����ʸ�֤����
		String certNo = creatCertNo(blockNpticeNo);
		// ����Ƿ�Ψһ
		while (checkCertNo(certNo) == false) {
			certNo = creatCertNo(blockNpticeNo);
		}

		return certNo;
	}

	public String generateCertNo() {
		String nowMonth = DateUtil.date2String(new java.util.Date(), "yyyyMM");
		Long certNo = idSpringManager.getIncrementId("CertNo" + nowMonth);
		return nowMonth + StringUtils.leftPad(certNo + "", 4, '0');
	}

	/**
	 * Ԥ�����ʸ�֤����
	 * 
	 * @param blockNpticeNo
	 * @return
	 */
	private String creatCertNo(String blockNpticeNo) {
		// Ԥ���õؿ鹫���
		if (blockNpticeNo == null) {
			blockNpticeNo = "000000000";
		} else {
			blockNpticeNo = blockNpticeNo.substring(3, 7) + blockNpticeNo.substring(8);
		}
		// ��ˮ��
		String tempCertNo = "000" + idSpringManager.getIncrementId("CertNo" + blockNpticeNo);
		// Ԥ�����ʸ�֤����
		String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
		return certNo;
	}

	/**
	 * �ж�ĳ���ʸ�֤�����Ƿ��Ѿ�����
	 * 
	 * @param certNo
	 * @return
	 */
	private boolean checkCertNo(String certNo) {
		List certList = (List) tdscBidderAppDao.getCretNo(certNo);
		if (certList != null && certList.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * �������-��������-�޸�
	 * 
	 * @param oneBidApp
	 * @param tdscBidderApp
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 */
	public String updateBidder(List oneBidApp, TdscBidderApp tdscBidderApp, TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		/**
		 * // ���ԭ���� ������ String acceptNo = (String) tdscBidderApp.getAcceptNo(); // ���ԭ���ı�֤�����嵽�����,������ ,�ʸ�֤���� �����µ�ʵ�� ; TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao
		 * .findOneBidderByBidderId(tdscBidderApp.getBidderId()); if (bidderApp != null) { tdscBidderApp.setBidderId(bidderApp.getBidderId()); if (bidderApp.getAppId() != null &&
		 * bidderApp.getAppId() != "") { tdscBidderApp.setAppId(bidderApp.getAppId()); } if (bidderApp.getBzjztDzqk() != null && bidderApp.getBzjztDzqk() != "") {
		 * tdscBidderApp.setBzjztDzqk(bidderApp.getBzjztDzqk()); } if (bidderApp.getReviewResult() != null && bidderApp.getReviewResult() != "") {
		 * tdscBidderApp.setReviewResult(bidderApp.getReviewResult()); } if (bidderApp.getCertNo() != null && bidderApp.getCertNo() != "") {
		 * tdscBidderApp.setCertNo(bidderApp.getCertNo()); } if (bidderApp.getAppUserId() != null && bidderApp.getAppUserId() != "") {
		 * tdscBidderApp.setAppUserId(bidderApp.getAppUserId()); } } // ���ԭ���ı�֤����ʱ�估�䵽����� TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao
		 * .getBidderByBidderId(tdscBidderApp.getBidderId());
		 * 
		 * if (personApp != null) { // ��ֵ���µ�ʵ����� tdscBidderPersonApp .setBidderPersonId(personApp.getBidderPersonId()); if (personApp.getBzjDzqk() != null && personApp.getBzjDzqk()
		 * != "") { tdscBidderPersonApp.setBzjDzqk(personApp.getBzjDzqk()); } if (personApp.getBzjDzsj() != null) { tdscBidderPersonApp.setBzjDzsj(personApp.getBzjDzsj()); } if
		 * (personApp.getBzjDzse() != null) { tdscBidderPersonApp.setBzjDzse(personApp.getBzjDzse()); } } tdscBidderPersonApp.setBidderId(bidderApp.getBidderId());
		 **/
		// 20080325-15:40�޸�
		// delOneBidder(oneBidApp);
		// addBidder(null,acceptNo, tdscBidderApp, tdscBidderPersonApp,
		// tdscBidMatList);
		// ɾ��ԭ�еľ������ύ�Ĳ�����Ϣ
		TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
		List bidMatList = (List) oneBidApp.get(2);
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
		/*
		 * tdscBidderAppDao.delete(bidderApp); tdscBidderPersonAppDao.delete(personApp); TdscBidderApp retBidderApp=(TdscBidderApp)tdscBidderAppDao.save(tdscBidderApp);
		 * tdscBidderPersonAppDao.save(tdscBidderPersonApp);
		 */
		tdscBidderAppDao.update(tdscBidderApp);
		tdscBidderPersonAppDao.update(tdscBidderPersonApp);
		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(i);
				tdscBidderMaterial.setBidderId(tdscBidderApp.getBidderId());
				tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidderMaterialDao.save(tdscBidderMaterial);
			}
		}
		/**
		 * if (tdscBidMatList != null && tdscBidMatList.size() > 0) { for (int i = 0; i < tdscBidMatList.size(); i++) { tdscBidderMaterial = (TdscBidderMaterial)
		 * tdscBidMatList.get(i); tdscBidderMaterial.setBidderId(bidderApp.getBidderId()); tdscBidderMaterial.setBidderPersonId(personApp.getBidderPersonId());
		 * tdscBidderMaterialDao.save(tdscBidderMaterial); } }
		 **/
		return tdscBidderApp.getBidderId();
	}

	/**
	 * ���Ͼ���--��Ӿ�����
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public String addUnionBidder(String provideBm, TdscBidderApp tdscBidderApp, List tdscBidPerList, List tdscBidMatList) {
		// ���� tdscBidderApp ����� bidderId
		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));

		// ���� ������
		if (tdscBidderApp.getAcceptNo() == null) {
			String acceptNo = createAcceptNo(tdscBidderApp.getAppId());
			tdscBidderApp.setAcceptNo(acceptNo);
		}
		// ���þ�������Ϊ ���Ͼ���
		tdscBidderApp.setBidderType("2");
		// ���õ������Ϊ����ʷ����
		if (tdscBidderApp.getBzjztDzqk() == null || tdscBidderApp.getBzjztDzqk() == "") {
			tdscBidderApp.setBzjztDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.save(tdscBidderApp);
		String bidderId = (String) tdscBidderApp.getBidderId();

		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				// �޸� tdscBidderPersonApp ���� bidderId
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				String bidderPersonId = tdscBidderPersonApp.getBidderPersonId();
				TdscBidderPersonApp newBidderPersonApp = new TdscBidderPersonApp();

				try {
					newBidderPersonApp = (TdscBidderPersonApp) BeanUtils.cloneBean(tdscBidderPersonApp);
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				newBidderPersonApp.setBidderPersonId(null);
				newBidderPersonApp.setBidderId(bidderId);
				newBidderPersonApp.setBzjDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
				tdscBidderPersonAppDao.save(newBidderPersonApp);

				// �޸� ÿ���������ύ�Ĳ��ϵ� bidderId �� BidderPersonId
				// List tdscBidMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(bidderPersonId);
				if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
					for (int j = 0; j < tdscBidMatList.size(); j++) {
						TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(j);
						TdscBidderMaterial newBidderMaterial = new TdscBidderMaterial();

						try {
							newBidderMaterial = (TdscBidderMaterial) BeanUtils.cloneBean(tdscBidderMaterial);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						newBidderMaterial.setBidderMaterialId(null);
						newBidderMaterial.setBidderId(bidderId);
						newBidderMaterial.setBidderPersonId(newBidderPersonApp.getBidderPersonId());

						tdscBidderMaterialDao.save(newBidderMaterial);
					}
				}
			}
		}
		// if (provideBm != null) {
		// List provideList = (List) tdscBidderProvideDao
		// .getProByProvideBm(provideBm);
		// if (provideList != null && provideList.size() > 0) {
		// TdscBidderProvide tdscBidderProvide = (TdscBidderProvide) provideList
		// .get(0);
		// tdscBidderProvide.setIfApp("1");
		// tdscBidderProvide.setAppDate(new Date(System
		// .currentTimeMillis()));
		// tdscBidderProvideDao.update(tdscBidderProvide);
		// }
		// }

		return bidderId;
	}

	/**
	 * ���Ͼ���--delLJBidderPer
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public void delLJBidderPer(List tdscBidPerList) {
		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				String bidderPersonId = tdscBidderPersonApp.getBidderPersonId();

				// ɾ��ÿ���������ύ�Ĳ����б�
				List tdscBidMatList = (List) tdscBidderMaterialDao.getBidderWithOutBidderId(bidderPersonId);
				if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
					for (int j = 0; j < tdscBidMatList.size(); j++) {
						TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(j);
						tdscBidderMaterialDao.delete(tdscBidderMaterial);
					}
				}
				// ɾ��tdscBidderPersonApp
				tdscBidderPersonAppDao.delete(tdscBidderPersonApp);
			}
		}
	}

	/**
	 * ���Ͼ���--��Ӿ�����
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public String addExistUnionBidder(TdscBidderApp tdscBidderApp, List tdscBidPerList) {
		// ���� tdscBidderApp ����� bidderId
		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
		// ���� ������
		if (tdscBidderApp.getAcceptNo() == null) {
			String acceptNo = createAcceptNo(tdscBidderApp.getAppId());
			tdscBidderApp.setAcceptNo(acceptNo);
		}
		// ���þ�������Ϊ ���Ͼ���
		tdscBidderApp.setBidderType("2");
		// ���õ������Ϊ����ʷ����
		if (tdscBidderApp.getBzjztDzqk() == null || tdscBidderApp.getBzjztDzqk() == "") {
			tdscBidderApp.setBzjztDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		tdscBidderApp.setBidderId("");
		tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.save(tdscBidderApp);
		String newBidderId = (String) tdscBidderApp.getBidderId();
		List newBidMatList = new ArrayList();
		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				// �޸� tdscBidderPersonApp ���� bidderId
				if (newBidMatList != null || newBidMatList.size() > 0) {
					newBidMatList = new ArrayList();
				}
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				String existBidderPersonId = tdscBidderPersonApp.getBidderPersonId();
				tdscBidderPersonApp.setBidderId(newBidderId);
				tdscBidderPersonApp.setBzjDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
				tdscBidderPersonApp.setBidderPersonId("");
				tdscBidderPersonAppDao.save(tdscBidderPersonApp);
				String newBidderPersonId = tdscBidderPersonApp.getBidderPersonId();

				// �޸� ���о������ύ�Ĳ��ϵ� bidderId��BidderPersonId,�õ�����������list
				List tdscBidMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(existBidderPersonId);
				if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
					for (int j = 0; j < tdscBidMatList.size(); j++) {
						TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(j);
						TdscBidderMaterial newMaterial = new TdscBidderMaterial();
						try {
							BeanUtils.copyProperties(newMaterial, tdscBidderMaterial);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {

							e.printStackTrace();
						}
						newMaterial.setBidderId(newBidderId);
						newMaterial.setBidderPersonId(newBidderPersonId);
						newMaterial.setBidderMaterialId("");
						newMaterial = (TdscBidderMaterial) tdscBidderMaterialDao.save(newMaterial);
					}
				}
			}
		}
		return newBidderId;
	}

	/**
	 * ���Ͼ���--�޸ľ�����--����
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public void updateUnionBidder(TdscBidderApp tdscBidderApp, List tdscBidPerList) {
		if (tdscBidderApp.getAcceptDate() == null) {
			tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
		}

		tdscBidderAppDao.update(tdscBidderApp);
		String newBidId = (String) tdscBidderApp.getBidderId();

		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				// �޸� tdscBidderPersonApp ���� bidderId
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				tdscBidderPersonApp.setBidderId(newBidId);
				tdscBidderPersonAppDao.update(tdscBidderPersonApp);

				// �޸� ÿ���������ύ�Ĳ��ϵ� bidderId �� BidderPersonId
				List tdscBidMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(tdscBidderPersonApp.getBidderPersonId());
				if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
					for (int j = 0; j < tdscBidMatList.size(); j++) {
						TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(j);
						tdscBidderMaterial.setBidderId(newBidId);
						tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
						tdscBidderMaterialDao.update(tdscBidderMaterial);
					}
				}
			}
		}
	}

	/**
	 * ���������� AcceptNo
	 * 
	 * @param bidderId
	 * @return
	 */
	public String createAcceptNo(String appId) {

		// �ؿ鹫���
		String blockNoticeNo = "000000000";
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
		if (tdscBlockTranApp.getBlockNoticeNo() != null) {
			String tempBlockNoticeNo = "000000000" + (String) tdscBlockTranApp.getBlockNoticeNo();
			blockNoticeNo = tempBlockNoticeNo.substring(tempBlockNoticeNo.length() - 8, tempBlockNoticeNo.length() - 4)
					+ tempBlockNoticeNo.substring(tempBlockNoticeNo.length() - 3);
		}

		// ������ = �ؿ鹫���+3λ��ˮ��
		String tmpId = "000" + idSpringManager.getIncrementId(blockNoticeNo);
		String acceptNo = blockNoticeNo + tmpId.substring(tmpId.length() - 3);
		/*
		 * int tempNum = 001; List tempList = (List)tdscBidderAppDao.getMaxByQueryNo(blockNoticeNo); if(tempList!=null&&tempList.size()>0){ TdscBidderApp
		 * tdscBidderApp=(TdscBidderApp)tempList.get(0); //String acceptNoEnd3=(String)tdscBidderApp.getAcceptNo().substring(tdscBidderApp.getAcceptNo().length()-3);
		 * tempNum=Integer.parseInt(tdscBidderApp.getAcceptNo().substring(tdscBidderApp.getAcceptNo().length()-3))+1; } String stringTempNum = "000"+tempNum;
		 * 
		 * String acceptNo = blockNoticeNo+stringTempNum.substring(stringTempNum.length()-3);
		 */
		return acceptNo;
	}

	/**
	 * ���� bidderId���������Ϣ
	 * 
	 * @param bidderId
	 * @return
	 */
	public List queryByBidderId(String bidderId) {
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.findOneBidderByBidderId(bidderId);
		List bidPerAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
		List bidMatList = (List) tdscBidderMaterialDao.getOneBidByBidderId(bidderId);
		List retList = new ArrayList();

		retList.add(tdscBidderApp);
		retList.add(bidPerAppList);
		retList.add(bidMatList);
		return retList;
	}// queryPerMatByBidderId

	/**
	 * ɾ�� һ��������Ϣ
	 * 
	 * @param bidderList
	 */
	public void delOneBidder(List bidderList) {
		TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderList.get(0);
		List bidPerAppList = (List) bidderList.get(1);
		List bidMatList = (List) bidderList.get(2);
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
		if (bidPerAppList != null && bidPerAppList.size() > 0) {
			for (int j = 0; j < bidPerAppList.size(); j++) {
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidPerAppList.get(j);
				tdscBidderPersonAppDao.delete(tdscBidderPersonApp);
			}
		}
		tdscBidderAppDao.delete(tdscBidderApp);

	}

	/**
	 * ����bidderIdɾ��������Ϣ
	 * 
	 * @param bidderId
	 */
	public void delOneBidderByBidderId(String bidderId) {
		List bidMatList = (List) tdscBidderMaterialDao.getOneBidByBidderId(bidderId);
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
		List bidPerAppList = (List) tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
		if (bidPerAppList != null && bidPerAppList.size() > 0) {
			for (int j = 0; j < bidPerAppList.size(); j++) {
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidPerAppList.get(j);
				tdscBidderPersonAppDao.delete(tdscBidderPersonApp);
			}
		}
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
		if (tdscBidderApp != null) {
			tdscBidderAppDao.delete(tdscBidderApp);
		}
	}

	/**
	 * ����bidderIdɾ��������������Ϣ
	 * 
	 * @param bidderId
	 */
	public void delOneBidderMaterialByBidderId(String bidderId) {
		List bidMatList = (List) tdscBidderMaterialDao.getOneBidByBidderId(bidderId);
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
	}

	/**
	 * �޸�һ��ͨ�Ŀ���������
	 * 
	 * @param bidderApp
	 */
	public void modBidYktMm(TdscBidderApp oldBidderApp, String newYktBh, String newYktXh) {

		tdscBidderAppDao.update(oldBidderApp);

	}

	public TdscBidderApp getTdscBidderAppByBidderId(String bidderId) {
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
		return tdscBidderApp;
	}

	/**
	 * ����provideBm�������������Ƿ����
	 * 
	 * @param provideBm
	 * @return
	 */
	public List getProByProvideBm(String provideBm) {
		List retList = (List) tdscBidderProvideDao.getProByProvideBm(provideBm);
		return retList;
	}

	/**
	 * У�� һ��ͨ����Ƿ��Ѿ�ʹ�ù�
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktXh(String yktXh) {
		List retList = (List) tdscBidderAppDao.checkIfUsedYktXh(yktXh);
		return retList;
	}

	/**
	 * У�� һ��ͨ�����Ƿ��Ѿ�ʹ�ù�
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktBh(String yktBh) {
		List retList = (List) tdscBidderAppDao.checkIfUsedYktBh(yktBh);
		return retList;
	}

	/**
	 * ******************************add by huangjiawei **************************************************************
	 */
	/**
	 * ͨ��bidderId ��þ����˵���Ϣ
	 * 
	 * @param bidderId
	 * @return List
	 */
	public List queryBidderPersonList(String bidderId) {
		List list = tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
		return list;
	}

	public TdscBidderPersonApp queryBidPerson(String bidderId) {
		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
		return tdscBidderPersonApp;
	}

	/**
	 * ͨ��appId ������вμӵؿ����Ĺҵľ�����Ϣ�б�
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderAppList(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.findBidderAppListByAppId(appId);
		return tdscBidderAppList;
	}

	/**
	 * ͨ��appId �������ͨ������ľ�����Ϣ�б�
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderListSrc(appId);
		return tdscBidderAppList;
	}

	/**
	 * ͨ��appId����Ѿ����ع��ʸ�֤��ľ�������Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListDownloadZgzs(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderListDownloadZgzs(appId);
		return tdscBidderAppList;
	}

	/**
	 * ͨ�����׿���Ż��һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktXh(String yktXh) {
		return tdscBidderAppDao.getBidderAppByYktXh(yktXh);
	}

	/**
	 * ͨ���ʸ�֤���Ż��һ��������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByCertNo(String certNo) {
		return tdscBidderAppDao.getBidderAppByCertNo(certNo);
	}

	/**
	 * ͨ��appId�ͺ��ƺ�����һ��������Ϣ
	 * 
	 * @param cardNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdConNo(String appId, String conNo) {
		return tdscBidderAppDao.getBidderAppByAppIdConNo(appId, conNo);
	}

	/**
	 * ��ѯ���׿���Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public String findyktBh(String appId, String yktBh) {
		String YktBh = tdscBidderAppDao.findyktBh(appId, yktBh);
		return YktBh;
	}

	/**
	 * ͨ��appId��һ��ͨ��Ż��һ��������Ϣ
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdYktXh(String appId, String yktXh) {
		return tdscBidderAppDao.getBidderAppByAppIdYktXh(appId, yktXh);
	}

	/**
	 * ͨ��appId���ʸ�֤���Ż��һ��������Ϣ
	 * 
	 * @param appId
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdCertNo(String appId, String certNo) {
		return tdscBidderAppDao.getBidderAppByAppIdCertNo(appId, certNo);
	}

	/**
	 * ͨ��bidderId��һ��ͨ��Ż��һ��������Ϣ
	 * 
	 * @param bidderId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktXh(String bidderId, String yktXh) {
		return tdscBidderAppDao.getBidderAppByBidderIdYktXh(bidderId, yktXh);
	}

	/**
	 * ͨ��appId��һ��ͨ���Ż��һ��������Ϣ
	 * 
	 * @param bidderId
	 * @param yktBh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktBh(String bidderId, String yktBh) {
		return tdscBidderAppDao.getBidderAppByBidderIdYktBh(bidderId, yktBh);
	}

	/**
	 * ͨ��appId��tenderId���һ��������Ϣ
	 * 
	 * @param appId
	 * @param tenderId
	 * @return
	 */
	public TdscBidderApp queryTenderInfoByAppIdTenderId(String appId, String tenderNo) {
		return tdscBidderAppDao.getBidderAppByAppIdTenderId(appId, tenderNo);
	}

	/**
	 * �޸ľ���������Ϣ��
	 * 
	 * @param tdscBidderApp
	 */
	public void updateTdscBidderApp(TdscBidderApp tdscBidderApp) {
		this.tdscBidderAppDao.update(tdscBidderApp);
	}

	/**
	 * ******************************add by huangjiawei **************************************************************
	 */

	/**
	 * ���һ���������ڱ��е�����
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public String queryBidderId(String appId, String yktXh) {
		String bidderId = this.tdscBidderAppDao.findBidderId(appId, yktXh);
		return bidderId;
	}

	/**
	 * ����yktBh���һ���������ڱ��е�����
	 * 
	 * @param appId
	 * @param yktBh
	 * @return
	 */
	public String queryBidderIdByYktBh(String appId, String yktBh) {
		String bidderId = this.tdscBidderAppDao.findBidderIdByYktBh(appId, yktBh);
		return bidderId;
	}

	/**
	 * �����������һ����Ӧ�ļ�¼
	 * 
	 * @param bidderId
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfo(String bidderId) {
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
		return tdscBidderApp;
	}

	/**
	 * ȡ�þ����˵�ȫ����¼
	 * 
	 * @return
	 */
	public List findPurchaserList() {
		List list = this.tdscBidderAppDao.findAll();
		return list;
	}

	/**
	 * ����AppId��ѯ������
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTenderNoList(String appId) {
		List tenderNoList = this.tdscBidderAppDao.findTenderNoListByAppId(appId);
		return tenderNoList;
	}

	public String queryCertNoByTenderNo(String tenderNo) {
		String certNo = this.tdscBidderAppDao.findCertNoByTenderNo(tenderNo);
		return certNo;
	}

	/**
	 * ͨ�����׿����Ż��һ��������Ϣ
	 * 
	 * @param yktBh
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktBh(String yktBh) {
		return tdscBidderAppDao.getBidderAppByYktBh(yktBh);
	}

	/**
	 * ͨ�����׿����Ż�����о�����Ϣ�б�
	 * 
	 * @param yktBh
	 * @return
	 */
	public List getBidderAppListByYktBh(String yktBh) {
		return tdscBidderAppDao.checkIfUsedYktBh(yktBh);
	}

	/**
	 * ����appIdȡ�þ����˵�ȫ����¼
	 * 
	 * @return
	 */
	public List findPurchaserListByAppId(String appId) {
		List list = this.tdscBidderAppDao.findBidderAppListByAppId(appId);
		return list;
	}

	/**
	 * ����appid�ж�һ��������һ���ؿ���ֳ����ۻ����Ƿ��ظ�ʹ��
	 * 
	 * @param appId
	 * @return
	 */
	public String checkConnumByAppId(String appId, String conNum) {
		String temp = tdscBidderAppDao.checkConnumByAppId(appId, conNum);
		return temp;
	}

	/**
	 * ����certNo�ж�һ��������һ���ؿ���ֳ����ۻ����Ƿ��ظ�ʹ��
	 * 
	 * @param appId
	 * @return
	 */
	public String checkConnumByCertNo(String appId, String conNum) {
		String temp = tdscBidderAppDao.checkConnumBycertNo(appId, conNum);
		return temp;
	}

	/**
	 * У��������������Ƿ����
	 * 
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public List checkCertNoByAppId(String appId, String certNo) {
		return tdscBidderAppDao.checkCertNoByAppId(appId, certNo);
	}

	public List checkIfUsedAcceptNo(String acceptNo) {
		List retList = (List) tdscBidderAppDao.getProByAcceptNo(acceptNo);
		return retList;
	}

	public List checkIfUsedCertNo(String certNo) {
		List retList = (List) tdscBidderAppDao.getProByCertNo(certNo);
		return retList;
	}

	/**
	 * ����block_id��ѯ�ӵؿ���Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		return tdscBlockPartDao.getTdscBlockPartList(blockId);
	}

	/**
	 * ����block_id��ѯBlockCode�ֶβ��ظ����ӵؿ���Ϣ����ʱ����ȡ��ĳ���ؿ�����в��ظ��ؿ���BlockCode
	 * 
	 * @param blockId
	 * @return
	 */
	public String getTdscBlockPartListInBlockCode(String blockId) {
		List tdscBlockPartList = (List) tdscBlockPartDao.getTdscBlockPartListInBlockCode(blockId);

		String blockCode = "";
		if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
			for (int i = 0; i < tdscBlockPartList.size(); i++) {
				blockCode += tdscBlockPartList.get(i) + "��";
			}
			if (!"".equals(blockCode) && blockCode.length() > 0)
				blockCode = blockCode.substring(0, blockCode.length() - 1);
		}

		return blockCode;
	}

	/**
	 * ��ѯ�ʸ�������б�
	 * 
	 * @return
	 */
	public PageList queryAppViewList(TdscBaseQueryCondition condition) {
		return tdscBidderAppDao.queryAppViewList(condition);
	}

	/**
	 * ����bidderId�����������ƣ���֤��������
	 * 
	 * @param bidderId
	 * @return TdscBidderPersonApp
	 */
	public TdscBidderPersonApp tidyBidderByBidderId(String bidderId) {
		// ����bidderId��ѯ���еľ�����
		List bidderPersonList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		if (bidderPersonList != null && bidderPersonList.size() > 0) {
			StringBuffer personNames = new StringBuffer("");
			BigDecimal bzjDzse = new BigDecimal(0.00);
			for (int i = 0; i < bidderPersonList.size(); i++) {
				TdscBidderPersonApp tempApp = (TdscBidderPersonApp) bidderPersonList.get(i);
				if (i < bidderPersonList.size() - 1) {
					personNames.append(tempApp.getBidderName()).append("��");
				} else {
					personNames.append(tempApp.getBidderName());
				}
				bzjDzse = bzjDzse.add(tempApp.getBzjDzse() == null ? new BigDecimal(0) : tempApp.getBzjDzse());
			}
			tdscBidderPersonApp.setBzjDzse(bzjDzse == null ? new BigDecimal(0) : bzjDzse);
			tdscBidderPersonApp.setMemo(personNames.toString());
		}
		return tdscBidderPersonApp;
	}

	/**
	 * �����������һҳ��ykt�б�Action��
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * @return PageList����
	 */
	public PageList findAllYktPageList(TdscBidderCondition condition) {
		return tdscBidderAppDao.findListByCondition(condition);
	}

	/**
	 * ����appIdListȡ�����в��ظ��Ĺ���id�б�
	 * 
	 * @param appIdList
	 * @return
	 */
	public List findNoticeIdListByAppIdList(List appIdList) {
		List retList = (List) tdscBidderAppDao.findNoticeIdListByAppIdList(appIdList);
		return retList;
	}

	/**
	 * ��������ȡ�ù����б�
	 * 
	 * @param appIdList
	 * @return
	 */
	public List findNoticeListByCondition(TdscNoticeAppCondition condition) {
		List retList = (List) tdscNoticeAppDao.findNoticeAppList(condition);
		return retList;
	}

	public List getBidderIdListByBidderName(String bidderName) {
		List retList = (List) tdscBidderPersonAppDao.getBidderIdListByBidderName(bidderName);
		return retList;
	}

	public List tidyBidderAppList(TdscBidderCondition condition) {

		List yktBhList = (List) tdscBidderAppDao.queryYktBhList(condition);

		if (yktBhList != null && yktBhList.size() > 0) {
			List retList = new ArrayList();
			// �����ѯcondition
			TdscBidderCondition temp_condition = new TdscBidderCondition();
			// ���쾺����LIST
			List bidderAppList = new ArrayList();
			// ����������Ϣ
			for (int i = 0; i < yktBhList.size(); i++) {
				// ����bidderId��ѯ��������Ϣ
				TdscBidderApp tdscBidderApp = new TdscBidderApp();
				// У�齻�׿�����Ƿ�Ϊ��
				if (StringUtils.isNotEmpty((String) yktBhList.get(i))) {
					tdscBidderApp.setYktBh(StringUtils.trimToEmpty((String) yktBhList.get(i)));
					// ���ò�ѯ����
					temp_condition.setYktBh(StringUtils.trimToEmpty((String) yktBhList.get(i)));
					// ���bidderAppList
					if (bidderAppList != null)
						bidderAppList.clear();
					// ��ѯ��������Ϣ
					bidderAppList = tdscBidderAppDao.queryBidderAppListByCondition(temp_condition);
					if (bidderAppList != null && bidderAppList.size() > 0) {
						TdscBidderApp tempApp = (TdscBidderApp) bidderAppList.get(0);

						tdscBidderApp.setYktXh(StringUtils.trimToEmpty(tempApp.getYktXh()));
						tdscBidderApp.setBidderId(tempApp.getBidderId());
						tdscBidderApp.setAcceptNo(StringUtils.trimToEmpty(tempApp.getAcceptNo()));
						tdscBidderApp.setCertNo(StringUtils.trimToEmpty(tempApp.getCertNo()));
						TdscBidderPersonApp tempPersonApp = (TdscBidderPersonApp) tidyBidderByBidderId(tempApp.getBidderId());
						tdscBidderApp.setMemo(tempPersonApp.getMemo());
					}
				}
				if (StringUtils.isNotEmpty(tdscBidderApp.getBidderId())) {
					retList.add(tdscBidderApp);
				}
			}
			return retList;
		}
		return null;
	}

	public void updateBidderNoticeId(String noticeId) {
		List list = this.findAppIdsByNoticeId(noticeId);
		for (int i = 0; i < list.size(); i++) {
			TdscBlockTranApp blockTranApp = (TdscBlockTranApp) list.get(i);
			TdscBidderApp getYixiang = getYixiangPerson(blockTranApp.getAppId());
			if (null != getYixiang && StringUtils.isEmpty(getYixiang.getNoticeId()))
				getYixiang.setNoticeId(noticeId);
		}
	}

	public TdscBidderApp getYixiangPerson(String appId) {
		return this.tdscBidderAppDao.findYixiangBidderAppByAppId(appId);
	}

	public TdscBidderApp getYixiangPersonLikeAppId(String appId) {
		return this.tdscBidderAppDao.findYixiangPersonLikeAppId(appId);
	}

	public List findAppIdsByNoticeId(String noticeId) {
		List list = this.tdscBlockTranAppDao.findAppListByNoticeId(noticeId);
		return list;
	}

	public List findAppByNoticeId(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderAppByAppId(noticeId);
		return list;
	}

	public List queryBidderAppYixiangList(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderYixiangList(noticeId);
		return list;
	}

	public TdscBlockTranApp getTdscBlockTranAppById(String appId) {
		return (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
	}

	public TdscBidderPersonApp getTdscBidderPersonByBidderId(String bidderId) {
		return (TdscBidderPersonApp) this.tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
	}

	public void updateTdscBidderAppList(List tdscBidderAppList) {
		for (int i = 0; tdscBidderAppList != null && i < tdscBidderAppList.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) tdscBidderAppList.get(i);
			this.updateTdscBidderApp(app);
		}
	}

	public List findTdscBidderListByBidderName(String bidderName) {
		if (StringUtils.isEmpty(bidderName))
			return null;
		TdscBidderCondition condition = new TdscBidderCondition();
		condition.setBidderName(bidderName);
		List list = this.tdscBidderPersonAppDao.findTdscBidderPersonAppListByCondition(condition);
		List resultList = null;
		if (list != null && list.size() > 0) {
			resultList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				TdscBidderPersonApp personApp = (TdscBidderPersonApp) list.get(i);
				TdscBidderApp tdscBidderApp = this.tdscBidderAppDao.findOneBidderByBidderId(personApp.getBidderId());
				tdscBidderApp.setAloneTdscBidderPersonApp(personApp);
				resultList.add(tdscBidderApp);
			}
		}
		return resultList;
	}

	public String updateJmrInfo(String appId, boolean isPurposePerson, TdscBidderForm bidderForm, List bidderMatList, SysUser user) {
		// 1. ���澺��ĵؿ�, ��֤�� TDSC_BIDDER_PERSON_APP
		// 2. ������������Ϣ TDSC_BIDDER_APP
		// 2.1 ����ת������Ϣ TDSC_BIDDER_APP
		// 3. ���淨����Ϣ TDSC_BIDDER_PERSON_APP
		// 4. ���潻�׿�,�ʸ�֤���� ��Ϣ TDSC_BIDDER_APP
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		try {
			BeanUtils.copyProperties(bidderApp, bidderForm);
			BeanUtils.copyProperties(bidderPersonApp, bidderForm);

			if (isPurposePerson)
				bidderApp.setIsPurposePerson("1");
			else
				bidderApp.setIsPurposePerson("0");

			// ���ڲ�ѯ�ؿ龺����ʹ��
			// if (seq == 0)
			bidderApp.setIfDownloadZgzs("1");

			bidderApp.setBidderId(bidderForm.getBidderId());
			bidderApp.setAppId(appId);
			bidderApp.setIfCommit("1");
			bidderApp.setUserId(user.getUserId());
			tdscBidderAppDao.saveOrUpdate(bidderApp);

			// if (seq == 0) {
			bidderPersonApp.setBidderId(bidderApp.getBidderId());
			// bidderPersonApp.setBzjDzse(bzj);
			// bidderPersonApp.setPurposeAppId(purposeAppId);
			tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);

			// �������ݿ��е�������ɾ������� TDSC_BIDDER_MATERIAL
			delOneBidderMaterialByBidderId(bidderApp.getBidderId());
			if (bidderMatList != null && bidderMatList.size() > 0) {
				for (int i = 0; i < bidderMatList.size(); i++) {
					TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidderMatList.get(i);
					tdscBidderMaterial.setBidderId(bidderApp.getBidderId());
					tdscBidderMaterial.setBidderPersonId(bidderPersonApp.getBidderPersonId());
					tdscBidderMaterialDao.save(tdscBidderMaterial);
				}
			}
			// }

			return bidderApp.getBidderId();

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	public String saveJmrInfo(String appId, boolean isPurposePerson, TdscBidderForm bidderForm, List bidderMatList, SysUser user) {
		// 1. ���澺��ĵؿ�, ��֤�� TDSC_BIDDER_PERSON_APP
		// 2. ������������Ϣ TDSC_BIDDER_APP
		// 2.1 ����ת������Ϣ TDSC_BIDDER_APP
		// 3. ���淨����Ϣ TDSC_BIDDER_PERSON_APP
		// 4. ���潻�׿�,�ʸ�֤���� ��Ϣ TDSC_BIDDER_APP
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		try {
			BeanUtils.copyProperties(bidderApp, bidderForm);
			BeanUtils.copyProperties(bidderPersonApp, bidderForm);
			if (isPurposePerson)
				bidderApp.setIsPurposePerson("1");
			else
				bidderApp.setIsPurposePerson("0");
			// ���ڲ�ѯ�ؿ龺����ʹ��
			// if (seq == 0)
			bidderApp.setIfDownloadZgzs("1");

			bidderApp.setAppId(appId);
			bidderApp.setIfCommit("1");
			bidderApp.setUserId(user.getUserId());
			tdscBidderAppDao.save(bidderApp);

			// if (seq == 0) {
			bidderPersonApp.setBidderId(bidderApp.getBidderId());
			// bidderPersonApp.setBzjDzse(bzj);
			tdscBidderPersonAppDao.save(bidderPersonApp);

			if (bidderMatList != null && bidderMatList.size() > 0) {
				for (int i = 0; i < bidderMatList.size(); i++) {
					TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidderMatList.get(i);
					tdscBidderMaterial.setBidderId(bidderApp.getBidderId());
					tdscBidderMaterial.setBidderPersonId(bidderPersonApp.getBidderPersonId());
					tdscBidderMaterialDao.save(tdscBidderMaterial);
				}
			}
			// }

			return bidderApp.getBidderId();

		} catch (Exception e) {

			return null;
		}

	}

	public List queryNotDuplicateBidderAppListByNoticeId(String noticeId) {
		return tdscBidderAppDao.queryNotDuplicateBidderAppListByNoticeId(noticeId);
	}

	public List queryNotYixiangBidderAppListByNoticeId(String noticeId) {
		return tdscBidderAppDao.queryNotYixiangBidderAppListByNoticeId(noticeId);
	}

	public TdscBidderApp queryIsYixiangBidderAppList(String noticeId, String appId) {
		return tdscBidderAppDao.queryIsYixiangBidderAppList(noticeId, appId);
	}

	public List queryBidderAppListByCertNo(String certNo) {
		return tdscBidderAppDao.queryBidderAppListByCertNo(certNo);
	}

	public List queryBidderAppListLikeAppId(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderAppListLikeAppId(appId);
		return tdscBidderAppList;
	}

	/**
	 * ���ݾ���λ���׿�оƬ�����ѯ
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByYktBh( String yktXh) {
		return tdscBidderAppDao.getTdscBidderAppByYktBh( yktXh);
	}

	public TdscBidderApp queryBidderAppListLikeAppIdAndCardNo(String appId, String cardNo) {
		return tdscBidderAppDao.queryBidderAppListLikeAppIdAndCardNo(appId, cardNo);
	}

	public boolean delBidderInfo(String bidderId, String bidderPersonId) {
		try {

			if (!StringUtil.isEmpty(bidderId) && !StringUtil.isEmpty(bidderPersonId)) {
				tdscBidderAppDao.deleteById(bidderId);
				tdscBidderPersonAppDao.deleteById(bidderPersonId);
				return true;
			}
		} catch (Exception e) {
			// Nothing To Do.
		}
		return false;

	}

	public void modifyPurposePersonAppIds(String bidderId, String bidderPersonId, String yixiangAppIds) {
		// TDSC_BIDDER_APP.appid = yixiangAppIds TDSC_BIDDER_PERSON_APP.PURPOSE_APP_ID = yixiangAppIds

		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
		tdscBidderApp.setAppId(yixiangAppIds);
		tdscBidderAppDao.saveOrUpdate(tdscBidderApp);

		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(bidderPersonId);
		tdscBidderPersonApp.setPurposeAppId(yixiangAppIds);
		tdscBidderPersonAppDao.save(tdscBidderPersonApp);
	}

	/**
	 * 
	 * @param bidderName
	 *            ����������
	 * @param bidderCardNum
	 *            �����˽��׿�����ˢ��
	 * @return
	 */
	public List queryBidderCredList(String bidderName) {

		List retList = new ArrayList();
		List personList = tdscBidderPersonAppDao.likeTdscBidderPersonAppByBidderName(bidderName);

		// 1.�������
		// 2.������
		// 3.�ʸ�֤����
		// TdscBidderForm

		if (personList != null && personList.size() > 0) {
			for (int i = 0; i < personList.size(); i++) {
				TdscBidderPersonApp personApp = (TdscBidderPersonApp) personList.get(i);

				TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao.get(personApp.getBidderId());
				TdscBidderForm bean = new TdscBidderForm();

				TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(bidderApp.getNoticeId());

				bean.setBlockNoticeNo(tdscNoticeApp.getNoticeNo());
				bean.setBidderName(bidderName);
				bean.setCertNo(bidderApp.getCertNo());

				retList.add(bean);
			}
		}

		return retList;
	}

	public boolean saveSelectHaoPai(String strBidderId, String strConNum) {
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao.get(strBidderId);
		bidderApp.setConNum(strConNum);
		bidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
		tdscBidderAppDao.saveOrUpdate(bidderApp);
		return true;
	}

	public String getNoticeNoByNoticeId(String noticeId) {
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		return tdscNoticeApp.getNoticeNo();
	}

	public String getPasswordByCardNoAndYktXh(String cardNo, String yktXh) {

		return tdscBidderAppDao.getPasswordByCardNoAndYktXh(cardNo, yktXh);
	}

	public List queryBidderAppListLikeAppIdAndUserId(String appId, String userId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderAppListLikeAppIdAndUserId(appId, userId);
		return tdscBidderAppList;
	}

	public void saveReturnBzjInfo(String bidderId, List bailList) {
		//1. ��ɾ��
		List tdscReturnBailList = (List)tdscReturnBailDao.queryTdscReturnBailList(bidderId);
		if(tdscReturnBailList!=null && tdscReturnBailList.size()>0){
			for(int i=0; i<tdscReturnBailList.size(); i++){
				TdscReturnBail tdscReturnBail = (TdscReturnBail)tdscReturnBailList.get(i);
				tdscReturnBailDao.delete(tdscReturnBail);
			}
		}		
		
		//2. ������
		if (bailList != null && bailList.size() > 0)
			for (int i = 0; i < bailList.size(); i++) {
				TdscReturnBail bail = (TdscReturnBail) bailList.get(i);
				bail.setBidderId(bidderId);
				tdscReturnBailDao.saveOrUpdate(bail);
			}

	}

	public List queryTdscReturnBailList(String bidderId) {

		return tdscReturnBailDao.queryTdscReturnBailList(bidderId);
	}

	
	/**
	 * �õ���������Ϣ
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkJmrName(String bidderName) {
		List tdscBidderPersonAppList = (List) tdscBidderPersonAppDao.checkJmrName(bidderName);
		return tdscBidderPersonAppList;
	}
	/**
	 * ���ݾ�����id�õ��������Ƿ�Ϊ��������
	 * 
	 * @param yktXh
	 * @return
	 */
	public List findJmrByBidderId(String bidderId) {
		List tdscBidderAppList = (List) tdscBidderAppDao.findJmrByBidderId(bidderId);
		return tdscBidderAppList;
	}
	
	/**
	 * ����noticeId��ѯTdscBlockAppView�б�
	 * @param noticeId
	 * @return
	 */
	public List queryAppViewListByNoticeId(String noticeId) {
		List tdscNoticeList = tdscNoticeAppDao.queryAppViewListByNoticeId(noticeId);
		return tdscNoticeList;
	}
	
	/**
	 * ���ݾ��������Ʋ�ѯ��������Ϣ��
	 */
	public List findTdscBidderPersonListByBidderName(String bidderName) {
		if (StringUtils.isEmpty(bidderName))
			return null;
		TdscBidderCondition condition = new TdscBidderCondition();
		condition.setBidderName(bidderName);
		List resultList = this.tdscBidderPersonAppDao.findTdscBidderPersonAppListByCondition(condition);
		
		return resultList;
	}
	
	/**
	 * ����������ѯ��������Ϣ
	 */
	public List findTdscBidderPersonAppListByCondition(TdscBidderCondition condition) {
		List resultList = this.tdscBidderPersonAppDao.findTdscBidderPersonAppListByCondition(condition);		
		return resultList;
	}
	/**
	 * ��ȡȡ�þ����ʸ�ľ������б�
	 * @param appId
	 * @return
	 */
	public List findHaveCertNoBidderList(String appId){
		return this.tdscBidderAppDao.findHaveCertNoBidderList(appId);
	}
}
