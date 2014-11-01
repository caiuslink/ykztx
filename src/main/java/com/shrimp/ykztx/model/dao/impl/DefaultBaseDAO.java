package com.shrimp.ykztx.model.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;

import com.shrimp.ykztx.model.User;
import com.shrimp.ykztx.model.dao.BaseDAO;
import com.shrimp.ykztx.model.dao.HibernateSimpleDAO;
import com.shrimp.ykztx.web.model.Page;

/**
 * 
 * @author ksy
 *
 * @param <T> entity
 * @param <ID> primary key name
 */
public abstract class DefaultBaseDAO<T, ID extends Serializable> extends
		HibernateSimpleDAO implements BaseDAO<T, ID> {

	

	/**
	 * 获取当前DAO对应的entity的class类
	 * @return
	 */
	abstract protected Class<T> getEntityClass();
	
	/**
	 * 获取当前DAO对应entity的Id字段名
	 * @return
	 */
	protected String getIdName() {
		return sessionFactory.getClassMetadata(getEntityClass())
				.getIdentifierPropertyName();
	}

	/**
	 * 获取当前DAO对应的entity的Id
	 * @param entity
	 * @return
	 */
	protected Serializable getIdFromEntity(T entity) {
		return getSession().getIdentifier(entity);
	}
	
	/**
	 * @see Session.get(Class,Serializable)
	 * @param id
	 * @return 持久化对象
	 */
	@Override
	public T get(ID id) {
		Assert.notNull(id);
		return get(id, false);
	}

	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id 对象ID
	 * @param lock 是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public T get(ID id, boolean lock) {
		Assert.notNull(id);
		T entity;
		if (lock) {
			entity = (T) getSession().get(getEntityClass(), id,
					LockMode.UPGRADE);
		} else {
			entity = (T) getSession().get(getEntityClass(), id);
		}
		return entity;
	}
	
	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id 对象ID
	 * @param lock 是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T load(ID id, boolean lock) {
		Assert.notNull(id);
		T entity = null;
		if (lock) {
			entity = (T) getSession().load(getEntityClass(), id,
					LockMode.UPGRADE);
		} else {
			entity = (T) getSession().load(getEntityClass(), id);
		}
		return entity;
	}

	/**
	 * 通过ID查找对象,不锁定对象
	 * 
	 * @param id 记录的ID
	 * @return 实体对象
	 */
	@Override
	public T load(ID id) {
		Assert.notNull(id);
		return load(id, false);
	}

	@Override
	public T save(T t) {
		Assert.notNull(t);
		getSession().save(t);
		return t;
	}
	
	/**
	 * 保存对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	@Override
	public Serializable saveAny(Object t) {
		Assert.notNull(t);
		return getSession().save(t);
	}

	/**
	 * 更新对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	@Override
	public Object update(Object entity) {
		Assert.notNull(entity);
		getSession().update(entity);
		return entity;
	}

	/**
	 * 保存或更新对象
	 * 
	 * @param entity 实体对象
	 * @return 实体对象
	 */
	@Override
	public Object saveOrUpdate(Object entity) {
		Assert.notNull(entity);
		getSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * 删除对象
	 * 
	 * @param entity 实体对象
	 */
	@Override
	public void delete(Object entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
	}

	/**
	 * 根据ID删除记录
	 * 
	 * @param id 记录ID
	 */
	@Override
	public T deleteById(ID id) {
		Assert.notNull(id);
		T entity = get(id, true);
		getSession().delete(entity);
		return entity;
	}
	
	@Override
	public boolean deleteByIdUseHql(ID id){
		String hql = "delete "+getEntityClass().getSimpleName()+" e where e."+getIdName()+" = ?";
		getSession().createQuery(hql).setParameter(0, id).executeUpdate();
		return true;
	}

	/**
	 * 刷新对象
	 * 
	 * @param entity
	 */
	@Override
	public void refresh(Object entity) {
		getSession().refresh(entity);
	}

	
	@Override
	public Page<T> findByPage(int pageIndex, int pageSize, Order[] orders) {
		return findByPage(pageIndex, pageSize, false, null, null, orders);
	}
	
	/**
	 * 分页查询
	 * @param pageIndex 当前页(0~n)
	 * @param pageSize 页面大小(每页最大记录数)
	 * @param isCacheable 是否启用缓存
	 * @param eqConditions equals查询条件(key:hibernate entity 属性名,value 属性值)
	 * @param likeConditions like查询条件(key:hibernate entity 属性名,value 属性值)
	 * @param orders 排序字段
	 * @return 页面查询结果封装对象
	 */
	@Override
	public Page<T> findByPage(int pageIndex, int pageSize, boolean isCacheable,
			Map<String, Object> eqConditions,
			Map<String, String> likeConditions, Order[] orders) {
		DetachedCriteria dc = getDC();
		if (null != eqConditions) {
			for (String key : eqConditions.keySet()) {
				dc.add(Restrictions.eq(key, eqConditions.get(key)));
			}
		}
		if (null != likeConditions) {
			for (String key : likeConditions.keySet()) {
				dc.add(Restrictions.like(key, likeConditions.get(key),
						MatchMode.ANYWHERE));
			}
		}
		if (null != orders) {
			for (Order order : orders) {
				dc.addOrder(order);
			}
		}
		return findByPage(dc, pageIndex, pageSize, isCacheable);
	}
	
	

	@Override
	public Page<T> findByPage(DetachedCriteria dc, int pageIndex, int pageSize,
			boolean isCacheable) {
		try {
			Long count = super.countByDC(dc);
			dc.setProjection(null);
			dc.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
			List<T> list = listByDC(dc, pageIndex * pageSize, pageSize,
					isCacheable);
			return new Page<T>(pageIndex + 1, count, pageSize, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前DAO对应entity的DetachedCriteria
	 * @return
	 */
	@Override
	public DetachedCriteria getDC() {
		return DetachedCriteria.forClass(getEntityClass());
	}

	@Override
	public List<T> findAll() {
		return super.listByDC(getDC(),false);
	}
	
	@Override
	public List<T> listByDC(DetachedCriteria dc){
		return super.listByDC(dc,false);
	}
	
	@Override
	public List<T> listByDC(DetachedCriteria dc,String[] sels){
		String alias =  getEntityClass().getSimpleName();
		dc.createCriteria(alias);
		ProjectionList pList = Projections.projectionList(); 
		if (null != sels) {
			for (String sel : sels) {
				pList.add(Projections.property(alias + "." + sel).as(sel));
			}
		}
		dc.setProjection(pList);  
		dc.setResultTransformer(Transformers.aliasToBean(getEntityClass()));  
		return listByDC(dc);
	}
	
	@Override
	public Long countByDC(DetachedCriteria dc){
		return super.countByDC(dc);
	}
	
	@Override
	public List<T> findByProperty(String propName, Object propVal) {
		DetachedCriteria dc = getDC();
		if (null != propVal) {
			dc.add(Restrictions.eq(propName, propVal));
		}
		return listByDC(dc);
	}
	
	@Override
	public T getByProperty(String propName, Object propVal) {
		DetachedCriteria dc = getDC();
		if (null != propVal) {
			dc.add(Restrictions.eq(propName, propVal));
		}
		return (T) uniqueByDC(dc);
	}

	@Override
	public int updatePropertiesById(Map<String, Object> updateSet, ID id) {
		Map updateWhere = new HashMap();
		updateWhere.put(getIdName(), id);
		return updateProperties(updateSet, updateWhere);
	}

	@Override
	public int updateProperties(Map<String, Object> updateSet,
			Map<String, Object> updateWhere) {
		try {
			List<Object> list = new ArrayList();
			String entityName = getEntityClass().getName();
			String hql = "update " + entityName + " e ";
			String setHql = "set ";
			for (String setKey : updateSet.keySet()) {
				setHql += "e." + setKey + "=?,";
				list.add(updateSet.get(setKey));
			}
			setHql = setHql.substring(0, setHql.length() - 1);
			String whereHql = "";
			if (null != updateWhere) {
				whereHql = " where ";
				for (String whereKey : updateWhere.keySet()) {
					whereHql += " e." + whereKey + "=? and";
					list.add(updateWhere.get(whereKey));
				}
				whereHql = whereHql.substring(0, whereHql.length() - 3);
			}
			Query query = createQuery(hql + setHql + whereHql, list.toArray());
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	
}
