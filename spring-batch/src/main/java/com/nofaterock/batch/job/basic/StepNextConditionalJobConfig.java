package com.nofaterock.batch.job.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionalJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private enum CustomExitStatus {
		COMPLETE_WITH_SKIPS
	}

	@Bean
	public Job stepNextConditionalJob() {
		return jobBuilderFactory.get("stepNextConditionalJob")
			.start(conditionalStep1())
				.on("FAILED") // step1 의 결과가 FAILED 일 경우
				.to(conditionalStep3()) // step3 으로 이동
				.on("*") // step3 의 ExitStatus 가 무엇이든 간에
				.end() // 종료
			.from(conditionalStep1()) // step1
				.on("*") // step1 의 ExitStatus 가 위에서 설정한 FAILED 가 아닌 다른 값인 경우
				.to(conditionalStep2()) // step2 로 이동
				.on(CustomExitStatus.COMPLETE_WITH_SKIPS.toString()) // step2 의 ExitStatus 가 COMPLETE_WITH_SKIPS 인 경우
				.end() // 종료
			.from(conditionalStep2()) // step2
				.on("*") // step2 의 ExitStatus 가 위에서 설정한 COMPLETE_WITH_SKIPS 가 아닌 다른 값인 경우
				.to(conditionalStep3()) // step3 으로 이동
				.on("*") // step3 의 ExitStatus 가 무엇이든 간에
				.end() // 종료
			.end() // 작업종료
			.build();
	}

	@Bean
	public Step conditionalStep1() {
		return stepBuilderFactory.get("conditionalStep1")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is ConditionalStep1");

				// 이 부분을 주석처리 하면서 테스트 할 것
				contribution.setExitStatus(ExitStatus.FAILED);

				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step conditionalStep2() {
		return stepBuilderFactory.get("conditionalStep2")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is ConditionalStep2");

				// 이 부분을 주석처리 하면서 테스트 할 것
				contribution.setExitStatus(new ExitStatus(CustomExitStatus.COMPLETE_WITH_SKIPS.toString()));

				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step conditionalStep3() {
		return stepBuilderFactory.get("conditionalStep3")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is conditionalStep3");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

}
