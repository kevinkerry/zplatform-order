/* 
 * TxncodeDefDAO.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.dao;

import com.zlebank.zplatform.order.common.dao.BaseDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 上午11:50:27
 * @since 
 */
public interface TxncodeDefDAO extends BaseDAO<PojoTxncodeDef>{

	/**
	 * 获取内部业务代码实体类
	 * @param txntype
	 * @param txnsubtype
	 * @param biztype
	 * @return
	 */
	public PojoTxncodeDef getBusiCode(String txntype,String txnsubtype,String biztype);
}
