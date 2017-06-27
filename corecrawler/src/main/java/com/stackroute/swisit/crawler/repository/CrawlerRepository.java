package com.stackroute.swisit.crawler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stackroute.swisit.crawler.domain.SearcherResult;

public interface CrawlerRepository extends MongoRepository< SearcherResult,String> {

}
