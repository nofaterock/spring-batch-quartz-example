package com.nofaterock.quartz.springquartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author 한승룡
 * @since 2019-04-12
 */
@Slf4j
public class SpringQuartzJob1 extends QuartzJobBean {

	public static final String JOB_NAME = "SpringQuartzJob1";

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("{} : Hello {}", JOB_NAME, context.getJobDetail().getJobDataMap().get("name").toString());
	}
}
