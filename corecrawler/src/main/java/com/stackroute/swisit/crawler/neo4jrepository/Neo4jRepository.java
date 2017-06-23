package com.stackroute.swisit.crawler.neo4jrepository;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.stackroute.swisit.crawler.domain.Term;

public interface Neo4jRepository extends GraphRepository<Term>{
	@org.springframework.data.neo4j.annotation.Query("MATCH (n:`Term`) RETURN n")
	List<Term> findTerms();
}
