package com.swd.models;

import com.swd.entities.Account;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.swd.config.Config;
import com.swd.entities.AccountBaseClass;
import org.bson.Document;
import org.bson.conversions.Bson;

public class AccountDao implements DaoImpl<AccountBaseClass> {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    public AccountDao() {
        init();
    }

    @Override
    public void init() {
        mongoClient = new MongoClient(Config.ConnectionString);
        db = mongoClient.getDatabase(Config.DbName);
        collection = db.getCollection("account");
    }

    @Override
    public void Insert(AccountBaseClass obj) { collection.insertOne(obj.ToDocument()); }

    @Override
    public Document Find(AccountBaseClass obj) { return collection.find(Filters.eq("_id", obj.get_id())).first(); }

    @Override
    public void Update(AccountBaseClass obj_orig, AccountBaseClass obj_dest) {
        Bson filter = Filters.eq("_id", obj_orig.get_id());
        Bson new_doc = new Document("$set", obj_dest.ToDocument());
        collection.findOneAndUpdate(filter, new_doc);
    }

    @Override
    public void Delete(AccountBaseClass obj) { collection.findOneAndDelete(Filters.eq("_id", obj.get_id())); }
}
