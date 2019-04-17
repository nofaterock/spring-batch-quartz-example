package com.nofaterock.batch.job.extra;

import com.nofaterock.batch.pay.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
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
public class CompositeProcessorWithoutGenericJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job compositeProcessorWithoutGenericJob() {
		return jobBuilderFactory.get("compositeProcessorWithoutGenericJob")
			.preventRestart()
			.start(compositeProcessorWithoutGenericJobStep())
			.build();
	}

	@Bean
	@SuppressWarnings("unchecked")
	public Step compositeProcessorWithoutGenericJobStep() {
		return stepBuilderFactory.get("compositeProcessorWithoutGenericJobStep")
			.<Pay, String>chunk(CHUNK_SIZE)
			.reader(compositeProcessorWithoutGenericJobReader(null))
			.processor(compositeProcessorWithoutGenericJobProcessor())
			.writer(compositeProcessorWithoutGenericJobWriter())
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Pay> compositeProcessorWithoutGenericJobReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("compositeProcessorWithoutGenericJobReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p WHERE p.amount >= :amount")
			.parameterValues(new HashMap<String, Object>() {
				{
					put("amount", amount);
				}
			})
			.build();
	}

	@Bean
	@SuppressWarnings("unchecked")
	public CompositeItemProcessor compositeProcessorWithoutGenericJobProcessor() {
		List<ItemProcessor> delegates = new ArrayList<>(2);
		delegates.add(processor1());
		delegates.add(processor2());

		CompositeItemProcessor processor = new CompositeItemProcessor();
		processor.setDelegates(delegates);

		return processor;
	}

	private ItemProcessor<Pay, String> processor1() {
		return Pay::getTxName;
	}

	private ItemProcessor<String, String> processor2() {
		return txName -> "<" + txName + ">";
	}

	@Bean
	public ItemWriter<String> compositeProcessorWithoutGenericJobWriter() {
		return pays -> {
			for (String pay : pays) {
				log.info(">>>>> pay = {}", pay);
			}
		};
	}

}
