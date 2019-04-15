package com.nofaterock.batch.job.extra;

import com.nofaterock.batch.pay.domain.Pay;
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
public class CompositeProcessorJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private static final int CHUNK_SIZE = 10;

	@Bean
	public Job compositeProcessorJob() {
		return jobBuilderFactory.get("compositeProcessorJob")
			.preventRestart()
			.start(compositeProcessorStep())
			.build();
	}

	@Bean
	public Step compositeProcessorStep() {
		return stepBuilderFactory.get("compositeProcessorStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(compositeProcessorReader(null))
			.processor(compositeProcessorProcessor())
			.writer(compositeProcessorWriter())
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Pay> compositeProcessorReader(@Value("#{jobParameters[amount]}") Long amount) {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("compositeProcessorReader")
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
	public CompositeItemProcessor<Pay, Pay> compositeProcessorProcessor() {
		List<ItemProcessor<Pay, Pay>> delegates = new ArrayList<>(2);
		delegates.add(processor1());
		delegates.add(processor2());

		CompositeItemProcessor<Pay, Pay> processor = new CompositeItemProcessor<>();
		processor.setDelegates(delegates);

		return processor;
	}

	private ItemProcessor<Pay, Pay> processor1() {
		return pay -> {
			pay.setName("<<<<< " + pay.getName());
			return pay;
		};
	}

	private ItemProcessor<Pay, Pay> processor2() {
		return pay -> {
			pay.setName(pay.getName() + " >>>>>");
			return pay;
		};
	}

	@Bean
	public ItemWriter<Pay> compositeProcessorWriter() {
		return items -> {
			for (Pay item : items) {
				log.info("Custom - {}", item);
			}
		};
	}

}
