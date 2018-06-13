package com.demo.security;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MongoClient mongoClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MongoDatabase db = mongoClient.getDatabase("demodb");
        MongoCollection<Document> collection = db.getCollection("users");
        Document document = collection.find(Filters.eq("username", username)).first();
        if (document != null) {
            String u = document.getString("username");
            String p = document.getString("password");
            List<String> roles = (List<String>) document.get("roles");
            MongoUserDetails mongoUserDetails = new MongoUserDetails(u, p, roles.toArray(new String[roles.size()]));
            return mongoUserDetails;
        } else {
            throw new UsernameNotFoundException(username + " not found!");
        }
    }
}
