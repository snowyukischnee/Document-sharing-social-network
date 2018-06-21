package com.swd.models;

import com.swd.entities.EntityImpl;
import org.bson.Document;

public interface DaoImpl<T extends EntityImpl > {
    public void init(String collection_name);
    public void Insert(T obj);
    public Document Find(T obj);
    public void Update(T obj_orig, T obj_dest);
    public void Delete(T obj);
}
