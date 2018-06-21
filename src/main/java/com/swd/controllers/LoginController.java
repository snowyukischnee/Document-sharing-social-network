package com.swd.controllers;

import com.swd.entities.Account;
import com.swd.entities.AccountBaseClass;
import com.swd.models.AccountDao;
import com.swd.models.DaoImpl;
import com.swd.security.CustomUserDetails;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String usrname, @RequestParam("password") String pssword) {
        DaoImpl<AccountBaseClass> accdao = new AccountDao();
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
        return "redirect:login";
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public String changepass(@RequestParam("password") String pssword) {
        DaoImpl<AccountBaseClass> accdao = new AccountDao();
        String HashedPassword = passwordEncoder.encode(pssword);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account acc_orig = new Account(
                userDetails.get_id(),
                null,
                null,
                null,
                null,
                userDetails.isEnabled(),
                null,
                null,
                null,
                false);
        Document doc = accdao.Find(acc_orig);
        Account acc_dest = new Account(
                doc.getObjectId("_id"),
                doc.getString("username"),
                doc.getString("password"),
                (List<String>)doc.get("roles"),
                doc.getDate("dateCreated"),
                doc.getBoolean("enabled"),
                doc.getString("name"),
                doc.getDate("dob"),
                doc.getString("email"),
                doc.getBoolean("gender")
        );
        acc_dest.setPassword(HashedPassword);
        accdao.Update(acc_orig, acc_dest);
        return "redirect:index";
    }

}
