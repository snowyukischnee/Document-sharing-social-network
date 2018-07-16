package com.swd.db.documents.models;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.swd.config.Config;
import com.swd.db.documents.entities.MongoEntityBaseClass;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoDaoBaseClass<T extends MongoEntityBaseClass> implements MongoDaoInterface<T>  {

    protected MongoClient mongoClient;
    protected MongoDatabase db;
    protected MongoCollection<Document> collection;

    public MongoDaoBaseClass(String collection_name) {
        init(collection_name);
    }

    @Override
    public void init(String collection_name) {
        mongoClient = new MongoClient(Config.ConnectionString);
        db = mongoClient.getDatabase(Config.DbName);
        collection = db.getCollection(collection_name);
    }

    @Override
    public void Insert(T obj) { collection.insertOne(obj.ToDocument()); }

    @Override
    public Document Find(T obj) { return collection.find(Filters.eq("_id", obj.get_id())).first(); }

    @Override
    public void Update(T obj_orig, T obj_dest) {
        Bson filter = Filters.eq("_id", obj_orig.get_id());
        Bson new_doc = new Document("$set", obj_dest.ToDocument());
        collection.findOneAndUpdate(filter, new_doc);
    }

    @Override
    public void Delete(T obj) { collection.findOneAndDelete(Filters.eq("_id", obj.get_id())); }

    @Override
    public List<Document> List(Bson filter) {
        List<Document> arr = null;
        FindIterable<Document> docs = (filter != null) ? collection.find(filter) : collection.find();
        arr = docs.into(new ArrayList<>());
        return arr;
    }

    public List<Document> List_custom() {
        return null;
    }

    public Document Search_custom(ObjectId _id) {
        return null;
    }
}
