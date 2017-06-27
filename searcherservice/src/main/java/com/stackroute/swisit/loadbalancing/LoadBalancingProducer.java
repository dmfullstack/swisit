package com.stackroute.swisit.loadbalancing;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class LoadBalancingProducer implements LoadBal{

	@Override
    public void LoadProducer() {
        
         Properties configProperties = new Properties();
            configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "172.23.239.165:9092");
            //configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
            configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
            configProperties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            Producer producer = new KafkaProducer(configProperties);
            
            
            for (int i = 0; i < 100; i++) {
                String msg = "Message " + i;
                producer.send(new ProducerRecord<String, String>("producer", msg));
                System.out.println("Sent:" + msg);
            }
    }

    @Override
    public void LoadConsumer() {
        // TODO Auto-generated method stub
        Properties properties = new Properties();
        properties.put("group.id", "group-1");
        
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
       // List<SearcherResult> searcherResultKafka=new ArrayList<SearcherResult>();
        
        //props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        //props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //SwisitBean[] cb =new Array
        
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("kafkaproducer"));
        
        
        while (true) {
          ConsumerRecords<String, String> records = kafkaConsumer.poll(100000);
          for (ConsumerRecord<String, String> record : records) {
              //searcherResultKafka.add(record.value());
              System.out.println(record.value());
          
          }
          
        }
    }
}
