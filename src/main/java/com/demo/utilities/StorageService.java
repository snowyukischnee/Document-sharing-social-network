package com.demo.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class StorageService implements StorageImpl {

    public final static String BaseDir = "uploads";


    @Override
    public void MkDirIfnExist(String path) {
        if(! new File(path).exists()) {
            new File(path).mkdir();
        }
    }

    @Override
    public void Save(MultipartFile file, String path, String uid) throws IOException {
        if (file.isEmpty()) throw new IOException("File is null");
        MkDirIfnExist(path);
        path = path.concat(BaseDir + "/");
        MkDirIfnExist(path);
        path = path.concat(uid + "/");
        MkDirIfnExist(path);
        String orgName = file.getOriginalFilename();
        String filePath = path + orgName;
        file.transferTo(new File(filePath));
    }
}
