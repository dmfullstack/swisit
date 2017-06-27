package com.stackroute.swisit.intentparser.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="counterIndicatorOf")
public class CounterIndicatorOf {
    @GraphId @NotNull
    Long id;
    @StartNode @NotEmpty
    Term term;
    @EndNode @NotEmpty
    Intent intent;
    @NotNull
    float weight;

    public CounterIndicatorOf() { }

    public CounterIndicatorOf(Term term, Intent intent, float weight) {
        this.term = term;
        this.intent = intent;
        this.weight = weight;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = Float.parseFloat(weight);
    }
}
