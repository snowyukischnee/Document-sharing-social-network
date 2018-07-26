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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AdminController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(
            @RequestParam("email") String email,
            @RequestParam("password") String pssword,
            @RequestParam("name") String name,
            @RequestParam("dob") String dob,
            @RequestParam("gender") Boolean gender
    ) {
        MongoDaoBaseClass<com.swd.db.documents.entities.Account> accdao = new MongoDaoBaseClass<>("account");
        String HashedPassword = passwordEncoder.encode(pssword);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        ObjectId _id = new ObjectId();
        DateFormat jsfmt = new SimpleDateFormat("EE MMM d y");
        Date _dob = null;
        try {
            _dob = jsfmt.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
            Gson gson = new Gson();
            Map<String, String> result = new HashMap<>();
            result.put("Status", "ERROR");
            result.put("Message", "Can not parse DoB");
            accdao.close();
            return gson.toJson(result);
        }
        com.swd.db.documents.entities.Account acc = new com.swd.db.documents.entities.Account(
                _id,
                email,
                HashedPassword,
                roles,
                new Date(),
                true,
                name,
                _dob,
                gender
        );
        accdao.Insert(acc);
        com.swd.db.relationships.entities.Account acc_rel = new com.swd.db.relationships.entities.Account();
        acc_rel.setHex_string_id(_id.toHexString());
        accountRepository.save(acc_rel);
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        result.put("Message", "Account created successfully");
        accdao.close();
        return gson.toJson(result);
    }
}
