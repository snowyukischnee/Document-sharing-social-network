package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.CommentRepository;
import com.swd.db.relationships.models.PostRepository;
import com.swd.security.CustomUserDetails;
import com.swd.viewmodels.AccountViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/user/is_friend", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (_uid == null || _uid == "") {
            _id1 = userDetails.get_id();
        } else {
            _id1 = new ObjectId(_uid);
        }
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend relationship status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriend(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/is_friend_request_sent", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend_request_sent(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (_uid == null || _uid == "") {
            _id1 = userDetails.get_id();
        } else {
            _id1 = new ObjectId(_uid);
        }
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend request sent status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriendRequestSent(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/is_friend_request_received", method = RequestMethod.POST)
    @ResponseBody
    public String is_friend_request_received(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (_uid == null || _uid == "") {
            _id1 = userDetails.get_id();
        } else {
            _id1 = new ObjectId(_uid);
        }
        _id0 = userDetails.get_id();
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Get friend request received status successfully");
        result.put("Result", String.valueOf(accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel)));
        result.put("UserId", _id1.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user/friend_list", method = RequestMethod.POST)
    @ResponseBody
    public String friend_list(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id;
        if (_uid == null || _uid == "") {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            _id = userDetails.get_id();
        } else {
            _id = new ObjectId(_uid);
        }
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(_id.toHexString());
        List<com.swd.db.relationships.entities.Account> friend_list_rel = accountRepository.FindFriends(acc_rel);
        List<AccountViewModel> friend_list = new ArrayList<>();
        for (com.swd.db.relationships.entities.Account friend_list_r : friend_list_rel) {
            try {
                friend_list.add(new AccountViewModel(new ObjectId(friend_list_r.getHex_string_id())));
            } catch (Exception e) {
                Map<String, String> result = new HashMap<>();
                result.put("Status", "ERROR");
                result.put("Message", "Could not get friend list");
                result.put("UserId", _id.toHexString());
                return gson.toJson(result);
            }
        }
        return gson.toJson(friend_list);
    }

    @RequestMapping(value = "/user/friend_request_list", method = RequestMethod.POST)
    @ResponseBody
    public String friend_request_list(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id;
        if (_uid == null || _uid == "") {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            _id = userDetails.get_id();
        } else {
            _id = new ObjectId(_uid);
        }
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(_id.toHexString());
        List<com.swd.db.relationships.entities.Account> friend_request_list_rel = accountRepository.FindFriendRequest(acc_rel);
        List<AccountViewModel> friend_request_list = new ArrayList<>();
        try {
            for (com.swd.db.relationships.entities.Account friend_request_list_r : friend_request_list_rel) friend_request_list.add(new AccountViewModel(new ObjectId(friend_request_list_r.getHex_string_id())));
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Could not get friend request list");
            result.put("UserId", _id.toHexString());
            return gson.toJson(result);
        }
        return gson.toJson(friend_request_list);
    }

    @RequestMapping(value = "/user/friend_requested_by_list", method = RequestMethod.POST)
    @ResponseBody
    public String friend_requested_by_list(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id;
        if (_uid == null || _uid == "") {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            _id = userDetails.get_id();
        } else {
            _id = new ObjectId(_uid);
        }
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(_id.toHexString());
        List<com.swd.db.relationships.entities.Account> friend_requested_by_list_rel = accountRepository.FindFriendRequestedBy(acc_rel);
        List<AccountViewModel> friend_requested_by_list = new ArrayList<>();
        try {
            for (com.swd.db.relationships.entities.Account friend_requested_by_list_r : friend_requested_by_list_rel) friend_requested_by_list.add(new AccountViewModel(new ObjectId(friend_requested_by_list_r.getHex_string_id())));
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Could not get friend requested by list");
            result.put("UserId", _id.toHexString());
            return gson.toJson(result);
        }
        return gson.toJson(friend_requested_by_list);
    }

    @RequestMapping(value = "/user/friend_request_process", method = RequestMethod.POST)
    @ResponseBody
    public String friend_request_process(@RequestParam(name = "_id", required = false) String _uid) {
        Gson gson = new Gson();
        ObjectId _id0, _id1;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (_uid == null || _uid == "") {
            _id1 = userDetails.get_id();
        } else {
            _id1 = new ObjectId(_uid);
        }
        _id0 = userDetails.get_id();
        if (_id0.toHexString().equalsIgnoreCase(_id1.toHexString())) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Can not send friend request to yourself");
            return gson.toJson(result);
        }
        com.swd.db.relationships.entities.Account acc0_rel = accountRepository.findByHexId(_id0.toHexString());
        com.swd.db.relationships.entities.Account acc1_rel = accountRepository.findByHexId(_id1.toHexString());
        if (accountRepository.isFriend(acc0_rel, acc1_rel)) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "You are already friend");
            return gson.toJson(result);
        } else if (accountRepository.isFriendRequestReceived(acc0_rel, acc1_rel)) {
            accountRepository.CreateFriendRelationship(acc0_rel, acc1_rel);
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

    @RequestMapping(value = "/user/posted_posts", method = RequestMethod.POST)
    @ResponseBody
    public String posted_posts(@RequestParam(name = "_id", required = false) String _uid) {
        
        return "";
    }

    @RequestMapping(value = "/user/followed_posts", method = RequestMethod.POST)
    @ResponseBody
    public String followed_posts(@RequestParam(name = "_id", required = false) String _uid) {
        return "";
    }

    @RequestMapping(value = "/user/author_of_posts", method = RequestMethod.POST)
    @ResponseBody
    public String author_of_posts(@RequestParam(name = "_id", required = false) String _uid) {
        return "";
    }
}
