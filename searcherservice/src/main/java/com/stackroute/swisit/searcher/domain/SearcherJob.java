package com.stackroute.swisit.searcher.domain;




import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Component
@JsonIgnoreProperties(ignoreUnknown=true)
@Document
@JsonSerialize
public class SearcherJob extends ResourceSupport{
	
	
	@JsonProperty("domain")
	private String domain;
	
	
	@JsonProperty("concept")
	private List<String> concept;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public List<String> getConcept() {
		return concept;
	}
	public void setConcept(List<String> concept) {
		this.concept = concept;
	}
	@JsonProperty("engineid")
	private List<LinkedHashMap<String,String>> engineId;
	
	@JsonProperty("results")
	private String results;
	
	@JsonProperty("sitesearch")
	private String sitesearch;
	
	public List<LinkedHashMap<String, String>> getEngineId() {
		return engineId;
	}
	public void setEngineId(List<LinkedHashMap<String, String>> engineId) {
		this.engineId = engineId;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getSitesearch() {
		return sitesearch;
	}
	public void setSitesearch(String sitesearch) {
		this.sitesearch = sitesearch;
	}
}
