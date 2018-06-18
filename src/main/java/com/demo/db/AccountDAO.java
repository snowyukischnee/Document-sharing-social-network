package com.demo.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;

public class AccountDAO implements DbImpl<Account> {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    public AccountDAO() { init(); }

    @Override
    public void init() {
        mongoClient = new MongoClient("127.0.0.1", 27017);
        db = mongoClient.getDatabase("demodb");
        collection = db.getCollection("users");
    }

    @Override
    public String GetId(Account acc) { return Find(acc).getObjectId("_id").toString(); }

    @Override
    public void Insert(Account acc) {
        collection.insertOne(acc.ToDocument());
    }

    @Override
    public Document Find(Account acc) {
        return collection.find(Filters.eq("username", acc.getUsername())).first();
    }

    @Override
    public void Update(Account acc_orig, Account acc_dest) {
        Bson filter = Filters.eq("username", acc_orig.getUsername());
        Bson new_doc = new Document("$set", acc_dest.ToDocument());
        collection.findOneAndUpdate(filter, new_doc);
    }
    @Override
    public void Delete(Account acc) { collection.findOneAndDelete(Filters.eq("username", acc.getUsername())); }
}
