package com.swd.controllers;

import com.swd.entities.Account;
import com.swd.entities.AccountBaseClass;
import com.swd.models.AccountDao;
import com.swd.models.DaoImpl;
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
    @RequestMapping(value = "/upload-{sub_folder}", method = RequestMethod.POST)
    public String upload(@PathVariable("sub_folder") String sub_folder, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        for(MultipartFile file : files) {
            try {
                storageService.Save(file, path, uid, sub_folder, file.getOriginalFilename());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "redirect:/index";
    }

    @RequestMapping(value = "/download-{sub_folder}", method = RequestMethod.GET)
    public void download(@PathVariable("sub_folder") String sub_folder, @RequestParam("file") String filename, HttpServletRequest request, HttpServletResponse response) {
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        try {
            File downloadFile = storageService.GetFile(path, uid, sub_folder, filename);
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

    @RequestMapping(value = "/list-{sub_folder}", method = RequestMethod.GET)
    public String list_uploaded(@PathVariable("sub_folder") String sub_folder, HttpServletRequest request, ModelMap model) {
        StorageImpl storageService = new StorageService();
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.get_id().toString();
        String path = request.getServletContext().getRealPath("/");
        List<File> arr = storageService.ListFiles(path, uid, sub_folder);
        model.addAttribute("files", arr);
        return "list";
    }
}
