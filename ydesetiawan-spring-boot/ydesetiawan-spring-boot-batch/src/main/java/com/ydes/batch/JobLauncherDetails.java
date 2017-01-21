package com.ydes.batch;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@PersistJobDataAfterExecution
public class JobLauncherDetails extends QuartzJobBean {

    private static Logger log = Logger.getLogger(JobLauncherDetails.class);

    /**
     * Special key in job data map for the name of a job to run.
     */
    public static final String JOB_NAME = "jobName";
    public static final String FORCE_NEW = "forceNew";
    public static final String JOB_DELEGATOR = "jobDelegatorId";
    public static final String JOB_SCHEDULER_ID = "jobSchedulerId";

    private JobLocator jobLocator;

    private JobLauncher jobLauncher;

    private JobExplorer jobExplorer;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Map<String, Object> jobDataMap = context.getMergedJobDataMap();
        String jobName = (String) jobDataMap.get(JOB_NAME);
        boolean forceNew = false;
        if (jobDataMap.containsKey(FORCE_NEW)) {
            forceNew = (boolean) jobDataMap.get(FORCE_NEW);
        }
        JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
        try {
            Job job = jobLocator.getJob(jobName);
            boolean restartable = false;
            if (!forceNew && job.isRestartable()) {
                JobExecution lastExecution = getLastExecution(jobName);
                if (lastExecution != null
                        && (lastExecution.getStatus()
                                .equals(BatchStatus.FAILED) || lastExecution
                                .getStatus().equals(BatchStatus.STOPPED))) {
                    restartable = true;
                    jobParameters = lastExecution.getJobParameters();
                }
            }
            if (!restartable) {
                JobParametersIncrementer incrementer = job
                        .getJobParametersIncrementer();
                if (incrementer != null) {
                    jobParameters = incrementer.getNext(jobParameters);
                }
            }
            log.debug("Launching job: " + job + ", with paramaters: "
                    + jobParameters);
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            context.getJobDetail().getJobDataMap()
                    .put("jobExecution", jobExecution.getId());
        } catch (JobExecutionException e) {
            log.error("Could not execute job.", e);
        }
    }

    private JobParameters getJobParametersFromJobMap(
            Map<String, Object> jobDataMap) {
        JobParametersBuilder builder = new JobParametersBuilder();
        for (Entry<String, Object> entry : jobDataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String && !key.equals(JOB_NAME)
                    && !key.equals(FORCE_NEW)) {
                builder.addString(key, (String) value);
            } else if (value instanceof Float || value instanceof Double) {
                builder.addDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof Integer || value instanceof Long) {
                builder.addLong(key, ((Number) value).longValue());
            } else if (value instanceof Date) {
                builder.addDate(key, (Date) value);
            } else {
                log.debug("JobDataMap contains values which are not job parameters (ignoring).");
            }
        }
        return builder.toJobParameters();
    }

    private JobExecution getLastExecution(String jobName) {
        List<JobInstance> lastInstances = jobExplorer.getJobInstances(jobName,
                0, 1);
        if (!lastInstances.isEmpty()) {
            List<JobExecution> lastExecutions = jobExplorer
                    .getJobExecutions(lastInstances.get(0));
            if (!lastExecutions.isEmpty()) {
                return lastExecutions.get(0);
            }
        }
        return null;
    }

    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void setJobLocator(JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }
}
