package com.stackroute.swisit.crawler.service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.stackroute.swisit.crawler.domain.CrawlerBean;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.domain.Term;
import com.stackroute.swisit.crawler.repository.Neo4jRepository;

@Service
public class MasterScannerServiceImpl implements MasterScannerService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DOMCreatorService domCreatorService;
	
	@Autowired
	public void setDomCreatorService(DOMCreatorService domCreatorService) {
		this.domCreatorService = domCreatorService;
	}
	
	private KeywordScannerService keywordScannerService;
	 
	@Autowired
	public void setKeywordScannerService(KeywordScannerService keywordScannerService) {
		this.keywordScannerService = keywordScannerService;
	}
	@Autowired
	Neo4jRepository neo4jRepository;
	private StructureScannerService structureScannerService;
		
	@Autowired
	public void setStructureScannerService(StructureScannerService structureScannerService) {
		this.structureScannerService = structureScannerService;
	}
		
	
	@Override
	public String scanDocument(SearcherResult[] searcherResult) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("inside master scandocs"+searcherResult.length);
		for(SearcherResult sr : searcherResult)
		{
			System.out.println(sr.getLink());
			//String link=sr.getLink();
			DOMCreatorServiceImpl domCreatorService = new DOMCreatorServiceImpl();
			Document document=domCreatorService.constructDOM(sr.getLink());
			//System.out.println(document);
			//MasterScannerServiceImpl ms=new MasterScannerServiceImpl();
			//if(neo4jRepository.fetchTerms()==null)
				
			//neo4j implemntation
			System.out.println("gogogogogogogog");
			List<Term> l=neo4jRepository.fetchTerms();
			List<String> result=new ArrayList<String>();
			
			for(Term t:l){
				result.add(t.getName());
			//System.out.println(t.getName());
			}
			
			/* //Iterating terms.json for terms 
			ObjectMapper objectMapper = new ObjectMapper();
	        File file = new File("./src/main/resources/common/Terms.json");
	        List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) objectMapper.readValue(file, ArrayList.class);
	        List<String> result = new ArrayList<String>();
	        
	        for(int i=0;i<list.size();i++){
	            LinkedHashMap<String, String> hashMap = list.get(i);
	            //System.out.println(hashMap.get("name"));
	            result.add(hashMap.get("name"));
	            
	        }*/
			
			KeywordScannerServiceImpl keywordScannerService=new KeywordScannerServiceImpl();
			keywordScannerService.scanDocument(document, result , sr);
		}
		return "sucess";
	}


	public List<Term> getTerms() {
		System.out.println("hi neo");
		System.out.println(neo4jRepository.fetchTerms());
	return neo4jRepository.fetchTerms();
}	

}
