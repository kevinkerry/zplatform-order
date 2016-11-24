/* 
 * ProdCaseDAOImpl.java  
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
import com.zlebank.zplatform.order.dao.ProdCaseDAO;
import com.zlebank.zplatform.order.dao.pojo.PojoProdCase;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月12日 下午12:02:45
 * @since 
 */
@Repository
public class ProdCaseDAOImpl extends HibernateBaseDAOImpl<PojoProdCase> implements ProdCaseDAO{

	 
	 @Transactional(readOnly=true)
	 public PojoProdCase getMerchProd(String prdtver,String busicode){
		 //from ProdCaseModel where prdtver=? and busicode=?
		 Criteria criteria = getSession().createCriteria(PojoProdCase.class);
		 criteria.add(Restrictions.eq("prdtver", prdtver));
		 criteria.add(Restrictions.eq("busicode", busicode));
		 return (PojoProdCase) criteria.uniqueResult();
	 }


}
