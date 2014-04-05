package org.xadisk.integration.spring.xadiskspring.database;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.xadisk.integration.spring.xadiskspring.BaseContext;

@Configuration
@Import(value=BaseContext.class)
@ComponentScan(basePackages = "org.xadisk.integration.spring.xadiskspring.database")
class Context {

	@Bean
	DataSource datasource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.build();
	}

	@Bean
	AnnotationSessionFactoryBean annotationSessionFactoryBean() {
		AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();

		sessionFactory.setDataSource(datasource());
		sessionFactory
				.setPackagesToScan(new String[] { "org.xadisk.integration.spring.xadiskspring.database" });
		sessionFactory
				.setAnnotatedClasses(new Class[] { AnnotationConfiguration.class });

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect",
				"org.hibernate.dialect.H2Dialect");
		hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		hibernateProperties.put("hibernate.show_sql", "true");
		hibernateProperties.put("hibernate.format_sql", "true");		
		
		sessionFactory.setHibernateProperties(hibernateProperties);

		return sessionFactory;
	}

}