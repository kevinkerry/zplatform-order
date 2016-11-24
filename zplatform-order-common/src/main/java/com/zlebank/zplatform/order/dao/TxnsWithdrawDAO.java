/* 
 * TxnsWithdrawDAO.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.dao;

import com.zlebank.zplatform.order.common.dao.BaseDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsWithdraw;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午4:10:25
 * @since 
 */
public interface TxnsWithdrawDAO extends BaseDAO<PojoTxnsWithdraw>{

	/**
	 * 保存提现订单数据
	 * @param txnsWithdraw 提现订单pojo
	 */
	public void saveTxnsWithdraw(PojoTxnsWithdraw txnsWithdraw);
}
