package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class CommentViewModel {
    public String _id;
    public Date dateCreated;
    public String content;

    public CommentViewModel() { }

    public CommentViewModel(ObjectId _id) throws NullPointerException, IllegalStateException {
        MongoDaoBaseClass<com.swd.db.documents.entities.Comment> commentdao = new MongoDaoBaseClass<>("comment");
        Document doc = commentdao.Find(new com.swd.db.documents.entities.Comment(
                _id,
                null,
                null,
                true
        ));
        if (doc == null) throw new NullPointerException();
        if (doc.getBoolean("enabled") == false) throw new IllegalStateException();
        this._id = doc.getObjectId("_id").toHexString();
        this.dateCreated = doc.getDate("dateCreated");
        this.content = doc.getString("content");
    }
}
