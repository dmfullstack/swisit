package com.stackroute.swisit.crawler.controller;

import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stackroute.swisit.crawler.domain.CrawlerResult;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.loadbalancing.LoadBal;
import com.stackroute.swisit.crawler.service.KeywordScannerServiceImpl;
import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.crawler.subscriber.KafkaSubscriberImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Component
@RestController
@RequestMapping(value="/v1/api/swisit/crawler")
@Api(value="onlinestore", description="Operations pertaining to CrawlerService")
public class CrawlerRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MasterScannerService masterScannerService;

	@Autowired
	public void setMasterScannerService(MasterScannerService masterScannerService){
		this.masterScannerService=masterScannerService;
	}	

	private KafkaSubscriberImpl kafkaSubscriberImpl;

	@Autowired
	public void setSubscriber(KafkaSubscriberImpl kafkaSubscriberImpl) {
		this.kafkaSubscriberImpl = kafkaSubscriberImpl;
	}

	@Autowired
	private KeywordScannerServiceImpl keywordScannerServiceImpl;


	@Autowired
	private MessageSource messageSource;

	@Autowired
	LoadBal loadBal;

	/*method to receive the input from kafka subscriber as messages*/

	@ApiOperation(value="SearcherResult",response = SearcherResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved SearcherValue"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	}
			)
	@RequestMapping(value="/receiver", method=RequestMethod.GET)
	public ResponseEntity<Map<String,String>> receiveMessage(SearcherResult[] sr) throws JsonParseException, JsonMappingException, IOException{

		/*testcontrol is my topic name*/
		/*List<SearcherResult> list=kafkaSubscriberImpl.receivingMessage("testcontrol");
		SearcherResult searcherResult[]= new SearcherResult[list.size()];
		list.toArray(searcherResult);

		for(SearcherResult sr:list){
			logger.info(sr.getLink());
			logger.info(sr.getQuery());
			logger.info(sr.getSnippet());
			logger.info(sr.getTitle());
		}*/
		/*ObjectMapper mapper = new ObjectMapper();
		File file = new File("./src/main/resources/common/sample.json");
	    SearcherResult[] searcherResult=mapper.readValue(file, SearcherResult[].class);*/
		/*if(searcherResult.equals(null)){
			Locale locale = LocaleContextHolder.getLocale();
			String message = messageSource.getMessage ("user.excep.null", null, locale );
			return new ResponseEntity(message,HttpStatus.NOT_FOUND);  
		}*/
		masterScannerService.scanDocument(sr);
		Locale locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage ("user.success.receive", null, locale );
		return new ResponseEntity("succes",HttpStatus.OK);

	}
	
	/* method to publish result as messages to kafka for intent parser */

	@ApiOperation(value="CrawlerResult", response = CrawlerResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Publisher"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	}
			)
	@RequestMapping(value="/publisher" , method=RequestMethod.GET)
	public ResponseEntity<Map<String,String>> finalmethod() throws JsonProcessingException {
		keywordScannerServiceImpl.publishMessage();
		Locale locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage ("user.success.publish", null, locale );
		return new ResponseEntity(message,HttpStatus.OK);
	}

	/* method for loadbalancing producer */
	
	@RequestMapping(value="producer",method=RequestMethod.GET)
	public ResponseEntity producer() {
		logger.info("load balancer");
		loadBal.LoadProducer();
		return new ResponseEntity("success",HttpStatus.OK);
	}

	/* method for loadbalancing consumer */
	
	@RequestMapping(value="consumer",method=RequestMethod.GET)
	public ResponseEntity consumer() {
		logger.info("load consumer");
		loadBal.LoadConsumer();
		return new ResponseEntity("success",HttpStatus.OK);

	}
}
