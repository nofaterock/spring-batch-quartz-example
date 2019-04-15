package com.nofaterock.quartz.quartzonly;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author nofaterock
 * @since 2019-04-02
 */
@Slf4j
public class QuartzOnlyJob2 implements Job {

	public static final String JOB_NAME = "QuartzOnlyJob2";

	@Override
	public void execute(JobExecutionContext context) {
		log.info("{} : Hello {}", JOB_NAME, context.getJobDetail().getJobDataMap().get("name").toString());
	}

}
