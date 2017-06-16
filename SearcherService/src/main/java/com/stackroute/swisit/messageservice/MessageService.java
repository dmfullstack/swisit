package com.stackroute.swisit.messageservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SwisitBean;

public interface MessageService {
	public void publishmessage(String topic,SwisitBean message) throws JsonProcessingException;
	public void listenmessage(String topic);
}
