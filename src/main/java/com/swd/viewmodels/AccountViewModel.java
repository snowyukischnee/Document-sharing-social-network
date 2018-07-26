package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.PostRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class AccountViewModel {
    public String _id;
    public String name;
    public String email;
    public Date dob;
    public boolean gender;
    public List<AccountSummViewModel> friend_list;
    public List<AccountSummViewModel> friend_requested_by_list;
    public List<PostSummViewModel> posted_posts;
    public List<PostSummViewModel> followed_posts;
    public List<PostSummViewModel> author_of_posts;

    public AccountViewModel(ObjectId _id, AccountRepository accountRepository, PostRepository postRepository) throws NullPointerException, IllegalStateException {
        MongoDaoBaseClass<com.swd.db.documents.entities.Account> accdao = new MongoDaoBaseClass<>("account");
        Document doc = accdao.Find(new com.swd.db.documents.entities.Account(
                _id,
                null,
                null,
                null,
                null,
                true,
                null,
                null,
                false)
        );
        if (doc == null) throw new NullPointerException();
        this._id = doc.getObjectId("_id").toHexString();
        this.name = doc.getString("name");
        this.email = doc.getString("email");
        this.dob = doc.getDate("dob");
        this.gender = doc.getBoolean("gender");
        List<AccountSummViewModel> friend_list_x = new ArrayList<>();
        List<AccountSummViewModel> friend_requested_by_list_x = new ArrayList<>();
        List<PostSummViewModel> posted_posts_x = new ArrayList<>();
        List<PostSummViewModel> followed_posts_x = new ArrayList<>();
        List<PostSummViewModel> author_of_posts_x = new ArrayList<>();
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(_id.toHexString());
        List<com.swd.db.relationships.entities.Account> friend_list_rel = accountRepository.FindFriends(acc_rel);
        List<com.swd.db.relationships.entities.Account> friend_requested_by_list_rel = accountRepository.FindFriendRequestedBy(acc_rel);
        List<com.swd.db.relationships.entities.Post> posted_posts_rel = postRepository.findPostsByOwner(acc_rel);
        List<com.swd.db.relationships.entities.Post> followed_posts_rel = postRepository.findPostsByFollowed(acc_rel);
        List<com.swd.db.relationships.entities.Post> author_of_posts_rel = postRepository.findPostsByAuthor(acc_rel);
        for (com.swd.db.relationships.entities.Account friend_list_r : friend_list_rel) friend_list_x.add(new AccountSummViewModel(new ObjectId(friend_list_r.getHex_string_id())));
        for (com.swd.db.relationships.entities.Account friend_requested_by_list_r : friend_requested_by_list_rel) friend_requested_by_list_x.add(new AccountSummViewModel(new ObjectId(friend_requested_by_list_r.getHex_string_id())));
        for (com.swd.db.relationships.entities.Post posted_posts_r : posted_posts_rel) posted_posts_x.add(new PostSummViewModel(new ObjectId(posted_posts_r.getHex_string_id()), accountRepository, postRepository));
        for (com.swd.db.relationships.entities.Post followed_posts_r : followed_posts_rel) followed_posts_x.add(new PostSummViewModel(new ObjectId(followed_posts_r.getHex_string_id()),accountRepository, postRepository));
        for (com.swd.db.relationships.entities.Post author_of_posts_r : author_of_posts_rel) author_of_posts_x.add(new PostSummViewModel(new ObjectId(author_of_posts_r.getHex_string_id()), accountRepository, postRepository));
        this.friend_list = friend_list_x;
        this.posted_posts = posted_posts_x;
        this.followed_posts = followed_posts_x;
        this.author_of_posts = author_of_posts_x;
        this.friend_requested_by_list = friend_requested_by_list_x;
        accdao.close();
    }
}
