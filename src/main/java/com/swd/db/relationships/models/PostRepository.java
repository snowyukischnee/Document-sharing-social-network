package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PostRepository extends Neo4jRepository<Post, Long> {
    @Query("match (a:Post) where a.hex_string_id = {0} return a")
    Post findByHexId(String hex_id);
}
