package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class PostViewModel {
    public String _id;
    public AccountViewModel posted_by;
    public Date dateCreated;
    public String title;
    public String description;
    public Date publicationDate;
    public List<AccountViewModel> authors;
    public List<AccountViewModel> liked_by;
    public List<AccountViewModel> followed_by;

    public PostViewModel() {
    }

    public PostViewModel(ObjectId _id) throws NullPointerException, IllegalStateException {
        MongoDaoBaseClass<com.swd.db.documents.entities.Post> postdao = new MongoDaoBaseClass<>("post");
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
