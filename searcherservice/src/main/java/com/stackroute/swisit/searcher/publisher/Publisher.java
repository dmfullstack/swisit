package com.stackroute.swisit.searcher.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SearcherResult;

public interface Publisher {
	public void publishmessage(String topic, SearcherResult message) throws JsonProcessingException;
}
