package com.nofaterock.batchwithquartz.pay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author nofaterock
 * @since 2019-04-15
 */
@Configuration
public class PayConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.pay")
	public DataSource payDataSource() {
		return DataSourceBuilder.create().build();
	}

}
