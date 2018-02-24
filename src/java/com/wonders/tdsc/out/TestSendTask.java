package com.wonders.tdsc.out;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.util.DateUtil;

public class TestSendTask {

	public static String[] trimNullValue(String[] ttt) {
		for (int i = 0; i < ttt.length; i++) {
			if (StringUtils.isEmpty(ttt[i])) {
				ttt[i] = ttt[i + 1];
				ttt[i + 1] = null;
				trimNullValue(ttt);
			}
		}
		return ttt;
	}
	
	
	private static Set sortConNum(Set sst) {
		Set retSet = new HashSet();
		
		int iNum = 0;
		if(sst!=null && sst.size()>0){
			Iterator it = sst.iterator();
			while(it.hasNext()){
				int iNext = Integer.parseInt(it.next()+"");
				if(iNum<iNext)
				
				System.out.println(it.next());
			}
		}
		
		
		return retSet;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Set set = new HashSet();
		set.add("10");
		set.add("8");
		set.add("12");
		set.add("1");
		set.add("3");
		set.add("2");
		
		Set ss = new TreeSet(set);
		System.out.println(ss);
		
		sortConNum(set);

//		String nowMonth = DateUtil.date2String(new java.util.Date(), "yyyyMM");
//		System.out.println(nowMonth + StringUtils.leftPad(3 + "", 4, '0'));
//
//		String[] tmpAppIds = new String[] { "1", null, "2", "3", null, null };
//
//		System.out.println(trimNullValue(tmpAppIds));
//
//		List arrlist = new ArrayList();
//		for (int i = 0; i < 12; i++)
//			arrlist.add(i + "");
//
//		arrlist.remove(2);
//
//		for (int i = 0; i < arrlist.size(); i++)
//			System.out.println(arrlist.get(i));
//
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = df.format(new java.util.Date());
//		Timestamp ts = Timestamp.valueOf(time);
//		System.out.println(ts);

		// try {
		// ISendTaskList task = new SendTaskListImpl();
		// TaskListBean bean = new TaskListBean();
		// // @param 1. id 2. taskTitle 3. senderName 4. personId 接收者ID, 在CA中存在的user 5. taskUrl 链接到万达系统的 URL
		// //bean.setId(111);
		// bean.setActId(111);
		// bean.setTaskTitle("任务标题");
		// bean.setSenderName("发送者姓名");
		// bean.setPersonId(999);
		// bean.setTaskUrl("http://192.168.10.19:7001/tdsc/xxxx.do");
		//
		// //发送task
		// //task.send(bean);
		// //System.out.println("发送完毕");
		//
		// //完成 task
		// task.finish(111);
		// System.out.println("更新完毕");
		//
		// } catch (Exception e) {
		// System.out.println(e);
		// }

	}

}
