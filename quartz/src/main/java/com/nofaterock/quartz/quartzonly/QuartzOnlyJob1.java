package com.nofaterock.quartz.quartzonly;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author 한승룡
 * @since 2019-04-02
 */
@Slf4j
public class QuartzOnlyJob1 implements Job {

	public static final String JOB_NAME = "QuartzOnlyJob1";

	@Override
	public void execute(JobExecutionContext context) {
		log.info("{} : Hello {}", JOB_NAME, context.getJobDetail().getJobDataMap().get("name").toString());
	}

}
