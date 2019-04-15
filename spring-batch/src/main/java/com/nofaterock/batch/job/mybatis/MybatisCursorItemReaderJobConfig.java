package com.nofaterock.batch.job.mybatis;

import com.nofaterock.batch.pay.domain.Pay;
import com.nofaterock.batch.pay.domain.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MybatisCursorItemReaderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job mybatisCursorItemReaderJob() {
		return jobBuilderFactory.get("mybatisCursorItemReaderJob")
			.start(mybatisCursorItemReaderStep())
			.build();
	}

	@Bean
	public Step mybatisCursorItemReaderStep() {
		return stepBuilderFactory.get("mybatisCursorItemReaderStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(mybatisCursorItemReaderReader(null))
			.processor(mybatisCursorItemReaderProcessor())
			.writer(mybatisCursorItemReaderWriter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisCursorItemReader<Pay> mybatisCursorItemReaderReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new MyBatisCursorItemReaderBuilder<Pay>()
			.sqlSessionFactory(sqlSessionFactory)
			.queryId("com.nofaterock.batch.item.repository.PayMapper.selectAll")
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.build();
	}

	@Bean
	public ItemProcessor<Pay, Pay2> mybatisCursorItemReaderProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public ItemWriter<Pay2> mybatisCursorItemReaderWriter() {
		return items -> {
			for (Pay2 item : items) {
				log.info("Custom - {}", item);
			}
		};
	}

}
