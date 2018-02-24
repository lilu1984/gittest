package com.wonders.engine.exception;

public class EngineException extends Exception {

	private static final long serialVersionUID = 1L;

	public EngineException(String s) {
		super(s);
	}

	public EngineException(String s, Exception ex) {
		super(s, ex);
	}
}