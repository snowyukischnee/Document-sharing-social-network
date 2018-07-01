package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Comment;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CommentRepository extends Neo4jRepository<Comment, Long> {
}
