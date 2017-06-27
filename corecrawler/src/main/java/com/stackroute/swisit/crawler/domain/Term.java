package com.stackroute.swisit.crawler.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Term {

	/*-------------private variables of bean class------------*/
	
	@GraphId 
	private Long id;
	
	private String name;
	
	/*--------------- No args constructor-------------*/
	
	public Term() {
		super();
	}

	/*-------------- Constructor with fields ------------*/

	public Term(String name) {
		this.name = name;
	}

	/*------------- Getters and setters for fields -----------*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}