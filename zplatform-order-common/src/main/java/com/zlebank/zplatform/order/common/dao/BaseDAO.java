package com.zlebank.zplatform.order.common.dao;


/**
 * 
 * 基础数据库连接层DAO
 *
 * @author guojia
 * @version
 * @date 2015-6-26 上午10:09:10
 * @param T
 * @since
 */
public interface BaseDAO<T> {
    /**
     * 更新实体类T后返回持久化的实体类T
     * @param t
     * @return
     */
	public T merge(T t);
	/**
     * 更新实体类T
     * @param t
     * @return
     */
    public T update(T t);
    /**
     * 保存实体类T
     * @param t
     * @return
     */
    public void saveEntity(T t);
}
