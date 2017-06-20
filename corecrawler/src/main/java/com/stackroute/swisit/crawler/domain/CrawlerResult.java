package com.stackroute.swisit.crawler.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Component
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrawlerResult {
	
	@JsonProperty("query")
	@NotNull
	private String query;
	
	@JsonProperty("link")
	@NotNull
	private String link;
	
	@JsonProperty("terms")
	@NotNull
	private ContentSchema terms;
	
	@JsonProperty("title")
	@NotNull
	private String title;
	
	@JsonProperty("snippet")
	@NotNull
	private String snippet;
	
	@JsonProperty("lastindexedof")
	private Date lastindexedof;
	
	
	
	public CrawlerResult() {
		super();
	}
	
	
	public CrawlerResult(String query, String link, ContentSchema terms, String title, String snippet,
			Date lastindexedof) {
		super();
		this.query = query;
		this.link = link;
		this.terms = terms;
		this.title = title;
		this.snippet = snippet;
		this.lastindexedof = lastindexedof;
	}


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
	public ContentSchema getTerms() {
		return terms;
	}
	public void setTerms(ContentSchema terms) {
		this.terms = terms;
	}
	
}
