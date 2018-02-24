package com.wonders.wsjy.util;

import java.io.UnsupportedEncodingException;

import com.wonders.engine.socket.encode.EncodeUtils;

public class Testcode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		            
		String tt="8e29wOicxOScsbXNnOiflt7Lmt7vliqDliLDlrqLmiLfnq6/msaAnfQ==";
		String tt1="{op:'19',msg:'已添加到客户端池'}";
		String tt2 = "b33bee29wOicxOScsbXNnOiflt7Lmt7vl78003iqDliLDlrqLmiLfnq6/msaAnfQ==244792";
		try {
			System.out.println(EncodeUtils.encodeBase80(tt1));
			System.out.println(EncodeUtils.encode(tt1));
			System.out.println(EncodeUtils.decodeBase80(tt2));
			//System.out.println(EncodeUtils.decode(tt));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
