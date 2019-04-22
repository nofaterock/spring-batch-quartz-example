package com.nofaterock.batch.job.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 한승룡
 * @since 2019-04-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScopeJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job scopeJob() {
		return jobBuilderFactory.get("scopeJob")
			.start(scopeJobStep1(null))
			.next(scopeJobStep2())
			.build();
	}

	/**
	 * JobScope 로 인해 Job 이 실행될 때 Bean 이 생성됨.
	 * 이로 인해 Parameter 의 binding 시점을 늦출 수 있음.
	 * 또, 병렬 실행 시에도 각각의 Bean 이 생성되므로, 서로 간섭하지 않도록 할 수 있음.
	 */
	@Bean
	@JobScope
	public Step scopeJobStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return stepBuilderFactory.get("scopeJobStep1")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is scopeJobStep1 (@JobScope)");
				log.info(">>>>> requestDate = {}", requestDate);
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step scopeJobStep2() {
		return stepBuilderFactory.get("scopeJobStep2")
			.tasklet(scopeJobStep2Tasklet(null))
			.build();
	}

	/**
	 * StepScope 로 인해 Step 이 실행될 때 Bean 이 생성됨.
	 * 이로 인해 Parameter 의 binding 시점을 늦출 수 있음.
	 * 또, 병렬 실행 시에도 각각의 Bean 이 생성되므로, 서로 간섭하지 않도록 할 수 있음.
	 */
	@Bean
	@StepScope
	public Tasklet scopeJobStep2Tasklet(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return (contribution, chunkContext) -> {
			log.info(">>>>> This is scopeJobStep2 (@StepScope)");
			log.info(">>>>> requestDate = {}", requestDate);
			return RepeatStatus.FINISHED;
		};
	}

}
