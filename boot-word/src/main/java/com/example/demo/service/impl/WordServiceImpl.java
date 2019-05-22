package com.example.demo.service.impl;

import com.example.demo.service.WordService;
import com.example.demo.sync.WordAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    WordAsync wordAsync;

    @Override
    public void importWords(List<String> documentIdList) throws Exception {

        for (String documentId:documentIdList){
            wordAsync.handleWord(documentId);
        }
    }
}
