package com.demo.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class StorageService implements StorageImpl {

    @Override
    public void Save(MultipartFile file, String path) throws IOException {
        if (file.isEmpty()) throw new IOException("File is null");
        if(! new File(path).exists()) {
            new File(path).mkdir();
        }
        String orgName = file.getOriginalFilename();
        String filePath = path + orgName;
        file.transferTo(new File(filePath));
    }
}
