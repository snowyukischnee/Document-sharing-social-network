package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Account;
import com.swd.db.relationships.entities.Comment;
import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AccountRepository extends Neo4jRepository<Account, Long> {

    @Query("match (a:Account) where a.hex_string_id = {0} return a")
    Account findByHexId(String hex_id);

    @Query("match (a:Account), (b:Account) where id(a) = {0} and id(b) = {1} create (a)-[:FRIEND_OF]->(b), (b)-[:FRIEND_OF]->(a)")
    void CreateFriendRelationship(Account account_0, Account account_1);

    @Query("match (a:Account), (b:Account) where id(a) = {0} and id(b) = {1} create (a)-[:FRIEND_REQUEST]->(b)")
    void SendFriendRequest(Account account_0, Account account_1);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} create (a)-[:HAS_POSTED]->(b)")
    void Post(Account account, Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} create (a)-[:AUTHOR_OF]->(b)")
    void ClaimAuthor(Account account, Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} create (a)-[:HAS_FOLLOWED]->(b)")
    void FollowPost(Account account, Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} create (a)-[:HAS_REACTED{type:{2}}]->(b)")
    void ReactPost(Account account, Post post, Integer type);

    @Query("match (a:Account), (b:Post), (c:Comment) where id(a) = {0} and id(b) = {1} and id(c) = {2} create (a)-[:HAS_POSTED]->(c), (c)-[:COMMENT_OF]->(b)")
    void CommentOn(Account account, Post post, Comment comment);
}
