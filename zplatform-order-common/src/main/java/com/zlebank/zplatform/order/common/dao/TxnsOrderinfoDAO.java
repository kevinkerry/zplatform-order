/* 
 * ITxnsOrderinfoDAO.java  
 * 
 * version TODO
 *
 * 2015年8月29日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsOrderinfo;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月29日 下午3:39:25
 * @since 
 */
public interface TxnsOrderinfoDAO extends BaseDAO<PojoTxnsOrderinfo>{

    /**
     * 
     * @param orderNo
     * @param merchId
     */
    public void updateOrderToFail(String orderNo,String merchId);
    
    /**
     * 
     * @param orderNo
     * @param merchId
     * @return
     */
    public PojoTxnsOrderinfo getOrderinfoByOrderNo(String orderNo,String merchId);
    /**
     * 
     * @param orderinfo
     */
    public void updateOrderinfo(PojoTxnsOrderinfo orderinfo);
    
    /**
     * 
     * @param orderNo
     * @param merchId
     * @return
     */
    public PojoTxnsOrderinfo getOrderinfoByOrderNoAndMemberId(String orderNo,String merchId);
    
    /**
     * 
     * @param tn
     * @return
     */
    public PojoTxnsOrderinfo getOrderByTN(String tn);
    
    /**
     * 
     * @param txnseqno
     * @return
     */
    public PojoTxnsOrderinfo getOrderByTxnseqno(String txnseqno);
    
    /**
     * 
     * @param txnseqno
     */
    public void updateOrderToFail(String txnseqno);
    
    /**
     * 
     * @param txnseqno
     */
    public void updateOrderToSuccess(String txnseqno) ;
    
    /**
     * 
     * @param tn
     */
    public void updateOrderToSuccessByTN(String tn) ;
}
