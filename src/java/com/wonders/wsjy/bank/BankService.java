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
 * 银行接口业务操作类
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
	 * 数据发送重复
	 */
	public static String ERR01 = "01";
	/**
	 * 其他错误
	 */
	public static String ERR09 = "09";
	/**
	 * 系统错误 
	 */
	public static String ERR99 = "99";
	
	private BankPart m_clientBankPart = null;
	/**
	 * 获取 head
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
	 * 向客户端发送错误消息
	 * @param code 发送消息的业务代码
	 * @param msg
	 * @return
	 */
	public String sendException2Client(String code,String msg,String errCode){
		BankPart resultPart = new BankPart();
		resultPart.setBankHead(this.getHead(code));
		BankBody resultBankBody = new BankBody();
		resultBankBody.setResult(errCode);
		//由于一直出现中文乱码问题，所以这里的addword暂时屏蔽.
		resultBankBody.setAddWord("");
		//resultBankBody.setAddWord(msg);
		resultPart.setBankBody(resultBankBody);
		String resultXml = CastorFactory.marshalBean(resultPart, MAP_PATH);
		//		发送报文前补齐6位大小
		resultXml = StringUtils.leftPad(String.valueOf(resultXml.length()), 6, "0") + resultXml;
		return resultXml;
	}
	/**
	 * 服务器端响应客户端的请求，并返回响应消息XML。
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
			//银行向前置机发送竞买人缴款信息 
			if("G00001".equals(this.m_clientBankPart.getBankHead().getTransCode())){
				resultBankBody = this.executeG00001();
			}
		}else{
			throw new EngineException("XML文件信息不完整!");
		}
		resultPart.setBankBody(resultBankBody);
		resultXml = CastorFactory.marshalBean(resultPart, MAP_PATH);
		//发送报文前补齐6位大小
		resultXml = StringUtils.leftPad(String.valueOf(resultXml.length()), 6, "0") + resultXml;
		return resultXml;
	}
	/**
	 * 银行向前置机发送竞买人缴款信息 
	 * @return
	 */
	private BankBody executeG00001(){
		logger.info("*************银行接口G00001发送开始***************");
		BankBody  resultBankBody = new BankBody();
		//1、根据子账号查询竞买表，将此表的保证金金额跟新，且核对金额是否已经足额到账。
		//2、将银行的付款信息保存入数据库，（注意保存入库的时要核对银行交易流水号，保证此号的唯一性）
		String inBankFLCode = "";
		String resultCode= "";
		String resultMsg = "";
		if(this.m_clientBankPart.getBankBody()!=null){
			inBankFLCode = this.m_clientBankPart.getBankBody().getInBankFLCode();
			WsjyBankApp bankAppOld = this.wsjyBankAppDao.getBankAppByInBankFLCode(inBankFLCode);
			//根据子账号查找bidderApp表查找到竞买人信息，对保证金金额，以及保证金交纳状态做出更新。
			if(bankAppOld!=null){
				//加入数据库内已经存在此流水号的记录则表示为接收重复。
				resultCode = BankService.ERR01;
				//resultMsg = "此记录重复发送!";
				resultMsg = "";
			}else{
				logger.info("*************开始保存保证金信息***************");
				this.savePersonApp();
				//设置为正常处理
				resultCode = "00";
				//记录竞买人交纳保证金。
				this.saveBankApp(resultCode);
			}
			resultBankBody.setResult(resultCode);
			resultBankBody.setAddWord(resultMsg);
		}
		logger.info("*************银行接口G00001发送结束***************");
		return resultBankBody;
	
	}
	/**
	 * 保存竞买人信息。
	 *
	 */
	private void saveBankApp(String result){
		if(this.m_clientBankPart.getBankBody()!=null){
			//记录竞买人交纳保证金。
			WsjyBankApp bankApp = new WsjyBankApp();
			bankApp.setInacct(this.m_clientBankPart.getBankBody().getInAcct());
			bankApp.setInamout(new BigDecimal(this.m_clientBankPart.getBankBody().getInAmount()));
			bankApp.setInbankflcode(this.m_clientBankPart.getBankBody().getInBankFLCode());
			bankApp.setIndate(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"));
			bankApp.setInmemo(this.m_clientBankPart.getBankBody().getInMemo());
			bankApp.setInname(this.m_clientBankPart.getBankBody().getInName());
			bankApp.setInstcode(this.m_clientBankPart.getBankBody().getInstCode());
			bankApp.setResult(result);
			//保存记录信息
			this.wsjyBankAppDao.save(bankApp);
		}
	}
	/**
	 * 竞买人保证金交纳后处理竞买人保证金交纳情况
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
			logger.info("原有保证金为"+blockBzj.toString());
			if(personList!=null&&personList.size()>0){
				TdscBidderPersonApp personApp =	(TdscBidderPersonApp)personList.get(0);
				BigDecimal bzj = personApp.getBzjDzse();
				if(bzj==null){bzj=new BigDecimal(0);}
				BigDecimal newBzj = new BigDecimal(this.m_clientBankPart.getBankBody().getInAmount());
				//处理银行交纳保证金时除以10000
				BigDecimal result = bzj.add(newBzj.divide(new BigDecimal(10000),6, BigDecimal.ROUND_UP));
				personApp.setBzjDzse(result);
				if(blockBzj.compareTo(result)>0){
					personApp.setBzjDzqk("01");//未足额到账
				}else{
					if(!bzjDate.after(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"))){
						logger.info("处理保证金逾期到账");
						personApp.setBzjDzqk("02");//逾期到账
					}else{
						logger.info("处理保证金正常到账");
						personApp.setBzjDzqk("00");//保证金正常到账
						personApp.setBzjDzsj(DateUtil.string2Date(this.m_clientBankPart.getBankBody().getInDate(), "yyyyMMddHHmmss"));
						//生成资格证书编号,自动选择号牌
						if(StringUtils.isEmpty(bidderApp.getCertNo())){
							String certNo = subscribeService.generateCertNo(bidderApp.getNoticeId(),bidderApp.getUserId());
							bidderApp.setCertNo(certNo);
							bidderApp.setAcceptNo(certNo);
							bidderApp.setYktXh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡编号
							bidderApp.setYktBh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡芯片号
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
	 * --------------------客户端方法开始--------------------
	 * ---------------------------------------------------
	 * ---------------------------------------------------
	 */
	
	
	
	/**
	 * 向银行发送消息并取得申请的子账号
	 * @param tranApp
	 * @param bidderApp
	 * @param dicBank
	 * @throws Exception
	 */
	public void sendClientG00003(String bankCode,String bidderId) throws Exception{
		logger.info("*************银行接口G00003发送开始***************");
		TdscBidderApp bidderApp =(TdscBidderApp)this.tdscBidderAppDao.get(bidderId);
		String appId = bidderApp.getAppId();
		TdscBlockTranApp tranApp = this.tdscBlockTranAppDao.getMarginEndDate(appId);
		DicBank dicBank =  this.dicBankDao.getDataById(bankCode);
		logger.info("-------银行接口业务调用正常------");
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		logger.info("-------连接客户端IP为"+dicBank.getBankIp()+"------");
		BankClient bankClient = new BankClient(server);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setInstSeq(bidderApp.getSqNumber());
		body.setMatuDay(DateUtil.timestamp2String(tranApp.getMarginEndDate(), "yyyyMMddHHmmss"));
		BankPart result = bankClient.sendClient(BankClient.G00003, body);
		if(result!=null){
			logger.info("*************result取值正常***************");
			if(result.getBankHead()!=null){
				logger.info("*************head取值正常***************");
			}
			if(result.getBankBody()!=null){
				logger.info("*************Body取值正常***************");
			}
		}
		logger.info("*************银行接口G00003发送正常***************");
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01",bidderApp.getUserId(), bankClient.getRecallXml(), "01");
		//取得子账号后保存入数据库
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00003.equals(clientCode)){
				throw new Exception("回执信息业务编码有误TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("回执信息有误，返回错误编码为"+result.getBankBody().getResult());
			}
			//获取子账号
			String acctNo = result.getBankBody().getAcctNo();
			bidderApp.setBankNumber(acctNo);
			this.tdscBidderAppDao.update(bidderApp);
		}else{
			throw new Exception("回执信息有误");
		}
		
	}
	/**
	 * 向银行发送消息并且取得子账号信息付款列表
	 * @param bidderApp
	 * @param dicBank
	 * @return
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public BankPart sendClientG00005(String bankId,String bidderId) throws Exception{
		logger.info("*************银行接口G00005发送开始***************");
		TdscBidderApp bidderApp =(TdscBidderApp)this.tdscBidderAppDao.get(bidderId);
		DicBank dicBank = (DicBank) this.dicBankDao.get(bankId);
		Socket server = new Socket(dicBank.getBankIp(),Integer.parseInt(dicBank.getBankPort()));
		BankClient bankClient = new BankClient(server);
		BankBody body = new BankBody();
		body.setInstCode(dicBank.getDicValue());
		body.setInMemo(bidderApp.getBankNumber());
		BankPart result = bankClient.sendClient(BankClient.G00005, body);
		logger.info("*************银行接口G00005发送正常***************");
		this.saveWsjyBankLog(dicBank.getBankIp(), bankClient.getSendXml(), "01", bidderApp.getUserId(), bankClient.getRecallXml(), "01");
		if(result!=null&&result.getBankHead()!=null&&result.getBankBody()!=null){
			String clientCode = result.getBankHead().getTransCode();
			if(!BankClient.G00005.equals(clientCode)){
				throw new Exception("回执信息业务编码有误TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("回执信息有误，返回错误编码为"+result.getBankBody().getResult());
			}
		}
		return result;
	}
	/**
	 * 未竞得人归集通知
	 *
	 */
	private void sendClientG00007(List bidderList) throws Exception{
		logger.info("*************银行接口G00007开始发送***************");
		//已经处理过的银行
		ArrayList oldBank = new ArrayList();
		//将相同银行的竞买人分组同时处理
		for(int i=0;i<bidderList.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)bidderList.get(i);
			if(oldBank.indexOf(bidderApp.getBankId())==-1){
				List list = this.fixBank(bidderList, bidderApp.getBankId());
				this.sendClientG00007(bidderApp.getBankId(),list);
				oldBank.add(bidderApp.getBankId());
			}
		}
		logger.info("*************银行接口G00007发送正常***************");
	}
	/**
	 * 
	 * 给未竞得人所在银行发送消息，针对一家银行 。
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
				throw new Exception("回执信息业务编码有误TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("回执信息有误，返回错误编码为"+result.getBankBody().getResult());
			}
		}
	}
	/**
	 * 根据银行id筛选列表
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
	 * 竞得人归集通知
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
				throw new Exception("回执信息业务编码有误TransCode");
			}
			if(!"00".equals(result.getBankBody().getResult())){
				throw new Exception("回执信息有误，返回错误编码为"+result.getBankBody().getResult());
			}
		}
	}
	/**
	 * 测试连接线路接口
	 *
	 */
	public void sendClientG00009(){
		
	}
	
	/**
	 * 交易结束时或流标时执行
	 * @throws Exception 
	 *
	 */
	public void sendClientByTranEnd(String appId) throws Exception{
		TdscBlockTranApp tranApp = this.tdscBlockTranAppDao.getMarginEndDate(appId);
		List bidderAppList = this.tdscBidderAppDao.queryBidderAppListLikeAppId(appId);
		//竞得人
		String resultName = tranApp.getResultName();
		TdscBidderApp resultBidderApp = null;
		//未竞得人
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
		logger.info("*************出让结束时业务处理正常:未竞得人"+al.size()+"***************");
		if(resultBidderApp!=null){
			//发送竞得人信息
			this.sendClientG00008(resultBidderApp);
		}
		//发送未竞得人信息
		this.sendClientG00007(al);
	}
	/**
	 * 保存银行接口的LOG
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
