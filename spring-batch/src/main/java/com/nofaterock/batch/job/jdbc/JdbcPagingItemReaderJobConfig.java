package com.nofaterock.batch.job.jdbc;

import com.nofaterock.batch.pay.domain.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job jdbcPagingItemReaderJob() throws Exception {
		return jobBuilderFactory.get("jdbcPagingItemReaderJob")
			.start(jdbcPagingItemReaderStep())
			.build();
	}

	@Bean
	public Step jdbcPagingItemReaderStep() throws Exception {
		return stepBuilderFactory.get("jdbcPagingItemReaderStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jdbcPagingItemReader(null))
			.writer(jdbcPagingItemWriter())
			.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<Pay> jdbcPagingItemReader(@Value("#{jobParameters[amount]}") Long amount) throws Exception {
		return new JdbcPagingItemReaderBuilder<Pay>()
			.fetchSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Pay.class))
			.queryProvider(createQueryProvider())
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.name("jdbcPagingItemReader")
			.build();
	}

	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
		queryProvider.setFromClause("from Pay");
		queryProvider.setWhereClause("where amount >= :amount");
		queryProvider.setSortKeys(new HashMap<String, Order>(1) {
			{
				put("id", Order.ASCENDING);
			}
		});

		return queryProvider.getObject();
	}

	@Bean
	public ItemWriter<Pay> jdbcPagingItemWriter() {
		return list -> {
			for (Pay pay : list) {
				log.info("Current Pay={}", pay);
			}
		};
	}

}