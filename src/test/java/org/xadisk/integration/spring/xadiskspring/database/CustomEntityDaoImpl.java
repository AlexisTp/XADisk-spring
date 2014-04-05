package org.xadisk.integration.spring.xadiskspring.database;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class CustomEntityDaoImpl extends HibernateDaoSupport implements
		CustomEntityDao {

	@Autowired
	void initDao(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Transactional
	public void save(CustomEntity ce) {
		getHibernateTemplate().save(ce);
	}

	public CustomEntity get(Long id) {
		return getHibernateTemplate().get(CustomEntity.class, id);
	}

	public void delete(CustomEntity ce) {
		getHibernateTemplate().delete(ce);
		
	}

}
