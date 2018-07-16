package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import com.swd.security.CustomUserDetails;
import com.swd.viewmodels.AccountViewModel;
import com.swd.viewmodels.PostViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET)
    @ResponseBody
    public String get_post(@PathVariable("post_id") String post_id) {
        Gson gson = new Gson();
        PostViewModel post;
        try {
            post = new PostViewModel(new ObjectId(post_id));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Could not get post data");
            result.put("PostId", post_id);
            return gson.toJson(result);
        }
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(post_id);
        com.swd.db.relationships.entities.Account owner_rel = accountRepository.findOwnerByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> authors_rel = accountRepository.findAuthorsByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> liked_by_rel = accountRepository.findReactedByPost(post_rel);
        List<com.swd.db.relationships.entities.Account> followed_by_rel = accountRepository.findFollowedByPost(post_rel);
        AccountViewModel owner = null;
        List<AccountViewModel> authors = new ArrayList<>();
        List<AccountViewModel> liked_by = new ArrayList<>();
        List<AccountViewModel> followed_by = new ArrayList<>();
        try {
            owner = new AccountViewModel(new ObjectId(owner_rel.getHex_string_id()));
            for (com.swd.db.relationships.entities.Account author_rel : authors_rel) authors.add(new AccountViewModel(new ObjectId(author_rel.getHex_string_id())));
            for (com.swd.db.relationships.entities.Account liked_by_r : liked_by_rel) liked_by.add(new AccountViewModel(new ObjectId(liked_by_r.getHex_string_id())));
            for (com.swd.db.relationships.entities.Account followed_by_r : followed_by_rel) followed_by.add(new AccountViewModel(new ObjectId(followed_by_r.getHex_string_id())));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Could not get post data");
            result.put("PostId", post_id);
            return gson.toJson(result);
        }
        post.posted_by = owner;
        post.authors = authors;
        post.liked_by = liked_by;
        post.followed_by = followed_by;
        return gson.toJson(post);
    }

    @RequestMapping(value = "/post/react", method = RequestMethod.POST)
    @ResponseBody
    public String react_post(@RequestParam("_id") String _pid, @RequestParam("type") Boolean react_type) {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        accountRepository.DeleteReactPost(acc_rel, post_rel);
        if (react_type == true) {
            accountRepository.ReactPost(acc_rel, post_rel);
        }
        result.put("Status", "OK");
        result.put("Message", "React/Ignore successfully");
        return gson.toJson(result);
    }

    @RequestMapping(value = "/post/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow_post(@RequestParam("_id") String _pid, @RequestParam("type") Boolean react_type) {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        accountRepository.UnfollowPost(acc_rel, post_rel);
        if (react_type == true) {
            accountRepository.FollowPost(acc_rel, post_rel);
        }
        result.put("Status", "OK");
        result.put("Message", "Follow/Unfollow successfully");
        return gson.toJson(result);
    }

    @RequestMapping(value = "/post/is_reacted", method = RequestMethod.POST)
    @ResponseBody
    public String is_reacted(@RequestParam("_id") String _pid) {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        result.put("Status", "OK");
        result.put("Message", "Get reacted status successfully");
        result.put("PostId", _pid);
        result.put("Result", String.valueOf(postRepository.isReacted(acc_rel, post_rel)));
        return gson.toJson(result);
    }

    @RequestMapping(value = "/post/is_followed", method = RequestMethod.POST)
    @ResponseBody
    public String is_followed(@RequestParam("_id") String _pid) {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        result.put("Status", "OK");
        result.put("Message", "Get follow status successfully");
        result.put("PostId", _pid);
        result.put("Result", String.valueOf(postRepository.isFolowed(acc_rel, post_rel)));
        return gson.toJson(result);
    }
}
