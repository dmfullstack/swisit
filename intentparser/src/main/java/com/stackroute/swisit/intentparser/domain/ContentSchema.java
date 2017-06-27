package com.stackroute.swisit.intentparser.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentSchema {
	@NotEmpty
    @JsonProperty("word")
	private String word;
	@NotNull
    @JsonProperty("intensity")
    private float intensity;

    public ContentSchema() { }

    public ContentSchema(String word, float intensity) {
        this.word = word;
        this.intensity = intensity;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
