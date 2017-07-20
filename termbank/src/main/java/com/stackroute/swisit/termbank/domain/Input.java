package com.stackroute.swisit.termbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Input {
	@JsonProperty("synonyms")
	String[] synonyms;
	
	@JsonProperty("antonyms")
	String[] antonyms;
	
	@JsonProperty("synweight")
	String[] synweight;
	
	@JsonProperty("antweight")
	String[] antweight;
	
	@JsonProperty("intent")
	String[] intent;

	@JsonProperty("relation")
	String[] relation;
	
	public String[] getIntent() {
		return intent;
	}

	public void setIntent(String[] intent) {
		this.intent = intent;
	}

	public String[] getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String[] synonyms) {
		this.synonyms = synonyms;
	}

	public String[] getAntonyms() {
		return antonyms;
	}

	public void setAntonyms(String[] antonyms) {
		this.antonyms = antonyms;
	}

	public String[] getSynweight() {
		return synweight;
	}

	public String[] getRelation() {
		return relation;
	}

	public void setRelation(String[] relation) {
		this.relation = relation;
	}

	public void setSynweight(String[] synweight) {
		this.synweight = synweight;
	}

	public String[] getAntweight() {
		return antweight;
	}

	public void setAntweight(String[] antweight) {
		this.antweight = antweight;
	}
	
}
