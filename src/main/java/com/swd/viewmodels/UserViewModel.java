package com.swd.viewmodels;

import org.bson.types.ObjectId;

public class UserViewModel {
    public ObjectId _id;
    public String name;

    public UserViewModel() { }

    public UserViewModel(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }
}
