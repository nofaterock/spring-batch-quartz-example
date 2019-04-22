package com.nofaterock.batchwithquartz.quartz;

import com.nofaterock.batchwithquartz.job.PayJobConfig;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author 한승룡
 * @since 2019-04-12
 */
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	private final JobLauncher jobLauncher;
	private final JobLocator jobLocator;

	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		return jobRegistryBeanPostProcessor;
	}

	@Bean
	public JobDetail job1Detail() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("jobName", PayJobConfig.JOB_NAME);
		jobDataMap.put("jobLauncher", jobLauncher);
		jobDataMap.put("jobLocator", jobLocator);

		return JobBuilder.newJob(QuartzJob.class)
			.withIdentity(new JobKey(PayJobConfig.JOB_NAME))
			.setJobData(jobDataMap)
			.storeDurably()
			.build();
	}

	@Bean
	public Trigger job1Trigger() {
		return TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(PayJobConfig.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
			.forJob(PayJobConfig.JOB_NAME)
			.build();
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(job1Trigger());
		scheduler.setJobDetails(job1Detail());
		return scheduler;
	}

}
