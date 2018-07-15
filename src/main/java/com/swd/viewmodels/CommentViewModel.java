package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class CommentViewModel {
    public String _id;
    public Date dateCreated;
    public String content;
    public boolean enabled;

    public CommentViewModel() { }

    public CommentViewModel(ObjectId _id, Date dateCreated, String content, boolean enabled) {
        this._id = _id.toHexString();
        this.dateCreated = dateCreated;
        this.content = content;
        this.enabled = enabled;
    }

    public CommentViewModel(ObjectId _id) throws Exception {
        MongoDaoBaseClass<com.swd.db.documents.entities.Comment> accdao = new MongoDaoBaseClass<>("comment");
        Document doc = accdao.Find(new com.swd.db.documents.entities.Comment(
                _id,
                null,
                null,
                true
        ));
        if (doc == null) throw new Exception();
        this._id = doc.getObjectId("_id").toHexString();
        this.dateCreated = doc.getDate("dateCreated");
        this.content = doc.getString("content");
        this.enabled = doc.getBoolean("enabled");
    }
}
