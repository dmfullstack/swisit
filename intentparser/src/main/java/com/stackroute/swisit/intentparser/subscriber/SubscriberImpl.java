package com.stackroute.swisit.intentparser.subscriber;
/*-------Importing Libraries------*/
import com.stackroute.swisit.intentparser.domain.DocumentParserResult;
import com.stackroute.swisit.intentparser.service.IntentParseAlgo;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*-------Implementation Class for Kafka Subscriber------*/
@Service
public class SubscriberImpl implements Subscriber{
	@Autowired
	IntentParseAlgo intentParseAlgo;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public List<DocumentParserResult> receivingMessage(String string) {
        Properties props = new Properties();
        props.put("group.id", "group-1");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.23.239.165:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","com.stackroute.swisit.intentparser.serialization.CrawlerDeserializer");
        List<DocumentParserResult> final_kafka=new ArrayList<DocumentParserResult>();
        KafkaConsumer<String, DocumentParserResult> kafkaConsumer = new KafkaConsumer<String, DocumentParserResult>(props);
        kafkaConsumer.subscribe(Arrays.asList(string));
        while (true) {
            ConsumerRecords<String, DocumentParserResult> records = kafkaConsumer.poll(1000);
            for (ConsumerRecord<String, DocumentParserResult> record : records) {
                logger.debug("inside consumer i am getting"+record.value());
                final_kafka.add(record.value());
                //intentParseAlgo.calculateConfidence(intentInput);
                //final_kafka.add(record.value());
            }
            if(final_kafka!=null)
            intentParseAlgo.calculateConfidence(final_kafka);
            return final_kafka;
        }
        }
}
