package com.nofaterock.batchwithquartz.quartz;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author nofaterock
 * @since 2019-04-12
 */
@Getter
@Setter
public class QuartzJob extends QuartzJobBean {

	private String jobName;
	private JobLauncher jobLauncher;
	private JobLocator jobLocator;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			Job job = jobLocator.getJob(jobName);
			JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobId", context.getJobDetail().getKey().getName() + "_" + System.currentTimeMillis())
				.addLong("amount", 2000L)
				.toJobParameters();

			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
