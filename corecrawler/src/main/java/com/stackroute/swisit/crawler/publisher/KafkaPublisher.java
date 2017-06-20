package com.stackroute.swisit.crawler.publisher;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.crawler.domain.CrawlerResult;
@Service
public class KafkaPublisher implements Publisher {
	public void publishingMessage(String topicName,CrawlerResult message) throws JsonProcessingException{

        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        //configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put("value.serializer","com.stackroute.swisit.crawler.serialization.CrawlerSerializer");
        Producer producer = new KafkaProducer(configProperties);
        /*
        
        for (int i = 0; i < 100; i++) {
	        String msg = "Message " + i;
	        producer.send(new ProducerRecord<String, String>(topicName, msg));
	        System.out.println("Sent:" + msg);
        }
        TODO: Make sure to use the ProducerRecord constructor that does not take parition Id
        Movie m=new Movie();
        byte b[]=m.serialize("hi", message);
        ObjectMapper om=new ObjectMapper();
        String s=om.writeValueAsString(message);
        
        */      
        ProducerRecord<String, CrawlerResult> rec = new ProducerRecord<String, CrawlerResult>(topicName,message);
        System.out.println("sending........");
        producer.send(rec);
       
        producer.close();
        System.out.println("closed");
	}

}
