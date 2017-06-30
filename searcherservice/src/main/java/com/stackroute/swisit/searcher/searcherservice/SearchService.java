package com.stackroute.swisit.searcher.searcherservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
import com.stackroute.swisit.searcher.exception.SearcherServiceException;

public interface SearchService {
	
	public Iterable<SearcherResult> saveAllSearcherResult();
	public Iterable<SearcherJob> getAllSearcherJob();
	public Iterable<SearcherJob> saveAllSearcherJob(SearcherJob sj); 
	public Iterable<SearcherResult> getAllSearcherResult();
}
