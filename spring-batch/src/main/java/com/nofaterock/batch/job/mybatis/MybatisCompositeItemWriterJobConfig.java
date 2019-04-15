package com.nofaterock.batch.job.mybatis;

import com.nofaterock.batch.pay.domain.Pay;
import com.nofaterock.batch.pay.domain.Pay2;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MybatisCompositeItemWriterJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job mybatisCompositeItemWriterJob() {
		return jobBuilderFactory.get("mybatisCompositeItemWriterJob")
			.start(mybatisCompositeItemWriterStep())
			.build();
	}

	@Bean
	@SuppressWarnings("unchecked")
	public Step mybatisCompositeItemWriterStep() {
		return stepBuilderFactory.get("mybatisCompositeItemWriterStep")
			.<Pay, Pay2>chunk(CHUNK_SIZE)
			.reader(mybatisCompositeItemWriterReader(null))
			.processor(mybatisCompositeItemWriterProcessor())
			.writer(mybatisCompositeItemWriterWriter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisPagingItemReader<Pay> mybatisCompositeItemWriterReader(@Value("#{jobParameters[amount]}") Long amount) {
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
	public ItemProcessor<Pay, Pay2> mybatisCompositeItemWriterProcessor() {
		return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	@SuppressWarnings("unchecked")
	public CompositeItemWriter mybatisCompositeItemWriterWriter() {
		CompositeItemWriter compositeItemWriter = new CompositeItemWriter();
		List<ItemWriter<Pay2>> writers = new ArrayList<>(2);
		writers.add(payItemWriter());
		writers.add(pay2ItemWriter());

		compositeItemWriter.setDelegates(writers);
		return compositeItemWriter;
	}

	private ItemWriter<Pay2> payItemWriter() {
		MyBatisBatchItemWriter<Pay2> itemWriter = new MyBatisBatchItemWriterBuilder<Pay2>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.nofaterock.batch.item.repository.PayMapper.insertPay2")
			.build();

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

	private ItemWriter<Pay2> pay2ItemWriter() {
		MyBatisBatchItemWriter<Pay2> itemWriter = new MyBatisBatchItemWriterBuilder<Pay2>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.nofaterock.batch.item.repository.PayMapper.insertPay2")
			.build();

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

}
