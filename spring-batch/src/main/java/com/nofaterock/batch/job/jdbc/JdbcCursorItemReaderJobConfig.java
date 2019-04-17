package com.nofaterock.batch.job.jdbc;

import com.nofaterock.batch.pay.Pay;
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
			.start(jdbcCursorItemReaderJobStep())
			.build();
	}

	@Bean
	public Step jdbcCursorItemReaderJobStep() {
		return stepBuilderFactory.get("jdbcCursorItemReaderJobStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jdbcCursorItemReaderJobReader(null))
			.writer(jdbcCursorItemReaderJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Pay> jdbcCursorItemReaderJobReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JdbcCursorItemReaderBuilder<Pay>()
			.fetchSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Pay.class))
			.sql("SELECT id, amount, txName, txDateTime FROM Pay WHERE amount >= ?")
			.queryArguments(new Object[]{amount})
			.name("jdbcCursorItemReaderJobReader")
			.build();
	}

	@Bean
	public ItemWriter<Pay> jdbcCursorItemReaderJobWriter() {
		return pays -> {
			for (Pay pay : pays) {
				log.info(">>>>> Pay = {}", pay);
			}
		};
	}

}
