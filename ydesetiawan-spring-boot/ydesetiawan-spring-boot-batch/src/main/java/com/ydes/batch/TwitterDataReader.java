package com.ydes.batch;

import java.util.Date;
import java.util.UUID;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import com.ydes.persistence.model.TwitterData;

@Component
@StepScope
public class TwitterDataReader implements ItemStreamReader<TwitterData> {

    // @Autowired
    // private Twitter twitter;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.batch.item.ItemStream#open(org.springframework.batch
     * .item.ExecutionContext)
     */
    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.batch.item.ItemStream#update(org.springframework.
     * batch.item.ExecutionContext)
     */
    @Override
    public void update(ExecutionContext executionContext)
            throws ItemStreamException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.ItemStream#close()
     */
    @Override
    public void close() throws ItemStreamException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.ItemReader#read()
     */
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

        // SearchResults results = twitter.searchOperations().search(
        // new SearchParameters("#spring").lang("id")
        // .resultType(SearchParameters.ResultType.RECENT)
        // .count(2).includeEntities(false));
        //
        // List<Tweet> tweets = results.getTweets();
        // for (Tweet tweet : tweets) {
        // TwitterData td = new TwitterData();
        // td.setProfileName(tweet.getUser().getName());
        // td.setProfileImgUrl(tweet.getUser().getProfileImageUrl());
        // td.setText(tweet.getText());
        // td.setPostingDate(tweet.getCreatedAt());
        // return td;
        // }

        // return null;
    }

}
