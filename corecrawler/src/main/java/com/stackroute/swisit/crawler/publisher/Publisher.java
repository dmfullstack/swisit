package com.stackroute.swisit.crawler.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.CrawlerResult;

public interface Publisher {
	
	/*------------- Method to publish message via a messaging service--------------*/
	
	public void publishMessage(String topicName,CrawlerResult message) throws JsonProcessingException;
}
