package com.stackroute.swisit.publisher;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherResult;

public class publisherImpl implements Publisher {

	@Override
	public void publishmessage(String topic, SearcherResult message) throws JsonProcessingException {
		// TODO Auto-generated method stub
		Properties configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
		//configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		configProperties.put("key.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
		configProperties.put("value.serializer","com.stackroute.swisit.kafkaserialization.SwisitSerializer");
		Producer producer = new KafkaProducer(configProperties);
//		for (int i = 0; i < 100; i++) {
//		        String msg = "Message " + i;
//		        producer.send(new ProducerRecord<String, String>(topicName, msg));
//		        System.out.println("Sent:" + msg);
//		}
		//TODO: Make sure to use the ProducerRecord constructor that does not take parition Id
		//Movie m=new Movie();
		//byte b[]=m.serialize("hi", message);
		//ObjectMapper om=new ObjectMapper();
		//String s=om.writeValueAsString(message);
		System.out.println("inside publish "+message.getDescription());
		//byte[] b=message.serialize(topic, message);
		ProducerRecord<String, SearcherResult> rec = new ProducerRecord<String, SearcherResult>(topic,message);
		producer.send(rec);
		//producer.send(rec);
		producer.close();
	
	}

}
