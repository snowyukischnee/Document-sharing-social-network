package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.documents.entities.Account;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.security.CustomUserDetails;
import com.swd.viewmodels.AccountViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
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
        try {
            for (com.swd.db.relationships.entities.Account friend_list_r : friend_list_rel) friend_list.add(new AccountViewModel(new ObjectId(friend_list_r.getHex_string_id())));
        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Could not get user data");
            return gson.toJson(result);
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
            result.put("Message", "Could not get user data");
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
            result.put("Message", "Could not get user data");
            return gson.toJson(result);
        }
        return gson.toJson(friend_requested_by_list);
    }
}
