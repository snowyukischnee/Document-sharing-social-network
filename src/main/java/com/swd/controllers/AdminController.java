package com.swd.controllers;

import com.swd.entities.Account;
import com.swd.models.DaoBaseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "admin";
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String usrname, @RequestParam("password") String pssword) {
        DaoBaseClass<Account> accdao = new DaoBaseClass<Account>("account");
        String HashedPassword = passwordEncoder.encode(pssword);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        Account acc = new Account(
                null,
                usrname,
                HashedPassword,
                roles,
                new Date(),
                true,
                null,
                null,
                null,
                false
        );
        accdao.Insert(acc);
        return "redirect:/admin";
    }
}
