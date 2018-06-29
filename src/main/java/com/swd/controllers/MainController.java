package com.swd.controllers;

import com.swd.db.relationships.entities.AccountRelationship;
import com.swd.db.relationships.models.AccountRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    AccountRelationshipRepository accountRelationshipRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        AccountRelationship a = new AccountRelationship();
        //accountRelationshipRepository.CreateFriendRelationship("123456", "123457");
        //List<AccountRelationship> arr = accountRelationshipRepository.GetFriendsByHexStringId("123456");
        //System.out.println(arr);
        //model.put("arr", arr);
        return "index";
    }
}
