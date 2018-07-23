package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class PostViewModel {
    public String _id;
    public AccountSummViewModel posted_by;
    public Date dateCreated;
    public String title;
    public String description;
    public Date publicationDate;
    public List<AccountSummViewModel> authors;
    public List<AccountSummViewModel> liked_by;
    public List<AccountSummViewModel> followed_by;

    public PostViewModel(ObjectId _id, AccountRepository accountRepository, PostRepository postRepository) throws NullPointerException, IllegalStateException {
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
        //--------------------------------------------------------------------------------------------------------------
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(this._id);
        com.swd.db.relationships.entities.Account owner_rel = accountRepository.findOwnerByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> authors_rel = accountRepository.findAuthorsByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> liked_by_rel = accountRepository.findReactedByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> followed_by_rel = accountRepository.findFollowedByPost(post_rel);
        AccountSummViewModel owner = null;
        List<AccountSummViewModel> authors = new ArrayList<>();
        List<AccountSummViewModel> liked_by = new ArrayList<>();
        List<AccountSummViewModel> followed_by = new ArrayList<>();
        owner = new AccountSummViewModel(new ObjectId(owner_rel.getHex_string_id()));
        for (com.swd.db.relationships.entities.Account author_rel : authors_rel) authors.add(new AccountSummViewModel(new ObjectId(author_rel.getHex_string_id())));
        for (com.swd.db.relationships.entities.Account liked_by_r : liked_by_rel) liked_by.add(new AccountSummViewModel(new ObjectId(liked_by_r.getHex_string_id())));
        for (com.swd.db.relationships.entities.Account followed_by_r : followed_by_rel) followed_by.add(new AccountSummViewModel(new ObjectId(followed_by_r.getHex_string_id())));
        this.posted_by = owner;
        this.authors = authors;
        this.liked_by = liked_by;
        this.followed_by = followed_by;
    }
}
