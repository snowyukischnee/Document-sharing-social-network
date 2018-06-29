package com.swd.db.relationships.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Set;

@NodeEntity
public class Account {
    @Id
    @GeneratedValue
    protected Long id;

    protected String hex_string_id;

    public String getHex_string_id() {
        return hex_string_id;
    }

    public void setHex_string_id(String hex_string_id) {
        this.hex_string_id = hex_string_id;
    }

    @Relationship(type = "FRIEND_OF", direction = Relationship.OUTGOING)
    public List<Account> friends;

    @Relationship(type = "FRIEND_REQUEST", direction = Relationship.OUTGOING)
    public List<Account> outgoing_friend_requests;

    @Relationship(type = "FRIEND_REQUEST", direction = Relationship.INCOMING)
    public List<Account> incoming_friend_requests;

    @Relationship(type = "HAS_POSTED", direction = Relationship.OUTGOING)
    public List<Post> posted_posts;

    @Relationship(type = "AUTHOR_OF", direction = Relationship.OUTGOING)
    public List<Post> authored_posts;
}
