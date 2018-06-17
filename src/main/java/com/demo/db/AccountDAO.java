package com.demo.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

public class AccountDAO {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    public AccountDAO() {
        mongoClient = new MongoClient("127.0.0.1", 27017);
        db = mongoClient.getDatabase("demodb");
        collection = db.getCollection("users");
    }

    public void Insert(Account acc) {
        collection.insertOne(acc.ToDocument());
    }

    public Document Find(Account acc) {
        return collection.find(Filters.eq("username", acc.getUsername())).first();
    }

    public void Update(Account acc_orig, Account acc_dest) {
        Bson filter = Filters.eq("username", acc_orig.getUsername());
        Bson new_doc = new Document("$set", acc_dest.ToDocument());
        collection.findOneAndUpdate(filter, new_doc);
    }

    public void Delete(Account acc) {
        Bson filter = Filters.eq("username", acc.getUsername());
        collection.findOneAndDelete(filter);
    }
}
