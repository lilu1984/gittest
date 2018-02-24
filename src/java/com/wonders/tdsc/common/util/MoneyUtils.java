package com.wonders.tdsc.common.util;

public class MoneyUtils {

	public static void main(String[] args) {
		double dd = 325000000;
		String ss = MoneyUtils.NumToRMBStr(dd);
		System.out.println(ss);

	}

	/*
	 * ���� �����ַ���
	 */
	private static String	HanDigiStr[]	= new String[] { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };

	/*
	 * ���� ���ֵ�λ�ַ���
	 */
	private static String	HanDiviStr[]	= new String[] { "", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ" };

	/*
	 * ���� ���ֵ�λ�ַ���
	 */
	private static String	HanStr[]		= new String[] { "��", "һ", "��", "��", "��", "��", "��", "��", "��", "��" };

	/*
	 * ������תΪ�����ַ���
	 */
	public static String PositiveIntegerToHanStr(String NumStr) { // �����ַ���������������ֻ����ǰ���ո�(�����Ҷ���)��������ǰ����
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // �ڡ����λǰ����ֵ���
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "��ֵ����!";
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "���뺬�������ַ�!";

			if (n != 0) {
				if (lastzero)
					RMBStr += HanDigiStr[0]; // ���������������ֵ��ֻ��ʾһ����
				// ��������ǰ���㲻��������
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
				// ��ʮ��λǰ����Ҳ����Ҽ���ô���
				if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // ʮ��λ���ڵ�һλ����Ҽ��
					RMBStr += HanDigiStr[n];
				RMBStr += HanDiviStr[i]; // ����ֵ��ӽ�λ����λΪ��
				hasvalue = true; // �����λǰ��ֵ���
			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // ����֮������з���ֵ����ʾ��
					RMBStr += HanDiviStr[i]; // ���ڡ�����
			}
			if (i % 8 == 0)
				hasvalue = false; // ���λǰ��ֵ��Ƿ��ڸ�λ
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0)
			return HanDigiStr[0]; // ������ַ���"0"������"��"
		return RMBStr;
	}

	/*
	 * ����ת��Ϊ����Һ����ַ���
	 */
	public static String NumToRMBStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "��";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "��ֵλ������!";
		// �������뵽��
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "��";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "��";
			if (integer == 0 && jiao == 0) // ��Ԫ��д�㼸��
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "��";
		}

		// ��һ�п����ڷ�������ڳ��ϣ�0.03ֻ��ʾ"����"������"��Ԫ����"
		// if( !integer ) {
		// return SignStr+TailStr;
		// }

		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer));
		// + "Ԫ" + TailStr;
	}

	public static String NumToRMBStrAll(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "��";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "��ֵλ������!";
		// �������뵽��
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "��";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "��";
			if (integer == 0 && jiao == 0) // ��Ԫ��д�㼸��
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "��";
		}

		// ��һ�п����ڷ�������ڳ��ϣ�0.03ֻ��ʾ"����"������"��Ԫ����"
		// if( !integer ) {
		// return SignStr+TailStr;
		// }

		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "Ԫ" + TailStr;
	}

	public static String NumToRMBStrWithJiao(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "��";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "��ֵλ������!";
		// �������뵽��
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "��";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "��";
			if (integer == 0 && jiao == 0) // ��Ԫ��д�㼸��
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "��";
		}

		// ��һ�п����ڷ�������ڳ��ϣ�0.03ֻ��ʾ"����"������"��Ԫ����"
		// if( !integer ) {
		// return SignStr+TailStr;
		// }

		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "Ԫ" + TailStr;
	}

	/*
	 * ���ڵ�������תΪ�����ַ���
	 */
	public static String DateIntegerToHanStr(String NumStr) { // �����ַ�������������
		String RiQiStr = "";
		int len, n, n1, n2;
		if (NumStr == null || "".equals(NumStr))
			return "";
		len = NumStr.length();
		if (len > 15)
			return "��ֵ����!";
		// �·ݣ��� �� 0 ��ͷ��ȥ
		if (NumStr.startsWith("0")) {
			NumStr = NumStr.substring(1, len);
			len = len - 1;
		}
		// �����£���
		if (len == 2) {
			n1 = NumStr.charAt(0) - '0';
			n2 = NumStr.charAt(1) - '0';
			if (n1 == 1) {
				if (n2 == 0)
					return "ʮ";
				else
					RiQiStr = "ʮ" + HanStr[n2];
			} else {
				RiQiStr = HanStr[n1] + "ʮ" + HanStr[n2];
			}

		} else {
			for (int i = 0; i <= len - 1; i++) {
				n = NumStr.charAt(i) - '0';

				if (n < 0 || n > 9)
					return "���뺬�������ַ�!";
				RiQiStr += HanStr[n];
			}
		}

		if (RiQiStr.length() == 0)
			return ""; // ������ַ�
		return RiQiStr;
	}

}// END public class Money
