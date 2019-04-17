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
 * @author nofaterock
 * @since 2019-04-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlowJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job flowJob() {
		return jobBuilderFactory.get("flowJob")
			.start(flowJobStep1())
			.next(flowJobStep2())
			.next(flowJobStep3())
			.build();
	}

	@Bean
	public Step flowJobStep1() {
		return stepBuilderFactory.get("flowJobStep1")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is flowJobStep1");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step flowJobStep2() {
		return stepBuilderFactory.get("flowJobStep2")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is flowJobStep2");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step flowJobStep3() {
		return stepBuilderFactory.get("florJobStep3")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is florJobStep3");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

}
