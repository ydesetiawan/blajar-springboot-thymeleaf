package com.ydes.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ydes.persistence.model.TwitterData;

@Configuration
public class TwitterDataBatchConfig {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public Job getTwitterDataJob(Step getTwitterDataStep) {
		return jobs.get("getTwitterDataJob").start(getTwitterDataStep).build();
	}

	@Bean
	protected Step getTwitterDataStep(
			ItemReader<TwitterData> twitterDataReader,
			ItemProcessor<TwitterData, TwitterData> twitterDataProcessor,
			ItemWriter<TwitterData> twitterDataWriter) {
		return steps.get("getTwitterDataStep")
				.<TwitterData, TwitterData> chunk(10)
				.reader(twitterDataReader)
				.processor(twitterDataProcessor)
				.writer(twitterDataWriter).build();
	}

}
