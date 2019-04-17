package com.nofaterock.batch.job.jpa;

import com.nofaterock.batch.pay.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class jpaPagingItemReaderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaPagingItemReaderJob() throws Exception {
		return jobBuilderFactory.get("jpaPagingItemReaderJob")
			.start(jpaPagingItemReaderJobStep())
			.build();
	}

	@Bean
	public Step jpaPagingItemReaderJobStep() throws Exception {
		return stepBuilderFactory.get("jpaPagingItemReaderJobStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jpaPagingItemReaderJobReader(null))
			.writer(jpaPagingItemReaderJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Pay> jpaPagingItemReaderJobReader(@Value("#{jobParameters[amount]}") Long amount) throws Exception {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaPagingItemReaderJobReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p WHERE amount >= :amount")
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.build();
	}

	@Bean
	public ItemWriter<Pay> jpaPagingItemReaderJobWriter() {
		return pays -> {
			for (Pay pay : pays) {
				log.info(">>>>> Pay = {}", pay);
			}
		};
	}
}
