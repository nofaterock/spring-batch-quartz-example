package com.nofaterock.batch.job.mybatis;

import com.nofaterock.batch.pay.Pay;
import com.nofaterock.batch.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
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
public class MybatisPagingItemReaderJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job mybatisPagingItemReaderJob() {
		return jobBuilderFactory.get("mybatisPagingItemReaderJob")
			.start(mybatisPagingItemReaderJobStep())
			.build();
	}

	@Bean
	public Step mybatisPagingItemReaderJobStep() {
		return stepBuilderFactory.get("mybatisPagingItemReaderJobStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(mybatisPagingItemReaderJobReader(null))
			.processor(mybatisPagingItemReaderJobProcessor())
			.writer(mybatisPagingItemReaderJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisPagingItemReader<Pay> mybatisPagingItemReaderJobReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new MyBatisPagingItemReaderBuilder<Pay>()
			.sqlSessionFactory(sqlSessionFactory)
			.queryId("com.nofaterock.batch.item.repository.PayMapper.selectPaged")
			.pageSize(CHUNK_SIZE)
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.build();
	}

	@Bean
	public ItemProcessor<Pay, Pay2> mybatisPagingItemReaderJobProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public ItemWriter<Pay2> mybatisPagingItemReaderJobWriter() {
		return pay2s -> {
			for (Pay2 pay2 : pay2s) {
				log.info(">>>>> Pay2 = {}", pay2);
			}
		};
	}

}
