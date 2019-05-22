package com.example.demo.web;

import com.example.demo.service.WordService;
import com.example.demo.web.request.ImportWordsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("word")
public class WordController {
    @Autowired
    WordService wordService;

    @PostMapping("/importWords")
    public String importWords(@RequestBody ImportWordsRequest request) throws Exception {
        wordService.importWords(request.getDocumentIdList());
        return "success!";
    }

}
