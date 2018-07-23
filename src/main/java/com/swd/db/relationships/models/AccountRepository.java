package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Account;
import com.swd.db.relationships.entities.Comment;
import com.swd.db.relationships.entities.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface AccountRepository extends Neo4jRepository<Account, Long> {

    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account) where a.hex_string_id = {0} return a")
    Account findByHexId(String hex_id);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account)-[:AUTHOR_OF]->(b:Post) where id(b) = {0} return a")
    List<Account> findAuthorsByPost(Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} merge (a)-[:AUTHOR_OF]->(b)")
    void ClaimAuthor(Account account, Post post);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account)-[:HAS_POSTED]->(b:Post) where id(b) = {0} return a")
    Account findOwnerByPost(Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} merge (a)-[:HAS_POSTED]->(b)")
    void Post(Account account, Post post);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account)-[:HAS_REACTED]->(b:Post) where id(b) = {0} return a")
    List<Account> findReactedByPost(Post post);

    @Query("match (a:Account)-[r:HAS_REACTED]->(b:Post) where id(a) = {0} and id(b) = {1} delete r")
    void DeleteReactPost(Account account, Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} merge (a)-[:HAS_REACTED]->(b)")
    void ReactPost(Account account, Post post);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account),(b:Account) where id(a) = {0} and id(b) = {1} return exists((a)-[:FRIEND_OF]-(b))")
    boolean isFriend(Account account_0, Account account_1);

    @Query("match (a:Account)-[r:FRIEND_OF]->(b:Account) where id(a) = {0} return b")
    List<Account> FindFriends(Account account);

    @Query("match (a:Account), (b:Account) where id(a) = {0} and id(b) = {1} merge (a)-[:FRIEND_OF]->(b) merge (b)-[:FRIEND_OF]->(a)")
    void CreateFriendRelationship(Account account_0, Account account_1);

    @Query("match (a:Account)-[r:FRIEND_OF]-(b:Account) where id(a) = {0} and id(b) = {1} delete r")
    void DeleteFriendRelationship(Account account_0, Account account_1);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account),(b:Account) where id(a) = {0} and id(b) = {1} return exists((a)-[:FRIEND_REQUEST]->(b))")
    boolean isFriendRequestSent(Account account_0, Account account_1);

    @Query("match (a:Account),(b:Account) where id(a) = {0} and id(b) = {1} return exists((b)-[:FRIEND_REQUEST]->(a))")
    boolean isFriendRequestReceived(Account account_0, Account account_1);

    @Query("match (a:Account)-[r:FRIEND_REQUEST]->(b:Account) where id(b) = {0} return a")
    List<Account> FindFriendRequestedBy(Account account);

    @Query("match (a:Account)-[r:FRIEND_REQUEST]->(b:Account) where id(a) = {0} return b")
    List<Account> FindFriendRequest(Account account);

    @Query("match (a:Account), (b:Account) where id(a) = {0} and id(b) = {1} merge (a)-[:FRIEND_REQUEST]->(b)")
    void SendFriendRequest(Account account_0, Account account_1);

    @Query("match (a:Account)-[r:FRIEND_REQUEST]->(b:Account) where id(a) = {0} and id(b) = {1} delete r")
    void DeleteFriendRequest(Account account_0, Account account_1);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account)-[:HAS_FOLLOWED]->(b:Post) where id(b) = {0} return a")
    List<Account> findFollowedByPost(Post post);

    @Query("match (a:Account), (b:Post) where id(a) = {0} and id(b) = {1} merge (a)-[:HAS_FOLLOWED]->(b)")
    void FollowPost(Account account, Post post);

    @Query("match (a:Account)-[r:HAS_FOLLOWED]->(b:Post) where id(a) = {0} and id(b) = {1} delete r")
    void UnfollowPost(Account account, Post post);
    //------------------------------------------------------------------------------------------------------------------
    @Query("match (a:Account), (b:Post), (c:Comment) where id(a) = {0} and id(b) = {1} and id(c) = {2} create (a)-[:HAS_POSTED]->(c), (c)-[:COMMENT_OF]->(b)")
    void CommentOn(Account account, Post post, Comment comment);

    @Query("match (a:Account)-[:HAS_POSTED]->(b:Comment) where id(b) = {0} return a")
    Account findOwnerByComment(Comment comment);
}
