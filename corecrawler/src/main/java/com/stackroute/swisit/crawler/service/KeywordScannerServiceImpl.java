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
							ContentSchema c=new ContentSchema();
							c.setIntensity(count+"");
							c.setWord(text);
							contentSchema.add(c);
							logger.info("Term is: "+term.get(i)+" and Intensity is: "+count);
							k++;
						}
					}
				}
			}
			
			CrawlerResult crawlerResultRef=new CrawlerResult();
			crawlerResultRef.setQuery(searcherResult.getQuery());
			crawlerResultRef.setLink(searcherResult.getLink());
			crawlerResultRef.setTitle(searcherResult.getTitle());
			crawlerResultRef.setSnippet(searcherResult.getSnippet());
			crawlerResultRef.setTerms(contentSchema);
			crawlerResultRef.setLastindexedof(new Date());
			crawlerResultRef.setConcept(searcherResult.getConcept());
			crawlerResult.add(crawlerResultRef);
			KafkaPublisherImpl kafkaPublisherImpl = new KafkaPublisherImpl();
			kafkaPublisherImpl.publishMessage("tointent", crawlerResultRef);
	}
	catch(Exception e){
		count=0;
		e.printStackTrace();
	}
		return count;
	}

	/*-- Method implementation to publish message into kafka --*/
	@Override
	public void publishMessage() throws JsonProcessingException {
		logger.info("inside the list"+crawlerResult);
		logger.info("size is "+crawlerResult.size());
		for(CrawlerResult crawlerResultRef : crawlerResult){
			logger.info("link is "+crawlerResultRef.getLink());
			logger.info("query is "+crawlerResultRef.getQuery());
			logger.info("snippet is "+crawlerResultRef.getSnippet());
			logger.info("title is "+crawlerResultRef.getTitle());
			logger.info("terms is "+crawlerResultRef.getTerms());
			logger.info("last indexed of is "+crawlerResultRef.getLastindexedof());
			kafkaPublisherImpl.publishMessage("tointent", crawlerResultRef);
		}
		crawlerResult=null;
	}
}