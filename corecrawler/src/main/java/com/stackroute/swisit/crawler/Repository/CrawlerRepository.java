package com.stackroute.swisit.crawler.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

//import com.stackroute.swisit.crawler.domain.CrawlerBean;
import com.stackroute.swisit.crawler.domain.SearcherResult;

public interface CrawlerRepository extends MongoRepository< SearcherResult,String> {

}
