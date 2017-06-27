package com.stackroute.swisit.searcher.kafkaserialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.searcher.domain.SearcherJob;

public class QueryDeserializer implements Deserializer<SearcherJob>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearcherJob deserialize(String topic, byte[] data) {
		// TODO Auto-generated method stub
		ObjectMapper o=new ObjectMapper();
		SearcherJob c=null;
		try{
			System.out.println(data.toString());
			c=o.readValue(data,SearcherJob.class);
			}
		catch(Exception e){
			System.out.println("hi this "+e);
		}
		return c;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
