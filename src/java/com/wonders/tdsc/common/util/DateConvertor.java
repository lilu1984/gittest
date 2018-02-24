package com.wonders.tdsc.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.wonders.esframework.util.DateUtil;

/**
 * Creation date: 2010-5-22����03:41:58 ����: �����������ת��Ϊ�������ڣ�����: 2007-10-05 --> ���������ʮ������) ˵�����˳���ٶ������ʽΪyyyy-mm-dd, �������ղ��ֶ�Ϊ����, û�м��ϷǷ� ��������У�� ���Կ������� 2007-01-05 2007-1-05 2007-10-05
 */
public class DateConvertor {
	/**
	 * �������õ���ǰ���ں�ʱ��ʹ�� TimeZone yyyyMMddHHmmss
	 * 
	 * @param str
	 *            �����ַ���
	 * @return
	 * 			20110525092400
	 */
	public static String getCurrentDateWithTimeZone() {
		DateFormat dateFormatterChina = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.DEFAULT);// ��ʽ�����
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");// ��ȡʱ��
		dateFormatterChina.setTimeZone(timeZoneChina);// ����ϵͳʱ��
		Date curDate = new Date();// ��ȡϵͳʱ��

		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
		String currYmd = bartDateFormat.format(curDate);
		String tmpTime = dateFormatterChina.format(curDate);
		String tt = tmpTime.substring(tmpTime.length() - 8);
		String tHh = tt.substring(0, 2);
		if (tHh.trim().length() != 2)
			tHh = "0" + tHh.trim();
		String tMm = tt.substring(3, 5);
		if (tMm.trim().length() != 2)
			tMm = "0" + tMm.trim();
		String tSs = tt.substring(6, 8);
		if (tSs.trim().length() != 2)
			tSs = "0" + tSs.trim();

		// Long nowTtime = new Long(currYmd+tHh+tMm+tSs);
		return currYmd + tHh + tMm + tSs;
	}

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// String issueDate = "2001-02-01";
	// System.out.println(getYearStr(formatStr(issueDate)));
	// System.out.println(formatStr(issueDate));
	// }

	/**
	 * create date:2010-5-22����04:29:37 ������������ת��Ϊָ����ʽ�ַ���
	 * 
	 * @param date
	 *            ����
	 * @return
	 */
	public static String getDateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = sdf.format(date);
		return datestr;
	}

	/**
	 * create date:2010-5-22����03:40:44 ������ȡ�������ַ����е�����ַ���
	 * 
	 * @param str
	 *            �����ַ���
	 * @return
	 */
	public static String getYearStr(String str) {
		String yearStr = "";
		yearStr = str.substring(0, 4);
		return yearStr;
	}

	/**
	 * create date:2010-5-22����03:40:47 ������ȡ�������ַ����е��·��ַ���
	 * 
	 * @param str�����ַ���
	 * @return
	 */
	public static String getMonthStr(String str) {
		int startIndex = str.indexOf("��");
		int endIndex = str.indexOf("��");
		String monthStr = str.substring(startIndex + 1, endIndex);
		return monthStr;
	}

	/**
	 * create date:2010-5-22����03:32:31 ��������Դ�ַ����еİ��������ָ�ʽ��Ϊ����
	 * 
	 * @param sign
	 *            Դ�ַ����е��ַ�
	 * @return
	 */
	public static char formatDigit(char sign) {
		if (sign == '0')
			sign = '��';
		if (sign == '1')
			sign = 'һ';
		if (sign == '2')
			sign = '��';
		if (sign == '3')
			sign = '��';
		if (sign == '4')
			sign = '��';
		if (sign == '5')
			sign = '��';
		if (sign == '6')
			sign = '��';
		if (sign == '7')
			sign = '��';
		if (sign == '8')
			sign = '��';
		if (sign == '9')
			sign = '��';
		return sign;
	}

	/**
	 * create date:2010-5-22����03:31:51 ������ ����·��ַ����ĳ���
	 * 
	 * @param str
	 *            ��ת����Դ�ַ���
	 * @param pos1
	 *            ��һ��'-'��λ��
	 * @param pos2
	 *            �ڶ���'-'��λ��
	 * @return
	 */
	public static int getMidLen(String str, int pos1, int pos2) {
		return str.substring(pos1 + 1, pos2).length();
	}

	/**
	 * create date:2010-5-22����03:32:17 ��������������ַ����ĳ���
	 * 
	 * @param str
	 *            ��ת����Դ�ַ���
	 * @param pos2
	 *            �ڶ���'-'��λ��
	 * @return
	 */
	public static int getLastLen(String str, int pos2) {
		return str.substring(pos2 + 1).length();
	}

	/**
	 * create date:2010-5-22����03:40:50 ������ȡ�������ַ����е����ַ���
	 * 
	 * @param str
	 *            �����ַ���
	 * @return
	 */
	public static String getDayStr(String str) {
		String dayStr = "";
		int startIndex = str.indexOf("��");
		int endIndex = str.indexOf("��");
		dayStr = str.substring(startIndex + 1, endIndex);
		return dayStr;
	}

	/**
	 * create date:2010-5-22����03:32:46 ��������ʽ������
	 * 
	 * @param str
	 *            Դ�ַ����е��ַ�
	 * @return
	 */
	public static String formatStr(String str) {
		StringBuffer sb = new StringBuffer();
		int pos1 = str.indexOf("-");
		int pos2 = str.lastIndexOf("-");
		for (int i = 0; i < 4; i++) {
			sb.append(formatDigit(str.charAt(i)));
		}
		sb.append('��');
		if (getMidLen(str, pos1, pos2) == 1) {
			sb.append(formatDigit(str.charAt(5)) + "��");
			if (str.charAt(7) != '0') {
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(7)) + "��");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(7) != '1' && str.charAt(8) != '0') {
						sb.append(formatDigit(str.charAt(7)) + "ʮ" + formatDigit(str.charAt(8)) + "��");
					} else if (str.charAt(7) != '1' && str.charAt(8) == '0') {
						sb.append(formatDigit(str.charAt(7)) + "ʮ��");
					} else if (str.charAt(7) == '1' && str.charAt(8) != '0') {
						sb.append("ʮ" + formatDigit(str.charAt(8)) + "��");
					} else {
						sb.append("ʮ��");
					}
				}
			} else {
				sb.append(formatDigit(str.charAt(8)) + "��");
			}
		}
		if (getMidLen(str, pos1, pos2) == 2) {
			if (str.charAt(5) != '0' && str.charAt(6) != '0') {
				sb.append("ʮ" + formatDigit(str.charAt(6)) + "��");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "��");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ" + formatDigit(str.charAt(9)) + "��");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ��");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("ʮ" + formatDigit(str.charAt(9)) + "��");
						} else {
							sb.append("ʮ��");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "��");
					}
				}
			} else if (str.charAt(5) != '0' && str.charAt(6) == '0') {
				sb.append("ʮ��");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "��");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ" + formatDigit(str.charAt(9)) + "��");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ��");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("ʮ" + formatDigit(str.charAt(9)) + "��");
						} else {
							sb.append("ʮ��");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "��");
					}
				}
			} else {
				sb.append(formatDigit(str.charAt(6)) + "��");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "��");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ" + formatDigit(str.charAt(9)) + "��");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "ʮ��");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("ʮ" + formatDigit(str.charAt(9)) + "��");
						} else {
							sb.append("ʮ��");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "��");
					}
				}
			}
		}
		return sb.toString();
	}
}
