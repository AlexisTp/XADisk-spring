package org.xadisk.integration.spring.xadiskspring.database;

interface CustomEntityDao {

	void save(CustomEntity ce);

	CustomEntity get(Long id);

	void delete(CustomEntity ce);
}