package com.stackroute.swisit.crawler.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.CrawlerResult;

public interface Publisher {
	public void publishingMessage(String topicName,CrawlerResult message) throws JsonProcessingException;
}
