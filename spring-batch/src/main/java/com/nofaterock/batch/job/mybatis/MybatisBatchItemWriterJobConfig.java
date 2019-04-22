package com.nofaterock.batch.job.mybatis;

import com.nofaterock.batch.pay.Pay;
import com.nofaterock.batch.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author 한승룡
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MybatisBatchItemWriterJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job mybatisBatchItemWriterJob() {
		return jobBuilderFactory.get("mybatisBatchItemWriterJob")
			.start(mybatisBatchItemWriterJobStep())
			.build();
	}

	@Bean
	public Step mybatisBatchItemWriterJobStep() {
		return stepBuilderFactory.get("mybatisBatchItemWriterJobStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(mybatisBatchItemWriterJobReader(null))
			.processor(mybatisBatchItemWriterJobProcessor())
			.writer(mybatisBatchItemWriterJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisPagingItemReader<Pay> mybatisBatchItemWriterJobReader(@Value("#{jobParameters[amount]}") Long amount) {
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
	public ItemProcessor<Pay, Pay2> mybatisBatchItemWriterJobProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public MyBatisBatchItemWriter<Pay2> mybatisBatchItemWriterJobWriter() {
		MyBatisBatchItemWriter<Pay2> itemWriter = new MyBatisBatchItemWriterBuilder<Pay2>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.nofaterock.batch.item.repository.PayMapper.insertPay2")
			.build();

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

}
