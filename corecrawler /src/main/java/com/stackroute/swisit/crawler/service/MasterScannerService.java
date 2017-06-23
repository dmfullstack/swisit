package com.stackroute.swisit.crawler.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stackroute.swisit.crawler.domain.SearcherResult;


public interface MasterScannerService {

	public String scanDocument(SearcherResult[] cb1) throws JsonParseException, JsonMappingException, IOException;
}
