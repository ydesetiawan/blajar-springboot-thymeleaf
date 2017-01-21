package com.ydes.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import com.ydes.config.TwitterSearchParamsConfig;
import com.ydes.persistence.model.TwitterData;
import com.ydes.persistence.repository.TwitterDataRepository;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
@Component
@StepScope
public class TwitterDataTasklet implements Tasklet {

	private Logger log = LoggerFactory.getLogger(TwitterDataTasklet.class);

	@Autowired
	private Twitter twitter;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@Autowired
	private TwitterDataRepository twitterDataRepository;
	@Autowired
	private TwitterSearchParamsConfig searchParamsConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		SearchParameters searchParams = new SearchParameters(
				searchParamsConfig.getQuery())
				.resultType(searchParamsConfig.getResultType())
				.lang(searchParamsConfig.getLang())
				.locale(searchParamsConfig.getLocale()).includeEntities(false);

		SearchResults results = twitter.searchOperations().search(searchParams);

		List<Tweet> tweets = results.getTweets();
		for (Tweet tweet : tweets) {
			contribution.incrementReadCount();
			TwitterData td = new TwitterData();
			td.setId(tweet.getId());
			td.setProfileName(tweet.getUser().getName());
			td.setProfileImgUrl(tweet.getUser().getProfileImageUrl());
			td.setText(tweet.getText());
			td.setPostingDate(tweet.getCreatedAt());
			twitterDataRepository.saveAndFlush(td);
			contribution.incrementWriteCount(1);
			log.info("storing data : " + td.toString());
		}
		contribution.setExitStatus(new ExitStatus(ExitStatus.COMPLETED
				.getExitCode(),
				"Process for job getTwitterDataJob started. total data : ["
						+ contribution.getWriteCount() + "]"));
		return RepeatStatus.FINISHED;
	}
}
