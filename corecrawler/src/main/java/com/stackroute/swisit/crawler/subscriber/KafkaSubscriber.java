package com.stackroute.swisit.crawler.subscriber;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.crawler.domain.SearcherResult;

@Service
public class KafkaSubscriber implements Subscriber{
public List<SearcherResult> receivingMessage(String string) {
    System.out.println("inside receiving mesage");
    // TODO Auto-generated method stub
    Properties properties = new Properties();
    //props.put("bootstrap.servers", "172.23.239.180:9095");
    //props.put("bootstrap.servers", "localhost:9092");
    properties.put("group.id", "group-1");
    
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.put("value.deserializer", "com.stackroute.swisit.crawler.domain.SwisitBean");
    
    List<SearcherResult> searcherResultKafka=new ArrayList<SearcherResult>();
    
    //props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
    //props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
    //SwisitBean[] cb =new Array
    
    KafkaConsumer<String, SearcherResult> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Arrays.asList(string));
    
    
    while (true) {
      ConsumerRecords<String, SearcherResult> records = kafkaConsumer.poll(100000);
      for (ConsumerRecord<String, SearcherResult> record : records) {
    	  searcherResultKafka.add(record.value());
      
      }
      
      return searcherResultKafka;
 }
}
}
