package com.stackroute.swisit.searcher.publisher;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SearcherResult;

public class publisherImpl implements Publisher {

	@Override
	public void publishmessage(String topic, SearcherResult message) throws JsonProcessingException {
		// TODO Auto-generated method stub
		Properties configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
		configProperties.put("key.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
		configProperties.put("value.serializer","com.stackroute.swisit.searcher.kafkaserialization.SwisitSerializer");
		Producer producer = new KafkaProducer(configProperties);
		ProducerRecord<String, SearcherResult> rec = new ProducerRecord<String, SearcherResult>(topic,message);
		producer.send(rec);
		producer.close();
	
	}

}
