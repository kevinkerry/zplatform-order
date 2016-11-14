/* 
 * TxnsLogDAO.java  
 * 
 * version TODO
 *
 * 2016年9月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.dao;

import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月13日 下午5:31:41
 * @since 
 */
public interface TxnsLogDAO extends BaseDAO<PojoTxnsLog>{

	/**
	 * 保存交易流水
	 * @param txnsLog
	 */
	public void saveTxnsLog(PojoTxnsLog txnsLog);
	/**
	 * 查询卡bin信息
	 * @param cardNo
	 * @return
	 */
	public CardBin getCard(String cardNo) ;
	
	/**
	 * 通过交易序列号获取交易流水
	 * @param txnseqno 交易序列号
	 * @return 交易流水pojo
	 */
	public PojoTxnsLog getTxnsLogByTxnseqno(String txnseqno);
}
