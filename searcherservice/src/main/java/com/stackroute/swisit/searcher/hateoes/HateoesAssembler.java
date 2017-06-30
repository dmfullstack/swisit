package com.stackroute.swisit.searcher.hateoes;

import java.util.List;

import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
public interface HateoesAssembler {

	public List<SearcherResult> getAllLinks(List<SearcherResult> all);
	public List<SearcherJob> getAllQuery(List<SearcherJob> all);
	public List getLinksPost();
}
