package com.stackroute.swisit.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class ConsumerImpl implements Consumer{

	@Override
	public void listenmessage(String topic) {
		// TODO Auto-generated method stub
		Properties props = new Properties();
	    //props.put("bootstrap.servers", "172.23.239.180:9095");
		//props.put("bootstrap.servers", "localhost:9092");
	    props.put("group.id", "group-1");
	    //props.put("enable.auto.commit", "true");
	    //props.put("auto.commit.interval.ms", "1000");
	    //props.put("auto.offset.reset", "earliest");
	    //props.put("session.timeout.ms", "30000");
	    //props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		//props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
		//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

	    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
	    kafkaConsumer.subscribe(Arrays.asList(topic));
	    while (true) {
	      ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
	      for (ConsumerRecord<String, String> record : records) {
	       System.out.println("inside consumer i am getting"+record.value());
	    	  
	    	  // System.out.println("Partition: " + record.partition() + " Offset: " + record.offset()
	         //   + " Value: " + record.value() + " ThreadID: " + Thread.currentThread().getId());
	      }
	    }

	
	}

}
