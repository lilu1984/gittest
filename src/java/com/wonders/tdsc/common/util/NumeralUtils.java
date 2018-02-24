package com.wonders.tdsc.common.util;


//�÷���ԭ��������������ת���Ľ�������������������ת����
public class NumeralUtils {
	/** �������� */
	private static final String[] NUMBERS = { "��", "һ", "��", "��", "��", "��",
			"��", "��", "��", "��" };

	/** �������ֵĵ�λ */
	private static final String[] IUNIT = { "", "ʮ", "��", "ǧ", "��", "ʮ", "��",
			"ǧ", "��", "ʮ", "��", "ǧ", "��", "ʮ", "��", "ǧ" };

	/**
	 * �õ��������֡�
	 */
	public static String toChinese(String str) {
		str = str.replaceAll(",", "");// ȥ��","
		String integerStr;// ������������
		// // ��ʼ���������������ֺ�С������
		if (str.indexOf(".") > 0) {
			integerStr = str.substring(0, str.indexOf("."));
		} else if (str.indexOf(".") == 0) {
			integerStr = "";
		} else {
			integerStr = str;
		}
		// integerStrȥ����0������ȥ��decimalStr��β0(����������ȥ)
		if (!integerStr.equals("")) {
			integerStr = Long.toString(Long.parseLong(integerStr));
			if (integerStr.equals("0")) {
				integerStr = "";
			}
		}

		int[] integers = toArray(integerStr);// ������������
		boolean isMust5 = isMust5(integerStr);// ������λ;
		return getChineseInteger(integers, isMust5);
	}

	/**
	 * �������ֺ�С������ת��Ϊ���飬�Ӹ�λ����λ
	 */
	private static int[] toArray(String number) {
		int[] array = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			array[i] = Integer.parseInt(number.substring(i, i + 1));
		}
		return array;
	}

	/**
	 * �õ��������ֵ��������֡�
	 */
	private static String getChineseInteger(int[] integers, boolean isMust5) {
		StringBuffer chineseInteger = new StringBuffer("");
		int length = integers.length;
		for (int i = 0; i < length; i++) {
			// 0�����ڹؼ�λ�ã�1234(��)5678(��)9012(��)3456
			// ���������10(ʰ��Ҽʰ��Ҽʰ��ʰ��)
			String key = "";
			if (integers[i] == 0) {
				if ((length - i) == 13)// ��(��)(����)
					key = IUNIT[4];
				else if ((length - i) == 9)// ��(����)
					key = IUNIT[8];
				else if ((length - i) == 5 && isMust5)// ��(������)
					key = IUNIT[4];
				else if ((length - i) == 1)// Ԫ(����)
					key = IUNIT[0];
				// 0����0ʱ���㣬���������һλ
				if ((length - i) > 1 && integers[i + 1] != 0)
					key += NUMBERS[0];
			}
			chineseInteger.append(integers[i] == 0 ? key
					: (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
		}
		return chineseInteger.toString();
	}

	/**
	 * �жϵ�5λ���ֵĵ�λ"��"�Ƿ�Ӧ�ӡ�
	 */
	private static boolean isMust5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";
			if (length > 8) {
				// ȡ�ôӵ�λ������5����8λ���ִ�
				subInteger = integerStr.substring(length - 8, length - 4);
			} else {
				subInteger = integerStr.substring(0, length - 4);
			}
			return Integer.parseInt(subInteger) > 0;
		} else {
			return false;
		}
	}

//	public static void main(String[] args) {
//		String number = "121.23";
//		System.out.println(number + " " + NumeralUtils.toChinese(number));
//		number = "1234567890123456.12";
//		System.out.println(number + " " + NumeralUtils.toChinese(number));
//		number = "0.07";
//		System.out.println(number + " " + NumeralUtils.toChinese(number));
//		number = "10,001,000.09";
//		System.out.println(number + " " + NumeralUtils.toChinese(number));
//		number = "01.10";
//		System.out.println(number + " " + NumeralUtils.toChinese(number));
//	}
}