package com.ydes.batch;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public class TimestampIncrementer implements JobParametersIncrementer {

    private static final String TIMESTAMP_KEY = "timestamp";

    private String key = TIMESTAMP_KEY;

    /**
     * Set the time stamp parameter.
     */
    @Override
    public JobParameters getNext(JobParameters parameters) {

        JobParameters params = (parameters == null) ? new JobParameters()
                : parameters;

        return new JobParametersBuilder(params).addDate(key, new Date())
                .toJobParameters();
    }

    /**
     * The name of the time stamp in the job parameters. Defaults to
     * "timestamp".
     *
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
}
