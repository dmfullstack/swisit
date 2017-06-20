package com.stackroute.swisit.crawler.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.stackroute.swisit.crawler.publisher.KafkaPublisher;


@Service
public class KeywordScannerServiceImpl implements KeywordScannerService{

	float count=0;
	
	List<CrawlerResult> crawlerResult = new ArrayList<CrawlerResult>(); 
	
	private DOMCreatorService domCreatorService;
	
	@Autowired
	public void setDOMCreatorService(DOMCreatorService domCreatorService) {
		this.domCreatorService=domCreatorService;

	}
	@Autowired
	private KafkaPublisher publisher; 

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
	        			}
	        		}
	        	}
	        }
	        
		        ContentSchema contentSchema  = new ContentSchema();
	        	contentSchema.setWord(text);
	        	
	        	contentSchema.setIntensity(count+"");
	        	CrawlerResult cb=new CrawlerResult();
	        	cb.setQuery(searcherResult.getQuery());
	        	cb.setLink(searcherResult.getLink());
	        	cb.setTitle(searcherResult.getTitle());
	        	cb.setSnippet(searcherResult.getSnippet());
	        	cb.setTerms(contentSchema);
	        	cb.setLastindexedof(new Date());
	        	crawlerResult.add(cb);
	        	System.out.println();
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
			publisher.publishingMessage("tointent", cr);
		}
		crawlerResult=null;
	}
	
	
		
}
