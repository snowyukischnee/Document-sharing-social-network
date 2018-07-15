package com.swd.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.AccountRepository;
import com.swd.db.relationships.models.PostRepository;
import com.swd.security.CustomUserDetails;
import com.swd.utilities.StorageImpl;
import com.swd.utilities.StorageService;
import com.swd.viewmodels.FileViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UploadController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("data") String data, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        MongoDaoBaseClass<com.swd.db.documents.entities.Post> postdao = new MongoDaoBaseClass<>("post");
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DateFormat jsfmt = new SimpleDateFormat("EE MMM d y");
        Map<String, String> data_map = gson.fromJson(data, new TypeToken<Map<String, String>>(){}.getType());
        System.out.println(data_map.get("title"));
        System.out.println(data_map.get("publication_date"));
        System.out.println(data_map.get("description"));
        //--------------------------------------------------------------------------------------------------------------
        String title = data_map.get("title");
        Date publicationDate = null;
        try {
            publicationDate = jsfmt.parse(data_map.get("publication_date"));
        } catch (ParseException e) {
            e.printStackTrace();
            result.put("Status", "ERROR");
            result.put("Message", "Could not parse input");
            return gson.toJson(result);
        }
        String description = data_map.get("description");
        ObjectId _id = new ObjectId();
        com.swd.db.documents.entities.Post post = new com.swd.db.documents.entities.Post(_id, title, description, publicationDate, new Date(), true);
        postdao.Insert(post);
        //--------------------------------------------------------------------------------------------------------------
        com.swd.db.relationships.entities.Post post_rel = new com.swd.db.relationships.entities.Post();
        post_rel.setHex_string_id(_id.toHexString());
        postRepository.save(post_rel);
        com.swd.db.relationships.entities.Account acc_rel = accountRepository.findByHexId(userDetails.get_id().toHexString());
        accountRepository.Post(acc_rel, post_rel);
        //--------------------------------------------------------------------------------------------------------------
        StorageImpl storageService = new StorageService();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        for(MultipartFile file : files) {
            try {
                storageService.Save(file, path, "posts", _id.toHexString(), file.getOriginalFilename());
            } catch (IOException ex) {
                ex.printStackTrace();
                result.put("Status", "ERROR");
                result.put("Message", "Could not upload files");
                result.put("PostId", _id.toHexString());
                return gson.toJson(result);
            }
        }
        result.put("Status", "OK");
        result.put("Message", "Upload successfully");
        result.put("PostId", _id.toHexString());
        return gson.toJson(result);
    }

    @RequestMapping(value = "/download/{post_id}", method = RequestMethod.GET)
    @ResponseBody
    public String download(@PathVariable("post_id") String post_id, @RequestParam("file") String filename, HttpServletRequest request, HttpServletResponse response) {
        StorageImpl storageService = new StorageService();
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        try {
            File downloadFile = storageService.GetFile(path, "posts", post_id, filename);
            FileInputStream inputStream = new FileInputStream(downloadFile);
            response.setContentType("application/octet-stream");
            response.setContentLength((int)downloadFile.length());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getName()));
            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) outStream.write(buffer, 0, bytesRead);
            inputStream.close();
            outStream.close();
            result.put("Status", "OK");
            result.put("Message", "Download files not error");
            result.put("PostId", post_id);
            return gson.toJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("Status", "ERROR");
            result.put("Message", "Could not download files");
            result.put("PostId", post_id);
            return gson.toJson(result);
        }
    }

    @RequestMapping(value = "/list/{post_id}", method = RequestMethod.GET)
    @ResponseBody
    public String list_uploaded(@PathVariable("post_id") String post_id, HttpServletRequest request) {
        Gson gson = new Gson();
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        List<File> arr = storageService.ListFiles(path, "posts", post_id);
        ArrayList<FileViewModel> result = new ArrayList<>();
        for (File file : arr) result.add(new FileViewModel(file.getName(), file.length()));
        System.out.println(result);
        return gson.toJson(result);
    }
}
