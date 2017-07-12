package com.stackroute.swisit.documentparser.threadconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

//import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.documentparser.service.MasterParserService;
@Service
@PropertySource("classpath:application.yml")
public class KafkaConsumer {

	@Autowired
	private Environment environment;
	@Autowired
	MasterParserService masterScannerService;

	public void consumeMessage(){
		String topic = environment.getProperty("topic-fromconsumer");
		String brokerid = environment.getProperty("brokerid");
		KafkaConsumerThread consumerRunnable = new KafkaConsumerThread(brokerid,topic,masterScannerService);
		consumerRunnable.start();
		consumerRunnable.getKafkaConsumer().wakeup();
	}
}
