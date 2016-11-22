/* 
 * AbstractException.java  
 * 
 * version 1.0
 *
 * 2015年8月19日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.exception;

/**
 * Represent platform base exception
 *
 * @author yangying
 * @version
 * @date 2015年8月19日 下午3:51:48
 * @since
 */
public class BaseException extends Exception{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1953662807682578990L;
	protected Object[] params; 
	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message, Object... params) {
		super(message);
		this.params = params;
	}
 
	public Object[] getParams() {
		return params;
	}
 
	public void setParams(Object... params) {
		this.params = params;
	}
}
