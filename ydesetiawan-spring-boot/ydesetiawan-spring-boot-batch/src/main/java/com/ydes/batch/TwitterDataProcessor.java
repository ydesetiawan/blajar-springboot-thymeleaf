package com.ydes.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.ydes.persistence.model.TwitterData;

@Component
public class TwitterDataProcessor implements
        ItemProcessor<TwitterData, TwitterData> {

    @Override
    public TwitterData process(TwitterData item) throws Exception {
        TwitterData a = item;
        return item;
    }

}
