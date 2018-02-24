package com.wonders.util;

public class MOD37_2 extends ISO7064{

	protected int getModulus() {
		return 37;
	}

	protected int getRadix() {
		return 2;
	}

	protected String getCharacterSet() {
		return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ*";
	}

	protected boolean isDoubleCheckDigit() {
		return false;
	}
	
}
