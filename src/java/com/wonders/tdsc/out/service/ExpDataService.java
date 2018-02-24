package com.wonders.tdsc.out.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wonders.esframework.common.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.out.service.bean.StBlockRecode;
import com.wonders.tdsc.out.service.bean.StData;
import com.wonders.tdsc.out.service.bean.StNoticeRecode;
import com.wonders.tdsc.out.service.bean.StNoticeTableData;
import com.wonders.tdsc.out.service.bean.StPart;
import com.wonders.tdsc.out.service.bean.StSubTableData;
import com.wonders.tdsc.tdscbase.dao.CommonQueryDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.util.PropertiesUtil;

public class ExpDataService{
	
	private Logger logger = Logger.getLogger(ExpDataService.class);
	private String MAP_PATH = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "stDataMap.xml";
	
	TdscBlockPlanTableDao tdscBlockPlanTableDao = ((CommonQueryService)AppContextUtil.getInstance().getAppContext().getBean("commonQueryService")).getTdscBlockPlanTableDao();
	CommonQueryDao commonQueryDao = ((CommonQueryService)AppContextUtil.getInstance().getAppContext().getBean("commonQueryService")).getCommonQueryDao();
	TdscBlockPartDao tdscBlockPartDao = ((CommonQueryService)AppContextUtil.getInstance().getAppContext().getBean("commonQueryService")).getTdscBlockPartDao();
	
	private String noticeId;
	private String noticeNo;
	private String loginName;
	
	
	public ExpDataService(String noticeId,String noticeNo,String loginName){
		this.noticeId = noticeId;
		this.noticeNo = noticeNo;
		this.loginName = loginName;
	}
	/**
	 * 导出上报省厅数据
	 * @param noticeId
	 */
	public String expData(){
		//stxml.save.path
		StPart stPart = this.getData();
		String xml = CastorFactory.marshalBeanByEncoding(stPart, this.MAP_PATH,"UTF-8");
		return xml;
		//CastorFactory.marshalBean(stPart, this.MAP_PATH, xmlFile);
	}
	/**
	 * 获取数据
	 * @param noticeId
	 * @return
	 */
	public StPart getData(){
		StPart stPart = this.newStPart();
		StData stData = new StData();
		ArrayList subDataList = new ArrayList();
		subDataList.add(this.getBlockTableData());
		subDataList.addAll(this.getOtherSunTableData());
		stData.setNoticeData(this.getNoticeData());
		stData.setSubDataList(subDataList);
		stPart.setStData(stData);
		return stPart;
	}
	
	private StPart newStPart(){
		StPart part = new StPart();
		part.setAction("1");
		part.setBusinessType("12");
		part.setDate(this.getCurDate());
		return part;
	}
	/**
	 * 获取公告数据
	 * @return
	 */
	private StNoticeTableData getNoticeData(){
		StNoticeTableData noticeTable = new StNoticeTableData();
		ArrayList recodeList = new ArrayList();
		StNoticeRecode recode = new StNoticeRecode();
		recode.setGyggGuid(noticeId);
		this.setGDValue(recode);
		this.setTdscPlanTable(recode);
		this.setTdscBlockAppView(recode);
		
		recodeList.add(recode);
		noticeTable.setRecodeList(recodeList);
		noticeTable.setTableName("T_REMISE_AFFICHE");
		return noticeTable;
	}
	/**
	 * 设置固定值
	 * @param recode
	 */
	private void setGDValue(StNoticeRecode recode){
		recode.setXzqDm("320201");
		//recode.setPostDate(this.getCurDate());
		recode.setAfficheName("无锡市国有建设用地使用权挂牌出让公告");
		recode.setAuditingStatus("-1");
		recode.setRemiseUnit("无锡市国土资源局");
		recode.setGetfileAddress("无锡国土资源交易网");
		recode.setSignAddress("无锡国土资源交易网");
		recode.setBidAddress("无锡国土资源交易网");
		recode.setCrDd("无锡国土资源交易网");
		recode.setLinkaddress("无锡市文华路199号、无锡市锡山区东亭南路2号、无锡市惠山区文惠路16号、无锡市滨湖区青莲路78号、无锡市新区汉江北路88号");
		recode.setLinkphone("0510-85726069、88210092、83592053、85139152、85259202");
		recode.setAgentStatus("1");
		recode.setPostUser(this.getPostUser());//省厅提供的账号
		recode.setBidTerm("中华人民共和国境内外的法人、自然人和其他组织（法律另有规定或有特殊情况者除外）均可申请参加。经江苏省用地企业诚信数据库比对为非诚信单位的，不具备竞买资格。申请人应当单独申请。");
		recode.setPzJg("无锡市人民政府");
		recode.setSqFs("1");
		recode.setIsnew("1");
		recode.setResourcelb("1");
		recode.setSffb("1");
		recode.setCreateDate(this.getCurDate());
		recode.setEditDate(this.getCurDate());
		recode.setTjSj(this.getCurDate());
	}
	/**
	 * 设置TdscPlanTable有关数据
	 * @param recode
	 */
	private void setTdscPlanTable(StNoticeRecode recode){
		TdscBlockPlanTable planTable = this.getTdscBlockPlanTable();
		if(planTable!=null){
			String transferMode = planTable.getTransferMode();
			transferMode = DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TRANSFER, transferMode);
			recode.setAfficheDate(this.getStDate(planTable.getIssueStartDate()));
			recode.setAfficheEnd(this.getStDate(planTable.getIssueEndDate()));
			recode.setRemiseType(transferMode);
			recode.setConfirmTime(this.getStDate(planTable.getJmzgqrEndDate()));
			recode.setGetfileStarttime(this.getStDate(planTable.getGetFileStartDate()));
			recode.setGetfileEndTime(this.getStDate(planTable.getGetFileEndDate()));
			recode.setSignStarttime(this.getStDate(planTable.getAccAppStatDate()));
			recode.setSignEndtime(this.getStDate(planTable.getAccAppEndDate()));
			recode.setBidStarttime(this.getStDate(planTable.getListStartDate()));
			recode.setBidEndtime(this.getStDate(planTable.getListEndDate()));
			recode.setPaymentStarttime(this.getStDate(planTable.getAccAppStatDate()));
			//recode.setLinkman(planTable.getUserId());
			recode.setGgLx(transferMode);
			recode.setSfwsjy(planTable.getIfOnLine());
			
		}else{
			logger.error("未查找到此公告ID对应的地块数据");
		}
	}
	/**
	 * 设置TdscBlockAppView有关数据
	 * @param recode
	 * @param noticeId
	 */
	private void setTdscBlockAppView(StNoticeRecode recode){
		List list = this.getTdscAppViewList();
		if(list!=null&&list.size()>0){
			TdscBlockAppView appView = (TdscBlockAppView)list.get(0);
			String blockType = this.getStBlockType(appView.getBlockQuality());
			recode.setAfficheNo(appView.getNoitceNo());
			recode.setAfficheType(blockType);
			if("101".equals(appView.getBlockQuality())){
				recode.setLinkman("孙丽新、刘晓霞、虞琪超、华立飞、朱丽娜");
				recode.setOtherTerm("自国有建设用地使用权出让合同签订之日起，竞得人未能在下列时限内取得环境影响评价审批和工业项目审批、核准文件的，国有建设用地使用权出让合同自然中止，所支付的定金按50%及定金以外已支付的土地出让金予以退还（不计利息）：（一）由国家环境保护总局负责环境影响评价审批或者由国家发展和改革委员会负责审批、核准的，时限为12个月；（二）由省环境厅负责环境影响评价审批或者由省级投资行政主管部门负责审批、核准的，时限为9个月；（三）由市、县级环境保护行政主管部门负责环境影响评价审批或者由市、县投资行政主管部门负责审批、核准的，时限为6个月。");
			}else{
				recode.setLinkman("戈虹、刘晓霞、虞琪超、华立飞、朱丽娜");
			}
			recode.setParcelMsg(String.valueOf(list.size()));
			recode.setZdSl(String.valueOf(list.size()));
			recode.setPaymentEndtime(this.getStDate(appView.getMarginEndDate()));
		}
	}
	/**
	 * 获取公告对应的地块数据
	 * @return
	 */
	private StSubTableData getBlockTableData(){
		StSubTableData blockTable = new StSubTableData();
		blockTable.setTableName("T_AFFICHE_PARCEL");
		
		ArrayList recodeList = new ArrayList();
		List list = this.getTdscAppViewList();
		for(int i=0;list!=null&&i<list.size();i++){
			TdscBlockAppView appView = (TdscBlockAppView)list.get(i);
			StBlockRecode recode = new StBlockRecode();
			this.setGDValue(recode);
			this.setTdscBlockAppView(recode, appView);
			this.setTdscPlanTable(recode);
			recodeList.add(recode);
		}
		blockTable.setRecodeList(recodeList);
		return blockTable;
	}
	/**
	 * 设置固定值
	 * @param recode
	 */
	private void setGDValue(StBlockRecode recode){
		recode.setDealType("出让");
		recode.setRemiseUnit("无锡市国土资源局");
		recode.setBidRules("1");
		recode.setSqFs("1");
		recode.setCjfs("01");
		recode.setCjdw("01");
		recode.setSharepercent("7");
		recode.setCrxzfs("00");
		recode.setBidAddress("无锡国土资源交易网");
		//recode.setPostDate(this.getCurDate());
		recode.setTdTjXx("(一)");
		recode.setSffb("1");
		recode.setResourcelb("01");
		recode.setCreateDate(this.getCurDate());
		recode.setEditDate(this.getCurDate());
	}
	/**
	 * 设置地块信息
	 * @param recode
	 */
	private void setTdscBlockAppView(StBlockRecode recode,TdscBlockAppView appView){
		String districtId = String.valueOf(appView.getDistrictId());
		districtId = getStDistrict(districtId);//区县
		String blockType = this.getStBlockType(appView.getBlockQuality());
		String dijia = (appView.getDiJia()!=null)?"1":"0";
		String transferMode = appView.getTransferMode();
		transferMode = DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TRANSFER, transferMode);
		String countUse = appView.getCountUse();//统计用途
		countUse = DicDataUtil.getInstance().getDicItemName(18308, countUse);
		
		
		recode.setGgdk(appView.getAppId());
		recode.setAfficheId(appView.getNoticeId());
		recode.setXzqDm(districtId);
		recode.setAfficheNo(appView.getNoitceNo());
		recode.setAfficheType(blockType);
		recode.setSfwsjy(dijia);
		recode.setParcelNo(appView.getBlockNoticeNo());
		recode.setParcelName(appView.getBlockName());
		recode.setLandPosition(appView.getLandLocation());
		recode.setRemiseType(transferMode);
		recode.setBail(appView.getMarginAmount().toString());
		recode.setLandUse(countUse);
		recode.setTdYt("");//土地用途暂无
		recode.setRemiseArea(appView.getTotalLandArea().toString());
		recode.setUseYear(appView.getTransferLife());
		recode.setStartPrice(appView.getInitPrice().toString());
		recode.setBidScope(appView.getSpotAddPriceRange().toString());
		recode.setPaymentEndtime(this.getStDate(appView.getMarginEndDate()));
		recode.setCdPz(DicDataUtil.getInstance().getDicItemName(18237, appView.getCurrentSituationCondition()));
		
		//处理容积率appView.getVolumeRate()
		List blockPartList = tdscBlockPartDao.getTdscBlockPartList(appView.getBlockId());
		if(blockPartList!=null&&blockPartList.size()>0){
			TdscBlockPart blockPart = (TdscBlockPart)blockPartList.get(0);
			String volumeRate = blockPart.getVolumeRate();
			String volumeRate2 = blockPart.getVolumeRate2();
			String sign = "";
			String sign2 = blockPart.getVolumeRateSign();
			if("06".equals(sign2)){
				//介于(闭区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				sign = "<";
				sign2 = "<";
			}else if("07".equals(sign2)){
				///介于(开区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				sign = "≤";
				sign2 = "≤";
			}else{
				//如果非介于的情况，将数值都填在上限内并且符号开口向右(如:容积率>上限)
				//由于目前系统单个数字都填在下限，所以做出如下设置
				sign2 = DicDataUtil.getInstance().getDicItemName(18313, sign2);
				volumeRate2 = volumeRate;
			}
			recode.setMinRjl(volumeRate);
			recode.setMaxRjl(volumeRate2);
			recode.setSelrjl1(sign);
			recode.setSelrjl2(sign2);
			recode.setCubagePercent(blockPart.getVolumeRateMemo());
			//处理规划建筑密度
			String density1 = blockPart.getDensity1();
			String density2 = blockPart.getDensity2();
			String densitySign = "";
			String densitySign2 = blockPart.getDensitySign();
			if("06".equals(densitySign2)){
				//介于(闭区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				densitySign = "<";
				densitySign2 = "<";
			}else if("07".equals(densitySign2)){
				///介于(开区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				densitySign = "≤";
				densitySign2 = "≤";
			}else{
				//如果非介于的情况，将数值都填在上限内并且符号开口向右(如:容积率>上限)
				//由于目前系统单个数字都填在下限，所以做出如下设置
				densitySign2 = DicDataUtil.getInstance().getDicItemName(18313, densitySign2);
				density2 = density1;
			}
			recode.setMinJzMd(density1);
			recode.setMaxJzMd(density2);
			recode.setMinJzMdTag(densitySign);
			recode.setMaxJzMdTag(densitySign2);
			recode.setSiteCoverage(blockPart.getDensity());
			//处理绿地率
			String greenRate1 = blockPart.getGreeningRate1();
			String greenRate2 = blockPart.getGreeningRate2();
			String greenSign = "";
			String greenSign2 = blockPart.getGreeningRateSign();
			if("06".equals(greenSign2)){
				//介于(闭区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				greenSign = "<";
				greenSign2 = "<";
			}else if("07".equals(greenSign2)){
				///介于(开区间)的情况，系统内没有具体符号标明，所以暂时介于的标准是(下限<容积率<上限)
				greenSign = "≤";
				greenSign2 = "≤";
			}else{
				//如果非介于的情况，将数值都填在上限内并且符号开口向右(如:容积率>上限)
				//由于目前系统单个数字都填在下限，所以做出如下设置
				greenSign2 = DicDataUtil.getInstance().getDicItemName(18313, greenSign2);
				greenRate2 = greenRate1;
			}
			recode.setMinLhl(greenRate1);
			recode.setMaxLhl(greenRate2);
			recode.setMinLhlTag(greenSign);
			recode.setMaxLhlTag(greenSign2);
			recode.setGreenbeltPercent(blockPart.getGreeningRate());
			//出让年限
			recode.setUseYear(blockPart.getTransferLife());
		}
	}
	/**
	 * 设置实施方案信息
	 */
	private void setTdscPlanTable(StBlockRecode recode){
		TdscBlockPlanTable planTable = this.getTdscBlockPlanTable();
		recode.setAfficheDate(this.getStDate(planTable.getIssueStartDate()));
		recode.setAfficheDate(this.getStDate(planTable.getIssueEndDate()));
		recode.setSfwsjy(planTable.getIfOnLine());
		recode.setBidStrattime(this.getStDate(planTable.getListStartDate()));
		recode.setBidEndtime(this.getStDate(planTable.getListEndDate()));
		recode.setSignStartTime(this.getStDate(planTable.getAccAppStatDate()));
		recode.setSignEndtime(this.getStDate(planTable.getAccAppEndDate()));
		recode.setPaymentStarttime(this.getStDate(planTable.getAccAppStatDate()));
	}
	/**
	 * 获取其他子表信息(空字段)
	 * @return
	 */
	 
	private ArrayList getOtherSunTableData(){
		ArrayList al = new ArrayList();
		//附件
		StSubTableData otherTable1 = new StSubTableData();
		otherTable1.setRecodeList(null);
		otherTable1.setTableName("T_FJ");
		al.add(otherTable1);
		//T_ZD_DK
		StSubTableData otherTable2 = new StSubTableData();
		otherTable2.setRecodeList(null);
		otherTable2.setTableName("T_ZD_DK");
		al.add(otherTable2);
		//T_ZD_JZDZB
		StSubTableData otherTable3 = new StSubTableData();
		otherTable3.setRecodeList(null);
		otherTable3.setTableName("T_ZD_JZDZB");
		al.add(otherTable3);
		//T_ZD_ZBSX
		StSubTableData otherTable4 = new StSubTableData();
		otherTable4.setRecodeList(null);
		otherTable4.setTableName("T_ZD_ZBSX");
		al.add(otherTable4);
		return al;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	private String getCurDate(){
		return DateUtil.date2String(new Date(), "yyyy-MM-dd hh:mm:ss");
	}
	
	private List tdscAppViewList = null;
	/**
	 * 得到地块信息数据
	 * @return
	 */
	private List getTdscAppViewList(){
		if(tdscAppViewList==null){
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			List list = this.commonQueryDao.findTdscBlockAppViewListByPlanId(condition);
			tdscAppViewList = list;
		}
		return tdscAppViewList;
	}
	private TdscBlockPlanTable tdscBlockPlanTable;
	/**
	 * 得到实施方案信息数据
	 * @return
	 */
	private TdscBlockPlanTable getTdscBlockPlanTable(){
		if(tdscBlockPlanTable==null){
			List list = getTdscAppViewList();
			if(list!=null&&list.size()>0){
				TdscBlockAppView appView = (TdscBlockAppView)list.get(0);
				String planId = appView.getPlanId();
				TdscBlockPlanTable blockPlanTable = (TdscBlockPlanTable)this.tdscBlockPlanTableDao.get(planId);
				tdscBlockPlanTable = blockPlanTable;
			}
		}
		return tdscBlockPlanTable;
	}
	/**
	 * 处理省厅区县字典
	 * @param districtId
	 * @return
	 */
	private String getStDistrict(String districtId){
		Map map = new HashMap();
		map.put("2", "320284");//惠山区
		map.put("5", "320201");//崇安区
		map.put("6", "320201");//南长区
		map.put("7", "320201");//北塘区
		map.put("1", "320283");//锡山区
		map.put("3", "320202");//滨湖区
		map.put("4", "320203");//无锡新区
		map.put("8", "320209");//梁溪区
		return (String)map.get(districtId);
	}
	/**
	 * 处理土地类型字典
	 * @param code
	 * @return
	 */
	private String getStBlockType(String code){
		if("101".equals(code)){
			return "0";
		}
		if("102".equals(code)){
			return "1";
		}
		return null;
	}
	/**
	 * 处理日期方法
	 * @param time
	 * @return
	 */
	private String getStDate(Timestamp time){
		if(time==null)
			return null;
		Date date = new Date();
		date.setTime(time.getTime());
		String result = DateUtil.date2String(date, "yyyy-MM-dd HH:mm:ss");
		return result;
	}
	/**
	 * 
	 * @return
	 */
	private String getPostUser(){
		if(this.loginName.equals("tangx")){
			return "320201_tangx";
		}
		if(this.loginName.equals("geh")){
			return "320201_geh";
		}
		if(this.loginName.equals("qianwt")){
			return "320201_qianwt";
		}
		if(this.loginName.equals("sunlx")){
			return "320201_sunlx";
		}
		return "";
	}
}
