package com.example.demo.web.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImportWordsRequest {

    private List<MultipartFile> fileList;

    public List<MultipartFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<MultipartFile> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "ImportWordsRequest{" +
                "fileList=" + fileList +
                '}';
    }
}
