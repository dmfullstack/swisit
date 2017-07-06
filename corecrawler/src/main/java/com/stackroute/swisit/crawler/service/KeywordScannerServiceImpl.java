package com.stackroute.swisit.crawler.service;

/*-------Importing Libraries------*/
import java.io.File;

import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.ContentSchema;
import com.stackroute.swisit.crawler.domain.CrawlerResult;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.exception.TitleIntensityCalculationException;
import com.stackroute.swisit.crawler.publisher.KafkaPublisherImpl;

/* KeywordScannerServiceImpl implements KeywordScannerService to scan for keywords and 
 * generate the intensity for terms and produce the result to kafka as crawler result
 * */
@Service
public class KeywordScannerServiceImpl implements KeywordScannerService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*--- topic name for producing the result to kafka --*/
	@Value("${topic-toproducer}")
	String producerTopic;
	
	float count=0;

	List<CrawlerResult> crawlerResult = new ArrayList<CrawlerResult>(); 

	private KafkaPublisherImpl kafkaPublisherImpl; 

	@Autowired
	public void setKafkaPublisherImpl(KafkaPublisherImpl kafkaPublisherImpl) {
		this.kafkaPublisherImpl=kafkaPublisherImpl;

	}
	
//	@Autowired
//	private ContentSchema contentSchema[];	
	
	
	/* Overriding method for scanning the document 
	 * and calculating intensity
	 * arguments- link, crawler list, searcherResult bean
	 * returns- float
	 * */
	@Override
	public float scanDocument(Document document, List<String> term,SearcherResult searcherResult) throws JsonProcessingException {

		float  intensity;
		//KeywordScannerServiceImpl keywordScannerServiceImpl=new KeywordScannerServiceImpl();
		intensity= calculateIntensity(document, term, searcherResult);
		List<Float> l=new ArrayList<Float>();

		/*if(intensity == 0){
			String[] resultArrayFromWorkNet = getFromWordNet(term);
			for(String res : resultArrayFromWorkNet){
				intensity = calculateIntensity(document, res);
			}
		}*/

		return intensity;
	}

	/* Overriding method for getting terms from 
	 * wordnet if terms do not match neo4j terms 
	 * arguments- term
	 * returns- string
	 * */
	@Override
	public String[] getFromWordNet(String term) {
		//Call WorkNet Service
		return null;
	}

	/* Local method of KeywordScannerService class to calculate the 
	 * intensity of words scanned in the document retrieved from DOMCreatorService
	 * Intensity is calculated based on an algorithm by comparing the intensity fixed 
	 * for the appearance of the words in a particular tag
	 * arguments- document, term, searcher result
	 * returns- float
	 * */
	public float calculateIntensity(Document document, List<String> term, SearcherResult searcherResult) throws JsonProcessingException {
		String text = null;
		List<ContentSchema> contentSchema = new ArrayList<>();
		try{
			if(document==null || term== null || searcherResult== null){
				throw new TitleIntensityCalculationException("Intensity not calculated");

			}
			count=0;
			int k=0;
			
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("./src/main/resources/common/intensity.json");
			List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) mapper.readValue(file,ArrayList.class);
			List<String> titleList = new ArrayList<String>();
			List<String> intensityList = new ArrayList<String>();
			//logger.debug("hi this is main");
			for(int i=0;i<list.size();i++){
				LinkedHashMap<String, String> hashMap = list.get(i);
				titleList.add(hashMap.get("title"));
				intensityList.add(hashMap.get("intensity"));
			} 
			
			
			//logger.info("hi man "+contentSchema);
			// = null;
			for(int i=0;i<term.size();i++){
				for(int j=0;j<titleList.size();j++){
					Elements tag=document.select(titleList.get(j));
					for(Element element:tag){
						text=term.get(i);
						if(element.text().matches(term.get(i))){
							count=count+Integer.parseInt(intensityList.get(j));
							contentSchema[0].setIntensity("8.0");
							contentSchema[0].setWord("how to code");
							System.out.println("after setting intensity");
							logger.info("hi dude "+contentSchema[k].getIntensity());
							k++;
						}
					}
				}
			}
			
			CrawlerResult cb=new CrawlerResult();
			cb.setQuery(searcherResult.getQuery());
			cb.setLink(searcherResult.getLink());
			cb.setTitle(searcherResult.getTitle());
			cb.setSnippet(searcherResult.getSnippet());
			//ContentSchema[] contentSchema;
			cb.setTerms(contentSchema.);
			cb.setTerms(contentSchema);
			//System.out.println(contentSchema[k].getIntensity()+" "+contentSchema[k].getWord());
			cb.setLastindexedof(new Date());
//			for(ContentSchema c:contentSchema){
//				logger.info(c.getIntensity()+"  "+c.getWord());
//			}
			crawlerResult.add(cb);
			
			//KafkaPublisherImpl kafkaPublisherImpl = new KafkaPublisherImpl();
			//kafkaPublisherImpl.publishMessage("tointent", cb);
	}
	catch(Exception e){
		System.out.println("this is keyword "+e);
		count=0;
		//e.printStackTrace();
	}

		
		//logger.info(searcherResult.getLink());
		//logger.info("intensity is "+count);

		return count;
	}

	/*-- Method implementation to publish message into kafka --*/
	@Override
	public void publishMessage() throws JsonProcessingException {
		logger.info("inside the list"+crawlerResult);
		logger.info("size is "+crawlerResult.size());
		for(CrawlerResult cr : crawlerResult){
			logger.info("link is "+cr.getLink());
			logger.info("query is "+cr.getQuery());
			logger.info("snippet is "+cr.getSnippet());
			logger.info("title is "+cr.getTitle());
			logger.info("terms is "+cr.getTerms());
			logger.info("last indexed of is "+cr.getLastindexedof());
			kafkaPublisherImpl.publishMessage(producerTopic, cr);
		}
		crawlerResult=null;
	}
}