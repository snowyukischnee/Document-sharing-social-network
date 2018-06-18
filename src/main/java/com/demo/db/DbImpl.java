package com.demo.db;

import org.bson.Document;

public interface DbImpl<T> {
    public void init();
    public String GetId(T obj);
    public void Insert(T obj);
    public Document Find(T obj);
    public void Update(T obj_orig, T obj_dest);
    public void Delete(T obj);
}
