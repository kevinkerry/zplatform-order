package com.zlebank.zplatform.order.dao.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.order.common.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.order.dao.InsteadPayRealtimeDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoInsteadPayRealtime;


@Repository("insteadPayRealtimeDAO")
public class InsteadPayRealtimeDAOImpl extends
		HibernateBaseDAOImpl<PojoInsteadPayRealtime> implements
		InsteadPayRealtimeDAO {
	private static final Logger log = LoggerFactory.getLogger(InsteadPayRealtimeDAOImpl.class);

	
	/**
	 *
	 * @param bean
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void saveInsteadTrade(PojoInsteadPayRealtime bean) {
		// TODO Auto-generated method stub
		saveEntity(bean);
	}

	/**
	 *
	 * @param txnseqno
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public PojoInsteadPayRealtime getInsteadByTxnseqno(String txnseqno) {
		Criteria criteria = getSession().createCriteria(
				PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("txnseqno", txnseqno));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}

	

	/**
	 *
	 * @param txnseqno
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateInsteadSuccess(String txnseqno) {
		// TODO Auto-generated method stub
		String hql = "update PojoInsteadPayRealtime set status = ?,retCode = ?,retInfo = ?,updateTime = ? where txnseqno = ?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, txnseqno);
		query.setParameter(1, "0000");
		query.setParameter(2, "交易成功");
		query.setParameter(3, new Date());
		query.setParameter(4, txnseqno);
		int rows = query.executeUpdate();
		log.info("updateInsteadSuccess() effect rows:"+rows);
	}

	/**
	 *
	 * @param txnseqno
	 * @param retCode
	 * @param retMsg
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateInsteadFail(String txnseqno, String retCode, String retMsg) {
		// TODO Auto-generated method stub
		String hql = "update PojoInsteadPayRealtime set status = ?,retCode = ?,retInfo = ?,updateTime = ? where txnseqno = ?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, txnseqno);
		query.setParameter(1, retCode);
		query.setParameter(2, retMsg);
		query.setParameter(3, new Date());
		query.setParameter(4, txnseqno);
		int rows = query.executeUpdate();
		log.info("updateInsteadSuccess() effect rows:"+rows);
	}

	/**
	 *
	 * @param orderNo
	 * @param merchNo
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public PojoInsteadPayRealtime queryInsteadPayOrder(String orderNo,
			String merchNo) {
		Criteria criteria = getSession().createCriteria(PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("merId", merchNo));
		criteria.add(Restrictions.eq("orderno", orderNo));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}

}
