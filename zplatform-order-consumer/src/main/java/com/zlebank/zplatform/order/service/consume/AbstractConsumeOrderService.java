/* 
 * AbstractConsumeOrderService.java  
 * 
 * version TODO
 *
 * 2016年11月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.service.consume;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zlebank.zplatform.acc.bean.enums.AcctStatusType;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.member.coopinsti.bean.CoopInsti;
import com.zlebank.zplatform.member.coopinsti.service.CoopInstiService;
import com.zlebank.zplatform.member.individual.bean.MemberAccountBean;
import com.zlebank.zplatform.member.individual.bean.MemberBean;
import com.zlebank.zplatform.member.individual.bean.PoMemberBean;
import com.zlebank.zplatform.member.individual.bean.enums.MemberType;
import com.zlebank.zplatform.member.individual.service.MemberAccountService;
import com.zlebank.zplatform.member.individual.service.MemberService;
import com.zlebank.zplatform.member.merchant.bean.MerchantBean;
import com.zlebank.zplatform.member.merchant.service.MerchService;
import com.zlebank.zplatform.order.bean.BaseOrderBean;
import com.zlebank.zplatform.order.bean.ResultBean;
import com.zlebank.zplatform.order.consume.bean.ConsumeOrderBean;
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.TxncodeDefDAO;
import com.zlebank.zplatform.order.dao.TxnsOrderinfoDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;
import com.zlebank.zplatform.order.dao.pojo.PojoTxncodeDef;
import com.zlebank.zplatform.order.dao.pojo.PojoTxnsOrderinfo;
import com.zlebank.zplatform.order.enums.BusiTypeEnum;
import com.zlebank.zplatform.order.exception.OrderException;
import com.zlebank.zplatform.order.service.CheckOfServcie;
import com.zlebank.zplatform.order.service.OrderService;
import com.zlebank.zplatform.order.utils.ValidateLocator;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月22日 上午10:30:15
 * @since 
 */
@Component
public abstract class AbstractConsumeOrderService implements CheckOfServcie<ConsumeOrderBean>,OrderService{

	private static final Logger logger = LoggerFactory.getLogger(AbstractConsumeOrderService.class);
	private static final String ANONMEMBERID="999999999999999";
	@Autowired
	private TxnsOrderinfoDAO txnsOrderinfoDAO;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private MerchService merchService;
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MemberAccountService memberAccountService;
	/**
	 * 订单非空有效性检查
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException{
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans(baseOrderBean);
		if(!resultBean.isResultBool()){
			throw new OrderException("OD049", resultBean.getErrMsg());
		}
	}
	/**
	 * 检查订单二次支付
	 * @param baseOrderBean
	 * @return 受理订单号 tn
	 * @throws OrderException
	 */
	@Override
	public String checkOfSecondPay(ConsumeOrderBean orderBean) throws OrderException{
		PojoTxnsOrderinfo orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if(orderinfo==null){
			return null;
		}
		if(orderinfo.getOrderamt().longValue()!=Long.valueOf(orderBean.getTxnAmt()).longValue()){
			logger.info("订单金额:{};数据库订单金额:{}", orderBean.getTxnAmt(),orderinfo.getOrderamt());
			throw new OrderException("OD015");
		}
		
		if(!orderinfo.getOrdercommitime().equals(orderBean.getTxnTime())){
			logger.info("订单时间:{};数据库订单时间:{}", orderBean.getTxnTime(),orderinfo.getOrdercommitime());
			throw new OrderException("OD016");
		}
		return orderinfo.getTn();
	}
	
	/**
	 * 检查订单是否为二次提交
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfRepeatSubmit(ConsumeOrderBean orderBean) throws OrderException{
		PojoTxnsOrderinfo orderInfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerId());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new OrderException("OD001","订单交易成功，请不要重复支付");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new OrderException("OD002","订单正在支付中，请不要重复支付");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new OrderException("OD003","订单失效");
			}
			
		}
	}
	
	/**
	 * 检查订单业务有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiness(ConsumeOrderBean orderBean) throws OrderException {
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.consumption){//消费
        	if(StringUtils.isEmpty(orderBean.getMerId())){
        		 throw new OrderException("OD004");
        	}
        	MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerId());//memberService.getMemberByMemberId(order.getMerId());.java
        	if(member==null){
        		throw new OrderException("OD009");
        	}
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new OrderException("OD005");
            }
        }else{
            throw new OrderException("OD045");
        }
	}
	
	/**
	 * 检查商户和合作机构有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfMerchantAndCoopInsti(ConsumeOrderBean orderBean) throws OrderException{
        MerchantBean subMember = merchService.getMerchBymemberId(orderBean.getMerId());
        if (subMember == null) {
        	throw new OrderException("OD009");
        }
        PoMemberBean pojoMember = memberService.getMbmberByMemberId(orderBean.getMerId(), null);
        //校验商户会员信息 
        if (pojoMember.getMemberType()==MemberType.ENTERPRISE) {// 对于企业会员需要进行检查
        	CoopInsti pojoCoopInsti = coopInstiService.getInstiByInstiID(pojoMember.getInstiId());
            if (!orderBean.getCoopInstiId().equals(pojoCoopInsti.getInstiCode())) {
            	throw new OrderException("OD010");
            }
        }
	}
	
	/**
	 * 检查商户和会员的账户状态
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiAcct(ConsumeOrderBean orderBean) throws OrderException{
		if (!ANONMEMBERID.equals(orderBean.getMemberId())&&StringUtils.isNotEmpty(orderBean.getMemberId())) {
			MemberBean member = new MemberBean();
			member.setMemberId(orderBean.getMemberId());
			MemberAccountBean memberAccountBean = null;
			
				try {
					memberAccountBean = memberAccountService.queryBalance(MemberType.INDIVIDUAL, member, Usage.BASICPAY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage());
					throw new OrderException("OD049", "账户查询异常:"+e.getMessage());
				}
			
			if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus())== AcctStatusType.STOP_OUT) {
				//throw new TradeException("GW19");
				throw new OrderException("OD049", "会员账户状态异常");
			}
		}
		if(StringUtils.isEmpty(orderBean.getMerId())){
			return ;
		}
		MemberBean member = new MemberBean();
		member.setMemberId(orderBean.getMerId());
		MemberAccountBean memberAccountBean = null;
		try {
			memberAccountBean = memberAccountService.queryBalance(MemberType.ENTERPRISE, member, Usage.BASICPAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new OrderException("OD049", "账户查询异常:"+e.getMessage());
		}
		if (AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.FREEZE||AcctStatusType.fromValue(memberAccountBean.getStatus()) == AcctStatusType.STOP_IN) {
			//throw new TradeException("GW05");
			throw new OrderException("OD049", "商户账户状态异常");
		}
	}
	
	/**
	 * 检查消费订单特殊性要求检查，如果没有可以为空
	 * @param orderBean
	 * @throws OrderException 
	 */
	@Override
	public void checkOfSpecialBusiness(ConsumeOrderBean orderBean) throws OrderException{
		
	}
	
	/**
	 * 检查所有订单有效性检查项
	 * @param baseOrderBean
	 * @throws OrderException 
	 */
	public abstract void checkOfAll(BaseOrderBean baseOrderBean) throws OrderException;
	
	/**
	 * 保存订单信息
	 * @param orderBean
	 * @throws OrderException 
	 */
	public abstract String saveConsumeOrder(BaseOrderBean baseOrderBean) throws OrderException;
}
