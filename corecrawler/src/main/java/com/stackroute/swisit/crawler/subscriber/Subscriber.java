package com.stackroute.swisit.crawler.subscriber;

import java.util.List;

import com.stackroute.swisit.crawler.domain.SearcherResult;

public interface Subscriber {
	public List<SearcherResult> receivingMessage(String string);
}
