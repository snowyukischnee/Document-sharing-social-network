package com.demo.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageService implements StorageImpl {

    public final static String BaseDir = "uploads";


    @Override
    public void MkDirIfnExist(String path) {
        if(!new File(path).exists()) {
            new File(path).mkdir();
        }
    }

    @Override
    public void Save(MultipartFile file, String path, String uid) throws IOException {
        if (file.isEmpty()) throw new IOException("File is invalid");
        MkDirIfnExist(path);
        path = path.concat(BaseDir + "/");
        MkDirIfnExist(path);
        path = path.concat(uid + "/");
        MkDirIfnExist(path);
        String orgName = file.getOriginalFilename();
        String filePath = path + orgName;
        file.transferTo(new File(filePath));
    }

    @Override
    public List<File> ListFiles(String path, String uid) {
        List<File> arr = new ArrayList<File>();
        path = path.concat(BaseDir + "/");
        path = path.concat(uid + "/");
        File file = new File(path);
        if(!file.exists()) {
            return arr;
        }
        File[] arrf = file.listFiles();
        for (File item : arrf) arr.add(item);
        return arr;
    }

    @Override
    public FileInputStream GetStream(String path, String uid, String filename) throws FileNotFoundException {
        path = path.concat(BaseDir + "/");
        path = path.concat(uid + "/");
        path = path.concat(filename);
        File file = new File(path);
        if(!file.exists()) {
            return null;
        }
        return new FileInputStream(file);
    }

    @Override
    public File GetFile(String path, String uid, String filename) {
        path = path.concat(BaseDir + "/");
        path = path.concat(uid + "/");
        path = path.concat(filename);
        File file = new File(path);
        if(!file.exists()) {
            return null;
        }
        return file;
    }
}
