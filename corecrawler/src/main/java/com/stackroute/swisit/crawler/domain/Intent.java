package com.stackroute.swisit.crawler.domain;

import org.neo4j.ogm.annotation.*;

import java.util.*;

@NodeEntity
public class Intent {
	
	/*-------------private variables of bean class------------*/
	
    @GraphId
    private Long id;
    
    private String name;
    
    /*---------- variable that maps the relationship from neo4j-----------------*/
    
    @Relationship(type = "indicatorOf", direction = Relationship.UNDIRECTED)
    private Set<Term> terms;

    /*--------------- No args constructor-------------*/
    
    public Intent(){
    	
    }
    
    /*-------------- Constructor with fields ------------*/
    
    public Intent(String name){
        this.name=name;
    }

    /*----------method that adds the terms from neo4j into terms set-----------*/
    
    public void indicatorOf(Term term) {
        if (terms == null) {
            terms = new HashSet<>();
        }
        terms.add(term);
    }
    
    /*------------- Getters and setters for fields -----------*/
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

}