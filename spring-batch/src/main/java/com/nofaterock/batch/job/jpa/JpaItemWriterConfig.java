package com.nofaterock.batch.job.jpa;

import com.nofaterock.batch.pay.Pay;
import com.nofaterock.batch.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

/**
 * @author 한승룡
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaItemWriterJob() throws Exception {
		return jobBuilderFactory.get("jpaItemWriterJob")
			.start(jpaItemWriterJobStep())
			.build();
	}

	@Bean
	public Step jpaItemWriterJobStep() throws Exception {
		return stepBuilderFactory.get("jpaItemWriterJobStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(jpaItemWriterJobReader(null))
			.processor(jpaItemWriterJobProcessor())
			.writer(jpaItemWriterJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Pay> jpaItemWriterJobReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaItemWriterJobReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p WHERE p.amount >= :amount")
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.build();
	}

	@Bean
	public ItemProcessor<Pay, Pay2> jpaItemWriterJobProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public JpaItemWriter<Pay2> jpaItemWriterJobWriter() throws Exception {
		JpaItemWriter<Pay2> jpaItemWriter = new JpaItemWriter<>();

		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

		jpaItemWriter.afterPropertiesSet();

		return jpaItemWriter;
	}

}
