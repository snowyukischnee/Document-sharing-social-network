package com.swd.controllers;

import com.google.gson.Gson;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class AdminController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("email") String email, @RequestParam("password") String pssword) {
        MongoDaoBaseClass<com.swd.db.documents.entities.Account> accdao = new MongoDaoBaseClass<>("account");
        String HashedPassword = passwordEncoder.encode(pssword);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        ObjectId _id = new ObjectId();
        com.swd.db.documents.entities.Account acc = new com.swd.db.documents.entities.Account(
                _id,
                email,
                HashedPassword,
                roles,
                new Date(),
                true,
                null,
                null,
                false
        );
        accdao.Insert(acc);
        com.swd.db.relationships.entities.Account acc_rel = new com.swd.db.relationships.entities.Account();
        acc_rel.setHex_string_id(_id.toHexString());
        accountRepository.save(acc_rel);
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Account created successfully");
        return gson.toJson(result);
    }
}
