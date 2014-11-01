package com.shrimp.ykztx.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import com.shrimp.ykztx.web.model.Page;


public interface BaseDAO<T, ID extends Serializable> {

	/**
	 * @see Session.get(Class,Serializable)
	 * @param id
	 * @return 持久化对象
	 */
	T get(ID id);

	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id 对象ID
	 * @param lock 是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	T get(ID id, boolean lock);
	

	/**
	 * 通过ID查找对象,不锁定对象
	 * 
	 * @param id 记录的ID
	 * @return 实体对象
	 */
	T load(ID id);

	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id 对象ID
	 * @param lock 是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	T load(ID id, boolean lock);

	/**
	 * 保存对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	T save(T t);
	
	/**
	 * 保存对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	Serializable saveAny(Object t);

	/**
	 * 更新对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	Object update(Object entity);

	/**
	 * 保存或更新对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	Object saveOrUpdate(Object entity);

	/**
	 * 删除对象
	 * 
	 * @param entity 实体对象
	 */
	void delete(Object entity);

	/**
	 * 根据ID删除记录
	 * 
	 * @param id 记录ID
	 */
	T deleteById(ID id);
	
	/**
	 * 根绝ID使用hql删除记录
	 * @param id 记录ID
	 * @return 
	 */
	boolean deleteByIdUseHql(ID id);

	/**
	 * 刷新对象
	 * 
	 * @param entity
	 */
	void refresh(Object entity);

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	Page<T> findByPage(int pageIndex, int pageSize, Order[] orders);
	
	/**
	 * 分页查询
	 * 
	 * @param pageIndex 当前页(0~n)
	 * @param pageSize 页面大小(每页最大记录数)
	 * @param isCacheable 是否启用缓存
	 * @param eqConditions equals查询条件(key:hibernate entity 属性名,value 属性值)
	 * @param likeConditions like查询条件(key:hibernate entity 属性名,value 属性值)
	 * @param orders  排序字段
	 * @return 页面查询结果封装对象
	 */
	Page<T> findByPage(int pageIndex, int pageSize, boolean isCacheable,
			Map<String, Object> eqConditions,
			Map<String, String> likeConditions, Order[] orders);

	/**
	 * 分页查询（重载）
	 * @param dc 查询条件
	 * @param pageIndex 当前页(0~n)
	 * @param pageSize 页面大小(每页最大记录数)
	 * @param isCacheable 是否启用缓存
	 * @return
	 */
	Page<T> findByPage(DetachedCriteria dc, int pageIndex, int pageSize,
			boolean isCacheable);
	
	/**
	 * 获取当前DAO对应entity的DetachedCriteria
	 * @return
	 */
	DetachedCriteria getDC();

	/**
	 * 查询所有记录
	 * @return
	 */
	List<T> findAll();
	
	/**
	 * 单属性查询列表
	 * @param propName 属性名
	 * @param propVal 属性值
	 * @return
	 */
	List<T> findByProperty(String propName,Object propVal);
	
	/**
	 * 单属性查询唯一结果
	 * @param propName
	 * @param propVal
	 * @return
	 */
	T getByProperty(String propName,Object propVal);
	
	/**
	 * 根据ID更新字段
	 * @param updateSet 需要更新的字段名及值 key-value
	 * @param id
	 * @return
	 */
	int updatePropertiesById(Map<String, Object> updateSet, ID id);
	
	/**
	 * 根据条件更新字段
	 * @param updateSet
	 * @param updateWhere
	 * @return
	 */
	int updateProperties(Map<String, Object> updateSet,Map<String,Object> updateWhere);
	
	/**
	 * 使用DetachedCriteria进行list查询
	 * @param dc
	 * @return
	 */
	List listByDC(DetachedCriteria dc,boolean isCacheable);
	
	List listByDC(DetachedCriteria dc,String[] sels);
	
	List listByDC(DetachedCriteria dc);

	/**
	 * 使用DC查询唯一记录
	 * @param dc
	 * @return
	 */
	Object uniqueByDC(DetachedCriteria dc);
	
	/**
	 * 使用DetachedCriteria查询记录总数Count(*)
	 * @param dc
	 * @return
	 */
	Long countByDC(DetachedCriteria dc);
	
	
}
