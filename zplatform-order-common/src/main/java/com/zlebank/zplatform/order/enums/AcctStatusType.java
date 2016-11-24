/* 
 * AcctStatusType.java  
 * 
 * version v1.0
 *
 * 2015年8月24日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.enums;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年8月24日 下午4:29:10
 * @since 
 */
public enum AcctStatusType {
    /**正常**/
    NORMAL("00","正常"),
    /**冻结**/
    FREEZE("11","冻结"),
    /**止入**/
    STOP_IN("10","止入"),
    /**止出**/
    STOP_OUT("01","止出"),
    /**注销**/
    LOGOUT("99","注销"),
    /**未知代码**/
    UNKNOW("88","未知");

    private String code;
    private String desc;
    
    private AcctStatusType(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    
    public static AcctStatusType fromValue(String value) {
        for(AcctStatusType status:values()){
            if(status.code.equals(value)){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public String getCode(){
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    
}
