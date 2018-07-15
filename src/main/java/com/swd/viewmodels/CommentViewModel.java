package com.swd.viewmodels;

import org.bson.types.ObjectId;

import java.util.Date;

public class CommentViewModel {
    public ObjectId _id;
    public Date dateCreated;
    public String content;

    public CommentViewModel() { }

    public CommentViewModel(ObjectId _id, Date dateCreated, String content) {
        this._id = _id;
        this.dateCreated = dateCreated;
        this.content = content;
    }
}
