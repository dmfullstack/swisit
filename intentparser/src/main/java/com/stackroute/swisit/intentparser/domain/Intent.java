package com.stackroute.swisit.intentparser.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

@NodeEntity
public class Intent {

    @GraphId @NotNull
    private Long id;
    @NotNull
    String nodeid;
    @NotEmpty
    String name;

    public Intent(){}

    public Intent(String nodeid, String name){
        this.nodeid = nodeid;
        this.name = name;
    }

    @Relationship(type = "indicatorOf", direction = Relationship.INCOMING)
    private Set<Term> indicatorTerms;

    public void indicatorOf(Term term) {
        if (indicatorTerms == null) {
            indicatorTerms = new HashSet<Term>();
        }
        indicatorTerms.add(term);
    }

    @Relationship(type = "counterIndicatorOf", direction = Relationship.INCOMING)
    private Set<Term> counterIndicatorTerms;

    public void counterIndicatorOf(Term term) {
        if (counterIndicatorTerms == null) {
            counterIndicatorTerms = new HashSet<Term>();
        }
        counterIndicatorTerms.add(term);
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
