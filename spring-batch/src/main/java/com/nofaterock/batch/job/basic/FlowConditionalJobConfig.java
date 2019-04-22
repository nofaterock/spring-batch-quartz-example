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
 * @author 한승룡
 * @since 2019-04-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlowConditionalJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private enum CustomExitStatus {
		COMPLETE_WITH_SKIPS
	}

	@Bean
	public Job flowConditionalJob() {
		return jobBuilderFactory.get("flowConditionalJob")
			.start(flowConditionalJobStep1())
				.on("FAILED") // flowJobStep1 의 결과가 FAILED 일 경우
				.to(flowConditionalJobStep3()) // flowJobStep3 으로 이동
				.on("*") // flowJobStep3 의 ExitStatus 가 무엇이든 간에
				.end() // 종료
			.from(flowConditionalJobStep1()) // flowJobStep1
				.on("*") // flowJobStep1 의 ExitStatus 가 위에서 설정한 FAILED 가 아닌 다른 값인 경우
				.to(flowConditionalJobStep2()) // flowJobStep2 로 이동
				.on(CustomExitStatus.COMPLETE_WITH_SKIPS.toString()) // flowJobStep2 의 ExitStatus 가 COMPLETE_WITH_SKIPS 인 경우
				.end() // 종료
			.from(flowConditionalJobStep2()) // flowJobStep2
				.on("*") // flowJobStep2 의 ExitStatus 가 위에서 설정한 COMPLETE_WITH_SKIPS 가 아닌 다른 값인 경우
				.to(flowConditionalJobStep3()) // flowJobStep3 으로 이동
				.on("*") // flowJobStep3 의 ExitStatus 가 무엇이든 간에
				.end() // 종료
			.end() // 작업종료
			.build();
	}

	@Bean
	public Step flowConditionalJobStep1() {
		return stepBuilderFactory.get("flowConditionalJobStep1")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is flowConditionalJobStep1");

				// 이 부분을 주석처리 하면서 테스트 할 것
				contribution.setExitStatus(ExitStatus.FAILED);

				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step flowConditionalJobStep2() {
		return stepBuilderFactory.get("flowConditionalJobStep2")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is flowConditionalJobStep2");

				// 이 부분을 주석처리 하면서 테스트 할 것
				contribution.setExitStatus(new ExitStatus(CustomExitStatus.COMPLETE_WITH_SKIPS.toString()));

				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step flowConditionalJobStep3() {
		return stepBuilderFactory.get("flowConditionalJobStep3")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> This is flowConditionalJobStep3");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

}
