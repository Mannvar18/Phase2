package com.exception;

public class AutoworkExceptions extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String errorCode;
	public String errorMsg;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public  AutoworkExceptions(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
//	public AutoworkExceptions(String errorMsg) {
//		super(errorMsg);
//		this.errorMsg=errorMsg;
//	}
	public AutoworkExceptions() {
		
	}
}