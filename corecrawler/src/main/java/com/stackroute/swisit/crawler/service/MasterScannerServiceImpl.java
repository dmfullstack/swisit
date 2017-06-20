package com.stackroute.swisit.crawler.service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.stackroute.swisit.crawler.domain.CrawlerBean;
import com.stackroute.swisit.crawler.domain.SearcherResult;

@Service
public class MasterScannerServiceImpl implements MasterScannerService{
	
	
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
	
	private StructureScannerService structureScannerService;
		
	@Autowired
	public void setStructureScannerService(StructureScannerService structureScannerService) {
		this.structureScannerService = structureScannerService;
	}
		
	
	@Override
	public String scanDocument(SearcherResult[] searcherResult) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("inside master scandocs");
		for(SearcherResult sr : searcherResult)
		{
			String link=sr.getLink();
			Document document=domCreatorService.constructDOM(link);
			
			/* Iterating terms.json for terms */
			ObjectMapper objectMapper = new ObjectMapper();
	        File file = new File("./src/main/resources/common/Terms.json");
	        List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) objectMapper.readValue(file, ArrayList.class);
	        List<String> result = new ArrayList<String>();
	        
	        for(int i=0;i<list.size();i++){
	            LinkedHashMap<String, String> hashMap = list.get(i);
	            //System.out.println(hashMap.get("name"));
	            result.add(hashMap.get("name"));
	            
	        }
			keywordScannerService.scanDocument(document, result , sr);
		}
		return "sucess";
	}



	

	

	

}
