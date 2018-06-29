package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.Account;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRelationshipRepository extends Neo4jRepository<Account, Long> {

    @Query("match (a:Account), (b:Account) where id(a) = {0} and id(b) = {1} create (a)-[:FRIEND_OF]->(b), (b)-[:FRIEND_OF]->(a)")
    void CreateFriendRelationship(Account account_0, Account account_1);

}
