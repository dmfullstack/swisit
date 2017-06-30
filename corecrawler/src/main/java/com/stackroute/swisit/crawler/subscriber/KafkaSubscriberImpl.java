package com.stackroute.swisit.crawler.subscriber;

/*-------Importing Libraries------*/
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.crawler.domain.SearcherResult;

@Service
public class KafkaSubscriberImpl implements Subscriber{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${broker-id}")
	String brokerid;
	public List<SearcherResult> receivingMessage(String string) {


		System.out.println("inside receiving mesage");
		// TODO Auto-generated method stub
		Properties properties = new Properties();
		/* configure properties for kafka */
		properties.put("group.id", "group-1");
		System.out.println("inside main");
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , brokerid);
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "com.stackroute.swisit.crawler.domain.SearcherResult");
		List<SearcherResult> searcherResultKafka=new ArrayList<SearcherResult>();
		/* consume message from the kafka topic */
		KafkaConsumer<String, SearcherResult> kafkaConsumer = new KafkaConsumer(properties);
		kafkaConsumer.subscribe(Arrays.asList(string));
		System.out.println(kafkaConsumer.toString());

		while (true) {
			ConsumerRecords<String, SearcherResult> records = kafkaConsumer.poll(10000);
			System.out.println("records  "+records);
			for (ConsumerRecord<String, SearcherResult> record : records) {
				System.out.println("record  "+record);
				searcherResultKafka.add(record.value());

			}
			return searcherResultKafka;
		}
	}
}
