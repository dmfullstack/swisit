package com.stackroute.swisit.intentparser.domain;

import javax.validation.constraints.NotNull;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Term {
    @GraphId @NotNull
    private Long id;
    @NotNull
    private String nodeid;

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    private String name;
    public Term() {}

    public Term(String nodeid, String name) {
        this.name = name;
        this.nodeid = nodeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
