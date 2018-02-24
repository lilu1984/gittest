package com.wonders.wsjy.bank;

public class EngineException extends Exception {

	public EngineException(String s) {
		super(s);
	}

	public EngineException(String s, Exception ex) {
		super(s, ex);
	}
}