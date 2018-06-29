package com.swd.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageImpl {
    public void MkDirIfnExist(String path);
    public void Save(MultipartFile file, String path, String folder, String sub_folder, String file_name) throws IOException;
    public List<File> ListFiles(String path, String folder, String sub_folder);
    public File GetFile(String path, String folder, String sub_folder, String filename);
}
