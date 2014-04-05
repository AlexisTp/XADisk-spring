package org.xadisk.integration.spring.xadiskspring.database;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class CustomEntity {

	private Long id;
	private String field1;

	/**
	 * Damn you JavaBeans
	 */
	public CustomEntity(){
		super();
	}
	
	public CustomEntity(Long id, String field1){
		super();
		this.id = id;
		this.field1 = field1;
	}
	
	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

}
