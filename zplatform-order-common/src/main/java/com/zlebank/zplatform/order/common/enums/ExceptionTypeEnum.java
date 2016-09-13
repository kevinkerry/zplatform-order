/* 
 * ExceptionTypeEnum.java  
 * 
 * version TODO
 *
 * 2016年9月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.enums;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月9日 下午2:36:35
 * @since 
 */
public enum ExceptionTypeEnum {

	SECOND_PAY("Order");
	
	
	/**
	 * @param code
	 */
	private ExceptionTypeEnum(String code) {
		this.code = code;
	}

	private String code;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	public static ExceptionTypeEnum fromValue(String code){
		for(ExceptionTypeEnum exceptionTypeEnum : values()){
			if(exceptionTypeEnum.getCode().equals(code)){
				return exceptionTypeEnum;
			}
		}
		return null;
	}
}
