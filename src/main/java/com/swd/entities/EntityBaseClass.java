package com.swd.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

public class EntityBaseClass implements EntityImpl {
    protected ObjectId _id;

    public EntityBaseClass(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId get_id() { return _id; }

    public void set_id(ObjectId _id) { this._id = _id; }

    @Override
    public Document ToDocument() {
        return null;
    }
}
