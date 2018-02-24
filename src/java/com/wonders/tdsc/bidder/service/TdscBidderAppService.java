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
	 * 根据条件获得竞买人列表
	 */
	public List findPageBidderList(String appId) {
		// 获得申请列表
		return tdscBidderAppDao.findPageList(appId);
	}

	/**
	 * 根据appid,appUserId获得竞买人列表
	 */
	public List findPageBidderListByUserId(String appId, String appUserId) {
		// 获得申请列表
		return tdscBidderAppDao.findPageBidderListByUserId(appId, appUserId);
	}

	/**
	 * 根据条件获得竞买人列表 list_jmrgl.jsp（Action）
	 */
	public List findBidderList(TdscBidderCondition condition) {
		// 获得申请列表
		List bidderAppList = (List) tdscBidderAppDao.findPageListByIfCommit(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// 设置返回的List
			List retList = new ArrayList();
			List personAppList = new ArrayList();
			TdscBidderApp tdscBidderApp = new TdscBidderApp();
			for (int i = 0; i < bidderAppList.size(); i++) {
				tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
				// 查询这次申请的人数
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
	 * 根据条件获得竞买人列表
	 */
	public List queryBidderAppListByCondition(TdscBidderCondition condition) {
		// 获得申请列表
		List bidderAppList = (List) tdscBidderAppDao.queryBidderAppListByCondition(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// 设置返回的List
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
				// 查询这次申请的人数
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
		// 获得申请列表
		List bidderAppList = (List) tdscBidderAppDao.findPageList(condition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// 设置返回的List
			List retList = new ArrayList();
			List personAppList = new ArrayList();
			TdscBidderApp tdscBidderApp = new TdscBidderApp();
			for (int i = 0; i < bidderAppList.size(); i++) {
				tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
				// 查询这次申请的人数
				if (personAppList != null && personAppList.size() > 0) {
					tdscBidderApp.setUnionCount(String.valueOf(personAppList.size()));
				}
				retList.add(tdscBidderApp);
			}
			return retList;
		}
		return null;
	}

	// 根据appIdList取得所有竞买人信息列表
	public List findBidderListByAppIdList(List appIdList) {
		// 取得交易卡编号不重复的app
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

				// 获得申请列表
				// List bidderAppList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);

				// 设置返回的List
				List personAppList = new ArrayList();
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.getBidderByBidderId(bidderId);
				personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
				// 拼接联合竞买人姓名,并保存在UnionCount字段中
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

	// 根据noticeId取得所有yktbh不相同的竞买人信息列表
	public List queryBidderAppListByNoticeId(String noticeId) {
		// 取得交易卡编号不重复的yktBh
		List retList = new ArrayList();
		List yktBhList = (List) tdscBidderAppDao.queryBidderAppListByNoticeId(noticeId);
		if (yktBhList != null && yktBhList.size() > 0) {
			for (int j = 0; j < yktBhList.size(); j++) {
				String yktBh = (String) yktBhList.get(j);

				// 设置返回的List
				List personAppList = new ArrayList();
				TdscBidderCondition condition = new TdscBidderCondition();
				condition.setNoticeId(noticeId);
				condition.setYktBh(yktBh);
				List bidderAppList = (List) tdscBidderAppDao.getTdscBidderAppList(condition);
				if (bidderAppList != null && bidderAppList.size() > 0) {
					// 除了主键，都是一样的，随便取一条数据
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(0);
					if (tdscBidderApp != null) {
						personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(tdscBidderApp.getBidderId());
						// 拼接联合竞买人姓名,并保存在UnionCount字段中
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

	// 根据bidderId取得所有竞买人名称
	public String getPersonNameByBidderId(String bidderId) {
		String personName = "";
		if (bidderId != null && !"".equals(bidderId)) {
			List personAppList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
			if (personAppList != null && personAppList.size() > 0) {
				for (int k = 0; k < personAppList.size(); k++) {
					TdscBidderPersonApp tdscBidderPerson = (TdscBidderPersonApp) personAppList.get(k);
					personName += tdscBidderPerson.getBidderName() + "，";
				}
				if (personName != null && personName.length() > 0)
					personName = personName.substring(0, personName.length() - 1);
			}
		}
		return personName;
	}

	/**
	 * 根据appId查出该条土地信息
	 * 
	 * @param appId
	 * @return
	 */
	public TdscBlockTranApp findTdscBlockTranApp(String appId) {
		return tdscBlockTranAppDao.getMarginEndDate(appId);
	}

	/**
	 * 联合竞买中添加一个竞买人
	 * 
	 * @param tdscBidderPersonApp
	 *            （Action）
	 * @param tdscBidMatList
	 * @return
	 */
	public TdscBidderPersonApp addOneBidderPerson(TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// 增加竞买人时候 设置他的到账情况 为无操作；
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
	 * 通过appId 获得所有参加地块申请的条数
	 * 
	 * @param appId
	 * @return
	 */
	public List findBiddCountByAppId(String appId) {
		List countList = (List) tdscBidderAppDao.findBidderAppListByAppId(appId);
		return countList;
	}

	/**
	 * 根据条件获得一页的用户列表（Action）
	 * 
	 * @param condition
	 *            查询条件对象
	 * @return PageList对象
	 */
	public PageList findPersonPageList(TdscBidderCondition condition) {
		return tdscBidderPersonAppDao.findPageList(condition);
	}

	/**
	 * 根据条件删除一条记录（Action）
	 * 
	 * @param tdscBidderPersonAppDao
	 * 
	 */
	public void removeById(String id) {
		tdscBidderPersonAppDao.deleteById(id);
	}

	/**
	 * 通过APPID找到一共有多少人参与了拍卖,即有多少人换领了号牌
	 * 
	 * @param appId
	 * @return
	 */
	public List queryJoinAuctionList(String appId) {
		return this.tdscBidderAppDao.findJoinAuctionListByAppId(appId);
	}

	/**
	 * 通过bidderPersonId获得一个竞买人信息
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public TdscBidderPersonApp queryOneBiddPer(String bidderPersonId) {
		TdscBidderPersonApp rtnPerson = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(bidderPersonId);
		return rtnPerson;
	}

	/**
	 * 通过bidderPersonId获得一个竞买人提交材料信息
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryOnePerMat(String bidderPersonId) {
		List perMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(bidderPersonId);
		return perMatList;
	}

	/**
	 * 修改联合竞买中 单个竞买人的信息
	 * 
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 * @return
	 */
	public TdscBidderPersonApp modifyBidPerXX(TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// 获得原来的保证金到账时间及其到账情况
		// TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao
		// .get(tdscBidderPersonApp.getBidderPersonId());
		// // TdscBidderPersonApp
		// // personApp=(TdscBidderPersonApp)tdscBidderPersonAppDao.get(tdscBidderPersonApp.getBidderPersonId());
		// if (personApp != null) {
		// // 赋值给新的实体对象
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
		// 删除 bidderPersonId 竞买人提交的材料
		List bidMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(tdscBidderPersonApp.getBidderPersonId());
		if (bidMatList != null && bidMatList.size() > 0) {
			for (int i = 0; i < bidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) bidMatList.get(i);
				tdscBidderMaterialDao.delete(tdscBidderMaterial);
			}
		}
		// 添加 新录入的信息
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
	 * 根据 bidderPersonId查询竞买人的材料信息
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryMatList(String bidderPersonId) {
		List queryMatList = (List) tdscBidderMaterialDao.getBidderByBidderId(bidderPersonId);
		return queryMatList;
	}

	/**
	 * 根据 bidderPersonId查询手动增加的材料信息
	 * 
	 * @param bidderPersonId
	 * @return
	 */
	public List queryOtherMatList(String bidderPersonId) {
		List queryMatList = (List) tdscBidderMaterialDao.getOtherMateByPersonId(bidderPersonId);
		return queryMatList;
	}

	/**
	 * 删除一个竞买人及其材料信息
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
	 * 单独竞买-新增竞买人--保存 TDSC_BIDDER_APP表 TDSC_BIDDER_PERSON_APP表 TDSC_BIDDER_MATERIAL表
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 */
	public String addBidder(String provideBm, TdscBidderApp tdscBidderApp, TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		// 保存 tdscBidderApp 并获得 bidderId；

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

		// 保存 tdscBidderPersonApp 并获得 bidderPersonId
		tdscBidderPersonApp.setBidderId(bidderId);
		if (tdscBidderPersonApp.getBzjDzqk() == null || tdscBidderPersonApp.getBzjDzqk() == "") {
			tdscBidderPersonApp.setBzjDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		if (StringUtils.isEmpty(tdscBidderPersonApp.getBidderPersonId())) {
			tdscBidderPersonApp.setBidderPersonId(null);
		}
		tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.saveOrUpdate(tdscBidderPersonApp);
		String bidderPersonId = (String) tdscBidderPersonApp.getBidderPersonId();
		// 保存 tdscBidderMaterial
		if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
			for (int i = 0; i < tdscBidMatList.size(); i++) {
				TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(i);
				tdscBidderMaterial.setBidderId(bidderId);
				tdscBidderMaterial.setBidderPersonId(bidderPersonId);
				tdscBidderMaterialDao.save(tdscBidderMaterial);
			}
		}
		// 修改 Tdsc_Bidder_Provide 表中材料编号为provideBm的 ifApp 值为 “1”
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
	 * 根据blockNpticeNo生成资格证书编号
	 * 
	 * @param appId
	 * @return
	 */
	public String getCertNo(String blockNpticeNo) {
		// 获得一个预生成资格证书编号
		String certNo = creatCertNo(blockNpticeNo);
		// 检查是否唯一
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
	 * 预生成资格证书编号
	 * 
	 * @param blockNpticeNo
	 * @return
	 */
	private String creatCertNo(String blockNpticeNo) {
		// 预设置地块公告号
		if (blockNpticeNo == null) {
			blockNpticeNo = "000000000";
		} else {
			blockNpticeNo = blockNpticeNo.substring(3, 7) + blockNpticeNo.substring(8);
		}
		// 流水号
		String tempCertNo = "000" + idSpringManager.getIncrementId("CertNo" + blockNpticeNo);
		// 预生成资格证书编号
		String certNo = blockNpticeNo + tempCertNo.substring(tempCertNo.length() - 3);
		return certNo;
	}

	/**
	 * 判断某个资格证书编号是否已经存在
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
	 * 竞买管理-单独竞买-修改
	 * 
	 * @param oneBidApp
	 * @param tdscBidderApp
	 * @param tdscBidderPersonApp
	 * @param tdscBidMatList
	 */
	public String updateBidder(List oneBidApp, TdscBidderApp tdscBidderApp, TdscBidderPersonApp tdscBidderPersonApp, List tdscBidMatList) {
		/**
		 * // 获得原来的 受理编号 String acceptNo = (String) tdscBidderApp.getAcceptNo(); // 获得原来的保证金总体到账情况,机审结果 ,资格证书编号 赋给新的实体 ; TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao
		 * .findOneBidderByBidderId(tdscBidderApp.getBidderId()); if (bidderApp != null) { tdscBidderApp.setBidderId(bidderApp.getBidderId()); if (bidderApp.getAppId() != null &&
		 * bidderApp.getAppId() != "") { tdscBidderApp.setAppId(bidderApp.getAppId()); } if (bidderApp.getBzjztDzqk() != null && bidderApp.getBzjztDzqk() != "") {
		 * tdscBidderApp.setBzjztDzqk(bidderApp.getBzjztDzqk()); } if (bidderApp.getReviewResult() != null && bidderApp.getReviewResult() != "") {
		 * tdscBidderApp.setReviewResult(bidderApp.getReviewResult()); } if (bidderApp.getCertNo() != null && bidderApp.getCertNo() != "") {
		 * tdscBidderApp.setCertNo(bidderApp.getCertNo()); } if (bidderApp.getAppUserId() != null && bidderApp.getAppUserId() != "") {
		 * tdscBidderApp.setAppUserId(bidderApp.getAppUserId()); } } // 获得原来的保证金到账时间及其到账情况 TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao
		 * .getBidderByBidderId(tdscBidderApp.getBidderId());
		 * 
		 * if (personApp != null) { // 赋值给新的实体对象 tdscBidderPersonApp .setBidderPersonId(personApp.getBidderPersonId()); if (personApp.getBzjDzqk() != null && personApp.getBzjDzqk()
		 * != "") { tdscBidderPersonApp.setBzjDzqk(personApp.getBzjDzqk()); } if (personApp.getBzjDzsj() != null) { tdscBidderPersonApp.setBzjDzsj(personApp.getBzjDzsj()); } if
		 * (personApp.getBzjDzse() != null) { tdscBidderPersonApp.setBzjDzse(personApp.getBzjDzse()); } } tdscBidderPersonApp.setBidderId(bidderApp.getBidderId());
		 **/
		// 20080325-15:40修改
		// delOneBidder(oneBidApp);
		// addBidder(null,acceptNo, tdscBidderApp, tdscBidderPersonApp,
		// tdscBidMatList);
		// 删除原有的竞买人提交的材料信息
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
	 * 联合竞买--添加竞买人
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public String addUnionBidder(String provideBm, TdscBidderApp tdscBidderApp, List tdscBidPerList, List tdscBidMatList) {
		// 保存 tdscBidderApp 并获得 bidderId
		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));

		// 设置 受理编号
		if (tdscBidderApp.getAcceptNo() == null) {
			String acceptNo = createAcceptNo(tdscBidderApp.getAppId());
			tdscBidderApp.setAcceptNo(acceptNo);
		}
		// 设置竞买类型为 联合竞买
		tdscBidderApp.setBidderType("2");
		// 设置到账情况为无历史操作
		if (tdscBidderApp.getBzjztDzqk() == null || tdscBidderApp.getBzjztDzqk() == "") {
			tdscBidderApp.setBzjztDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.save(tdscBidderApp);
		String bidderId = (String) tdscBidderApp.getBidderId();

		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				// 修改 tdscBidderPersonApp 设置 bidderId
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

				// 修改 每个竞买人提交的材料的 bidderId 和 BidderPersonId
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
	 * 联合竞买--delLJBidderPer
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public void delLJBidderPer(List tdscBidPerList) {
		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				String bidderPersonId = tdscBidderPersonApp.getBidderPersonId();

				// 删除每个竞买人提交的材料列表
				List tdscBidMatList = (List) tdscBidderMaterialDao.getBidderWithOutBidderId(bidderPersonId);
				if (tdscBidMatList != null && tdscBidMatList.size() > 0) {
					for (int j = 0; j < tdscBidMatList.size(); j++) {
						TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) tdscBidMatList.get(j);
						tdscBidderMaterialDao.delete(tdscBidderMaterial);
					}
				}
				// 删除tdscBidderPersonApp
				tdscBidderPersonAppDao.delete(tdscBidderPersonApp);
			}
		}
	}

	/**
	 * 联合竞买--添加竞买人
	 * 
	 * @param tdscBidderApp
	 * @param tdscBidPerList
	 */
	public String addExistUnionBidder(TdscBidderApp tdscBidderApp, List tdscBidPerList) {
		// 保存 tdscBidderApp 并获得 bidderId
		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
		// 设置 受理编号
		if (tdscBidderApp.getAcceptNo() == null) {
			String acceptNo = createAcceptNo(tdscBidderApp.getAppId());
			tdscBidderApp.setAcceptNo(acceptNo);
		}
		// 设置竞买类型为 联合竞买
		tdscBidderApp.setBidderType("2");
		// 设置到账情况为无历史操作
		if (tdscBidderApp.getBzjztDzqk() == null || tdscBidderApp.getBzjztDzqk() == "") {
			tdscBidderApp.setBzjztDzqk(GlobalConstants.DIC_ID_BIDDERDZQK_NOACTION);
		}
		tdscBidderApp.setBidderId("");
		tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.save(tdscBidderApp);
		String newBidderId = (String) tdscBidderApp.getBidderId();
		List newBidMatList = new ArrayList();
		if (tdscBidPerList != null && tdscBidPerList.size() > 0) {
			for (int i = 0; i < tdscBidPerList.size(); i++) {
				// 修改 tdscBidderPersonApp 设置 bidderId
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

				// 修改 所有竞买人提交的材料的 bidderId和BidderPersonId,得到新增竞买人list
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
	 * 联合竞买--修改竞买人--保存
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
				// 修改 tdscBidderPersonApp 设置 bidderId
				TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidPerList.get(i);
				tdscBidderPersonApp.setBidderId(newBidId);
				tdscBidderPersonAppDao.update(tdscBidderPersonApp);

				// 修改 每个竞买人提交的材料的 bidderId 和 BidderPersonId
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
	 * 生成受理编号 AcceptNo
	 * 
	 * @param bidderId
	 * @return
	 */
	public String createAcceptNo(String appId) {

		// 地块公告号
		String blockNoticeNo = "000000000";
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getMarginEndDate(appId);
		if (tdscBlockTranApp.getBlockNoticeNo() != null) {
			String tempBlockNoticeNo = "000000000" + (String) tdscBlockTranApp.getBlockNoticeNo();
			blockNoticeNo = tempBlockNoticeNo.substring(tempBlockNoticeNo.length() - 8, tempBlockNoticeNo.length() - 4)
					+ tempBlockNoticeNo.substring(tempBlockNoticeNo.length() - 3);
		}

		// 受理编号 = 地块公告号+3位流水号
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
	 * 根据 bidderId查出竞买信息
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
	 * 删除 一个申请信息
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
	 * 根据bidderId删除竞买信息
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
	 * 根据bidderId删除竞买报名材料信息
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
	 * 修改一卡通的卡号与密码
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
	 * 根据provideBm查出该申请材料是否存在
	 * 
	 * @param provideBm
	 * @return
	 */
	public List getProByProvideBm(String provideBm) {
		List retList = (List) tdscBidderProvideDao.getProByProvideBm(provideBm);
		return retList;
	}

	/**
	 * 校验 一卡通编号是否已经使用过
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkIfUsedYktXh(String yktXh) {
		List retList = (List) tdscBidderAppDao.checkIfUsedYktXh(yktXh);
		return retList;
	}

	/**
	 * 校验 一卡通卡号是否已经使用过
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
	 * 通过bidderId 获得竞买人的信息
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
	 * 通过appId 获得所有参加地块招拍挂的竞买信息列表
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderAppList(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.findBidderAppListByAppId(appId);
		return tdscBidderAppList;
	}

	/**
	 * 通过appId 获得所有通过机审的竞买信息列表
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderListSrc(appId);
		return tdscBidderAppList;
	}

	/**
	 * 通过appId获得已经下载过资格证书的竞买人信息
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListDownloadZgzs(String appId) {
		List tdscBidderAppList = tdscBidderAppDao.queryBidderListDownloadZgzs(appId);
		return tdscBidderAppList;
	}

	/**
	 * 通过交易卡编号获得一个竞买信息
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktXh(String yktXh) {
		return tdscBidderAppDao.getBidderAppByYktXh(yktXh);
	}

	/**
	 * 通过资格证书编号获得一个竞买信息
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByCertNo(String certNo) {
		return tdscBidderAppDao.getBidderAppByCertNo(certNo);
	}

	/**
	 * 通过appId和号牌号码获得一个竞买信息
	 * 
	 * @param cardNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdConNo(String appId, String conNo) {
		return tdscBidderAppDao.getBidderAppByAppIdConNo(appId, conNo);
	}

	/**
	 * 查询交易卡信息
	 * 
	 * @param appId
	 * @return
	 */
	public String findyktBh(String appId, String yktBh) {
		String YktBh = tdscBidderAppDao.findyktBh(appId, yktBh);
		return YktBh;
	}

	/**
	 * 通过appId和一卡通序号获得一个竞买信息
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdYktXh(String appId, String yktXh) {
		return tdscBidderAppDao.getBidderAppByAppIdYktXh(appId, yktXh);
	}

	/**
	 * 通过appId和资格证书编号获得一个竞买信息
	 * 
	 * @param appId
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getBidderAppByAppIdCertNo(String appId, String certNo) {
		return tdscBidderAppDao.getBidderAppByAppIdCertNo(appId, certNo);
	}

	/**
	 * 通过bidderId和一卡通序号获得一个竞买信息
	 * 
	 * @param bidderId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktXh(String bidderId, String yktXh) {
		return tdscBidderAppDao.getBidderAppByBidderIdYktXh(bidderId, yktXh);
	}

	/**
	 * 通过appId和一卡通卡号获得一个竞买信息
	 * 
	 * @param bidderId
	 * @param yktBh
	 * @return
	 */
	public TdscBidderApp getBidderAppByBidderIdYktBh(String bidderId, String yktBh) {
		return tdscBidderAppDao.getBidderAppByBidderIdYktBh(bidderId, yktBh);
	}

	/**
	 * 通过appId和tenderId获得一个竞买信息
	 * 
	 * @param appId
	 * @param tenderId
	 * @return
	 */
	public TdscBidderApp queryTenderInfoByAppIdTenderId(String appId, String tenderNo) {
		return tdscBidderAppDao.getBidderAppByAppIdTenderId(appId, tenderNo);
	}

	/**
	 * 修改竞买申请信息表
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
	 * 查出一个竞买人在表中的主键
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
	 * 根据yktBh查出一个竞买人在表中的主键
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
	 * 根据主键查出一条对应的记录
	 * 
	 * @param bidderId
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfo(String bidderId) {
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
		return tdscBidderApp;
	}

	/**
	 * 取得竞买人的全部记录
	 * 
	 * @return
	 */
	public List findPurchaserList() {
		List list = this.tdscBidderAppDao.findAll();
		return list;
	}

	/**
	 * 根据AppId查询评标编号
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
	 * 通过交易卡卡号获得一个竞买信息
	 * 
	 * @param yktBh
	 * @return
	 */
	public TdscBidderApp getBidderAppByYktBh(String yktBh) {
		return tdscBidderAppDao.getBidderAppByYktBh(yktBh);
	}

	/**
	 * 通过交易卡卡号获得所有竞买信息列表
	 * 
	 * @param yktBh
	 * @return
	 */
	public List getBidderAppListByYktBh(String yktBh) {
		return tdscBidderAppDao.checkIfUsedYktBh(yktBh);
	}

	/**
	 * 根据appId取得竞买人的全部记录
	 * 
	 * @return
	 */
	public List findPurchaserListByAppId(String appId) {
		List list = this.tdscBidderAppDao.findBidderAppListByAppId(appId);
		return list;
	}

	/**
	 * 根据appid判断一个号牌在一个地块的现场竞价会中是否重复使用
	 * 
	 * @param appId
	 * @return
	 */
	public String checkConnumByAppId(String appId, String conNum) {
		String temp = tdscBidderAppDao.checkConnumByAppId(appId, conNum);
		return temp;
	}

	/**
	 * 根据certNo判断一个号牌在一个地块的现场竞价会中是否重复使用
	 * 
	 * @param appId
	 * @return
	 */
	public String checkConnumByCertNo(String appId, String conNum) {
		String temp = tdscBidderAppDao.checkConnumBycertNo(appId, conNum);
		return temp;
	}

	/**
	 * 校验输入的受理编号是否存在
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
	 * 根据block_id查询子地块信息
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		return tdscBlockPartDao.getTdscBlockPartList(blockId);
	}

	/**
	 * 根据block_id查询BlockCode字段不重复的子地块信息，暂时用于取出某个地块的所有不重复地块编号BlockCode
	 * 
	 * @param blockId
	 * @return
	 */
	public String getTdscBlockPartListInBlockCode(String blockId) {
		List tdscBlockPartList = (List) tdscBlockPartDao.getTdscBlockPartListInBlockCode(blockId);

		String blockCode = "";
		if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
			for (int i = 0; i < tdscBlockPartList.size(); i++) {
				blockCode += tdscBlockPartList.get(i) + "、";
			}
			if (!"".equals(blockCode) && blockCode.length() > 0)
				blockCode = blockCode.substring(0, blockCode.length() - 1);
		}

		return blockCode;
	}

	/**
	 * 查询资格审查结果列表
	 * 
	 * @return
	 */
	public PageList queryAppViewList(TdscBaseQueryCondition condition) {
		return tdscBidderAppDao.queryAppViewList(condition);
	}

	/**
	 * 根据bidderId整理竞买人名称，保证金金额总数
	 * 
	 * @param bidderId
	 * @return TdscBidderPersonApp
	 */
	public TdscBidderPersonApp tidyBidderByBidderId(String bidderId) {
		// 根据bidderId查询多有的竞买人
		List bidderPersonList = (List) tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		if (bidderPersonList != null && bidderPersonList.size() > 0) {
			StringBuffer personNames = new StringBuffer("");
			BigDecimal bzjDzse = new BigDecimal(0.00);
			for (int i = 0; i < bidderPersonList.size(); i++) {
				TdscBidderPersonApp tempApp = (TdscBidderPersonApp) bidderPersonList.get(i);
				if (i < bidderPersonList.size() - 1) {
					personNames.append(tempApp.getBidderName()).append("，");
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
	 * 根据条件获得一页的ykt列表（Action）
	 * 
	 * @param condition
	 *            查询条件对象
	 * @return PageList对象
	 */
	public PageList findAllYktPageList(TdscBidderCondition condition) {
		return tdscBidderAppDao.findListByCondition(condition);
	}

	/**
	 * 根据appIdList取得所有不重复的公告id列表
	 * 
	 * @param appIdList
	 * @return
	 */
	public List findNoticeIdListByAppIdList(List appIdList) {
		List retList = (List) tdscBidderAppDao.findNoticeIdListByAppIdList(appIdList);
		return retList;
	}

	/**
	 * 根据条件取得公告列表
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
			// 构造查询condition
			TdscBidderCondition temp_condition = new TdscBidderCondition();
			// 构造竞买人LIST
			List bidderAppList = new ArrayList();
			// 整理竞买人信息
			for (int i = 0; i < yktBhList.size(); i++) {
				// 根据bidderId查询竞买人信息
				TdscBidderApp tdscBidderApp = new TdscBidderApp();
				// 校验交易卡编号是否为空
				if (StringUtils.isNotEmpty((String) yktBhList.get(i))) {
					tdscBidderApp.setYktBh(StringUtils.trimToEmpty((String) yktBhList.get(i)));
					// 设置查询条件
					temp_condition.setYktBh(StringUtils.trimToEmpty((String) yktBhList.get(i)));
					// 清空bidderAppList
					if (bidderAppList != null)
						bidderAppList.clear();
					// 查询竞买人信息
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
		// 1. 保存竞买的地块, 保证金 TDSC_BIDDER_PERSON_APP
		// 2. 保存受托人信息 TDSC_BIDDER_APP
		// 2.1 保存转托人信息 TDSC_BIDDER_APP
		// 3. 保存法人信息 TDSC_BIDDER_PERSON_APP
		// 4. 保存交易卡,资格证书编号 信息 TDSC_BIDDER_APP
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		try {
			BeanUtils.copyProperties(bidderApp, bidderForm);
			BeanUtils.copyProperties(bidderPersonApp, bidderForm);

			if (isPurposePerson)
				bidderApp.setIsPurposePerson("1");
			else
				bidderApp.setIsPurposePerson("0");

			// 便于查询地块竞买人使用
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

			// 所有数据库中的数据先删除再添加 TDSC_BIDDER_MATERIAL
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
		// 1. 保存竞买的地块, 保证金 TDSC_BIDDER_PERSON_APP
		// 2. 保存受托人信息 TDSC_BIDDER_APP
		// 2.1 保存转托人信息 TDSC_BIDDER_APP
		// 3. 保存法人信息 TDSC_BIDDER_PERSON_APP
		// 4. 保存交易卡,资格证书编号 信息 TDSC_BIDDER_APP
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		try {
			BeanUtils.copyProperties(bidderApp, bidderForm);
			BeanUtils.copyProperties(bidderPersonApp, bidderForm);
			if (isPurposePerson)
				bidderApp.setIsPurposePerson("1");
			else
				bidderApp.setIsPurposePerson("0");
			// 便于查询地块竞买人使用
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
	 * 根据竞买单位交易卡芯片号码查询
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
	 *            竞买人名称
	 * @param bidderCardNum
	 *            竞买人交易卡，可刷卡
	 * @return
	 */
	public List queryBidderCredList(String bidderName) {

		List retList = new ArrayList();
		List personList = tdscBidderPersonAppDao.likeTdscBidderPersonAppByBidderName(bidderName);

		// 1.公告号码
		// 2.竞买人
		// 3.资格证书编号
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
		//1. 先删除
		List tdscReturnBailList = (List)tdscReturnBailDao.queryTdscReturnBailList(bidderId);
		if(tdscReturnBailList!=null && tdscReturnBailList.size()>0){
			for(int i=0; i<tdscReturnBailList.size(); i++){
				TdscReturnBail tdscReturnBail = (TdscReturnBail)tdscReturnBailList.get(i);
				tdscReturnBailDao.delete(tdscReturnBail);
			}
		}		
		
		//2. 后新增
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
	 * 得到竞买人信息
	 * 
	 * @param yktXh
	 * @return
	 */
	public List checkJmrName(String bidderName) {
		List tdscBidderPersonAppList = (List) tdscBidderPersonAppDao.checkJmrName(bidderName);
		return tdscBidderPersonAppList;
	}
	/**
	 * 根据竞买人id得到竞买人是否为意向竞买人
	 * 
	 * @param yktXh
	 * @return
	 */
	public List findJmrByBidderId(String bidderId) {
		List tdscBidderAppList = (List) tdscBidderAppDao.findJmrByBidderId(bidderId);
		return tdscBidderAppList;
	}
	
	/**
	 * 根据noticeId查询TdscBlockAppView列表
	 * @param noticeId
	 * @return
	 */
	public List queryAppViewListByNoticeId(String noticeId) {
		List tdscNoticeList = tdscNoticeAppDao.queryAppViewListByNoticeId(noticeId);
		return tdscNoticeList;
	}
	
	/**
	 * 根据竞买人名称查询竞买人信息表
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
	 * 根据条件查询竞买人信息
	 */
	public List findTdscBidderPersonAppListByCondition(TdscBidderCondition condition) {
		List resultList = this.tdscBidderPersonAppDao.findTdscBidderPersonAppListByCondition(condition);		
		return resultList;
	}
	/**
	 * 获取取得竞买资格的竞买人列表
	 * @param appId
	 * @return
	 */
	public List findHaveCertNoBidderList(String appId){
		return this.tdscBidderAppDao.findHaveCertNoBidderList(appId);
	}
}
