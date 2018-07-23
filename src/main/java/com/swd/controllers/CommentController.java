package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import com.swd.security.CustomUserDetails;
import com.swd.viewmodels.CommentViewModel;
import com.swd.viewmodels.PostSummViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class CommentController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/list/comments", method = RequestMethod.GET)
    @ResponseBody
    public String list_users(@RequestParam("_id") String _pid) {
        Gson gson = new Gson();
        try {
            PostSummViewModel postSummViewModel = new PostSummViewModel(new ObjectId(_pid), accountRepository, postRepository);
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Post id is invalid");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        if (post_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Post id is invalid");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        List<com.swd.db.relationships.entities.Comment> comment_list_rel = commentRepository.findCommentsByPost(post_rel);
        List<CommentViewModel> list = new ArrayList<>();
        for (com.swd.db.relationships.entities.Comment comment_rel : comment_list_rel) {
            try {
                list.add(new CommentViewModel(new ObjectId(comment_rel.getHex_string_id()), accountRepository, commentRepository));
            } catch (NullPointerException e) {
                e.printStackTrace();
                Map<String, String> result = new HashMap<>();
                result.put("Status", "ERROR");
                result.put("Message", "Can not list comment");
                result.put("PostId", _pid);
                return gson.toJson(result);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(list);
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public String comment(@RequestParam("_id") String _pid, @RequestParam("content") String content) {
        Gson gson = new Gson();
        try {
            PostSummViewModel postSummViewModel = new PostSummViewModel(new ObjectId(_pid), accountRepository, postRepository);
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Post id is invalid");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        MongoDaoBaseClass<com.swd.db.documents.entities.Comment> commentdao = new MongoDaoBaseClass<>("comment");
        com.swd.db.relationships.entities.Post post_rel = postRepository.findByHexId(_pid);
        if (post_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Post id is invalid");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId _id = new ObjectId();
        com.swd.db.documents.entities.Comment comment = new com.swd.db.documents.entities.Comment(
                _id,
                new Date(),
                content,
                true
        );
        commentdao.Insert(comment);
        com.swd.db.relationships.entities.Comment comment_rel = new com.swd.db.relationships.entities.Comment();
        comment_rel.setHex_string_id(_id.toHexString());
        com.swd.db.relationships.entities.Account account_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        commentRepository.save(comment_rel);
        accountRepository.CommentOn(account_rel, post_rel, comment_rel);
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Comment successfully");
        return gson.toJson(result);
    }
}
