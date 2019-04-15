package com.nofaterock.quartz.controller;

import com.nofaterock.quartz.quartzonly.QuartzOnlyJob1;
import com.nofaterock.quartz.quartzonly.QuartzOnlyJob2;
import com.nofaterock.quartz.quartzonly.QuartzOnlyJobScheduler;
import com.nofaterock.quartz.springquartz.SpringQuartzJob1;
import com.nofaterock.quartz.springquartz.SpringQuartzJob2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nofaterock
 * @since 2019-04-03
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class ReScheduleController {

	private final QuartzOnlyJobScheduler quartzOnlyJobScheduler;
	private final SchedulerFactoryBean schedulerFactoryBean;

	@GetMapping("/quartzOnlyJob1/{interval}")
	public void quartzOnlyJob1(@PathVariable Integer interval) throws SchedulerException {
		quartzOnlyJobScheduler.scheduler.rescheduleJob(new TriggerKey(QuartzOnlyJob1.JOB_NAME + "Trigger"), TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(QuartzOnlyJob1.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/" + interval + " * * * * ?"))
			.build());

		log.info("{}Trigger rescheduled to {} sec.", QuartzOnlyJob1.JOB_NAME, interval);
	}

	@GetMapping("/quartzOnlyJob2/{interval}")
	public void job2(@PathVariable Integer interval) throws SchedulerException {
		quartzOnlyJobScheduler.scheduler.rescheduleJob(new TriggerKey(QuartzOnlyJob2.JOB_NAME + "Trigger"), TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(QuartzOnlyJob2.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/" + interval + " * * * * ?"))
			.build());

		log.info("{}Trigger rescheduled to {} sec.", QuartzOnlyJob2.JOB_NAME, interval);
	}

	@GetMapping("/springQuartzJob1/{interval}")
	public void springQuartzJob1(@PathVariable Integer interval) throws SchedulerException {
		schedulerFactoryBean.getScheduler().rescheduleJob(new TriggerKey(SpringQuartzJob1.JOB_NAME + "Trigger"), TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(SpringQuartzJob1.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/" + interval + " * * * * ?"))
			.build());

		log.info("{}Trigger rescheduled to {} sec.", SpringQuartzJob1.JOB_NAME, interval);
	}

	@GetMapping("/springQuartzJob2/{interval}")
	public void springQuartzJob2(@PathVariable Integer interval) throws SchedulerException {
		schedulerFactoryBean.getScheduler().rescheduleJob(new TriggerKey(SpringQuartzJob2.JOB_NAME + "Trigger"), TriggerBuilder.newTrigger()
			.withIdentity(new TriggerKey(SpringQuartzJob2.JOB_NAME + "Trigger"))
			.withSchedule(CronScheduleBuilder.cronSchedule("0/" + interval + " * * * * ?"))
			.build());

		log.info("{}Trigger rescheduled to {} sec.", SpringQuartzJob2.JOB_NAME, interval);
	}
}
