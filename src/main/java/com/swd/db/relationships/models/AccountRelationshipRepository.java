package com.swd.db.relationships.models;

import com.swd.db.relationships.entities.AccountRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRelationshipRepository extends Neo4jRepository<AccountRelationship, Long> {

    @Query("match (a:AccountRelationship)-[r:FRIEND_OF]->(b:AccountRelationship) where a.hex_string_id = {0} return b")
    List<AccountRelationship> GetFriendsByHexStringId(String hex_string_id);

    @Query("match (a:AccountRelationship), (b:AccountRelationship) where a.hex_string_id = {0} and b.hex_string_id = {1} create (a)-[:FRIEND_OF]->(b), (b)-[:FRIEND_OF]->(a)")
    void CreateFriendRelationship(String hex_string_id_source, String hex_string_id_target);

}
