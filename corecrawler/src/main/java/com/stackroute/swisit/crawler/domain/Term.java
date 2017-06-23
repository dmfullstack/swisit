package com.stackroute.swisit.crawler.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Term {

	@GraphId 
	private Long id;
	private String name;
	public Term() {

	}


	public Term(String name) {
		this.name = name;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
