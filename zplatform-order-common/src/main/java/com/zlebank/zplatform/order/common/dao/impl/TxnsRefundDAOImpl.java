/* 
 * TxnsRefundDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年11月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.order.common.dao.TxnsRefundDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoTxnsRefund;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月11日 下午3:29:35
 * @since 
 */
@Repository("txnsRefundDAO")
public class TxnsRefundDAOImpl extends HibernateBaseDAOImpl<PojoTxnsRefund> implements TxnsRefundDAO {

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public Long getSumAmtByOldTxnseqno(String txnseqno_old){
    	String sql = "select nvl(sum(t.amount),0) totalamt from t_txns_refund t where t.oldtxnseqno=? and t.status not in(?,?,?,?)";
    	SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	Object[] paramaters = new Object[]{txnseqno_old,"09","19","29","39"};
    	for(int i=0;i<paramaters.length;i++){
    		query.setParameter(i, paramaters[i]);
    	}
    	List<Map<String, Object>> queryBySQL = query.list();
    	return  Long.valueOf(queryBySQL.get(0).get("TOTALAMT")+"");
    }

	/**
	 *
	 * @param txnsRefund
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveRefundOrder(PojoTxnsRefund txnsRefund) {
		// TODO Auto-generated method stub
		saveA(txnsRefund);
	}

}
