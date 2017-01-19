package com.ydes.batch;

import java.util.Date;
import java.util.UUID;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import com.ydes.persistence.model.TwitterData;

@Component
@StepScope
public class TwitterDataReader implements ItemReader<TwitterData> {

	@Override
	public TwitterData read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		TwitterData td = new TwitterData();
		td.setUuid(UUID.randomUUID().toString());
		td.setPostingDate(new Date());
		td.setProfileImgUrl(" ");
		td.setProfileName(" ");
		td.setText("");
		return td;
	}

}
