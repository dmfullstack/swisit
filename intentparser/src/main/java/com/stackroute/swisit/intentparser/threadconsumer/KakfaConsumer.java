package com.stackroute.swisit.intentparser.threadconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.intentparser.service.IntentParseAlgo;
@Service
public class KakfaConsumer {

	@Autowired
	IntentParseAlgo intentParseAlgo;

	public void consumeMessage(String topic) {
		KafkaConsumerThread consumerRunnable = new KafkaConsumerThread(topic, intentParseAlgo);
		consumerRunnable.start();
		consumerRunnable.getKafkaConsumer().wakeup();
	}
}
