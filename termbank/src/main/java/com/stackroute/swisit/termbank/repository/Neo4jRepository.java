package com.stackroute.swisit.termbank.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.stackroute.swisit.termbank.domain.Term;
import com.stackroute.swisit.termbank.domain.TermToIntentRelation;


public interface Neo4jRepository extends GraphRepository<Term>{
	
	@Query("match (i:Intent)<-[r]-(t:Term) return t.name AS TermName,i.name AS IntentName, type(r) AS Relation,r.weight As Weight")
	List<HashMap<String,String>> fetchIndicatorOf();
	
	//@Query("match (i:Intent)<-[r]-(t:Term) return t.name AS TermName,i.name AS IntentName, type(r) AS Relation,r.weight As Weight")
	//List<Words> fetchIndicatorOff();
	
	@Query("match (i:Intent)<-[r:counterIndicatorOf]-(t:Term) return t.name AS TermName,i.name AS IntentName, type(r) AS Relation,r.weight AS Weight")
	List<HashMap<String,String>> fetchCounterIndicatorOf();
}
