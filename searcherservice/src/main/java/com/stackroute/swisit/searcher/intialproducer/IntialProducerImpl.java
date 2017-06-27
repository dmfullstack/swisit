package com.stackroute.swisit.searcher.intialproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SearcherJob;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
@Service
public class IntialProducerImpl implements IntialProducer{

	@Override
	public void publishmessage(String topic, SearcherJob message) throws JsonProcessingException {
		// TODO Auto-generated method stub
		Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "172.23.239.165:9092");
        //configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put("value.serializer","com.stackroute.swisit.searcher.kafkaserialization.QuerySerializer");
        Producer producer = new KafkaProducer(configProperties);
        ProducerRecord<String, SearcherJob> rec = new ProducerRecord<String, SearcherJob>(topic,message);
        System.out.println("sending........");
        producer.send(rec);
        producer.close();
	
	}

}
