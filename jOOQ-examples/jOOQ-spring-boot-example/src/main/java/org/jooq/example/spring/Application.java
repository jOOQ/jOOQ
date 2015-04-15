package org.jooq.example.spring;

import javax.sql.DataSource;

import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * @author Thomas Darimont
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public DSLContext dsl(org.jooq.Configuration config) {
		return new DefaultDSLContext(config);
	}

	@Bean
	public ConnectionProvider connectionProvider(DataSource dataSource) {
		return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
	}

	@Bean
	public TransactionProvider transactionProvider() {
		return new SpringTransactionProvider();
	}

	@Bean
	public ExceptionTranslator exceptionTranslator() {
		return new ExceptionTranslator();
	}

	@Bean
	public ExecuteListenerProvider executeListenerProvider(ExceptionTranslator exceptionTranslator) {
		return new DefaultExecuteListenerProvider(exceptionTranslator);
	}

	@Bean
	public org.jooq.Configuration jooqConfig(ConnectionProvider connectionProvider,
			TransactionProvider transactionProvider, ExecuteListenerProvider executeListenerProvider) {

		return new DefaultConfiguration() //
				.derive(connectionProvider) //
				.derive(transactionProvider) //
				.derive(executeListenerProvider) //
				.derive(SQLDialect.H2);
	}
}
