package com.wonders.wsjy.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscCorpInfo;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.credit.dao.TdscCorpInfoDao;
import com.wonders.tdsc.presell.dao.FileRefDao;
import com.wonders.tdsc.tdscbase.dao.TdscBidderViewDao;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.dao.PersonInfoDAO;
import com.wonders.wsjy.dao.SubscribeDAO;
import com.wonders.wsjy.dao.WsjyBankAppDao;
public class SubscribeService {
	private org.apache.log4j.Logger logger = Logger.getLogger(SubscribeService.class);
	private SubscribeDAO subscribeDAO;
	private PersonInfoDAO personInfoDAO;
	private IdSpringManager idSpringManager;
	private TdscCorpInfoDao tdscCorpInfoDao;
	private String PATH = PropertiesUtil.getInstance().getProperty("file.save.path")+"\\JMR";
	//���Ӿ����˱�֤����ϸ��Ϣ
	private WsjyBankAppDao wsjyBankAppDao;
	public WsjyBankAppDao getWsjyBankAppDao() {
		return wsjyBankAppDao;
	}
	public void setWsjyBankAppDao(WsjyBankAppDao wsjyBankAppDao) {
		this.wsjyBankAppDao = wsjyBankAppDao;
	}
	//�����Ѹ���֤��ľ�������Ϣ
	private TdscBidderViewDao tdscBidderViewDao;
	public TdscBidderViewDao getTdscBidderViewDao() {
		return tdscBidderViewDao;
	}
	public void setTdscBidderViewDao(TdscBidderViewDao tdscBidderViewDao) {
		this.tdscBidderViewDao = tdscBidderViewDao;
	}
	
	public void setTdscCorpInfoDao(TdscCorpInfoDao tdscCorpInfoDao) {
		this.tdscCorpInfoDao = tdscCorpInfoDao;
	}
	private TradeServer tradeServer;
	private TdscBlockTranAppDao tdscBlockTranAppDao;
	private TdscBidderPersonAppDao tdscBidderPersonAppDao;
	private TdscBidderAppDao tdscBidderAppDao;
	private FileRefDao fileRefDao;
	
	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}
	public PersonInfoDAO getPersonInfoDAO() {
		return this.personInfoDAO;
	}
	public void setPersonInfoDAO(PersonInfoDAO personInfoDAO) {
		this.personInfoDAO = personInfoDAO;
	}
	
	public void setSubscribeDAO(SubscribeDAO subscribeDAO) {
		this.subscribeDAO = subscribeDAO;
	}
	

	public FileRefDao getFileRefDao() {
		return fileRefDao;
	}
	public void setFileRefDao(FileRefDao fileRefDao) {
		this.fileRefDao = fileRefDao;
	}
	/**
	 * �����û����ȡ�ö�Ӧ���û������깺��Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public List getMySubscribeList(String userId) {
		List list = subscribeDAO.querySubscribeListByUserId(userId);
		return list;
	}
	/**
	 * ���澺�����ϴ����Ͼ���ĸ���
	 * @param appId
	 * @param bytes
	 * @param fileName
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public FileRef uploadFile(String appId, byte[] bytes, String fileName,String fileType,String fileCatlog) throws Exception {
		FileRef fileRef = new FileRef();
		fileRef.setBusId(appId);
		fileRef.setFileCatalog(fileCatlog);
		fileRef.setFileName(fileName);
		fileRef.setFileType(fileType);
		fileRef.setUpdateTime(new Date());
		fileRef = (FileRef)fileRefDao.saveOrUpdate(fileRef);
		String filePath = PATH + File.separator + fileRef.getFileId() + "." + fileType;
		fileRef.setFilePath(filePath);
		fileRefDao.update(fileRef);
		File file = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		return fileRef;
	}
	/**
	 * ��ȡ���Ͼ����˵ĸ���
	 * @param presellId
	 * @param catalog
	 * @return
	 */
	public FileRef findFileRefByBusId(String busId,String fileCatlog){
		if (StringUtils.isNotEmpty(busId)) {
			List list = fileRefDao.findListFileRefByCondition(busId, fileCatlog);
			if(list!=null&&list.size()>0){
				return (FileRef)list.get(0);
			}
		}
		return null;
		
	}

	/**
	 * ��ȡ���Ͼ����˵ĸ����б�
	 * @param presellId
	 * @param catalog
	 * @return
	 */
	public List findFileRefList(String busId,String fileCatlog){
		if (StringUtils.isNotEmpty(busId)) {
			return fileRefDao.findListFileRefByCondition(busId, fileCatlog);
		}
		return null;
		
	}

	/**
	 * ��ȡ�ļ�����
	 * @param fileId
	 */
	public FileRef getFileRefById(String fileId) {
		if (StringUtils.isNotEmpty(fileId)) {
			return (FileRef) fileRefDao.get(fileId);
		}
		return null;
	}
	/**
	 * ɾ������
	 * @param fileId
	 */
	public void delFileRefById(String fileId) {
		if (StringUtils.isNotEmpty(fileId)) {
			fileRefDao.deleteById(fileId);
		}
	}
	/**
	 * �����ҵ��깺��Ϣ
	 * 
	 * @param userId
	 * @param appId
	 */
	public synchronized TdscBidderApp addMySubscribeList(String userId, TdscTradeView tdscTradeView) {
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		// ȡ���û���Ϣ
		PersonInfo personInfo = getPersonInfo(userId);
		BeanUtils.copyProperties(tdscBidderApp, personInfo);
		tdscBidderApp.setBidderId(null);
		tdscBidderApp.setUserId(userId);
		tdscBidderApp.setAppId(tdscTradeView.getAppId());
		tdscBidderApp.setNoticeId(tdscTradeView.getNoticeId());
		tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
		
		return (TdscBidderApp)subscribeDAO.persistentSubscribe(tdscBidderApp);
	}
	
	/**
	 * ȡ����ˮ��
	 * @return
	 */
	public String createSqNumber() {
		// ���ɶ������
		Long id = idSpringManager.getIncrementId("sgNumber");
		DecimalFormat decimalFormat = new DecimalFormat("000");
		String formatId = decimalFormat.format(id);
		String date = DateUtil.date2String(new Date(), "yyyyMMdd");
		formatId = date + formatId;
		formatId = formatId + System.currentTimeMillis();
		return formatId;
	}
	/**
	 * ȡ�ú���
	 * @return
	 */
	public String generateHaoPai(String noticeId){
		List list = this.tradeServer.findTdscBidderAppListByNoticeId(noticeId);
		ArrayList al = new ArrayList();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			al.add(app.getConNum());
		}
		Map map = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_CON_NUM);
		Collection valueSet = map.values();
		Iterator it = valueSet.iterator();
		ArrayList dicList= new ArrayList();
		while(it.hasNext()) { 
			String getNum = (String)it.next();
			if(al.indexOf(getNum)==-1){
				dicList.add(getNum);
			}
		}
		//ȡ�ú��Ƶ�������
		int count = dicList.size();
		//�ں������������ȡ��һ������
		Random random = new Random();
		int index = random.nextInt(count-1);
		return (String)dicList.get(index);
	}
	/**
	 * ��ȡ�ʸ�֤����
	 * @return
	 */
	public String generateCertNo() {
		String nowMonth = DateUtil.date2String(new java.util.Date(), "yyyyMM");
		Long certNo = idSpringManager.getIncrementId("CertNo" + nowMonth);
		return nowMonth + StringUtils.leftPad(certNo + "", 4, '0');
	}
	/**
	 * 
	 * ���Ҵ��û��ڴ˹������Ƿ��깺�������ؿ鲢����ȡ�˺��ƣ������������򷵻�����ȡ���ƵĶ���
	 * ���򷵻ؿ�
	 * @param noticeId
	 * @param userId
	 * @return
	 */
	public TdscBidderApp getSameNoticeConNum(String noticeId,String userId){
		List list = this.tdscBidderAppDao.findSameNoticeBidderListByUserId(noticeId, userId);
		TdscBidderApp result = null;
		if(list!=null&&list.size()>1){
			for(int i=0;i<list.size();i++){
				TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
				if(StringUtils.isNotBlank(bidderApp.getConNum())){
					result = bidderApp;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * ��ȡ�ʸ�֤����
	 * @return
	 */
	public String generateCertNo(String noticeId,String userId){
		List list = this.tdscBidderAppDao.findSameNoticeBidderListByUserId(noticeId, userId);
		String result = "";
		//���Ҵ��û��ڴ˹������Ƿ��깺�������ؿ飬�����깺�����ؿ飬���Ҳ��������ؿ���ʸ�֤���ţ�
		//Ȼ��ֵ������
		if(list!=null&&list.size()>1){
			for(int i=0;i<list.size();i++){
				TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
				if(StringUtils.isNotBlank(bidderApp.getCertNo())){
					result = bidderApp.getCertNo();
					break;
				}
			}
		}
		if(StringUtils.isNotBlank(result)){
			return result;
		}
		String nowMonth = DateUtil.date2String(new java.util.Date(), "yyyyMM");
		Long certNo = idSpringManager.getIncrementId("CertNo" + nowMonth);
		result = nowMonth + StringUtils.leftPad(certNo + "", 4, '0');
		return result;
		
	}
	
	/**
	 * �����û���ź͵ؿ���ȡ�ö�Ӧ���깺��Ϣ
	 * 
	 * @param userId
	 * @param appId
	 * @return
	 */
	public TdscBidderApp getSubscribeInfo(String userId, String appId) {
		return subscribeDAO.getSubscribeInfoByUserIdAndAppId(userId, appId);
	}
	
	/**
	 * �����û���ź͵ؿ���ȡ�ö�Ӧ���깺��Ϣ
	 * 
	 * @param userId
	 * @param appId
	 * @return
	 */
	public TdscBidderApp getSubscribeInfo(String bidderId) {
		return (TdscBidderApp)subscribeDAO.get(bidderId);
	}
	
	
	/**
	 * ȡ�þ�������Ϣ
	 * @param bidderId
	 * @return
	 */
	public PersonInfo getPersonInfo(String bidderId) {
		if (StringUtils.isEmpty(bidderId)) return null;
		return personInfoDAO.getPersonInfo(bidderId);
	}
	
	/**
	 * �־û��û���Ϣ
	 * @param personInfo
	 * @return
	 */
	public PersonInfo persistentInfo(PersonInfo personInfo) {
		if (null == personInfo) return null;
		return personInfoDAO.persistentPersonInfo(personInfo);
	}

	/**
	 * ɾ���깺��Ϣ
	 * @param bidderId
	 */
	public void delSubscribe(String bidderId) {
		TdscBidderApp bidderApp = (TdscBidderApp)tdscBidderAppDao.get(bidderId);
		if(bidderApp!=null){
			//����������������ɾ��֮�����ݽ��и��¡�
			if("1".equals(bidderApp.getIsPurposePerson())){
				bidderApp.setUserId("");
				bidderApp.setNoticeId("");
				bidderApp.setAcceptDate(null);
				bidderApp.setBankId("");
				bidderApp.setBankNumber("");
				bidderApp.setSgDate(null);
				bidderApp.setSqNumber("");
				tdscBidderAppDao.update(bidderApp);
				return;
			}
		}
		subscribeDAO.deleteById(bidderId);
		//deleteFileRef(bidderId);
	}
	public void paySubscribeInfoByBankNumber(String bankNumber) {
		this.paySubscribeInfoByBankNumber(bankNumber,null);
	}
	public void paySubscribeInfoByBankNumber(String bankNumber,String bankId) {
		
		TdscBidderApp bidderApp = subscribeDAO.getSubscribeInfoByBankNumber(bankNumber);
		logger.info("֧����֤��ʼ---,bidderIdΪ"+bidderApp.getBidderId()+",bankIdΪ"+bankId);
		System.out.println("֧����֤��ʼ---,bidderIdΪ"+bidderApp.getBidderId()+",bankIdΪ"+bankId);
		if (bidderApp != null) {
			TdscBlockTranApp blockTranApp = this.tdscBlockTranAppDao.getMarginEndDate(bidderApp.getAppId());
			List personList = this.tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderApp.getBidderId());
			bidderApp.setIfCommit("1");
			if(StringUtils.isEmpty(bidderApp.getCertNo())){
				String certNo = generateCertNo(bidderApp.getNoticeId(),bidderApp.getUserId());
				bidderApp.setCertNo(certNo);
				bidderApp.setAcceptNo(certNo);
				bidderApp.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
				bidderApp.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
			}
			//�������к����ǿ����þ������Լ�ѡ���
			//String haoPai = this.generateHaoPai(bidderApp.getNoticeId());
			//bidderApp.setConNum(haoPai);
			TdscBidderApp sameConNumObj = this.getSameNoticeConNum(bidderApp.getNoticeId(),bidderApp.getUserId());
			if(sameConNumObj!=null){
				bidderApp.setConNum(sameConNumObj.getConNum());
				bidderApp.setConTime(sameConNumObj.getConTime());
				if ("1".equals(bidderApp.getIsPurposePerson())) {
					String purposeAppId = bidderApp.getAppId();
					this.tradeServer.insertListingAppOfPurposePerson(bidderApp, purposeAppId);
				}
			}
			if(StringUtils.isNotEmpty(bankId)){
				bidderApp.setBankId(bankId);
			}

			//��֤��
			BigDecimal blockBzj = blockTranApp.getMarginAmount();
			if(personList!=null&&personList.size()>0){
				TdscBidderPersonApp personApp =	(TdscBidderPersonApp)personList.get(0);
				personApp.setBzjDzse(blockBzj);
				personApp.setBzjDzsj(new Date());
				personApp.setBzjDzqk("00");//��֤����������
				System.out.println("���汣֤����������");
				this.tdscBidderPersonAppDao.update(personApp);
			}
			tdscBidderAppDao.update(bidderApp);
			CoreService.reloadClientPipe(bidderApp.getUserId());
//			bidderApp.setAcceptNo(certNo);
//			bidderApp.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
//			bidderApp.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
		}
	}
	
	/**
	 * ����������Ϣ������ļ�
	 * @param bidderId
	 * @param fileId1
	 * @param fileId2
	 */
	public void bindBidderAppWithFileRef(String bidderId,String fileId1,String fileId2) {
		FileRef fileRef = (FileRef)fileRefDao.get(fileId1);
		fileRef.setBusId(bidderId);
		fileRefDao.update(fileRef);
		fileRef = (FileRef)fileRefDao.get(fileId2);
		fileRef.setBusId(bidderId);
		fileRefDao.update(fileRef);
	}
	
	/**
	 * ɾ������ļ�
	 * @param bidderId
	 */
	private void deleteFileRef(String bidderId) {
		List fileRefs = fileRefDao.findListFileRefByCondition(bidderId, null);
		for (int i = 0; i < fileRefs.size(); i++) {
			FileRef fileRef = (FileRef)fileRefs.get(i);
			String filePath = fileRef.getFilePath();
			new File(filePath).delete();
			fileRefDao.delete(fileRef);
		}
		
	}
	
	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	public TdscBidderAppDao getTdscBidderAppDao() {
		return tdscBidderAppDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public TdscBidderPersonAppDao getTdscBidderPersonAppDao() {
		return tdscBidderPersonAppDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public TdscBlockTranAppDao getTdscBlockTranAppDao() {
		return tdscBlockTranAppDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}
	/**
	 * ͨ��inacct���Ҷ�Ӧ��wsjyBankApp����
	 * @param inacct
	 * @return
	 */
	public List findPayList(String inacct){
		return wsjyBankAppDao.findPayListByInacct(inacct);
	}
	/**
	 * ����tdscBlockTranApp����
	 * @param 
	 * @return
	 */
	public PageList findBlockList(int pageSize,int pageCurrent){
		return tdscBlockTranAppDao.findPayBzjBlockList(pageSize,pageCurrent);
	}
	/**
	 * ��ȡtdscBidderView����
	 * ���Ҿ�������Ϣ
	 * @param 
	 * @return
	 */
	public List findBidderList(String appId){
		return tdscBidderViewDao.findPayBzjBidderList(appId);
	}
	
	public TdscCorpInfo getCorpByName(String name){
		return tdscCorpInfoDao.getCorpByName(name);
	}
}
