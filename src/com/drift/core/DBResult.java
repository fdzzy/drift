package com.drift.core;

public class DBResult {


	private int code;	// status code
	private Object result; 	// result object
	
	public Object getResultObject() {
		return result;
	}

	public void setResultObject(Object result) {
		this.result = result;
	}

	public DBResult() {
		this.code = DAO.DB_STATUS_ERR_GENERIC;
		this.result = null;
	}	
	
	public DBResult(int code, Object result) {
		this.code = code;
		this.result = result;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

}
