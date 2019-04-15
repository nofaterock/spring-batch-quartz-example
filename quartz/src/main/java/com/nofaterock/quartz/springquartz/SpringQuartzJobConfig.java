package com.nofaterock.quartz.springquartz;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author nofaterock
 * @since 2019-04-12
 */
@RequiredArgsConstructor
@Configuration
public class SpringQuartzJobConfig {

	@Bean
	public JobDetail springQuartzJob1Detail() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("name", "승룡");

		return JobBuilder.newJob(SpringQuartzJob1.class)
			.withIdentity(new JobKey(SpringQuartzJob1.JOB_NAME))
			.setJobData(jobDataMap)
			.storeDurably()
			.build();
	}

	@Bean
	public Trigger springQuartzJob1Trigger() {
		return TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(SpringQuartzJob1.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
			.forJob(SpringQuartzJob1.JOB_NAME)
			.build();
	}

	@Bean
	public JobDetail springQuartzJob2Detail() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("name", "승룡");

		return JobBuilder.newJob(SpringQuartzJob2.class)
			.withIdentity(new JobKey(SpringQuartzJob2.JOB_NAME))
			.setJobData(jobDataMap)
			.storeDurably()
			.build();
	}

	@Bean
	public Trigger springQuartzJob2Trigger() {
		return TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(SpringQuartzJob2.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
			.forJob(SpringQuartzJob2.JOB_NAME)
			.build();
	}

	@Bean
	public SchedulerFactoryBean springQuartzJob1SchedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(springQuartzJob1Trigger(), springQuartzJob2Trigger());
		scheduler.setJobDetails(springQuartzJob1Detail(), springQuartzJob2Detail());
		return scheduler;
	}

}
