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
package com.zlebank.zplatform.order.common.dao;

import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsLog;

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
}
