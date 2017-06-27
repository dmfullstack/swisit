package com.stackroute.swisit.searcher.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerImpl implements Consumer{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void listenmessage(String topic) {
		// TODO Auto-generated method stub
		Properties props = new Properties();
	    props.put("group.id", "group-1");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
	    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
	    kafkaConsumer.subscribe(Arrays.asList(topic));
	    while (true) {
	      ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
	      for (ConsumerRecord<String, String> record : records) {
	    	  logger.debug("inside consumer i am getting"+record.value());
	    	  }
	    }

	
	}

}
