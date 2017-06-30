package com.stackroute.swisit.searcher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.swisit.searcher.domain.SearchResponse;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;

/* saving SearcherResult using MongoRepository */
@Repository
public interface SearcherResultRepository extends MongoRepository<SearcherResult, String>{
}
