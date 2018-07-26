package com.swd.viewmodels;

import com.swd.db.documents.entities.Post;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.PostRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class PostSummViewModel {
    public String _id;
    public AccountSummViewModel posted_by;
    public Date dateCreated;
    public String title;
    public String description;
    public Date publicationDate;
    public Integer likes;
    public Integer followers;

    public PostSummViewModel(ObjectId _id, AccountRepository accountRepository, PostRepository postRepository) throws IllegalStateException, NullPointerException {
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
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(this._id);
        com.swd.db.relationships.entities.Account owner_rel = accountRepository.findOwnerByPost(post_rel);
        AccountSummViewModel owner = new AccountSummViewModel(new ObjectId(owner_rel.getHex_string_id()));
        this.posted_by = owner;
        this.likes = accountRepository.findNumberOfReactedByPost(post_rel);
        this.followers = accountRepository.findNumberOfFollowedByPost(post_rel);
        postdao.close();
    }
}
