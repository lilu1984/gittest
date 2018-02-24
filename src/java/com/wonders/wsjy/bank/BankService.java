package com.wonders.wsjy.bank;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.Leftb;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.safehaus.uuid.UUIDGenerator;

import com.wonders.engine.CoreService;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.wsjy.bank.bo.BankBody;
import com.wonders.wsjy.bank.bo.BankEntity;
import com.wonders.wsjy.bank.bo.BankEntityList;
import com.wonders.wsjy.bank.bo.BankHead;
import com.wonders.wsjy.bank.bo.BankPart;
import com.wonders.wsjy.bo.DicBank;
import com.wonders.wsjy.bo.WsjyBankApp;
import com.wonders.wsjy.bo.WsjyBankLog;
import com.wonders.wsjy.dao.DicBankDao;
import com.wonders.wsjy.dao.WsjyBankAppDao;
import com.wonders.wsjy.dao.WsjyBankLogDao;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;

/**
 * ���нӿ�ҵ�������
 * @author Administrator
 *
 */
public class BankService {
	private Logger logger = Logger.getLogger(BankService.class);
	private String MAP_PATH = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "bankMap.xml";
	private WsjyBankAppDao wsjyBankAppDao = (WsjyBankAppDao)AppContextUtil.getInstance().getAppContext().getBean("wsjyBankAppDao");
	private TdscBidderAppDao tdscBidderAppDao = (TdscBidderAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppDao");
	private TdscBidderPersonAppDao tdscBidderPersonAppDao = (TdscBidderPersonAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscBidderPersonAppDao");
	private TdscBlockTranAppDao tdscBlockTranAppDao = (TdscBlockTranAppDao)AppContextUtil.getInstance().getAppContext().getBean("tdscBlockTranAppDao");
	private DicBankDao dicBankDao = (DicBankDao)AppContextUtil.getInstance().getAppContext().getBean("dicBankDao");
	private SubscribeService subscribeService = (SubscribeService)AppContextUtil.getInstance().getAppContext().getBean("subscribeService");
	private WsjyBankLogDao wsjyBankLogDao = (WsjyBankLogDao)AppContextUtil.getInstance().getAppContext().getBean("wsjyBankLogDao");
	private TradeServer tradeServer = (TradeServer)AppContextUtil.getInstance().getAppContext().getBean("tradeServer");
	/**
	 * ���ݷ����ظ�
	 */
	public static String ERR01 = "01";
	/**
	 * ��������
	 */
	public static String ERR09 = "09";
	/**
	 * ϵͳ���� 
	 */
	public static String ERR99 = "99";
	
	private BankPart m_clientBankPart = null;
	/**
	 * ��ȡ head
	 * @param code
	 * @return
	 */
	private BankHead getHead(String code){
		BankHead head = new BankHead();
		head.setTransCode(code);
		head.setTransDate(DateUtil.date2String(new Date(), "yyyyMMdd"));
		head.setTransTime(DateUtil.date2String(new Date(), "HHmmss"));
		head.setSeqNo(UUIDGenerator.getInstance().generateRandomBasedUUID().toString().replaceAll("-", ""));
		return head;
	}
	/**
	 * ��ͻ��˷��ʹ�����Ϣ
	 * @param code ������Ϣ��ҵ�����
	 * @param msg
	 * @return
	 */
	public String sendException2Client(String code,String msg,String errCode){
		BankPart resultPart = new BankPart();
		resultPart.setBankHead(this.getHead(code));
		BankBody resultBankBody = new BankBody();
		resultBankBody.setResult(errCode);
		//����һֱ���������������⣬���������addword��ʱ����.
		resultBankBody.setAddWord("");
		//resultBankBody.setAddWord(msg);
		resultPart.setBankBody(resultBankBody);
		String resultXml = CastorFactory.marshalBean(resultPart, MAP_PATH);
		//		���ͱ���ǰ����6λ��С
		resultXml = StringUtils.leftPad(String.valueOf(resultXml.length()), 6, "0") + resultXml;
		return resultXml;
	}
	/**
	 * ����������Ӧ�ͻ��˵����󣬲�������Ӧ��ϢXML��
	 * @param xmlStr
	 * @return
	 * @throws EngineException 
	 */
	public String ExecuteServer(String xmlStr) throws EngineException{
		String resultXml = "";
		if(this.m_clientBankPart==null){
			this.m_clientBankPart = (BankPart)CastorFactory.unMarshalBean(MAP_PATH, xmlStr);
		}
		BankPart resultPart = new BankPart();
		resultPart.setBankHead(this.m_clientBankPart.getBankHead());
		BankBody resultBankBody = new BankBody();
		if(this.m_clientBankPart!=null&&this.m_clientBankPart.getBankHead()!=null&&this.m_clientBankPart.getBankBody()!=null){
			//������ǰ�û����;����˽ɿ���Ϣ 
			if("G00001".equals(this.m_clientBankPart.getBankHead().getTransCode())){
				resultBankBody = this.executeG00001();
			}
		}else{
			throw new EngineException("XML�ļ���Ϣ������!");
		}
		resultPart.setBankBody(resultBankBody);
		resultXml = CastorFactory.marshalBean(resultPart, MAP_PATH);
		//���ͱ���ǰ����6λ��С
		resultXml = StringUtils.leftPad(String.valueOf(resultXml.length()), 6, "0") + resultXml;
		return resultXml;
	}
	/**
	 * ������ǰ�û����;����˽ɿ���Ϣ 
	 * @return
	 */
	private BankBody executeG00001(){
		logger.info("*************���нӿ�G00001���Ϳ�ʼ***************");
		BankBody  resultBankBody = new BankBody();
		//1���������˺Ų�ѯ��������˱�ı�֤������£��Һ˶Խ���Ƿ��Ѿ����ˡ�
		//2�������еĸ�����Ϣ���������ݿ⣬��ע�Ᵽ������ʱҪ�˶����н�����ˮ�ţ���֤�˺ŵ�Ψһ�ԣ�
		String inBankFLCode = "";
		String resultCode= "";
		String resultMsg = "";
		if(this.m_clientBankPart.getBankBody()!=null){
			inBankFLCode = this.m_clientBankPart.getBankBody().getInBankFLCode();
			WsjyBankApp bankAppOld = this.wsjyBankAppDao.getBankAppByInBankFLCode(inBankFLCode);
			//�������˺Ų���bidderApp����ҵ���������Ϣ���Ա�֤����Լ���֤����״̬�������¡�
			if(bankAppOld!=null){
				//�������ݿ����Ѿ����ڴ���ˮ�ŵļ�¼���ʾΪ�����ظ���
				resultCode = BankService.ERR01;
				//resultMsg = "�˼�¼�ظ�����!";
				resultMsg = "";
			}else{
				logger.info("*************��ʼ���汣֤����Ϣ***************");
				this.savePersonApp();
				//����Ϊ��������
				resultCode = "00";
				//��¼�����˽��ɱ�֤��
				this.saveBankApp(resultCode);
			}
			resultBankBody.setResult(resultCode);
			resultBankBody.setAddWord(resultMsg);
		}
		logger.info("*************���нӿ�G00001���ͽ���***************");
		return resultBankBody;
	
	}
	/**
	 * ���澺������Ϣ��
	 *
	 */
	private void saveBankApp(String result){
		if(this.m_clientBankPart.getBankBody()!=null){
			//��¼�����˽��ɱ�֤��
			WsjyBankApp bankApp = new WsjyBankApp();
			bankApp.setInacct(this.m_clientBankPart.getBankBody().getInAcct());
			bankApp.setInamout(new BigDecimal(this.m_clientBankPart.getBankBody().getInAmount()));
			bankApp.setInbankflcode(this.m_clientBankPart.getBankBody().getInBankFLCode());
			bankApp.setIndate(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"));
			bankApp.setInmemo(this.m_clientBankPart.getBankBody().getInMemo());
			bankApp.setInname(this.m_clientBankPart.getBankBody().getInName());
			bankApp.setInstcode(this.m_clientBankPart.getBankBody().getInstCode());
			bankApp.setResult(result);
			//�����¼��Ϣ
			this.wsjyBankAppDao.save(bankApp);
		}
	}
	/**
	 * �����˱�֤���ɺ������˱�֤�������
	 *
	 */
	private void savePersonApp(){
		String inMemo = "";
		inMemo = this.m_clientBankPart.getBankBody().getInMemo();
		TdscBidderApp bidderApp = this.tdscBidderAppDao.getBidderAppByInMemo(inMemo);
		if(bidderApp!=null){
			String bidderId = bidderApp.getBidderId();
			String appId = bidderApp.getAppId();
			List personList = this.tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
			TdscBlockTranApp blockTranApp = this.tdscBlockTranAppDao.getMarginEndDate(appId);
			BigDecimal blockBzj = blockTranApp.getMarginAmount();
			Timestamp bzjDate = blockTranApp.getMarginEndDate();
			logger.info("ԭ�б�֤��Ϊ"+blockBzj.toString());
			if(personList!=null&&personList.size()>0){
				TdscBidderPersonApp personApp =	(TdscBidderPersonApp)personList.get(0);
				BigDecimal bzj = personApp.getBzjDzse();
				if(bzj==null){bzj=new BigDecimal(0);}
				BigDecimal newBzj = new BigDecimal(this.m_clientBankPart.getBankBody().getInAmount());
				//�������н��ɱ�֤��ʱ����10000
				BigDecimal result = bzj.add(newBzj.divide(new BigDecimal(10000),6, BigDecimal.ROUND_UP));
				personApp.setBzjDzse(result);
				if(blockBzj.compareTo(result)>0){
					personApp.setBzjDzqk("01");//δ����
				}else{
					if(!bzjDate.after(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"))){
						logger.info("����֤�����ڵ���");
						personApp.setBzjDzqk("02");//���ڵ���
					}else{
						logger.info("����֤����������");
						personApp.setBzjDzqk("00");//��֤����������
						personApp.setBzjDzsj(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"));
						//�����ʸ�֤����,�Զ�ѡ�����
						if(StringUtils.isEmpty(bidderApp.getCertNo())){
							String certNo = subscribeService.generateCertNo(bidderApp.getNoticeId(),bidderApp.getUserId());
							bidderApp.setCertNo(certNo);
							bidderApp.setAcceptNo(certNo);
							bidderApp.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
							bidderApp.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
						}
						bidderApp.setIfCommit("1");
						TdscBidderApp sameConNumObj = subscribeService.getSameNoticeConNum(bidderApp.getNoticeId(),bidderApp.getUserId());
						if(sameConNumObj!=null){
							bidderApp.setConNum(sameConNumObj.getConNum());
							bidderApp.setConTime(sameConNumObj.getConTime());
							if ("1".equals(bidderApp.getIsPurposePerson())) {
								String purposeAppId = bidderApp.getAppId();
								this.tradeServer.insertListingAppOfPurposePerson(bidderApp, purposeAppId);
							}
						}
						tdscBidderAppDao.update(bidderApp);
					}
				}
				this.tdscBidderPersonAppDao.update(personApp);
				CoreService.reloadClientPipe(bidderApp.getUserId());
			}
		}
	}
	/**
	 * ---------------------------------------------------
	 * ---------------------------------------------------
	 * --------------------�ͻ��˷�����ʼ--------------------
	 * ---------------------------------------------------
	 * ---------------------------------------------------
	 */
	
	
	
	/**
	 * �����з�����Ϣ��ȡ����������˺�
	 * @param tranApp
	 * @param bidderApp
	 * @param dicBank
	 * @throws Exception
	 */
	public void sendClientG00003(String bankCode,String bidderId) throws Exception{
		logger.info("*************���нӿ�G00003���Ϳ�ʼ***************");
		TdscBidderApp bidderApp =(TdscBidderApp)this.tdscBidderAppDao.get(bidderId);
		String appId = bidderApp.getAppId();
		TdscBlockTranApp tranApp = this.tdscBlockTranAppDao.getMarginEndDate(appId);
		DicBank dicBank =  this.dicBankDao.getDataById(bankCode);
		logger.info("-------���нӿ�ҵ���������------");
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		logger.info("-------���ӿͻ���IPΪ"+dicBank.getBankIp()+"------");
		BankClient bankClient = new BankClient(server);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setInstSeq(bidderApp.getSqNumber());
		body.setMatuDay(DateUtil.timestamp2String(tranApp.getMarginEndDate(), "yyyyMMddHHmmss"));
		BankPart result = bankClient.sendClient(BankClient.G00003, body);
		if(result!=null){
			logger.info("*************resultȡֵ����***************");
			if(result.getBankHead()!=null){
				logger.info("*************headȡֵ����***************");
			}
			if(result.getBankBody()!=null){
				logger.info("*************Bodyȡֵ����***************");
			}
		}
		logger.info("*************���нӿ�G00003��������***************");
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01",bidderApp.getUserId(), bankClient.getRecallXml(), "01");
		//ȡ�����˺ź󱣴������ݿ�
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00003.equals(clientCode)){
				throw new Exception("��ִ��Ϣҵ���������TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("��ִ��Ϣ���󣬷��ش������Ϊ"+result.getBankBody().getResult());
			}
			//��ȡ���˺�
			String acctNo = result.getBankBody().getAcctNo();
			bidderApp.setBankNumber(acctNo);
			this.tdscBidderAppDao.update(bidderApp);
		}else{
			throw new Exception("��ִ��Ϣ����");
		}
		
	}
	/**
	 * �����з�����Ϣ����ȡ�����˺���Ϣ�����б�
	 * @param bidderApp
	 * @param dicBank
	 * @return
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public BankPart sendClientG00005(String bankId,String bidderId) throws Exception{
		logger.info("*************���нӿ�G00005���Ϳ�ʼ***************");
		TdscBidderApp bidderApp =(TdscBidderApp)this.tdscBidderAppDao.get(bidderId);
		DicBank dicBank = (DicBank) this.dicBankDao.get(bankId);
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		BankClient bankClient = new BankClient(server);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setInMemo(bidderApp.getBankNumber());
		BankPart result = bankClient.sendClient(BankClient.G00005, body);
		logger.info("*************���нӿ�G00005��������***************");
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01", bidderApp.getUserId(), bankClient.getRecallXml(), "01");
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00005.equals(clientCode)){
				throw new Exception("��ִ��Ϣҵ���������TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("��ִ��Ϣ���󣬷��ش������Ϊ"+result.getBankBody().getResult());
			}
		}
		return result;
	}
	/**
	 * δ�����˹鼯֪ͨ
	 *
	 */
	private void sendClientG00007(List bidderList) throws Exception{
		logger.info("*************���нӿ�G00007��ʼ����***************");
		//�Ѿ������������
		ArrayList oldBank = new ArrayList();
		//����ͬ���еľ����˷���ͬʱ����
		for(int i=0;i<bidderList.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)bidderList.get(i);
			if(oldBank.indexOf(bidderApp.getBankId())==-1){
				List list = this.fixBank(bidderList, bidderApp.getBankId());
				this.sendClientG00007(bidderApp.getBankId(),list);
				oldBank.add(bidderApp.getBankId());
			}
		}
		logger.info("*************���нӿ�G00007��������***************");
	}
	/**
	 * 
	 * ��δ�������������з�����Ϣ�����һ������ ��
	 * @param bankId
	 * @param bidderList
	 * @throws Exception
	 */
	private void sendClientG00007(String bankId,List bidderList) throws Exception{
		DicBank dicBank = (DicBank) this.dicBankDao.get(bankId);
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		BankClient bankClient = new BankClient(server);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setCount(String.valueOf(bidderList.size()));
		BankEntityList bankEntityList = new BankEntityList();
		List list = new ArrayList();
		for(int i=0;i<bidderList.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)bidderList.get(i);
			BankEntity entity = new BankEntity();
			entity.setInMemo(bidderApp.getBankNumber());
			list.add(entity);
		}
		bankEntityList.setBankEntityList(list);
		body.setBankEntityList(bankEntityList);
		BankPart result = bankClient.sendClient(BankClient.G00007, body);
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01", "", bankClient.getRecallXml(), "01");
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00007.equals(clientCode)){
				throw new Exception("��ִ��Ϣҵ���������TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("��ִ��Ϣ���󣬷��ش������Ϊ"+result.getBankBody().getResult());
			}
		}
	}
	/**
	 * ��������idɸѡ�б�
	 * @param bidderList
	 * @return
	 */
	private List fixBank(List bidderList,String bankId){
		ArrayList al = new ArrayList();
		for(int i=0;i<bidderList.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)bidderList.get(i);
			if(bankId.equals(bidderApp.getBankId())){
				al.add(bidderApp);
			}
		}
		return al;
	}
	/**
	 * �����˹鼯֪ͨ
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 *
	 */
	private void sendClientG00008(TdscBidderApp bidderApp) throws Exception{
		String bankId = bidderApp.getBankId();
		DicBank dicBank = (DicBank) this.dicBankDao.get(bankId);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setInMemo(bidderApp.getBankNumber());
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		BankClient bankClient = new BankClient(server);
		BankPart result = bankClient.sendClient(BankClient.G00008, body);
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01", "", bankClient.getRecallXml(), "01");
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00008.equals(clientCode)){
				throw new Exception("��ִ��Ϣҵ���������TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("��ִ��Ϣ���󣬷��ش������Ϊ"+result.getBankBody().getResult());
			}
		}
	}
	/**
	 * ����������·�ӿ�
	 *
	 */
	public void sendClientG00009(){
		
	}
	
	/**
	 * ���׽���ʱ������ʱִ��
	 * @throws Exception 
	 *
	 */
	public void sendClientByTranEnd(String appId) throws Exception{
		TdscBlockTranApp tranApp = this.tdscBlockTranAppDao.getMarginEndDate(appId);
		List bidderAppList = this.tdscBidderAppDao.queryBidderAppListLikeAppId(appId);
		//������
		String resultName = tranApp.getResultName();
		TdscBidderApp resultBidderApp = null;
		//δ������
		ArrayList al = new ArrayList();
		for(int i=0;i<bidderAppList.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
			TdscBidderPersonApp person = this.getTdscBidderPersonAppDao().getOneBidderByBidderId(bidderApp.getBidderId());
			if(person!=null){
				if(person.getBidderName()!=null&&person.getBidderName().equals(resultName)){
					resultBidderApp = bidderApp;
				}else{
					al.add(bidderApp);
					
				}
			}

		}
		logger.info("*************���ý���ʱҵ��������:δ������"+al.size()+"***************");
		if(resultBidderApp!=null){
			//���;�������Ϣ
			this.sendClientG00008(resultBidderApp);
		}
		//����δ��������Ϣ
		this.sendClientG00007(al);
	}
	/**
	 * �������нӿڵ�LOG
	 * @param bankId
	 * @param bankIp
	 * @param bankPort
	 * @param content
	 * @param logType
	 * @param userId
	 */
	public void saveWsjyBankLog(String bankIp,String content,String logType,String userId,String recallXml,String result){
		WsjyBankLog bankLog = new WsjyBankLog();
		bankLog.setLogBankIp(bankIp);
		bankLog.setLogBankXml(content);
		bankLog.setLogDate(new Date());
		bankLog.setLogType(logType);
		bankLog.setLogUserId(userId);
		bankLog.setLogRecallXmll(recallXml);
		bankLog.setLogResult(result);
		this.wsjyBankLogDao.save(bankLog);
	}
	
	public WsjyBankAppDao getWsjyBankAppDao() {
		return wsjyBankAppDao;
	}
	public void setWsjyBankAppDao(WsjyBankAppDao wsjyBankAppDao) {
		this.wsjyBankAppDao = wsjyBankAppDao;
	}
	public TdscBlockTranAppDao getTdscBlockTranAppDao() {
		return tdscBlockTranAppDao;
	}
	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
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
	
	
	
}
