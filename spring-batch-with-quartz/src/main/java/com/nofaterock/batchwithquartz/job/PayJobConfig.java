package com.nofaterock.batchwithquartz.job;

import com.nofaterock.batchwithquartz.pay.Pay;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author 한승룡
 * @since 2019-04-03
 */
@Slf4j
@Configuration
public class PayJobConfig {

	public static final String JOB_NAME = "PayJob";
	private static final int CHUNK_SIZE = 10;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource payDataSource;

	@Autowired
	public PayJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, @Qualifier("payDataSource") DataSource payDataSource) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.payDataSource = payDataSource;
	}

	@Bean(JOB_NAME)
	public Job job() throws Exception {
		return jobBuilderFactory.get(JOB_NAME)
			.start(step())
			.build();
	}

	@Bean(JOB_NAME + "Step")
	public Step step() throws Exception {
		return stepBuilderFactory.get(JOB_NAME + "Step")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(itemReader(null))
			.writer(itemWriter())
			.build();
	}

	@Bean(JOB_NAME + "ItemReader")
	@StepScope
	public JdbcPagingItemReader<Pay> itemReader(@Value("#{jobParameters[amount]}") Long amount) throws Exception {
		return new JdbcPagingItemReaderBuilder<Pay>()
			.fetchSize(CHUNK_SIZE)
			.dataSource(payDataSource)
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

	@Bean(JOB_NAME + "ItemWriter")
	public ItemWriter<Pay> itemWriter() {
		return list -> {
			for (Pay pay : list) {
				log.info("{} Pay={}", JOB_NAME, pay);
			}
		};
	}

	@Bean(JOB_NAME + "QueryProvider")
	public PagingQueryProvider createQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(payDataSource);
		queryProvider.setSelectClause("id, amount, txName, txDateTime");
		queryProvider.setFromClause("from Pay");
		queryProvider.setWhereClause("where amount >= :amount");
		queryProvider.setSortKeys(new HashMap<String, Order>(1) {
			{
				put("id", Order.ASCENDING);
			}
		});

		return queryProvider.getObject();
	}
}
