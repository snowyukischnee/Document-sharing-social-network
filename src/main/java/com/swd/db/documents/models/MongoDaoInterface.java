package com.swd.db.documents.models;

import com.swd.db.documents.entities.MongoEntityInterface;
import org.bson.Document;

public interface MongoDaoInterface<T extends MongoEntityInterface> {
    public void init(String collection_name);
    public void Insert(T obj);
    public Document Find(T obj);
    public void Update(T obj_orig, T obj_dest);
    public void Delete(T obj);
}
