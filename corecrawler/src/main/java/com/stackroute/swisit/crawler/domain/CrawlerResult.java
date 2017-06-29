package com.stackroute.swisit.crawler.domain;

/*---------------Importing Libraries--------------*/
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*--------Crawler Result Domain Class which is output to Intent Parser Service--------*/
@Component
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrawlerResult extends ResourceSupport {

	/*-------------Private variables of domain class------------*/

	@JsonProperty("query")
	@NotNull
	private String query;

	@JsonProperty("link")
	@NotNull
	private String link;

	@JsonProperty("terms")
	@NotNull
	private List<ContentSchema> terms;

	@JsonProperty("title")
	@NotNull
	private String title;

	@JsonProperty("snippet")
	@NotNull
	private String snippet;

	public List<ContentSchema> getTerms() {
		return terms;
	}

	public void setTerms(List<ContentSchema> terms) {
		this.terms = terms;
	}

	@JsonProperty("lastindexedof")
	private Date lastindexedof;

	/*-----------Default Constructor of Crawler Result Class------------*/
	public CrawlerResult() {
		super();
	}

	/*----------Parameterized Constructor of Crawler Result Class---------*/
	public CrawlerResult(String query, String link, List<ContentSchema> terms, String title, String snippet,
			Date lastindexedof) {
		super();
		this.query = query;
		this.link = link;
		this.terms = terms;
		this.title = title;
		this.snippet = snippet;
		this.lastindexedof = lastindexedof;
	}

	/*------------- Getters and setters for fields -----------*/

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLastindexedof() {
		return lastindexedof;
	}
	
	public void setLastindexedof(Date lastindexedof) {
		this.lastindexedof = lastindexedof;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getSnippet() {
		return snippet;
	}
	
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	
	
	

}
