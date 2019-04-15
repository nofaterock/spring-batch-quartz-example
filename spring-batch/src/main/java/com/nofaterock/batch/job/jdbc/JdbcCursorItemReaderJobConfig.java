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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job jdbcCursorItemReaderJob() {
		return jobBuilderFactory.get("jdbcCursorItemReaderJob")
			.start(jdbcCursorItemReaderStep())
			.build();
	}

	@Bean
	public Step jdbcCursorItemReaderStep() {
		return stepBuilderFactory.get("jdbcCursorItemReaderStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jdbcCursorItemReader(null))
			.writer(jdbcCursorItemWriter())
			.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Pay> jdbcCursorItemReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JdbcCursorItemReaderBuilder<Pay>()
			.fetchSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Pay.class))
			.sql("SELECT id, amount, tx_name, tx_date_time FROM Pay WHERE amount >= ?")
			.queryArguments(new Object[]{amount})
			.name("jdbcCursorItemReader")
			.build();
	}

	@Bean
	public ItemWriter<Pay> jdbcCursorItemWriter() {
		return list -> {
			for (Pay pay : list) {
				log.info("Current Pay={}", pay);
			}
		};
	}

}
