package com.nofaterock.batch.job.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 한승룡
 * @since 2019-04-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob")
			.start(simpleJobStep())
			.build();
	}

	@Bean
	public Step simpleJobStep() {
		return stepBuilderFactory.get("simpleJobStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is simpleJobStep");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

}
