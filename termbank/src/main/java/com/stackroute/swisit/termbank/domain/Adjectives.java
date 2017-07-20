package com.stackroute.swisit.termbank.domain;

import java.util.Arrays;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Adjectives {
	
	@JsonProperty("syn")
	String[] syn;
	
	@JsonProperty("ant")
	String[] ant;

	public String[] getSyn() {
		return syn;
	}

	public void setSyn(String[] syn) {
		this.syn = syn;
	}

	public String[] getAnt() {
		return ant;
	}

	public void setAnt(String[] ant) {
		this.ant = ant;
	}

	@Override
	public String toString() {
		return "Adjective [syn=" + Arrays.toString(syn) + ", ant=" + Arrays.toString(ant) + "]";
	}

	
	
}
