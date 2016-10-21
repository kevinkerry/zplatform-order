package com.zlebank.zplatform.order.common.enums;

public enum AccountTypeEnum {
	PUBLIC("02"),//对公账户
	PRIVATE("01"),//对私账户
    UNKNOW("");//未知
    private String code;
    
    private AccountTypeEnum(String code){
        this.code = code;
    }
    
    public static AccountTypeEnum fromValue(String value) {
        for(AccountTypeEnum enumCry:values()){
            if(enumCry.getCode().equals(value)){
                return enumCry;
            }
        }
        return UNKNOW;
    }
    
    public String getCode(){
        return code;
    }
}
