package com.nofaterock.batch.job.jdbc;

import com.nofaterock.batch.pay.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
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
public class JdbcBatchItemWriterJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job jdbcBatchItemWriterJob() {
		return jobBuilderFactory.get("jdbcBatchItemWriterJob")
			.start(jdbcBatchItemWriterJobStep())
			.build();
	}

	@Bean
	public Step jdbcBatchItemWriterJobStep() {
		return stepBuilderFactory.get("jdbcBatchItemWriterJobStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jdbcBatchItemWriterJobReader(null))
			.writer(jdbcBatchItemWriterJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Pay> jdbcBatchItemWriterJobReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JdbcCursorItemReaderBuilder<Pay>()
			.fetchSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Pay.class))
			.sql("SELECT id, amount, txName, txDateTime FROM Pay WHERE amount >= ?")
			.queryArguments(new Object[]{amount})
			.name("jdbcBatchItemWriterJobReader")
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<Pay> jdbcBatchItemWriterJobWriter() {
		JdbcBatchItemWriter<Pay> itemWriter = new JdbcBatchItemWriterBuilder<Pay>()
			.dataSource(dataSource)
			.sql("INSERT INTO Pay2(amount, txName, txDateTime) VALUES (:amount, :txName, :txDateTime)")
			.beanMapped()
			.build();

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

}
