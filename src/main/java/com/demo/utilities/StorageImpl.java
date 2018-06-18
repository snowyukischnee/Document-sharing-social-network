package com.demo.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageImpl {

    public void MkDirIfnExist(String path);
    public void Save(MultipartFile file, String path, String uid) throws IOException;
}
