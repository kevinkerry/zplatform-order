package com.zlebank.zplatform.order.enums;

/**
 * 业务类型枚举
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年12月9日 下午4:16:03
 * @since
 */
public enum BusiTypeEnum {
    /**
     *  (消费类：1000；充值类：2000；提现类：3000；退款类：4000；转账类：5000；保障金：6000;代付类:7000)
     */
    consumption("1000"),
    charge("2000"),
    withdrawal("3000"),
    refund("4000"),
    transfer ("5000"),
    safeGuard("6000"),
    insteadPay("7000"),
    UNKNOW("99");
    private String code;
    
    private BusiTypeEnum(String code){
        this.code = code;
    }
    
    public static BusiTypeEnum fromValue(String value) {
        for(BusiTypeEnum status:values()){
            if(status.code.equals(value)){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public String getCode(){
        return code;
    }
}
