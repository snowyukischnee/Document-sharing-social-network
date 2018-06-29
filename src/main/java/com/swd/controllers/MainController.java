package com.swd.controllers;

import com.swd.db.relationships.entities.Account;
import com.swd.db.relationships.models.AccountRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    AccountRelationshipRepository accountRelationshipRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        /*Account a = new Account();
        a.setHex_string_id("123456");
        Account b = new Account();
        b.setHex_string_id("00000");
        accountRelationshipRepository.save(a);
        accountRelationshipRepository.save(b);
        accountRelationshipRepository.CreateFriendRelationship(a.getHex_string_id(), b.getHex_string_id());*/
        Optional<Account> aa = accountRelationshipRepository.findById((long) 28);
        Account a = aa.get();
        System.out.println(a.friends);
        //Account b = new Account();
        //b.setHex_string_id("2346");
        //accountRelationshipRepository.save(b);
        //accountRelationshipRepository.CreateFriendRelationship(a, b);
        //System.out.println(a.friends);
        return "index";
    }
}
