package com.stackroute.swisit.hateoes;

import java.util.List;

import com.stackroute.swisit.domain.SearcherJob;
import com.stackroute.swisit.domain.SearcherResult;


public interface HateoesAssembler {

	public List<SearcherResult> getalllinks(List<SearcherResult> all);
	public List<SearcherJob> getallquery(List<SearcherJob> all);
}
