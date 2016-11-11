/* 
 * TxnsRefundDAO.java  
 * 
 * version TODO
 *
 * 2016年11月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsRefund;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月11日 下午3:28:41
 * @since 
 */
public interface TxnsRefundDAO extends BaseDAO<PojoTxnsRefund>{

	/**
	 * 
	 * @param txnseqno_old
	 * @return
	 */
    public Long getSumAmtByOldTxnseqno(String txnseqno_old);
    
    /**
     * 保存退款交易记录
     * @param txnsRefund 退款交易日志
     */
    public void saveRefundOrder(PojoTxnsRefund txnsRefund);
}
