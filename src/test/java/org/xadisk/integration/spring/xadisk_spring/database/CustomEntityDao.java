package org.xadisk.integration.spring.xadisk_spring.database;

public interface CustomEntityDao {

	void save(CustomEntity ce);

	CustomEntity get(Long id);

	void delete(CustomEntity ce);
}