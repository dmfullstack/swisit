package com.stackroute.swisit.messageservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherResult;

public interface MessageService {
	public void publishmessage(String topic,SearcherResult message) throws JsonProcessingException;
	public void listenmessage(String topic);
}
