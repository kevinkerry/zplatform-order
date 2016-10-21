package com.zlebank.zplatform.order.common.dao;

import java.util.List;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.order.common.dao.pojo.PojoInsteadPayRealtime;

/**
 * 
 * 实时代付订单DAO接口
 *
 * @author guojia
 * @version
 * @date 2016年10月17日 下午2:37:10
 * @since
 */
public interface InsteadPayRealtimeDAO extends BaseDAO<PojoInsteadPayRealtime>  {

	
	/****
	 * 保存实时代付信息
	 * @param bean
	 */
	public void saveInsteadTrade(PojoInsteadPayRealtime bean);
	/***
	 * 获取代付交易流水
	 * @param txnseqno
	 * @return
	 */
	public PojoInsteadPayRealtime getInsteadByTxnseqno(String txnseqno);
	
	/***
	 * 代付成功
	 * @param txnseqno
	 */
	public void updateInsteadSuccess(String txnseqno);
	/****
	 * 代付失败
	 * @param txnseqno
	 * @param retCode
	 * @param retMsg
	 */
	public void updateInsteadFail(String txnseqno, String retCode, String retMsg);
	
	/**
	 * 查询代付订单
	 * @param orderNo 商户订单号
	 * @param merchNo 商户号
	 * @return 代付订单pojo
	 */
	public PojoInsteadPayRealtime queryInsteadPayOrder(String orderNo,String merchNo);
}
