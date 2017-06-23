package com.stackroute.swisit.crawler.domain;


import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.hateoas.ResourceSupport;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SearcherResult extends ResourceSupport implements Deserializer<SearcherResult> {
	
    @JsonProperty("query")
    @NotNull
	private String query;
    
    @JsonProperty("link")
    @Pattern(regexp="(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")
	private String link;
    
    
	@JsonProperty("title")
	@NotNull
	private String title;
    
	@NotNull
    @JsonProperty("snippet")
	private String snippet;
	
	
    
	
	public SearcherResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearcherResult(String query, String link, String title, String snippet) {
		super();
		this.query = query;
		this.link = link;
		this.title = title;
		this.snippet = snippet;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearcherResult deserialize(String arg0, byte[] arg1) {
		//return (CrawlerBean) SerializationUtils.deserialize(arg1);
		ObjectMapper o=new ObjectMapper();
		SearcherResult c=null;
		try{
			System.out.println(arg1.toString());
			c=o.readValue(arg1,SearcherResult.class);
			}
		catch(Exception e){
			System.out.println("hi this "+e);
		}
		return c;
	}

	
	
	
}
