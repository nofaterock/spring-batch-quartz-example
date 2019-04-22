package com.nofaterock.quartz.springquartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * Stateful Job
 *
 * @author 한승룡
 * @since 2019-04-12
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SpringQuartzJob2 extends MethodInvokingJobDetailFactoryBean.MethodInvokingJob {

	public static final String JOB_NAME = "SpringQuartzJob2";

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("{} : Hello {}", JOB_NAME, context.getJobDetail().getJobDataMap().get("name").toString());
	}
}
