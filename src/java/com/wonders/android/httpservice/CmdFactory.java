package com.wonders.android.httpservice;


import javax.servlet.http.HttpServletRequest;

import com.wonders.android.httpservice.service.AndroidService;
import com.wonders.esframework.common.util.StringUtil;

public class CmdFactory {
	public static String executeMethod(HttpServletRequest request){
		String method = request.getParameter("method");
		AndroidService service = new AndroidService();
		String result = "";
		if(method.equals(AndroidConsts.GET_NOTICE_LIST)){
			String pageNum = request.getParameter("pageNum");
			String pageSize = request.getParameter("pageSize");
			if(StringUtil.isEmpty(pageNum)||StringUtil.isEmpty(pageSize)){
				result = service.getNoticeList(0, 10);
			}else{
				result = service.getNoticeList(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			}
			return result;
		}
		return null;
	}
	
}
