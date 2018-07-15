package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Comment;
import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface CommentRepository extends Neo4jRepository<Comment, Long> {
    @Query("match (a:Comment) where a.hex_string_id = {0} return a")
    Comment findByHexId(String hex_id);

    @Query("match(a:Comment)-[:COMMENT_OF]->(b:Post) where id(b) = {0} return a")
    List<Comment> findCommentsByPost(Post post);
}
