package com.stackroute.swisit.intialproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherJob;
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
        configProperties.put("value.serializer","com.stackroute.swisit.kafkaserialization.QuerySerializer");
        Producer producer = new KafkaProducer(configProperties);
        System.out.println(message.getResults());
        System.out.println(message.getSitesearch());
       // System.out.println(message.getExactTerm());
        System.out.println(message);
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
        System.out.println("inside sedning message");
        ProducerRecord<String, SearcherJob> rec = new ProducerRecord<String, SearcherJob>(topic,message);
        System.out.println("sending........");
        producer.send(rec);
       
        producer.close();
        System.out.println("closed");
	
	}

}
