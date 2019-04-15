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
 * @author nofaterock
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
			.start(scopeStep1(null))
			.next(scopeStep2())
			.build();
	}

	/**
	 * JobScope 로 인해 Job 이 실행될 때 Bean 이 생성됨.
	 * 이로 인해 Parameter 의 binding 시점을 늦출 수 있음.
	 * 또, 병렬 실행 시에도 각각의 Bean 이 생성되므로, 서로 간섭하지 않도록 할 수 있음.
	 */
	@Bean
	@JobScope
	public Step scopeStep1(@Value("#{jobParameters[amount]}") Long amount) {
		return stepBuilderFactory.get("scopeStep1")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is ScopeStep1 (@JobScope)");
				log.info(">>>>> amount = {}", amount);
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step scopeStep2() {
		return stepBuilderFactory.get("scopeStep2")
			.tasklet(scopeStep2Tasklet(null))
			.build();
	}

	/**
	 * StepScope 로 인해 Step 이 실행될 때 Bean 이 생성됨.
	 * 이로 인해 Parameter 의 binding 시점을 늦출 수 있음.
	 * 또, 병렬 실행 시에도 각각의 Bean 이 생성되므로, 서로 간섭하지 않도록 할 수 있음.
	 */
	@Bean
	@StepScope
	public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return (contribution, chunkContext) -> {
			log.info(">>>>> This is ScopeStep2 (@StepScope)");
			log.info(">>>>> requestDate = {}", requestDate);
			return RepeatStatus.FINISHED;
		};
	}

}
