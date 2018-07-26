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
    public String list_comments(@RequestParam("_id") String _pid) {
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
            commentdao.close();
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
        commentdao.close();
        return gson.toJson(result);
    }

    @RequestMapping(value = "/delete/comment", method = RequestMethod.POST)
    @ResponseBody
    public String delete_comment(@RequestParam("_id") String _pid) {
        Gson gson = new Gson();
        CommentViewModel commentViewModel = null;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            commentViewModel = new CommentViewModel(new ObjectId(_pid), accountRepository, commentRepository);
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Comment id is invalid");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        if (userDetails.get_id().toHexString().equalsIgnoreCase(commentViewModel.posted_by._id) == false) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Current user doesn't own this post");
            result.put("PostId", _pid);
            return gson.toJson(result);
        }
        MongoDaoBaseClass<com.swd.db.documents.entities.Comment> commentdao = new MongoDaoBaseClass<>("comment");
        com.swd.db.documents.entities.Comment comment_orig = new com.swd.db.documents.entities.Comment(
                new ObjectId(commentViewModel._id),
                commentViewModel.dateCreated,
                commentViewModel.content,
                true
        );
        com.swd.db.documents.entities.Comment comment_dest = new com.swd.db.documents.entities.Comment(
                new ObjectId(commentViewModel._id),
                commentViewModel.dateCreated,
                commentViewModel.content,
                false
        );
        commentdao.Update(comment_orig, comment_dest);
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Comment deleted");
        commentdao.close();
        return gson.toJson(result);
    }
}
