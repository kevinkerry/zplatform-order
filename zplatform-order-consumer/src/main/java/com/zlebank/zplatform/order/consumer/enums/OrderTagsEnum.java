/* 
 * OrderTagsEnum.java  
 * 
 * version TODO
 *
 * 2016年9月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.consumer.enums;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月7日 下午3:31:34
 * @since 
 */
public enum OrderTagsEnum {

	/**消费订单-传统*/
	COMMONCONSUME_TRADITION("TAG_001"),
	/**消费订单-精简*/
	COMMONCONSUME_SIMPLIFIED("TAG_002"),
	/**产品订单-传统*/
	PRODUCTCONSUME_SIMPLIFIED("TAG_003"),
	/**产品订单-精简*/
	PRODUCTCONSUME_TRADITION("TAG_004"),
	/**实时代付订单*/
	INSTEADPAY_REALTIME("TAG_005");
	private String code;

	/**
	 * @param code
	 */
	private OrderTagsEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	
	public static OrderTagsEnum fromValue(String code){
		for(OrderTagsEnum tagsEnum : values()){
			if(tagsEnum.getCode().equals(code)){
				return tagsEnum;
			}
		}
		return null;
	}
	
}
