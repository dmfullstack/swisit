package com.stackroute.swisit.searcher.hateoes;

import java.util.List;

import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;


public interface HateoesAssembler {

	public List<SearcherResult> getalllinks(List<SearcherResult> all);
	public List<SearcherJob> getallquery(List<SearcherJob> all);
	public List getlinkspost();
}
