package com.swd.db.relationships.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class AccountRelationship {
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
    Set<AccountRelationship> friends;

    @Relationship(type = "HAS_POSTED", direction = Relationship.OUTGOING)
    Set<PostRelationship> posted_posts;

    @Relationship(type = "AUTHOR_OF", direction = Relationship.OUTGOING)
    Set<PostRelationship> authored_posts;
}
