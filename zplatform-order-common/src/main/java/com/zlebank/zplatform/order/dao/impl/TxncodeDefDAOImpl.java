/* 
 * TxncodeDefDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年9月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.order.common.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 上午11:50:55
 * @since 
 */
@Repository
public class TxncodeDefDAOImpl extends HibernateBaseDAOImpl<PojoTxncodeDef> implements TxncodeDefDAO{

	
	@Transactional(readOnly=true)
	public PojoTxncodeDef getBusiCode(String txntype,String txnsubtype,String biztype){
		Criteria criteria = getSession().createCriteria(PojoTxncodeDef.class);
		//from TxncodeDefModel where txntype=? and txnsubtype=? and biztype=?
		criteria.add(Restrictions.eq("txntype", txntype));
		criteria.add(Restrictions.eq("txnsubtype", txnsubtype));
		criteria.add(Restrictions.eq("biztype", biztype));
		criteria.add(Restrictions.eq("status", "00"));
		return (PojoTxncodeDef) criteria.uniqueResult();
	}
	
}
