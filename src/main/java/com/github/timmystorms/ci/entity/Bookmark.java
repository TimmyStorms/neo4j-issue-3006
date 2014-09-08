package com.github.timmystorms.ci.entity;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "BOOKMARKED")
public class Bookmark {

    @GraphId
    private Long id;

    private Long date;
    
    @StartNode
    private InternalPerson user;

    @EndNode
    private Collaboration collaboration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public InternalPerson getUser() {
        return user;
    }

    public void setUser(InternalPerson user) {
        this.user = user;
    }

    public Collaboration getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(Collaboration collaboration) {
        this.collaboration = collaboration;
    }
    
}
