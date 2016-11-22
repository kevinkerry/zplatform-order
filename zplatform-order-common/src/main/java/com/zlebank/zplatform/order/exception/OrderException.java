/* 
 * InsteadPayOrderException.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.exception;

import java.util.ResourceBundle;

import com.zlebank.zplatform.order.common.exception.AbstractConsumeOrderDescException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 上午10:36:29
 * @since
 */
public class OrderException extends AbstractConsumeOrderDescException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5825227604243819693L;
	private String code;
	public OrderException(String code,Object ... para ) {
        this.params = para;
        this.code = code;
    }
	
	public OrderException(String code) {
        this.code = code;
    }
	
    /**
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }
	/**
	 * 
	 */
	public OrderException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
