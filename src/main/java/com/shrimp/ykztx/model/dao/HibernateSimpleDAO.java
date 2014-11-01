package com.shrimp.ykztx.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 提供hql分页查询，不带泛型，与具体实体类无关
 * 
 */

public abstract class HibernateSimpleDAO {

	protected Logger log = LoggerFactory.getLogger(getClass());
	protected SessionFactory sessionFactory;

	/***
	 * 查询数据数量
	 */
	protected long findRows(String hql, boolean isCacheable, Object... values) {
		return (Long) createQuery(hql, values).setCacheable(isCacheable)
				.uniqueResult();
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	protected Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}
	
	/**
	 * 创建SQL查询
	 * @param sql sql语句
	 * @param values 参数
	 * @return
	 */
	protected SQLQuery createSQLQuery(String sql, Object... values) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	protected List findBySQL(String sql, boolean isCacheable, Object... values) {
		return createSQLQuery(sql, values).setCacheable(isCacheable).list();
	}

	/**
	 * 通过HQL查询对象列表
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	protected List find(String hql, boolean isCacheable, Object... values) {
		// return createQuery(hql, values).setCacheable(isCacheable).list();
		Query query = createQuery(hql, values);
		query.setCacheable(isCacheable);
		List list = query.list();
		return list;

	}

	/***
	 * hql里用sql语句
	 * 
	 * @author lenovo
	 * @param hql
	 * @param isCacheable
	 * @param values
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	protected List findDistinct(String hql, boolean isCacheable,
			Object... values) {
		Query query = createQuery(hql, values);
		query.setCacheable(isCacheable);
		return query.list();
	}

	/**
	 * 通过HQL查询对象列表 分页功能
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	protected List findPage(String hql, boolean isCacheable, int page,
			int maxSize, Object... values) {
		Query query = createQuery(hql, values);
		query.setCacheable(isCacheable);
		query.setFirstResult((page - 1) * maxSize);
		query.setMaxResults(maxSize);
		return query.list();
	}

	/***
	 * hql里用sql语句
	 * 
	 * @author lenovo
	 * @param hql
	 * @param isCacheable
	 * @param page
	 * @param maxSize
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List findPageDistinct(String hql, boolean isCacheable, int page,
			int maxSize, Object... values) {
		Query query = createQuery(hql, values);
		query.setCacheable(isCacheable);
		query.setFirstResult((page - 1) * maxSize);
		query.setMaxResults(maxSize);
		return query.list();
	}

	/**
	 * 通过HQL查询唯一对象
	 */
	protected Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).setMaxResults(1).uniqueResult();
	}

	/**
	 * 使用DetachedCriteria进行list查询
	 * @param dc
	 * @return
	 */
	public List listByDC(DetachedCriteria dc,boolean isCacheable) {
		return dc.getExecutableCriteria(getSession()).list();
	}

	/**
	 * 使用DetachedCriteria进行list查询(重载)
	 * @param dc 
	 * @param first 记录开始位置(0~n)
	 * @param max 记录最大位置
	 * @param isCacheable 是否启用hibernate缓存
	 * @return
	 */
	public List listByDC(DetachedCriteria dc, int first, int max,
			boolean isCacheable) {
		Criteria c = dc.getExecutableCriteria(getSession());
		c.setFirstResult(first);
		c.setMaxResults(max);
		c.setCacheable(isCacheable);
		return c.list();
	}
	
	/**
	 * 使用DC查询唯一记录
	 * @param dc
	 * @return
	 */
	public Object uniqueByDC(DetachedCriteria dc) {
		return dc.getExecutableCriteria(getSession()).uniqueResult();
	}

	/**
	 * 使用DetachedCriteria查询记录总数Count(*)
	 * @param dc
	 * @return
	 */
	public Long countByDC(DetachedCriteria dc) {
		Criteria c = dc.getExecutableCriteria(getSession());
		c.setProjection(Projections.rowCount());
		return new Long((Integer) c.uniqueResult());
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
