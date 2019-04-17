package com.nofaterock.batch.job.rest;

import com.nofaterock.batch.pay.Pay;
import com.nofaterock.batch.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author nofaterock
 * @since 2019-04-15
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job restJob() {
		return jobBuilderFactory.get("restJob")
			.start(restJobStep())
			.build();
	}

	@Bean
	public Step restJobStep() {
		return stepBuilderFactory.get("restJobStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(restJobReader())
			.processor(restJobProcessor())
			.writer(restJobWriter())
			.build();
	}

	@Bean
	public ItemReader<Pay> restJobReader() {
		return new RestPayReader(new RestTemplate());
	}

	@Bean
	public ItemProcessor<Pay, Pay2> restJobProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public ItemWriter<Pay2> restJobWriter() {
		return new RestPay2Writer(new RestTemplate());
	}

}
