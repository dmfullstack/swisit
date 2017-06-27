package com.stackroute.swisit.intialproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherJob;

public interface IntialProducer {
	public void publishmessage(String topic,SearcherJob message) throws JsonProcessingException;
}
