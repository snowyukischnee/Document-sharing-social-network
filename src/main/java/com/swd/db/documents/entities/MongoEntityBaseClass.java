package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoEntityBaseClass implements MongoEntityInterface {
    protected ObjectId _id;

    public MongoEntityBaseClass(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId get_id() { return _id; }

    public void set_id(ObjectId _id) { this._id = _id; }

    @Override
    public Document ToDocument() {
        return null;
    }
}
