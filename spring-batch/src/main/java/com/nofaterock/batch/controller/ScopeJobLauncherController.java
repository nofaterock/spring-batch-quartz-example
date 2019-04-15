package com.nofaterock.batch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nofaterock
 * @since 2019-04-03
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ScopeJobLauncherController {

	private final JobLauncher jobLauncher;
	private final Job scopeJob;

	@GetMapping("/launchScopeJob/{requestDate}")
	public String launchScopeJob(@PathVariable String requestDate) {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("requestDate", requestDate)
			.toJobParameters();

		try {
			jobLauncher.run(scopeJob, jobParameters);
		} catch (Exception e) {
			log.error("error.", e);
		}
		return null;
	}

}
