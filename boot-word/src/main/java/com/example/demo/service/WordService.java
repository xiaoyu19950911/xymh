package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WordService {
    void importWords(List<MultipartFile> documentIdList) throws Exception;

    void handler(List<String> documentIdList)  throws Exception;
}
