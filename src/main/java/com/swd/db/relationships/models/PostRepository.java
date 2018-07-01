package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PostRepository extends Neo4jRepository<Post, Long> {

}
