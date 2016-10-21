package com.zlebank.zplatform.order.common.utils;

import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zlebank.zplatform.order.common.bean.ResultBean;







public class ValidateLocator{
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("order_exception");
	private static Validator validator;
	public static final String SYSTEM_ERROR="订单信息错误,请重新提交订单";
    private static final Log logger = LogFactory.getLog(ValidateLocator.class);
	public static<T> ResultBean validateBeans(T obj){
	    ResultBean resultBean=null;
		try {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			 validator = factory.getValidator();
			 Set<ConstraintViolation<T>> constraintViolations =
			         validator.validate(obj);
			 String message=SYSTEM_ERROR;
			 for (ConstraintViolation<T> constraintViolation : constraintViolations) { 
				 message=RESOURCE.getString(constraintViolation.getMessage())==null?SYSTEM_ERROR
						 	:RESOURCE.getString(constraintViolation.getMessage());
				 resultBean=new ResultBean(constraintViolation.getPropertyPath().toString(),message);
			     return resultBean;
			 }
			 resultBean = new ResultBean(obj);
		} catch (Exception e) {
		    logger.error("ValidateLocator.validateBeans throws error", e);
		    //rDto.setErrorResultDto(SYSTEM_ERROR);
		} 
	     return resultBean;
	}
	
	
}
