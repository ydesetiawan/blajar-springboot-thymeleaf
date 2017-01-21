package com.ydes.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AssertionErrors;
/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = { TwitterDataBatchConfig.class,
		BatchConfigTest.class })
public class TwitterDataBatchConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void shouldImportStores() throws Exception {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		StepExecution firstStepExecution = jobExecution.getStepExecutions()
				.iterator().next();

		AssertionErrors.assertEquals("job execution status",
				jobExecution.getExitStatus(), ExitStatus.COMPLETED);
		AssertionErrors.assertEquals("job execution commit count",
				firstStepExecution.getCommitCount(), 1);

	}

}
