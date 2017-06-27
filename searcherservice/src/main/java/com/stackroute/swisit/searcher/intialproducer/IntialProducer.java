package com.stackroute.swisit.searcher.intialproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SearcherJob;

public interface IntialProducer {
	public void publishmessage(String topic,SearcherJob message) throws JsonProcessingException;
}
