package com.stackroute.swisit.crawler.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.crawler.domain.CrawlerResult;



public class CrawlerSerializer implements Serializer<CrawlerResult>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, CrawlerResult data) {
		// TODO Auto-generated method stub
		byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(data).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;}
	

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	

}
