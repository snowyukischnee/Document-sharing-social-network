package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class CommentViewModel {
    public String _id;
    public AccountSummViewModel posted_by;
    public Date dateCreated;
    public String content;

    public CommentViewModel(ObjectId _id, AccountRepository accountRepository, CommentRepository commentRepository) throws NullPointerException, IllegalStateException {
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
        com.swd.db.relationships.entities.Comment comment_rel = commentRepository.findByHexId(this._id);
        com.swd.db.relationships.entities.Account owner_rel = accountRepository.findOwnerByComment(comment_rel);
        AccountSummViewModel owner = new AccountSummViewModel(new ObjectId(owner_rel.getHex_string_id()));
        this.posted_by = owner;
    }
}
