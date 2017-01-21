package com.ydes.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
@Configuration
public class TwitterDataBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job getTwitterDataJob(Step getTwitterDataStep) {
		return jobBuilderFactory.get("getTwitterDataJob").preventRestart()
				.incrementer(new TimestampIncrementer())
				.flow(getTwitterDataStep).end().build();
	}

	@Bean
	protected Step getTwitterDataStep(Tasklet twitterDataTasklet) {
		return stepBuilderFactory.get("getTwitterDataStep")
				.tasklet(twitterDataTasklet).throttleLimit(1).build();
	}

}
