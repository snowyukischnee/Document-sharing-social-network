package com.swd.controllers;

import com.swd.entities.Account;
import com.swd.models.AccountDao;
import com.swd.models.DaoImpl;
import com.swd.security.CustomUserDetails;
import com.swd.utilities.StorageImpl;
import com.swd.utilities.StorageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        DaoImpl<Account> accdao = new AccountDao();
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        for(MultipartFile file : files) {
            try {
                storageService.Save(file, path, uid, "gg", file.getOriginalFilename());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "redirect:index";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("file") String filename, HttpServletRequest request, HttpServletResponse response) {
        DaoImpl<Account> accdao = new AccountDao();
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        try {
            File downloadFile = storageService.GetFile(path, uid, "gg", filename);
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list_uploaded(HttpServletRequest request, ModelMap model) {
        DaoImpl<Account> accdao = new AccountDao();
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        List<File> arr = storageService.ListFiles(path, uid, "gg");
        model.addAttribute("files", arr);
        return "list";
    }
}
