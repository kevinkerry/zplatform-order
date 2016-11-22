package com.zlebank.zplatform.order.common.exception;

import java.util.ResourceBundle;

public abstract class AbstractConsumeOrderDescException extends AbstractDescribeException{
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 8484664176033605192L;
	private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("customer_exception");
	@Override
	public ResourceBundle getResourceBundle() {
		return RESOURCE;
	}
}
