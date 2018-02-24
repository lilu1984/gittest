package com.wonders.tdsc.out.service;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.safehaus.uuid.UUIDGenerator;

import com.wonders.esframework.common.util.StringUtil;
import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.WebDocumentFile;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.util.MOD37_2;
import com.wonders.util.PropertiesUtil;
import com.wonders.util.VelocityUtils;
import com.wonders.wsjy.bo.PersonInfo;

public class Export4PubPlatService extends BaseService {
	
	private String attSetCode = "";
	
	private String serialCode = "";
	
	private TdscNoticeService tdscNoticeService;
	
	private TdscScheduletableService tdscScheduletableService;
	
	private TdscBlockInfoService tdscBlockInfoService;
	
	private TdscFileService tdscFileService;
	
	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setTdscScheduletableService(
			TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	/**
	 * �������ݰ�
	 * @param busId(appId or noticeId)
	 * @param flag
	 * @throws Exception
	 */
	public void makeVm(String busId, int flag) throws Exception{
		Map<String, Object> headdata = new HashMap<String, Object>();
		Map<String, Object> descontext = new HashMap<String, Object>();
		List<Map<String, Object>> desbinarys = new ArrayList<Map<String, Object>>();
		Map<String, Object> outMap = new HashMap<String, Object>();
		String vmTemplate = "";
		serialCode = this.genSerialCode();
		//�����ļ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String middle = serialCode;
		String end = flag == 1 ? "600" : (flag == 2 ? "601" : "603");
		String path = sdf.format(new Date()) + "-" + middle + "-" + end;
		File directory = new File(GlobalConstants.XML_PATH + path);
		if(!directory.exists()) directory.mkdirs();
		
		headdata = this.makeHead(headdata, flag);
		
		switch (flag) {
		case 1:
			logger.info("-------��ʼ���ɳɽ���Ϊ��Ϣ--------");
			vmTemplate = GlobalConstants.TRADE_RESULT_INFO;//�ɽ���Ϊ��Ϣ
			descontext = this.makeResultNotice(descontext, busId);
			break;
		case 2:
			logger.info("-------��ʼ���ɳɽ��ڵ���Ϣ--------");
			vmTemplate = GlobalConstants.RESULT_BLOCK_INFO;//�ɽ��ڵ���Ϣ
			descontext = this.makeResultBlock(descontext, busId);
			break;
		case 3:
			vmTemplate = GlobalConstants.TRADE_MATERIALS;//����
			break;
		default:
			break;
		}
//		desbinarys = this.makeResultFiles(desbinarys, busId, flag, path);
//		descontext.put("attSetCode", attSetCode.substring(0, attSetCode.lastIndexOf(",")));
		outMap.put("headdata", headdata);
		outMap.put("descontext", descontext);
		outMap.put("desbinarys", desbinarys);
		String str = VelocityUtils.merge(outMap, vmTemplate);
		
		try {
			File xmlFile = new File(directory + "/" + path + ".xml");
			if(!StringUtil.isEmpty(str)){
				OutputStream os = new FileOutputStream(xmlFile);
				OutputStreamWriter osw = new OutputStreamWriter(os, "GBK");
				osw.write(str);
				osw.flush();
				osw.close();
			}//����ļ��������
			logger.info("-------xml�������---------");
			//��ʼ����zip
			File zip = new File(GlobalConstants.ZIP_PATH + File.separator + path + ".zip");
			InputStream input = null;
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zip));
			zipOut.setComment(directory.getName());
			if(directory.isDirectory()){
				File[] files = directory.listFiles();
				for(int d = 0; d < files.length; d++){
					input = new FileInputStream(files[d]);
					zipOut.putNextEntry(new ZipEntry(files[d].getName()));
					int l;
					while((l = input.read()) != -1){
						zipOut.write(l);
					}
					input.close();
				}
			}
			zipOut.close();
			logger.info("-----------zip���������------------");
			//�������÷��۸�ϵͳ��ͬ�����ܣ������ڱ���wxmh������
			//transfer2Wxmh(zip, PropertiesUtil.getInstance().getProperty("httpclient_platform_attachFile_url"));
			//logger.info("---------zip���������----------");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
            
	/**
	 * �����ݰ����͸�ǰ�û�
	 * @param zip
	 * @param url
	 */
	private void transfer2Wxmh(File zip, String url) {
		logger.info("<----------��ʼ����zip��--------->·����"+zip.getAbsolutePath()+"---��ַ��"+url);
		PostMethod filePost = new PostMethod(url);
		try {
			Part[] parts = { new FilePart(zip.getName(), zip) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				logger.info("<----------���ͳɹ���---------->");
			} else {
				logger.info("<----------����ʧ�ܣ�---------->");
			}
		} catch (Exception e) {
			logger.error(e);
		} finally{
			filePost.releaseConnection();
		}
	}

	/**
	 * ����xmlͷ�ڵ�
	 * @param h
	 * @param flag
	 * @return
	 */
	private Map<String, Object> makeHead(Map<String, Object> h, int flag) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		h.put("fromPlatFormCode", this.FROMPLATFORMCODE);
		h.put("toPlatFormCode", this.TOPLATFORMCODE);
		switch(flag){
		case 1:
			h.put("dataNo", this.DATA_NO_N);
			h.put("dataEn", this.DATA_EN_N);
			h.put("dataCn", this.DATA_CN_N);
			break;
		case 2:
			h.put("dataNo", this.DATA_NO_B);
			h.put("dataEn", this.DATA_EN_B);
			h.put("dataCn", this.DATA_CN_B);
			break;
		case 3:
			h.put("dataNo", this.DATA_NO_M);
			h.put("dataEn", this.DATA_EN_M);
			h.put("dataCn", this.DATA_CN_M);
			break;
		}
		h.put("dataType", this.DATA_TYPE);
		h.put("dataKey", UUIDGenerator.getInstance().generateRandomBasedUUID());
		h.put("timeStamp", sdf.format(new Date()));
		logger.info("---------ͷ�ڵ��������-----------");
		return h;
	}

	/**
	 * ���ɳɽ���Ϊ��Ϣ���ݽڵ�
	 * @param m
	 * @param busId
	 * @return
	 */
	private Map<String, Object> makeResultNotice(Map<String, Object> m, String busId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat min = new SimpleDateFormat("yyyyMMddHHmm");
			TdscNoticeApp notice = this.tdscNoticeService.findNoticeAppByNoticeId(busId);
			List appList = this.tdscNoticeService.findTdscBlockTranAppByNoticeNo(notice.getNoticeId(), notice.getNoticeNo());
			String htmPath = PropertiesUtil.getInstance().getProperty("html.save.path");
			File htmFile = new File(htmPath + notice.getRecordId() + ".htm");
			InputStream is = new FileInputStream(htmPath + notice.getRecordId() + ".htm");
			byte[] b = new byte[Integer.parseInt(htmFile.length()+"")];
			int size = is.read(b);
			String htm = new String(b,0,size);
			if(appList == null || appList.size() == 0)
				return null;
			TdscBlockTranApp app = (TdscBlockTranApp) appList.get(0);
			TdscBlockPlanTable plan = this.tdscScheduletableService.findPlanTableByPlanId(app.getPlanId());
			m.put("guid", this.reGUID(notice.getNoticeId()));
			m.put("dealCode", this.genDealCode());
			m.put("annCode", notice.getNoticeNo());
			m.put("annTitle", notice.getNoticeName());
			m.put("annConnect", this.changeHTML(htm));
			m.put("annType", this.ANNOUNCEMENT_TYPE);
			m.put("landProjectName", notice.getNoticeNo());
			m.put("tenderProjectType", this.TENDER_PROJECT_TYPE);
			m.put("publishingTime", day.format(notice.getNoticeDate()));
			m.put("url", this.URL + notice.getRecordId() + ".html");
			m.put("endDate", day.format(plan.getIssueEndDate()));
			m.put("annStartTime", day.format(plan.getIssueStartDate()));
			m.put("annDeadline", day.format(plan.getIssueEndDate()));
			m.put("listingStartTime", min.format(plan.getListStartDate()));			
			m.put("listingDeadline", min.format(plan.getListEndDate()));
			m.put("listingType", this.LISTING_TYPE);
			m.put("supplyType", this.SUPPLY_TYPE);
			m.put("fieldNum", appList.size()+"");
			m.put("landDistrict", this.LAND_DISTRICT);
			m.put("annUnit", this.UNIT);
			m.put("liaisonUnit", this.UNIT);
			m.put("unitAddress", this.ADDRESS);
			m.put("zipCode", this.ZIP_CODE);
			m.put("contactPerson", plan.getUserId());
			m.put("contactNumber", this.PHONE);
			m.put("email", this.EMAIL);
			m.put("cancelReason", "��");
			m.put("createTime", day.format(new Date()));
			m.put("uploadTime", day.format(new Date()));
			m.put("changeTime", day.format(new Date()));
			m.put("retreatTime", day.format(new Date()));
			m.put("attSetCode", null);//�������set
			m.put("platformCode", this.FROMPLATFORMCODE);
			m.put("pubServicePlatCode", this.TOPLATFORMCODE);
			m.put("dataTimestamp", sdf.format(new Date()));
		} catch (Exception e) {
			logger.error("--------�ɽ���Ϊ��Ϣ���ɱ���---------"+e.getMessage());
		}
		return m;
	}
	
	private String changeHTML(String htm) {
//		htm = htm.substring(htm.indexOf("<body"), htm.indexOf("</html>"));
		htm=htm.replaceAll("\r", "");
		htm=htm.replaceAll("\n", "");
		htm=htm.replaceAll(">", "&#62;");
		htm=htm.replaceAll("<", "&#60;");
		htm=htm.replaceAll("&nbsp;", "&#160;");
//		htm=htm.replaceAll(" ", "&nbsp;");
		return htm;
	}

	/**
	 * ���ɸ������ڵ㣨���棩
	 * @param l
	 * @param busId
	 * @param flag
	 * @param path
	 * @return
	 */
	private List<Map<String, Object>> makeResultFiles(List<Map<String, Object>> l, String busId, int flag, String path){
		Map<String, Object> r = new HashMap<String, Object>();
		if(flag == 1){
			TdscNoticeApp n = this.tdscNoticeService.getTdscNoticeAppByNoticeId(busId);
			String guid = UUIDGenerator.getInstance().generateRandomBasedUUID().toString();
			attSetCode += guid + ",";
			WebDocumentFile f = this.tdscFileService.getWebFileByRecordId(n.getRecordId());
			r.put("attSetCode", "LEND_NOTICE");
			r.put("attCount", "1");
			r.put("attCode", guid);
			r.put("attName", n.getNoticeNo()+"���ù���");
			r.put("attType", f.getFileType());
			r.put("md5", this.genFileMD5(f.getFileBody()));
			r.put("attFileName", guid);
			r.put("url", this.URL + n.getResultRecordId() + ".html");
			this.createFile(f, path, guid);
			l.add(r);
		}else if(flag == 2){
			TdscBlockTranApp app = this.tdscBlockInfoService.findTdscBlockTranApp(busId);
			TdscNoticeApp n = this.tdscNoticeService.getTdscNoticeAppByNoticeId(app.getNoticeId());
			String guid = UUIDGenerator.getInstance().generateRandomBasedUUID().toString();
			attSetCode += guid + ",";
			WebDocumentFile f = this.tdscFileService.getWebFileByRecordId(n.getResultRecordId());
			r.put("attSetCode", "RESULT_LAND_NOTICE");
			r.put("attCount", "1");
			r.put("attCode", guid);
			r.put("attName", n.getNoticeNo()+"�ɽ���ʾ");
			r.put("attType", f.getFileType());
			r.put("md5", this.genFileMD5(f.getFileBody()));
			r.put("attFileName", guid);
			r.put("url", this.URL + n.getResultRecordId() + ".html");
			this.createFile(f, path, guid);
			l.add(r);
		}
		return makeBlockFiles(l, busId, flag, path);
	}
	
	/**
	 * ���ɸ������ڵ㣨�ؿ飩
	 * @param l
	 * @param busId
	 * @param flag
	 * @param path
	 * @return
	 */
	private List<Map<String, Object>> makeBlockFiles(List<Map<String, Object>> l, String busId, int flag, String path) {
		//�����ļ���webDocumentFile���appId��blockFileApp����recordId���= =��
		if(flag == 1){
			TdscNoticeApp n = this.tdscNoticeService.getTdscNoticeAppByNoticeId(busId);
			List appList = this.tdscNoticeService.findTdscBlockTranAppByNoticeNo(n.getNoticeId(), n.getNoticeNo());
			if(appList == null || appList.size() == 0) return l;
			for(int i = 0; i < appList.size(); i++){
				TdscBlockTranApp app = (TdscBlockTranApp) appList.get(i);
				TdscBlockFileApp fileApp = this.tdscFileService.getBlockFileAppById(app.getAppId());
				WebDocumentFile blockFile = this.tdscFileService.getWebFileByRecordId(fileApp.getRecordId());
				String blockFileGuid = UUIDGenerator.getInstance().generateRandomBasedUUID().toString();
				attSetCode += blockFileGuid + ",";
				Map<String, Object> r = new HashMap<String, Object>();
				r.put("attSetCode", "LEND_SOLUTION_PERMIT_FILE");
				r.put("attCount", "1");
				r.put("attCode", blockFileGuid);
				r.put("attName", blockFile.getFileName()+"�����ļ�");
				r.put("attType", blockFile.getFileType());
				r.put("md5", this.genFileMD5(blockFile.getFileBody()));
				r.put("attFileName", blockFileGuid);
				r.put("url", "http://58.215.18.116/wsjy/trade/downFile.jsp?appId="+busId);
				this.createFile(blockFile, path, blockFileGuid);
				l.add(r);
			}
		}else if(flag == 2){
			//�����ļ�
			TdscBlockFileApp fileApp = this.tdscFileService.getBlockFileAppById(busId);
			WebDocumentFile blockFile = this.tdscFileService.getWebFileByRecordId(fileApp.getRecordId());
			String blockFileGuid = UUIDGenerator.getInstance().generateRandomBasedUUID().toString();
			attSetCode += blockFileGuid + ",";
			Map<String, Object> r = new HashMap<String, Object>();
			r.put("attSetCode", "LEND_SOLUTION_PERMIT_FILE");
			r.put("attCount", "1");
			r.put("attCode", blockFileGuid);
			r.put("attName", blockFile.getFileName()+"�����ļ�");
			r.put("attType", blockFile.getFileType());
			r.put("md5", this.genFileMD5(blockFile.getFileBody()));
			r.put("attFileName", blockFileGuid);
			r.put("url", "http://58.215.18.116/wsjy/trade/downFile.jsp?appId="+busId);
			this.createFile(blockFile, path, blockFileGuid);
			l.add(r);
		}
		return l;
	}

	/**
	 * ���ɳɽ��ڵ���Ϣ���ݽڵ�
	 * @param m
	 * @param busId
	 * @return
	 */
	private Map<String, Object> makeResultBlock(Map<String, Object> m, String busId) {
		/**       ���ܻ���ƥ�䲻�ϵ����������
		 * 			select a.tran_result,  b.app_id
		 *          from tdsc_block_tran_app a, tdsc_bidder_app b, tdsc_bidder_person_app c, wx_person_info d
		 *         where a.app_id = b.app_id
		 *           and a.tran_result = '01'--�ɽ��ؿ�
		 *           and b.bidder_id = c.bidder_id
		 *           and c.bidder_name = d.bidder_name		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		TdscBlockTranApp app = this.tdscBlockInfoService.findTdscBlockTranApp(busId);
		TdscBlockInfo block = this.tdscBlockInfoService.findBlockInfo(app.getBlockId());
		TdscBlockPart part = this.tdscBlockInfoService.getBlockInfoPart(app.getBlockId());
		TdscBidderApp bidder = this.tdscBlockInfoService.getTdscBidderAppByAppId(app.getAppId());
		TdscBidderPersonApp person = this.tdscBlockInfoService.getTdscBidderPersonAppByBidderId(bidder.getBidderId());
		PersonInfo wxPerson = this.tdscBlockInfoService.getWxPersonInfo(person.getBidderName());
		TdscNoticeApp notice = this.tdscNoticeService.getTdscNoticeAppByNoticeId(app.getNoticeId());
		if(wxPerson == null) wxPerson = new PersonInfo();
		m.put("dealLandCode", this.reGUID(app.getAppId()));
		m.put("dealGuid", this.reGUID(app.getNoticeId()));
		m.put("dealCode", this.genDealCode());
		m.put("projectCode", this.genProjectCode());
		m.put("landProjectName", app.getBlockNoticeNo());
		m.put("landDistrict", this.LAND_DISTRICT);
		m.put("tenderProjectType", this.TENDER_PROJECT_TYPE);
		m.put("landCode", block.getBlockName());
		m.put("area", block.getTotalLandArea().setScale(6, BigDecimal.ROUND_HALF_UP));
		m.put("usage", part.getLandUseType());
		m.put("age", part.getTransferLife());
		m.put("usageCondition", block.getOtherBuildingCondition());
		m.put("position", block.getLandLocation());
		m.put("resultPrice", app.getResultPrice().setScale(6, BigDecimal.ROUND_HALF_UP));
		m.put("currencyCode", "156");
		m.put("priceUnit", "1");
		m.put("resultPerson", app.getResultName());
		m.put("bidderType", "11".equals(bidder.getBidderType())?"1":("22".equals(bidder.getBidderType())?"2":"9"));
		m.put("bidderCode", wxPerson.getOrgNo() == null ? person.getOrgNo() : wxPerson.getOrgNo());
		m.put("bidderCharacter", "09");
		m.put("supplyAnnTlandCode", this.reGUID(app.getAppId()));
		m.put("landUrl", this.URL + notice.getRecordId() + ".html");
		m.put("publishingTime", sdf.format(notice.getResultPublishDate()));
		m.put("platformCode", this.FROMPLATFORMCODE);
		m.put("pubServicePlatCode", this.TOPLATFORMCODE);
		m.put("dataTimestamp", sdf.format(new Date()));
		return m;
	}

	/**
	 * ����ͳһ���ױ�ʶ��
	 * @return
	 */
	private String genDealCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		MOD37_2 mod = new MOD37_2();
		String hyphen = "-";
		String dealType = "B01";
		String socialCreditCode = this.FROMPLATFORMCODE;
		String dateCode = sdf.format(new Date()).toString();
		String serialCode = this.serialCode;
		String checkCode = mod.compute(dealType+socialCreditCode+dateCode+serialCode);
		checkCode = checkCode.substring(checkCode.length()-1, checkCode.length());
		return dealType + hyphen + socialCreditCode + hyphen + dateCode + hyphen + serialCode + hyphen + checkCode;
	}

	/**
	 * �ļ�MD5����
	 * @param fileBody
	 * @return
	 */
	private String genFileMD5(byte[] fileBody) {
		try {
			MessageDigest MD5	= MessageDigest.getInstance("MD5");
			InputStream in = new ByteArrayInputStream(fileBody);
			int l;
			while((l = in.read(fileBody)) != -1){
				MD5.update(fileBody, 0, l);
			}
			return new String(Hex.encodeHex(MD5.digest())).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �����ļ�
	 * @param f
	 * @param path
	 * @param guid
	 */
	private void createFile(WebDocumentFile f, String path, String guid) {
		try {
			String fileName = guid + f.getFileType();
			File file = new File(GlobalConstants.XML_PATH + path + "/" + fileName);
			byte[] data = f.getFileBody();
			data = new DBstep.iMsgServer2000().ToDocument(f.getFileBody());
			OutputStream os = new FileOutputStream(file);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * �������к�
	 * @return
	 */
	private String genSerialCode(){
		Long xx = tdscBlockInfoService.genIncrementId(GlobalConstants.SERIAL_INTERFACE);
		String x = xx.toString();
		while(x.length() < 6){
			x = "0" + x;
		}
		return x;
	}
	
	/**
	 * ��ϵͳ������ԭ��GUID
	 * @param b
	 * @return
	 */
	private String reGUID(String b){
		b = b.substring(0, 8) + "-" +b.substring(8, 12) + "-" 
			+ b.substring(12, 16) + "-" + b.substring(16, 20) + "-" + b.substring(20);
		return b;
	}
	
	/**
	 * ����Ͷ����Ŀ����
	 * @return
	 */
	private String genProjectCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String front = sdf.format(new Date());
		String districtCode = this.LAND_DISTRICT;
		String industryCode = "4700";
		String projectTypeCode = "B01";
		return front + "-" + districtCode + "-" + industryCode + "-" + projectTypeCode + "-" + this.serialCode.substring(3);
	}
	/** Constants */
	/** ������Դƽ̨��ʶ��*/
	private String FROMPLATFORMCODE = "12320200466372674G";
	/** �����ʹ�ƽ̨��ʶ�� */
	private String TOPLATFORMCODE = "12320000466011756J";
	/** �Խ�������Ϣ������� - �ɽ�����*/
	private String DATA_NO_N = "600";
	/** �Խ�������Ϣ����Ӣ�� - �ɽ����� */
	private String DATA_EN_N = "DEAL_BEHAVIOR_INFO";
	/** �Խ�������Ϣ�������� - �ɽ����� */
	private String DATA_CN_N = "�ɽ���Ϊ��Ϣ";
	/** �Խ�������Ϣ������� - �ɽ��ؿ� */
	private String DATA_NO_B = "601";
	/** �Խ�������Ϣ����Ӣ��  - �ɽ��ؿ�*/
	private String DATA_EN_B = "DEAL_LAND_INFO";
	/** �Խ�������Ϣ�������� - �ɽ��ؿ�*/
	private String DATA_CN_B = "�ɽ��ڵ���Ϣ";
	/** �Խ�������Ϣ������� - ������ */
	private String DATA_NO_M = "603";
	/** �Խ�������Ϣ����Ӣ��  - ������*/
	private String DATA_EN_M = "LAND_ATTACHMENTS";
	/** �Խ�������Ϣ�������� - ������*/
	private String DATA_CN_M = "�������ṹ";
	/** �������� */
	private String DATA_TYPE = "1";
	
	/** ��ʾ���� */
	private String ANNOUNCEMENT_TYPE = "1";
	/** ����url */
	private String URL = "http://landwx.com/wxmh/fabu/yjsc/info/";
	/** ���Ĺ����� */
	private String LISTING_TYPE = "4";
	/** ��Ӧ��ʽ */
	private String SUPPLY_TYPE = "3";
	/** �������� */
	private String LAND_DISTRICT = "320200";
	/** ��ʾ��λ/��ϵ��λ  */
	private String UNIT = "�����й�����Դ��";
	private String ADDRESS = "�������Ļ�·199��";
	private String PHONE = "0510-85726069";
	private String ZIP_CODE = "214023";
	private String EMAIL = "1073058645@qq.com";
	
	/** �б���Ŀ���� */
	private String TENDER_PROJECT_TYPE = "B01";
}
