package com.stackroute.swisit.crawler.domain;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntensityBean {
	
	@JsonProperty("title")
	@NotNull
	private String title;
	
	@JsonProperty("intensity")
	@NotNull
	private String intensity;
	

	public IntensityBean() {
		super();
	}

	
	public IntensityBean(String title, String intensity) {
		super();
		this.title = title;
		this.intensity = intensity;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}

}
