package com.stackroute.swisit.intialconsumer;

import com.stackroute.swisit.domain.SearcherJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Service;
@Service
public class IntialConsumerImpl implements IntialConsumer{

	@Override
	public SearcherJob listenmessage(String topic) {
		Properties props = new Properties();
			props.put("group.id", "group-1");
		    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		    props.put("value.deserializer", "com.stackroute.swisit.kafkaserialization.QueryDeserializer");
			
			//props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
			//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
		    List<SearcherJob> l=new ArrayList<SearcherJob>();
		    KafkaConsumer<String, SearcherJob> kafkaConsumer = new KafkaConsumer<>(props);
		    kafkaConsumer.subscribe(Arrays.asList(topic));
		    SearcherJob q=new SearcherJob();
		    System.out.println("hi inside consuumer");
		    while (true) {
		      ConsumerRecords<String, SearcherJob> records = kafkaConsumer.poll(10000);
		      for (ConsumerRecord<String, SearcherJob> record : records) {
		      // System.out.println("inside consumer i am getting"+record.value());
		    	// System.out.println("in consumer"+record.key()+" "+record.value());
		    	  q.setDomain(record.value().getDomain());
		    	  q.setResults(record.value().getResults());//=record.value();
		    	  q.setConcept(record.value().getConcept());
		    	  q.setSitesearch(record.value().getSitesearch());
		    	  // System.out.println("Partition: " + record.partition() + " Offset: " + record.offset()
		         //   + " Value: " + record.value() + " ThreadID: " + Thread.currentThread().getId());
		      }
		return q;
		}

	}
	

}
