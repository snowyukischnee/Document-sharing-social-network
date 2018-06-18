package com.demo.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageImpl {
    public void Save(MultipartFile file, String username) throws IOException;
}
