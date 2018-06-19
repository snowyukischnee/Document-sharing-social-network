package com.swd.utilities;

import com.swd.config.Config;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StorageService implements StorageImpl {

    @Override
    public void MkDirIfnExist(String path) {
        if(!new File(path).exists()) {
            new File(path).mkdir();
        }
    }

    @Override
    public void Save(MultipartFile file, String path, String uid, String sub_folder, String file_name) throws IOException {
        if (file.isEmpty()) throw new IOException("File is invalid");
        MkDirIfnExist(path);
        path = path.concat(Config.BaseUploadDir + "/");
        MkDirIfnExist(path);
        path = path.concat(uid + "/");
        MkDirIfnExist(path);
        path = path.concat(sub_folder + "/");
        MkDirIfnExist(path);
        String filePath = path + file_name;
        file.transferTo(new File(filePath));
    }

    @Override
    public List<File> ListFiles(String path, String uid) {
        return null;
    }

    @Override
    public File GetFile(String path, String uid, String filename) {
        return null;
    }
}
