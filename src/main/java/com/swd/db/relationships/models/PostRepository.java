package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Account;
import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface PostRepository extends Neo4jRepository<Post, Long> {
    @Query("match (a:Post) where a.hex_string_id = {0} return a")
    Post findByHexId(String hex_id);

    @Query("match(a:Account)-[:AUTHOR_OF]->(b:Post) where id(a) = {0} return b")
    List<Post> findPostsByAuthor(Account account);

    @Query("match(a:Account)-[:HAS_POSTED]->(b:Post) where id(a) = {0} return b")
    List<Post> findPostsByOwner(Account account);

    @Query("match(a:Account)-[:HAS_REACTED]->(b:Post) where id(a) = {0} return b")
    List<Post> findPostsByReacted(Account account);

    @Query("match(a:Account)-[:HAS_FOLLOWED]->(b:Post) where id(a) = {0} return b")
    List<Post> findPostsByFollowed(Account account);

    @Query("match(a:Account), (b:Post) where id(a) = {0} and id(b) = {1} return exists((a)-[:HAS_REACTED]->(b))")
    boolean isReacted(Account account, Post post);

    @Query("match(a:Account), (b:Post) where id(a) = {0} and id(b) = {1} return exists((a)-[:HAS_FOLLOWED]->(b))")
    boolean isFolowed(Account account, Post post);

    @Query("match (a:Account)-[r:HAS_FOLLOWED]->(b:Post) return b, count(distinct r) as num order by num desc skip {0} limit {1}")
    List<Post> getMostFollowedPostsWBound(Integer lower_bound, Integer no_items);

    @Query("match (a:Account)-[r:HAS_FOLLOWED]->(b:Post) return b, count(distinct r) as num order by num desc")
    List<Post> getMostFollowedPosts();
}
