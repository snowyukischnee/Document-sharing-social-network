package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import com.swd.security.CustomUserDetails;
import com.swd.viewmodels.AccountSummViewModel;
import com.swd.viewmodels.AccountViewModel;
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
public class UserController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/user/unfriend", method = RequestMethod.POST)
    @ResponseBody
    public String unfriend(@RequestParam("_id") String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        _id1 = new ObjectId(_uid);
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (acc0_rel == null || acc1_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "User not found");
            return gson.toJson(result);
        }
        if (!accountRepository.isFriend(acc0_rel, acc1_rel)) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "OK");
            result.put("Message", "User is not current user's friend");
            result.put("UserId", _id1.toHexString());
            return gson.toJson(result);
        }
        accountRepository.DeleteFriendRelationship(acc0_rel, acc1_rel);
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Unfriend successfully");
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/is_friend", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend(@RequestParam("_id") String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        _id1 = new ObjectId(_uid);
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (acc0_rel == null || acc1_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "User not found");
            return gson.toJson(result);
        }
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend relationship status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriend(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/is_friend_request_sent", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend_request_sent(@RequestParam("_id") String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        _id1 = new ObjectId(_uid);
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (acc0_rel == null || acc1_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "User not found");
            return gson.toJson(result);
        }
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend request sent status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriendRequestSent(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/is_friend_request_received", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend_request_received(@RequestParam("_id") String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        _id1 = new ObjectId(_uid);
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (acc0_rel == null || acc1_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "User not found");
            return gson.toJson(result);
        }
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend request received status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/friend_request_process", method = RequestMethod.POST)
    @ResponseBody
    public String friend_request_process(@RequestParam("_id") String _uid, @RequestParam(name = "accept", required = false, defaultValue = "false") boolean accept) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        _id1 = new ObjectId(_uid);
        _id0 = userDetails.get_id();
        if (_id0.toHexString().equalsIgnoreCase(_id1.toHexString())) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Can not send friend request to yourself");
            return gson.toJson(result);
        }
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (acc0_rel == null || acc1_rel == null) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "User not found");
            return gson.toJson(result);
        }
        if (accountRepository.isFriend(acc0_rel, acc1_rel)) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "You are already friend");
            return gson.toJson(result);
        } else if (accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel)) {
            accountRepository.DeleteFriendRequest(acc1_rel, acc0_rel);
            if (accept) accountRepository.CreateFriendRelationship(acc0_rel, acc1_rel);
        } else if (!accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel) && !accountRepository.isFriendRequestSent(acc0_rel, acc1_rel)) {
            accountRepository.SendFriendRequest(acc0_rel, acc1_rel);
        } else if (!accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel) && accountRepository.isFriendRequestSent(acc0_rel, acc1_rel)) {
            accountRepository.DeleteFriendRequest(acc0_rel, acc1_rel);
        }
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Friend request process successfully");
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    public String get_user(@PathVariable("user_id") String user_id) {
        Gson gson = new Gson();
        AccountViewModel accountViewModel = null;
        try {
            accountViewModel = new AccountViewModel(new ObjectId(user_id), accountRepository, postRepository);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> result = new HashMap<>();
            result.put("Status", "OK");
            result.put("Message", "Can not get user data");
            result.put("UserId", user_id);
            return gson.toJson(result);
        }
        return gson.toJson(accountViewModel);
    }
}
