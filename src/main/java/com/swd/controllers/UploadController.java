package com.swd.controllers;

import com.swd.security.CustomUserDetails;
import com.swd.utilities.StorageImpl;
import com.swd.utilities.StorageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class UploadController {
    @RequestMapping(value = "/upload-{post_id}", method = RequestMethod.POST)
    public void upload(@PathVariable("post_id") String post_id, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        for(MultipartFile file : files) {
            try {
                storageService.Save(file, path, "posts", post_id, file.getOriginalFilename());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/download-{post_id}", method = RequestMethod.GET)
    public void download(@PathVariable("post_id") String post_id, @RequestParam("file") String filename, HttpServletRequest request, HttpServletResponse response) {
        StorageImpl storageService = new StorageService();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/list-{post_id}", method = RequestMethod.GET)
    public String list_uploaded(@PathVariable("post_id") String post_id, HttpServletRequest request, ModelMap model) {
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        List<File> arr = storageService.ListFiles(path, "posts", post_id);
        model.addAttribute("files", arr);
        return "list";
    }
}
