package com.stackroute.swisit.crawler.domain;

import javax.validation.constraints.NotNull;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IntensityBean {
	
	/*-------------private variables of bean class------------*/
	
	@JsonProperty("title")
	@NotNull
	private String title;
	
	@JsonProperty("intensity")
	@NotNull
	private String intensity;
	
	/*--------------- No args constructor-------------*/
	
	public IntensityBean() {
		super();
	}

	/*-------------- Constructor with fields ------------*/
	
	public IntensityBean(String title, String intensity) {
		super();
		this.title = title;
		this.intensity = intensity;
	}

	/*------------- Getters and setters for fields -----------*/
	
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
