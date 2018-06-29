package com.swd.db.relationships.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class PostRelationship {
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

    @Relationship(type = "HAS_POSTED", direction = Relationship.INCOMING)
    Set<AccountRelationship> posted_by;

    @Relationship(type = "AUTHOR_OF", direction = Relationship.INCOMING)
    Set<AccountRelationship> authors_list;
}
