package com.swd.viewmodels;

public class FileViewModel {
    public String fileName;
    public long length;

    public FileViewModel() { }

    public FileViewModel(String fileName, long length) {
        this.fileName = fileName;
        this.length = length;
    }
}
