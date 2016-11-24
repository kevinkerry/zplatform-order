/* 
 * BaseHibernateImpl.java  
 * 
 * version TODO
 *
 * 2015-6-25 
 * 
 * Copyright (c) 2015,.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zlebank.zplatform.order.common.dao.BaseDAO;

/**
 * hibernate数据库连接实现类，里面有SessionFactory，可以取到Session
 * 
 * @author guojia
 * @version
 * @date 2015-6-25 下午05:28:34
 * @since
 */
public class HibernateBaseDAOImpl<T> implements BaseDAO<T> {

    @Autowired
    private SessionFactory sessionFactory = null;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    
    @SuppressWarnings("unchecked")
    @Override 
    public T merge(T t) {
        return (T) getSession().merge(t.getClass().getSimpleName(), t);
    }
    @Override 
    public void saveEntity(T t) {
          getSession().save(t);
    }
    @Override 
    public T update(T t) {
        getSession().update(t);
        return t;
    }


}
