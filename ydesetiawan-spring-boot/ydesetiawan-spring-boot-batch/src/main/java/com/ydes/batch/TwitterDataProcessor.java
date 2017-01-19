package com.ydes.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.ydes.persistence.model.TwitterData;

@Component
@StepScope
public class TwitterDataProcessor implements
		ItemProcessor<TwitterData, TwitterData> {

	@Override
	public TwitterData process(TwitterData item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
