/* 
 * CommonException.java  
 * 
 * version TODO
 *
 * 2016年9月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.exception;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月9日 下午2:36:01
 * @since 
 */
public class CommonException extends Exception{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3137676281266807343L;
	private String code;
	
    private String message;
    
    public CommonException(String code,String message){
        super();
        this.code = code;
        this.message = message;
    }
    
    public String getCode(){
        return code;
    }
    @Override
    public String getMessage(){
       return message;
    }
}
