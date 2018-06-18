package com.demo;

import com.demo.db.Account;
import com.demo.db.AccountDAO;
import com.demo.db.DbImpl;
import com.demo.security.MongoUserDetails;
import com.demo.utilities.StorageImpl;
import com.demo.utilities.StorageService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("")
@Controller
public class Hello {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    public String heello(HttpServletRequest request, ModelMap model) {
        model.addAttribute("message", "heeeeelllllo");
        return "hello";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String usrname, @RequestParam("password") String pssword, HttpServletRequest request, ModelMap model) {
        DbImpl<Account> accdao = new AccountDAO();
        String HashedPassword = passwordEncoder.encode(pssword);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_MEMBER");
        roles.add("ROLE_USER");
        Account acc = new Account(usrname, HashedPassword, roles);
        accdao.Insert(acc);
        return "redirect:login";
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public String changepass(@RequestParam("password") String pssword, HttpServletRequest request, ModelMap model) {
        DbImpl<Account> accdao = new AccountDAO();
        String HashedPassword = passwordEncoder.encode(pssword);
        String usrname = ((MongoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account acc_orig = new Account(usrname, null, null);
        Document doc = accdao.Find(acc_orig);
        Account acc_dest = new Account(doc.getString("username"), doc.getString("password"), (List<String>)doc.get("roles"));
        acc_dest.setPassword(HashedPassword);
        accdao.Update(acc_orig, acc_dest);
        return "redirect:index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("files") MultipartFile file, HttpServletRequest request, ModelMap model) {
        DbImpl<Account> accdao = new AccountDAO();
        StorageImpl storageService = new StorageService();
        String usrname = ((MongoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account acc_orig = new Account(usrname, null, null);
        String uid = accdao.GetId(acc_orig);
        String path = request.getServletContext().getRealPath("/");
        try {
            storageService.Save(file, path, uid);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "redirect:index";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list_uploaded(HttpServletRequest request, ModelMap model) {
        DbImpl<Account> accdao = new AccountDAO();
        StorageImpl storageService = new StorageService();
        String usrname = ((MongoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account acc_orig = new Account(usrname, null, null);
        String uid = accdao.GetId(acc_orig);
        String path = request.getServletContext().getRealPath("/");
        List<File> arr = new ArrayList<>();
        arr = storageService.ListFiles(path, uid);
        model.addAttribute("files", arr);
        return "list";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("file") String filename, HttpServletRequest request, HttpServletResponse response) {
        DbImpl<Account> accdao = new AccountDAO();
        StorageImpl storageService = new StorageService();
        String usrname = ((MongoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account acc_orig = new Account(usrname, null, null);
        String uid = accdao.GetId(acc_orig);
        String path = request.getServletContext().getRealPath("/");
        try {
            File downloadFile = storageService.GetFile(path, uid, filename);
            FileInputStream inputStream = storageService.GetStream(path, uid, filename);
            response.setContentType("application/octet-stream");
            response.setContentLength((int)downloadFile.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getName()));
            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) outStream.write(buffer, 0, bytesRead);
            inputStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
