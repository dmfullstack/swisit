package com.stackroute.swisit.intentparser.threadconsumer;
//package com.stackroute.swisit..threadconsumer;

import java.io.IOException;
import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.stackroute.swisit.crawler.domain.SearcherResult;
//import com.stackroute.swisit.crawler.domain.SearcherResult;
//import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.intentparser.domain.DocumentParserResult;
import com.stackroute.swisit.intentparser.service.IntentParseAlgo;

public class KafkaConsumerThread extends Thread {
	private String topicName;
	private String groupId;
	private KafkaConsumer<String, DocumentParserResult> kafkaConsumer;
	private IntentParseAlgo intentParseAlgo;
	private Environment environment;
	//@Autowired
	//MasterScannerService masterScannerService;
	//----------------------------------logger implementation----------------------------------
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public KafkaConsumerThread(String topicName, IntentParseAlgo intentParseAlgo ){
		this.topicName = topicName;
		//this.groupId = groupId;
		this.intentParseAlgo = intentParseAlgo;
		//this.circleService = circleService;
		//this.environment = environment;
	}

	public void run() {
		Properties configProperties = new Properties();
		configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
		configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.stackroute.swisit.intentparser.serialization.CrawlerDeserializer");
		configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
		//configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");
		//List<SearcherResult> searcherResultKafka=new ArrayList<SearcherResult>();
		Set<DocumentParserResult> final_kafka=new HashSet<>();
		//Figure out where to start processing messages from
		kafkaConsumer = new KafkaConsumer<String, DocumentParserResult>(configProperties);
		kafkaConsumer.subscribe(Arrays.asList(topicName));

		//Start processing messages
		while (true) {
			ConsumerRecords<String, DocumentParserResult> records = kafkaConsumer.poll(1000);
			for (ConsumerRecord<String, DocumentParserResult> record : records) {
				//JsonNode node = objectMapper.readTree(record.value());
				//logger.debug(record.value());
				DocumentParserResult crawlerResult = new DocumentParserResult();
				crawlerResult = record.value();
				System.out.println("hi i am getting "+crawlerResult.getConcept());
				try {
					final_kafka.add(record.value());
					//implenment here
					//intentParseAlgo.calculateConfidenceScore(intentParserInput, intentList)
					//(searcherResult);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//final_kafka.clear();
				System.out.println(record.value());
				//circleService.getActivityType(node); 
			}intentParseAlgo.calculateConfidence(final_kafka);
			
		}

	}
	public KafkaConsumer<String,DocumentParserResult> getKafkaConsumer()
	{
		return this.kafkaConsumer;
	}
}
