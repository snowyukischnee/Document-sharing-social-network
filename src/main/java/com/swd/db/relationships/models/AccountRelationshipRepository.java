package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.AccountRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRelationshipRepository extends Neo4jRepository<AccountRelationship, Long> {

    @Query("match (a:account)-[r:FRIEND_OF]->(b:account) return b")
    List<AccountRelationship> GetFriends(@Param("acc") AccountRelationship account);


}
