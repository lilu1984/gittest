package com.wonders.org.liuy.conf;

public class TestConf {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JscaConf jc=new JscaConf();
		jc.setPara("F:\\MyEclipse\\MyEclipse5.5\\tomCatWork\\caDemo\\src\\jsca.properties");
		System.out.println(jc.mp.get("svsIP"));

	}

}
