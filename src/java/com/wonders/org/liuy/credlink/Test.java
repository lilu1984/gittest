package com.wonders.org.liuy.credlink;

import java.security.cert.X509Certificate;
import java.util.LinkedList;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
			// TODO Auto-generated method stub
			int total;
			int result=0;
			int now;
			for(total=6;total>1;total++){
				now=total;
				for(int j=0;j<7;j++){
					now=now-1;
					if(now%5!=0) 
						break;
					else if(now%5==0&&j!=6) 
						now=now/5;
					else if(now%5==0&&j==6) 
						result=total;
				}
				if(result!=0) 
					break;
			}
			System.out.print("½á¹ûÊÇ£º"+result);
	}
}
