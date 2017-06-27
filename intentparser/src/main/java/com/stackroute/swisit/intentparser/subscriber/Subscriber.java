package com.stackroute.swisit.intentparser.subscriber;

import com.stackroute.swisit.intentparser.domain.CrawlerResult;

import java.util.List;

/**
 * Created by user on 21/6/17.
 */
public interface Subscriber {
    public List<CrawlerResult> receivingMessage(String string);
}
