package com.stackroute.swisit.intentparser.neo4jresult;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.neo4j.ogm.annotation.*;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class CounterIndicatorOf {
	@NotEmpty
    @Property(name="t.name")
    String termName;
	@NotEmpty
    @Property(name="i.name")
    String intentName;
	@NotEmpty
    @Property(name="type(r)")
    String relationshipType;
	@NotNull
    @Property(name="r.weight")
    String weight;

    public CounterIndicatorOf() { }

    public CounterIndicatorOf(String termName, String intentName, String relationshipType, String weight) {
        this.termName = termName;
        this.intentName = intentName;
        this.relationshipType = relationshipType;
        this.weight = weight;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
