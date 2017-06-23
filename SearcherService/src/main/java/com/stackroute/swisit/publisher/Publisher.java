package com.stackroute.swisit.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherResult;

public interface Publisher {
	public void publishmessage(String topic, SearcherResult message) throws JsonProcessingException;
}
