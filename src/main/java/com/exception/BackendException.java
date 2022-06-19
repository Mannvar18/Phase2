package com.exception;

import org.springframework.stereotype.Component;

@Component
public class BackendException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String errCode;
	public String errMsg;
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BackendException(String errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
 
	public BackendException() {
		
	}
}