package com.stackroute.swisit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.swisit.domain.SearcherJob;
import com.stackroute.swisit.domain.ResponsiveBean;
import com.stackroute.swisit.domain.SearcherResult;


@Repository
public interface SearchRepository extends MongoRepository<SearcherResult, String>{
}
