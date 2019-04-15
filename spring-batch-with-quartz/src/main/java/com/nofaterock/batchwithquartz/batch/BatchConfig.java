package com.nofaterock.batchwithquartz.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author nofaterock
 * @since 2019-04-15
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.batch")
	public DataSource batchDataSource() {
		return DataSourceBuilder.create().build();
	}

}
