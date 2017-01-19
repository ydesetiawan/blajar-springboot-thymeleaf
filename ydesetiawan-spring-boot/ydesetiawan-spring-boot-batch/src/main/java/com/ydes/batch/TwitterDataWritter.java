package com.ydes.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.ydes.persistence.model.TwitterData;

@Component
@StepScope
public class TwitterDataWritter implements ItemWriter<TwitterData> {

    private static Logger log = Logger.getLogger(TwitterDataWritter.class);

    @Override
    public void write(List<? extends TwitterData> items) throws Exception {
        // TODO Auto-generated method stub

        for (TwitterData tweet : items) {
            log.info("tweet : " + tweet.toString());
        }

    }

}
