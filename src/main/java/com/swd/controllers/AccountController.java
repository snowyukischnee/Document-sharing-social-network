package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.security.CustomUserDetails;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/not_logged_in", method = RequestMethod.GET)
    @ResponseBody
    public String not_logged_in() {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        result.put("Status", "ERROR");
        result.put("Message", "Not logged in");
        return gson.toJson(result);
    }

    @RequestMapping(value = "/user_info", method = RequestMethod.GET)
    @ResponseBody
    public String user_info() {
        Gson gson = new Gson();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return gson.toJson(userDetails);
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    @ResponseBody
    public String changepass(@RequestParam("password") String pssword) {
        MongoDaoBaseClass<com.swd.db.documents.entities.Account> accdao = new MongoDaoBaseClass<>("account");
        String HashedPassword = passwordEncoder.encode(pssword);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.swd.db.documents.entities.Account acc_orig = new com.swd.db.documents.entities.Account(
                userDetails.get_id(),
                null,
                null,
                null,
                null,
                userDetails.isEnabled(),
                null,
                null,
                false);
        Document doc = accdao.Find(acc_orig);
        com.swd.db.documents.entities.Account acc_dest = new com.swd.db.documents.entities.Account(
                doc.getObjectId("_id"),
                doc.getString("email"),
                doc.getString("password"),
                (List<String>)doc.get("roles"),
                doc.getDate("dateCreated"),
                doc.getBoolean("enabled"),
                doc.getString("name"),
                doc.getDate("dob"),
                doc.getBoolean("gender")
        );
        acc_dest.setPassword(HashedPassword);
        accdao.Update(acc_orig, acc_dest);
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Password changed successfully");
        return gson.toJson(result);
    }

}
