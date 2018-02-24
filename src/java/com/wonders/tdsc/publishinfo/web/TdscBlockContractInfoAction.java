package com.wonders.tdsc.publishinfo.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockContractInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.publishinfo.service.TdscBlockContractInfoService;
import com.wonders.tdsc.publishinfo.web.form.TdscBlockContractInfoForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBlockContractInfoAction extends BaseAction{

    private CommonQueryService commonQueryService;
    
    private TdscBlockInfoService tdscBlockInfoService;
	
    private TdscBlockContractInfoService tdscBlockContractInfoService;
    
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}
	
	public void setTdscBlockContractInfoService(
			TdscBlockContractInfoService tdscBlockContractInfoService) {
		this.tdscBlockContractInfoService = tdscBlockContractInfoService;
	}
	/**
	 * ��ѯ���ú�ͬ�б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryContractInfoList_old(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// ���ظ�ҳ���ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		this.bindObject(condition, tdscBlockContractInfoForm);
		
		//if(tdscBlockForm!=null){
		if(tdscBlockContractInfoForm!=null){
		condition.setContractNum(tdscBlockContractInfoForm.getContractNum());
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
		condition.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
		condition.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
		}
		
        TdscBaseQueryCondition condition2 = new TdscBaseQueryCondition();
        this.bindObject(condition2, tdscBlockContractInfoForm);
        if(tdscBlockContractInfoForm!=null){
        condition2.setContractNum(tdscBlockContractInfoForm.getContractNum());
    	condition2.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
    	condition2.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
    	condition2.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
    	}
        
        //��ѯ���׽����ؿ������
        condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);
        List statusIdList = new ArrayList();
        statusIdList.add("9001");
        statusIdList.add("9002");
        condition.setStatusIDList(statusIdList);
        
        //��ѯ���׳ɹ�������ʧ�ܣ�������ֹ�ĵؿ������
        List statusList = new ArrayList();
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADESUCCESS);
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADEFAILURE);
        statusList.add(GlobalConstants.DIC_ID_STATUS_TRADEEND);
        condition2.setStatusList(statusList);
        
        // ����û���½��������Ϣ
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
            condition2.setDistrictId(String.valueOf(quList.get(0)));
        }else if(quList.size()>1){
            condition.setDistrictIdList(quList);
            condition2.setDistrictIdList(quList);
        }
        
        List viewList1 = tdscBlockContractInfoService.queryTdscBlockAppViewListWithoutNode(condition);
        List viewList2 = tdscBlockContractInfoService.queryTdscBlockAppViewListWithoutNode(condition2);        
        for(int i=0;i<viewList2.size();i++){
            viewList1.add(viewList2.get(i));
        }
        
        //lz+  ���������(�O)����totalLandArea   �滮���������(�O) totalbuildingarea
        TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
        TdscBlockPart tdscBlockPart = new TdscBlockPart();
        List resultList = new ArrayList();
        
        BigDecimal totalArea = new BigDecimal(0.00);//�������
        BigDecimal buildingArea = new BigDecimal(0.00);//�滮�������
        String contrackNum = "";//��ͬ���
        String acceptPeason = "";//������
        
        if(viewList1!=null&&viewList1.size()>0){
        	for(int i=0;i<viewList1.size();i++){
        		tdscBlockAppView = (TdscBlockAppView)viewList1.get(i);
        		if(tdscBlockAppView.getBlockId()!=null&!"".equals(tdscBlockAppView.getBlockId())){
        			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
        			for(int j=0;j<tdscBlockPartList.size();j++){
        				tdscBlockPart = (TdscBlockPart)tdscBlockPartList.get(j);
        				if(tdscBlockPart.getBlockArea()!=null)
        				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
        				if(tdscBlockPart.getPlanBuildingArea()!=null)
        				buildingArea = buildingArea.add(tdscBlockPart.getPlanBuildingArea());
        			}
        			tdscBlockAppView.setTotalLandArea(totalArea);
	        		tdscBlockAppView.setTotalBuildingArea(buildingArea);
        		
        			//���ӳ��ú�ͬ��Ϣ
        			TdscBlockContractInfo TdscBlockContractInfo = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(tdscBlockAppView.getBlockId());
        			if(TdscBlockContractInfo!=null){
	        			contrackNum = TdscBlockContractInfo.getContractNum();
	        			acceptPeason = TdscBlockContractInfo.getAcceptPerson();
	        			//��ͬ��� ����rangeEast�У�������  ���� RangeNorth
		        		tdscBlockAppView.setRangeEast(contrackNum);
		        		tdscBlockAppView.setRangeNorth(acceptPeason);
		        		
		        		resultList.add(tdscBlockAppView);
        			}
        		}
            }
        }
        
        //lz end
        
//      ƴװ��ҳ
        PageList tdscBlockAppPageList = new PageList();
        //tdscBlockAppPageList.setList(viewList1);
        tdscBlockAppPageList.setList(viewList1);

        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        tdscBlockAppPageList = PageUtil.getPageList(viewList1, pageSize,currentPage);
		request.setAttribute("PageList", tdscBlockAppPageList);// ��������ѯ�б�
        
		condition2.setBlockName(StringUtil.ISO88591toGBK(condition2.getBlockName()));
		request.setAttribute("queryAppCondition", condition2);
		return mapping.findForward("queryList");
	}
	
	/**
	 * ��ѯ���ú�ͬ�б�from��ͼTDSC_CONTRACK_INFO_VIEW    lz+   20090713
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryContractInfoList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int currentPage = 0;
		if(!StringUtils.isEmpty(request.getParameter("currentPage"))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		// ���ظ�ҳ���ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		this.bindObject(condition, tdscBlockContractInfoForm);
		
		if(tdscBlockContractInfoForm!=null){
		condition.setContractNum(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getContractNum()));
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getBlockName()));
		condition.setAcceptPerson(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getAcceptPerson()));
		condition.setLandLocation(StringUtil.GBKtoISO88591(tdscBlockContractInfoForm.getLandLocation()));
		}
        
        //��ѯ���׳ɹ��ؿ������
        condition.setStatus(GlobalConstants.DIC_ID_STATUS_TRADESUCCESS);
        condition.setTranResult("01");
        
        List statusIdList = new ArrayList();
        statusIdList.add("9001");
        statusIdList.add("9002");
        condition.setStatusIDList(statusIdList);
        
        // ����û���½��������Ϣ
        List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
        if(quList!=null && quList.size()==1){
            condition.setDistrictId(String.valueOf(quList.get(0)));
        }else if(quList.size()>1){
            condition.setDistrictIdList(quList);
        }
        condition.setOrderKey("contractSignDate");
        condition.setOrderType("desc");
    	//��ȡ�û���Ϣ
    	SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
    	//��ð�ťȨ���б�
    	List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
    	//ת��ΪbuttonMap
    	Map buttonMap = new HashMap();
    	for ( int j = 0; j < buttonList.size() ; j++){
    		String id = (String)buttonList.get(j);
    		buttonMap.put(id, buttonList.get(j));
    	}
    	//�ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
    	if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null){
    		condition.setUserId(user.getUserId());
    	}
        
        List viewList1 = tdscBlockContractInfoService.queryContractInfoListFromView(condition);
        //lz end
        
        // ƴװ��ҳ
        PageList tdscBlockAppPageList = new PageList();
        tdscBlockAppPageList.setList(viewList1);

        int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
        tdscBlockAppPageList = PageUtil.getPageList(viewList1, pageSize,currentPage);
		request.setAttribute("PageList", tdscBlockAppPageList);//��������ѯ�б�
        
		condition.setContractNum(StringUtil.ISO88591toGBK(condition.getContractNum()));
		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		condition.setLandLocation(StringUtil.ISO88591toGBK(condition.getLandLocation()));
		condition.setAcceptPerson(StringUtil.ISO88591toGBK(condition.getAcceptPerson()));
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("queryList");
	}

	
	//��ͬ��ϸ��Ϣ
	public ActionForward toViewContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		String blockId = request.getParameter("blockId");
		String type = request.getParameter("type");
		
		TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		if(blockId!=null&&!"".equals(blockId))
		   tdscBaseQueryCondition.setBlockId(blockId);
		   tdscBlockContractInfo = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
		   tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppViewByBlockId(blockId);
		
		   request.setAttribute("tdscBlockContractInfo", tdscBlockContractInfo);
		   request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		   request.setAttribute("type", type);
		
		   if("xiugai".equals(type))
		       return mapping.findForward("toEditContractInfo");
		   else
			   return mapping.findForward("toViewContractInfo");
		
     }
	
	
	//��ͬ��ϸ��Ϣ����
	public ActionForward toSaveContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		// ��ȡҳ�����
		TdscBlockContractInfoForm tdscBlockContractInfoForm = (TdscBlockContractInfoForm)form;
		
		String blockId = request.getParameter("blockId");
		String contrackInfoId = request.getParameter("contrackInfoId");
		if(blockId!=null&&!"".equals(blockId)){
			TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
			
			//�ļ��ϴ�start
	        String appRealPath = request.getSession().getServletContext().getRealPath("/");
	        String fileName = null;
	        String savePath = null;//�����ļ����ļ���·��
			String filePath = null;//������ļ�·��
			String endStyle = null;
			String contractNum = tdscBlockContractInfoForm.getContractNum();//��Ϊ�ļ����Ʊ���
			
			FormFile file = null;
			if(tdscBlockContractInfoForm.getFile().getFileName()!=null&&!"".equals(tdscBlockContractInfoForm.getFile().getFileName())&&!"null".equals(tdscBlockContractInfoForm.getFile().getFileName())){
				try {
					file = tdscBlockContractInfoForm.getFile();//ȡ���ϴ����ļ�
					InputStream stream = file.getInputStream();//���ļ�����
					String uplodFilePathName = file.getFileName();
		            fileName = uplodFilePathName.substring(uplodFilePathName.lastIndexOf(File.separator) + 1);
					endStyle = fileName.substring(uplodFilePathName.lastIndexOf("."));
		            //filePath = appRealPath+"dk"+File.separator+"upload"+File.separator+"tempFile"+File.separator+System.currentTimeMillis()+endStyle;
					
					//�ļ���·��
					savePath = appRealPath+"tdsc"+File.separator+"uploadfile"+File.separator+"contrackfile";
					filePath = appRealPath+"tdsc"+File.separator+"uploadfile"+File.separator+"contrackfile"+File.separator+contractNum+endStyle;
					//Ϊ�˱�֤Ŀ¼����,���û�����½���Ŀ¼
					File saveDir  = new File(savePath);
		            if (!saveDir .exists()) {
		            	saveDir .mkdirs();
					}
		            //�����ļ�
		            File fileOnServer = new File(filePath);
					OutputStream bos = new FileOutputStream(fileOnServer);
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
						bos.write(buffer, 0, bytesRead);
					}
					
				} catch (Exception e) {
		            e.printStackTrace();
		        }
		    } else {
		        System.out.println("the enctype must be multipart/form-data");
		    }
			//�ļ��ϴ�end
			
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		
		tdscBlockContractInfo.setActionDate(new Timestamp(System.currentTimeMillis()));
		tdscBlockContractInfo.setActionUser(StringUtils.trimToEmpty(user.getDisplayName()));
		tdscBlockContractInfo.setIfValidity("1");
		tdscBlockContractInfo.setBlockId(blockId);
		tdscBlockContractInfo.setContractInfoId(contrackInfoId);
		
		//���汣����ļ����ƺ�·��
		if(tdscBlockContractInfoForm.getFile().getFileName()!=null&&!"".equals(tdscBlockContractInfoForm.getFile().getFileName())&&!"null".equals(tdscBlockContractInfoForm.getFile().getFileName())){
			tdscBlockContractInfo.setContractFileName(contractNum + StringUtils.trimToEmpty(endStyle));
			tdscBlockContractInfo.setContractFileUrl(StringUtils.trimToEmpty(filePath));
		}else{
			TdscBlockContractInfo tdscBlockContractInfoOld = (TdscBlockContractInfo)tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
			if(tdscBlockContractInfoOld!=null){
				tdscBlockContractInfo.setContractFileName(StringUtils.trimToEmpty(tdscBlockContractInfoOld.getContractFileName()));
				tdscBlockContractInfo.setContractFileUrl(StringUtils.trimToEmpty(tdscBlockContractInfoOld.getContractFileUrl()));
			}
		}
		
		bindObject(tdscBlockContractInfo, tdscBlockContractInfoForm);
		
		tdscBlockContractInfoService.save(tdscBlockContractInfo);
		}
		
		return queryContractInfoList(mapping,null,request,response);
		
     }
	
	/**
	 * ��ͬ��������  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void fileDownLand(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		// ��ȡҳ�����
		String blockId = request.getParameter("blockId");
		TdscBlockContractInfo tdscBlockContractInfo = new TdscBlockContractInfo();
		
		if(blockId!=null&&!"".equals(blockId)){
			tdscBlockContractInfo = tdscBlockContractInfoService.findContractInfoByBlockId(blockId);
			String filePath = tdscBlockContractInfo.getContractFileUrl();
			
			
			File f = new File(filePath);
			if(!f.exists()){
			response.sendError(404,"File not found!");
			return;
			}
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset(); //�ǳ���Ҫ
			
			//�����ط�ʽ
			response.setContentType("application/x-msdownload"); 
			response.setHeader("Content-Disposition", "attachment; filename=" + f.getName()); 
			
			OutputStream out = response.getOutputStream();
			while((len = br.read(buf)) >0)
			out.write(buf,0,len);
			br.close();
			out.close();
		}
		
     }
		
		/**
		* ɾ�������ļ�
		* 
		*/
		public ActionForward toDeleteContractInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			String blockId = request.getParameter("blockId");
		
			try{
			    tdscBlockContractInfoService.deleteContractInfo(blockId);
			}catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("saveMessage", "ɾ��ʧ��");
            }
		
			return queryContractInfoList(mapping,null,request,response);
		}
	
		/**
		* ���س��ú�ͬ�б�without����
		* 
		*/
		public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			return queryContractInfoList(mapping,null,request,response);
		}
	
	
}
