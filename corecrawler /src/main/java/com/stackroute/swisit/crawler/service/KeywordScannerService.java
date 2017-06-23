package com.stackroute.swisit.crawler.service;

import java.util.*;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.crawler.domain.SearcherResult;

public interface KeywordScannerService {
	
	public float scanDocument(Document link, List<String> result,SearcherResult swisitbean);
	
	public	String[] getFromWordNet(String term);
	
	public void publishingMessage() throws JsonProcessingException;

}
