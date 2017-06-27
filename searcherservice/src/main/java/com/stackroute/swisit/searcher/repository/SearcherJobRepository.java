package com.stackroute.swisit.searcher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;

public interface SearcherJobRepository extends MongoRepository<SearcherJob, String>{

}
