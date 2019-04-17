package com.nofaterock.batch.job.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author nofaterock
 * @since 2019-04-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
			.start(deciderJobStep()) // deciderJobStep 시작
			.next(decider()) // decider 실행 (홀수, 짝수 구분)
			.from(decider()) // decider 부터
				.on("ODD") // decider 의 FlowExecutionStatus 가 ODD 인 경우
				.to(deciderOddStep()) // deciderOddStep 으로
			.from(decider())
				.on("EVEN") // decider 의 FlowExecutionStatus 가 EVEN 인 경우
				.to(deciderEvenStep()) // deciderEvenStep 으로
			.end() // 작업종료
			.build();
	}

	@Bean
	public Step deciderJobStep() {
		return stepBuilderFactory.get("deciderJobStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is deciderJobStep");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step deciderEvenStep() {
		return stepBuilderFactory.get("deciderEvenStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is deciderEvenStep");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step deciderOddStep() {
		return stepBuilderFactory.get("deciderOddStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is deciderOddStep");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	public static class OddDecider implements JobExecutionDecider {

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			Random rand = new Random();

			int randomNumber = rand.nextInt(50) + 1;
			log.info("랜덤숫자: {}", randomNumber);

			if (randomNumber % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
