/* 
 * TxnsWithdrawDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.order.common.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.order.dao.TxnsWithdrawDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsWithdraw;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午4:11:03
 * @since 
 */
@Repository("txnsWithdrawDAO")
public class TxnsWithdrawDAOImpl extends HibernateBaseDAOImpl<PojoTxnsWithdraw> implements
		TxnsWithdrawDAO {

	/**
	 *
	 * @param txnsWithdraw
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveTxnsWithdraw(PojoTxnsWithdraw txnsWithdraw) {
		// TODO Auto-generated method stub
		saveEntity(txnsWithdraw);
	}

	

}
