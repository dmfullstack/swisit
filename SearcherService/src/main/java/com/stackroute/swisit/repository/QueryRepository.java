package com.stackroute.swisit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stackroute.swisit.domain.SearcherJob;
import com.stackroute.swisit.domain.SearcherResult;

public interface QueryRepository extends MongoRepository<SearcherJob, String>{

}
