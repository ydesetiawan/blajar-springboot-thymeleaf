package com.ydes.batch;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ydes.config.Application;
import com.ydes.config.DatabaseConfig;
/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Configuration
@Import({ Application.class, BatchConfig.class, DatabaseConfig.class })
public class BatchConfigTest {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}
}
