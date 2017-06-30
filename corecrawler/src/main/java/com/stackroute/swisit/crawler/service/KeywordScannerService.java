package com.stackroute.swisit.crawler.service;

/*-------Importing Libraries------*/
import java.util.*;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.SearcherResult;

/* KeywordScannerService interface that declares methods 
 * to be implemented to find the intensity for terms 
 * in neo4j and its synonyms
 * */
public interface KeywordScannerService {
	
	/* Method declaration for scanning the document 
	 * and calculating intensity
	 * arguments- link, crawler list, searcherResult bean
	 * returns- float
	 * */
	public float scanDocument(Document link, List<String> result,SearcherResult searcherReult) throws JsonProcessingException;
	
	/* Method declaration for getting terms from 
	 * wordnet if terms do not match neo4j terms 
	 * arguments- term
	 * returns- string
	 * */
	public	String[] getFromWordNet(String term);
	
	/* Method declaration to publish message into messaging service*/
	public void publishMessage() throws JsonProcessingException;

}
