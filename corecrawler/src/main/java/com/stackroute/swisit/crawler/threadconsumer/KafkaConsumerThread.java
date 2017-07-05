package com.stackroute.swisit.crawler.threadconsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.service.MasterScannerService;

public class KafkaConsumerThread extends Thread {
	private String topicName;
	private String groupId;
	private KafkaConsumer<String, SearcherResult> kafkaConsumer;
	private MasterScannerService masterScannerService;
	private Environment environment;
	//@Autowired
	//MasterScannerService masterScannerService;
	//----------------------------------logger implementation----------------------------------
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public KafkaConsumerThread(String topicName, MasterScannerService masterScannerService ){
		this.topicName = topicName;
		//this.groupId = groupId;
		this.masterScannerService = masterScannerService;
		//this.circleService = circleService;
		//this.environment = environment;
	}

	public void run() {
		Properties configProperties = new Properties();
		configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
		configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.stackroute.swisit.crawler.domain.SearcherResult");
		configProperties.put("group.id", "group-1");
		//configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
		//configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

		//Figure out where to start processing messages from
		kafkaConsumer = new KafkaConsumer<String, SearcherResult>(configProperties);
		kafkaConsumer.subscribe(Arrays.asList(topicName));

		//Start processing messages
		while (true) {
			ConsumerRecords<String, SearcherResult> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, SearcherResult> record : records) {
				//JsonNode node = objectMapper.readTree(record.value());
				//logger.debug(record.value());
				SearcherResult searcherResult = new SearcherResult();
				searcherResult = record.value();
				//System.out.println("hi i am getting "+searcherResult.getConcept());
				try {
					masterScannerService.scanDocument(searcherResult);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(record.value());
				//circleService.getActivityType(node); 
			}
		}

	}
	public KafkaConsumer<String,SearcherResult> getKafkaConsumer()
	{
		return this.kafkaConsumer;
	}
}
