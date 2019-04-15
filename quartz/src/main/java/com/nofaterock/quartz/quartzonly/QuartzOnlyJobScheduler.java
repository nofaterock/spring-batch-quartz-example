package com.nofaterock.quartz.quartzonly;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author nofaterock
 * @since 2019-04-02
 */
@Component
public class QuartzOnlyJobScheduler {

	public Scheduler scheduler;

	@PostConstruct
	public void start() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		this.scheduler = schedulerFactory.getScheduler();
		this.scheduler.start();

		JobDetail quartzOnlyJob1Detail = JobBuilder.newJob(QuartzOnlyJob1.class)
			.withIdentity(new JobKey(QuartzOnlyJob1.JOB_NAME))
			.usingJobData("name", "승룡")
			.build();

		Trigger quartzOnlyJob1Trigger = TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(QuartzOnlyJob1.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
			.build();

		JobDetail quartzOnlyJob2Detail = JobBuilder.newJob(QuartzOnlyJob2.class)
			.withIdentity(new JobKey(QuartzOnlyJob2.JOB_NAME))
			.usingJobData("name", "승룡")
			.build();

		Trigger quartzOnlyJob2Trigger = TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(QuartzOnlyJob2.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
			.build();

		scheduler.scheduleJob(quartzOnlyJob1Detail, quartzOnlyJob1Trigger);
		scheduler.scheduleJob(quartzOnlyJob2Detail, quartzOnlyJob2Trigger);
	}

}
