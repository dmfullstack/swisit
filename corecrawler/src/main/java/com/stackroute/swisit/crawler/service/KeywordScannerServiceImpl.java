package com.stackroute.swisit.crawler.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.controller.CrawlerRestController;
import com.stackroute.swisit.crawler.domain.ContentSchema;
import com.stackroute.swisit.crawler.domain.CrawlerResult;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.publisher.KafkaPublisherImpl;

//import scala.annotation.meta.setter;


@Service
public class KeywordScannerServiceImpl implements KeywordScannerService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${topic-toproducer}")
	String producerTopic;
	float count=0;

	List<CrawlerResult> crawlerResult = new ArrayList<CrawlerResult>(); 

	private DOMCreatorService domCreatorService;

	@Autowired
	public void setDOMCreatorService(DOMCreatorService domCreatorService) {
		this.domCreatorService=domCreatorService;

	}
	@Autowired
	private KafkaPublisherImpl publisher; 
	@Autowired
	private ContentSchema contentSchema[];	
	@Override
	public float scanDocument(Document document, List<String> term,SearcherResult searcherResult) {

		float  intensity;
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

	@Override
	public String[] getFromWordNet(String term) {
		//Call WorkNet Service
		return null;
	}



	public float calculateIntensity(Document document, List<String> term, SearcherResult searcherResult) {
		String text = null;
		try{
			count=0;
			int k=0;
			ObjectMapper mapper = new ObjectMapper();
			File file = new File("./src/main/resources/common/intensity.json");
			List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) mapper.readValue(file,ArrayList.class);
			List<String> titleList = new ArrayList<String>();
			List<String> intensityList = new ArrayList<String>();

			for(int i=0;i<list.size();i++){
				LinkedHashMap<String, String> hashMap = list.get(i);
				titleList.add(hashMap.get("title"));
				intensityList.add(hashMap.get("intensity"));
			} 

			for(int i=0;i<term.size();i++){
				for(int j=0;j<titleList.size();j++){
					//System.out.println(term.get(i)+" "+titleList.get(i)+"  "+intensityList.get(i));
					Elements tag=document.select(titleList.get(j));
					for(Element element:tag){
						text=term.get(i);
						if(element.text().matches(term.get(i))){
							count=count+Integer.parseInt(intensityList.get(j));
							System.out.println(count+"  "+term.get(i));
							contentSchema[k].setIntensity(count+"");
							contentSchema[k].setWord(term.get(i));
							k++;
						}
					}
				}
			}
			//System.out.println("hi");
			//ContentSchema contentSchema[]  = new ContentSchema()[];
			//contentSchema[0].setWord(text);
			//contentSchema[0].setIntensity(count+"");
			//System.out.println("hi "+contentSchema[0].getIntensity()+" "+contentSchema[0].getWord());
			CrawlerResult cb=new CrawlerResult();
			cb.setQuery(searcherResult.getQuery());
			cb.setLink(searcherResult.getLink());
			cb.setTitle(searcherResult.getTitle());
			cb.setSnippet(searcherResult.getSnippet());
			cb.setTerms(contentSchema);
			cb.setLastindexedof(new Date());
			//System.out.println("hi1");
			for(ContentSchema c:contentSchema){
				System.out.println(c.getIntensity()+"  "+c.getWord());
			}
			crawlerResult.add(cb);
			publisher.publishMessage(producerTopic, cb);
			//k=0;
			//System.out.println();
			//System.out.println(crawlerResult.size());
		}

		catch(Exception e){
			System.out.println("ghost "+e);
		}
		System.out.println(searcherResult.getLink());
		System.out.println("intensity is "+count);

		return count;
	}

	@Override
	public void publishingMessage() throws JsonProcessingException {
		// TODO Auto-generated method stub
		System.out.println("inside the list"+crawlerResult);
		System.out.println("size is "+crawlerResult.size());
		for(CrawlerResult cr : crawlerResult){
			System.out.println("link is "+cr.getLink());
			System.out.println("query is "+cr.getQuery());
			System.out.println("snippet is "+cr.getSnippet());
			System.out.println("title is "+cr.getTitle());
			System.out.println("terms is "+cr.getTerms());
			System.out.println("last indexed of is "+cr.getLastindexedof());
			publisher.publishMessage(producerTopic, cr);
		}
		crawlerResult=null;
	}



}