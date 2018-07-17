package com.swd.viewmodels;

import com.swd.db.documents.entities.Post;
import com.swd.db.documents.models.MongoDaoBaseClass;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class PostSummViewModel {
    public String _id;
    public Date dateCreated;
    public String title;
    public String description;
    public Date publicationDate;

    public PostSummViewModel(ObjectId _id) throws IllegalStateException, NullPointerException {
        MongoDaoBaseClass<Post> postdao = new MongoDaoBaseClass<>("post");
        Document doc = postdao.Find(new com.swd.db.documents.entities.Post(
                _id,
                null,
                null,
                null,
                null,
                true
        ));
        if (doc == null) throw new NullPointerException();
        if (doc.getBoolean("enabled") == false) throw new IllegalStateException();
        this._id = doc.getObjectId("_id").toHexString();
        this.title = doc.getString("title");
        this.description = doc.getString("description");
        this.publicationDate = doc.getDate("publicationDate");
        this.dateCreated = doc.getDate("dateCreated");
    }
}
