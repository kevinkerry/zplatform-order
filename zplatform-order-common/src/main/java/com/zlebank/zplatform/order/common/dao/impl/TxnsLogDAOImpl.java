/* 
 * TxnsLogDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年9月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.order.common.dao.TxnsLogDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月13日 下午5:33:02
 * @since 
 */
@Repository
public class TxnsLogDAOImpl extends HibernateBaseDAOImpl<PojoTxnsLog> implements TxnsLogDAO {

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveTxnsLog(PojoTxnsLog txnsLog){
		super.saveA(txnsLog);
	}
	
}
